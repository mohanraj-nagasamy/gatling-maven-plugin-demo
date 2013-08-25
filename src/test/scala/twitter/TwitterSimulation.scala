package twitter

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.Headers.Names._
import scala.concurrent.duration._
import bootstrap._
import assertions._

class TwitterSimulation extends Simulation {

  val noOfUsers: Int = Integer.getInteger("users", 1).intValue
  println("noOfUsers = " + noOfUsers)

  val noOfTweets: Int = Integer.getInteger("tweets", 1)
  println("noOfTweets = " + noOfTweets)

  val checkTweets = "<strong>" + noOfTweets + "</strong> Tweets"
  println("checkTweets = " + checkTweets)

  val username = System.getProperty("username", "")
  val password = System.getProperty("password", "")

  println("username = " + username)
  println("password = " + password)

  val httpConf = http
    .baseURL("https://twitter.com")
    .disableCaching
    .disableResponseChunksDiscarding
    .extraInfoExtractor((status: Status, session: Session, request: Request, response: Response) => {
      println("requestUrl: " + request.getUrl())
      println("requestHeader: " + request.getHeaders())
      println("requestCookies: " + request.getCookies())

      println("request.getRawUrl() " + request.getRawUrl())
      println("response.getStatusCode() :: " + response.getStatusCode())
      println("responseHeader: " + response.getHeaders())
      println("responseCookies: " + response.getCookies())
      //      println("responseBody: " + response.getResponseBody())

      List[String](request.getRawUrl())
    })

  val headers = Map("Accept" -> """text/html,application/xhtml+xml,application/xml""")
  val authenticity_token = regex("""input type="hidden" value="([^"]*)"""").saveAs("authenticity_token")

  val scn = scenario("Twitter test")
    .exec(
      http("get login")
        .get("/")
        .headers(headers)
        .check(authenticity_token))
    .exec(
      session => {
        println("session before login: " + session)
        println("authenticity_token: " + session("authenticity_token"))
        session
      })
    .exec(
      http("post login & get the twitter page")
        .post("/sessions")
        .param("session[username_or_email]", username)
        .param("session[password]", password)
        .param("authenticity_token", "${authenticity_token}")
        .headers(headers)
        .check(status.is(200))
        .check(regex(checkTweets).exists))
    .exec(session => {
      println("session after login: " + session)
      session
    })
    .exec(
      http("logout from twitter")
        .post("/logout")
        .param("authenticity_token", "${authenticity_token}")
        .headers(headers)
        .check(status.is(200)))

  setUp(scn.inject(atOnce(noOfUsers users))).protocols(httpConf)
}