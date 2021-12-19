package cgs.bdl.scheduler.quartz

import cgs.bdl.scheduler.quartz.job.FooJob
import org.quartz.impl.StdSchedulerFactory
import org.quartz.{JobBuilder, JobDetail, Scheduler, SimpleScheduleBuilder, Trigger, TriggerBuilder}

/**
 * @Description TODO
 * @Author sherlock
 * @Date
 */
object FooJobMain {
	
	def main(args: Array[String]): Unit = {
		
		val fooJob: JobDetail = JobBuilder.newJob(classOf[FooJob])
				.withIdentity("foo-job", "JobGroup1")
				.build()
		
		val trigger: Trigger = TriggerBuilder.newTrigger()
				.withIdentity("foo-trigger")
				.startNow()
				.withSchedule(
					SimpleScheduleBuilder
							.simpleSchedule()
							.withIntervalInSeconds(3)
							.repeatForever()
				).build()
		
		val defaultScheduler: Scheduler = StdSchedulerFactory.getDefaultScheduler
		defaultScheduler.scheduleJob(fooJob, trigger)
		defaultScheduler.start()
		
	}
}
