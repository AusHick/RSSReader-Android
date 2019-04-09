package com.austinhickey.lis4331.rssreader;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RSSSourceAdapter extends RecyclerView.Adapter<RSSSourceAdapter.ViewHolder> {
	private List<RSSSource> rsList;
	private Activity activity;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textName;
		public TextView textURL;

		public ViewHolder(View v) {
			super(v);
			textName = v.findViewById(R.id.textSourceName);
			textURL = v.findViewById(R.id.textSourceURL);
		}
	}

	public RSSSourceAdapter(Activity a, List<RSSSource> rsl) {
		this.rsList = rsl;
		this.activity = a;
	}

	public void alohaSnackbar(final int position) {
		final RSSSource deletedRSSSource = rsList.get(position);
		rsList.remove(position);
		notifyItemRemoved(position);

		View v = this.activity.findViewById(R.id.sourcesLayout);
		Snackbar undo = Snackbar.make(v, "Source deleted", Snackbar.LENGTH_LONG);
		undo.setAction("Undo", v1 -> {
			rsList.add(position, deletedRSSSource);
			notifyItemInserted(position);
		});
		undo.show();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rss_source_item, parent, false);

		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int pos) {
		final RSSSource rs = this.rsList.get(pos);

		holder.textName.setText(rs.getName());
		holder.textURL.setText(rs.getUrl());
	}

	@Override
	public int getItemCount() { return this.rsList.size(); }
}
