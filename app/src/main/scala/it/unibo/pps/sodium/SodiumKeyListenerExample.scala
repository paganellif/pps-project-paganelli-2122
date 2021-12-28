package it.unibo.pps.sodium

import io.github.sodium.StreamSink

import java.awt.{FlowLayout, Label}
import java.awt.event.{KeyEvent, KeyListener}
import javax.swing.JFrame

object SodiumKeyListenerExample extends App {

  val myStream: StreamSink[String] = new StreamSink[String]()

  val jFrame: JFrame = new JFrame("TEST")

  jFrame.setLayout(new FlowLayout)
  jFrame.setSize(0, 0)
  jFrame.setVisible(true)

  jFrame.addKeyListener(new KeyListener() {
    override def keyTyped(e: KeyEvent): Unit = e.getKeyCode match {
      case KeyEvent.VK_LEFT => println("left")
      case KeyEvent.VK_RIGHT => println("right")
      case KeyEvent.VK_UP => println("up")
      case KeyEvent.VK_DOWN => println("down")
      case KeyEvent.VK_S => println("s")
      case KeyEvent.VK_Q => println("q")
      case KeyEvent.VK_U => println("u")
      case KeyEvent.VK_D => println("d")
      case _ => null
    }

    override def keyPressed(e: KeyEvent): Unit = e.getKeyCode match {
      case KeyEvent.VK_LEFT => myStream.send("pressed left")
      case KeyEvent.VK_RIGHT => myStream.send("pressed right")
      case KeyEvent.VK_UP => myStream.send("pressed up")
      case KeyEvent.VK_DOWN => myStream.send("pressed down")
      case KeyEvent.VK_S => myStream.send("pressed s")
      case KeyEvent.VK_Q => myStream.send("pressed q")
      case KeyEvent.VK_U => myStream.send("pressed u")
      case KeyEvent.VK_D => myStream.send("pressed d")
      case _ => null
    }

    override def keyReleased(e: KeyEvent): Unit = e.getKeyCode match {
      case KeyEvent.VK_LEFT => myStream.send("released left")
      case KeyEvent.VK_RIGHT => myStream.send("released right")
      case KeyEvent.VK_UP => myStream.send("released up")
      case KeyEvent.VK_DOWN => myStream.send("released down")
      case KeyEvent.VK_S => myStream.send("released s")
      case KeyEvent.VK_Q => myStream.send("released q")
      case KeyEvent.VK_U => myStream.send("released u")
      case KeyEvent.VK_D => myStream.send("released d")
      case _ => null
    }
  })

  myStream.listen(println)
}
