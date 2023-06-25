
Let's consider we need to write a validator for some case class like th following:  

```scala
case class Planet(name: String, diameter: Double, distanceToHostStar: Long)
```

The validator should validate all (or some of) fields and return 