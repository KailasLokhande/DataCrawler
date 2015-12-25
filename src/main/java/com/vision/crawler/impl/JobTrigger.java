package com.vision.crawler.impl;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

public class JobTrigger {

		public static void main(String[] args) throws Exception {
			JobDetail job = JobBuilder.newJob(CrawlerJob.class).withIdentity("MovieCrawl", "Crawler").build();
			Trigger trigger = TriggerBuilder.newTrigger().withIdentity("MovieCrawl", "Crawler").withSchedule(CronScheduleBuilder.cronSchedule("0/5 * * * * ?")).build();
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();
			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		}
}
