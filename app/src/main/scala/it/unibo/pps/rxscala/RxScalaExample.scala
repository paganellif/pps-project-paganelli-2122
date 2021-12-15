package it.unibo.pps.rxscala

import rx.lang.scala.Observable

object RxScalaExample extends App {
  def applyMapFunc[A](observable: Observable[A])(f: A => A): Observable[A] = observable.map(item => f(item))
  def applyFilterFunc[A](observable: Observable[A])(f: A => Boolean): Observable[A] = observable.filter(item => f(item))
  def mergeObs[A](obs1: Observable[A], obs2: Observable[A]): Observable[A] = obs1.merge(obs2)
  def takeN[A](n: Int, observable: Observable[A]): Observable[A] = observable.take(n)

  val obs = applyFilterFunc(Observable.from(0 to 100))(_)
  val even = obs(item => item % 2 == 0)
  val odd = obs(item => item % 2 != 0)

  mergeObs(takeN(2,even), takeN(3,odd)).subscribe(println(_))
}
