object Groups {

  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  object lib {

    trait Monoid[A] {
      def id: A
      def op(x: A, y: A): A
    }

    object Monoid {

      implicit object LongMonoid extends Monoid[Long] {
        def op(a: Long, b: Long): Long = a + b
        def id: Long = 0L
      }
    }

    // Group = monoid with symmetry!

    // a |+| a.inverse = a.inverse |+| a == 0

    // Examples:
    // addition with Int, Double, BigInt, etc
    // Multiplication with Double, BigDecimal, etc
    // Set of permutations
    // Rubik's cube

    trait Group[A] extends Monoid[A] {
      def inverse(a: A): A
    }

    object Group {

      implicit object LongGroup extends Group[Long] {
        def op(a: Long, b: Long): Long = a + b
        def id: Long = 0L
        def inverse(a: Long) = -a
      }
    }

    // log example
    // * We want a "catalogue" (add and remove items; stored in append-only db)
    // * The goal: summarize the items in the catalogue
    // * Can groups help?
    // abstract over the symmetry

    trait Catalogue[A] {

      def reduce0[B: Monoid](f: Event[A] => B): B

      def reduce[B: Group](f: A => B): B = {
        val bGroup = implicitly[Group[B]]
        reduce0({
          case Add(a) => f(a)
          case Remove(a) => bGroup.inverse(f(a))
        })
      }

    }

    sealed trait Event[A]
    case class Add[A](item: A) extends Event[A]
    case class Remove[A](item: A) extends Event[A]

    def count[A](cat: Catalogue[A]): Long =
      cat.reduce0[Long]({
        case Add(_) => 1L
        case Remove(_) => -1L
      })

    def countUsingGroup[A](cat: Catalogue[A]): Long =
      cat.reduce(_ => 1L)

  }

  import lib._

  class CatalogueImpl[A](events: Seq[Event[A]]) extends Catalogue[A] {

    def reduce0[B: Monoid](f: Event[A] => B): B = {
      val monoid = implicitly[Monoid[B]]
      events.foldLeft(monoid.id)((x, y) => monoid.op(x, f(y)))
    }

  }

  val c = new CatalogueImpl(Vector(Add("foo"), Add("bar"), Remove("foo"), Add("baz")))
                                                  //> c  : Groups.CatalogueImpl[String] = Groups$$anonfun$main$1$CatalogueImpl$1@
                                                  //| 4ce2cb55

  count(c)                                        //> res0: Long = 2

  countUsingGroup(c)                              //> res1: Long = 2

}