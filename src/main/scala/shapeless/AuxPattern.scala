package shapeless

trait OperationAux[T] {
  type Result1
  type Result2

  def apply(t: T): Result1
}

trait OperationNormal[T, Result1, Result2] {
  def apply(t: T): Result1
}

object AuxPattern {

  val strLenAux = new OperationAux[String] {
    override type Result1 = Int
    override type Result2 = Boolean

    override def apply(t: String) = t.length
  }

  val intIncAux = new OperationAux[Int] {
    type Result = Int

    def apply(t: Int): Result = t + 1
  }

  def applyOpAux[T](t: T)(op: OperationAux[T]): op.Result1 = op.apply(t)

  val strLenNormal = new OperationNormal[String, Int, Boolean] {
    def apply(t: String): Int = t.length
  }
  val intIncAuxNormal = new OperationNormal[Int, Int, Boolean] {
    def apply(t: Int): Int = t + 1
  }

  //Nom Aux way: Here more type info has to be given. even though i don't need all of them
  def applyOpNormal[T, R, Z](t: T)(op: OperationNormal[T, R, Z]): R = op.apply(t)

  println(applyOpNormal("hello")(strLenNormal))


  //Next
  def applyOps[T](t: T)(implicit op: OperationAux[T], op2: OperationAux[op.Result1]): op2.Result1 = op2.apply(op.apply(t))

}
