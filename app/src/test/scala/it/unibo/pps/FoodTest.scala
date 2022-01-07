package it.unibo.pps

import it.unibo.pps.snake.model.Food
import org.junit.runner.RunWith
import org.scalatest.funspec.AnyFunSpec
import org.scalatest.matchers.should
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class FoodTest extends AnyFunSpec with should.Matchers {
  describe("An healthy food (1,1)"){
    def food = Food.createHealthyFood(1,1)
    it("should have position (1,1)"){
      assert(food.position == (1,1))
    }

    it("should have positive score"){
      assert(food.score > 0)
    }
  }

  describe("A junk food (1,1)"){
    def food = Food.createJunkFood(1,1)
    it("should have position (1,1)"){
      assert(food.position == (1,1))
    }

    it("should have negative score"){
      assert(food.score < 0)
    }
  }
}
