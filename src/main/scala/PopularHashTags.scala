import org.apache.spark.streaming.twitter.TwitterUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

object PopularHashTags {

  def main(args: Array[String]): Unit = {

    /** inicializa as configurações de acesso ao Twitter */
    configApp.ConfigTwitter.setupTwitter()

    /** cria o contexto do sparkStreaming */
    val ssc = confSparkStreaming.InstanceSparkStreaming.ssc

    /** manda algum erro se houver */
    errorMessage.Message.setupLoggin()

    /** cria um DStream utilizando a lib que o Twitter disponibiliza*/
    val tweets = TwitterUtils.createStream(ssc, None)

    /** Dentro do json tem uma chave  chamada "text", abaixo pegamos o valor correspondente a essa chave*/
    val statuses = tweets.map(status => status.getText())

    /** Aqui estamos criando tuplas usando o espaço como separador */
    val tweetwords = statuses.flatMap(tweetText => tweetText.split(" "))

    /** Estamos filtrando tudo o que tenha "#", só isso nós importa agora */
    val hashtags = tweetwords.filter(word => word.startsWith("#"))

    /** criando um par de chave valor */
    val hashtagKeyValues = hashtags.map(hashtag => (hashtag, 1))

    /** Configuração da exibição, vamos ver um resultado de cada 300 segundos passados (5 minutos), mas a janela
     * vai se mexer a cada 1 segundo */
    val hashtagCounts = hashtagKeyValues.reduceByKeyAndWindow(
      (x, y) => x + y, (x, y) => x - y, Seconds(300), Seconds(1)
    )

    val sortedResults = hashtagCounts.transform(rdd => rdd.sortBy(x => x._2, false))

    sortedResults.print
    //tweets.print()

    ssc.checkpoint("/home/alison/IdeaProjects/resultado")
    ssc.start()
    ssc.awaitTermination()

  }
}