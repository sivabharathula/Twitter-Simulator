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
import akka.routing.Router
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic
import java.io.FileWriter
import scala.collection.mutable.Queue
import akka.remote._
import akka.actor.PoisonPill

import TwitterClient._

case class send_tweets(tweetDB: String)
class TweetSender extends Actor {

    def receive = {

     

      case send_tweets(tweetDB) => {

        for (line <- Source.fromFile(tweetDB).getLines()) {
            
          var r = new scala.util.Random
          var seq = r.nextInt(userList.length)

	  var TweetSendService = context.actorSelection("akka.tcp://TweetClientSys@" + ipaddr + ":2251/user/TweetSim/"+ userList(seq))
          
          //val tweetrcpnt = context.actorSelection("akka.tcp://TweetClientSys:2251/user/TweetSim/"+ userList(seq)
           TweetSendService ! sendTweet(userList(seq),line)
            
          
          }
      }

    }

  }