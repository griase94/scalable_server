

package de.scalable.routes

import akka.actor.{Actor, ActorRef, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import de.scalable.json.ModelJsonSupport
import de.scalable.model._
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
    pathEndOrSingleSlash {
      put {
        entity(as[String]) { partyName =>
          onComplete(createParty(partyName)) {
            case Success(result) => complete(result.toJson)
            case Failure(e) =>
              e.printStackTrace()
              complete((InternalServerError, e.toString))
          }
        }
      }
    }~
    pathPrefix("song") {
      post {
        entity(as[SongPlayed]) { songPlayed =>
          onComplete(setSongPlayed(songPlayed.id,songPlayed.partyKey)) {
            case Success(result) => complete((result.toJson))
            case Failure(e) =>
              e.printStackTrace()
              complete((InternalServerError, e.toString))

          }
        }
      }~
      path(Remaining) { partyKey =>
        put {
          entity(as[SongToAdd]) { songToAdd =>
            onComplete(addSong(songToAdd, partyKey)) {
              case Success(result) => complete(result.toJson)
              case Failure(e) =>
                e.printStackTrace()
                complete((InternalServerError, e.toString))
            }
          }
        } ~
        get {
          onComplete(getSongsForParty(partyKey)) {
            case Success(echoReturn) => complete((echoReturn))
            case Failure(e) =>
              e.printStackTrace()
              complete((InternalServerError, e.toString))

          }
        }
      }
    }~
    pathPrefix("vote") {
      pathEndOrSingleSlash{
        post {
          entity(as[PartyVote]) { vote =>
            onComplete(voteForSong(vote)) {
              case Success(result) => complete(result.toJson)
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

    case AddSong(songToAdd,partyID) =>
      pipe(
        PartyActorLogic.addSong(songToAdd,partyID)
      ) to sender()

    case CreateParty(name) =>
      pipe(
        PartyActorLogic.createParty(name)
      ) to sender()

    case GetSongsForParty(partyKey) =>
      pipe(
        PartyActorLogic.getSongsForParty(partyKey)
      ) to sender()

    case VoteForSong(vote) =>
      pipe(
        PartyActorLogic.voteForSong(vote)
      ) to sender()

    case SetSongPlayed(songID, partyKey) =>
      pipe(
        PartyActorLogic.setSongPlayed(songID, partyKey)
      ) to sender()
  }
}

object PartyMessages {
  def props(implicit timeout: Timeout): Props = Props(new PartyActor)

  def name: String = "scalable_party" //TODO rename

  case class Echo(echo: String)
  case class AddSong(song: SongToAdd, partyKey: String)
  case class CreateParty(name:String)
  case class GetSongsForParty(partyKey: String)
  case class VoteForSong(vote: PartyVote)
  case class SetSongPlayed(songID: Long, partyKey: String)
}

trait PartyApi {

  import PartyMessages._

  def createPartyActor(): ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  lazy val partyActor: ActorRef = createPartyActor()

  def echo(echo: String): Future[String] =
    (partyActor ? Echo(echo)).mapTo[String]

  def addSong(song: SongToAdd, partyKey:String): Future[SongReturn] =
    (partyActor ? AddSong(song,partyKey)).mapTo[SongReturn]

  def createParty(name:String): Future[Party] ={
    (partyActor ? CreateParty(name)).mapTo[Party]
  }

  def getSongsForParty(partyKey: String): Future[Seq[SongReturn]] =
    (partyActor ? GetSongsForParty(partyKey)).mapTo[Seq[SongReturn]]

  def voteForSong (vote: PartyVote): Future[Int] ={
    (partyActor ? VoteForSong(vote)).mapTo[Int]
  }

  def setSongPlayed(songID:Long, partyKey:String): Future[Int] ={
    (partyActor ? SetSongPlayed(songID,partyKey)).mapTo[Int]
  }
}

