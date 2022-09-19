package lectures.part4

import exercices.EqualityPlayground.Equal

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

  println(HTMLSerializer.serializer(44))
}
