package testing
import java.util.Arrays
import scala.collection.JavaConverters._
import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.serialization.StringDeserializer 
import java.util.{Date, Properties}
import org.apache.kafka.clients.consumer.KafkaConsumer


object kafkaConsumer {
  def main(args: Array[String]): Unit = {
    val TOPIC="axestrack1"

  val  props = new Properties()
  props.put("bootstrap.servers", "107.6.151.182:8092")

  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("group.id", "ax12")
  
  
  val consumer = new KafkaConsumer[String, String](props)

  
  consumer.subscribe(java.util.Arrays.asList("axestrack1"))
  while(true){
    val records=consumer.poll(100)
    for (record<-records.asScala){
     println(record)
    }
  }
  

}
}