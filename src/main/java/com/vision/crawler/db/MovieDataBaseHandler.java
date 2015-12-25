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

		String hql = "FROM Movie E WHERE E.name = :name";// AND E.relaseYear =
															// :releaseYear";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		query.setParameter("name", name);
		List<Movie> results = query.list();
		session.close();
		return results;

	}

	public long getMovieCount() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery("select count(*) from Movie");
		// query.setString("email", "something");
		// query.setString("password", "password");
		Long count = (Long) query.uniqueResult();
		session.close();
		return count;
	}

	public long getMinMovieId() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery("select min(mid) from Movie");
		// query.setString("email", "something");
		// query.setString("password", "password");
		Long count = (Long) query.uniqueResult();
		session.close();
		return count;
	}

	public long getMaxMovieId() {

		Session session = HibernateUtil.getSessionFactory().openSession();

		Query query = session.createQuery("select max(mid) from Movie");
		// query.setString("email", "something");
		// query.setString("password", "password");
		Long count = (Long) query.uniqueResult();
		session.close();
		return count;
	}

	public List<Movie> getMoviesById(int startFrom, int count) {
		String hql = "FROM Movie E WHERE E.mId >= :start AND E.mID < :end";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		query.setParameter("start", startFrom);
		query.setParameter("end", startFrom + count);
		List<Movie> results = query.list();
		session.close();
		return results;
	}

	public List<Movie> getMoviesByName(String nameLike) {
		String hql = "FROM Movie E WHERE UPPER(E.name) like UPPER(:query) ORDER BY E.mId";
		Session session = HibernateUtil.getSessionFactory().openSession();
		Query query = session.createQuery(hql);
		query.setParameter("query", "%" + nameLike + "%");
		List<Movie> results = query.list();
		session.close();
		return results;
	}

}
