package validation.examples

import cats.data.{NonEmptyChain, Validated}
import cats.data.Validated.*

private object ValidatingWithDerived extends App:
  case class Planet(name: String, diameter: Double, distanceToHostStar: Long)
  // derives Validator

  val validator = Validator.derived[Planet]

  val validPlanet = Planet("Earth", 13000.0, 150000000000L)
  val invalidPlanet = Planet("SomePlanetWithLongName", 13000.0, 100000L)

  // val validator = summon[Validator[Planet]]

  println(validator.validate(validPlanet))
  println(validator.validate(invalidPlanet))

  given nameValidator: Validator[String] = (x: String) =>
    Validated.cond(
      x.length < 10,
      x,
      NonEmptyChain("Planet name should not be long")
    )
  given diameterValidator: Validator[Double] = (x: Double) =>
    Validated.cond(x > 0, x, NonEmptyChain("Diameter should be positive"))

  given distanceInHabitableZoneValidator: Validator[Long] = (x: Long) =>
    Validated.cond(
      x >= 150000000,
      x,
      NonEmptyChain("Distance should be in habitable zone")
    )
