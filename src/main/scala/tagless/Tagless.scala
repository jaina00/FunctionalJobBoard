package tagless

import cats.Monad
import cats.implicits._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

case class User(id: String = "", loyaltyPoints: Int = 0)

trait UserRepositoryAlgebra[F[_]] {
  def findUser(id: String): F[Option[User]]
}

class LoyaltyPoints[F[_] : Monad](ur: UserRepositoryAlgebra[F]) {
  def addPoints(userId: String, pointsToAdd: Int): F[Either[String, User]] = {
    ur.findUser(userId).flatMap {
      case None => implicitly[Monad[F]].pure(Left("User not found"))
      case Some(user) =>
        implicitly[Monad[F]].pure(
          Right(user.copy(loyaltyPoints = user.loyaltyPoints + pointsToAdd)))
    }
  }
}

object FutureInterpreter extends UserRepositoryAlgebra[Future] {
  override def findUser(id: String): Future[Option[User]] =
    Future.successful(None) /* go and talk to a database */
}


object TaglessApp extends App {
  val resultProd: Future[Either[String, User]] =
    new LoyaltyPoints(FutureInterpreter).addPoints("", 10)

  println(">>>>>>>>>>>>" + Await.result(resultProd, Duration.Inf))
}


