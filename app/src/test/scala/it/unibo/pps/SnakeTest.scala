package it.unibo.pps

import it.unibo.pps.snake.model.{Directions, Snake}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SnakeTest extends AnyFunSpec with should.Matchers {
  describe("A snake [(1,1),(1,2),(1,1)]"){
    def snake = Snake(Array((1,1),(1,2),(1,1)))
    it("should be knotted") {
      assert(snake.isKnotted)
    }

    it("shouldn't be reversed"){
      assert(snake.reversed.isEmpty)
    }

    it("shouldn't be moved in any direction"){
      assert(snake.move(Directions.RIGHT).isEmpty)
      assert(snake.move(Directions.LEFT).isEmpty)
      assert(snake.move(Directions.UP).isEmpty)
      assert(snake.move(Directions.DOWN).isEmpty)
    }

  }
}
