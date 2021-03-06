package it.unibo.pps.akkastream

import akka.Done
import akka.actor.{ActorSystem, Cancellable}
import akka.stream.scaladsl.Source

import scala.concurrent.duration.DurationInt
import scala.concurrent.{ExecutionContextExecutor, Future}

object AkkaStreamTimeFlowExample extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("HelloWorldSystem")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  val source: Source[Long, Cancellable] =
    Source.tick(100.milliseconds, 500.milliseconds, System.currentTimeMillis())
          .map(ts => System.currentTimeMillis() - ts)
          .take(20)

  val done1: Future[Done] = source.filter(ts => (ts % 2 ) == 0).runForeach(ts => println(s"[1] ms: $ts"))
  val done2: Future[Done] = source.filter(ts => (ts % 2 ) != 0).runForeach(ts => println(s"[2] ms: $ts"))

  done1.onComplete(_ => actorSystem.terminate())
}
