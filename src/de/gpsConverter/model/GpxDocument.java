package de.gpsConverter.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "gpx", namespace = "http://www.topografix.com/GPX/1/1")
public class GpxDocument {

	@XmlElement(name = "trk", namespace = "http://www.topografix.com/GPX/1/1")
	private Track track;

	@XmlAttribute(name = "version")
	public static final double version = 1.1;

	@XmlAttribute(name = "creator")
	public static final String creator = "GPX Converter";

	public Track getTrack() {
		return track;
	}

	public void setTrack(Track track) {
		this.track = track;
	}

	@Override
	public String toString() {
		return "GpxDocument [track=" + track + "]";
	}

	public boolean isEmpty() {
		if (track != null)
			return track.getTrackPoints().isEmpty();
		else
			return true;
	}

	public int size() {
		if (track != null)
			return track.getTrackPoints().size();
		else
			return 0;
	}
}
