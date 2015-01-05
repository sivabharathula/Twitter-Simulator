package com.twitter.client

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import spray.httpx.Json4sSupport
import spray.json._

object MyJsonProtocol extends DefaultJsonProtocol {
  implicit val Format = jsonFormat2(Tweet)
}
case class Tweet(source: String, tweetbody: String) {}


