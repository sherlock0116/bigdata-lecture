package cgs.bigdata.kafka

import org.apache.kafka.clients.consumer.ConsumerConfig

import java.util.Properties
import scala.collection.mutable

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object KafkaParamsBuilder {
	
	class Builder {
		private[this] val options: mutable.Map[String, String] = new mutable.HashMap[String, String]()
		private[this] val props: Properties = new Properties
		
		def config(key: String, value: String): Builder = synchronized {
			options += key -> value
			this
		}
		
		def config(key: String, value: Long): Builder = synchronized {
			options += key -> value.toString
			this
		}
		
		def config(key: String, value: Double): Builder = synchronized {
			options += key -> value.toString
			this
		}
		
		def config(key: String, value: Boolean): Builder = synchronized {
			options += key -> value.toString
			this
		}
		
		def bootstraps(servers: String): Builder = synchronized {
			options += "bootstrap.servers" -> servers
			this
		}
		
		def keySerializer(className: String): Builder = synchronized {
			options += "key.serializer" -> className
			this
		}
		
		def valueSerializer(className: String): Builder = synchronized {
			options += "value.serializer" -> className
			this
		}
		
		def keyDeserializer(className: String): Builder = synchronized {
			options += "key.deserializer" -> className
			this
		}
		
		def valueDeserializer(className: String): Builder = synchronized {
			options += "value.deserializer" -> className
			this
		}
		
		def groupId(grpId: String): Builder = synchronized {
			options += "value.deserializer" -> grpId
			this
		}
		
		def transactionId(transId: String): Builder = synchronized {
			options += "transactional.id" -> transId
			this
		}
		
		def build(): Properties = synchronized {
			options foreach { case (k, v) =>
				props.put(k, v)
			}
			props
		}
		
	}
	
	def builder(): Builder = new Builder
}
