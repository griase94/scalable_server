

package de.scalable.routes

import akka.actor.{Actor, ActorRef, Props}
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import com.typesafe.config.{Config, ConfigFactory}
import de.scalable.json.ModelJsonSupport
import org.slf4j.{Logger, LoggerFactory}
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

trait PartyRoute extends PartyApi with ModelJsonSupport {

  val log: Logger = LoggerFactory.getLogger("ScalableRoute")

  val token: String = ConfigFactory.load().getString("scalable.token")

  def mailRoute: Route =
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
    }

}

class ScalableActor(implicit timeout: Timeout) extends Actor {

  import ScalableMessages._

  val log: Logger = LoggerFactory.getLogger("MailActor")

  val config: Config = ConfigFactory.load()

  override def receive: PartialFunction[Any, Unit] = {

    case Echo(echo) =>
      pipe(
        ScalableActorLogic.echo(echo)
      ) to sender()
  }
}

object ScalableMessages {
  def props(implicit timeout: Timeout): Props = Props(new ScalableActor)

  def name: String = "scalable_scalable" //TODO rename

  case class Echo(echo: String)

}

trait PartyApi {

  import ScalableMessages._

  def createScalableActor(): ActorRef

  implicit def executionContext: ExecutionContext

  implicit def requestTimeout: Timeout

  lazy val scalableActor: ActorRef = createScalableActor()

  def echo(echo: String): Future[String] =
    (scalableActor ? Echo(echo)).mapTo[String]
}
