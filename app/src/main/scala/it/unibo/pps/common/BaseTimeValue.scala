package it.unibo.pps.common

import java.util.concurrent.{Executors, ScheduledExecutorService, ScheduledFuture}

case class BaseTimeValue() {
  private var time: Double = 0.0
  private val exec: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def start: ScheduledFuture[_] =
    exec.scheduleAtFixedRate(() => {
      time = time + 0.01
    }, 0, 100, java.util.concurrent.TimeUnit.MILLISECONDS)

  def getCurrentValue: Double = time
}
