package com.austinhickey.lis4331.rssreader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RSSSource {
	private String name = "Unnamed Source";
	private String url = "";

	public static List<RSSSource> Load(final File saveFile) {
		List<RSSSource> feedList = new ArrayList<>();

		try {
			FileInputStream fis;
			StringBuffer data = new StringBuffer();
			fis = new FileInputStream(saveFile);

			int b;
			while((b = fis.read()) != -1) {
				data.append((char) b);
			}

			JSONArray j = new JSONArray(data.toString());
			for (int i = 0; i < j.length(); i++) {
				JSONObject o = j.getJSONObject(i);
				feedList.add(new RSSSource(o.getString("name"), o.getString("url")));
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

		return feedList;
	}

	public static boolean Save(final List<RSSSource> feedList, final File saveFile) {
		try {
			FileOutputStream fos;
			JSONArray j = new JSONArray();

			for (RSSSource r : feedList) {
				JSONObject o = new JSONObject();

				o.put("name", r.name);
				o.put("url", r.url);
				j.put(o);
			}

			fos = new FileOutputStream(saveFile);
			fos.write(j.toString().getBytes());
			fos.flush();

			return true;
		} catch (IOException | JSONException e) {
			e.printStackTrace();
		}

		return false;
	}

	public RSSSource(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public RSSSource(String url) {
		this("Unnamed Source", url);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
