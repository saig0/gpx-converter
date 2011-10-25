package de.gpsTrack.controler;

import java.util.LinkedList;
import java.util.List;

public class Result<T> {

	public enum State {
		Ok, Warning, Error
	}

	private State state = State.Ok;
	private T result;

	private List<Exception> bufferedExceptions = new LinkedList<Exception>();

	public State getState() {
		return state;
	}

	public List<Exception> getBufferedExceptions() {
		return bufferedExceptions;
	}

	public void addBufferedException(Exception e) {
		bufferedExceptions.add(e);
	}

	public T getResult() {
		return result;
	}

	public void setState(State state) {
		this.state = state;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
