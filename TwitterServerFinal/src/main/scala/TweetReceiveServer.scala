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

import TweetServer._


class TweetReceiveServer extends Actor {



    /*var TweetRouter = {
      val TweetRoutees = Vector.fill(tweetFactor) {
        val r = context.actorOf(Props[TweetRoutingServer])
        context watch r
        ActorRefRoutee(r)
      }
      Router(RoundRobinRoutingLogic(), TweetRoutees)
    }*/

    def receive = {

      case "start" =>

      case "Hello" => println("It works")
        
      case getUserTweet(user) => {
        
        
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

        context.actorSelection("akka://TweetEngine/user/Tweetengine/RoutingServer-"+svrid.toString) ! getUserTweets(user)

      }

      case incomingTweet(source, tweet) => {

        // println("Received tweet: " + tweet + " from user: " + source)

        //var remote = context.actorFor("akka.tcp://TweetClientSys@" + ipaddr + ":2251/user/TweetSim")
	
	//var remote = context.actorSelection("akka.tcp://TweetClientSys@" + ipaddr + ":2251/user/TweetSim")

	

        //remote ! AckTweet(source, tweet)
        Tweetengine ! "tweet_from_user"

        var tmpList = tweet.split(" ")

        var test: Boolean = true
        var i: Int = 0
        var privList = ArrayBuffer[String]()

        if (tmpList.length > 0) {
          while (test) {

            if (tmpList(i) == "") { i = i + 1; }

            else {

              var tmpUser = tmpList(i)

              if (tmpUser.charAt(0).toString == "@") {
                privList += tmpUser;
              } else {
                test = false
              }
              i = i + 1;
            }
          }

          if (privList.length > 0) {
            for (i <- 0 until privList.length) {
              //var r = new scala.util.Random
              //var seq = r.nextInt(cpuFactor)

              //TweetRouter.route(PropagateTweet(source, tweet, privList(i).stripPrefix("@")), sender())

               val tgt = privList(i).stripPrefix("@")

              var userfound = true
              var j = 0;
              var userid: Int = 0
              while (userfound && (i < userList.length)) {

                if (userList(j) == tgt) {
                  userid = j;
                  userfound = false
                }
                j += 1;

              }
              
              var svrid: Int = ((userid*cpuFactor)/userList.length)

              context.actorSelection("akka://TweetEngine/user/Tweetengine/RoutingServer-"+svrid.toString) ! PropagateTweet(source, tweet, tgt)
	      //println("Sending Tweets to : " + tgt);

            }
          }
          
          var userfound = true
              var j = 0;
              var userid: Int = 0
              while (userfound && (i < userList.length)) {

                if (userList(j) == source) {
                  userid = j;
                  userfound = false
                }
                j += 1;

              }
              

          var flist = Users(userid).FollowerList

          if (flist.length > 0) {
            for (i <- 0 until flist.length) {

              var r = new scala.util.Random
              var seq = r.nextInt(cpuFactor)

              //TweetRouter.route(PropagateTweet(source, tweet, flist(i)), sender())
              
               val tgt = flist(i)

              var userfound = true
              var j = 0;
              var userid: Int = 0
              while (userfound && (i < userList.length)) {

                if (userList(j) == tgt) {
                  userid = j;
                  userfound = false
                }
                j += 1;

              }
              
              var svrid: Int = ((userid*cpuFactor)/userList.length)

              context.actorSelection("akka://TweetEngine/user/Tweetengine/RoutingServer-"+svrid.toString) ! PropagateTweet(source, tweet, tgt)


            }
          }

        }
      }
    }
  }
