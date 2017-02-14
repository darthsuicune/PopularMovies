package com.dlgdev.popularmovies.data.movies;

import com.dlgdev.popularmovies.data.reviews.Review;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * {
 * "poster_path":"\/zSouWWrySXshPCT4t3UKCQGayyo.jpg",
 * "adult":false,
 * "overview":"Since the dawn of civilization, he was worshipped as a god. Apocalypse, the first and most powerful mutant from Marvel’s X-Men universe, amassed the powers of many other mutants, becoming immortal and invincible. Upon awakening after thousands of years, he is disillusioned with the world as he finds it and recruits a team of powerful mutants, including a disheartened Magneto, to cleanse mankind and create a new world order, over which he will reign. As the fate of the Earth hangs in the balance, Raven with the help of Professor X must lead a team of young X-Men to stop their greatest nemesis and save mankind from complete destruction.",
 * "release_date":"2016-05-18",
 * "genre_ids":[
 * 28,
 * 12,
 * 14,
 * 878
 * ],
 * "remoteId":246655,
 * "original_title":"X-Men: Apocalypse",
 * "original_language":"en",
 * "title":"X-Men: Apocalypse",
 * "backdrop_path":"\/oQWWth5AOtbWG9o8SCAviGcADed.jpg",
 * "popularity":72.291655,
 * "vote_count":404,
 * "video":false,
 * "vote_average":5.85
 * },
 */
public class Movie {

	public long rowId;
	public String posterPath;
	public boolean adult;
	@SerializedName("overview") public String plotOverview;
	public String releaseDate;
	public int[] genreIds;
	@SerializedName("id") public int remoteId;
	public String originalTitle;
	public String title;
	public String backdropPath;
	public double popularity;
	public int voteCount;
	public boolean video;
	public double voteAverage;
	public boolean favorite;
	public List<Review> reviews;
	public List<Trailer> trailers;

	public Movie() {
	}

	public Movie(String title, String posterPath, String plotOverview, String releaseDate,
				 int remoteId, double voteAverage) {
		this.title = title;
		this.posterPath = posterPath;
		this.plotOverview = plotOverview;
		this.releaseDate = releaseDate;
		this.remoteId = remoteId;
		this.voteAverage = voteAverage;
	}

	public String poster() {
		return posterPath;
	}
}
