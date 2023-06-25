package validation.examples

import cats.data.ValidatedNec
import validation.examples.MirrorHelper.deriveForValidators
import validation.examples.Validator.ErrorsOr

import scala.deriving.Mirror

object Validator:
  type ErrorsOr[A] = ValidatedNec[String, A]

  inline def derived[A](using m: Mirror.ProductOf[A]): Validator[A] =
    deriveForValidators[A]



trait Validator[A]:
  def validate(x: A): ValidatedNec[String, A]

