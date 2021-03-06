
package com.twitter.server
import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import akka.event.Logging
import akka.actor.Props
import akka.pattern.ask
import scala.concurrent.Await
import scala.concurrent.Future
import TweetServer.userList
import akka.util.Timeout
import scala.concurrent.duration._
import TweetServer.Users
import spray.json.DefaultJsonProtocol._
import spray.httpx.SprayJsonSupport._
import MyJsonProtocol1._


import TweetServer.cpuFactor
import TweetServer.ipaddr

object FollowerGetActor {

  case class GetFollower(user: String)
  
  implicit val timeout = Timeout( 15 seconds)


  
}

class FollowerGetActor(requestContext: RequestContext) extends Actor {

  import FollowerGetActor._

  implicit val system = context.system
  
  
  import system.dispatcher
  val log = Logging(system, getClass)

  def receive = {
    case GetFollower(user) =>
      getfollower(user)
      context.stop(self)
  }

  def getfollower(user: String) = { 
    
    println("Getting Followers for " + user )

    var userfound = true
        var i = 0;
        var userid: Int = 0
        while (userfound && (i < userList.length)) {

          if (userList(i) == user) {
            userid = i;
            userfound = false
          }
          i += 1;

        }
        
        var svrid: Int = ((userid*cpuFactor)/userList.length)

     log.info("Getting followers for user: {}", user)  
     //var r = new scala.util.Random
     //var seq = r.nextInt(cpuFactor)
     //val TweetSendService = context.actorSelection("akka://TweetEngine/user/Tweetengine/"+seq)
     //val TweetSendService = context.actorSelection("akka.tcp://TweetEngine@" + ipaddr + ":2250/user/Tweetengine/"+seq)
     //println(TweetSendService)
     
     //val TweetSendService = context.actorSelection("akka://TweetEngine/user/Tweetengine/RoutingServer-"+svrid.toString)
     
     //val future = TweetSendService ? getUserTweets(user)
     //val result = Await.result(future, timeout.duration).asInstanceOf[String]
     
        var follwelist = Users(userid).FollowerList.toString

	if (userfound == true) follwelist = "ERROR: User not found"

	var tmptweetL = new TweetList(user,follwelist)

     
     
     requestContext.complete(tmptweetL)

    
  }
}
