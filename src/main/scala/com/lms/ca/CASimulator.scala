package com.lms.ca

import processing.core._

import scala.collection.mutable

object CASimulator {
  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "com.lms.ca.CASimulatorApplet"))
  }
}

class CASimulatorApplet extends PApplet {

  val res = 1
  val complexity = 5
  val screenBuffer = new mutable.SynchronizedQueue[Vector[Boolean]]
  val caComputer = new CAThread(CAThreadParam(res, 2560, complexity, screenBuffer))

  override def settings() {
    size(2560, 1440) // "processing.opengl.PGraphics2D"
    fullScreen()
    noSmooth()
  }

  override def setup() {
    frameRate(64)
    noStroke()

    println("---- CA simulation starts ----")
    println("Screen resolution: " + width + "*" + height)
    println("Screen cell count: " + (width * height) / (res * res))
    println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
    println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
    println("Rule definition size: " + math.pow(2, complexity).toInt + "bit")
    println("Number of possible rules: " + math.pow(2, math.pow(2, complexity)).toLong)
  }

  override def keyPressed() {
    if (key equals ' ') caComputer.reset(true)
  }

  var screenOffset = 0
  override def draw() {

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
        if (screenOffset >= (width * height) / (res * res)) {
          screenOffset = 0
          caComputer.reset(false)
        }
    }
  }


}