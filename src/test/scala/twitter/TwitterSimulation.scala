package twitter

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.http.HeaderNames
import scala.concurrent.duration._

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
    .extraInfoExtractor(ExtraInfo => {
      println("requestUrl: " + ExtraInfo.request.getUrl())
      println("requestHeader: " + ExtraInfo.request.getHeaders())
      println("requestCookies: " + ExtraInfo.request.getCookies())

      println("request.getRawUrl() " + ExtraInfo.request.getUri().toUrl())
      println("response.getStatusCode() :: " + ExtraInfo.response.statusCode)
      println("responseHeader: " + ExtraInfo.response.headers)
      println("responseCookies: " + ExtraInfo.response.cookies)
      //      println("responseBody: " + response.getResponseBody())

      List[String](ExtraInfo.request.getUri().toUrl())
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
        .formParam("session[username_or_email]", username)
        .formParam("session[password]", password)
        .formParam("authenticity_token", "${authenticity_token}")
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
        .formParam("authenticity_token", "${authenticity_token}")
        .headers(headers)
        .check(status.is(200)))

  setUp(scn.inject(atOnceUsers(noOfUsers)).protocols(httpConf))
}