package processing

import processing.core._
import processing.model.CellularAutomata._

import scala.collection.mutable.SynchronizedQueue
import scala.util.Random

object Scala2dCAThreading {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAThreading"))
  }
}

class RunCAThreading extends PApplet {

  val res = 1
  val complexity = 5
  val screenBuffer = new SynchronizedQueue[Vector[Boolean]]
  var screenOffset = 0

  def screenCellCount = (width * height) / (res * res)

  override def settings {
    size(2560, 1440) //, "processing.opengl.PGraphics2D")
    fullScreen
    noSmooth
  }

  override def setup {
    frameRate(32)
    noStroke
    t.init
    t.start
    println("---- CA simulation starts ----")
    println("Screen resolution: " + width + "*" + height)
    println("Screen cell count: " + screenCellCount)
    println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
    println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
    println("Rule definition size: " + math.pow(2, complexity).toInt + "bit")
    println("Number of possible rules: " + math.pow(2, math.pow(2, complexity)).toLong)
  }

  override def keyPressed {
    if (key.equals(' ')) {
      screenBuffer.clear
      screenOffset = 0
      t.init
    }
  }

  val t = new Thread() {

    var rule, gen = Vector[Boolean]()

    def init {
      // Interesting rules
      // 00111011
      // 01110100110111001000010011011010, 01010011100000011110110000110101
      // 11011111010100001011001010101110100111000011001110010110101010101101110000101101101000111011101101001001110100111000101100111111
      rule = (1 to math.pow(2, complexity).toInt map (_ => Random.nextBoolean())).toVector
      gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
      println("---- New rule starts ----")
      println("Rule ID: " + rule.map { case (b) => if (b) 1 else 0 }.mkString)
    }

    override def run {
      while (true) {
        gen = compute(gen, rule, (complexity - 1) / 2)
        screenBuffer.enqueue(gen)
      }
    }
  }

  override def draw {

    def findline(x: Int) = (screenOffset + x) / (width / res) * res
    def findcol(y: Int) = (screenOffset + y) % (width / res) * res

    screenBuffer.dequeueAll(gen => true).foreach {
      case (gen: Vector[Boolean]) =>
        gen.zipWithIndex.foreach {
          case (c, i) =>
            if (c) fill(0) else fill(255)
            rect(findcol(i), findline(i), res, res)
        }
        screenOffset = screenOffset + gen.length
        if (screenOffset >= screenCellCount) {
          screenBuffer.clear
          screenOffset = 0
        }
    }
  }


}