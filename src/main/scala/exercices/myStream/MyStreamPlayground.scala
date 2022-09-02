package exercices.myStream

import exercices.myStream.domain.{EmptyStream, MyStream, NotEmptyStream}

import scala.annotation.tailrec
import scala.compiletime.ops.any.==

object MyStreamPlayground extends App {
  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head)
  println(naturals.tail.head)
  println(naturals.tail.tail.head)

  val startFrom0 = 0 #:: naturals
  println(startFrom0.head)

//  startFrom0.take(10_000).foreach(println)

  println(startFrom0.map(_ * 2).take(100).toList())
  println(
    startFrom0
      .flatMap(x =>
        new NotEmptyStream(x, new NotEmptyStream(x + 1, new EmptyStream))
      )
      .takeAsList(10)
  )

//  println(startFrom0.filter(_ < 10).take(10).toList())

  def fibonacci(elem1: Int, elem2: Int): MyStream[Int] = {
    new NotEmptyStream[Int](
      elem1,
      fibonacci(elem2, elem1 + elem2)
    )
  }

  val fib = fibonacci(1, 1)
  println(fib.takeAsList(7))

  def sieve(
      stream: MyStream[Int]
  ): MyStream[Int] = stream match {
    case stream: EmptyStream => stream
    case stream: NotEmptyStream[_] =>
      val head = stream.head
      val tail = stream.tail

      if (head != 0 && head != 1) {
        new NotEmptyStream[Int](
          head,
          sieve(tail.filter(_ % head != 0))
        )
      } else sieve(tail)

    case _ => stream
  }

  val sv = sieve(naturals.take(100))
  println(sv.takeAsList(100))

}
