package lectures.part1

import scala.annotation.tailrec

object Recap extends App {
  val aCondition: Boolean = false
  val aConditionVal = if (aCondition) 42 else 64

  val aCodeBlock = {
    if (aCondition) 54
    56
  }

  val theUnit: Unit = println("hello")

  def aFunction(x: Int): Int = x + 1

  @tailrec def factorial(n: Int, accumulator: Int): Int =
    if (n <= 0) accumulator
    else factorial(n - 1, accumulator * n)

  class Animal
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  val aCroc = new Crocodile
  aCroc.eat(aDog)
  aCroc eat aDog

  // anonymous class
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  abstract class MyList[+A]
  object MyList

  case class Person(name: String, age: Int)

  val throwsExceptions = throw new RuntimeException
  val aPotentialFailure =
    try {
      throw new RuntimeException
    } catch {
      case e: Exception => "I caught exception"
    } finally {
      println("some logs")
    }

  val incrementer = new Function[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }
  incrementer(1)

  //lambda
  val anonymousIncrementer = (x: Int) => x + 1

  //HOF
  List(1, 2, 3, 4).map(anonymousIncrementer)

  val pairs = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield num + "-" + char

  val aMap = Map(
    "Daniel" -> 789,
    "Jess" -> 244
  )

  val anOption = Some(2)

  val x = 2

  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => s"${x}th"
  }

  val bob = Person("Bob", 22)
  val greetings = bob match {
    case Person(name, _) => s"Hi, my name is $name"
  }
}
