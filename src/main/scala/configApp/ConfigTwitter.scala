package configApp

object ConfigTwitter {

  /** Configura as credenciais do Twitter, que no caso estão no arquivo apiKeyTitter.txt, para que não fiquem expostas */
  def setupTwitter() = {
    import scala.io.Source
    for (line <- Source.fromFile("/home/alison/IdeaProjects/apiKeyTwitter.txt").getLines) {
      val fields = line.split(" ")
      if (fields.length == 2) {
        System.setProperty("twitter4j.oauth." + fields(0), fields(1))
      }
    }
  }
}
