package lectures.part1

object AdvancedPatternMatching extends App {
  val numbers = List(1)
  val description: Unit = numbers match {
    case head :: Nil => println(head)
    case _           =>
  }

  class Person(val name: String, val age: Int)

  object Person {
    def unapply(person: Person): Option[(String, Int)] = Some(
      (person.name, person.age)
    )

    def unapply(age: Int): Option[String] = Some(
      if (age < 21) "minor" else "major"
    )
  }

  val bob = new Person("Bob", 24)
  val greetings = bob match {
    case Person(n, a) => s"Hi, my name is $n and my age is $a"
  }

  println(greetings)

  val legalStatus = bob.age match {
    case Person(status) => s"my legal status is $status"
  }

  println(legalStatus)

  val n = 15

  val mathProperty = n match {
    case x if x < 10     => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _               => "no property"
  }

  object even {
    def unapply(arg: Int): Boolean =
      arg % 2 == 0
  }

  object singleDigit {
    def unapply(arg: Int): Boolean = arg < 10
  }

  val newN = 44
  val numberMatching = newN match {
    case singleDigit() => "I am single digit"
    case even()        => "I am even"
    case _             => "no property"
  }

  println(numberMatching)
}
