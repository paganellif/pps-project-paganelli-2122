package it.unibo.pps.reactify

import reactify.{Dep, Var}

case class  ReactifyThermalManager(tempSensor: ReactifyTempSensor, thresholdTemp: Var[Double]) {
  def start(): Unit = tempSensor.start()

  def sumTemp: Var[Double] = Var(0.0)

  def log(): Unit = {
    sumTemp.attach(t => println(s"[${Thread.currentThread().getName}] sum: $t"))
  }
}

object ReactifyThermalManagerExample extends App {
  val tempSensor: ReactifyTempSensor = ReactifyTempSensor(Var(10), Var(40), Var(0.1))
  val thermalManager: ReactifyThermalManager = ReactifyThermalManager(tempSensor, Var(20.0))

  thermalManager.log()
  thermalManager.start()
}
