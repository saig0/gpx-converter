package de.gpsTrack.view;

import java.net.URL;

public enum IconPool {

	Ok("ok.png"), Warning("warning.png"), Error("error.png");

	private final String basePath = "/resources/images/";
	private String url;

	IconPool(String url) {
		this.url = url;
	}

	public URL getUrl() {
		return getClass().getResource(basePath + url);
	}
}
