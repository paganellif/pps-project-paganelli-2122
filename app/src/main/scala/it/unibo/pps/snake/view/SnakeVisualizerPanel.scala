package it.unibo.pps.snake.view

import io.github.sodium.{Cell, Stream}
import it.unibo.pps.snake.model.Snake

import java.awt.{Graphics, Graphics2D, RenderingHints}
import javax.swing.JPanel

case class SnakeVisualizerPanel(widthView: Int, heightView: Int, speed: Cell[Int]) extends JPanel {

  private var tmpSnake: Option[Snake] = Option.empty

  override def paint(g: Graphics): Unit = {
    val g2 = g.asInstanceOf[Graphics2D]

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
    g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY)
    g2.clearRect(0, 0, getWidth, getHeight)

    if(tmpSnake.isDefined)
      tmpSnake.get.body.foreach(snakePiece => {
        g2.drawString("@", snakePiece._1, snakePiece._2)
      })

    /*foods.sample().foreach(food => {
      g2.drawString(new String(Character.toChars(0x1F40D)), food._1, food._2)
    })*/

    g2.drawString(s"[S]tart [Q]uit [R]esume [P]ause - \nSpeed: ${speed.sample()} [U]p [D]own", 15, 15)

  }

  def repaintSnake(snake: Snake): Unit = {
    this.tmpSnake = Option(snake)
    repaint()
  }
}