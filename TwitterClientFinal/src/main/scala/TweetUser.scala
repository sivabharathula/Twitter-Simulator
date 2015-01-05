package com.twitter.client
import java.util.Calendar
import scala.collection.mutable.ArrayBuffer
import java.net.InetAddress
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props
import scala.io.Source
import java.security.MessageDigest
import scala.collection.mutable.HashMap
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import akka.routing.Router
import scala.collection.mutable.Queue
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

import TwitterClient.ipaddr
import TwitterClient.svraddr
import TwitterClient.cpuFactor
//import spray.httpx.RequestBuilding._
import spray.json.AdditionalFormats


import MyJsonProtocol._
 
 import TwitterClient._
 class TweetUser(uname: String) extends Actor {

    
    def receive  = {
      
      case "start" => 
        
      case "RequestTweet" => {
        
      var r = new scala.util.Random
      var seq = r.nextInt(cpuFactor)
        
      //val getTserver = context.actorSelection("akka.tcp://TweetEngine@" + ipaddr + ":2250/user/Tweetengine/"+seq)
        
        //getTserver !  getUserTweet(uname)
        //println("Requested tweet list of user: " + uname)
        
        
      }
      
      case sendUserTweet(tweetqueue) => {
        
        println("Printing the tweets of user " +uname+ "  as requested \n")
        
        if (!(tweetqueue isEmpty))
        {
        
        //for (i <- 0 until tweetqueue.length) {
          
          //if (!(tweetqueue(i)  == null)) {
                  
        println(tweetqueue+ "\n")
          //}
        
        //}
        }
        
        println("Printed the tweets of user " +uname+ "  as requested \n")
      }
      case sendTweet(sender,tweet) => {
        
       // var r = new scala.util.Random
        //var seq = r.nextInt(loadFactor)
        //val server = context.actorSelection("akka.tcp://TweetEngine@" + ipaddr + ":2250/user/Tweetengine/"+seq)
        
        //server ! incomingTweet(sender, tweet)

	//import SprayJsonSupport._
    //val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
      
      var tmptweet = new Tweet(sender,tweet)

     
     val pipeline = sendReceive
     val tweeter = sender
     val tweetmsg = tweet
     val json = s"""{"source": "$sender", "tweetbody": "$tweet"}"""
     

    // println(json)

     //pipeline(Get("http://192.168.56.1:8080/api/SendTweet",HttpEntity(`application/json`, """{"source": "$tweeter", "tweetbody": "$tweetmsg"}""")))
     
      pipeline(Get(s"http://$svraddr:8080/api/SendTweet",HttpEntity(`application/json`, json )))
        

     /* val uri = Uri("http://$svraddr:8080/api/SendTweet")
      val json = ("source" -> sender) ~ ("tweetbody" -> tweet)
      val body = compact(render(json))
      val js = Marshaller(tweet)
      val rq = HttpRequest(HttpMethods.GET , uri = uri, entity = body)*/

      //HttpRequest(method = GET, uri = "http://" + svraddr + ":8080/api/SendTweet", entity = HttpEntity(Tweet)) 
    
     // Get(s"http://$svraddr:8080/api/SendTweet/$sender/$tweet")
    
    
      }
      
      
    
    }
    
    
  }
  
