package lectures.part1

import lectures.part1.AdvancedPatternMatching.Person

object AdvancedPatterMatching2 extends App {
  val numbers = List(1, 2, 3)

  case class Or[A, B](a: A, b: B)
  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }

  println(humanDescription)

  // decomposing sequences
  val varargs = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???
    def tail: MyList[A] = ???
  }
  case object Empty extends MyList[Nothing]
  case class Cons[+A](override val head: A, override val tail: MyList[A])
      extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] = if (list == Empty)
      Some(Seq.empty)
    else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Cons(4, Empty))))
  val decompose = myList match {
    case MyList(1, 2, 3, 4, _*) => "starting 1 2 3 4"
    case _                      => "something else"
  }

  println(decompose)

  // custom return types

  abstract class Wrapper[T] {
    def isEmpty: Boolean
    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false

      override def get: String = person.name
    }
  }

  val bob = Person("Bob", 24)

  println(bob match {
    case PersonWrapper(n) => s"This person name is $n"
    case _                => "An alien "
  })
}
