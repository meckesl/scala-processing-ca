package sandbox

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

object ActorTest extends App {

  sealed trait CoffeeRequest
  case object CappuccinoRequest extends CoffeeRequest
  case object EspressoRequest extends CoffeeRequest
  case object CaffeineWithdrawalWarning
  case class Bill(cents: Int)
  case object ClosingTime

  /**
   * BARISTA
   */
  class Barista extends Actor {
    def receive = {
      case CappuccinoRequest =>
        sender ! Bill(250)
        println("I have to prepare a cappuccino!")
      case EspressoRequest =>
        sender ! Bill(200)
        println("Let's prepare an espresso.")
      case ClosingTime => context.system.shutdown()
    }
  }

  /**
   * CUSTOMER
   */
  class Customer(caffeineSource: ActorRef) extends Actor {
    def receive = {
      case CaffeineWithdrawalWarning => caffeineSource ! EspressoRequest
      case Bill(cents) => println(s"I have to pay $cents cents, or else!")
    }
  }

  // Define actor system
  val system = ActorSystem("Barista")
  val barista = system.actorOf(Props[Barista], "Barista")
  val customer = system.actorOf(Props(classOf[Customer], barista), "Customer")

  // Send 1st msg
  customer ! CaffeineWithdrawalWarning

  // Shutdown thread pool
  system.shutdown()

}
