package it.unibo.pps.sodium

import io.github.sodium.{Cell, Stream}

case class SodiumThermalManager(tempSensor: SodiumTempSensor, thresholdTemp: Cell[Double]){

  implicit class StreamCounter[A](stream: Stream[A]) {
    def count(): Stream[Int] = stream
      .accum(0, (t: A, acc: Int) => acc + 1).updates()
  }

  def start(): Unit = tempSensor.start()

  def sumTemp: Stream[Double] = tempSensor.temp
    .accum(0.0, (t: Double,acc: Double) => acc + t).updates()

  def firingCount: Stream[Int] = tempSensor.temp.count()

  def avgTemp: Stream[Double] = sumTemp.merge(firingCount.map(i => i.toDouble), (s,f) => s / f)

  def minTemp: Stream[Double] = tempSensor.temp
    .accum(tempSensor.max.sample(), (t: Double, acc: Double) => if (t < acc) t else acc).updates()

  def maxTemp: Stream[Double] = tempSensor.temp
    .accum(tempSensor.min.sample(), (t: Double, acc: Double) => if (t > acc) t else acc).updates()

  def spikeTemp: Stream[Double] = tempSensor.temp
    .merge(avgTemp, (t, avg) => Math.abs(t - avg)).filter(t => t > thresholdTemp.sample())

  def spikeCount: Stream[Int] = spikeTemp.count()

  def spikeRate: Stream[Double] = spikeCount.map(s => s.toDouble)
    .merge(firingCount.map(f => f.toDouble), (spikes, firings) => spikes / firings)

  def log(): Unit = {
    tempSensor.temp.listen(t => println(s"[${Thread.currentThread().getName}] temp: $t"))
    sumTemp.listen(s => println(s"[${Thread.currentThread().getName}] sum: $s"))
    firingCount.listen(f => println(s"[${Thread.currentThread().getName}] firingCount: $f"))
    avgTemp.listen(avg => println(s"[${Thread.currentThread().getName}] avg: $avg"))
    minTemp.listen(min => println(s"[${Thread.currentThread().getName}] min: $min"))
    maxTemp.listen(max => println(s"[${Thread.currentThread().getName}] max: $max"))
    spikeTemp.listen(spike => println(s"[${Thread.currentThread().getName}] spike: $spike"))
    spikeCount.listen(s => println(s"[${Thread.currentThread().getName}] spikeCount: $s"))
    spikeRate.listen(s => println(s"[${Thread.currentThread().getName}] spikeRate: $s"))
  }
}


object SodiumThermalManagerExample extends App {
  val tempSensor: SodiumTempSensor = SodiumTempSensor(new Cell[Double](10), new Cell[Double](40), new Cell[Double](0.1))
  val thermalManager: SodiumThermalManager = SodiumThermalManager(tempSensor, new Cell[Double](20.0))

  thermalManager.log()
  thermalManager.start()
}
