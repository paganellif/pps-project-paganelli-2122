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

    it("shouldn't be increased"){
      assert(snake.increase((10,10)).isEmpty)
    }
  }

  describe("A snake [(1,1),(1,2),(1,3)]"){
    def snake = Snake(Array((1,1),(1,2),(1,3)))
    it("shouldn't be knotted") {
      assert(!snake.isKnotted)
    }

    it("should  be reversed"){
      assert(snake.reversed.isDefined)
      assert(snake.reversed.get.body sameElements Snake(Array((1,3),(1,2),(1,1))).body)
    }

    it("should be moved in the right direction"){
      assert(snake.move(Directions.RIGHT).isDefined)
      assert(snake.move(Directions.RIGHT).get.body sameElements Snake(Array((11,1),(1,1),(1,2))).body)
    }

    it("should be moved in the left direction"){
      assert(snake.move(Directions.LEFT).isDefined)
      assert(snake.move(Directions.LEFT).get.body sameElements Snake(Array((-9,1),(1,1),(1,2))).body)
    }

    it("should be moved in the up direction"){
      assert(snake.move(Directions.UP).isDefined)
      assert(snake.move(Directions.UP).get.body sameElements Snake(Array((1,-9),(1,1),(1,2))).body)
    }

    it("should be moved in the down direction"){
      assert(snake.move(Directions.DOWN).isDefined)
      assert(snake.move(Directions.DOWN).get.body sameElements Snake(Array((1,11),(1,1),(1,2))).body)
    }

    it("should be increased"){
      assert(snake.increase((10,10)).isDefined)
      assert(snake.increase((10,10)).get.body sameElements Snake(Array((10,10),(1,1),(1,2),(1,3))).body)
    }
  }
}
