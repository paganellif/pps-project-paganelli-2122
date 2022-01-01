package it.unibo.pps.snake.model

import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.World.{Boundary, Position}

import scala.util.Random

trait Food {
  /**
   * The 2D location of the food.
   *
   * @return a <code>Position</code>
   * @see World#Position
   */
  def position: Position

  /**
   * The score of the food.
   *
   * @return <code>Score</code>
   * @see Food#Score
   */
  def score: Score
}

/**
 *  Food Factory
 */
object Food {
  type Score = Int

  /**
   * @param position 2D location where to place the food
   * @return a healty <code>Food</code>
   */
  def createHealthyFood(position: Position): Food =
    FoodImpl(position, 100)

  /**
   * Returns a set of random foods.
   *
   * @param positionToBeExcluded positions that should not be included
   * @param boundary lower and upper limits (x, y) of the game "world"
   * @param nFood number of foods to be created
   * @return <code>Array[Food]</code>
   */
  def createRandomFoods(positionToBeExcluded: Array[Position], boundary: Boundary, nFood: Int): Array[Food] = {
    // For comprehension random food creation
    (for {
      i <- 0 until nFood
      position = (Random.between((boundary._1/10).round*10, (boundary._2/10).round*10),
        Random.between((boundary._3/10).round*10, (boundary._4/10)*10))
      // TODO: fix generate random while if condition is true
      if !positionToBeExcluded.contains(position)
    } yield if((position._1 + position._2) % 2 == 0) Food.createHealthyFood(position) else Food.createJunkFood(position)).toArray
  }

  /**
   *
   * @param position 2D location where to place the food
   * @return a junk <code>Food</code>
   */
  def createJunkFood(position: Position): Food =
    FoodImpl(position, -100)

  /**
   * Private base implementation of Food
   *
   * @param position 2D location where to place the food
   * @param score the score associated with this food
   */
  private case class FoodImpl(position: Position, score: Score) extends Food
}
