package it.unibo.pps.snake.model

object Directions extends Enumeration {
  type Direction = Value
  val UP, DOWN, LEFT, RIGHT = Value

  def opposite(direction: Direction): Direction = direction match {
    case UP => DOWN
    case DOWN => UP
    case LEFT => RIGHT
    case RIGHT => LEFT
    case _ => null
  }
}

object World {
  type Position = (Int,Int)
  type Boundary = (Int,Int,Int,Int)


}
