package it.unibo.pps.reactify

import reactify.{Channel, Dep, Val, Var}

import scala.jdk.DoubleAccumulator

case class  ReactifyThermalManager(tempSensor: ReactifyTempSensor, thresholdTemp: Var[Double]) {

  private var _sum: Var[Double] = Var[Double](0.0)
  def sumTemp: Val[Double] = Val[Double](_sum)

  def start(): Unit = {
    tempSensor.temp.attach(t => _sum.set(_sum.get + t))
    tempSensor.start()
  }

  def log(): Unit = {
    tempSensor.temp.attach(t => println(s"[${Thread.currentThread().getName}] temp: $t"))
    sumTemp.attach(t => println(s"[${Thread.currentThread().getName}] sum: $t"))
  }
}

object ReactifyThermalManagerExample extends App {
  val tempSensor: ReactifyTempSensor = ReactifyTempSensor(Var(10), Var(40), Var(0.1))
  val thermalManager: ReactifyThermalManager = ReactifyThermalManager(tempSensor, Var(20.0))

  thermalManager.log()
  thermalManager.start()
}
