package processing

import processing.core._

import scala.util.Random

import model.CellularAutomata._

object Scala2dCA {
  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCA"))
  }
}

class RunCA extends PApplet {

  var gen, rule = Seq[Boolean]()
  var screen = Seq[Boolean]()
  var iter = 0
  val res = 16

  override def settings {
    fullScreen
  }

  override def setup {
    iter = 0
    gen = 1 to width / res map (_ => Random.nextBoolean())
    rule = 1 to 8 map (_ => Random.nextBoolean())
  }

  override def keyPressed {
    setup
  }

  override def draw {

    gen = computeCA(gen, rule)
    screen +: gen
    gen.zipWithIndex.foreach {
      case (cell, index) =>
        if (cell) fill(0) else fill(255)
        rect(index * res, iter * res, res, res)
    }

    iter = iter + 1
    if (iter == height / res) setup
  }



}