/*
package it.unibo.pps.snake.test

import it.unibo.pps.snake.Directions.Direction

import scala.collection.mutable.ListBuffer

case class Position(x: Int, y: Int)

object Directions extends Enumeration {
  type Direction = Value
  val NONE, UP, DOWN, LEFT, RIGHT = Value
}

abstract class Item {
  def body: ListBuffer[Position]
  def head: Position
  def tail: Position
  def direction: Directions.Direction
  def size: Int = body.size
}

trait StaticItem extends Item {
  abstract override val direction: Direction = Directions.NONE
}

trait DynamicItem extends Item {
  abstract override var direction: Direction = Directions.RIGHT
  def move(): ListBuffer[Position]
}

class SingleItem(bodyPos: Position) extends Item with StaticItem {
  override def body: ListBuffer[Position] = ListBuffer.from(List(bodyPos))
  override def head: Position = bodyPos
  override def tail: Position = bodyPos
}

class MultiItem(bodyPos: collection.mutable.ListBuffer[Position]) extends Item with StaticItem {
  override def body: ListBuffer[Position] = bodyPos
  override def head: Position = body.head
  override def tail: Position = body.last
}

*/
