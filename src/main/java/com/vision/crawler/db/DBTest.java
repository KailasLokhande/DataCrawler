package com.vision.crawler.db;

import java.util.List;

import com.vision.crawler.db.model.Movie;

public class DBTest {

	
	public static void main(String[] args) {
		
		
		System.out.println("Movie DB Test Class");
		MovieDataBaseHandler handler = new MovieDataBaseHandler();
		List<Movie> movies = handler.getMoviesByName("sa");
		System.out.println(movies.size());
		for(Movie movie: movies) {
			System.out.println("MOVIE FOUND: "+ movie.getName() + " ID: "+ movie.getmId());
		}
	}
}
