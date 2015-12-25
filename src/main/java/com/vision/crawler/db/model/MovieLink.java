package com.vision.crawler.db.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="MovieLink", catalog="Movie")
public class MovieLink {

	
	private Integer linkId;
	private Movie movie;
	private String link;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "linkId", unique=true, nullable=false)
	public Integer getLinkId() {
		return linkId;
	}
	
	@Column(name="link", nullable=false)
	public String getLink() {
		return link;
	}
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="mId", nullable=false)
	public Movie getMovie() {
		return movie;
	}
	
	public void setLink(String link) {
		this.link = link;
	}
	
	public void setLinkId(Integer linkId) {
		this.linkId = linkId;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}
}
