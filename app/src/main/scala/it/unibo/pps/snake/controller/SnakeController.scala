package it.unibo.pps.snake.controller

import io.github.sodium.{Cell, CellSink, Stream}
import it.unibo.pps.snake.model.World.Boundary
import it.unibo.pps.snake.model.{Directions, Food, Snake}

import java.util.concurrent.{ExecutorService, Executors}
import scala.util.Random

case class SnakeController(
  directionInput: Cell[Directions.Direction],
  statusInput: Cell[Status.Status],
  speedInput: Cell[Int],
  boundary: Boundary
) {
  private val initSnake: Snake = Snake(Array(((boundary._2/2).round,(boundary._4/2).round)))

  // For comprehension food creation
  private val initFood: IndexedSeq[Food] = for {
    i <- 0 to 30
    position = (Random.between(boundary._1, boundary._2), Random.between(boundary._3, boundary._4))
    if !initSnake.body.contains(position)
    } yield Food.createHealthyFood(position)

  private val executor: ExecutorService = Executors.newSingleThreadExecutor()
  private val engine: Engine = Engine(statusInput, speedInput)

  def start(): Unit = executor.execute(engine)

  def snakeOutput(): Stream[Snake] = engine.ticker.accum(initSnake,
    (e: Event, s: Snake) => s.move(directionInput.sample()).getOrElse(s)).updates()

  def foodOutput(): CellSink[Array[Food]] = new CellSink[Array[Food]](initFood.toArray)

  def isKnotted: Stream[Snake] = snakeOutput().filter(s => s.isKnotted)



}
