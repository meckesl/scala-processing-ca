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
  var gen = Seq[Boolean]()
  var rule = Seq[Boolean]()

  override def settings() {
    size(800, 600)
  }

  override def setup() {
    frameRate(1000);
    background(255, 255, 255)
    noStroke()
    iter = 0
    gen = 1 to width map (_ => Random.nextBoolean)
    rule = 1 to 8 map (_ => Random.nextBoolean)
  }

  override def keyPressed(): Unit = {
    setup()
  }

  override def draw() {

    gen = computeCA(rule, gen)
    gen.zipWithIndex.foreach {
      case (cell, index) => {
        if (cell == true) fill(0) else fill(255)
        rect(index, iter, 1, 1)
      }
    }

    iter = iter + 1
    if (iter == height) setup
  }


  def computeCA(rule: Seq[Boolean], curgen: Seq[Boolean]): Seq[Boolean] = {
    
    def generate: Seq[Boolean] = {

      def nextchild(a: Boolean, b: Boolean, c: Boolean, rule: Seq[Boolean]): Boolean = {

        if (a == true) {
          if (b == true) {
            if (c == true) {
              return rule(0)
            } else {
              return rule(1)
            }
          } else {
            if (c == true) {
              return rule(2)
            } else {
              return rule(3)
            }
          }
        } else {
          if (b == true) {
            if (c == true) {
              return rule(4)
            } else {
              return rule(5)
            }
          } else {
            if (c == true) {
              return rule(6)
            } else {
              return rule(7)
            }
          }
        }

      }

      var newgen = Seq[Boolean]()
      var prev, next = false;
      curgen.zipWithIndex.foreach {
        case (cur, i) => {
          if (i == 0) {
            prev = curgen(curgen.length - 1);
            next = curgen(i + 1)
          } else if (i == curgen.length - 1) {
            next = curgen.head;
            prev = curgen(i - 1)
          } else {
            prev = curgen(i - 1);
            next = curgen(i + 1)
          }
          newgen = newgen :+ nextchild(prev, cur, next, rule)
        }
      }
      newgen
    }

    generate
  }

}