package it.unibo.pps

import it.unibo.pps.snake.model.Snake
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class AppSuite extends AnyFunSpec with should.Matchers {
  describe("A snake"){
    def snake = Snake(Array((1,1),(1,2),(1,3)))
    it("should be knotted") {
      assert(snake.isKnotted)
    }
  }
}
