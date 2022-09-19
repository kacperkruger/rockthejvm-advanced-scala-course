package exercices

import lectures.part4.TypeClasses.User

object EqualityPlayground extends App {
  trait Equal[T] {
    def apply(el1: T, el2: T): Boolean
  }

  implicit object UserNameEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean =
      user1.name == user2.name
  }

  object UserFullEquality extends Equal[User] {
    override def apply(user1: User, user2: User): Boolean =
      user1.name == user2.name && user1.email == user2.email
  }

  object Equal {
    def apply[T](val1: T, val2: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer(val1, val2)
  }

  val anotherJohn = User("John", 45, "another.john@rockthejvm.com")
  val john = User("John", 44, "john@rockthejvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism
}
