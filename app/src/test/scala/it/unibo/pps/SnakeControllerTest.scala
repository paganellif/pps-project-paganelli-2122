package it.unibo.pps

import io.github.sodium.CellSink
import it.unibo.pps.snake.controller.{Engine, SnakeController, Status}
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
  val mockedBoundary: Boundary = (0,500,0,500)
  val mockedEngine: Engine = Engine(new CellSink[Status](Status.PAUSE), new CellSink[Int](1))

  val snakeController: SnakeController = SnakeController(mockedDirectionInput, mockedBoundary, mockedEngine)

  "A snake controller when created" should "have right direction" in {
    assert(snakeController.directionInput.sample() == Directions.RIGHT)
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

  it should "not move the snake if not started" in {
    val initialSnake = snakeController.snakeOutput.sample()

    assert(snakeController.snakeSizeOutput().sample() == initialSnake.body.length)
    assert(snakeController.snakeOutput.sample().head == initialSnake.head)
  }
}
