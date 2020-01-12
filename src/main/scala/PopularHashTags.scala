import org.apache.spark.SparkConf
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object PopularHashTags {

  val TIME = 1

  def setupLoggin() = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
  }

  def setupTwitter() = {
    import scala.io.Source
    for (line <- Source.fromFile("/home/alison/IdeaProjects/apiKeyTwitter.txt").getLines) {
      val fields = line.split(" ")
      if (fields.length == 2) {
        System.setProperty("twitter4j.oauth." + fields(0), fields(1))
      }
    }
  }

  def main(args: Array[String]): Unit = {

    setupTwitter()

    val conf = new SparkConf().setAppName("AnalitycsWorlds").setMaster("local[*]")
    val ssc = new StreamingContext(conf, Seconds(TIME))

    setupLoggin()

    val tweets = TwitterUtils.createStream(ssc, None)

    val statuses = tweets.map(status => status.getText())

    val tweetwords = statuses.flatMap(tweetText => tweetText.split(" "))

    val hashtags = tweetwords.filter(word => word.startsWith("#"))

    val hashtagKeyValues = hashtags.map(hashtag => (hashtag, 1))

    val hashtagCounts = hashtagKeyValues.reduceByKeyAndWindow((x, y) => x + y, (x, y) => x - y, Seconds(TIME), Seconds(TIME))

    val sortedResults = hashtagCounts.transform(rdd => rdd.sortBy(x => x._2, false))

    sortedResults.print

    ssc.checkpoint("/home/alison/IdeaProjects/resultado")
    ssc.start()
    ssc.awaitTermination()

  }
}