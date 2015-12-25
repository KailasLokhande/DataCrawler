package com.vision.crawler.db.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;

@Entity
@Table( name = "Movie", catalog="Movie")
public class Movie implements java.io.Serializable {
	
	private Integer mId;
	
	private String name;
	
	private String releaseYear;
	
	private String posterUrl;
	
	private Set<MovieLink> movieLinks = new HashSet<MovieLink>();
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	@Column(name = "mid", unique=true, nullable=false)
	public Integer getmId() {
		return mId;
	}
	
	public void setmId(Integer mId) {
		this.mId = mId;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "movie")
	public Set<MovieLink> getMovieLinks() {
		return movieLinks;
	}
	
	public void setMovieLinks(Set<MovieLink> movieLinks) {
		this.movieLinks = movieLinks;
	}
	
	@Column(name="name", unique=false, nullable=false)
	public String getName() {
		return name;
	}
	
	public void setName(String movieName) {
		this.name = movieName;
	}
	
	@Column(name="relaseYear", unique=false, nullable=true)
	public String getReleaseYear() {
		return releaseYear;
	}
	
	public void setReleaseYear(String releaseYear) {
		this.releaseYear = releaseYear;
	}
	
	@Column(name="posterUrl", unique=true, nullable=true)
	public String getPosterUrl() {
		return posterUrl;
	}
	
	public void setPosterUrl(String posterUrl) {
		this.posterUrl = posterUrl;
	}
}
