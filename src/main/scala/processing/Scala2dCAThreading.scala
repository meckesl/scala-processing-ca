package processing

import processing.core._
import processing.awt._
import processing.model.CellularAutomata._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object Scala2dCAThreading {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAThreading"))
  }
}

class RunCAThreading extends PApplet {

  val res = 1
  val complexity = 3
  var screenbuffer = ArrayBuffer[Boolean]()
  var screenOffset = 0

  def screenCellCount = (width * height) / (res * res)

  val t = new Thread() {

    var rule, gen = Vector[Boolean]()
    var pause = false

    def init {
      pause = true
      rule = (1 to math.pow(2, complexity).toInt map (_ => Random.nextBoolean())).toVector
      gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
      screenbuffer.clear
      screenOffset = 0
      println("---- New rule starts ----")
      println("Rule ID: " + rule.map { case (b) => if (b) 1 else 0 }.mkString)
      pause = false
    }

    override def run {
      while (pause == false) {
        gen = compute(gen, rule, complexity)
        screenbuffer.appendAll(gen)
      }
      run
    }
  }

  override def settings {
    size(2560, 1440)//, "processing.opengl.PGraphics2D")
    fullScreen
  }

  override def setup {
    frameRate(25)
    noStroke
    noSmooth


    t.init
    t.start
    println("---- CA simulation starts ----")
    println("Screen resolution: " + width + "*" + height)
    println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
    println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
    println("Rule definition size: " + math.pow(2, complexity).toInt + "bit")
    println("Total of possible rules: " + math.pow(2, math.pow(2, complexity)).toLong)
  }

  override def keyPressed {
    t.init
  }

  /*def initVars {

    // Interesting rules
    // 00111011
    // 01110100110111001000010011011010, 01010011100000011110110000110101
    // 11011111010100001011001010101110100111000011001110010110101010101101110000101101101000111011101101001001110100111000101100111111

    t.init
  }*/

  override def draw {

    def findline(x: Int) = (screenOffset + x) / (width / res) * res
    def findcol(y: Int) = (screenOffset + y) % (width / res) * res

    val towrite = screenbuffer.length
    screenbuffer.take(towrite).zipWithIndex.foreach {
      case (cell, index) => {
        if (cell) fill(0) else fill(255)
        rect(findcol(index), findline(index), res, res)
      }
    }

    screenOffset = screenOffset + towrite
    screenbuffer.remove(0, towrite)

    if (screenOffset >= screenCellCount) {
      t.pause = true
      screenbuffer.clear
      screenOffset = 0
      t.pause = false
    }
  }
}