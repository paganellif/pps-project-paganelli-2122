package it.unibo.pps.reactify

import reactify.{Val, Var}

import java.lang.Thread.sleep

object ReactifyTimeFlowExample extends App {
  val startTime: Val[Long] = Val(System.currentTimeMillis())

  val currentTime: Var[Long] = Var(System.currentTimeMillis())

  val delta: Val[Long] = Val[Long](currentTime - startTime)

  // Reaction
  delta.attach(ms => println(s"ms: $ms"))

  new Thread(() => {
    while(true) {
      // Since delta is a Val it is immutable itself,
      // but its value is derived from the formula 'currentTime - startTime'.
      // This means that a change to currentTime will cause the
      // value of delta to change as a result
      currentTime.set(System.currentTimeMillis())
      sleep(500)
    }
  }).start()
}
