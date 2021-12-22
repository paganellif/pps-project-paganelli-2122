/*
package it.unibo.pps.snake.test

import scala.collection.mutable.ListBuffer

case class Snake(override val body: ListBuffer[Position]) extends MultiItem(body) with DynamicItem {
  def eat(food: Food): ListBuffer[Position] = body.addOne(food.position)
  override def move(): ListBuffer[Position] = this.direction match {
    case Directions.RIGHT => body.remove(body.size - 1); body.addOne(Position(this.head.x + 1, this.head.y))
    case Directions.LEFT => body.remove(body.size - 1); body.addOne(Position(this.head.x - 1, this.head.y))
    case Directions.DOWN => body.remove(body.size - 1); body.addOne(Position(this.head.x, this.head.y + 1))
    case Directions.UP => body.remove(body.size - 1); body.addOne(Position(this.head.x, this.head.y - 1))
  }

  def turn(direction: Directions.Direction): Unit = this.direction = direction
}

case class Food(position: Position, score: Int) extends SingleItem(position) with StaticItem
*/
