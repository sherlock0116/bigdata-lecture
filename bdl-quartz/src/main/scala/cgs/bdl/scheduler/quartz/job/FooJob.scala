package cgs.bdl.scheduler.quartz.job

import org.quartz.{Job, JobDataMap, JobExecutionContext}

import java.time.LocalDateTime

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
class FooJob extends Job {
	
	override def execute(context: JobExecutionContext): Unit = {
		val jobDataMap: JobDataMap = context.getJobDetail.getJobDataMap
		val name: String = jobDataMap.get("name").toString
		println(s"Job: ${name} executed at ${LocalDateTime.now()}")
	}
}
