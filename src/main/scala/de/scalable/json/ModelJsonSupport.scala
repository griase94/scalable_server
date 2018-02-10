

package de.scalable.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import de.scalable.model._
import spray.json.RootJsonFormat

import scala.language.implicitConversions


trait ModelJsonSupport extends SprayJsonSupport with AdditionalJsonTypes {

  implicit val receiverReturnFormat: RootJsonFormat[Song] =
    jsonFormat7(Song)

}
