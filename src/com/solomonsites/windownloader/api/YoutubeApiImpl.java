package com.solomonsites.windownloader.api;

import java.io.IOException;
import java.net.URL;
import com.google.gdata.client.youtube.YouTubeQuery;
import com.google.gdata.client.youtube.YouTubeService;
import com.google.gdata.data.youtube.VideoFeed;
import com.google.gdata.util.ServiceException;

public class YoutubeApiImpl implements YoutubeApiGDdata {
	private static final String YOUTUBE_GDATA_SERVER = "http://gdata.youtube.com";
	private static final String VIDEOS_FEED = YOUTUBE_GDATA_SERVER
		      + "/feeds/api/videos";
	private static YouTubeService mService = new YouTubeService("solomonsites");
	@Override
	public VideoFeed searchFeed(String searchTerms) throws IOException, ServiceException {
		YouTubeQuery query = new YouTubeQuery(new URL(VIDEOS_FEED));
	    query.setOrderBy(YouTubeQuery.OrderBy.VIEW_COUNT);
	    query.setSafeSearch(YouTubeQuery.SafeSearch.NONE);
	    query.setFullTextQuery(searchTerms);
	    VideoFeed videoFeed = mService.query(query, VideoFeed.class);
		return videoFeed;
	}

}
