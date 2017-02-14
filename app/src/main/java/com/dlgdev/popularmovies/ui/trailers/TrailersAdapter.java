package com.dlgdev.popularmovies.ui.trailers;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.dlgdev.popularmovies.R;
import com.dlgdev.popularmovies.data.trailers.Trailer;
import com.dlgdev.popularmovies.ui.trailers.TrailerViewHolder.OnTrailerClickListener;

import java.util.ArrayList;
import java.util.List;


public class TrailersAdapter extends RecyclerView.Adapter<TrailerViewHolder> {
	List<Trailer> trailers = new ArrayList<>();
	private OnTrailerClickListener listener;

	public TrailersAdapter(List<Trailer> trailers, OnTrailerClickListener listener) {
		this.trailers = trailers;
		this.listener = listener;
	}

	@Override public TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		TrailerViewHolder vh = new TrailerViewHolder(
				LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false));
		return vh;
	}

	@Override public void onBindViewHolder(TrailerViewHolder holder, int position) {
		holder.trailer(trailers.get(position), listener);
	}

	@Override public int getItemCount() {
		return trailers.size();
	}
}
