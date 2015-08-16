package processing

import processing.core._
import processing.model.CellularAutomata._

import scala.util.Random

object Scala2dCAThreading {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAThreading"))
  }

  private class RunCAThreading extends PApplet {

    val res = 2
    var gen, rule = Seq[Boolean]()
    var screen = Seq[Boolean]()
    var screenOffset = 0
    var t = CAThread

    override def settings {
      size(2560, 1440)
      noSmooth
      fullScreen
      initVars
    }

    override def setup {
      frameRate(20)
      noStroke
    }

    override def keyPressed {
      initVars
    }

    def initVars {
      try {
        t.stop
      }
      gen = 1 to width / res map (_ => Random.nextBoolean())
      rule = 1 to math.pow(2, 3).toInt map (_ => Random.nextBoolean())
      screen = Seq[Boolean]()
      screenOffset = 0
      t = CAThread
      t.start
    }

    def CAThread = new Thread("CA") {
      override def run {
        while (true) {
          if (screen.length < screenCellCount) {
            gen = compute(gen, rule)
            screen = screen ++ gen
          }
        }
      }
    }

    def screenCellCount = (width * height) / (res * res)

    override def draw {

      def findline(x: Int) = (screenOffset + x) / (width / res)
      def findcol(x: Int) = (screenOffset + x) % (width / res)

      val todraw = screen.slice(screenOffset, screen.length)
      todraw.zipWithIndex.foreach {
        case (cell, index) => {
          val curline = findline(index)
          val curcol = findcol(index)
          if (cell) fill(0) else fill(255)
          rect(curcol * res, curline * res, res, res)
        }
      }

      screenOffset = screenOffset + todraw.length
      if (screenOffset > screenCellCount) initVars

    }

  }

}