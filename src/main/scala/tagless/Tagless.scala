package tagless

import cats.Monad
import cats.data.EitherT
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class User(id: String = "", loyaltyPoints: Int = 0)

trait UserRepositoryAlgebra[F[_]] {
  //def findUser(id: String): F[EitherString[User]]
  def findUser(id: String): EitherT[F, BusinessError, User]
}

class LoyaltyPoints[F[_] : Monad](ur: UserRepositoryAlgebra[F]) {
  def addPoints(userId: String, pointsToAdd: Int): EitherT[F, BusinessError, User] = {
    for {
      user <- ur.findUser(userId)
    } yield user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)
  }
}

object FutureInterpreter extends UserRepositoryAlgebra[Future] {
  override def findUser(id: String): EitherT[Future, BusinessError, User] = EitherT[Future, BusinessError, User] {
    Future.successful(Right(User()))
  }
}


object TaglessApp extends App {
  val resultProd: EitherT[Future, BusinessError, User] =
    new LoyaltyPoints(FutureInterpreter).addPoints("", 10)
  println(">>>>>>>>>>>>" + Await.result(resultProd.value, Duration.Inf))
}


