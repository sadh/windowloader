package com.solomonsites.windownloader.activity;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import android.app.ListActivity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gdata.data.media.mediarss.MediaThumbnail;
import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.data.youtube.YouTubeMediaGroup;
import com.google.gdata.util.ServiceException;
import com.solomonsites.windownloader.api.YoutubeApiImpl;

public class WinDowloaderActivity extends ListActivity {
	private CharSequence searchTerms;
	private EditText searchInput;
	private ListView feedListView;
	private Button searchButton;
	private FeedListAdapter mAdapter;
	private List<VideoEntry> mVideoFeed;
	private ArrayList<Bitmap> mThumbList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_layout);
		searchInput = (EditText) findViewById(R.id.searchTerm);
		searchInput.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					searchTerms = searchInput.getText();
				}
				return false;
			}
		});
		searchButton = (Button) findViewById(R.id.search);
		searchButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SearchTask task = new SearchTask();
				task.execute();
			}
		});
		
		searchButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				SearchTask task = new SearchTask();
				task.execute();
				return true;
			}
		});
		CompoundButton.OnCheckedChangeListener button_listener = new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					Toast.makeText(getBaseContext(), "Selected", 4000).show();
				}

			}
		};
		RadioGroup navbar = (RadioGroup) findViewById(R.id.navbar);
		RadioButton navbar_button = (RadioButton) findViewById(R.id.navbar_search);
		navbar_button.setOnCheckedChangeListener(button_listener);

		navbar_button = (RadioButton) findViewById(R.id.navbar_downloads);
		navbar_button.setOnCheckedChangeListener(button_listener);
		navbar_button = (RadioButton) findViewById(R.id.navbar_player);
		navbar_button.setOnCheckedChangeListener(button_listener);
		navbar_button = (RadioButton) findViewById(R.id.navbar_popular);
		navbar_button.setOnCheckedChangeListener(button_listener);
		navbar_button = (RadioButton) findViewById(R.id.navbar_settings);
		navbar_button.setOnCheckedChangeListener(button_listener);
		// feedListView = getListView();
		// ProgressBar mProgressBar = new ProgressBar(getBaseContext());
		// mProgressBar.setIndeterminate(true);
		// mProgressBar.setLayoutParams(new
		// LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// feedListView.setEmptyView(mProgressBar);

	}

	private class SearchTask extends AsyncTask<Void, Void, VideoFeed> {
		private YoutubeApiImpl api;

		public SearchTask() {
			api = new YoutubeApiImpl();
		}

		@Override
		protected void onPostExecute(VideoFeed result) {
			if (result != null) {
				mVideoFeed = result.getEntries();
				DownloadThumbTask mDownloadThumbTask = new DownloadThumbTask();
				mDownloadThumbTask.execute();
			} else {
				Toast.makeText(getBaseContext(),
						"Search return emty results..!!", 3000);
			}
			super.onPostExecute(result);
		}

		@Override
		protected VideoFeed doInBackground(Void... params) {
			try {
				return api.searchFeed(searchTerms.toString());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ServiceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}

		private class DownloadThumbTask extends AsyncTask<Void, Void, ArrayList<Bitmap>> {
			
			public DownloadThumbTask(){
				
			}

			@Override
			protected void onPostExecute(ArrayList<Bitmap> result) {
				if (result != null) {
					mThumbList = result;
					mAdapter = new FeedListAdapter(getBaseContext(),
							R.layout.feed_list, mVideoFeed, mThumbList);
					setListAdapter(mAdapter);

				}
				super.onPostExecute(result);
			}

			@Override
			protected ArrayList<Bitmap> doInBackground(Void... params) {
				ArrayList<Bitmap> thumneils = new ArrayList<Bitmap>();
				try {
					for (VideoEntry entry : mVideoFeed) {
						YouTubeMediaGroup mMediaGroup = entry.getMediaGroup();
						List<MediaThumbnail> mThumbnail_list = mMediaGroup
								.getThumbnails();
						InputStream in = new BufferedInputStream(new URL(
								mThumbnail_list.get(0).getUrl()).openStream());
						Bitmap bitmap = BitmapFactory.decodeStream(in);
						thumneils.add(bitmap);
						in.close();
					}
					return thumneils;
				} catch (IOException e) {
					Log.e("Image download error", "Could not load Bitmap");
				}
				return null;
			}

		}

	}
}