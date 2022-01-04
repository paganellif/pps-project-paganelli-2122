package it.unibo.pps.sodium

import io.github.sodium.{Cell, StreamSink}
import it.unibo.pps.common.BaseTimeValue

import java.util.Random
import java.util.concurrent.{Executors, ScheduledExecutorService}

case class SodiumTempSensor(min: Cell[Double], max: Cell[Double], spikeFreq: Cell[Double]){
  val temp: StreamSink[Double] = new StreamSink[Double]()
  private var currentTemp: Double = 0.0
  private val time = BaseTimeValue()
  private val gen: Random = new Random(System.nanoTime())
  private val executor: ScheduledExecutorService = Executors.newScheduledThreadPool(1)

  def start(): Unit = {
    executor.scheduleAtFixedRate(() => {
      val delta: Double = (-0.5 + gen.nextDouble()) * (max.sample() - min.sample())*0.5
      val range: Double = (max.sample() - min.sample()) * 0.5

      if(gen.nextDouble() <= spikeFreq.sample())
        currentTemp = currentTemp + (range*10)
      else
        currentTemp = ((max.sample() + min.sample()) * 0.5) + Math.sin(time.getCurrentValue)*range*0.8+delta

      temp.send(currentTemp)
    }, 0, 200, java.util.concurrent.TimeUnit.MILLISECONDS)
  }
}

