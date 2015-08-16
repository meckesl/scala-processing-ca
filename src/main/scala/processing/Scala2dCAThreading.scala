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

  val res = 2
  val complexity = 3
  var gen, rule = Vector[Boolean]()
  var screenbuffer = Vector[Boolean]()
  var screenOffset = 0
  var t = CAThread

  override def settings {
    size(2560, 1440)
    noSmooth
    fullScreen
    initVars

    println("---- CA simulation starts ----")
    println("Screen resolution: " + width + "*" + height)
    println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
    println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
    println("Rule definition size: " + rule.length + "bit")
    println("Total of possible rules: " + math.pow(2, rule.length).toLong)
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
    screenbuffer = Vector[Boolean]()
    screenOffset = 0
    t = CAThread
    t.start
    println("---- New rule starts ----")
    println("Rule ID: " + rule.map { case (x) => if (x) 1 else 0 }.mkString)
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

    if (screenOffset > screenCellCount) {
      t.stop
      screenbuffer = Vector[Boolean]()
      screenOffset = 0
      t = CAThread
      t.start
    }
  }
}