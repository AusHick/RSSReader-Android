package com.austinhickey.lis4331.rssreader;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

public class ArticleCard extends View {
	public ArticleCard(Context context) {
		super(context);
		init(null, 0);
	}

	public ArticleCard(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(attrs, 0);
	}

	public ArticleCard(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(attrs, defStyle);
	}

	private void init(AttributeSet attrs, int defStyle) {
		final TypedArray a = getContext().obtainStyledAttributes(
				attrs, R.styleable.ArticleCard, defStyle, 0);

		a.recycle();
	}
}
