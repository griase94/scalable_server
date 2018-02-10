

package de.scalable.routes


import org.slf4j.{Logger, LoggerFactory}
import scala.concurrent.Future

object ScalableActorLogic {

  private val log: Logger = LoggerFactory.getLogger("ScalableActorLogic")

  def echo(echo: String) = {
    Future.successful(echo)
  }

}

