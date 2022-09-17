package lectures.part3

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object FuturesAndPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(3000)
    44
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } // (global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("waiting for the future")
  aFuture.onComplete {
    case Failure(exception) => println(exception)
    case Success(value)     => println(value)
  }

  Thread.sleep(3000)
}
