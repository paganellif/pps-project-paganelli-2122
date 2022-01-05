package it.unibo.pps.reactify

import reactify.{Val, Var}

case class  ReactifyThermalManager(tempSensor: ReactifyTempSensor, thresholdTemp: Var[Double]) {

  private var _sum: Double = 0.0
  val sumTemp: Val[Double] = Val[Double]({ _sum += tempSensor.temp; _sum })

  private var _firingCount: Int = 0
  val firingCount: Val[Int] = tempSensor.temp.map(t => { _firingCount += 1; _firingCount })

  val avgTemp: Val[Double] = Val[Double](sumTemp / firingCount)

  private var _minTemp: Double = Double.MaxValue
  val minTemp: Val[Double] = Val[Double]({if(tempSensor.temp < _minTemp) _minTemp = tempSensor.temp.get; _minTemp })

  private var _maxTemp: Double = Double.MinValue
  val maxTemp: Val[Double] = Val[Double]({if(tempSensor.temp > _maxTemp) _maxTemp = tempSensor.temp.get; _maxTemp })

  val spikeTemp: Val[Option[Double]] = Val[Option[Double]] {
    val delta = Math.abs(tempSensor.temp - avgTemp)
    if(delta > thresholdTemp) Option(delta) else Option.empty
  }

  private var _spikeCount: Int = 0
  val spikeCount: Val[Int] = Val[Int]({if(spikeTemp.isDefined) _spikeCount += 1; _spikeCount})

  val spikeRate: Val[Double] = Val[Double](spikeCount / firingCount)

  def start(): Unit = tempSensor.start()

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
