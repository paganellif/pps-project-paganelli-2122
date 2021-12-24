package it.unibo.pps.reactify

import reactify.{Val, Var}

case class  ReactifyThermalManager(tempSensor: ReactifyTempSensor, thresholdTemp: Var[Double]) {

  val sumTemp: Var[Double] = Var[Double](0.0)

  val firingCount: Var[Int] = Var[Int](0)

  val avgTemp: Val[Double] = Val[Double](sumTemp / firingCount)

  val minTemp: Var[Double] = Var[Double](tempSensor.max)

  val maxTemp: Var[Double] = Var[Double](tempSensor.min)

  val spikeTemp: Var[Option[Double]] = Var[Option[Double]] {
    val delta = Math.abs(tempSensor.temp - avgTemp)
    if(delta > thresholdTemp) Option(delta) else Option.empty
  }

  val spikeCount: Var[Int] = Var[Int](0)

  val spikeRate: Val[Double] = Val[Double](spikeCount / firingCount)

  def start(): Unit = {
    tempSensor.temp.attach(t => {
      sumTemp.set(sumTemp.get + t)
      firingCount.set(firingCount.get + 1)
      minTemp.set(if(t < minTemp.get) t else minTemp.get)
      maxTemp.set(if(t > maxTemp.get) t else maxTemp.get)
    })

    spikeTemp.attach(t => spikeCount.set(spikeCount.get + 1))
    tempSensor.start()
  }

  def log(): Unit = {
    tempSensor.temp.attach(t => println(s"[${Thread.currentThread().getName}] temp: $t"))
    sumTemp.attach(t => println(s"[${Thread.currentThread().getName}] sum: $t"))
    firingCount.attach(f => println(s"[${Thread.currentThread().getName}] firingCount: $f"))
    avgTemp.attach(avg => println(s"[${Thread.currentThread().getName}] avg: $avg"))
    minTemp.attach(min => println(s"[${Thread.currentThread().getName}] min: $min"))
    maxTemp.attach(max => println(s"[${Thread.currentThread().getName}] max: $max"))
    spikeTemp.attach(spike => println(s"[${Thread.currentThread().getName}] spike: $spike"))
    spikeCount.attach(s => println(s"[${Thread.currentThread().getName}] spikeCount: $s"))
    spikeRate.attach(s => println(s"[${Thread.currentThread().getName}] spikeRate: $s"))
  }
}

object ReactifyThermalManagerExample extends App {
  val tempSensor: ReactifyTempSensor = ReactifyTempSensor(Var(10), Var(40), Var(0.1))
  val thermalManager: ReactifyThermalManager = ReactifyThermalManager(tempSensor, Var(20.0))

  thermalManager.log()
  thermalManager.start()
}
