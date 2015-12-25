package com.vision.crawler.db;

import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;

import com.vision.crawler.db.model.Movie;
import com.vision.crawler.db.model.MovieLink;
import com.vision.crawler.db.util.HibernateUtil;
import com.vision.crawler.impl.MovieDetails;

public class MovieDataBaseHandler {

	public static void save(Map<String, MovieDetails> details) {
		for (MovieDetails movieDetails : details.values()) {
			saveMovie(movieDetails);
		}
	}

	public static void saveMovie(MovieDetails movieDetails) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		session.beginTransaction();
		Movie movie = new Movie();
		movie.setName(movieDetails.getName());
		movie.setPosterUrl(movieDetails.getThumbNail());
		movie.setReleaseYear(movieDetails.getRelaseYear());
		session.save(movie);

		for (String link : movieDetails.getFullVideoUrls()) {
			MovieLink dbLink = new MovieLink();
			dbLink.setLink(link);
			dbLink.setMovie(movie);
			movie.getMovieLinks().add(dbLink);
			session.save(dbLink);
		}

		session.getTransaction().commit();
		session.close();
	}

	public List<Movie> getMovie(String name) {

		String hql = "FROM Movie E WHERE E.name = :name";// AND E.relaseYear = :releaseYear";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List<Movie> results = query.list();
		session.close();
		return results;

	}

}
