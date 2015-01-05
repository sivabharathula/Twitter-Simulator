
package com.twitter.client
import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import akka.event.Logging
import akka.actor.Props

import MyJsonProtocol1._
import TwitterClient.ipaddr
import TwitterClient.svraddr
import TwitterClient.cpuFactor

object TweetGetActor {
  case class PutTweet(user: String)
  
  
}

class TweetGetActor(requestContext: RequestContext) extends Actor {

  import TweetGetActor._

  implicit val system = context.system
  import system.dispatcher
  val log = Logging(system, getClass)

  def receive = {
    case PutTweet(user) =>
      gettweet(user)
      context.stop(self)
  }

  def gettweet(user: String) = { 
    
    
     //log.info("Placing tweet for user: {}, tweet: {}", user, tweet)  
     var r = new scala.util.Random
     var seq = r.nextInt(cpuFactor)
     //val TweetSendService = context.actorSelection("akka://TweetEngine/user/Tweetengine/"+seq)
     //val TweetSendService = context.actorSelection("akka.tcp://TweetEngine@" + ipaddr + ":2250/user/Tweetengine/"+seq)
     var TweetSendService = context.actorSelection("akka.tcp://TweetClientSys@" + ipaddr + ":2251/user/TweetSim/"+ user)

     //println(TweetSendService)
     
     TweetSendService ! sendUserTweet(user)
     requestContext.complete("Tweet Placed")

    
  }
}
