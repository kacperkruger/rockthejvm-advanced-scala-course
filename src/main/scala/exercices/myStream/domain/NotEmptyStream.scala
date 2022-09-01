package exercices.myStream.domain

import exercices.myStream.domain.EmptyStream
import exercices.myStream.domain.MyStream

class NotEmptyStream[+A](headElem: A, tailElems: => MyStream[A])
    extends MyStream[A] {
  def isEmpty: Boolean = false
  override val head: A = headElem
  override lazy val tail: MyStream[A] = tailElems // call by need

  def #::[B >: A](element: B): MyStream[B] =
    new NotEmptyStream[B](element, this)
  def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] =
    new NotEmptyStream[B](head, tail ++ anotherStream)

  def foreach(f: A => Unit): Unit = {
    f(head)
    tail.foreach(f)
  }
  def map[B](f: A => B): MyStream[B] =
    new NotEmptyStream[B](f(head), tail.map(f))
  def flatMap[B](f: A => MyStream[B]): MyStream[B] =
    f(head) ++ tail.flatMap(f)
  def filter(predicate: A => Boolean): MyStream[A] = if (predicate(head))
    new NotEmptyStream[A](head, tail.filter(predicate))
  else tail.filter(predicate)

  def take(n: Int): MyStream[A] =
    if (n <= 0) new EmptyStream
    else new NotEmptyStream[A](head, tail.take(n - 1))
}
