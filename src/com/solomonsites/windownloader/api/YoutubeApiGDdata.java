package com.solomonsites.windownloader.api;

import java.io.IOException;

import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;

public interface YoutubeApiGDdata extends YoutubeApi<VideoFeed, String> {
	public VideoFeed searchFeed(String searchTerms) throws IOException, ServiceException;
}
