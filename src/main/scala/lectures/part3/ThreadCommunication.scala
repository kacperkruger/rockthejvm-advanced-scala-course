package lectures.part3

import sun.jvm.hotspot.runtime.PerfMemory.capacity

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /*
      the producer-consumer problem

      producer -> [ ? ] -> consumer
   */

  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def get: Int = {
      val result = value
      value = 0
      result
    }
    def set(newValue: Int): Unit = value = newValue
  }

  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer
    val consumer = new Thread(() => {
      println("consumer is waiting")
      while (container.isEmpty) {
        println("consumer is still waiting")
      }

      println(s"consumer has consumed ${container.get}")
    })
    val producer = new Thread(() => {
      println("producer is computing")
      Thread.sleep(500)
      val value = 44
      println(s"producer has produced $value")
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("consumer is waiting")
      container.synchronized {
        container.wait()
      }
      println(s"consumer has consumed ${container.get}")
    })

    val producer = new Thread(() => {
      println("producer is working")
      Thread.sleep(1500)
      val value = 44
      container.synchronized {
        container.set(value)
        println(s"producer has produced $value")
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

//  naiveProducerConsumer()
//  smartProducerConsumer()

  /*
    producer -> [ ? ? ? ] -> consumer
   */

  def prodConsLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting")
            buffer.wait()
          }
          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer] i consumed $x")
          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting")
            buffer.wait()
          }
          // there must be at least ONE EMPTY SPACE in the buffer
          println(s"[producer] producing $i")
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }
        Thread.sleep(random.nextInt(250))
      }
    })
    consumer.start()
    producer.start()
  }

//  prodConsLargeBuffer()

  /*
  Prod cons, level 3

      producer1 -> [ ? ? ? ] -> consumer1
      producer2 ----^     ^--- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()

      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer $id] buffer empty, waiting")
            buffer.wait()
          }
          // there must be at least ONE value in the buffer
          val x = buffer.dequeue()
          println(s"[consumer $id] i consumed $x")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class ProducerFactory

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int)
      extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer $id] buffer is full, waiting")
            buffer.wait()
          }
          println(s"[producer $id] producing $i")
          buffer.enqueue(i)

          buffer.notify()

          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdConsBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]()
    val capacity = 6

    val consumer1 = new Consumer(1, buffer)
    val consumer2 = new Consumer(2, buffer)

    val producer1 = new Producer(1, buffer, capacity)
    val producer2 = new Producer(2, buffer, capacity)

    consumer1.start()
    consumer2.start()

    producer1.start()
    producer2.start()
  }

//  multiProdConsBuffer()

  def testNotifyAll(): Unit = {
    val bell = new Object

    (1 to 10).foreach(i =>
      new Thread(() => {
        bell.synchronized {
          println(s"[thread $i] waiting")
          bell.wait()
          println(s"[thread $i] hooray!")
        }
      }).start()
    )

    new Thread(() => {
      Thread.sleep(2000)
      bell.synchronized {
        println("[announcer] start")
        bell.notifyAll()
      }
    }).start()
  }

//  testNotifyAll()

  case class Friend(name: String) {
    def bow(other: Friend): Unit = {
      this.synchronized {
        println(s"[$this.name] i'm bowing to my friend $other")
        other.rise(this)
        println(s"[$this.name] my friend $other has risen")
      }
    }

    def rise(other: Friend): Unit = {
      this.synchronized {
        println(s"[$this.name] i am rising to my friend $other")
      }
    }

    var side = "right"
    def switchSide(): Unit = {
      if (side == "right") side = "left"
      else side = "right"
    }

    def pass(other: Friend): Unit = {
      while (this.side == other.side) {
        println(s"[$this.name] Oh, $other, feel free to pass")
        switchSide()
        Thread.sleep(1000)
      }
    }
  }

  val sam = Friend("Sam")
  val pierre = Friend("Pierre")

  // deadlock

//  new Thread(() => sam.bow(pierre)).start() // sam's lock, then pierre's lock
//  new Thread(() => pierre.bow(sam)).start() // pierre's lock, then sam's lock

  // livelock

  new Thread(() => sam.pass(pierre)).start()
  new Thread(() => pierre.pass(sam)).start()
}
