package de.gpsConverter.controler;

public class ResultCallBack<T> {

	public interface ResultCallBackListener<T> {
		public void setSteps(int steps);

		public void step(int doneSteps);

		public void setResult(Result<T> result);
	}

	private final ResultCallBackListener<T> listener;

	private int doneSteps = 0;

	public ResultCallBack(ResultCallBackListener<T> listener) {
		this.listener = listener;
	}

	public void setSteps(int steps) {
		listener.setSteps(steps);
	}

	public void step() {
		doneSteps += 1;
		listener.step(doneSteps);
	}

	public void setResult(Result<T> result) {
		listener.setResult(result);
		doneSteps = 0;
	}
}
