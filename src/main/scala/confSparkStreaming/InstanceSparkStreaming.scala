package confSparkStreaming

import org.apache.spark.SparkConf
import org.apache.spark.streaming.{Seconds, StreamingContext}

object InstanceSparkStreaming {

  val sizeLote : Int = 1

  /** Queremos que rode em todos os núcleos 'local[*]' os nucleos chamamos de "AnalitycsWorlds" */
  val conf = new SparkConf().setAppName("AnalitycsWorlds").setMaster("local[*]")

  /** cria um contexto para o sparkStream passando como parâmetros
   *  as configurações e o tamanho do lote, que é medido em tempo de segundos */
  val ssc = new StreamingContext(conf, Seconds(sizeLote))
}
