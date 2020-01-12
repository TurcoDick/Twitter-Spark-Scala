import ConfigApp.{ConfigConstants}
import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object PopularHashTags {

  def main(args: Array[String]): Unit = {

    val configTime : Int = new ConfigConstants().getTimeRun()

    ConfigApp.ConfigTwitter.setupTwitter()

    val ssc = SparkStreaming.InstanceSparkStreaming.ssc

    ErrorMessage.Message.setupLoggin()

    val tweets = TwitterUtils.createStream(ssc, None)

    val statuses = tweets.map(status => status.getText())

    val tweetwords = statuses.flatMap(tweetText => tweetText.split("StatusJSONImpl"))

    val hashtags = tweetwords.filter(word => word.startsWith("#"))

    val hashtagKeyValues = hashtags.map(hashtag => (hashtag, 1))

    val hashtagCounts = hashtagKeyValues.reduceByKeyAndWindow(
      (x, y) => x + y, (x, y) => x - y, Seconds(configTime), Seconds(configTime)
    )

    val sortedResults = hashtagCounts.transform(rdd => rdd.sortBy(x => x._2, false))

    sortedResults.print
    //tweets.print()

    ssc.checkpoint("/home/alison/IdeaProjects/resultado")
    ssc.start()
    ssc.awaitTermination()

  }
}