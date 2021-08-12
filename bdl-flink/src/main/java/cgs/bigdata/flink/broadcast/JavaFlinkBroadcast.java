package cgs.bigdata.flink.broadcast;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class JavaFlinkBroadcast {

	public static void main (String[] args) throws Exception {

		final StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		DataStream<String> sourceStream = sEnv.fromCollection(Arrays.asList("java", "scala", "go", "java", "python", "ruby", "java")).name("colletion source").uid("collection source");

		sourceStream.print();

		sEnv.execute("Flink Java Broadcast");
	}
}
