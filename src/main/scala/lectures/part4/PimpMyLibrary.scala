package lectures.part4

import scala.annotation.tailrec
import scala.language.implicitConversions

object PimpMyLibrary extends App {
  implicit class RichInt(value: Int) {
    def isEven: Boolean = value % 2 == 0
    def sqrt: Double = Math.sqrt(value)
  }

  44.isEven

  // type enrichment = pimping

  1 to 10

  implicit class RichString(value: String) {
    def asInt: Int = value.map(_.toInt).mkString("").toInt
    def encrypt: String = value.map(c => (c + 2).asInstanceOf[Char])
  }

  println("abc".encrypt)
  println("abc".asInt)

  implicit class EnrichInt(value: Int) {
    def times[A](f: () => Unit): Unit = {
      @tailrec
      def timeHelper(f: () => Unit, n: Int): Unit = {
        if (n <= 0) ()
        else {
          f()
          timeHelper(f, n - 1)
        }
      }
      timeHelper(f, value)
    }

    def *[T](list: List[T]): List[T] = {
      @tailrec
      def concatenate(list: List[T], n: Int, acc: List[T] = List()): List[T] = {
        if (n <= 0) acc
        else concatenate(list, n - 1, acc ++ list)
      }
      concatenate(list, value)
    }
  }

  3.times(() => println("44"))
  println(3 * List(1, 2, 3))

  implicit def stringToInt(string: String): Int = Integer.parseInt(string)

  println("6" / 3) // implicit conversion

  class RichAltInt(value: Int)
  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  implicit def intToBoolean(i: Int): Boolean = i == 1

  val aConditionValue = if (3) "OK" else "SOMETHING WRONG"
  println(aConditionValue)
}
