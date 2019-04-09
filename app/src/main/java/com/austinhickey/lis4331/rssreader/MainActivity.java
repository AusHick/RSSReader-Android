package com.austinhickey.lis4331.rssreader;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
	private File saveFile;
	public static List<RSSSource> rssSourcesList = new ArrayList<>();
	private RequestQueue mRequestQueue;
	private final List<Article> cardContainerList = new ArrayList<>();
	private CardAdapter cardAdapter;

	private List<Article> ParseRSSArticles(String xml) {
		List<Article> articles = new ArrayList<>();

		try {
			XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
			factory.setNamespaceAware(true);
			XmlPullParser xpp = factory.newPullParser();

			xpp.setInput(new StringReader(xml));
			int eventType = xpp.getEventType();

			String name = null;
			Article newArticle = null;
			String sourceName = "Unknown Source";
			String sourceImage = "";

			String parentTag = null;

			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType) {
					case XmlPullParser.START_DOCUMENT:
						//Log.d("XML", "Start Document");
						break;
					case XmlPullParser.START_TAG:
						name = xpp.getName();
						//Log.d("XML", "Start Tag: " + name);
						if(parentTag == null && name.equalsIgnoreCase("image"))
						{
							parentTag = name;
						} else if(parentTag != null && parentTag.equalsIgnoreCase("image")) {
							if(name.equalsIgnoreCase("title"))
								sourceName = xpp.nextText();
							else if(name.equalsIgnoreCase("url"))
								sourceImage = xpp.nextText();
						}

						if(parentTag == null && name.equalsIgnoreCase("item"))
						{
							parentTag = name;
							newArticle = new Article();
						} else if(parentTag != null && newArticle != null && parentTag.equalsIgnoreCase("item")) {
							if(name.equalsIgnoreCase("title"))
								newArticle.setTitle(xpp.nextText());
							else if(name.equalsIgnoreCase("description"))
								newArticle.setDescription(xpp.nextText());
							else if(name.equalsIgnoreCase("link"))
								newArticle.setHref(xpp.nextText());
							else if(name.equalsIgnoreCase("pubdate"))
								newArticle.setPublishDate(xpp.nextText());
						}
						break;
					case XmlPullParser.END_TAG:
						name = xpp.getName();
						//Log.d("XML", "End Tag: " + xpp.getName());
						if(parentTag == name && name.equalsIgnoreCase("image"))
						{
							parentTag = null;
						}
						if(parentTag == name && name.equalsIgnoreCase("item") && newArticle != null) {
							newArticle.setPreview(sourceImage);
							newArticle.setSource(sourceName);
							articles.add(newArticle);
							parentTag = null;
						}
						break;
					case XmlPullParser.TEXT:
						//Log.d("XML", "Text: " + xpp.getText());
						break;
				}

				eventType = xpp.next();
			}
		} catch (XmlPullParserException | IOException e) {
			e.printStackTrace();
		}

		return articles;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		this.saveFile = new File(getFilesDir(), "feeds.json");
		this.mRequestQueue = Volley.newRequestQueue(this);

		rssSourcesList = RSSSource.Load(this.saveFile);

		RecyclerView cardList = findViewById(R.id.cardList);
		cardAdapter = new CardAdapter(this, cardContainerList);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		cardList.setLayoutManager(llm);
		cardList.setItemAnimator(new DefaultItemAnimator());
		cardList.setAdapter(cardAdapter);
		cardList.setHasFixedSize(true);
		cardList.addItemDecoration(new ArticleListDecoration(32,1));
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteArticleCallback(cardAdapter));
		itemTouchHelper.attachToRecyclerView(cardList);
	}

	@Override
	protected void onStart() {
		super.onStart();

		cardContainerList.clear();
		cardAdapter.notifyDataSetChanged();

		if(rssSourcesList.size() <= 0) {
			findViewById(R.id.cardList).setVisibility(View.GONE);
			findViewById(R.id.textStatus).setVisibility(View.VISIBLE);
		}

		for (RSSSource feed : rssSourcesList) {
			findViewById(R.id.textStatus).setVisibility(View.GONE);
			findViewById(R.id.cardList).setVisibility(View.VISIBLE);
			StringRequest req = new StringRequest(Request.Method.GET, feed.getUrl(), response -> {
				cardContainerList.addAll(ParseRSSArticles(response));
				Collections.sort(cardContainerList, (o1, o2) -> o2.getPublishDate().compareTo(o1.getPublishDate()));
				cardAdapter.notifyDataSetChanged();
			}, error -> error.printStackTrace());
			mRequestQueue.add(req);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		RSSSource.Save(rssSourcesList, this.saveFile);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.sources_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menuRSSSources:
				startActivity(new Intent(this, RSSSourcesActivity.class));
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
