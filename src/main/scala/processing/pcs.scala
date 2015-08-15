package processing

import processing.core._

import scala.util._

object Pcs {
  def main(args: Array[String]) {
    PApplet.main(Array("--present", "processing.Sample"))
  }
}

class Sample extends PApplet {

  val d = 200
  var a = List(List(d / 2, 0, 255, 0, 0), List(d, d, 0, 255, 0), List(0, d, 0, 0, 255))
  var p = List(d, 0, 255, 0, 0)


  override def settings() {
    size(d * 2, d * 2)
  }

  override def setup() {
    background(0, 10, 20)
  }

  override def draw() {
    for (i <- 1 to 100) {
      def randA = a(Random.nextInt(2))
      p = p.zip(randA).map { case (x, y) => x / 2 + y }
      stroke(p(2), p(3), p(4))
      point(p.head, p(1))
    }
  }

}
