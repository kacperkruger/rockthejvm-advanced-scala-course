package lectures.part1

import scala.util.Try

object DarkSugar extends App {

  //#1 methods with single param
  def singleArgMethod(arg: Int): String = s"$arg little ducks..."

  val description = singleArgMethod {
    // write some code
    24
  }

  val aTryInstance = Try { //java's try {..}
    throw new RuntimeException
  }

  List(1, 2, 3).map { x =>
    x + 1
  }

  // #2 single abstract method pattern
  trait Action {
    def act(x: Int): Int
  }

  val anInstance: Action = new Action {
    override def act(x: Int): Int = x + 1
  }

  val aFunkyInstance: Action = (x: Int) => x + 1

  val aThread = new Thread(new Runnable {
    override def run(): Unit = println("hola amigos")
  })

  val aSweeterThread = new Thread(() => println("sweet thread"))

  abstract class AnAbstractType {
    def implemented: Int = 23

    def f(a: Int): Unit
  }

  val anAbstractInstance: AnAbstractType = (a: Int) => println("sweet")

  // #3 :: :#

  val prependedList = 2 :: List(1, 2) //2.::List(1, 2)
  //List(1, 2).::2 ????>>>>>!!!

  //scala spec: last char decides associativity of method
  1 :: 2 :: 3 :: List(4, 5) // List(4,5).::(3).::(2).::(1)

  class MyStreams[T] {
    def -->:(value: T): MyStreams[T] = this // implementation
  }

  val myStreams = 1 -->: 2 -->: 3 -->: new MyStreams[Int]

  // #4 multi-word method naming

  class TeenGirl(name: String) {
    def `and then said`(gossip: String): Unit = println(s"$name said $gossip")
  }

  val lilly = new TeenGirl("Lilly")

  lilly `and then said` "Scala is so sweet"

  // #5 infix types

  class Composite[A, B]
  val composite: Int Composite String = ???

  class -->[A, B]
  val towards: Int --> String = ???

  // #6 update()
  val anArray = Array(1, 2, 3)
  anArray(2) = 7 // rewritten to anArray.update(2, 7)
  // used in mutable collections

  // #7 setters
  class Mutable {
    private var internalMember: Int = 0

    def member: Int = internalMember // "getter"

    def member_=(value: Int): Unit =
      internalMember = value
  }

  val aMutableContainer = new Mutable
  aMutableContainer.member = 42
}
