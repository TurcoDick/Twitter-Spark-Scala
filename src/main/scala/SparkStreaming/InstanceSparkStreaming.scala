package SparkStreaming

import ConfigApp.ConfigConstants
import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object InstanceSparkStreaming {

  val time = new ConfigConstants().getTimeRun()

  val conf = new SparkConf().setAppName("AnalitycsWorlds").setMaster("local[*]")

  val ssc = new StreamingContext(conf, Seconds(time))
}
