/*package processing

import akka.actor.{Props, ActorSystem, Actor}
import processing.core._

import scala.collection.mutable


object Scala2dCAActors {

  def main(args: Array[String]): Unit = {
    PApplet.main(Array("--present", "processing.RunCAActors"))
  }

  case class Rule(bits: Vector[Boolean], complexity: Integer)

  class CellCompute(queue: mutable.SynchronizedQueue[Vector[Boolean]]) extends Actor {

    var gen = Vector[Boolean]()

    def receive = {
      case Rule(rule, complexity) =>
        queue.clear()
        while (true) {
          gen = model.CellularAutomata.compute(gen, rule, (complexity - 1) / 2).toVector
          queue enqueue gen
        }
    }
  }

  class RunCAActors extends PApplet {

    val res = 1
    val complexity = 5
    val screenBuffer = new mutable.SynchronizedQueue[Vector[Boolean]]
    var screenOffset = 0

    def screenCellCount = (width * height) / (res * res)

    override def settings() {
      size(2560, 1440) //, "processing.opengl.PGraphics2D")
      fullScreen()
      noSmooth()
    }

    override def setup() {
      frameRate(64)
      noStroke()

      val system = ActorSystem("CA")
      val cacomputer = system.actorOf(Props(classOf[Customer], system), "CAComputer")

      // Send 1st msg
      val r : Vector[Boolean] = (1 to math.pow(2, complexity).toInt map (_ => math.Random.nextBoolean())).toVector
      cacomputer ! Rule(r, complexity)

      println("---- CA simulation starts ----")
      println("Screen resolution: " + width + "*" + height)
      println("Screen cell count: " + screenCellCount)
      println("Simulation resolution: " + res + " pixel per cell -> " + width / res + "*" + height / res)
      println("Complexity: " + complexity + " -> " + (complexity - 1) / 2 + " siblings on each side for child computation")
      println("Rule definition size: " + math.pow(2, complexity).toInt + "bit")
      println("Number of possible rules: " + math.pow(2, math.pow(2, complexity)).toLong)
    }

    override def keyPressed() {
      if (key equals ' ') t.init(true)
    }


    /*val t = new Thread() {

      var rule, gen = Vector[Boolean]()

      def init(newRule: Boolean) {
        screenBuffer.clear()
        screenOffset = 0
        if (newRule) {
          // Interesting rules
          // 00111011
          // 01110100110111001000010011011010, 01010011100000011110110000110101
          // 11011111010100001011001010101110100111000011001110010110101010101101110000101101101000111011101101001001110100111000101100111111
          // 10000001000110011111001100001111001111001001110101010110110011110001001011100010100010100001001111101101100010010000101111001111
          // 00000010100000001010010101001001001000000011100010100001011110001010100100110111001111110100101001100000101000101011100110001001
          rule = (1 to math.pow(2, complexity).toInt map (_ => Random.nextBoolean())).toVector
          gen = (1 to width / res map (_ => Random.nextBoolean())).toVector
          println("---- New rule starts ----")
          println("Rule ID: " + rule.map { case (b) => if (b) 1 else 0 }.mkString)
        }
      }

      override def run() {
        while (true) {
          gen = compute(gen, rule, (complexity - 1) / 2).toVector
          screenBuffer enqueue gen
        }
      }
    }*/

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
          if (screenOffset >= screenCellCount) t.init(false)
      }
    }


  }


}*/