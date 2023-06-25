package validation.examples

import cats.syntax.all.*
import cats.{Invariant, Semigroupal}
import validation.examples.ValidatingWithDerived.given
import validation.examples.Validator.ErrorsOr

object ValidatingWithApplicative extends App:
  case class Planet(name: String, diameter: Double, distanceToHostStar: Long)

  val planetValidator: Validator[Planet] = (
    nameValidator,
    diameterValidator,
    distanceInHabitableZoneValidator
  ).imapN(Planet.apply)(Tuple.fromProductTyped(_))

  given invariant: Invariant[Validator] with
    override def imap[A, B](fa: Validator[A])(f: A => B)(g: B => A) =
      (x: B) => fa.validate(g(x)).map(f)


  given semigroupalValidator: Semigroupal[Validator] with
    override def product[A, B](fa: Validator[A], fb: Validator[B]) =
      (x: (A, B)) => Semigroupal[ErrorsOr].product(fa.validate(x._1), fb.validate(x._2))