package de.gpsTrack.model;

import com.drew.lang.CompoundException;

public class ConverterException extends CompoundException {

	private static final long serialVersionUID = 1L;

	public ConverterException(String msg) {
		super(msg);
	}

	public ConverterException(Throwable exception) {
		super(exception);
	}

	public ConverterException(String msg, Throwable innerException) {
		super(msg, innerException);
	}

	@Override
	public String toString() {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append(getMessage());
		if (getInnerException() != null) {
			sbuffer.append("\n");
			sbuffer.append("  > ");
			sbuffer.append(getInnerException().toString());
		}
		return sbuffer.toString();
	}

}
