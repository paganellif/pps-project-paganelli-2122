package it.unibo.pps.snake.model

import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.World.Position

trait Snake {

  /**
   * The body of the snake.
   */
  val body: Array[Position]

  /**
   * The head of the snake.
   */
  val head: Position

  /**
   * Return true if the snake is knotted, false otherwise.
   *
   * @return <code>Boolean</code>
   */
  def isKnotted: Boolean

  /**
   * Return a new Snake with the body reversed.
   *
   * @return <code>Snake</code>
   */
  def reversed: Snake

  /**
   * Return a new Snake moved in a specific direction.
   *
   * @param direction the direction in which the snake is to be moved.
   * @return <code>Snake</code>
   */
  def move(direction: Directions.Direction): Snake

  /**
   * Return a new Snake increased with a specific position prepended.
   *
   * @param position the position to be added
   * @return <code>Snake</code>
   */
  def increase(position: Position): Snake
}

object Snake {

  def apply(body: Array[Position]): Snake = SnakeImpl(body)

  /**
   * Private implementation of Snake
   *
   * @param body <code>Array</code> of <code>Position</code>
   */
  private case class SnakeImpl(override val body: Array[Position]) extends Snake {

    private val deltaMove: Int = 10

    override val head: Position = body.head

    override def isKnotted: Boolean = body.foldRight(false)((elem1, acc) => {
      if(!acc) body.count(elem2 => elem1 == elem2) > 1 else acc
    })

    override def reversed: Snake = Snake(body.reverse)

    override def move(direction: Direction): Snake = direction match {
      case Directions.RIGHT => Snake(Array.from(body.init).prepended((head._1 + deltaMove, head._2)))
      case Directions.LEFT  => Snake(Array.from(body.init).prepended((head._1 - deltaMove, head._2)))
      case Directions.UP    => Snake(Array.from(body.init).prepended((head._1, head._2 - deltaMove)))
      case Directions.DOWN  => Snake(Array.from(body.init).prepended((head._1, head._2 + deltaMove)))
    }

    override def increase(position: Position): Snake = Snake(body.prepended(position))
  }
}