package tagless.domain

import cats.Monad
import cats.implicits._

import scala.concurrent.Future

case class JobSeeker(name: String, emailAddress: String, address: Option[String])
case class JobPost(title: String, desc: String, salary: Int, city: String)

trait Logger[F[_]] {
  def info(message: String): F[Unit]
}

trait JobSeekerAlgebra[F[_]] {
  def searchJobs(searchString: String): F[List[JobPost]]
  def applyJob(jobPost: JobPost): F[Boolean]
}

class SearchJobs[F[_] : Monad](jsa: JobSeekerAlgebra[F], logger: Logger[F]) {
  def searchJobs(searchString: String): F[List[JobPost]] = {
    for {
      _ <- logger.info(s"Search performed of criteria : $searchString")
      jobs <- jsa.searchJobs(searchString)
      _ <- logger.info(s"Found : ${jobs.size}")
    } yield jobs
  }
}

class JobSeekerAlgebraFutureIntrepeter extends JobSeekerAlgebra[Future] {
  override def searchJobs(searchString: String): Future[List[JobPost]] = Future.successful(List.empty)
  override def applyJob(jobPost: JobPost): Future[Boolean] = Future.successful(false)
}