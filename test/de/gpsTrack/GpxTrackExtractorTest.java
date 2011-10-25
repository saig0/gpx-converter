package de.gpsTrack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.gpsTrack.controler.GpxTrackExtractor;
import de.gpsTrack.controler.Result;
import de.gpsTrack.controler.Result.State;
import de.gpsTrack.controler.ResultCallBack;
import de.gpsTrack.controler.ResultCallBack.ResultCallBackListener;
import de.gpsTrack.model.GpxDocument;
import de.gpsTrack.model.Track;

public class GpxTrackExtractorTest {

	boolean done = false;

	@Before
	public void setup() {
		done = false;
	}

	@Test
	public void convertJpgs() throws Exception {

		GpxTrackExtractor converter = new GpxTrackExtractor(new File(
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

						done = true;
					}
				}));
		Thread.sleep(2000);
		assertTrue(done);
	}

	@Test
	public void convertJpgsWithNonExistingFolder() throws InterruptedException {
		GpxTrackExtractor converter = new GpxTrackExtractor(new File(
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

						done = true;
					}
				}));
		Thread.sleep(2000);
		assertTrue(done);
	}

	@Test
	public void convertJpgsWithSameFailures() throws InterruptedException {
		GpxTrackExtractor converter = new GpxTrackExtractor(new File(
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

						done = true;
					}
				}));
		Thread.sleep(2000);
		assertTrue(done);
	}
}
