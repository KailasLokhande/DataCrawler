package com.vision.crawler.impl;

import java.util.ArrayList;
import java.util.List;

import javax.swing.Box.Filler;

public class MovieDetails {

	private String name;
	private String thumbNail;
	private String releaseYear;
	
	private List<String> fullVideoUrls = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public String getThumbNail() {
		return thumbNail;
	}

	public void setThumbNail(String thumbNail) {
		this.thumbNail = thumbNail;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getFullVideoUrls() {
		return fullVideoUrls;
	}

	public void addFullVideoUrl(String url) {
		fullVideoUrls.add(url);
	}

	public String getReleaseYear() {
		return releaseYear;
	}
	
	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof MovieDetails) {
			MovieDetails otherMovie = (MovieDetails) obj;
			return name.equals(otherMovie.name);
		}
		return false;
	}

	@Override
	public String toString() {
		return "Movie Name: " + name + " \n " + "Movie Thumbnail: " + thumbNail
				+ " \n Movie URL: " + fullVideoUrls;
	}

	public String getRelaseYear() {
		// TODO Auto-generated method stub
		return null;
	}
}
