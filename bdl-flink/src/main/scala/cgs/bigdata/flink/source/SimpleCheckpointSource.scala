package cgs.bigdata.flink.source

import cgs.bigdata.util.log.Logging
import org.apache.flink.api.common.state.{ListState, ListStateDescriptor}
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.runtime.state.{FunctionInitializationContext, FunctionSnapshotContext}
import org.apache.flink.streaming.api.checkpoint.CheckpointedFunction
import org.apache.flink.streaming.api.functions.source.{RichParallelSourceFunction, SourceFunction}

import java.time.Instant
import java.util.concurrent.TimeUnit
import scala.util.Random
import scala.collection.JavaConversions._

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class SimpleCheckpointSource(interval: Long = 1000) extends RichParallelSourceFunction[(Long, Long, String)] with CheckpointedFunction with Logging {
	
	@volatile private var isRunning: Boolean = true
	// 保存消费偏移量 offset
	private var offsetState: ListState[Long] = _
	// offset的状态名
	private val OFFSET_STATE_NAME = "offset-state"
	//这里是long类型,初始值是0
	private var index: Long = _
	// 当前任务实例的编号
	private var indexOfTask: Int = _
	// ListStateDescriptor
	private var offsetStateDescriptor: ListStateDescriptor[Long] = _
	private val urls = Seq("baidu", "google", "biyin", "sogou")
	
	override def run(ctx: SourceFunction.SourceContext[(Long, Long, String)]): Unit = {
		while (isRunning) {
			val epochMilli: Long = Instant.now().toEpochMilli
			val url: String = urls(Random.nextInt(urls.size))
			index += 1
			ctx.collect(index, epochMilli, url)
			if (interval > 0) TimeUnit.MILLISECONDS.sleep(interval)
		}
	}
	
	override def cancel(): Unit = isRunning = false
	
	override def initializeState(context: FunctionInitializationContext): Unit = {
		this.indexOfTask = getRuntimeContext.getIndexOfThisSubtask
		this.offsetStateDescriptor = new ListStateDescriptor[Long](OFFSET_STATE_NAME, createTypeInformation[Long])
		// job 第一次或者异常恢复都会进入,设置状态
		this.offsetState = context.getOperatorStateStore.getListState(offsetStateDescriptor)
		val longs: Iterable[Long] = this.offsetState.get()
		for (offsetTemp <- longs) {
			this.index = offsetTemp
			logger.info(s"====> Index of task: $indexOfTask, 从第 $index 条数据开始处理.....")
//			if (this.index == 4 || this.index == 9) this.index += 1
//			logger.error(s"重启task编号:${this.indexOfTask} offset:${this.index}")
		}
	}
	
	override def snapshotState(context: FunctionSnapshotContext): Unit = {
		if (isRunning) {
			// 清空并设置状态
			this.offsetState.clear()
			//				this.offsetState.update(index);
			this.offsetState.add(index)
		} else {
			logger.error("停止的 source 被调用 snapshotState()");
		}
		
	}
}
