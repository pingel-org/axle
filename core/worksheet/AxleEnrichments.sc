
object enrichedO {

  import axle._

  true and false

  true ∧ false

  true and true

  true ∧ true

  true or false

  true ∨ false

  true implies false

  true implies true

  Set(1, 2, 3) ∃ (_ % 2 == 0)

  List(1, 2, 3) ∀ (_ % 2 == 0)

  (1 to 10) Π((i: Int) => { () => i * 3 }) // TODO: clean this up

  (1 to 10) Σ (_ * 2)

  List(1, 2, 3).doubles

  Set(1, 2, 3).triples

  (List(1, 2, 3) ⨯ List(4, 5, 6)).toList

}
