package lectures.part5

import lectures.part5.SelfType.A

class FBoundedPolymorphism extends App {
  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Animal] = ??? // List[Cat]
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Animal] = ??? // List[Dog]
  //  }

  // Solution 1 - naive
  //  trait Animal {
  //    def breed: List[Animal]
  //  }
  //
  //  class Cat extends Animal {
  //    override def breed: List[Cat] = ??? // List[Cat]
  //  }
  //
  //  class Dog extends Animal {
  //    override def breed: List[Cat] = ??? // List[Dog]
  //  }

  // solution 2 - FBD
  //  trait Animal[A <: Animal[A]] { // recursive type: F-Bounded Polymorphism
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat] {
  //    override def breed: List[Animal[Cat]] = ??? // List[Cat]
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ??? // List[Dog]
  //  }
  //
  //  trait Entity[E <: Entity[E]] // ORM
  //  class Person extends Comparable[Person] { // FBP
  //    override def compareTo(o: Person): Int = ???
  //  }
  //
  //  class Crocodile extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!!
  //  }

  // solution 3 - FBP + self-types

  //  trait Animal[A <: Animal[A]] { self: A =>
  //    def breed: List[Animal[A]]
  //  }
  //
  //  class Cat extends Animal[Cat] {
  //    override def breed: List[Animal[Cat]] = ??? // List[Cat]
  //  }
  //
  //  class Dog extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ??? // List[Dog]
  //  }
  //
  //  trait Entity[E <: Entity[E]] // ORM
  //  class Person extends Comparable[Person] { // FBP
  //    override def compareTo(o: Person): Int = ???
  //  }

  //  class Crocodile extends Animal[Dog] {
  //    override def breed: List[Animal[Dog]] = ??? // List[Dog] !!!
  //  }

  //  trait Fish extends Animal[Fish]
  //  class Shark extends Fish {
  //    override def breed: List[Animal[Fish]] = List(new Cod) // wrong
  //  }
  //
  //  class Cod extends Fish {
  //    override def breed: List[Animal[Fish]] = ???
  //  }

  // solution 4 - type classes!
  //  trait Animal
  //  trait CanBreed[A] {
  //    def breed(a: A): List[A]
  //  }
  //
  //  class Dog extends Animal
  //  object Dog {
  //    given CanBreed[Dog] with
  //      override def breed(a: Dog): List[Dog] = List.empty
  //
  //  }
  //
  //  extension [A](animal: A)  {
  //    def breed(using canBreed: CanBreed[A]): List[A] = canBreed.breed(animal)
  //  }
  //
  //  val dog = new Dog
  //  dog.breed

  // solution 5
  trait Animal[A] { // pure type class
    def breed(a: A): List[A]
  }

  class Dog
  object Dog {
    given DogAnimal: Animal[Dog] with {
      override def breed(a: Dog): List[Dog] = List.empty
    }
  }

  extension [A](animal: A) {
    def breed(using animalTypeClassInstance: Animal[A]): List[A] = animalTypeClassInstance.breed(animal)
  }

  val dog = new Dog
  dog.breed

  
}
