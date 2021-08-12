package cgs.bigdata.flink.state;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.checkpoint.ListCheckpointed;

import java.util.Collections;
import java.util.List;

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
public class AccMapFunction implements MapFunction<String, Integer>, ListCheckpointed<Integer> {

	private int acc = 0;

	@Override
	public Integer map (String value) throws Exception {
		acc ++;
		return acc;
	}

	@Override
	public List<Integer> snapshotState (long checkpointId, long timestamp) throws Exception {
		return Collections.singletonList(acc);
	}

	@Override
	public void restoreState (List<Integer> state) throws Exception {
		for (Integer e : state) {
			acc += e;
		}
	}
}
