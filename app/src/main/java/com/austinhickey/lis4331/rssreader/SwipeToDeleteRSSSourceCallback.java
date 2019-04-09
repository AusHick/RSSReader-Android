package com.austinhickey.lis4331.rssreader;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteRSSSourceCallback extends ItemTouchHelper.SimpleCallback {
	private RSSSourceAdapter rssAdapter;

	public SwipeToDeleteRSSSourceCallback(RSSSourceAdapter rsa) {
		super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
		this.rssAdapter = rsa;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		int position = viewHolder.getAdapterPosition();
		this.rssAdapter.alohaSnackbar(position);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		return true;
	}
}
