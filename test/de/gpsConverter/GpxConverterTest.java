package de.gpsConverter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Test;

import de.gpsConverter.controler.GpxConverter;
import de.gpsConverter.controler.Result;
import de.gpsConverter.controler.Result.State;
import de.gpsConverter.controler.ResultCallBack;
import de.gpsConverter.controler.ResultCallBack.ResultCallBackListener;
import de.gpsConverter.model.GpxDocument;
import de.gpsConverter.model.Track;

public class GpxConverterTest {

	@Test
	public void convertJpgs() throws Exception {
		GpxConverter converter = new GpxConverter(new File(
				"test-resources/sample"));
		converter.convertToGpx(new ResultCallBack<GpxDocument>(
				new ResultCallBackListener<GpxDocument>() {

					public void setSteps(int steps) {
					}

					public void step(int doneSteps) {
					}

					public void setResult(Result<GpxDocument> result) {
						assertEquals(State.Ok, result.getState());
						assertTrue(result.getBufferedExceptions().isEmpty());

						GpxDocument gpx = result.getResult();
						assertNotNull(gpx);
						Track track = gpx.getTrack();
						assertNotNull(track);
						assertEquals(6, track.getTrackPoints().size());
					}
				}));
	}

	@Test
	public void convertJpgsWithNonExistingFolder() {
		GpxConverter converter = new GpxConverter(new File(
				"test-resources/not-exist"));
		converter.convertToGpx(new ResultCallBack<GpxDocument>(
				new ResultCallBackListener<GpxDocument>() {

					public void setSteps(int steps) {
					}

					public void step(int doneSteps) {
					}

					public void setResult(Result<GpxDocument> result) {
						assertEquals(State.Error, result.getState());
						assertFalse(result.getBufferedExceptions().isEmpty());
					}
				}));
	}

	@Test
	public void convertJpgsWithSameFailures() {
		GpxConverter converter = new GpxConverter(new File(
				"test-resources/sampleWithFail"));
		converter.convertToGpx(new ResultCallBack<GpxDocument>(
				new ResultCallBackListener<GpxDocument>() {

					public void setSteps(int steps) {
					}

					public void step(int doneSteps) {
					}

					public void setResult(Result<GpxDocument> result) {
						assertEquals(State.Warning, result.getState());
						List<Exception> bufferedExceptions = result
								.getBufferedExceptions();
						assertFalse(bufferedExceptions.isEmpty());
						assertEquals(2, bufferedExceptions.size());

						GpxDocument gpx = result.getResult();
						assertNotNull(gpx);
						Track track = gpx.getTrack();
						assertNotNull(track);
						assertEquals(1, track.getTrackPoints().size());
					}
				}));
	}
}
