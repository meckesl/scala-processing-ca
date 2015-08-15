package processing

import processing.core._

import scala.util.Random

object Scala2dCA {
  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCA"));
  }
}

class RunCA extends PApplet {

  var gen, rule = Seq[Boolean]()
  var iter = 0
  val res = 16

  override def settings() {
    fullScreen()
  }

  override def setup() {
    iter = 0
    gen = 1 to width / res map (_ => Random.nextBoolean)
    rule = 1 to 8 map (_ => Random.nextBoolean)
    frameRate(100);
  }

  override def keyPressed() {
    setup()
  }

  override def draw() {

    gen = computeCA(gen, rule)
    gen.zipWithIndex.foreach {
      case (cell, index) => {
        if (cell) fill(0) else fill(255)
        rect(index * res, iter * res, res, res)
      }
    }

    iter = iter + 1
    if (iter == height / res) setup
  }


  def computeCA(gen: Seq[Boolean], rule: Seq[Boolean]): Seq[Boolean] = {

    implicit def b2i(b: Boolean) = if (b) 1 else 0

    def computeChild(siblings: Seq[Boolean]): Boolean = {
      require(siblings.length.equals(3), "Provide prev, cur, and next cell for parent generation")
      var n = 0;
      siblings.foreach {
        case (x) => {
          n = (n << 1) + x
        }
      }
      rule(n)
    }

    var newgen = Seq[Boolean]()
    var prev, next = false
    gen.zipWithIndex.foreach {
      case (cur, i) => {
        if (i == 0) {
          prev = gen.reverse.head;
          next = gen(i + 1)
        } else if (i == gen.length - 1) {
          next = gen.head;
          prev = gen(i - 1)
        } else {
          prev = gen(i - 1);
          next = gen(i + 1)
        }
        newgen = newgen :+ computeChild(Seq(prev, cur, next))
      }
    }
    newgen

  }

}