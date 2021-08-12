package cgs.bigdata.hadoop.mapreduce.wordcount

import org.apache.hadoop.io._
import org.apache.hadoop.mapreduce.{Mapper, Reducer}

import java.{lang, util}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object WordCountMR {
	
	private val regex_is_number = "^[0-9]+(.[0-9]+)?$"
	
	def formatWord(word: String): String = {
		require(word.nonEmpty)
		var _word: String = word.trim
		while (_word.contains("(") || _word.contains(")") || _word.contains(",") || _word.contains(".")) {
			_word = _word.replace("(", "")
					.replace(")", "")
					.replace(",", "")
					.replace(".", "")
		}
		_word
	}
	
	def isNumberic(text: String): Boolean = {
		text.matches(regex_is_number)
	}
	
	def nonNumberic(text: String): Boolean = !isNumberic(text)
	
}

class WcMapper extends Mapper[LongWritable, Text, Text, IntWritable] {
	
	import WordCountMR._
	
	private val k: Text = new Text()
	private val one: IntWritable = new IntWritable(1)
	
	override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, IntWritable]#Context): Unit = {
		// split, format and filter
		val words: Array[String] = value.toString.split("\\s+")
				.filter(_.nonEmpty)
				.map(formatWord)
				.filter(nonNumberic)
				.map(_.toLowerCase)
		// write
		words.foreach {word =>
			k.set(word)
			context.write(k, one)
		}
	}
}

class WcReducer extends Reducer[Text, IntWritable, Text, IntWritable] {
	
	private val sum: IntWritable = new IntWritable()
	
	override def reduce(key: Text, values: lang.Iterable[IntWritable], context: Reducer[Text, IntWritable, Text, IntWritable]#Context): Unit = {
		var acc: Int = 0
		val itrt: util.Iterator[IntWritable] = values.iterator()
		while (itrt.hasNext) {
			acc += itrt.next().get()
		}
		sum.set(acc)
		context.write(key, sum)
	}
}