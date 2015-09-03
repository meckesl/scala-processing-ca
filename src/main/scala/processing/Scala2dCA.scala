package processing

import processing.core._
import processing.model.CellularAutomata._

import scala.util.Random

object Scala2dCA {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCA"))
  }

}

class RunCA extends PApplet {

  var gen, rule = Vector[Boolean]()
  var iter = 0
  val res = 16

  override def settings {
    fullScreen
  }

  override def setup {
    iter = 0
    gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
    rule = (1 to 8 map (_ => Random.nextBoolean())).toVector
  }

  override def keyPressed {
    setup
  }

  override def draw {

    gen = compute(gen, rule, 3).toVector
    gen.zipWithIndex.foreach {
      case (cell, index) =>
        if (cell) fill(0) else fill(255)
        rect(index * res, iter * res, res, res)
    }

    iter = iter + 1
    if (iter == height / res) setup
  }

}

