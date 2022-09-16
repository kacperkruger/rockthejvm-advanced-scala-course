package lectures.part3

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
  smartProducerConsumer()
}
