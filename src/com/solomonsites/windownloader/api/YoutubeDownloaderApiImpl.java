package com.solomonsites.windownloader.api;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.Vector;

import android.os.Environment;

public class YoutubeDownloaderApiImpl implements YoutubeDownloaderApiGDdataImpl {

	@Override
	public String downloadVideo(String url, String path)
			throws MalformedURLException, IOException {
		int begin, end;
		String tmpstr = null;
		String decoded_url = "";
		InputStream is;
		URL link = new URL(url);
		HttpURLConnection con = (HttpURLConnection) link.openConnection();
		con.setRequestMethod("GET");
		InputStream stream = con.getInputStream();
		InputStreamReader reader = new InputStreamReader(stream);
		StringBuffer buffer = new StringBuffer();
		char[] buf = new char[262144];
		int chars_read;
		while ((chars_read = reader.read(buf, 0, 262144)) != -1) {
			buffer.append(buf, 0, chars_read);
		}
		tmpstr = buffer.toString();

		begin = tmpstr.indexOf("url_encoded_fmt_stream_map=");
		end = tmpstr.indexOf("&", begin + 27);
		if (end == -1) {
			end = tmpstr.indexOf("\"", begin + 27);
		}
		tmpstr = URLDecoder.decode(tmpstr.substring(begin + 27, end), "UTF-8");
		Vector<String> url_encoded_fmt_stream_map = new Vector<String>();
		begin = 0;
		end = tmpstr.indexOf(",");
		while (end != -1) {
			url_encoded_fmt_stream_map.addElement(tmpstr.substring(begin, end));
			begin = end + 1;
			end = tmpstr.indexOf(",", begin);
		}

		url_encoded_fmt_stream_map.addElement(tmpstr.substring(begin,
				tmpstr.length()));
		Enumeration<String> url_encoded_fmt_stream_map_enum = url_encoded_fmt_stream_map
				.elements();
		while (url_encoded_fmt_stream_map_enum.hasMoreElements()) {
			tmpstr = (String) url_encoded_fmt_stream_map_enum.nextElement();
			begin = tmpstr.indexOf("itag=");
			if (begin != -1) {
				end = tmpstr.indexOf("&", begin + 5);

				if (end == -1) {
					end = tmpstr.length();
				}

				int fmt = Integer.parseInt(tmpstr.substring(begin + 5, end));

				if (fmt == 35) {
					begin = tmpstr.indexOf("url=");
					if (begin != -1) {
						end = tmpstr.indexOf("&", begin + 4);
						if (end == -1) {
							end = tmpstr.length();
						}
						decoded_url = URLDecoder.decode(
								tmpstr.substring(begin + 4, end), "UTF-8");
						break;
					}
				}
			}
		}
		link = new URL(decoded_url);
		is = link.openStream();
		HttpURLConnection huc =(HttpURLConnection) link.openConnection(); 
		if (huc != null) { 
			int size = huc.getContentLength(); 
			FileOutputStream fos = new FileOutputStream(path); 
			if (fos != null) { 
				byte[] d_buffer = new byte[1024]; 
				int length = 0; 
				while ((length = is.read(d_buffer)) > 0) {
				 fos.write(d_buffer, 0, length);
				 }
				 fos.close(); 
				 } 
			huc.disconnect(); 
				 is.close(); 
				 return "successful"; 
				 }
				 return null;
	}
}
