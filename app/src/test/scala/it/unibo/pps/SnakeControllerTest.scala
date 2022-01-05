package it.unibo.pps

import io.github.sodium.{Cell, CellSink}
import it.unibo.pps.snake.controller.{SnakeController, Status}
import it.unibo.pps.snake.controller.Status.Status
import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.World.Boundary
import it.unibo.pps.snake.model.Directions
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SnakeControllerTest extends AnyFlatSpec with Matchers {
  // Mocked Inputs
  val mockedDirectionInput: CellSink[Direction] = new CellSink[Direction](Directions.RIGHT)
  val mockedStatusInput: CellSink[Status] = new CellSink[Status](Status.PAUSE)
  val mockedSpeedInput: CellSink[Int] = new CellSink[Int](1)
  val mockedBoundary: Boundary = (0,500,0,500)

  val snakeController: SnakeController = SnakeController(mockedDirectionInput, mockedStatusInput, mockedSpeedInput, mockedBoundary)

  "A snake controller when created" should "have status PAUSED" in {
      assert(snakeController.statusInput.sample() == Status.PAUSE)
  }

  it should "have right direction" in {
    assert(snakeController.directionInput.sample() == Directions.RIGHT)
  }

  it should "have speed '1'" in {
    assert(snakeController.speedInput.sample() == 1)
  }

  it should "initialize a snake with size 1, positioned in the middle of the playground" in {
    assert(snakeController.snakeSizeOutput().sample() == 1)
    assert(snakeController.snakeOutput.sample().head == ((mockedBoundary._2/2).round,(mockedBoundary._4/2).round))
  }

  it should "initialize the score to zero" in {
    assert(snakeController.scoreOutput.sample() == 0)
  }

  it should "should initialize a set of foods" in {
    assert(!snakeController.foodOutput.sample().isEmpty)
  }

  it should "not move the snake if started with status PAUSE" in {
    val initialSnake = snakeController.snakeOutput.sample()

    snakeController.start()

    assert(snakeController.statusInput.sample() == Status.PAUSE)
    assert(snakeController.snakeSizeOutput().sample() == initialSnake.body.length)
    assert(snakeController.snakeOutput.sample().head == initialSnake.head)
  }
}
