package processing.model

object CellularAutomata {

  def computeCA(gen: Seq[Boolean], rule: Seq[Boolean]): Seq[Boolean] = {

    implicit def b2i(b: Boolean) = if (b) 1 else 0

    def computeChild(siblings: Seq[Boolean]): Boolean = {
      require(siblings.length.equals(3), "Provide prev, cur, and next cell for parent generation")
      var n = 0
      siblings.foreach { case (s) => n = (n << 1) + s }
      rule(n)
    }

    var newgen = Seq[Boolean]()
    var prev, next = false
    gen.zipWithIndex.foreach {
      case (cur, i) =>
        if (i == 0) {
          prev = gen.reverse.head
          next = gen(i + 1)
        } else if (i == gen.length - 1) {
          next = gen.head
          prev = gen(i - 1)
        } else {
          prev = gen(i - 1)
          next = gen(i + 1)
        }
        newgen = newgen :+ computeChild(Seq(prev, cur, next))
    }

    return newgen

  }

}
