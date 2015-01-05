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
import akka.io.IO
import spray.can.Http
import akka.util.Timeout
import scala.concurrent.duration._
import spray.routing._
import spray.http._
import MediaTypes._
import akka.actor.Props
import spray.httpx.Json4sSupport
import spray.json._

case class incr(dtCnt: Int, ptCnt: Int)
case class incomingTweet(Sender: String, Tweet: String)
case class receiveTweet1(Sender: String, Tweet: String, Target: String)
case class SetupUser(user: String, Flist: ArrayBuffer[String])
case class AckTweet(Sender: String, Tweet: String)
case class PropagateTweet(Sender: String, Tweet: String, Target: String)
case class getUserTweet(User: String)
case class getUserTweets(User: String)
case class sendUserTweet(TwQ: Queue[String])
case class UpdateRecord(User: String, mssage: String)
case class UserSetup(k: String)
case class WriteStore(msg: String, userid: Int)

object TweetServer {
  case class setup_node(k: String, i: Int)
  var userList = ArrayBuffer[String]()
  var Users = ArrayBuffer[TweetUser](new TweetUser())
  var UserMap = HashMap.empty[String, Int]
  var ipaddr: String = "192.168.56.1"
  var cpuFactor: Int = 20
  var userFactor: Int = 20
  var tweetFactor: Int = 5
  var tweetCount: Int = 0
  var procTweetCnt: Int = 0
  var startTime = System.currentTimeMillis();
  var twSize: Int = 0
  var UserStore = HashMap.empty[String, String]
  var usersetupcnt = 0

  

  val hostname = InetAddress.getLocalHost.getHostName
  val address = InetAddress.getLocalHost().getHostAddress()
  val config = ConfigFactory.parseString(
    """akka{
		  		actor{
		  			provider = "akka.remote.RemoteActorRefProvider"
		  		}
		  		remote{
                   enabled-transports = ["akka.remote.netty.tcp"]
		  			netty.tcp{
						hostname = """ + ipaddr + """ 
						port = 2250
					}
				}     
    	}""")
  implicit val system = ActorSystem("TweetEngine", ConfigFactory.load(config))
  val Tweetengine = system.actorOf(Props(new TweetEngine), "Tweetengine")
  val TweetHttpServer = system.actorOf(Props(new ApiActor), "tweet-service")
  IO(Http) ! Http.Bind(TweetHttpServer, interface = ipaddr, port = 8080)

def ipset(s: String): String = {
return s}

  def hash(s: String): String = {
    val md = MessageDigest.getInstance("SHA-256")
    md.update(s.getBytes)
    val byteData = md.digest()
    val sb = new StringBuffer()
    for (i <- 0 until byteData.length) {
      sb.append(java.lang.Integer.toString((byteData(i) & 0xff) + 0x100, 16)
        .substring(1))
    }
    return sb.toString
  }

  
  
      

  
  def main(args: Array[String]) {

//  var ipaddress = args(0)
//  println(ipaddress)

    Tweetengine ! "start"

  }

}
