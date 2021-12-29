package it.unibo.pps.snake.model

import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.World.Position

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

  /**
   * The unicode emoji representation of the food.
   *
   * @return <code>String</code>
   * @see https://unicode.org/emoji/charts/full-emoji-list.html
   */
  def unicodeEmojiCode: String
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
    FoodImpl(position, 100, new String(Character.toChars(0x1F40D)))

  /**
   *
   * @param position 2D location where to place the food
   * @return a junk <code>Food</code>
   */
  def createJunkFood(position: Position): Food =
    FoodImpl(position, -100, new String(Character.toChars(0x1F35F)))

  /**
   * Private base implementation of Food
   *
   * @param position 2D location where to place the food
   * @param score the score associated with this food
   * @param unicodeEmojiCode the unicode emoji representation of this food
   */
  private case class FoodImpl(position: Position, score: Score, unicodeEmojiCode: String) extends Food
}
