object UnweightedDocumentVectorSpaceDemo {

  println("Unweighted Document Vector Space")     //> Unweighted Document Vector Space

  import axle.lx._

  val strings = Vector(
    "a tall drink of water",
    "the tall dog drinks the water",
    "a quick brown fox jumps the other fox",
    "the lazy dog drinks",
    "the quick brown fox jumps over the lazy dog",
    "the fox and the dog are tall",
    "a fox and a dog are tall",
    "lorem ipsum dolor sit amet"
  )                                               //> strings  : scala.collection.immutable.Vector[String] = Vector(a tall drink o
                                                  //| f water, the tall dog drinks the water, a quick brown fox jumps the other fo
                                                  //| x, the lazy dog drinks, the quick brown fox jumps over the lazy dog, the fox
                                                  //|  and the dog are tall, a fox and a dog are tall, lorem ipsum dolor sit amet)
                                                  //| 

  val stopWords = Set("the", "a", "of", "and")    //> stopWords  : scala.collection.immutable.Set[String] = Set(the, a, of, and)

  val uwDVS = new UnweightedDocumentVectorSpace(stopWords, () => strings.iterator)
                                                  //> uwDVS  : axle.lx.UnweightedDocumentVectorSpace = axle.lx.UnweightedDocumentV
                                                  //| ectorSpace@ed42d08

  val v1 = uwDVS.doc2vector(strings(1))           //> v1  : UnweightedDocumentVectorSpaceDemo.uwDVS.TermVector = Map(dog -> 1, dri
                                                  //| nks -> 1, tall -> 1, water -> 1)

  val v2 = uwDVS.doc2vector(strings(2))           //> v2  : UnweightedDocumentVectorSpaceDemo.uwDVS.TermVector = Map(brown -> 1, q
                                                  //| uick -> 1, jumps -> 1, fox -> 2, other -> 1)

  uwDVS.space                                     //> res0: spire.algebra.NormedInnerProductSpace[UnweightedDocumentVectorSpaceDem
                                                  //| o.uwDVS.TermVector,Double]{def nroot: spire.algebra.NRoot.DoubleIsNRoot.type
                                                  //| ; val _innerProductSpace: spire.algebra.InnerProductSpace[UnweightedDocument
                                                  //| VectorSpaceDemo.uwDVS.TermVector,Double]{def zero: scala.collection.immutabl
                                                  //| e.Map[String,Nothing]; implicit def scalar: spire.algebra.Field.DoubleIsFiel
                                                  //| d.type}; def space(): spire.algebra.InnerProductSpace[UnweightedDocumentVect
                                                  //| orSpaceDemo.uwDVS.TermVector,Double]{def zero: scala.collection.immutable.Ma
                                                  //| p[String,Nothing]; implicit def scalar: spire.algebra.Field.DoubleIsField.ty
                                                  //| pe}} = axle.lx.UnweightedDocumentVectorSpace$$anon$1@7cb25b9d

  uwDVS.space.distance(v1, v2)                    //> res1: Double = 3.4641016151377544

  uwDVS.space.distance(v1, v1)                    //> res2: Double = 0.0

  import axle.algebra._
  
  val m = uwDVS.space.distanceMatrix(uwDVS.vectors)
                                                  //> m  : axle.matrix.JblasMatrixModule.Matrix[Double] = 0.000000 1.732051 3.3166
                                                  //| 25 2.449490 3.162278 2.236068 2.236068 2.828427
                                                  //| 1.732051 0.000000 3.464102 1.732051 3.000000 2.000000 2.000000 3.000000
                                                  //| 3.316625 3.464102 0.000000 3.316625 2.236068 2.828427 2.828427 3.605551
                                                  //| 2.449490 1.732051 3.316625 0.000000 2.449490 2.236068 2.236068 2.828427
                                                  //| 3.162278 3.000000 2.236068 2.449490 0.000000 2.645751 2.645751 3.464102
                                                  //| 2.236068 2.000000 2.828427 2.236068 2.645751 0.000000 0.000000 3.000000
                                                  //| 2.236068 2.000000 2.828427 2.236068 2.645751 0.000000 0.000000 3.000000
                                                  //| 2.828427 3.000000 3.605551 2.828427 3.464102 3.000000 3.000000 0.000000

  m.max                                           //> res3: Double = 3.605551275463989


  val tfidf = new TFIDFDocumentVectorSpace(stopWords, () => strings.iterator)
                                                  //> tfidf  : axle.lx.TFIDFDocumentVectorSpace = axle.lx.TFIDFDocumentVectorSpace
                                                  //| @46c837cd

  val tfidfV1 = tfidf.doc2vector(strings(1))      //> tfidfV1  : UnweightedDocumentVectorSpaceDemo.tfidf.TermVector = Map(dog -> 1
                                                  //| , drinks -> 1, tall -> 1, water -> 1)

  val tfidfV2 = tfidf.doc2vector(strings(2))      //> tfidfV2  : UnweightedDocumentVectorSpaceDemo.tfidf.TermVector = Map(brown ->
                                                  //|  1, quick -> 1, jumps -> 1, fox -> 2, other -> 1)

  tfidf.space                                     //> res4: spire.algebra.NormedInnerProductSpace[UnweightedDocumentVectorSpaceDem
                                                  //| o.tfidf.TermVector,Double]{def nroot: spire.algebra.NRoot.DoubleIsNRoot.type
                                                  //| ; val _innerProductSpace: spire.algebra.InnerProductSpace[UnweightedDocument
                                                  //| VectorSpaceDemo.tfidf.TermVector,Double]{def zero: scala.collection.immutabl
                                                  //| e.Map[String,Nothing]; implicit def scalar: spire.algebra.Field.DoubleIsFiel
                                                  //| d.type; def termWeight(term: String,doc: UnweightedDocumentVectorSpaceDemo.t
                                                  //| fidf.TermVector): Double}; def space(): spire.algebra.InnerProductSpace[Unwe
                                                  //| ightedDocumentVectorSpaceDemo.tfidf.TermVector,Double]{def zero: scala.colle
                                                  //| ction.immutable.Map[String,Nothing]; implicit def scalar: spire.algebra.Fiel
                                                  //| d.DoubleIsField.type; def termWeight(term: String,doc: UnweightedDocumentVec
                                                  //| torSpaceDemo.tfidf.TermVector): Double}} = axle.lx.TFIDFDocumentVectorSpace$
                                                  //| $anon$1@2c79809

  tfidf.space.distance(tfidfV1, tfidfV2)          //> res5: Double = 4.068944074907273

  tfidf.space.distance(tfidfV1, tfidfV1)          //> res6: Double = 0.0

  val tfidfMatrix = tfidf.space.distanceMatrix(tfidf.vectors)
                                                  //> tfidfMatrix  : axle.matrix.JblasMatrixModule.Matrix[Double] = 0.000000 2.54
                                                  //| 2989 4.328703 3.284946 4.408971 2.978096 2.978096 5.324165
                                                  //| 2.542989 0.000000 4.068944 2.079442 4.100714 2.499178 2.499178 5.115209
                                                  //| 4.328703 4.068944 0.000000 4.009470 3.357279 3.632236 3.632236 5.799285
                                                  //| 3.284946 2.079442 4.009470 0.000000 3.534371 2.593519 2.593519 5.068029
                                                  //| 4.408971 4.100714 3.357279 3.534371 0.000000 3.796523 3.796523 5.859443
                                                  //| 2.978096 2.499178 3.632236 2.593519 3.796523 0.000000 0.000000 4.972324
                                                  //| 2.978096 2.499178 3.632236 2.593519 3.796523 0.000000 0.000000 4.972324
                                                  //| 5.324165 5.115209 5.799285 5.068029 5.859443 4.972324 4.972324 0.000000

  tfidfMatrix.max                                 //> res7: Double = 5.859442584384325

  // TODO Triangle Inequality for tfidf.space
}