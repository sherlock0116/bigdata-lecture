package cgs.bigdata.hadoop.mapreduce.wordcount

import org.apache.commons.io.FileUtils
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.Path
import org.apache.hadoop.io.{IntWritable, Text}
import org.apache.hadoop.mapreduce.Job
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat

import java.io.File

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object WordCountDriver {
	
	private val config = new Configuration
	private val default_input_path = "datas/README.txt"
	private val default_output_path = "results/mr/wordcount"
	private var input_path: String = _
	private var output_path: String = _
	
	
	def main(args: Array[String]): Unit = {
		if (args.length < 2) {
			input_path = default_input_path
			output_path = default_output_path
		} else {
			input_path = args(0)
			output_path = args(1)
		}
		checkOutputDir(output_path)
		val job: Job = Job.getInstance(config)
		job.setJarByClass(this.getClass)
		job.setMapperClass(classOf[WcMapper])
		job.setReducerClass(classOf[WcReducer])
		job.setMapOutputKeyClass(classOf[Text])
		job.setMapOutputValueClass(classOf[IntWritable])
		job.setOutputKeyClass(classOf[Text])
		job.setOutputValueClass(classOf[IntWritable])
		FileInputFormat.setInputPaths(job, new Path(input_path))
		FileOutputFormat.setOutputPath(job, new Path(output_path))
		job.waitForCompletion(true)
	}
	
	def checkOutputDir(outputDir: String): Unit = {
		require(outputDir.nonEmpty)
		val file: File = new File(outputDir)
		if (file.exists()) FileUtils.deleteDirectory(file)
	}
}
