package lectures.part4

object TypeClasses extends App {
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHTML: String =
      s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  val john = User("John", 44, "john@rockthejvm.com")

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String =
      s"<div>${user.name} (${user.age} yo) <a href=${user.email} /> </div>"
  }

  println(UserSerializer.serialize(john))

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

  object HTMLSerializer {
    def serializer[T](value: T)(implicit
        serializer: HTMLSerializer[T]
    ): String = serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]): HTMLSerializer[T] =
      serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value</div>"
  }

  object Equal {
    def apply[T](val1: T, val2: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer(val1, val2)
  }

  println(HTMLSerializer.serializer(44))

  val anotherJohn = User("John", 45, "another.john@rockthejvm.com")
  println(Equal(john, anotherJohn))

  // AD-HOC polymorphism
}
