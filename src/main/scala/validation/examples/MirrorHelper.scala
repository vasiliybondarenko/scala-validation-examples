package validation.examples

import cats.SemigroupK
import cats.data.NonEmptyChain
import cats.data.Validated.Invalid
import cats.syntax.all.*

import scala.compiletime.*
import scala.deriving.*
import scala.deriving.Mirror

object MirrorHelper:
 private inline def getElemLabels[A <: Tuple]: List[String] =
    inline erasedValue[A] match {
      case _: EmptyTuple => Nil
      case _: (head *: tail) =>
        val headElementLabel =
          constValue[head].toString
        val tailElementLabels =
          getElemLabels[tail]
        headElementLabel :: tailElementLabels
    }

 private inline def getTypeClassInstances[A <: Tuple, F[_]]: List[F[Any]] =
    inline erasedValue[A] match {
      case _: EmptyTuple => Nil
      case _: (head *: tail) =>
        val headTypeClass =
          summonInline[F[head]]
        headTypeClass
          .asInstanceOf[F[Any]] :: getTypeClassInstances[tail, F]
    }

 inline def deriveForValidators[A](using
                                    m: Mirror.ProductOf[A]
                                   ): Validator[A] = (x: A) => {
    val elemInstances =
      getTypeClassInstances[m.MirroredElemTypes, Validator]

    val elems = x.asInstanceOf[Product].productIterator
    val elemValidates =
      elems.zip(elemInstances).map { case (elem, instance) =>
        instance.validate(elem)
      }

    val errors =
      SemigroupK[NonEmptyChain].combineAllOptionK(elemValidates.collect {
        case Invalid(e) => e
      })

    errors match
      case Some(errors) => Invalid(errors)
      case None => x.validNec
  }



