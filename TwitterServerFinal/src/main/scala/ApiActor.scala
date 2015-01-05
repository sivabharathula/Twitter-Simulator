package com.twitter.server

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import spray.json._
import MyJsonProtocol._
import spray.httpx.SprayJsonSupport._ 

import TweetServer.cpuFactor
import TweetServer.ipaddr

class ApiActor extends Actor with ApiService {

  //implicit def format = jsonFormat2
  def actorRefFactory = context

  def receive = runRoute(reqRoute)
}

trait ApiService extends HttpService  {
  

import MyJsonProtocol._
  val reqRoute =
    pathPrefix("api") {
     path("GetFollowers" /Segment) { user =>
        requestContext =>
          val tweetgetService = actorRefFactory.actorOf(Props(new FollowerGetActor(requestContext)))
          tweetgetService ! FollowerGetActor.GetFollower(user.toString)

      } ~  
      path("GetTweets" /Segment) { user =>
        requestContext =>
          val tweetgetService = actorRefFactory.actorOf(Props(new TweetGetActor(requestContext)))
          tweetgetService ! TweetGetActor.GetTweet(user.toString)

      } ~
      path("SendTweet") { 
	  get {
           entity(as[Tweet]) { TweetObj =>
	   complete {
	    
            val tweetsndService = actorRefFactory.actorOf(Props(new TweetSendActor()))
            tweetsndService ! TweetSendActor.PlaceTweet(TweetObj.source, TweetObj.tweetbody)
            TweetObj
          }
         }
        }
     }

  }

}