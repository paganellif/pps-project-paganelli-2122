package it.unibo.pps.sodium

import io.github.sodium.{Cell, StreamSink}

import java.lang.Thread.sleep

object SodiumTimeFlowExample extends App {
  // Constant primitive: There’s no way to modify a cell after it’s created,
  // so its value is guaranteed to be constant forever.
  val startTime: Cell[Long] = new Cell(System.currentTimeMillis())

  val currentTime: StreamSink[Long] = new StreamSink[Long]()

  // Map, sample primitive
  currentTime.map(ct => ct - startTime.sample())
    .listen(ms => println(s"ms: $ms"))

  new Thread(() => {
    while(true) {
      // Stream firing
      currentTime.send(System.currentTimeMillis())
      sleep(500)
    }
  }).start()
}
