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
   * @return <code>Option[Snake]</code>
   */
  def reversed: Option[Snake]

  /**
   * Return a new Snake moved in a specific direction.
   *
   * @param direction the direction in which the snake is to be moved.
   * @return <code>Option[Snake]</code>
   */
  def move(direction: Directions.Direction): Option[Snake]

  /**
   * Return a new Snake increased with a specific position prepended.
   *
   * @param position the position to be added
   * @return <code>Option[Snake]</code>
   */
  def increase(position: Position): Option[Snake]
}

object Snake {

  def apply(body: Array[Position]): Snake = SnakeImpl(body)

  /**
   * Private implementation of Snake
   *
   * @param body <code>Array[Position]</code>
   */
  private case class SnakeImpl(override val body: Array[Position]) extends Snake {

    implicit class OptionalSnake(snake: Snake) {
      def toOptional: Option[Snake] = if(!snake.isKnotted) Option(snake) else Option.empty
    }

    private val deltaMove: Int = 10

    override val head: Position = body.head

    override def isKnotted: Boolean = body.foldRight(false)((elem1, acc) => {
      if(!acc) body.count(elem2 => elem1 == elem2) > 1 else acc
    })

    override def reversed: Option[Snake] = Snake(body.reverse).toOptional

    override def move(direction: Direction): Option[Snake] = direction match {
      case Directions.RIGHT => if(!this.isKnotted) Snake(Array.from(body.init).prepended((head._1 + deltaMove, head._2))).toOptional else Option.empty
      case Directions.LEFT  => if(!this.isKnotted) Snake(Array.from(body.init).prepended((head._1 - deltaMove, head._2))).toOptional else Option.empty
      case Directions.UP    => if(!this.isKnotted) Snake(Array.from(body.init).prepended((head._1, head._2 - deltaMove))).toOptional else Option.empty
      case Directions.DOWN  => if(!this.isKnotted) Snake(Array.from(body.init).prepended((head._1, head._2 + deltaMove))).toOptional else Option.empty
    }

    override def increase(position: Position): Option[Snake] = if(!this.isKnotted) Snake(body.prepended(position)).toOptional else Option.empty
  }
}