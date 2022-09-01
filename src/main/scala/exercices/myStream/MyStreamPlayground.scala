package exercices.myStream

import exercices.myStream.domain.{MyStream, NotEmptyStream, EmptyStream}

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
}
