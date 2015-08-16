package processing

import processing.core._
import processing.model.CellularAutomata._

import scala.util.Random

object Scala2dCAThreading {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAThreading"))
  }
}

class RunCAThreading extends PApplet {

  val res = 1
  val complexity = 7
  var gen, rule = Vector[Boolean]()
  var screenbuffer = List[Boolean]()
  var screenOffset = 0
  var t = CAThread

  override def settings {
    size(2560, 1440)
    noSmooth
    fullScreen
    initVars
  }

  override def setup {
    frameRate(40)
    noStroke
  }

  override def keyPressed {
    initVars
  }

  def initVars {
    try {
      t.stop
    }
    gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
    rule = (1 to math.pow(2, complexity).toInt map (_ => Random.nextBoolean())).toVector
    screenbuffer = List[Boolean]()
    screenOffset = 0
    t = CAThread
    t.start
  }

  def CAThread = new Thread("CA") {
    override def run {
      while (true) {
        if (screenOffset < screenCellCount) {
          gen = compute(gen, rule, complexity)
          screenbuffer = screenbuffer ++ gen
        }
      }
    }
  }

  def screenCellCount = (width * height) / (res * res)

  override def draw {

    def findline(x: Int) = (screenOffset + x) / (width / res)
    def findcol(y: Int) = (screenOffset + y) % (width / res)

    val todraw = screenbuffer.slice(screenOffset, screenbuffer.length)
    todraw.zipWithIndex.foreach {
      case (cell, index) => {
        if (cell) fill(0) else fill(255)
        rect(findcol(index) * res, findline(index) * res, res, res)
      }
    }

    screenOffset = screenOffset + todraw.length

    println("so: " + screenOffset + " sc: " + screenCellCount)
    if (screenOffset > screenCellCount) {
      t.stop
      screenbuffer = List[Boolean]()
      screenOffset = 0
      t = CAThread
      t.start
    }

  }


}