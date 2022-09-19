package lectures.part4

import exercices.EqualityPlayground.Equal

object TypeClasses extends App {
  trait HTMLWritable {
    def toHTML: String
  }

  case class User(name: String, age: Int, email: String)

  val john = User("John", 44, "john@rockthejvm.com")

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
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

  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)
  }
  println(john.toHTML)
  println(2.toHTML)

  def htmlBoilerplate[T](content: T)(implicit
      serializer: HTMLSerializer[T]
  ): String = s"<html><body> ${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializer = implicitly[HTMLSerializer[T]]
    s"<html><body> ${content.toHTML(serializer)}</body></html>"
  }

}
