package testing

import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.redis._

object connectRedis {
  def main(args: Array[String]) {
    val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("RecalculateOdo For History Data")
      .set("redis.host", "107.6.151.182")
      .set("redis.port", "7001")
      .set("spark.executor.memory", "1g")
      .set("spark.driver.memory", "1g")
      .set("spark.submit.deployMode", "cluster")
      .set("spark.executor.instances", "4")
      .set("spark.executor.cores", "1")
      
      val r = new RedisClient("107.6.151.182", 7001)
      val keys = "keys"
      println(r.keys("*"))
      println(r.get("UserGroups"))
  }
}