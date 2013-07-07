package com.solomonsites.windownloader.api;

import java.io.IOException;
import java.net.MalformedURLException;

public interface YoutubeDownloaderApi<TInput, TOutput> {
public TOutput downloadVideo(TInput url,TInput path) throws MalformedURLException, IOException;
}
