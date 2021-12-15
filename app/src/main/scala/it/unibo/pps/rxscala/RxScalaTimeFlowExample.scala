package it.unibo.pps.rxscala

import rx.lang.scala.Observable

import scala.concurrent.duration.DurationInt

object RxScalaTimeFlowExample extends App {
  val startTime = System.currentTimeMillis()

  Observable.interval(100.milliseconds)
    .timestamp
    .sample(500.milliseconds)
    .map(ts => String.format("%d ms: %d", (ts._1 - startTime), ts._2))
    .take(10)
    .subscribe(println(_))

  Thread.sleep(10000)
}
