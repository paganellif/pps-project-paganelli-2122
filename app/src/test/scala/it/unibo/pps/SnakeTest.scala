package it.unibo.pps

import it.unibo.pps.snake.model.{Directions, Snake}
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SnakeTest extends AnyFunSpec with should.Matchers {
  describe("A snake [(1,1),(1,2),(1,1)]") {
    def snake = Snake(Array((1, 1), (1, 2), (1, 1)))

    it("should be knotted") {
      assert(snake.isKnotted)
    }

    describe("A single body snake [(1,1)]") {
      def snake = Snake(Array((1, 1)))

      it("shouldn't be knotted") {
        assert(!snake.isKnotted)
      }

      it("should have the head equal to (1,1)") {
        assert(snake.head == (1, 1))
      }

      it("should be reversed") {
        assert(snake.reversed.body sameElements Snake(Array((1, 1))).body)
      }

      it("should be moved in the right direction") {
        assert(snake.move(Directions.RIGHT).body sameElements Snake(Array((11, 1))).body)
      }

      it("should be moved in the left direction") {
        assert(snake.move(Directions.LEFT).body sameElements Snake(Array((-9, 1))).body)
      }

      it("should be moved in the up direction") {
        assert(snake.move(Directions.UP).body sameElements Snake(Array((1, -9))).body)
      }

      it("should be moved in the down direction") {
        assert(snake.move(Directions.DOWN).body sameElements Snake(Array((1, 11))).body)
      }

      it("should be increased in the right direction") {
        assert(snake.increase(Directions.RIGHT).body sameElements Snake(Array((11, 1), (1, 1))).body)
      }

      it("should be increased in the left direction") {
        assert(snake.increase(Directions.LEFT).body sameElements Snake(Array((-9, 1), (1, 1))).body)
      }

      it("should be increased in the up direction") {
        assert(snake.increase(Directions.UP).body sameElements Snake(Array((1, -9), (1, 1))).body)
      }

      it("should be increased in the down direction") {
        assert(snake.increase(Directions.DOWN).body sameElements Snake(Array((1, 11), (1, 1))).body)
      }
    }

    describe("A multiple body snake [(10,10),(20,10)]") {
      def snake = Snake(Array((10, 10), (20, 10)))

      it("shouldn't be knotted") {
        assert(!snake.isKnotted)
      }

      it("should have the head equal to (10,10)") {
        assert(snake.head == (10, 10))
      }

      it("should be reversed") {
        assert(snake.reversed.body sameElements Snake(Array((20, 10), (10, 10))).body)
      }

      it("should be moved in the right direction") {
        assert(snake.move(Directions.RIGHT).body sameElements Snake(Array((20, 10), (10, 10))).body)
      }

      it("shouldn't be moved in the left direction") {
        assert(snake.move(Directions.LEFT).body sameElements Snake(Array((0, 10), (10, 10))).body)

      }

      it("should be moved in the up direction") {
        assert(snake.move(Directions.UP).body sameElements Snake(Array((10, 0), (10, 10))).body)
      }

      it("should be moved in the down direction") {
        assert(snake.move(Directions.DOWN).body sameElements Snake(Array((10, 20), (10, 10))).body)
      }

      it("should be increased in the left direction") {
        assert(snake.increase(Directions.LEFT).body sameElements Snake(Array((0, 10), (10, 10), (20, 10))).body)
      }

      it("should be increased in the up direction") {
        assert(snake.increase(Directions.UP).body sameElements Snake(Array((10, 0), (10, 10), (20, 10))).body)
      }

      it("should be increased in the down direction") {
        assert(snake.increase(Directions.DOWN).body sameElements Snake(Array((10, 20), (10, 10), (20, 10))).body)
      }
    }
  }
}
