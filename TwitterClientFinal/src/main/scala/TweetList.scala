package com.twitter.client

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import spray.httpx.Json4sSupport
import spray.json._

import TwitterClient.ipaddr
import TwitterClient.svraddr
import TwitterClient.cpuFactor

object MyJsonProtocol1 extends DefaultJsonProtocol {
  implicit val Format = jsonFormat2(TweetList)
}
case class TweetList(source: String, tweetlist: String) {}