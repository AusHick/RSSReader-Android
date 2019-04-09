package com.austinhickey.lis4331.rssreader;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
	private List<Article> articleList;
	private Activity activity;
	private RequestQueue imageRequestQueue;

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView textTitle;
		public TextView textPubDate;
		public TextView textDescription;
		public ImageView imagePreview;
		public TextView textSource;


		public ViewHolder(View v) {
			super(v);
			textTitle = v.findViewById(R.id.textTitle);
			textPubDate = v.findViewById(R.id.textPubDate);
			textDescription = v.findViewById(R.id.textDescription);
			imagePreview = v.findViewById(R.id.imagePreview);
			textSource = v.findViewById(R.id.textSource);
		}
	}

	public CardAdapter(Activity a, List<Article> al) {
		this.articleList = al;
		this.activity = a;
		this.imageRequestQueue = Volley.newRequestQueue(a.getApplicationContext());
	}

	public void alohaSnackbar(final int position) {
		final Article deletedArticle = articleList.get(position);
		articleList.remove(position);
		notifyItemRemoved(position);

		View v = this.activity.findViewById(R.id.coordinatorLayout);
		Snackbar undo = Snackbar.make(v, "Article deleted", Snackbar.LENGTH_LONG);
		undo.setAction("Undo", v1 -> {
			articleList.add(position, deletedArticle);
			notifyItemInserted(position);
		});
		undo.show();
	}

	public void followHref(String url) {
		this.activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_card, parent, false);

		return new ViewHolder(v);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int pos) {
		final Article ac = this.articleList.get(pos);

		/*ImageRequest im = new ImageRequest(ac.getPreview(), response -> {
			Log.d("Article Image", "Setting " + ac.getTitle() + " to " + ac.getPreview());
			BitmapDrawable bd = new BitmapDrawable(this.activity.getResources(), response);
			holder.imagePreview.setImageDrawable(bd.getCurrent());
		}, 0, 0, ImageView.ScaleType.CENTER, null, error -> Log.e("Article Image", error.toString()));
		imageRequestQueue.add(im);*/

		holder.textTitle.setText(ac.getTitle());
		holder.textPubDate.setText(ac.getPublishDateFormatted());
		holder.textDescription.setText(ac.getDescription());
		holder.textSource.setText("Source: " + ac.getSource());

		holder.itemView.setOnClickListener(v -> followHref(ac.getHref()));
	}

	@Override
	public int getItemCount() { return this.articleList.size(); }
}
