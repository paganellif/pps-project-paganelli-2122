package it.unibo.pps

import io.github.sodium.CellSink
import it.unibo.pps.snake.controller.{Engine, Event, SnakeController, Status}
import it.unibo.pps.snake.controller.Status.Status
import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.World.Boundary
import it.unibo.pps.snake.model.{Directions, Snake}
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

  it should "move the snake in the right direction if started" in {
    val initialSnake = snakeController.snakeOutput.sample()

    // mocked start
    val snakeUpdate = snakeController.snakeOutput.updates().hold(Snake(Array((0,0))))
    mockedEngine.ticker.send(Event())

    // check if the snake is moved or increased
    assert(snakeUpdate.sample().body.length >= initialSnake.body.length)
    assert(snakeUpdate.sample().head != initialSnake.head)

    // check if the snake is moved in the right direction: (x,y) => (x+10,y)
    assert(snakeUpdate.sample().head._1 > initialSnake.head._1)
  }

  it should "move the snake in the up direction" in {
    val initialSnake = snakeController.snakeOutput.sample()

    // mocked start
    val snakeUpdate = snakeController.snakeOutput.updates().hold(Snake(Array((0,0))))
    mockedDirectionInput.send(Directions.UP)
    mockedEngine.ticker.send(Event())

    // check if the snake is moved or increased
    assert(snakeUpdate.sample().body.length >= initialSnake.body.length)
    assert(snakeUpdate.sample().head != initialSnake.head)

    // check if the snake is moved in the up direction: (x,y) => (x,y-10)
    assert(snakeUpdate.sample().head._2 < initialSnake.head._2)
  }

  it should "move the snake in the down direction if started" in {
    val initialSnake = snakeController.snakeOutput.sample()

    // mocked start
    val snakeUpdate = snakeController.snakeOutput.updates().hold(Snake(Array((0,0))))
    mockedDirectionInput.send(Directions.DOWN)
    mockedEngine.ticker.send(Event())

    // check if the snake is moved or increased
    assert(snakeUpdate.sample().body.length >= initialSnake.body.length)
    assert(snakeUpdate.sample().head != initialSnake.head)

    // check if the snake is moved in the down direction: (x,y) => (x,y+10)
    assert(snakeUpdate.sample().head._2 > initialSnake.head._2)
  }
}
