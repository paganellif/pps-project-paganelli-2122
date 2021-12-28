package it.unibo.pps.snake.view

import javax.swing.JPanel

case class VisualizerPanel(width: Int, height: Int) extends JPanel {

  private 

  def this(width: Int, height: Int) {
    this(width, height)
    setSize(width, height)
  }

}