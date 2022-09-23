package lectures.part5

object PathDependentType extends App {
  class Outer {
    class Inner
    object InnerObject
    type InnerType

    def print(i: Inner): Unit = println(i)
    def printGeneral(i: Outer#Inner): Unit = println(i)
  }
  def aMethod: Int = {
    class HelperClass
    type HelperType = String

    44
  }

  // per-instance
  val o = new Outer
  val inner = new o.Inner // is a type

  val oo = new Outer
//  val otherInner: oo.Inner = new o.Inner
  o.print(inner)
//  oo.print(inner)

  // path-dependent types

  // Outer#Inner
  o.printGeneral(inner)
  oo.printGeneral(inner)

  trait Item[K] extends ItemLike {
    override type Key = K
  }
  trait IntItem extends Item[Int]
  trait StringItem extends Item[String]

  trait ItemLike {
    type Key
  }

//  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

//  get[IntItem]("string")
//  get[IntItem](12)
}
