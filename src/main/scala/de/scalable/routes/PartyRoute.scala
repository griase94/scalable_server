

package de.scalable.routes

import akka.actor.{Actor, ActorRef, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import de.scalable.json.ModelJsonSupport
import de.scalable.model.Song
import org.slf4j.{Logger, LoggerFactory}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait PartyRoute extends PartyApi with ModelJsonSupport {

  val log: Logger = LoggerFactory.getLogger("PartyRoute")

  val token: String = ConfigFactory.load().getString("scalable.token")

  def partyRoute: Route =
    pathPrefix("echo") {
      path(Remaining) { stringToEcho =>
        get {
          onComplete(echo(stringToEcho)) {
            case Success(echoReturn) => complete((echoReturn))
            case Failure(e) =>
              e.printStackTrace()
              complete((InternalServerError, e.toString))

          }
        }
      }
    } ~
  pathPrefix("party") {
    pathPrefix("song") {
      path(Remaining) { partyKey =>
        put {
          entity(as[Song]) { songToAdd =>
            onComplete(addSong(songToAdd, partyKey)) {
              case Success(echoReturn) => complete((echoReturn))
              case Failure(e) =>
                e.printStackTrace()
                complete((InternalServerError, e.toString))
            }
          }
        }
      }
    }
  }

}

class PartyActor(implicit timeout: Timeout) extends Actor {

  import PartyMessages._

  val log: Logger = LoggerFactory.getLogger("PartyActor")

  val config: Config = ConfigFactory.load()

  override def receive: PartialFunction[Any, Unit] = {

    case Echo(echo) =>
      pipe(
        PartyActorLogic.echo(echo)
      ) to sender()

    case AddSong(song,partyKey) =>
      pipe(
        PartyActorLogic.addSong(song,partyKey)
      ) to sender()
  }
}

object PartyMessages {
  def props(implicit timeout: Timeout): Props = Props(new PartyActor)

  def name: String = "scalable_party" //TODO rename

  case class Echo(echo: String)
  case class AddSong(song: Song,partyKey)

}

trait PartyApi {

  import PartyMessages._

  def createPartyActor(): ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  lazy val partyActor: ActorRef = createPartyActor()

  def echo(echo: String): Future[String] =
    (partyActor ? Echo(echo)).mapTo[String]

  def addSong(song: Song, partyKey:String): Future[Seq[Song]] =
    (partyActor ? AddSong(song,partyKey)).mapTo[Seq[Song]]
}

