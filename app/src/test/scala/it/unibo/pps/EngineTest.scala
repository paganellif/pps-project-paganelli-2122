package it.unibo.pps

import io.github.sodium.CellSink
import it.unibo.pps.snake.controller.Status.Status
import it.unibo.pps.snake.controller.{Engine, Status}
import org.junit.runner.RunWith
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.junit.JUnitRunner


@RunWith(classOf[JUnitRunner])
class EngineTest extends AnyFlatSpec with Matchers {
  // Mocked Inputs
  val mockedStatusInput: CellSink[Status] = new CellSink[Status](Status.PAUSE)
  val mockedSpeedInput: CellSink[Int] = new CellSink[Int](1)

  val engine: Engine = Engine(mockedStatusInput, mockedSpeedInput)

  "A engine when created" should "have status PAUSED" in {
      assert(engine.statusInput.sample() == Status.PAUSE)
  }

  it should "have speed '1'" in {
    assert(engine.speedInput.sample() == 1)
  }
}
