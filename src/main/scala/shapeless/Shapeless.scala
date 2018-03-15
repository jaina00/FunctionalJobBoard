package shapeless

import shapeless.Generic
import shapeless.syntax.singleton._


class Shapeless extends App {

  case class Vec(x: Int, y: Int)

  case class Rect(origin: Vec, size: Vec)

  def getRepr[A](value: A)(implicit gen: Generic[A]): gen.Repr =
    gen.to(value)

  val t = getRepr(Vec(1, 2))

  val x = getRepr(Rect(Vec(0, 0), Vec(5, 5)))


  val z = 42.narrow

}
