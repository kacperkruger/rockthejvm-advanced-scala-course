package lectures.part5

object TypeMembers extends App {
  class Animal
  class Dog extends Animal
  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // must extends animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val ac = new AnimalCollection
  val dog: ac.AnimalType = ???

//    val cat: ac.BoundedAnimal = new Cat

  val pup: ac.SuperBoundedAnimal = new Dog

  class Dogg extends Dog

  val pup2: ac.SuperBoundedAnimal = new Dogg
  val cat: ac.AnimalC = new Cat

  type CatAlies = Cat
  val anotherCat: CatAlies = new Cat

  // alternative to generic
  trait MyList {
    type T
    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int
    override def add(element: Int): MyList = ???
  }

  // .type
  type CatsType = cat.type
//  new CatsType

  trait MList {
    type A
    def head: A
    def tail: MList
  }

  trait ApplicableToNumber {
    type A <: Number
  }

//  class StringList(hd: String, tl: StringList) extends MList with ApplicableToNumber {
//    override type A = String
//
//    override def head: String = hd
//
//    override def tail: MList = tl
//  }

  class IntegerList(hd: Integer, tl: IntegerList)
      extends MList
      with ApplicableToNumber {
    override type A = Integer

    override def head: Integer = hd

    override def tail: MList = tl
  }
}
