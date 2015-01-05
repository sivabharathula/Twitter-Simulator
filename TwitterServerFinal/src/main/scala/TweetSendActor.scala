
package com.twitter.server
import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import akka.event.Logging
import akka.actor.Props
import spray.httpx.Json4sSupport
import org.json4s.Formats
import org.json4s.DefaultFormats

import TweetServer.cpuFactor
import TweetServer.ipaddr

object TweetSendActor {
  case class PlaceTweet(user: String, tweet: String)
  
}

class TweetSendActor() extends Actor {

  import TweetSendActor._

  


  implicit val system = context.system
  import system.dispatcher
  val log = Logging(system, getClass)

  def receive = {
    case PlaceTweet(user, tweet) =>
      placetweet(user,tweet)
      context.stop(self)
  }

  def placetweet(user: String, tweet: String) = { 
    
    //println("Placing tweet for " +user + tweet)

   //complete{"Tweet Placed"}

     //log.info("Placing tweet for user: {}, tweet: {}", user, tweet)  
     var r = new scala.util.Random
     var seq = r.nextInt(cpuFactor)
     //val TweetSendService = context.actorSelection("akka://TweetEngine/user/Tweetengine/"+seq)
     val TweetSendService = context.actorSelection("akka.tcp://TweetEngine@" + ipaddr + ":2250/user/Tweetengine/"+seq)
     //println(TweetSendService)
     
     TweetSendService ! incomingTweet(user, tweet)
     

    
  }
}
