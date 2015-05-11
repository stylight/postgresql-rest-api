package test

import play.api.test._
import org.specs2.execute.{Result, AsResult}

abstract class App(app:FakeApplication = App.app) extends WithApplication(app) {
  override def around[T: AsResult](t: => T): Result = super.around {
    wipeData()
    val result = t
    result
  }

  def wipeData() {

  }
}


object App {

  def app = FakeApplication(additionalConfiguration =
    Map(
      "db.default.driver" -> "org.postgresql.Driver",
      "db.default.url" -> "jdbc:postgresql://localhost:5432/booktown",
      "evolutionplugin" -> "enabled",
      "db.default.user" -> "",
      "db.default.password" -> "",
      "applyEvolutions.default" -> "true",
      "applyDownEvolutions.default" -> "true"
    )
  )


}