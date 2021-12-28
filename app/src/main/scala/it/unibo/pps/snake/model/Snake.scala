package it.unibo.pps.snake.model

import it.unibo.pps.snake.model.Directions.Direction
import it.unibo.pps.snake.model.World.Position

trait Snake {

  var body: Array[Position]

  val head: Position

  val isKnotted: Boolean

  val reversed: Array[Position]

  def move(direction: Directions.Direction): Array[Position]

  def increase(position: Position): Array[Position]
}

object Snake {

  def apply(body: Array[Position]): Snake = SnakeImpl(body)

  private case class SnakeImpl(var body: Array[Position]) extends Snake {

    override val head: Position = body.head

    override val isKnotted: Boolean = body.foldRight(false)((elem1, acc) => {
      if(!acc) body.count(elem2 => elem1 == elem2) > 1 else acc
    })

    override val reversed: Array[Position] = body.reverse

    override def move(direction: Direction): Array[Position] = direction match {
      case Directions.RIGHT => Array.from(body.init).prepended((head._1 + 10, head._2))
      case Directions.LEFT  => Array.from(body.init).prepended((head._1 - 10, head._2))
      case Directions.UP    => Array.from(body.init).prepended((head._1, head._2 - 10))
      case Directions.DOWN  => Array.from(body.init).prepended((head._1, head._2 + 10))
    }

    override def increase(position: Position): Array[Position] = body.prepended(position)
  }
}