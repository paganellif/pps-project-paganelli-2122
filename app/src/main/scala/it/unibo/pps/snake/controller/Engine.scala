package it.unibo.pps.snake.controller

import io.github.sodium.{Cell, StreamSink}
import it.unibo.pps.snake.controller.Status.Status

import java.lang.Thread.sleep
import scala.util.control.Breaks.break

object Status extends Enumeration {
  type Status = Value
  val RUNNING, PAUSE, STOP = Value
}

case class Event()

case class Engine(isRunning: Cell[Status], speed: Cell[Int]) extends Runnable {

  val ticker: StreamSink[Event] = new StreamSink[Event]()

  override def run(): Unit = {
    while(true){
      isRunning.sample() match {
        case Status.RUNNING => ticker.send(Event())
        case Status.STOP => System.exit(0)
        case Status.PAUSE => println("PAUSE STATUS")
        case _ => println("Unknown Status")
      }
      sleep(700 / speed.sample())
    }
  }
}
