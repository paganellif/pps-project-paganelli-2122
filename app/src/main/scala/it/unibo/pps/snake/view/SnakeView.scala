package it.unibo.pps.snake.view

import io.github.sodium.CellSink
import it.unibo.pps.snake.controller.{SnakeController, Status}
import it.unibo.pps.snake.controller.Status.Status
import it.unibo.pps.snake.model.Directions
import it.unibo.pps.snake.model.Directions.Direction

import java.awt.{BorderLayout, LayoutManager}
import java.awt.event.{KeyEvent, KeyListener, WindowAdapter, WindowEvent}
import javax.swing.{JFrame, JPanel}

case class SnakeView(widthView: Int = 500, heightView: Int = 500) extends JFrame() {
  // Initial default direction: 1 right
  private val directionInput: CellSink[Direction] = new CellSink[Direction](Directions.RIGHT)

  private val statusInput: CellSink[Status] = new CellSink[Status](Status.PAUSE)

  // Initial default speed: 1
  private val speedInput: CellSink[Int] = new CellSink[Int](1)

  def init(): Unit = {
    setSize(widthView, heightView)
    setTitle("FRP Snake")
    val snakeController: SnakeController = SnakeController(directionInput, statusInput, speedInput, (0,widthView,0,heightView))

    val snakeVisualizerPanel: SnakeVisualizerPanel = SnakeVisualizerPanel(widthView, heightView, speedInput)

    this.addKeyListener(new KeyListener {
      override def keyTyped(e: KeyEvent): Unit = null

      override def keyPressed(e: KeyEvent): Unit = e.getKeyCode match {
        case KeyEvent.VK_LEFT => directionInput.send(Directions.LEFT)
        case KeyEvent.VK_RIGHT => directionInput.send(Directions.RIGHT)
        case KeyEvent.VK_UP => directionInput.send(Directions.UP)
        case KeyEvent.VK_DOWN => directionInput.send(Directions.DOWN)
        case KeyEvent.VK_S => statusInput.send(Status.RUNNING)
        case KeyEvent.VK_Q => statusInput.send(Status.STOP)
        case KeyEvent.VK_R => statusInput.send(Status.RUNNING)
        case KeyEvent.VK_P => statusInput.send(Status.PAUSE)
        case KeyEvent.VK_U => speedInput.send(speedInput.sample() + 1)
        case KeyEvent.VK_D => speedInput.send(if (speedInput.sample() > 1) speedInput.sample() - 1 else 1)
        case _ => println("Unknown Command")
      }

      override def keyReleased(e: KeyEvent): Unit = e.getKeyCode match {
        case KeyEvent.VK_LEFT => println(s"${directionInput.sample()}")
        case KeyEvent.VK_RIGHT => println(s"${directionInput.sample()}")
        case KeyEvent.VK_UP => println(s"${directionInput.sample()}")
        case KeyEvent.VK_DOWN => println(s"${directionInput.sample()}")
        case KeyEvent.VK_S => println(s"${statusInput.sample()}")
        case KeyEvent.VK_Q => println(s"${statusInput.sample()}")
        case KeyEvent.VK_R => println(s"${statusInput.sample()}")
        case KeyEvent.VK_P => println(s"${statusInput.sample()}")
        case KeyEvent.VK_U => println(s"${speedInput.sample()}")
        case KeyEvent.VK_D => println(s"${speedInput.sample()}")
        case _ => println("Unknown Command")
      }
    })

    addWindowListener(new WindowAdapter() {
      override def windowClosing(e: WindowEvent): Unit = System.exit(-1)
      override def windowClosed(e: WindowEvent): Unit = System.exit(-1)
    })

    snakeController.start()
    snakeController.snakeOutput().listen(s => snakeVisualizerPanel.repaintSnake(s))

    val cp: JPanel = new JPanel()
    val lm: LayoutManager = new BorderLayout()
    cp.setLayout(lm)
    cp.add(BorderLayout.CENTER, snakeVisualizerPanel)
    setContentPane(cp)
    setResizable(false)
    setLocationRelativeTo(null)
    setVisible(true)
  }
}
