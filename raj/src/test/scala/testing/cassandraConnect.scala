package testing

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Date
import com.datastax.spark.connector.cql.CassandraConnector
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable.ListBuffer
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.datastax.driver.core.Cluster
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.toNamedColumnRef
import com.datastax.spark.connector.toRDDFunctions
import org.apache.commons.collections.ListUtils
import java.util.Calendar
import java.util.Locale

object cassandraConnect {
  def main(args: Array[String]) {
      
      val conf = new SparkConf()
      .setMaster("local[*]")
      .setAppName("connectToCassandra")
      .set("spark.cassandra.connection.host", "107.6.151.182")
      .set("spark.cassandra.connection.keep_alive_ms", "20000")
      .set("spark.executor.memory", "1g")
      .set("spark.driver.memory", "1g")
      .set("spark.submit.deployMode", "cluster")
      .set("spark.executor.instances", "4")
      .set("spark.executor.cores", "1")
    
    val sc = new SparkContext(conf)
    val sqlContext = new SQLContext(sc)
    val df = sqlContext
    .read
    .format("org.apache.spark.sql.cassandra")
    .options(Map("table" -> "t2", "keyspace" -> "test")).load.cache()
    val newDF = df.take(10);
    //df.rdd.saveAsTextFile("/home/raj/Desktop/DATAFRAME")
    val noOfrows = df.count();
    df.foreach { row => 
          println(row)
           row.toSeq.foreach{col => println(col) }
    }
    println("No Of Rows Found : ", noOfrows);
}
    
  
}