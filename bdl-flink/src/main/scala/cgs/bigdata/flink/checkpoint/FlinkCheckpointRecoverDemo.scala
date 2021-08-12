package cgs.bigdata.flink.checkpoint

import cgs.bigdata.flink.FlinkUtils.{enableCheckpoint, fixedDelayRestart, setLocalFsStateBackend, setParallism}
import cgs.bigdata.flink.source.SimpleCheckpointSource
import org.apache.flink.api.common.functions.RichMapFunction
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FlinkCheckpointRecoverDemo {
	
	def main(args: Array[String]): Unit = {
		
		import org.apache.flink.api.scala._
		
		implicit val sEnv: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment
		setParallism()
		enableCheckpoint()
		setLocalFsStateBackend()
		fixedDelayRestart(1, 2000)
		
		sEnv.addSource[(Long, Long, String)](new SimpleCheckpointSource)
				.map(new ErrorMapFunction)
				.print()
		
		sEnv.execute("Checkpoint Recover Demo")
	}
	
	class ErrorMapFunction extends RichMapFunction[(Long, Long, String), (Long, Long, String)] {
		
		override def map(value: (Long, Long, String)): (Long, Long, String) = {
			if (value._1 == 11) throw new RuntimeException(s"==> 数据 No.${value._1} 出错")
			value
		}
	}
	
}
