package de.gpsTrack.controler;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.GpsDirectory;

import de.gpsTrack.model.ConverterException;

public class GpsCoordinateExtractor {

	private final Metadata metadata;

	public GpsCoordinateExtractor(Metadata metadata) {
		this.metadata = metadata;
	}

	public boolean hasGpsCoordinates() {
		Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
		if (gpsDir != null) {
			return gpsDir.containsTag(GpsDirectory.TAG_GPS_LATITUDE)
					&& gpsDir.containsTag(GpsDirectory.TAG_GPS_LONGITUDE);
		} else
			return false;
	}

	public double getLatitude() throws ConverterException, MetadataException {
		Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
		if (hasGpsCoordinates()) {
			String latitudeAsString = gpsDir
					.getDescription(GpsDirectory.TAG_GPS_LATITUDE);
			String latitudeRef = gpsDir
					.getDescription(GpsDirectory.TAG_GPS_LATITUDE_REF);

			double latitude = convertHourToDecimal(latitudeAsString);

			if (latitudeRef.equalsIgnoreCase("S"))
				latitude = latitude * -1;

			return latitude;
		} else
			throw new ConverterException("keine GPS-Koordinaten vorhanden");
	}

	public double getLongitude() throws ConverterException, MetadataException {
		Directory gpsDir = metadata.getDirectory(GpsDirectory.class);
		if (hasGpsCoordinates()) {
			String lontitudeAsString = gpsDir
					.getDescription(GpsDirectory.TAG_GPS_LONGITUDE);
			String lontitudeRef = gpsDir
					.getDescription(GpsDirectory.TAG_GPS_LONGITUDE_REF);

			double lontitude = convertHourToDecimal(lontitudeAsString);

			if (lontitudeRef.equalsIgnoreCase("W"))
				lontitude = lontitude * -1;

			return lontitude;
		} else
			throw new MetadataException("keine GPS-Koordinaten verhanden");
	}

	private double convertHourToDecimal(String degree) {
		String[] strArray = degree.split("[\"']");
		return Double.parseDouble(strArray[0])
				+ Double.parseDouble(strArray[1]) / 60
				+ Double.parseDouble(strArray[2]) / 3600;
	}
}
