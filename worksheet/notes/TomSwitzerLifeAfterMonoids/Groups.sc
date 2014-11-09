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

    def square(d: Double) = d * d

    case class Stats(sum: Double, sumSq: Double, count: Long) {
      def mean = sum / count
      def variance = sumSq / count - square(sum / count)
      def stdDev = math.sqrt(variance)
    }

    object Stats {
      def apply(x: Double): Stats = Stats(x, x * x, 1L)
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

      implicit object DoubleGroup extends Group[Double] {
        def op(a: Double, b: Double): Double = a + b
        def id: Double = 0L
        def inverse(a: Double) = -a
      }

      implicit def tuple2Group[A: Group, B: Group] = {
        val aGroup = implicitly[Group[A]]
        val bGroup = implicitly[Group[B]]
        new Group[(A, B)] {
          def op(x: (A, B), y: (A, B)): (A, B) = (aGroup.op(x._1, y._1), bGroup.op(x._2, y._2))
          def id: (A, B) = (aGroup.id, bGroup.id)
          def inverse(x: (A, B)): (A, B) = (aGroup.inverse(x._1), bGroup.inverse(x._2))
        }
      }

      implicit object StatsGroup extends Group[Stats] {
        def id = Stats(0D, 0D, 0L)

        def op(x: Stats, y: Stats) = Stats(
          x.sum + y.sum,
          x.sumSq + y.sumSq,
          x.count + y.count)

        def inverse(x: Stats) = Stats(-x.sum, -x.sumSq, -x.count)

      }
    }
  }

  object CatalogueLib {

    import lib._

    // log example
    // * We want a "catalogue" (add and remove items; stored in append-only db)
    // * The goal: summarize the items in the catalogue
    // * Can groups help?
    // abstract over the symmetry

    sealed trait Event[A]
    case class Add[A](item: A) extends Event[A]
    case class Remove[A](item: A) extends Event[A]

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

    def count[A](cat: Catalogue[A]): Long =
      cat.reduce0[Long]({
        case Add(_) => 1L
        case Remove(_) => -1L
      })

  }

  import lib._
  import CatalogueLib._

  class CatalogueImpl[A](events: Seq[Event[A]]) extends Catalogue[A] {

    def reduce0[B: Monoid](f: Event[A] => B): B = {
      val monoid = implicitly[Monoid[B]]
      events.foldLeft(monoid.id)((x, y) => monoid.op(x, f(y)))
    }

  }

  case class Movie(title: String, length: Double)

  val movies = new CatalogueImpl[Movie](Vector(
    Add(Movie("foo", 124.5)),
    Add(Movie("a", 102.4)),
    Add(Movie("b", 154)),
    Remove(Movie("foo", 124.5)),
    Add(Movie("c", 100.5)),
    Add(Movie("d", 88.9)),
    Add(Movie("e", 124.5))))                      //> movies  : Groups.CatalogueImpl[Groups.Movie] = Groups$$anonfun$main$1$Catal
                                                  //| ogueImpl$1@16bdb503

  val c = new CatalogueImpl(Vector(Add("foo"), Add("bar"), Remove("foo"), Add("baz")))
                                                  //> c  : Groups.CatalogueImpl[String] = Groups$$anonfun$main$1$CatalogueImpl$1@
                                                  //| b6e39f

  count(c)                                        //> res0: Long = 2

  c.reduce(_ => 1L)                               //> res1: Long = 2

  movies.reduce(_ => 1L)                          //> res2: Long = 5

  movies.reduce(_.length)                         //> res3: Double = 570.3

  val sumCountLength = movies.reduce(m => (1L, m.length))
                                                  //> sumCountLength  : (Long, Double) = (5,570.3)

  sumCountLength._2 / sumCountLength._1           //> res4: Double = 114.05999999999999

  val movieStats = movies.reduce(m => Stats(m.length))
                                                  //> movieStats  : Groups.lib.Stats = Stats(570.3,67705.47,5)

  movieStats.mean                                 //> res5: Double = 114.05999999999999

  movieStats.stdDev                               //> res6: Double = 23.052340445169648

}