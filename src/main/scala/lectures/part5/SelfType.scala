package lectures.part5

object SelfType extends App {
  trait Instrumentalist {
    def play(): Unit
  }

  trait Singer {
    self: Instrumentalist => // whoever implements Singer to implements Instrumentalist
    def sign(): Unit
  }

  class LeadSinger extends Singer with Instrumentalist {
    override def play(): Unit = ???

    override def sign(): Unit = ???
  }

  //  class Vocalist extends Singer {
  //    override def sign(): Unit = ???
  //  }

  val jamesHetfield = new Singer with Instrumentalist {
    override def sign(): Unit = ???

    override def play(): Unit = ???
  }

  class Guitarist extends Instrumentalist {
    override def play(): Unit = println("(guitar solo)")
  }

  val ericClapton = new Guitarist with Singer {
    override def sign(): Unit = ???
  }

  // vs inheritance
  class A
  class B extends A // B is an A

  trait T
  trait S { self: T => } // S requires T

  // CAKE PATTERN -> "dependency injection"

  // DI
  class Component {
    // API
  }
  class ComponentA extends Component
  class ComponentB extends Component
  class DependentComponent(val component: Component)

  trait ScalaComponent {
    // API
    def action(x: Int): String
  }
  trait ScalaDependentComponent { self: ScalaComponent =>
    def dependentAction(x: Int): String = action(x) + "this rocks!"
  }
  trait ScalaApplication { self: ScalaDependentComponent with ScalaComponent =>
  }

  // layer 1 - small components
  trait Picture extends ScalaComponent
  trait Stats extends ScalaComponent

  // layer 2 - compose
  trait Profile extends ScalaDependentComponent with Picture
  trait Analytics extends ScalaDependentComponent with Stats

  // layer 3 - app
  trait AnalyticApp extends ScalaApplication with Analytics

  // cyclical dependencies
//  class X extends Y
//  class Y extends X

  trait X { self: Y => }
  trait Y { self: X => }

}
