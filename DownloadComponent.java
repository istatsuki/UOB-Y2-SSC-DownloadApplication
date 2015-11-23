import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JCheckBox;

import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * the class for choosing the file types and inputting the website address
 */
public class DownloadComponent extends JPanel {

	/**
	 * the serialVersionUID field.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the text field for inputting the website address.
	 */
	private JTextField webAddressF;
	
	/**
	 * the list of the urls of the images.
	 */
	private ArrayList<String> imageList;
	
	/**
	 * the list of the urls of the href.
	 */
	private ArrayList<String> hrefList;
	

	/** 
	 * constructor
	 */
	public DownloadComponent() {
		// set absolute layout
		setLayout(null);
		
		// initiate fields
		imageList = new ArrayList<String>();
		hrefList = new ArrayList<String>();
		
		// create fields for inputting the website address
		JLabel lblWebsiteAddress = new JLabel("Website address:");
		lblWebsiteAddress.setBounds(10, 11, 118, 20);
		add(lblWebsiteAddress);
		
		webAddressF = new JTextField();
		webAddressF.setBounds(124, 11, 228, 20);
		add(webAddressF);
		webAddressF.setColumns(10);
		
		// create the button for getting files
		JButton getFilesBut = new JButton("Get Files");
		getFilesBut.setBounds(234, 38, 118, 48);
		add(getFilesBut);
		
		// create options for choosing the file types
		JLabel lblNewLabel_1 = new JLabel("File Types:");
		lblNewLabel_1.setBounds(10, 42, 85, 14);
		add(lblNewLabel_1);
		
		JCheckBox chckbxJpg = new JCheckBox("jpg");
		chckbxJpg.setBounds(79, 38, 44, 23);
		add(chckbxJpg);
		
		JCheckBox chckbxJpeg = new JCheckBox("jpeg");
		chckbxJpeg.setBounds(124, 38, 52, 23);
		add(chckbxJpeg);
		
		JCheckBox chckbxPng = new JCheckBox("png");
		chckbxPng.setBounds(178, 38, 50, 23);
		add(chckbxPng);
		
		JCheckBox chckbxGif = new JCheckBox("gif");
		chckbxGif.setBounds(79, 63, 44, 23);
		add(chckbxGif);
		
		JCheckBox chckbxZip = new JCheckBox("zip");
		chckbxZip.setBounds(124, 66, 52, 20);
		add(chckbxZip);
		
		JCheckBox chckbxPdf = new JCheckBox("pdf");
		chckbxPdf.setBounds(178, 66, 44, 20);
		add(chckbxPdf);
		
		// the get-file button listener
		getFilesBut.addActionListener(new ActionListener()
					{
							public void actionPerformed(ActionEvent e)
							{
								// get the website address
								String webAddress = webAddressF.getText();
								
								// clear the lists
								imageList.clear();
								hrefList.clear();
								
								// check for empty website address
								if ((webAddress != null) && (webAddress.length() > 0)) {
									
									// check if address is valid
									URL u;
									try {
										u = new URL(webAddress);
										
										// check which file types are selected
										if(chckbxJpg.isSelected())
										{
											imageList.add("jpg");					
										}
										
										if(chckbxJpeg.isSelected())
										{
											imageList.add("jpeg");								
										}
										
										if(chckbxPng.isSelected())
										{
											imageList.add("png");									
										}
										
										if(chckbxGif.isSelected())
										{
											imageList.add("gif");								
										}
										
										if(chckbxZip.isSelected())
										{
											hrefList.add("zip");									
										}
										
										if(chckbxPdf.isSelected())
										{
											hrefList.add("pdf");								
										}
										
										// create the component for showing the list of files
										DownloadList list = new DownloadList(webAddress, imageList, hrefList);
										
										// create the frame for holding the component
										JFrame frame = new JFrame("Download List");
										frame.add(list);
										frame.setSize(390, 470);
										frame.setVisible(true);	 
										
									} catch (MalformedURLException e1) {									
										e1.printStackTrace();
										
										// open error window if there is exception
										JLabel errorLabel = new JLabel("The website address is invalid");
										
										JFrame error = new JFrame("error");
										
										error.getContentPane().add(errorLabel);
										
										error.pack();
										
										error.setVisible(true);
									} 									
								}
							}
					});
	}	
}
