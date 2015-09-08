package com.lms.ca

import scala.util.Random

case class CAThreadParam(res: Int, width: Int, complexity: Int, screenBuffer: scala.collection.mutable.Queue[Vector[Boolean]])

class CAThread(param: CAThreadParam) extends Thread {

  var rule, gen = Vector[Boolean]()
  reset(true)
  start()

  def reset(newRule: Boolean) {
    param.screenBuffer.clear()
    if (newRule) {
      rule = (1 to math.pow(2, param.complexity).toInt map (_ => Random.nextBoolean())).toVector
      gen = (1 to param.width / param.res map (_ => Random.nextBoolean())).toVector
      println("---- New rule starts ----")
      println("Rule ID: " + rule.map { case (b) => if (b) 1 else 0 }.mkString)
    }
  }

  override def run() {
    while (true) {
      gen = CAModel.computeGeneration(gen, rule, (param.complexity - 1) / 2).toVector
      param.screenBuffer enqueue gen
    }
  }

}
