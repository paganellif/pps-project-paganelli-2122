package it.unibo.pps.snake.controller

import io.github.sodium.{CellSink, StreamSink}
import it.unibo.pps.snake.controller.Status.Status
import org.slf4j.{Logger, LoggerFactory}

import java.lang.Thread.sleep

object Status extends Enumeration {
  type Status = Value
  val RUNNING, PAUSE, STOP = Value
}

case class Event()

case class Engine(statusInput: CellSink[Status], speedInput: CellSink[Int]) extends Runnable {

  private val logger: Logger = LoggerFactory.getLogger(Engine.getClass)
  val ticker: StreamSink[Event] = new StreamSink[Event]()

  override def run(): Unit = {
    while(true){
      statusInput.sample() match {
        case Status.RUNNING => logger.debug("RUNNING STATUS"); ticker.send(Event())
        case Status.STOP => System.exit(0)
        case Status.PAUSE => logger.debug("PAUSE STATUS")
        case _ => logger.error("Unknown Status")
      }
      val freq: Int = (1000 / speedInput.sample()).round
      logger.debug(s"engine freq: ${freq}")
      sleep(freq)
    }
  }
}
