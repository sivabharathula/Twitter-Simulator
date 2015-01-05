package com.twitter.server
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

import TweetServer._

class SetupNode extends Actor {

    def receive = {

      case "start" =>
      case "stop" => self ! PoisonPill

      case setup_node(k: String, i: Int) => {

        Users(i).username = k;
        Users(i).password = UserStore(k);
        var followerCount =0;
        
      if (i <= (userList.length/2)) {
          
         var usFactor: Int = i*20/userList.length
         followerCount = (13 - (usFactor*1))*userList.length/200
         
      }
        else {
          
           var r = new scala.util.Random
           followerCount = r.nextInt(50)
        }

        
        var r = new scala.util.Random
        var seq = r.nextInt(userList.length - followerCount)
        var followerList = ArrayBuffer[String]()

        for (fcnt <- seq until seq+followerCount -1) {
          
          /*while ((userList(seq) == k) || (followerList contains userList(seq))) {
            seq = r.nextInt(userList.length)
          }*/
          followerList += userList(fcnt)
        }

        Users(i).FollowerList = followerList
        Users(i).tweetDBFile = k + ".dat"
        Users(i).LatestTweets += "Tweet Body of user: " + k

        UserMap += { k -> i }

        Tweetengine ! UserSetup(k)
        self ! PoisonPill

      }

    }

  }
