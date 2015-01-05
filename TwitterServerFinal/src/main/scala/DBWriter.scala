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


import TweetServer._
  
  class DBWriter extends Actor {
   
   
   
     def receive = {

      case "start" =>
      
      case WriteStore(msg, userid) => {
      val svrid: Int = ((userid*cpuFactor)/userList.length)
        
        var filename = "TWDBFILE_" + svrid.toString +".dat"
        var fw = new FileWriter(filename, true);
        fw.write(msg);
        fw.write("\n");
        fw.close()
        
      }
        
     }
    
  }