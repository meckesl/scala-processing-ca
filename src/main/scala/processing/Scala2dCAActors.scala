package processing

import akka.actor.{ActorRef, Actor}
import processing.core._
import processing.model.CellularAutomata._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case object RestartSimulation
case class Chunk(bits: Vector[Boolean])

object Scala2dCAActors {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAActors"))
  }
}

/*val t = new Thread() {

var rule, gen = Vector[Boolean]()
var pause = true
var doInit = true

def init {
pause = true
while (doInit == false) {print('.')}
rule = (1 to math.pow(2, complexity).toInt map (_ => Random.nextBoolean())).toVector
gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
screenbuffer.clear
screenOffset = 0
println("---- New rule starts ----")
println("Rule ID: " + rule.map { case (b) => if (b) 1 else 0 }.mkString)
pause = false
}

override def run {
while (true) {
if (pause == false) {
gen = compute(gen, rule, complexity)
println("new gen of len: " + gen.length)
screenbuffer.appendAll(gen)
doInit = false
} else {
print(',')
doInit = true
}
}
}
}*/

class RunCAActors(x: ActorRef) extends PApplet with Actor {

  val res = 1
  val complexity = 5

  def screenCellCount = (width * height) / (res * res)

  var screenbuffer = ArrayBuffer[Boolean]()
  var screenOffset = 0

  def receive = {
    case Chunk(vec) => println("lol")
  }

  override def settings {
    size(2560, 1440) //, "processing.opengl.PGraphics2D")
    fullScreen
    noSmooth
  }

  override def setup {
    frameRate(25)
    noStroke

    println("---- CA simulation starts ----")
    println("Screen resolution: " + width + "*" + height)
    println("Screen cell count: " + screenCellCount)
    println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
    println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
    println("Rule definition size: " + math.pow(2, complexity).toInt + "bit")
    println("Number of possible rules: " + math.pow(2, math.pow(2, complexity)).toLong)
  }

  override def keyPressed {
    if (key.equals(' ')) x ! RestartSimulation
  }

  // Interesting rules
  // 00111011
  // 01110100110111001000010011011010, 01010011100000011110110000110101
  // 11011111010100001011001010101110100111000011001110010110101010101101110000101101101000111011101101001001110100111000101100111111

  override def draw {

    val todraw = screenbuffer.length
    def findline(x: Int) = (screenOffset + x) / (width / res) * res
    def findcol(y: Int) = (screenOffset + y) % (width / res) * res

    screenbuffer.take(todraw).zipWithIndex.foreach {
      case (cell, index) => {
        if (cell) fill(0) else fill(255)
        rect(findcol(index), findline(index), res, res)
      }
    }

    screenOffset = screenOffset + todraw
    screenbuffer.remove(0, todraw)

    if (screenOffset >= screenCellCount) {
      screenbuffer.clear
      screenOffset = 0
    }
  }
}