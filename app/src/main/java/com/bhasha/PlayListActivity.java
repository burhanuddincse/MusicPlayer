package com.bhasha;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayListActivity extends ListActivity
{
	String language;
	// Songs list
	public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playlist);

		language = getIntent().getExtras().getString("language");
		ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

		// get all songs from sdcard
		if(language.equals("gujarati"))
		{
			SongsManagerGujarati plm = new SongsManagerGujarati();
			this.songsList = plm.getPlayList();
		}
		else
		{
			SongsManagerEnglish plm = new SongsManagerEnglish();
			this.songsList = plm.getPlayList();
		}


		// looping through playlist
		for (int i = 0; i < songsList.size(); i++)
		{
			// creating new HashMap
			HashMap<String, String> song = songsList.get(i);

			// adding HashList to ArrayList
			songsListData.add(song);
		}

		if(songsListData.size() < 1)
		{
			Toast.makeText(PlayListActivity.this, "Play list is empty!", Toast.LENGTH_SHORT).show();
		}

		// Adding menuItems to ListView
		ListAdapter adapter = new SimpleAdapter(this, songsListData,
				R.layout.playlist_item, new String[] { "songTitle" }, new int[] {
						R.id.songTitle });

		setListAdapter(adapter);

		// selecting single ListView item
		ListView lv = getListView();
		// listening to single listitem click
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// getting listitem index
				int songIndex = position;
				
				// Starting new intent
				Intent in = new Intent(getApplicationContext(),
						AndroidBuildingMusicPlayerActivity.class);
				// Sending songIndex to PlayerActivity
				in.putExtra("songIndex", songIndex);
				setResult(100, in);
				// Closing PlayListView
				finish();
			}
		});

	}
}
