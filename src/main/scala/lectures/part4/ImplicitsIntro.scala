package lectures.part4

import scala.language.implicitConversions

object ImplicitsIntro extends App {
  val pair = "Daniel" -> 555

  case class Person(name: String) {
    def greet: String = s"Hi my name is $name"
  }

  implicit def fromStringToPerson(str: String): Person = Person(str)

  println("Peter".greet)

  def increment(x: Int)(implicit amount: Int) = x + amount

  implicit val defaultAmount: Int = 1

  increment(10)
  // NOT DEFAULT ARGS
}
