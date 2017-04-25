package com.bhasha;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class AndroidBuildingMusicPlayerActivity extends Activity implements OnCompletionListener, SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
	boolean doubleBackToExitPressedOnce = false;
	private ImageButton btnPlay;
	private ImageButton btnForward;
	private ImageButton btnBackward;
	private ImageButton btnNext;
	private ImageButton btnPrevious;
	private ImageButton btnPlaylist;
	private ImageButton btnRepeat;
	private ImageButton btnShuffle;
	private SeekBar songProgressBar;
	private TextView songTitleLabel;
	private TextView songCurrentDurationLabel;
	private TextView songTotalDurationLabel;
	// Media Player
	private  MediaPlayer mp;
	// Handler to update UI timer, progress bar etc,.
	private Handler mHandler = new Handler();;
	private SongsManagerGujarati songManagerGujarati;
	private  SongsManagerEnglish songManagerEnglish;
	private Utilities utils;
	private int seekForwardTime = 5000; // 5000 milliseconds
	private int seekBackwardTime = 5000; // 5000 milliseconds
	private int currentSongIndex = 0; 
	private boolean isShuffle = false;
	private boolean isRepeat = false;
	private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
	Button bt1, bt2, bt3, bt4, bt5, bt6, bt7, bt8, bt9, bt0, btDel, btClr, playbtn;
	TextView playNumber;
	LinearLayout linearPlayLayout;
	ImageView iv;

	final static int CLEAR = 1;
	final static int DONT_CLEAR = 0;
	int clearDisplay = 0;

	String language;
	boolean playClick = false;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.player);
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

	//	ActionBar actionBar = getSupportActionBar();
	//	actionBar.setLogo(R.drawable.bhasha_logo);
	//	actionBar.setDisplayUseLogoEnabled(true);
	//	actionBar.setDisplayShowHomeEnabled(true);

		language = getIntent().getExtras().getString("language");
		
		// All player buttons
		iv = (ImageView)findViewById(R.id.imageView);
		linearPlayLayout = (LinearLayout)findViewById(R.id.linearEditText);
		playNumber = (TextView)findViewById(R.id.etNumberToPlay);
		btnPlay = (ImageButton) findViewById(R.id.btnPlay);
		btnForward = (ImageButton) findViewById(R.id.btnForward);
		btnBackward = (ImageButton) findViewById(R.id.btnBackward);
		btnNext = (ImageButton) findViewById(R.id.btnNext);
		btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
		btnPlaylist = (ImageButton) findViewById(R.id.btnPlaylist);
		btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
		btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);
		songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
		songTitleLabel = (TextView) findViewById(R.id.songTitle);
		songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
		songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

		playbtn = (Button)findViewById(R.id.btPlay);
		playbtn.setOnClickListener(this);
		bt1 = (Button)findViewById(R.id.bt1);
		bt1.setOnClickListener(this);
		bt2 = (Button)findViewById(R.id.bt2);
		bt2.setOnClickListener(this);
		bt3 = (Button)findViewById(R.id.bt3);
		bt3.setOnClickListener(this);
		bt4 = (Button)findViewById(R.id.bt4);
		bt4.setOnClickListener(this);
		bt5 = (Button)findViewById(R.id.bt5);
		bt5.setOnClickListener(this);
		bt6 = (Button)findViewById(R.id.bt6);
		bt6.setOnClickListener(this);
		bt7 = (Button)findViewById(R.id.bt7);
		bt7.setOnClickListener(this);
		bt8 = (Button)findViewById(R.id.bt8);
		bt8.setOnClickListener(this);
		bt9 = (Button)findViewById(R.id.bt9);
		bt9.setOnClickListener(this);
		bt0 = (Button)findViewById(R.id.bt0);
		bt0.setOnClickListener(this);
		btClr = (Button)findViewById(R.id.btClear);
		btClr.setOnClickListener(this);
		btDel = (Button)findViewById(R.id.btDel);
		btDel.setOnClickListener(this);


		playNumber.setKeyListener(DigitsKeyListener.getInstance(true, true));


		
		// Mediaplayer
		mp = new MediaPlayer();
		songManagerGujarati = new SongsManagerGujarati();
		songManagerEnglish = new SongsManagerEnglish();
		utils = new Utilities();
		
		// Listeners
		songProgressBar.setOnSeekBarChangeListener(this); // Important
		mp.setOnCompletionListener(this); // Important
		
		// Getting all songs list
		if(language.equals("gujarati"))
		{
			songsList = songManagerGujarati.getPlayList();
		}
		else
		{
			songsList = songManagerEnglish.getPlayList();
		}
		
		// By default play first song
		//playSong(0);
				
		/**
		 * Play button click event
		 * plays a song and changes button to pause image
		 * pauses a song and changes button to play image
		 * */
		btnPlay.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// check for already playing
				if(playClick)
				{
					if(mp.isPlaying())
					{
						if(mp!=null)
						{
							mp.pause();
							// Changing button image to play button
							btnPlay.setImageResource(R.drawable.btn_play);
						}
					}
					else{
						// Resume song
						if(mp!=null)
						{
							mp.start();
							// Changing button image to pause button
							btnPlay.setImageResource(R.drawable.btn_pause);
						}
					}
				}
				
			}
		});
		
		/**
		 * Forward button click event
		 * Forwards song specified seconds
		 * */
		btnForward.setOnClickListener(new View.OnClickListener()
		{
			
			@Override
			public void onClick(View arg0)
			{
				// get current song position
				if(playClick)
				{
					int currentPosition = mp.getCurrentPosition();
					// check if seekForward time is lesser than song duration
					if(currentPosition + seekForwardTime <= mp.getDuration())
					{
						// forward song
						mp.seekTo(currentPosition + seekForwardTime);
					}
					else
					{
						// forward to end position
						mp.seekTo(mp.getDuration());
					}
				}
			}
		});
		
		/**
		 * Backward button click event
		 * Backward song to specified seconds
		 * */
		btnBackward.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// get current song position
				if(playClick)
				{
					int currentPosition = mp.getCurrentPosition();
					// check if seekBackward time is greater than 0 sec
					if(currentPosition - seekBackwardTime >= 0){
						// forward song
						mp.seekTo(currentPosition - seekBackwardTime);
					}else{
						// backward to starting position
						mp.seekTo(0);
					}
				}

				
			}
		});
		
		/**
		 * Next button click event
		 * Plays next song by taking currentSongIndex + 1
		 * */
		btnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// check if next song is there or not
				if(playClick)
				{
					if(currentSongIndex < (songsList.size() - 1)){
						playSong(currentSongIndex + 1);
						currentSongIndex = currentSongIndex + 1;
					}else{
						// play first song
						playSong(0);
						currentSongIndex = 0;
					}
				}

				
			}
		});
		
		/**
		 * Back button click event
		 * Plays previous song by currentSongIndex - 1
		 * */
		btnPrevious.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(playClick)
				{
					if(currentSongIndex > 0){
						playSong(currentSongIndex - 1);
						currentSongIndex = currentSongIndex - 1;
					}else{
						// play last song
						playSong(songsList.size() - 1);
						currentSongIndex = songsList.size() - 1;
					}
				}

				
			}
		});
		
		/**
		 * Button Click event for Repeat button
		 * Enables repeat flag to true
		 * */
		btnRepeat.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isRepeat){
					isRepeat = false;
					Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}else{
					// make repeat to true
					isRepeat = true;
					Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isShuffle = false;
					btnRepeat.setImageResource(R.drawable.btn_repeat_focused);
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}	
			}
		});
		
		/**
		 * Button Click event for Shuffle button
		 * Enables shuffle flag to true
		 * */
		btnShuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if(isShuffle){
					isShuffle = false;
					Toast.makeText(getApplicationContext(), "Shuffle is OFF", Toast.LENGTH_SHORT).show();
					btnShuffle.setImageResource(R.drawable.btn_shuffle);
				}else{
					// make repeat to true
					isShuffle= true;
					Toast.makeText(getApplicationContext(), "Shuffle is ON", Toast.LENGTH_SHORT).show();
					// make shuffle to false
					isRepeat = false;
					btnShuffle.setImageResource(R.drawable.btn_shuffle_focused);
					btnRepeat.setImageResource(R.drawable.btn_repeat);
				}	
			}
		});
		
		/**
		 * Button Click event for Play list click event
		 * Launches list activity which displays list of songs
		 * */
		btnPlaylist.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(), PlayListActivity.class);
				i.putExtra("language", language);
				startActivityForResult(i, 100);			
			}
		});
		
	}
	
	/**
	 * Receiving song index from playlist view
	 * and play the song
	 * */
	@Override
    protected void onActivityResult(int requestCode,
                                     int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 100){
         	 currentSongIndex = data.getExtras().getInt("songIndex");
         	 // play selected song
             playSong(currentSongIndex);
        }
 
    }
	
	/**
	 * Function to play a song
	 * @param songIndex - index of song
	 * */
	public void  playSong(int songIndex)
	{
		// Play song
		if(songsList.size() != 0)
		{
			Log.e("songsList","" + songsList.toString());
			try
			{
				mp.reset();
				mp.setDataSource(songsList.get(songIndex).get("songPath"));
				mp.prepare();
				mp.start();
				// Displaying Song title
				String songTitle = songsList.get(songIndex).get("songTitle");
				int temp = songIndex + 1;
				songTitleLabel.setText("(" + temp + ") " + songTitle);

				// Changing Button Image to pause image
				btnPlay.setImageResource(R.drawable.btn_pause);

				// set Progress bar values
				songProgressBar.setProgress(0);
				songProgressBar.setMax(100);

				// Updating progress bar
				updateProgressBar();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else
		{
			songTitleLabel.setText("Directory '/sdcard/bhasha-"+ language + "' is Empty!!");
			Toast.makeText(getApplicationContext(), "Directory '/sdcard/bhasha-"+language+"' is Empty!!", Toast.LENGTH_LONG).show();
			return;
		}
	}
	
	/**
	 * Update timer on seekbar
	 * */

	public void updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 100);
    }

	@Override
	protected void onResume() {
		super.onResume();
		if(playClick)
		{
			updateProgressBar();
		}
	}

	/**
	 * Background Runnable thread
	 * */

	Runnable mUpdateTimeTask = new Runnable()
	{
		   public void run()
		   {
			   long totalDuration = mp.getDuration();
			   long currentDuration = mp.getCurrentPosition();
			  
			   // Displaying Total Duration time
			   songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));
			   // Displaying time completed playing
			   songCurrentDurationLabel.setText(""+utils.milliSecondsToTimer(currentDuration));
			   
			   // Updating progress bar
			   int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
			   //Log.d("Progress", ""+progress);
			   songProgressBar.setProgress(progress);
			   
			   // Running this thread after 100 milliseconds
		       mHandler.postDelayed(this, 100);
		   }
		};
	/**
	 * 
	 * */
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromTouch) {
		
	}

	/**
	 * When user starts moving the progress handler
	 * */
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// remove message Handler from updating progress bar
		mHandler.removeCallbacks(mUpdateTimeTask);
    }
	
	/**
	 * When user stops moving the progress hanlder
	 * */
	@Override
    public void onStopTrackingTouch(SeekBar seekBar) {
		mHandler.removeCallbacks(mUpdateTimeTask);
		int totalDuration = mp.getDuration();
		int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);
		
		// forward or backward to certain seconds
		mp.seekTo(currentPosition);
		
		// update timer progress again
		updateProgressBar();
    }

	/**
	 * On Song Playing completed
	 * if repeat is ON play same song again
	 * if shuffle is ON play random song
	 * */

	@Override
	public void onCompletion(MediaPlayer arg0) {
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if(isShuffle){
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		}
	}
	/*
	@Override
	public void onCompletion(MediaPlayer arg0) {
		
		// check for repeat is ON or OFF
		if(isRepeat){
			// repeat is on play same song again
			playSong(currentSongIndex);
		} else if(isShuffle){
			// shuffle is on - play a random song
			Random rand = new Random();
			currentSongIndex = rand.nextInt((songsList.size() - 1) - 0 + 1) + 0;
			playSong(currentSongIndex);
		}
		else
		{
			// no repeat or shuffle ON - play next song
			if(currentSongIndex < (songsList.size() - 1)){
				playSong(currentSongIndex + 1);
				currentSongIndex = currentSongIndex + 1;
			}else{
				// play first song
				playSong(0);
				currentSongIndex = 0;
			}
		}
	}
	*/
	
	@Override
	 public void onDestroy(){
	 super.onDestroy();
	    mp.release();
	 }

	@Override
	protected void onPause() {
		super.onPause();
		mHandler.removeCallbacks(mUpdateTimeTask);
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (doubleBackToExitPressedOnce)
		{
			super.onBackPressed();
			return;
		}

		this.doubleBackToExitPressedOnce = true;
		Toast.makeText(this, "Please click BACK again to stop.", Toast.LENGTH_SHORT).show();

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				doubleBackToExitPressedOnce = false;
			}
		}, 3000);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.bt1:
				customPlay(true, 1);

				break;
			case R.id.bt2:
				customPlay(true, 2);

				break;
			case R.id.bt3:
				customPlay(true, 3);

				break;
			case R.id.bt4:
				customPlay(true, 4);

				break;
			case R.id.bt5:
				customPlay(true, 5);

			break;
			case R.id.bt6:
				customPlay(true, 6);

				break;
			case R.id.bt7:
				customPlay(true, 7);

				break;
			case R.id.bt8:
				customPlay(true, 8);

				break;
			case R.id.bt9:
				customPlay(true, 9);

				break;

			case R.id.bt0:
				customPlay(true, 0);

				break;

			case R.id.btPlay:

				if(playNumber.getText().toString().equals(""))
				{
					Toast.makeText(AndroidBuildingMusicPlayerActivity.this, "Field is Empty.", Toast.LENGTH_SHORT).show();
				}
				else
				{
					playClick = true;
					iv.setVisibility(View.VISIBLE);
					linearPlayLayout.setVisibility(View.GONE);

					Log.e("songsList","" + songsList.toString());
					//[{songPath=/sdcard/bhasha-gujarati/04 - Ek Garam Chai Ki Pyali-(MyMp3Singer.com).mp3, songTitle=04 - Ek Garam Chai Ki Pyali-(MyMp3Singer.com)}, {songPath=/sdcard/bhasha-gujarati/Afghan Jalebi  Ya Baba -(Mr-Jatt.com).mp3, songTitle=Afghan Jalebi  Ya Baba -(Mr-Jatt.com)}, {songPath=/sdcard/bhasha-gujarati/jaanetuyajaanena05(www.songs.pk).mp3, songTitle=jaanetuyajaanena05(www.songs.pk)}, {songPath=/sdcard/bhasha-gujarati/1.mp3, songTitle=1}]
					if(songsList.size() > 0)
					{
						String temp = playNumber.getText().toString();
						if(language.equals("gujarati"))
						{
							String str = "/sdcard/bhasha-gujarati/" + temp + ".mp3";
							Log.e("str","" + str);
							int value = search_key(str);
							Log.e("value","" + value);
							if(value == -1)
							{
								Toast.makeText(AndroidBuildingMusicPlayerActivity.this, "Selected song is not present.", Toast.LENGTH_SHORT).show();
							}
							else
							{
								playSong(value);
							}
						}
						else
						{
							String str = "/sdcard/bhasha-english/" + temp + ".mp3";
							Log.e("str","" + str);
							int value = search_key(str);
							Log.e("value","" + value);
							if(value == -1)
							{
								Toast.makeText(AndroidBuildingMusicPlayerActivity.this, "Selected song is not present.", Toast.LENGTH_SHORT).show();
							}
							else
							{
								playSong(value);
							}
						}

					}
					else
					{
						Toast.makeText(AndroidBuildingMusicPlayerActivity.this, "Song List is Empty.", Toast.LENGTH_SHORT).show();
					}
				}
				break;

			case R.id.btClear:
				playNumber.setText("");
				break;

			case R.id.btDel:

				String str=playNumber.getText().toString();
				if (str.length() > 1 ) {
					str = str.substring(0, str.length() - 1);
					playNumber.setText(str);
				}
				else if (str.length() <=1 ) {
					playNumber.setText("");
				}
				break;

			default:
				break;
		}
	}

	public void customPlay(boolean visible, int index)
	{
		if(visible)
		{
			iv.setVisibility(View.GONE);
			linearPlayLayout.setVisibility(View.VISIBLE);

			if (clearDisplay == CLEAR) {
				playNumber.setText("");
			}
			clearDisplay = DONT_CLEAR;
			playNumber.append(String.valueOf(index));
		}
	}
	public int search_key(String search_key) {
		for (int temp = 0; temp < songsList.size(); temp++) {
			String id = songsList.get(temp).get("songPath");
			if (id != null && id.equals(search_key)) {
				return temp;
			}
		}
		return -1;
	}
}