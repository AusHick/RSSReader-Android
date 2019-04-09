package com.austinhickey.lis4331.rssreader;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeToDeleteArticleCallback extends ItemTouchHelper.SimpleCallback {
	private CardAdapter cardAdapter;

	public SwipeToDeleteArticleCallback(CardAdapter ca) {
		super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
		this.cardAdapter = ca;
	}

	@Override
	public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
		int position = viewHolder.getAdapterPosition();
		this.cardAdapter.alohaSnackbar(position);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
		return true;
	}
}
