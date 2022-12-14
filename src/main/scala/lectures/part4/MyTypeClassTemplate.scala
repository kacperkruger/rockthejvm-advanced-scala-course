package lectures.part4

trait MyTypeClassTemplate[T] {
  def action(value: T): String
}

object MyTypeClassTemplate {
  def apply[T](value: T)(implicit instance: MyTypeClassTemplate[T]): String =
    instance.action(value)
}
