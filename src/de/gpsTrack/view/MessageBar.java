package de.gpsTrack.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.gpsTrack.controler.Result;
import de.gpsTrack.controler.Result.State;

public class MessageBar extends JPanel {

	private static final long serialVersionUID = 1L;

	private JLabel stateIcon;
	private JTextArea messageArea;

	public MessageBar() {
		super(new BorderLayout(5, 5));
		setBorder(BorderFactory.createTitledBorder("Meldungen"));

		stateIcon = new JLabel();
		add(stateIcon, BorderLayout.LINE_START);

		messageArea = new JTextArea();
		messageArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(messageArea);
		add(scrollPane);

		setPreferredSize(new Dimension(400, 100));
	}

	public void showResult(Result<?> result, String message) {
		if (result.getState() == State.Ok) {
			showInfo(message);
		} else if (result.getState() == State.Warning) {
			showWarning(message + "\n"
					+ formatExceptions(result.getBufferedExceptions()));
		} else if (result.getState() == State.Error) {
			showError(message + "\n"
					+ formatExceptions(result.getBufferedExceptions()));
		} else {
			showMessage(message);
		}
	}

	public String formatExceptions(List<Exception> bufferedExceptions) {
		String formatedExceptions = "";
		for (Exception e : bufferedExceptions) {
			formatedExceptions += "\n" + e;
		}
		return formatedExceptions;
	}

	public void showMessage(String message) {
		stateIcon.setIcon(new ImageIcon());
		messageArea.setText(message);
	}

	public void showInfo(String message) {
		stateIcon.setIcon(new ImageIcon(IconPool.Ok.getUrl()));
		messageArea.setText(message);
	}

	public void showWarning(String message) {
		stateIcon.setIcon(new ImageIcon(IconPool.Warning.getUrl()));
		messageArea.setText(message);
	}

	public void showError(String message) {
		stateIcon.setIcon(new ImageIcon(IconPool.Error.getUrl()));
		messageArea.setText(message);
	}

}
