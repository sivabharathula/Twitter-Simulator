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
import akka.routing.Router
import akka.routing.ActorRefRoutee
import akka.routing.RoundRobinRoutingLogic


import TwitterClient._
class TweetSimulator extends Actor {

    override def preStart() {
      

      val userDB = "userStore.dat"
      


      for (line <- Source.fromFile(userDB).getLines()) {
        var input = line.toString;
        var userName = input.split(",")(0);
        var passWord = input.split(",")(1);
        var passHash = hash(passWord)
        userMap += { userName -> passHash }
        userList += userName
    context.actorOf(Props(new TweetUser(userName)),userName) ! "start"
      }

      for (i <-0  until tweetFactor)
      {
        context.actorOf(Props(new TweetSender),i.toString) ! "start"
	context.actorOf(Props(new TweetSenderR),"R"+i.toString) ! "start"
      }
        
        
    }
    
    
   
        
    def receive = {

      
        case AckTweet(sender,tweet) => {
        
               twcnt+=1;
        if (twcnt == 999) {
          
           println("Tweet count 999")
           //System.exit(0)
        }
        
      }
      
        case "request" => {
        
        
        var r = new scala.util.Random
          var seq = r.nextInt(userList.length)
        for (i <- 1 until 2) {
          
          var s = new scala.util.Random
          //var seqs = s.nextInt(userList.length)
          var seqs =1
          
          
         context.actorSelection(userList(i)) ! "RequestTweet"
      }
      
      

        
    }
      case "start" => {

        println("Tweet Simulator client System Started !!");
        
        var tweetDB=new Array[String](10) 
      
      for (i <- 0 until tweetFactor )
      {
        tweetDB(i) = "tweetStore"+i.toString+".dat"
	}

	for (i <- 0 until tweetFactor) {
          val tweetrc = context.actorSelection(i.toString)
	  val tweetrcr = context.actorSelection("R"+i.toString)
          
	  tweetrc ! send_tweets(tweetDB(i))
	  tweetrcr ! send_tweetr()
          

          }
        
        
          
          
      }
      
    }
      
      
  }
  