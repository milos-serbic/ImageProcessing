package ScalaExamples

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class ScalaActor extends Actor {
  def receive = {
    case BookCase(name, author) => println(s"Book name is $name, Author name is: ${author.name}")
    case AuthorCase(name) => println(s"Author name is $name")
    case _ => println("Default...")
  }
}

object MainAppActor extends App {
    val system = ActorSystem("ScalaActorSystem")
    val scalaActor = system.actorOf(Props[ScalaActor], name = "ScalaActor")
    scalaActor ! BookCase("ScalaBook", new AuthorCase("ScalaAuthor"))
    scalaActor ! AuthorCase("ScalaAuthor2")
    scalaActor ! "TestForDefault"
}