package cgs.bdl.hadoop.mapred.example

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{LongWritable, Text}
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat
import org.apache.hadoop.mapreduce.{Job, Mapper, Reducer}

import java.lang

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object WordCountApp {
	
	private val input_path = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-hadoop/data/example.txt"
	private val output_path = "/Users/sherlock/IdeaProjects/Bigdata/bigdata-lecture/bdl-hadoop/output"
	
	def main(args: Array[String]): Unit = {
		
		val configuration: Configuration = new Configuration()
		val job: Job = Job.getInstance(configuration, WordCountApp.getClass.getSimpleName)
		job.setJarByClass(WordCountApp.getClass)
		
		FileInputFormat.setInputPaths(job, input_path)
		job.setMapperClass(classOf[LineSplitMapper])
		job.setMapOutputKeyClass(classOf[Text])
		job.setMapOutputValueClass(classOf[LongWritable])
		
		job.setReducerClass(classOf[WordReducer])
		job.setOutputKeyClass(classOf[Text])
		job.setOutputValueClass(classOf[LongWritable])
		FileOutputFormat.setOutputPath(job, new Path(output_path))
		
		job.waitForCompletion(true);
	}
}

class LineSplitMapper extends Mapper[LongWritable, Text, Text, LongWritable] {
	
	override def map(key: LongWritable, value: Text, context: Mapper[LongWritable, Text, Text, LongWritable]#Context): Unit = {
		value.toString.trim.split("\\s+").foreach {word =>
			context.write(new Text(word), new LongWritable(1))
		}
	}
}

class WordReducer extends Reducer[Text, LongWritable, Text, LongWritable] {
	
	override def reduce(key: Text, values: lang.Iterable[LongWritable], context: Reducer[Text, LongWritable, Text, LongWritable]#Context): Unit = {
		
		var acc: Long = 0L
		values.forEach {cnt => acc += cnt.get()}
		val accWritable: LongWritable = new LongWritable(acc)
		context.write(key, accWritable)
	}
}