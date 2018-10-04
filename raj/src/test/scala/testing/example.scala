package testing

import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import java.util.{Date, Properties}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}



object example  extends App {
   println( "Hello World!" )
   val formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
  val simIDs = 10000 to 99999 //99000
  val brokers = "107.6.151.182:8092";
  val topic = "axestrack1";
  val props = new Properties
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "Producer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  val producer = new KafkaProducer[Integer, String](props)
  val count = 0;
  while (true) {
        
      
      println("Messages : ",count);

      producer.send(new ProducerRecord[Integer, String](topic, "hii"))


    
    println("-------------------------------"+new Date())
    TimeUnit.SECONDS.sleep(1)
  }
}