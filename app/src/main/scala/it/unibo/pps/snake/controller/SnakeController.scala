package it.unibo.pps.snake.controller

import io.github.sodium.{Cell, CellSink, Stream}
import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.World.Boundary
import it.unibo.pps.snake.model.{Directions, Food, Snake}
import org.slf4j.LoggerFactory

import java.util.concurrent.{ExecutorService, Executors}
import scala.collection.mutable.ArrayBuffer

case class SnakeController(
  directionInput: Cell[Directions.Direction],
  statusInput: Cell[Status.Status],
  speedInput: Cell[Int],
  boundary: Boundary
) {
  private val logger = LoggerFactory.getLogger(SnakeController.getClass)

  /**
   *
   */
  private val initSnake: Snake = Snake(Array(((boundary._2/2).round,(boundary._4/2).round)))
  private val initFood: Array[Food] = Food.createRandomHealthyFoods(Array(),boundary,30)

  /**
   *
   */
  private val executor: ExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime.availableProcessors + 1)
  private val engine: Engine = Engine(statusInput, speedInput)

  /**
   *
   */
  def start(): Unit = executor.execute(engine)

  /**
   *
   * @return
   */
  def output: Stream[(Snake,Array[Food])] = engine.ticker
    .accum((initSnake, initFood), (event: Event, acc: (Snake, Array[Food])) => {
      val tmpD: Direction = directionInput.sample()
      val tmpS: Snake = acc._1.move(tmpD).getOrElse(acc._1)

      if(acc._2.map(f => f.position).exists(p => tmpS.isNearTo(p))){
        logger.debug("increased snake: new head {}",tmpS.head)
        val s: Snake = acc._1.increase(tmpD).getOrElse(acc._1)

        val tmpF: Array[Food] = acc._2.filter(elem => !s.isNearTo(elem.position))
        val f: Array[Food] = tmpF.prependedAll(Food.createRandomHealthyFoods(
            tmpF.map(f => f.position).prependedAll(s.body), boundary, 1))
        (s,f)
      } else {
        logger.debug("moved snake in direction {}", tmpD)
        (tmpS, acc._2)
      }
  }).updates()

  /**
   *
   * @return
   */
  def foodOutput: Stream[Array[Food]] = output.map(sf => sf._2)

  /**
   *
   * @return
   */
  def scoreOutput: Stream[Score] = foodOutput.accum((initFood, 0), (fp: Array[Food], acc: (Array[Food],Score)) => {
    val foodDiff = fp.diff(acc._1)
    if(foodDiff.length > 0){
      println((fp,acc._2 + foodDiff.head.score))
      // TODO: fix algo -> aumenta i punti una volta si e una no
      (fp,acc._2 + foodDiff.head.score)
    } else {
      (fp,acc._2)
    }
  }).updates().map((fs: (_, Score)) => fs._2)

  /**
   *
   * @return
   */
  def snakeOutput: Stream[Snake] = output.map(sf => sf._1)

  /**
   *
   * @return
   */
  def isKnotted: Stream[Snake] = output.filter(s => s._1.isKnotted).map(o => o._1)

  /**
   *
   * @return
   */
  def snakeSize(): Stream[Int] = output.map(s => s._1.body.length)

}
