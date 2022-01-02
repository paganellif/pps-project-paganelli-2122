package it.unibo.pps.snake.controller

import io.github.sodium.{Cell, Stream}
import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.World.Boundary
import it.unibo.pps.snake.model.{Directions, Food, Snake}
import org.slf4j.LoggerFactory

import java.util.concurrent.{ExecutorService, Executors}

case class SnakeController(
  directionInput: Cell[Directions.Direction],
  statusInput: Cell[Status.Status],
  speedInput: Cell[Int],
  boundary: Boundary
) {
  private val logger = LoggerFactory.getLogger(SnakeController.getClass)

  private val initSnake: Snake = Snake(Array(((boundary._2/2).round,(boundary._4/2).round)))
  private val initFood: Array[Food] = Food.createRandomFoods(Array(),boundary,5)

  private val threadPoolSize: Int = Runtime.getRuntime.availableProcessors + 1
  private val executor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)
  private val engine: Engine = Engine(statusInput, speedInput)

  implicit class BoundedSnake(snake: Snake) {
    def bound(boundary: Boundary): Snake = {
      Snake(snake.body.map(pos => {
        var tmpPos = pos
        if (pos._1 > boundary._2) tmpPos = (boundary._1 + 10, tmpPos._2)
        else if (pos._1 <= boundary._1) tmpPos = (boundary._2 - 10, tmpPos._2)
        else tmpPos = (pos._1, tmpPos._2)

        if (pos._2 > boundary._4) tmpPos = (tmpPos._1, boundary._3 + 10)
        else if (pos._2 <= boundary._3) tmpPos = (tmpPos._1, boundary._4 - 10)
        else tmpPos = (tmpPos._1, pos._2)

        logger.debug(s"pre: ${pos} - post: ${tmpPos}")
        tmpPos
      }))
    }
  }

  def start(): Unit = executor.execute(engine)

  /**
   * The foods and the snake of the game: modified when the engine ticks.
   *
   * @return <code>Stream[(Snake,Array[Food])]</code>
   */
  def output: Stream[(Snake,Array[Food])] = engine.ticker
    .accum((initSnake, initFood), (event: Event, acc: (Snake, Array[Food])) => {
      val tmpD: Direction = directionInput.sample()
      val tmpS: Snake = acc._1.move(tmpD).getOrElse(acc._1).bound(boundary)

      if(acc._2.map(f => f.position).exists(p => tmpS.isNearTo(p))){
        logger.debug("increased snake: new head {}",tmpS.head)
        val s: Snake = acc._1.increase(tmpD).getOrElse(acc._1)

        val tmpF: Array[Food] = acc._2.filter(elem => !s.isNearTo(elem.position))
        val f: Array[Food] = tmpF.prependedAll(Food.createRandomFoods(
            tmpF.map(f => f.position).prependedAll(s.body), boundary, 1))
        (s,f)
      } else {
        logger.debug("moved snake in direction {}", tmpD)
        (tmpS, acc._2)
      }
  }).updates()

  /**
   * The foods of the game: modified when the snake eats a food.
   *
   * @return <code>Stream[Array[Food]]</code>
   */
  def foodOutput: Stream[Array[Food]] = output.map(sf => sf._2)

  /**
   * The score of the game: modified when the snake eats a food.
   *
   * @return <code>Stream[Score]</code>
   */
  def scoreOutput: Stream[Score] = foodOutput.accum((initFood, 0), (fp: Array[Food], acc: (Array[Food],Score)) => {
    val foodDiff = fp.diff(acc._1)
    logger.debug(s"previous: ${fp.mkString("Array(", ", ", ")")} - " +
      s"acc: ${acc._1.mkString("Array(", ", ", ")")} - " +
      s"food diff: ${foodDiff.mkString("Array(", ", ", ")")}")
    if(foodDiff.length > 0){
      val out = (fp,acc._2 + foodDiff.head.score)
      logger.debug(s"${foodDiff.head}")
      logger.debug(s"score output: ${out}")
      // TODO: fix algo
      out
    } else {
      (fp,acc._2)
    }
  }).updates().map((fs: (_, Score)) => fs._2)

  /**
   * The snake of the game: modified when the snake is moved or increased.
   *
   * @return <code>Stream[Snake]</code>
   */
  def snakeOutput: Stream[Snake] = output.map(sf => sf._1)

  /**
   * The snake of the game: modified when the snake is knotted.
   *
   * @return <code>Stream[Snake]</code>
   */
  def isKnottedOutput: Stream[Snake] = output.filter(s => s._1.isKnotted).map(o => o._1)

  /**
   * The size of the snake: modified when the snake is moved or increased.
   *
   * @return <code>Stream[Int]</code>
   */
  def snakeSizeOutput(): Stream[Int] = output.map(s => s._1.body.length)
}
