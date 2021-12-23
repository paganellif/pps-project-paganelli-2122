package it.unibo.pps.sodium

import io.github.sodium.StreamSink

import java.lang.Thread.sleep
import java.util.Random

object SodiumTempSensorExample extends App {
  val minTempGuiInput: StreamSink[Double] = new StreamSink[Double]()
  val maxTempGuiInput: StreamSink[Double] = new StreamSink[Double]()
  val spikeFreqGuiInput: StreamSink[Double] = new StreamSink[Double]()

  val tempSensor1: SodiumTempSensor = SodiumTempSensor(
    minTempGuiInput.hold(20),
    maxTempGuiInput.hold(40),
    spikeFreqGuiInput.hold(0.1)
  )

  tempSensor1.temp.listen(println)

  tempSensor1.start()

  new Thread(() => {
    while(true) {
      // Mocked GUI Input
      val minTempGUI: Double = new Random().ints(0,50).findFirst().getAsInt
      val maxTempGUI: Double = new Random().ints(51,100).findFirst().getAsInt
      val spikeFreqGUI: Double = new Random().ints(0,10).findFirst().getAsInt * 0.1

      println(s"------Changed min,max,spikeFreq from mocked GUI: min=$minTempGUI max=$maxTempGUI spikeFreq=$spikeFreqGUI ------")

      minTempGuiInput.send(minTempGUI)
      maxTempGuiInput.send(maxTempGUI)
      spikeFreqGuiInput.send(spikeFreqGUI)
      sleep(5000)
    }
  }).start()
}
