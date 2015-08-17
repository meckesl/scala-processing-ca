package processing.model

object CellularAutomata {

  def compute(gen: Vector[Boolean], rule: Vector[Boolean], complexity: Int): Vector[Boolean] = {

    implicit def b2i(b: Boolean) = if (b) 1 else 0

    def child(siblings: Seq[Boolean]): Boolean = {
      require(siblings.length.equals(complexity), "Provide " + complexity + " cells for child generation")
      var n = 0
      siblings.foreach { case (s) => n = (n << 1) + s }
      rule(n)
    }


    def findPrev(index: Int, len: Int) : List[Boolean] = {
      var ret = List[Boolean]()
      (1 to len).foreach {
        case (x) => {
          var ni = index - x
          if (ni < 0) ni = gen.length + ni
          ret = ret ++ List(gen(ni))
        }
      }
      ret
    }

    def findNext(index: Int, len: Int) : List[Boolean] = {
      var ret = List[Boolean]()
      (1 to len).foreach {
        case (x) => {
          var ni = index + x
          if (ni > gen.length - 1) ni = ni - gen.length
          ret = ret ++ List(gen(ni))
        }
      }
      ret
    }

    var newgen = Vector[Boolean]()
    var prev, next  = List[Boolean]()

    gen.zipWithIndex.foreach {
      case (cur, i) =>
        prev = findPrev(i, (complexity-1)/2)
        next = findNext(i, (complexity-1)/2)
        newgen :+= child(prev ::: List(cur) ::: next)
    }

    return newgen

  }

}
