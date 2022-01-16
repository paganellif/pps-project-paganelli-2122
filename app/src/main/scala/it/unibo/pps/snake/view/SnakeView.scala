package it.unibo.pps.snake.view

import io.github.sodium.CellSink
import it.unibo.pps.snake.controller.{Engine, SnakeController, Status}
import it.unibo.pps.snake.controller.Status.Status
import it.unibo.pps.snake.model.Directions
import it.unibo.pps.snake.model.Directions.Direction
import org.slf4j.LoggerFactory

import java.awt.{BorderLayout, LayoutManager}
import java.awt.event.{KeyEvent, KeyListener, WindowAdapter, WindowEvent}
import javax.swing.{JFrame, JPanel}

case class SnakeView(widthView: Int = 500, heightView: Int = 500) extends JFrame() {
  private val logger = LoggerFactory.getLogger(SnakeView.getClass)

  // Initial default direction: 1 right
  private val directionInput: CellSink[Direction] = new CellSink[Direction](Directions.RIGHT)

  private val statusInput: CellSink[Status] = new CellSink[Status](Status.PAUSE)

  // Initial default speed: 1
  private val speedInput: CellSink[Int] = new CellSink[Int](1)

  def init(): Unit = {
    setSize(widthView, heightView)
    setTitle("FRP Snake")
    val snakeController: SnakeController = SnakeController(directionInput, (0,widthView,0,heightView), Engine(statusInput, speedInput))

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
        case _ => logger.error("Unknown Command")
      }

      override def keyReleased(e: KeyEvent): Unit = e.getKeyCode match {
        case KeyEvent.VK_LEFT => logger.debug(s"${directionInput.sample()}")
        case KeyEvent.VK_RIGHT => logger.debug(s"${directionInput.sample()}")
        case KeyEvent.VK_UP => logger.debug(s"${directionInput.sample()}")
        case KeyEvent.VK_DOWN => logger.debug(s"${directionInput.sample()}")
        case KeyEvent.VK_S => logger.debug(s"${statusInput.sample()}")
        case KeyEvent.VK_Q => logger.debug(s"${statusInput.sample()}")
        case KeyEvent.VK_R => logger.debug(s"${statusInput.sample()}")
        case KeyEvent.VK_P => logger.debug(s"${statusInput.sample()}")
        case KeyEvent.VK_U => logger.debug(s"${speedInput.sample()}")
        case KeyEvent.VK_D => logger.debug(s"${speedInput.sample()}")
        case _ => logger.error("Unknown Command")
      }
    })

    addWindowListener(new WindowAdapter() {
      override def windowClosing(e: WindowEvent): Unit = System.exit(-1)
      override def windowClosed(e: WindowEvent): Unit = System.exit(-1)
    })

    snakeController.start()
    snakeController.output.listen(sf => {snakeVisualizerPanel.repaintSnake(sf._1); snakeVisualizerPanel.repaintFood(sf._2)})
    snakeController.scoreOutput.listen(s => snakeVisualizerPanel.repaintScore(s))
    snakeController.isKnottedOutput.listen(s => if(s) System.exit(0))

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
