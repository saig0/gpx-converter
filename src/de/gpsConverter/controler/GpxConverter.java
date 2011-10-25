package de.gpsConverter.controler;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifDirectory;

import de.gpsConverter.controler.Result.State;
import de.gpsConverter.model.ConverterException;
import de.gpsConverter.model.GpxDocument;
import de.gpsConverter.model.Track;
import de.gpsConverter.model.TrackPoint;

public class GpxConverter {

	private static final class JpegFileFilter implements FilenameFilter {
		public boolean accept(File dir, String name) {
			return name.toLowerCase().endsWith(".jpg")
					|| name.toLowerCase().endsWith(".jpeg");
		}
	}

	private final File sourceDirectory;

	public GpxConverter(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public void convertToGpx(final ResultCallBack<GpxDocument> resultCallBack) {
		new Thread(new Runnable() {

			public void run() {
				convertToGpxAsync(resultCallBack);
			}
		}).start();
	}

	public void convertToGpxAsync(ResultCallBack<GpxDocument> resultCallBack) {
		try {
			String directoryName = sourceDirectory.getName();
			Result<GpxDocument> gpx = createGpxFromDirectory(directoryName,
					resultCallBack);
			writeGpx(directoryName, gpx.getResult());
			resultCallBack.setResult(gpx);
		} catch (Exception e) {
			e.printStackTrace();

			Result<GpxDocument> result = new Result<GpxDocument>();
			result.setState(State.Error);
			result.addBufferedException(e);
			resultCallBack.setResult(result);
		}
	}

	private Result<GpxDocument> createGpxFromDirectory(String directoryName,
			ResultCallBack<GpxDocument> resultCallBack)
			throws MetadataException {
		Result<GpxDocument> result = new Result<GpxDocument>();
		GpxDocument gpx = new GpxDocument();
		Track track = new Track(directoryName);
		gpx.setTrack(track);

		File[] jpegFiles = sourceDirectory.listFiles(new JpegFileFilter());
		resultCallBack.setSteps(jpegFiles.length);
		for (File jpegFile : jpegFiles) {
			try {
				TrackPoint trackPoint = convertJpegFile(jpegFile);
				track.addTrackPoint(trackPoint);
				resultCallBack.step();
			} catch (Exception e) {
				result.setState(State.Warning);
				result.addBufferedException(new ConverterException(
						"Fehler beim Verarbeiten der Datei: "
								+ jpegFile.getName(), e));
				resultCallBack.step();
			}
		}
		result.setResult(gpx);
		return result;
	}

	private void writeGpx(String directoryName, GpxDocument gpx)
			throws JAXBException, PropertyException, SAXException {
		JAXBContext jaxbContext = JAXBContext.newInstance(GpxDocument.class);
		Marshaller marshaller = jaxbContext.createMarshaller();
		marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
		marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");
		File schemaFile = new File("resources/gpx.xsd");
		Schema schema = schemaFactory.newSchema(schemaFile);
		marshaller.setSchema(schema);

		File gpxFile = new File(sourceDirectory, directoryName + ".gpx");
		marshaller.marshal(gpx, gpxFile);
	}

	private TrackPoint convertJpegFile(File jpegFile)
			throws ImageProcessingException, ConverterException,
			MetadataException {
		Metadata metadata = ImageMetadataReader.readMetadata(jpegFile);

		Date date = extractDate(metadata);

		GpsCoordinateExtractor gpsCoordinateExtractor = new GpsCoordinateExtractor(
				metadata);
		double latitude = gpsCoordinateExtractor.getLatitude();
		double lontitude = gpsCoordinateExtractor.getLongitude();

		return new TrackPoint(latitude, lontitude, date);
	}

	private Date extractDate(Metadata metadata) throws ConverterException,
			MetadataException {
		Directory exifDir = metadata.getDirectory(ExifDirectory.class);
		if (exifDir.containsTag(ExifDirectory.TAG_DATETIME))
			return exifDir.getDate(ExifDirectory.TAG_DATETIME);
		else if (exifDir.containsTag(ExifDirectory.TAG_DATETIME_DIGITIZED))
			return exifDir.getDate(ExifDirectory.TAG_DATETIME_DIGITIZED);
		else if (exifDir.containsTag(ExifDirectory.TAG_DATETIME_ORIGINAL))
			return exifDir.getDate(ExifDirectory.TAG_DATETIME_ORIGINAL);
		else
			throw new ConverterException("kein Aufnahmedatum vorhanden");
	}

}
