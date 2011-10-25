package de.gpsConverter.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;

import de.gpsConverter.controler.GpxConverter;
import de.gpsConverter.controler.Result;
import de.gpsConverter.controler.ResultCallBack;
import de.gpsConverter.controler.ResultCallBack.ResultCallBackListener;
import de.gpsConverter.model.GpxDocument;

public class GpsConverter {

	public static void main(String[] args) {
		GpsConverter gpsConverter = new GpsConverter();
		gpsConverter.show();
	}

	private MessageBar messagePanel;
	private JProgressBar progressBar;
	private JButton converterButton;

	private void show() {
		try {
			UIManager
					.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
		} catch (Exception e) {
			e.printStackTrace();
		}

		final JFrame dialog = new JFrame("GPS Converter");
		dialog.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		dialog.setLayout(new BorderLayout(5, 5));

		JPanel directoryPanel = new JPanel(new BorderLayout(10, 5));
		directoryPanel.setBorder(BorderFactory
				.createTitledBorder("Verzeichnis der JPEG-Dateien angeben"));
		dialog.add(directoryPanel, BorderLayout.PAGE_START);
		directoryPanel.setMinimumSize(new Dimension(400, 100));

		final JTextField directoryLabel = new JTextField();
		directoryLabel.setEditable(false);
		directoryPanel.add(directoryLabel);

		JButton directoryChooseButton = new JButton("auswählen");
		directoryChooseButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

				int state = fileChooser.showOpenDialog(null);
				if (state == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					directoryLabel.setText(file.getPath());
				}
			}
		});
		directoryPanel.add(directoryChooseButton, BorderLayout.LINE_END);

		JPanel actionPanel = new JPanel(new BorderLayout(10, 5));
		dialog.add(actionPanel);
		actionPanel.setMinimumSize(new Dimension(400, 100));

		progressBar = new JProgressBar();

		converterButton = new JButton("nach GPX konvertieren");
		converterButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				converterButton.setEnabled(false);
				messagePanel.showMessage("");
				String directory = directoryLabel.getText();
				if (!directory.isEmpty()) {
					GpxConverter converter = new GpxConverter(new File(
							directory));
					converter.convertToGpx(new ResultCallBack<GpxDocument>(
							new ResultCallBackListener<GpxDocument>() {

								public void setSteps(int steps) {
									progressBar.setValue(0);
									progressBar.setMaximum(steps);
								}

								public void step(int doneSteps) {
									progressBar.setValue(doneSteps);
								}

								public void setResult(Result<GpxDocument> result) {
									if (result.getResult() != null
											&& !result.getResult().isEmpty())
										messagePanel
												.showResult(
														result,
														result.getResult()
																.size()
																+ " Dateien erfolgreich nach GPX umgewandelt!");
									else if (result.getResult() != null
											&& result.getResult().isEmpty())
										messagePanel
												.showWarning("keine Dateien nach GPX umgewandelt!");
									else
										messagePanel.showResult(result,
												"Ein Fehler ist aufgetreten!");

									converterButton.setEnabled(true);
								}
							}));
				} else {
					messagePanel.showWarning("Kein Verzeichnis ausgewählt!");
					converterButton.setEnabled(true);
				}
			}
		});
		converterButton.setSize(400, 25);
		actionPanel.add(converterButton);

		actionPanel.add(progressBar, BorderLayout.PAGE_END);

		messagePanel = new MessageBar();
		dialog.add(messagePanel, BorderLayout.PAGE_END);

		// dialog.setSize(new Dimension(400, 300));
		dialog.pack();
		dialog.setLocationByPlatform(true);
		dialog.setVisible(true);
	}
}
