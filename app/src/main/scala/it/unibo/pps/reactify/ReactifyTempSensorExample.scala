package it.unibo.pps.reactify

import reactify.Var

import java.lang.Thread.sleep
import java.util.Random

object ReactifyTempSensorExample extends App {
  val minTempGuiInput: Var[Double] = Var[Double](0.0)
  val maxTempGuiInput: Var[Double] = Var[Double](0.0)
  val spikeFreqGuiInput: Var[Double] = Var[Double](0.0)

  val tempSensor1: ReactifyTempSensor = ReactifyTempSensor(
    minTempGuiInput, maxTempGuiInput, spikeFreqGuiInput
  )

  tempSensor1.temp.attach(println)

  tempSensor1.start()

  new Thread(() => {
    while(true) {
      // Mocked GUI Input
      val minTempGUI: Double = new Random().ints(0,50).findFirst().getAsInt
      val maxTempGUI: Double = new Random().ints(51,100).findFirst().getAsInt
      val spikeFreqGUI: Double = new Random().ints(0,10).findFirst().getAsInt * 0.1

      println(s"------Changed min,max,spikeFreq from mocked GUI: min=$minTempGUI max=$maxTempGUI spikeFreq=$spikeFreqGUI ------")

      minTempGuiInput.set(minTempGUI)
      maxTempGuiInput.set(maxTempGUI)
      spikeFreqGuiInput.set(spikeFreqGUI)
      sleep(5000)
    }
  }).start()
}
