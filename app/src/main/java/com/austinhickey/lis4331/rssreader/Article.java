package com.austinhickey.lis4331.rssreader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {
	private String title;
	private Date pubDate;
	private String description;
	private String preview;
	private String href;
	private String source;

	public Article() {
		this.title = "Unnamed Article";
		this.pubDate = null;
		this.description = "No article description";
		this.preview = "";
		this.href = "https://www.google.com";
		this.source = "Unknown Source";
	}

	/*public Article(String title, String pubDate, String description, String imageURL, String articleURL, String source) {
		this.title = title;
		this.setPublishDate(pubDate);
		this.description = description;
		this.preview = imageURL;
		this.href = articleURL;
		this.source = source;
	}*/

	public Bitmap getBitmap() {
		return BitmapFactory.decodeFile(this.getPreview());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getPublishDate() {
		return pubDate;
	}

	public String getPublishDateFormatted() {
		DateFormat df = new SimpleDateFormat("MMMM d, yyyy @ h:mm a");
		return df.format(pubDate);
	}

	public void setPublishDate(String pubDate) {
		DateFormat df = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z");
		try {
			this.pubDate = df.parse(pubDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.replaceAll("<.+>","").trim();
	}

	//TODO: Proper implement bitmap fetching
	public String getPreview() {
		return preview;
	}

	//TODO: return bitmap stuff
	public void setPreview(String preview) {
		this.preview = preview;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String toString() {
		return this.title + " by " + this.pubDate;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return source;
	}
}
