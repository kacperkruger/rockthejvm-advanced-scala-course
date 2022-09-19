package lectures.part4

import scala.annotation.tailrec

object ExtensionMethods extends App {
  case class Person(name: String) {
    def greet: String = s"Hi, my name is $name"
  }

  extension(string: String) { // extension method
    def greetAsPerson: String = Person(string).greet
  }

  val danielGreeting = "Daniel".greetAsPerson

  // extension methods <=> implicit classes

  object Scala2ExtensionMethod {
    implicit class RichInt(value: Int) {
      def isEven: Boolean = value % 2 == 0
      def sqrt: Double = Math.sqrt(value)

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
    }

  }

  //val is3Even = 3.isEven // new RichInt(3).isEven

  extension(value: Int) {
    // define all methods
    def increment: Int = value + 1
  }

  // generic extension
  extension [A](list: List[A]) {
    def ends: (A, A) = (list.head, list.last)
    def extremes(using ordering: Ordering[A]): (A, A) = list.sorted.ends // <- can call an extension method here
  }
}
