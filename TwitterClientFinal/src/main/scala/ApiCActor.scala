package com.twitter.client

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import spray.client.pipelining._
import spray.routing.RequestContext
import spray.httpx.SprayJsonSupport
import spray.client.pipelining._
import spray.http.HttpRequest
import scala.io.Source
import scala.util.Try
import spray.can.Http
import akka.io.IO
import scala.concurrent.ExecutionContext.Implicits.global
import spray.httpx.unmarshalling.{MalformedContent, Unmarshaller, Deserialized}
import spray.http._
import spray.httpx.SprayJsonSupport
import spray.json._
import HttpMethods._
import HttpHeaders._
import ContentTypes._
import MyJsonProtocol._


import TwitterClient.ipaddr
import TwitterClient.svraddr
import TwitterClient.cpuFactor

class ApiCActor extends Actor with ApiService {

  def actorRefFactory = context

  def receive = runRoute(reqRoute)
}

trait ApiService extends HttpService {
  

  val reqRoute =
    pathPrefix("api") {
      path("GetTweets" /Segment) { User =>

        requestContext =>
          //val tweetputService = actorRefFactory.actorOf(Props(new TweetGetActor(requestContext)))
          //tweetputService ! TweetGetActor.PutTweet(TweetObj(0), TweetObj(1))
	  val pipeline = sendReceive

	  val response = pipeline{Get(s"http://$svraddr:8080/api/GetTweets/$User")}

	  requestContext.complete(response)
	 
      }~
      path("GetFollowers" /Segment) { User =>

        requestContext =>
          //val tweetputService = actorRefFactory.actorOf(Props(new TweetGetActor(requestContext)))
          //tweetputService ! TweetGetActor.PutTweet(TweetObj(0), TweetObj(1))
	  val pipeline = sendReceive

	  val response = pipeline{Get(s"http://$svraddr:8080/api/GetFollowers/$User")}

	  requestContext.complete(response)
	 
      }
    }

}