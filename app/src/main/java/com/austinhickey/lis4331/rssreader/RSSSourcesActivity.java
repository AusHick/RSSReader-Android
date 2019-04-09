package com.austinhickey.lis4331.rssreader;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RSSSourcesActivity extends AppCompatActivity {
	private File saveFile;
	//private List<RSSSource> rssContainerList = new ArrayList<>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rss_sources);
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
						MainActivity.rssSourcesList.add(new RSSSource(((TextInputEditText)inputForm.findViewById(R.id.textFeedURLEdit)).getText().toString()));
						rssAdapter.notifyDataSetChanged();
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
