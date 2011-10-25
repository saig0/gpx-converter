package de.gpsConverter.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "trk", namespace = "http://www.topografix.com/GPX/1/1")
public class Track {

	@XmlElement(name = "name", required = true, namespace = "http://www.topografix.com/GPX/1/1")
	private String name;

	@XmlElementWrapper(name = "trkseg", namespace = "http://www.topografix.com/GPX/1/1")
	@XmlElement(name = "trkpt", namespace = "http://www.topografix.com/GPX/1/1")
	private List<TrackPoint> trackPoints = new ArrayList<TrackPoint>();

	Track() {
	}

	public Track(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<TrackPoint> getTrackPoints() {
		return trackPoints;
	}

	public void setTrackPoints(List<TrackPoint> trackPoints) {
		this.trackPoints = trackPoints;
	}

	public void addTrackPoint(TrackPoint trackPoint) {
		trackPoints.add(trackPoint);
	}

	@Override
	public String toString() {
		return "Track [name=" + name + ", trackPoints=" + trackPoints + "]";
	}
}
