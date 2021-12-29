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

    it("should be the same if reversed"){
      assert(snake.body sameElements snake.reversed.body)
    }

    it("should be equal to [(11,1),(1,1),(1,2)] if moved in the right direction"){
      assert(snake.move(Directions.RIGHT).body sameElements Snake(Array((11,1),(1,1),(1,2))).body)
    }

    it("should be equal to [(-9,1),(1,1),(1,2)] if moved in the left direction"){
      assert(snake.move(Directions.LEFT).body sameElements Snake(Array((-9,1),(1,1),(1,2))).body)
    }

    it("should be equal to [(1,-9),(1,1),(1,2)] if moved in the up direction"){
      assert(snake.move(Directions.UP).body sameElements Snake(Array((1,-9),(1,1),(1,2))).body)
    }

    it("should be equal to [(1,11),(1,1),(1,2)] if moved in the down direction"){
      assert(snake.move(Directions.DOWN).body sameElements Snake(Array((1,11),(1,1),(1,2))).body)
    }
  }
}
