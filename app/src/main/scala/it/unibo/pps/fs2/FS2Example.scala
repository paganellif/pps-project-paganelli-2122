package it.unibo.pps.fs2

import fs2._
import fs2.interop.reactivestreams._
import scala.concurrent.duration.DurationInt

object FS2Example extends App {
  (Stream(1,2,3).delayBy(5.seconds) ++ Stream(9,8,7)).toList.foreach(println)
}