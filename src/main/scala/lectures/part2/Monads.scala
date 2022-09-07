package lectures.part2

object Monads extends App {

  // our own try monad
  trait Attempt[+A] {
    def flatMap[B](f: A => Attempt[B]): Attempt[B]
  }

  object Attempt {
    def apply[A](value: => A): Attempt[A] = try {
      Success(value)
    } catch {
      case err: Throwable => Fail(err)
    }
  }

  case class Success[+A](value: A) extends Attempt[A] {
    override def flatMap[B](f: A => Attempt[B]): Attempt[B] = try {
      f(value)
    } catch {
      case err: Throwable => Fail(err)
    }
  }
  case class Fail(err: Throwable) extends Attempt[Nothing] {
    override def flatMap[B](f: Nothing => Attempt[B]): Attempt[B] = this
  }

  class Lazy[+A](value: => A) {
    private lazy val internalValue = value

    def flatMap[B](f: (=> A) => Lazy[B]): Lazy[B] = f(internalValue)

    def run: () => A = () => value
  }

  object Lazy {
    def apply[A](value: => A): Lazy[A] = new Lazy(value)
  }

  val lazyInst = Lazy({
    println("aaa")
    42
  })

  val flatMapInst = lazyInst.flatMap(x => Lazy(x * 10))
  val flatMapInst2 = lazyInst.flatMap(x => Lazy(x * 10))

  flatMapInst.run()
  flatMapInst2.run()

  println(lazyInst.run())
}
