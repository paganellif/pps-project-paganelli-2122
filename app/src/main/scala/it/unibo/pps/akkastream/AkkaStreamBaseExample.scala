package it.unibo.pps.akkastream

import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.stream.scaladsl.Source

import scala.concurrent.{ExecutionContextExecutor, Future}

object AkkaStreamBaseExample extends App {
  implicit val actorSystem: ActorSystem = ActorSystem("HelloWorldSystem")
  implicit val ec: ExecutionContextExecutor = actorSystem.dispatcher

  val source: Source[Int, NotUsed] = Source(1 to 100)

  val done: Future[Done] = source.runForeach(println)

  done.onComplete(_ => actorSystem.terminate())
}

