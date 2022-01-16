package it.unibo.pps.snake.controller

import io.github.sodium.Cell
import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.World.{Boundary, Position}
import it.unibo.pps.snake.model.{Directions, Food, Snake}
import org.slf4j.LoggerFactory

import java.util.concurrent.{ExecutorService, Executors}

case class SnakeController(
  directionInput: Cell[Directions.Direction],
  boundary: Boundary,
  engine: Engine
) {
  private val logger = LoggerFactory.getLogger(SnakeController.getClass)

  private val initSnake: Snake = Snake(Array(((boundary._2/2).round,(boundary._4/2).round)))
  private val initFood: Array[Food] = Food.createRandomFoods(Array(),boundary,5)

  private val threadPoolSize: Int = Runtime.getRuntime.availableProcessors + 1
  private val executor: ExecutorService = Executors.newFixedThreadPool(threadPoolSize)

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
  def stop(): Unit = executor.shutdown()

  /**
   * The foods and the snake of the game: modified when the engine ticks.
   *
   * @return <code>Cell[(Snake,Array[Food])]</code>
   */
  def output: Cell[(Snake,Array[Food])] = engine.ticker
    .accum((initSnake, initFood), (event: Event, acc: (Snake, Array[Food])) => {
      val tmpD: Direction = directionInput.sample()
      val tmpS: Snake = acc._1.move(tmpD).bound(boundary)

      if(acc._2.map(f => f.position).exists(p => tmpS.isNearTo(p))){
        val s: Snake = acc._1.increase(tmpD)
        logger.debug("increased snake: new head {}",s.head)

        val alreadyPresentFoods: Array[Food] = acc._2.filter(elem => !s.isNearTo(elem.position))
        val positionsToBeExcluded: Array[Position] = alreadyPresentFoods.map(f => f.position).prependedAll(s.body)

        (s,Food.createRandomFoods(positionsToBeExcluded, boundary,1).appendedAll(alreadyPresentFoods))
      } else {
        logger.debug("moved snake in direction {}", tmpD)
        (tmpS, acc._2)
      }
  })

  /**
   * The foods of the game: modified when the snake eats a food.
   *
   * @return <code>Cell[Array[Food]]</code>
   */
  def foodOutput: Cell[Array[Food]] = output.map(sf => sf._2)
  
  /**
   * The score of the game: modified when the snake eats a food.
   *
   * @return <code>Cell[Score]</code>
   */
  def scoreOutput: Cell[Score] = foodOutput.updates().accum((initFood, 0), (fp: Array[Food], acc: (Array[Food],Score)) => {
    val foodDiff = acc._1.diff(fp)
    logger.debug(s"next: ${fp.mkString("Array(", ", ", ")")}")
    logger.debug(s"prev: ${acc._1.mkString("Array(", ", ", ")")}")
    logger.debug(s"food diff: ${foodDiff.mkString("Array(", ", ", ")")}")
    if(foodDiff.length > 0){
      val out = (fp,acc._2 + foodDiff.head.score)
      logger.debug(s"${foodDiff.head}")
      logger.debug(s"score output: ${out._2}")
      // TODO: fix algo
      out
    } else {
      (acc._1,acc._2)
    }
  }).map((fs: (_, Score)) => fs._2)

  /**
   * The snake of the game: modified when the snake is moved or increased.
   *
   * @return <code>Cell[Snake]</code>
   */
  def snakeOutput: Cell[Snake] = output.map(sf => sf._1)

  /**
   * The snake of the game: modified when the snake is knotted.
   *
   * @return <code>Cell[Boolean]</code>
   */
  def isKnottedOutput: Cell[Boolean] = output.map(o => {
    logger.debug(s"snake ${o._1} knotted status: ${o._1.isKnotted}")
    o._1.isKnotted
  })

  /**
   * The size of the snake: modified when the snake is moved or increased.
   *
   * @return <code>Cell[Int]</code>
   */
  def snakeSizeOutput(): Cell[Int] = output.map(s => s._1.body.length)
}
