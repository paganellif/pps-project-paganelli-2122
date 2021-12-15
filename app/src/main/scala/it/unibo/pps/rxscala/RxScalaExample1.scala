package it.unibo.pps.rxscala

import rx.lang.scala.Observable

object RxScalaExample1 extends App {
  def applyMapFunc[A](observable: Observable[A])(f: A => A): Observable[A] = observable.map(item => f(item))
  def applyFilterFunc[A](observable: Observable[A])(f: A => Boolean): Observable[A] = observable.filter(item => f(item))
  def mergeObs[A](obs1: Observable[A], obs2: Observable[A]): Observable[A] = obs1.merge(obs2)

  val obs = applyFilterFunc(Observable.from(0 to 100))(_)
  val even = obs(item => item % 2 == 0)
  val odd = obs(item => item % 2 != 0)

  mergeObs(even, odd).subscribe(println(_))
}
