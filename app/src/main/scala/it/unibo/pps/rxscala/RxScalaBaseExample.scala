package it.unibo.pps.rxscala

import rx.lang.scala.{Observable, Observer}

object RxScalaBaseExample extends App {
  val helloWorld: String = "Hello RxScala World!"
  val observable = Observable.from(helloWorld)

  val observer = new Observer[Char] {
    override def onNext(char: Char): Unit = {
      print(char)
      if(char == '!') println("")
    }

    override def onError(error: Throwable): Unit = {
      println("Executing onError...")
      error.printStackTrace()
    }

    override def onCompleted(): Unit = {
      println("Executing onCompleted...")
    }
  }

  observable.subscribe(observer)
  observable.subscribe { msg =>
    print(s"$msg")
  }

}
