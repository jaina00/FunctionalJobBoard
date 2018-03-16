package tagless.crossFunctional

trait Logger[F[_]] {
  def info(message: String): F[Unit]
}
