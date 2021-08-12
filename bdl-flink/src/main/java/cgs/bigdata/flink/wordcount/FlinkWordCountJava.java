package cgs.bigdata.flink.wordcount;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.Arrays;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class FlinkWordCountJava {

	public static void main (String[] args) throws Exception {

		StreamExecutionEnvironment sEnv = StreamExecutionEnvironment.getExecutionEnvironment();
		sEnv.setParallelism(1);
		sEnv.fromCollection(Arrays.asList("a", "b", "c", "a", "a", "b", "a"))
				.map(new MapFun())
				.keyBy(new KeySelect())
				.sum(1)
				.print();
		sEnv.execute("Flink WordCount");
	}

	private static class KeySelect implements KeySelector<Tuple2<String, Integer>, String> {
		@Override
		public String getKey (Tuple2<String, Integer> value) throws Exception {
			return value.f0;
		}
	}

	private static class MapFun implements MapFunction<String, Tuple2<String, Integer>> {
		@Override
		public Tuple2<String, Integer> map (String value) throws Exception {
			return new Tuple2<>(value, 1);
		}
	}
}
