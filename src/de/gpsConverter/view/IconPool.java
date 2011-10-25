package de.gpsConverter.view;

public enum IconPool {

	Ok("ok.png"), Warning("warning.png"), Error("error.png");

	private final String basePath = "resources/images/";
	private String url;

	IconPool(String url) {
		this.url = url;
	}

	public String getUrl() {
		return basePath + url;
	}
}
