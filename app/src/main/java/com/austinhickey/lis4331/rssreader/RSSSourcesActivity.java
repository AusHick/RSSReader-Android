package com.austinhickey.lis4331.rssreader;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.util.Xml.newPullParser;

public class RSSSourcesActivity extends AppCompatActivity {
	private RequestQueue mRequestQueue;
	private File saveFile;
	//private List<RSSSource> rssContainerList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_sources);
		this.mRequestQueue = Volley.newRequestQueue(this);
		saveFile = new File(getFilesDir(), "feeds.json");
		//rssContainerList = RSSSource.Load(saveFile);

		//rssContainerList.add(new RSSSource("Reuters News", "http://feeds.reuters.com/Reuters/domesticNews"));
		//rssContainerList.add(new RSSSource("NYT > U.S.", "http://rss.nytimes.com/services/xml/rss/nyt/US.xml"));

		RecyclerView rssList = findViewById(R.id.sourceList);
		final RSSSourceAdapter rssAdapter = new RSSSourceAdapter(this, MainActivity.rssSourcesList);
		LinearLayoutManager llm = new LinearLayoutManager(this);
		rssList.setLayoutManager(llm);
		rssList.setItemAnimator(new DefaultItemAnimator());
		rssList.setAdapter(rssAdapter);
		rssList.setHasFixedSize(true);
		ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteRSSSourceCallback(rssAdapter));
		itemTouchHelper.attachToRecyclerView(rssList);

		rssAdapter.notifyDataSetChanged();

		FloatingActionButton fab = findViewById(R.id.fabAddSource);
		fab.setOnClickListener(v -> {
			final View inputForm = LayoutInflater.from(this).inflate(R.layout.dialog_add_feed, null);
			new MaterialAlertDialogBuilder(this)
					.setView(inputForm)
					.setTitle("Add RSS Feed")
					.setPositiveButton("Add", (dialog, which) -> {
						String feedURL = ((TextInputEditText)inputForm.findViewById(R.id.textFeedURLEdit)).getText().toString();

						StringRequest req = new StringRequest(Request.Method.GET, feedURL, response -> {
							try {
								XmlPullParserFactory xppf = XmlPullParserFactory.newInstance();
								xppf.setNamespaceAware(true);
								XmlPullParser xpp = xppf.newPullParser();

								xpp.setInput(new StringReader(response));
								int eventType = xpp.getEventType();

								String name = null;
								String parentTag = null;

								while (eventType != XmlPullParser.END_DOCUMENT) {
									if (eventType == XmlPullParser.START_TAG) {
										name = xpp.getName();

										if(parentTag == null && name.equalsIgnoreCase("image")) {
											parentTag = name;
										} else if(parentTag != null && parentTag.equalsIgnoreCase("image")) {
											if(name.equalsIgnoreCase("title")) {
												String t = xpp.nextText();
												RSSSource newSource = new RSSSource(t, feedURL);
												MainActivity.rssSourcesList.add(newSource);
												rssAdapter.notifyDataSetChanged();
												RSSSource.Save(MainActivity.rssSourcesList, saveFile);
												break;
											}
										}
									}

									eventType = xpp.next();
								}
							} catch (XmlPullParserException | IOException e) {
								Snackbar.make(v, "Not a valid RSS feed URL!", Snackbar.LENGTH_LONG).show();
								e.printStackTrace();
							}
						}, error -> {
							Snackbar.make(v, "Not a valid URL! (Include http:// or https://)", Snackbar.LENGTH_LONG).show();
							error.printStackTrace();
						});
						mRequestQueue.add(req);
					})
					.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel())
					.show();
		});
	}

	@Override
	protected void onStop() {
		super.onStop();
		RSSSource.Save(MainActivity.rssSourcesList, saveFile);
	}
}
