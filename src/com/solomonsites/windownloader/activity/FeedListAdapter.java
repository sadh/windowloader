package com.solomonsites.windownloader.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gdata.data.youtube.VideoEntry;
import com.google.gdata.data.youtube.YouTubeMediaContent;
import com.google.gdata.data.youtube.YouTubeMediaGroup;

public class FeedListAdapter extends ArrayAdapter<VideoEntry> {

	private Context mContext;
	private List<VideoEntry> mEntry;
	private ArrayList<Bitmap> mThumbList;
	private int resource;
	private VideoEntryHolder holder = null;

	public FeedListAdapter(Context context, int resource,
			List<VideoEntry> mVideoFeed,ArrayList<Bitmap> mThambList) {
		super(context, resource, mVideoFeed);
		mContext = context;
		mEntry = mVideoFeed;
		this.resource = resource;
		this.mThumbList = mThambList;
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View entryView = convertView;
		if (entryView == null) {
			LayoutInflater inflater = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			entryView = inflater.inflate(resource, parent, false);
			holder = new VideoEntryHolder();
			TextView title = (TextView) entryView.findViewById(R.id.entry_name);
			ImageView thumb = (ImageView) entryView.findViewById(R.id.thumbView);
			ImageButton play_button = (ImageButton) entryView.findViewById(R.id.play_button);
			ImageButton download_button = (ImageButton) entryView.findViewById(R.id.download_button);
			holder.setTitle(title);
			holder.setThumb(thumb);
			holder.setPlayButton(play_button);
			holder.setDownloadButton(download_button);
			entryView.setTag(holder);
		} else {
			holder = (VideoEntryHolder) entryView.getTag();
		}
		final VideoEntry entry = mEntry.get(position);
		holder.getThumb().setImageBitmap(mThumbList.get(position));
		holder.getTitle().setText(entry.getTitle().getPlainText());
		holder.getPlayButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startPlayerActivity(entry);
				
			}
		});
		
		holder.getDownloadButton().setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startDownloadActivity(entry);
				
			}
		});
		return entryView;
	}

	private class VideoEntryHolder {
		private TextView title;
		private ImageView thumb;
		private ImageButton playButton,downloadButton;

		public VideoEntryHolder() {

		}

		public TextView getTitle() {
			return title;
		}

		public void setTitle(TextView title) {
			this.title = title;
		}

		public ImageView getThumb() {
			return thumb;
		}

		public void setThumb(ImageView thumb) {
			this.thumb = thumb;
		}

		public ImageButton getPlayButton() {
			return playButton;
		}

		public void setPlayButton(ImageButton playButton) {
			this.playButton = playButton;
		}

		public ImageButton getDownloadButton() {
			return downloadButton;
		}

		public void setDownloadButton(ImageButton downloadButton) {
			this.downloadButton = downloadButton;
		}

	}
	
	private void startPlayerActivity(VideoEntry entry){
		Bundle mBundle = new Bundle();
		YouTubeMediaGroup mediaGroup = entry.getMediaGroup();
		List<YouTubeMediaContent> mContents = mediaGroup.getYouTubeContents();
		String url = mContents.get(1).getUrl();
		Intent i = new Intent(mContext,PlayerActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mBundle.putString("url", url);
		i.putExtra("url", mBundle);
		mContext.startActivity(i);
	}
	
	private void startDownloadActivity(VideoEntry entry){
		Bundle mBundle = new Bundle();
		YouTubeMediaGroup mediaGroup = entry.getMediaGroup();
		List<YouTubeMediaContent> mContents = mediaGroup.getYouTubeContents();
		String url = mContents.get(0).getUrl();
		Intent i = new Intent(mContext, DownloadActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mBundle.putString("url", url);
		i.putExtra("url", mBundle);
		mContext.startActivity(i);
	}
	
	

}
