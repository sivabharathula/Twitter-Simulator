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
import akka.remote._
import akka.io.IO
import spray.can.Http
import akka.util.Timeout
import scala.concurrent.duration._

case class incomingTweet(Sender: String, Tweet: String)
case class receiveTweet(Sender: String, Tweet: String,Target: String) 
case class AckTweet(Sender: String, Tweet: String)
case class getUserTweet(User: String)
//case class sendUserTweet(TwQ: Queue[String])
case class sendUserTweet(TwQ: String)
case class send_tweet(tweetDB: String)
//case class send_tweetr()

object TwitterClient {
  
  
  sealed trait TweeterClient

  case class sendTweet(Sender: String, Tweet: String) extends TweeterClient
   
  

  var userList = ArrayBuffer[String]()
  var userMap = HashMap.empty[String, String]
  var ipaddr: String = "192.168.56.1"
  var svraddr: String = "192.168.56.1"
  var loadFactor: Int = 20
  var userFactor: Int = 1000
  var tweetFactor: Int = 10
  var twcnt =0;
  var cpuFactor: Int = 20
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

    val hostname = InetAddress.getLocalHost.getHostName

//    ipaddr = args(0)
  //  svraddr = args(1)

    val config = ConfigFactory.parseString(
      """akka{
		  		actor{
		  			provider = "akka.remote.RemoteActorRefProvider"
		  		}
		  		remote{
                   enabled-transports = ["akka.remote.netty.tcp"]
		  			netty.tcp{
						hostname = """ + ipaddr + """
						port = 2251
					}
				}     
    	}""")

   implicit  val system = ActorSystem("TweetClientSys", ConfigFactory.load(config))
    val TweetSim = system.actorOf(Props(new TweetSimulator), "TweetSim")
    val TweetHttpClient = system.actorOf(Props(new ApiCActor), "tweet-service")
    IO(Http) ! Http.Bind(TweetHttpClient, interface = ipaddr, port = 8082)
    TweetSim ! "start"


          
    TweetSim ! "request"
            
        }
  

  
  
      
}
