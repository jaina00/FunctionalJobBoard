package tagless

import java.util.UUID

import cats.Id
import org.scalatest.WordSpec
import org.scalatest.Matchers._

class TaglessAppTest extends WordSpec {

  object IdInterpreter extends UserRepositoryAlgebra[Id] {
    override def findUser(id: String): Id[Option[User]] = {
      id match {
        case "" => None
        case "12345" => Some(User("1234", loyaltyPoints = 100))
        case "abc" => throw new RuntimeException("oops something went wrong")
      }
    }
  }

  val loyaltyPoints = new LoyaltyPoints(IdInterpreter)

  "Loyalty Points" should {
    "return user not found if no userid is sent" in {
      loyaltyPoints.addPoints("", 10) shouldBe Left("User not found")
    }

    "update user's loyalty points" in {
      loyaltyPoints.addPoints("12345", 10) shouldBe Right(User("1234", 110))
    }
  }
}
