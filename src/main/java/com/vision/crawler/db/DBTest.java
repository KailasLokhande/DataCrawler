package com.vision.crawler.db;

import org.hibernate.Session;

import com.vision.crawler.db.model.Movie;
import com.vision.crawler.db.model.MovieLink;
import com.vision.crawler.db.util.HibernateUtil;

public class DBTest {

	
	public static void main(String[] args) {
		
		
		System.out.println("Movie DB Test Class");
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Movie movie = new Movie();
		movie.setName("Dummy");
		movie.setPosterUrl("dummyUrl");
		movie.setReleaseYear("2015");
		session.save(movie);
		
		MovieLink link1 = new MovieLink();
		link1.setLink("Dummy Movie Link");
		link1.setMovie(movie);
		
		MovieLink link2 = new MovieLink();
		link2.setLink("Dummy Movie Link2");
		link2.setMovie(movie);
		
		movie.getMovieLinks().add(link1);
		
		movie.getMovieLinks().add(link2);
		session.save(link1);
		session.save(link2);
		
		session.getTransaction().commit();
		
	}
}
