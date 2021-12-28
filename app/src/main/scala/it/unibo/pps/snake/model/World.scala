package it.unibo.pps.snake.model

object Directions extends Enumeration {
  type Direction = Value
  val UP, DOWN, LEFT, RIGHT = Value
}

object World {
  type Position = (Int,Int)
  type Boundary = (Int,Int,Int,Int)


}
