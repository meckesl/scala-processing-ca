package processing

import processing.core._

import scala.util.Random

object Scala2dCA {
  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCA"));
  }
}

class RunCA extends PApplet {

  var iter = 0
  var gen, rule = Seq[Boolean]()
  val res = 8

  override def settings() {
    fullScreen()
  }

  override def setup() {
    frameRate(100);
    background(255, 255, 255)
    noStroke()
    iter = 0
    gen = 1 to width/res map (_ => Random.nextBoolean)
    rule = 1 to 8 map (_ => Random.nextBoolean)
  }

  override def keyPressed() {
    setup()
  }

  override def draw() {

    gen = computeCA(rule, gen)
    gen.zipWithIndex.foreach {
      case (cell, index) => {
        if (cell) fill(0) else fill(255)
        rect(index*res, iter*res, res, res)
      }
    }

    iter = iter + 1
    if (iter == height/res) setup
  }


  def computeCA(rule: Seq[Boolean], gen: Seq[Boolean]): Seq[Boolean] = {

    implicit def b2i(b: Boolean) = if (b) 1 else 0

    def computeChild(siblings: Seq[Boolean]): Boolean = {
      require(siblings.length == 3, "Provide prev, cur, and next cell for parent generation")
      var n = 0;
      siblings.foreach {
        case (x) => {
          n = (n << 1) + x
        }
      }
      rule(n)
    }

    var newgen = Seq[Boolean]()
    var prev, next = false;
    gen.zipWithIndex.foreach {
      case (cur, i) => {
        if (i == 0) {
          prev = gen(gen.length - 1);
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