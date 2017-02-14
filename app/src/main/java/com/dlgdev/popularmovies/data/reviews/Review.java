package com.dlgdev.popularmovies.data.reviews;

import android.database.Cursor;

import com.dlgdev.popularmovies.data.db.MoviesContract;
import com.google.gson.annotations.SerializedName;

/**
 * {
 *  "id": "5010553819c2952d1b000451",
 *  "author": "Travis Bell",
 *  "content": "I felt like this was a tremendous end to Nolan's Batman trilogy. The Dark Knight Rises may very well have been the weakest of all 3 films but when you're talking about a scale of this magnitude, it still makes this one of the best movies I've seen in the past few years.\r\n\r\nI expected a little more _Batman_ than we got (especially with a runtime of 2:45) but while the story around the fall of Bruce Wayne and Gotham City was good I didn't find it amazing. This might be in fact, one of my only criticisms—it was a long movie but still, maybe too short for the story I felt was really being told. I feel confident in saying this big of a story could have been split into two movies.\r\n\r\nThe acting, editing, pacing, soundtrack and overall theme were the same 'as-close-to-perfect' as ever with any of Christopher Nolan's other films. Man does this guy know how to make a movie!\r\n\r\nYou don't have to be a Batman fan to enjoy these movies and I hope any of you who feel this way re-consider. These 3 movies are without a doubt in my mind, the finest display of comic mythology ever told on the big screen. They are damn near perfect.",
 *  "url": "http://j.mp/QSjAK2"
 }
 */
public class Review {
	@SerializedName("id") public String remoteId;
	public String author;
	public String content;
	public String url;

	public Review() {}

	public Review(Cursor data) {
		remoteId = data.getString(data.getColumnIndex(MoviesContract.Reviews.REMOTE_ID));
		author = data.getString(data.getColumnIndex(MoviesContract.Reviews.AUTHOR));
		content = data.getString(data.getColumnIndex(MoviesContract.Reviews.CONTENT));
		url = data.getString(data.getColumnIndex(MoviesContract.Reviews.URL));
	}
}
