package com.solomonsites.windownloader.activity;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import com.solomonsites.windownloader.api.YoutubeDownloaderApiImpl;

public class DownloadActivity extends Activity{
	private boolean isExternalStorageAvailable = false,
			isExternalStorageWritable = false;
	private static final String state = Environment.getExternalStorageState();
	private File storage_path = null;
	private String mFile = null,url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_layout);
		Bundle mBundle = getIntent().getBundleExtra("url");
		url = mBundle.getString("url");
		if(isStorageAvailable()){
			Toast.makeText(getApplicationContext(), url, Toast.LENGTH_SHORT);
			DownloadTask task = new DownloadTask();
			task.execute();
		}else{
			Toast.makeText(getApplicationContext(), "External storage not available", Toast.LENGTH_LONG).show();
		}
	}

	private boolean isStorageAvailable() {
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			isExternalStorageAvailable = isExternalStorageWritable = true;
			storage_path = Environment
					.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC);
			String file_name = "first.flv";
			File directory = new File(storage_path, file_name);
			mFile = directory.toString();
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
			isExternalStorageAvailable = true;
			isExternalStorageWritable = false;
		} else {
			isExternalStorageAvailable = isExternalStorageWritable = false;
		}

		return isExternalStorageWritable;
	}
	
	private class DownloadTask extends AsyncTask<Void, Void, String>{

		@Override
		protected String doInBackground(Void... params) {
			YoutubeDownloaderApiImpl download = new YoutubeDownloaderApiImpl();
			try {
				return download.downloadVideo(url, mFile);
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block   
				e.printStackTrace();
			}
			return null;
		}
		
	}
}