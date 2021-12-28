package it.unibo.pps.snake.controller

import io.github.sodium.{Cell, Stream}
import it.unibo.pps.snake.model.{Directions, Snake}

import java.util.concurrent.{ExecutorService, Executors}

case class SnakeController(
  directionInput: Cell[Directions.Direction],
  statusInput: Cell[Status.Status],
  speedInput: Cell[Int],
  initSnake: Snake
) {

  private val executor: ExecutorService = Executors.newSingleThreadExecutor()
  private val engine: Engine = Engine(statusInput, speedInput)

  def start(): Unit = executor.execute(engine)

  def snakeOutput(): Stream[Snake] = engine.ticker.accum(initSnake, (e: Event, s: Snake) => Snake(s.move(directionInput.sample()))).updates()

  def isKnotted(): Stream[Snake] = snakeOutput().filter(s => s.isKnotted)


}
