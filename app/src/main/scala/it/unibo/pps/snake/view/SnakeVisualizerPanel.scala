package it.unibo.pps.snake.view

import io.github.sodium.{Cell, Stream}
import it.unibo.pps.snake.model.Food.Score
import it.unibo.pps.snake.model.{Food, Snake}

import java.awt.{Graphics, Graphics2D, RenderingHints}
import javax.swing.JPanel

case class SnakeVisualizerPanel(widthView: Int, heightView: Int, speed: Cell[Int]) extends JPanel {

  private var tmpSnake: Option[Snake] = Option.empty
  private var tmpFood: Option[Array[Food]] = Option.empty
  private var tmpScore: Option[Score] = Option.empty

  override def paint(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g2.clearRect(0, 0, getWidth, getHeight)

    if(tmpSnake.isDefined)
      tmpSnake.get.body.foreach(snakePiece => {
        g2.drawString("@", snakePiece._1, snakePiece._2)
      })

    if(tmpFood.isDefined)
      tmpFood.get.foreach(food => {
        g2.drawString("f", food.position._1, food.position._2)
      })

    g2.drawString("[S]tart [Q]uit [R]esume [P]ause", 15, 15)
    g2.drawString(s"Speed: ${speed.sample()} [U]p [D]own", 15, 35)
    g2.drawString(s"Score: ${tmpScore.getOrElse(0)} ", 15, 55)

  }

  def repaintSnake(snake: Snake): Unit = {
    this.tmpSnake = Option(snake)
    repaint()
  }

  def repaintFood(food: Array[Food]): Unit = {
    this.tmpFood = Option(food)
    repaint()
  }

  def repaintScore(score: Score): Unit = {
    this.tmpScore = Option(score)
    repaint()
  }

}