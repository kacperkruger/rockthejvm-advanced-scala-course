package lectures.part4

import scala.concurrent.Future

object MagnetPattern extends App {

  class P2PRequest
  class P2PResponse
  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int
    def receive(request: P2PRequest): Int
    def receive(response: P2PResponse): Int
    def receive[T: Serializer](message: T): Int
    def receive[T: Serializer](message: T, statusCode: Int): Int
    def receive(future: Future[P2PRequest]): Int
  }

  // 1 - type erasure
  // 2 - lifting doesn't work
  // val receiveFV = receive _ // ?!
  // 3 - code duplication
  // 4 - type inference and default args

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet()

  implicit class FromP2PRequest(request: P2PRequest)
      extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PRequest
      println("Handling P2P Request")
      44
    }
  }

  implicit class FromP2PResponse(response: P2PResponse)
      extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic for handling P2PResponse
      println("Handling P2P Response")
      45
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1 - no more erasure problems

  // 2 - lifting works
  trait MathLib {
    def add1(x: Int): Int = x + 1
    def add1(x: String): Int = x.toInt + 1
    // add1 overloads
  }

  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(s: String) extends AddMagnet {
    override def apply(): Int = s.toInt + 1
  }

  val addFV = add1 _
  println(addFV(1))
  println(addFV("4"))
//  addFV(println("do i work correctly?"))

  implicit class AddSideEffect(f: => Unit) extends AddMagnet {
    override def apply(): Int = {
      f
      0
    }
  }
  addFV(println("do i work correctly?"))

  class Handler {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet): Unit = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMehod(): String = {
    println("heelo scala")
    "hahah"
  }

//  handle(sideEffectMehod())
  handle {
    println("heelo scala")
    "hahah"
  }
}
