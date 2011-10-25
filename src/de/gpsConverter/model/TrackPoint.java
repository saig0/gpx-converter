package de.gpsConverter.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "trkpt", namespace = "http://www.topografix.com/GPX/1/1")
public class TrackPoint {

	@XmlAttribute(name = "lat", required = true)
	private double latitude;

	@XmlAttribute(name = "lon", required = true)
	private double lontitude;

	@XmlElement(name = "time", required = true, namespace = "http://www.topografix.com/GPX/1/1")
	@XmlJavaTypeAdapter(value = DateAdapter.class, type = Date.class)
	private Date date;

	TrackPoint() {
	}

	public TrackPoint(double latitude, double lontitude, Date date) {
		this.latitude = latitude;
		this.lontitude = lontitude;
		this.date = date;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLontitude() {
		return lontitude;
	}

	public void setLontitude(double lontitude) {
		this.lontitude = lontitude;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "TrackPoint [latitude=" + latitude + ", lontitude=" + lontitude
				+ ", date=" + date + "]";
	}
}
