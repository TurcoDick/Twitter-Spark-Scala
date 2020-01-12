package errorMessage

object Message {

  /** Gerencia as mensagens de erro **/
  def setupLoggin() = {
    import org.apache.log4j.{Level, Logger}
    val rootLogger = Logger.getRootLogger()
    rootLogger.setLevel(Level.ERROR)
  }
}
