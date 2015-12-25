package com.vision.crawler.impl;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import com.vision.crawler.Crawler;

public class CrawlerJob implements Job {

	@Override
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		System.out.println("Executing Movie Crawlere At "+ new Date());
		
		Crawler crawler = new BollywoodMovieCrawler();
		crawler.processPage("http://hindimovies.bollyv4u.com");
	}
	
	public static void main(String[] args) {
		Crawler crawler = new BollywoodMovieCrawler();
		crawler.processPage("http://hindimovies.bollyv4u.com");
	}

}
