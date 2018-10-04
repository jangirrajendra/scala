package testing
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions
import org.apache.kafka.common.serialization.StringSerializer
import java.text.SimpleDateFormat
import java.util.ArrayList
import scala.util.parsing.json._
import scala.util.parsing.json._
import java.util.Date
import scala.collection.mutable.ListBuffer
import org.apache.spark.sql._
import kafka.serializer.StringDecoder
import com.datastax.spark.connector.cql.CassandraConnector
import scala.collection.JavaConverters.asScalaBufferConverter
import scala.collection.mutable.ListBuffer
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.storage.StorageLevel
import com.datastax.driver.core.Cluster
import com.datastax.spark.connector.SomeColumns
import com.datastax.spark.connector.toNamedColumnRef
import com.datastax.spark.connector.toRDDFunctions
import org.apache.commons.collections.ListUtils
import java.util.Calendar
import net.liftweb.json.parse
import java.util.Locale
import org.apache.spark.streaming.Seconds
import org.apache.spark.streaming.StreamingContext
import org.apache.avro.data.Json
import net.liftweb.json.DefaultFormats


case class kafkaData(imei: Int, imeicount: String)


object kafkaStream extends Serializable {
  def parsingMethod(json: String): kafkaData = {
    //println("JSON : "+json.toString())
    implicit val formats = DefaultFormats
    val parsedJson = parse(json)
    //println("parsedJson : "+parsedJson);
    val getJson = parsedJson.extract[kafkaData]
    //println("getJson : "+getJson.toString());
    //println("imei = "+getJson.imei)
    //println("imeicount = "+getJson.imeicount)
    
    
    return kafkaData(getJson.imei, getJson.imeicount)
    
  }
  
  def main(args: Array[String]) {
    val conf = new SparkConf()
      //.setMaster("spark://192.168.0.40:7077")
      .setMaster("local[2]")
      .setAppName("connectToCassandra")
      .set("spark.cassandra.connection.host", "107.6.151.182")
      .set("spark.cassandra.connection.keep_alive_ms", "20000")
      .set("spark.executor.memory", "1g")
      .set("spark.driver.memory", "2g")
      .set("spark.submit.deployMode", "cluster")
      .set("spark.executor.instances", "1")
      .set("spark.executor.cores", "1")
      .set("spark.cores.max", "4")
 
    val spark = SparkSession
      .builder
      .appName("connectToCassandra")
      .config(conf)
      //.master("spark://192.168.0.40:7077")
      .getOrCreate()
    
    val sc = SparkContext.getOrCreate(conf)
    val ssc = new StreamingContext(sc, Seconds(2));
    val topics = Map("axestrack1" -> 1)
    val kafkaParams = Map[String, String](
      "zookeeper.connect" -> "107.6.151.182:2185",
      "group.id" -> "rajConsumer",
      "auto.offset.reset" -> "largest")
    import spark.implicits._
    val kafkaStream = KafkaUtils.createStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topics, StorageLevel.MEMORY_AND_DISK_SER) 
    kafkaStream.map(_._2).foreachRDD(rdd => {
      
      if(!rdd.isEmpty()){
          rdd.foreach(f => {
      //println(f.toString())
        
      }
      )
      }
      else{
        println("EMPTY RDD");
      }
    }
    )
    val parsedJson = kafkaStream.map(_._2).map(parsingMethod)
    parsedJson.foreachRDD(rdds =>{
      if(!rdds.isEmpty()){
        val schema = StructType(Array(StructField("imei",IntegerType,true),StructField("imeicount",StringType,true)))
        val df = rdds.toDF()
        //df.show()
        println("*********PRINTING DF SCHEMA******************")
        df.printSchema()
        //df.write.format("org.apache.spark.sql.cassandra").options(Map("table" -> "imeicount", "keyspace" -> "test"))
        //    .mode(SaveMode.Append).save()
        print("Data Has Been Inserted Into Cassandra");
        
      
      rdds.foreach( k => {
        //println(k.toString());
      }
      )
      }
      else{
        println("EMPTY RDD")
      }
      
      
    }
    )
    
    ssc.start();
    ssc.awaitTermination();
  }
}