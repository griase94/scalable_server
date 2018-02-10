package de.scalable.routes

import akka.http.scaladsl.marshalling.ToResponseMarshallable.apply
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import org.slf4j.{Logger, LoggerFactory}

trait RestRoutes extends PartyRoute {

  override val log: Logger = LoggerFactory.getLogger("RestRoutes")

  def routes: Route =
    healthCheck ~
      mailRoute //~

  def healthCheck: Route =
    pathPrefix("health") {
      pathEndOrSingleSlash {
        get {
          complete {
            "Server is Healthy!"
          }
        }
      }
    }
}
