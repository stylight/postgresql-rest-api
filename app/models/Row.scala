package models


import play.api.libs.json.{JsNumber, JsObject, JsValue, Json}
import java.sql.Timestamp
import xml.Utility.escape

/**
 * Created by Engin Yoeyen on 02/10/14.
 */
class Row {

  private var map = Map.empty[String, Any]

  def select(name: String) = {
    map get name getOrElse Logger.error("method not found")
  }


  def update(name: String)(value: Any) {
    map += name -> value
  }

  def hasElement = map.size > 0

  override def toString = toJson().toString()

  def toJson(): Option[JsObject] = {
    if(hasElement){
      val list = for ((k,v) <- map) yield Json.obj( k -> anyWriter(v))
      Some(list.reduce(_ ++ _))
    }else{
      None
    }
  }

  def toXML(): String = {
    if(hasElement){
      val list = for ((k,v) <- map) yield "<%s>%s</%s>".format(k, escape(v.toString), k)
      list.reduce(_ ++ _)
    }else{
      ""
    }
  }

  private def anyWriter(a:Any): JsValue = {
    a match {
      case a: Double => Json.toJson(a)
      case a: Float => Json.toJson(a)
      case a: Long => Json.toJson(a)
      case a: Int => Json.toJson(a)
      case a: String => Json.toJson(a)
      case a: Boolean => Json.toJson(a)
      case a: Timestamp => Json.toJson(a)
      case a: java.sql.Time => Json.toJson(a)
      case a: Date => Json.toJson(a)
      case a: java.math.BigInteger => Json.toJson(a.toString()) //TEMPORARY SOLUTION WRITE
      case a: BigDecimal => Json.toJson(a)

      case _ => throw new RuntimeException("Type not serializable : "+a)
    }
  }




}

