package it.unibo.pps.reactify

import it.unibo.pps.common.BaseTimeValue
import reactify.Var

import java.util.Random
import java.util.concurrent.{Executors, ScheduledExecutorService}

case class ReactifyTempSensor(min: Var[Double], max: Var[Double], spikeFreq: Var[Double]){
  val temp: Var[Double] = Var[Double](0.0)

  private var currentTemp: Double = 0.0
  private val time = BaseTimeValue()
  private val gen: Random = new Random(System.nanoTime())
  private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def start(): Unit = {
    executor.scheduleAtFixedRate(() => {
      val delta: Double = (-0.5 + gen.nextDouble()) * (max - min)*0.5
      val range: Double = (max - min) * 0.5

      if(gen.nextDouble() <= spikeFreq)
        currentTemp = currentTemp + (range*10)
      else
        currentTemp = ((max.get + min.get) * 0.5) + Math.sin(time.getCurrentValue)*range*0.8+delta

      temp.set(currentTemp)
    }, 0, 200, java.util.concurrent.TimeUnit.MILLISECONDS)
  }
}
