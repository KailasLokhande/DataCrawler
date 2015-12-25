package com.vision.crawler.impl;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.vision.crawler.Crawler;
import com.vision.crawler.db.MovieDataBaseHandler;

public class BollywoodMovieCrawler implements Crawler {

	private static Map<String, String> visitedLinks = new HashMap<String, String>();
	private static Map<String, MovieDetails> moveDetails = new HashMap<String, MovieDetails>();
	private static AtomicInteger moveCount = new AtomicInteger(0);
	private boolean breakParse = true;
	private MovieDataBaseHandler movieDatabaseHandler = new MovieDataBaseHandler();

	public void processPageUrl(String URL) {
		try {
			if (visitedLinks.containsKey(URL))
				return;
			visitedLinks.put(URL, URL);
			// System.out.println("Processing: " + URL);
			URL url = new URL(URL);
			Document doc = Jsoup.connect(URL).get();
			String name = null, thumbNail = null;
			if (URL.contains("watch-")) {
				// This is detail page of Movie
				// Movie Title , Image and links are given in Center tag, there
				// can be many such center tags, but we are intrested in one
				// which have H1 tag in it. \
				// As per investigation till now we have found that only 1 H1
				// tag is available in page
				Elements centers = doc.select("center");
				for (Element center : centers) {
					Elements h1s = center.select("h1");
					if (h1s.size() > 0) {
						for (Element h1 : h1s) {
							name = h1.text();
						}
						Elements thumbnails = center.select("img[src]");
						for (Element thumbnail : thumbnails) {
							if (!thumbnail.attr("src").endsWith("gif")
									&& thumbnail.attr("src").contains("jpg"))
								thumbNail = thumbnail.attr("src");
						}
					}
				}

				if (name != null
						&& movieDatabaseHandler.getMovie(name).size() > 0)
					return;
			}
			// get all links and recursively call the processPage method
			Elements questions = doc.select("a[href]");
			for (Element link : questions) {
				String nextParseLink = link.attr("href");
				if ("#".equals(nextParseLink.trim()))
					continue;
				if (!nextParseLink.startsWith("http")) {

					String urlPrefix = URL;
					String urlHost = url.getProtocol() + "://"
							+ url.getAuthority();
					if (URL.replace(urlHost, "").lastIndexOf("/") >= 0) {
						urlPrefix = URL.substring(0, URL.lastIndexOf("/"));
					}
					nextParseLink = urlPrefix + "/" + nextParseLink;
					// System.out.println("Created URL for " + link.attr("href")
					// + " As " + nextParseLink + " URL :" + URL);
				}
				boolean br = breakParse;
				breakParse = false;

				// System.out.println(visitedLinks);
				if (!visitedLinks.containsKey(nextParseLink)
						&& nextParseLink.endsWith("php")) {

					MovieDetails movieDetails;
					if (name != null || thumbNail != null) {

						movieDetails = new MovieDetails();
						movieDetails.setName(name);
						movieDetails.setThumbNail(thumbNail);
						// System.out.println("Looking for " + name + URL);
						processPage(nextParseLink, movieDetails);
						if (moveDetails.containsKey(name.toUpperCase().trim())) {
							visitedLinks.put(nextParseLink, nextParseLink);
						}
					} else {
						// System.out.println("Looking "+URL);
						processPageUrl(nextParseLink);
						visitedLinks.put(nextParseLink, nextParseLink);
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		visitedLinks.put(URL, URL);
	}

	private void processPage(String URL, MovieDetails movieDetails) {
		try {
			// System.out.println("PROCESSING " + URL);
			URL url = new URL(URL);

			// System.out.println("Processing " + URL);
			String pattern = url.getAuthority() + "(.*)(full(\\d)*)(.php)";

			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			if (visitedLinks.containsKey(URL))
				return;
			// visitedLinks.put(URL, URL);
			// Now create matcher object.
			Matcher m = r.matcher(URL);
			if (m.find()) {
				processPageForVideoLink(URL, movieDetails);
				visitedLinks.put(URL, URL);
				return;
			}
			// else {
			// processPage(URL);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void processPageForVideoLink(String URL, MovieDetails movieDetails) {
		// System.out.println("VIDEO LINK " + URL + " Name: "
		// + movieDetails.getName());
		try {
			Document doc = Jsoup.connect(URL).get();

			// get all links and recursively call the processPage method
			Elements questions = doc.select("iframe[src]");
			for (Element iframe : questions) {
				String videoFrameLink = iframe.attr("src");
				if (videoFrameLink.contains("player")) {

					Document videoDoc = Jsoup.connect(videoFrameLink).get();
					Elements videoFrames = videoDoc.select("iframe[src]");
					for (Element link : videoFrames) {
						String videoLink = link.attr("src");
						if (videoLink.contains("embed")) {
							if (moveDetails.containsKey(movieDetails.getName()
									.toUpperCase().trim())) {
								movieDetails = moveDetails.get(movieDetails
										.getName().toUpperCase().trim());
								movieDetails.addFullVideoUrl(videoLink);
							} else {
								movieDetails.addFullVideoUrl(videoLink);
								moveDetails.put(movieDetails.getName()
										.toUpperCase().trim(), movieDetails);
								// System.out.println("Added: " + movieDetails);
								System.out.println("Count: "
										+ moveCount.incrementAndGet()
										+ "Name: " + movieDetails.getName());
								;

								break;
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// public static void main(String[] args) throws IOException,
	// InterruptedException {
	// Crawler crawler = new BollywoodMovieCrawler();
	// crawler.processPage("http://hindimovies.bollyv4u.com");
	// System.out.println(moveDetails);
	// System.out.println(moveDetails.size());
	//
	// Gson gson = new Gson();
	// String movieDetails = gson.toJson(moveDetails);
	// File movieDB = new File("C:\\Code\\Folder1\\MovieDB.json");
	// if (movieDB.exists()) {
	// movieDB.delete();
	// Thread.currentThread().sleep(5000);
	// }
	// movieDB.createNewFile();
	// FileWriter writer = new FileWriter(movieDB);
	// writer.write(movieDetails);
	// writer.flush();
	// writer.close();
	// }

	private static void print(String string) {
		System.out.println(string);

	}

	@Override
	public void processPage(String URL) {
		try {
			processPageUrl(URL);
			movieDatabaseHandler.save(moveDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void writeToDB(Map<String, MovieDetails> moveDetails2) {
		// TODO Auto-generated method stub

	}
}
