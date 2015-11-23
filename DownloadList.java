import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.JComboBox;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;

import javax.swing.JFrame;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.SwingUtilities;

import java.awt.FlowLayout;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;

/**
 * the class for choosing the files to download and inputting the saving location
 */
public class DownloadList extends JPanel {
	/**
	 * the serialVersionUID field.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * the text field for inputting the saving location
	 */
	private JTextField saveLocationF;
	
	/**
	 * the box for choosing the number of threads
	 */
	private JComboBox threadsNumF;
	
	/**
	 * the list of file addresses to download
	 */
	private ArrayList<String> downloadList;
	
	/**
	 * the icon for pdf files
	 */
	private BufferedImage pdfImage;
	
	/**
	 * the icon for zip files
	 */
	private BufferedImage zipImage;
	
	/**
	 * the website Address
	 */
	private String webAddress;
	
	/** 
	 * constructor
	 * 
	 * @param webAddress  the website address
	 * @param imageList   the list of image types to download
	 * @param hrefList    the list of href types to download
	 */
	public DownloadList(String webAddress, ArrayList<String> imageList, ArrayList<String> hrefList) {
		// set absolute layout
		setLayout(null);
		
		// initiate fields
		downloadList = new ArrayList<String>();		
		this.webAddress = webAddress;
		
		// get the icon images
		try {                
	          pdfImage = ImageIO.read(new File("D:\\Program Files\\Java codes\\ex4-SSCyear2\\src\\pdf.png"));
	          zipImage = ImageIO.read(new File("D:\\Program Files\\Java codes\\ex4-SSCyear2\\src\\zip.png"));
	       } catch (IOException ex) {
	    	   ex.printStackTrace();
	    	   
	    	    // open error window if there is exception	
	    	    JLabel errorLabel = new JLabel("Can't get image elements for the panel");
				
				JFrame error = new JFrame("error");
				
				error.getContentPane().add(errorLabel);
				
				error.pack();
				
				error.setVisible(true);
	       }
		
		// the fields for inputting the saving location
		JLabel label = new JLabel("Save location:");
		label.setBounds(10, 361, 85, 14);
		add(label);
		
		saveLocationF = new JTextField();
		saveLocationF.setColumns(10);
		saveLocationF.setBounds(105, 358, 241, 20);
		add(saveLocationF);
		
		// the fields for choosing the number of threads
		JLabel label_1 = new JLabel("Number of threads:");
		label_1.setBounds(10, 389, 122, 20);
		add(label_1);
		
		String[] threadsNum = {"1", "2", "3", "4", "5"};
		threadsNumF = new JComboBox(threadsNum);
		threadsNumF.setBounds(142, 389, 43, 20);
		add(threadsNumF);
		
		// the button for initiate the downloading action
		JButton downloadBut = new JButton("Download Files");
		downloadBut.setBounds(210, 389, 136, 23);
		add(downloadBut);

		// the download button action
		downloadBut.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						// get the saving location and the threads number
						String saveLocation = saveLocationF.getText();
						String threadsNum = threadsNumF.getSelectedItem().toString();
						
						// check for saving location
						File f = new File(saveLocation);						
						if(f.exists() && f.isDirectory())
						{
							// scroll panel for the component
							JScrollPane scrollPane = new JScrollPane();
							
							// the component for showing the download process of the files
							DownloadingWindow wind = new DownloadingWindow(downloadList, saveLocation, threadsNum);
							
							// put the component in the scroll panel
							scrollPane.setViewportView(wind);
							
							// the frame for showing the component
							JFrame frame = new JFrame("Downloading Window");						
							frame.add(scrollPane);
							frame.setSize(500, 500);							
							frame.setVisible(true);

							// close the frame
							closeFrame();
						}
						else
						{
							// open error window if there is error
							JLabel errorLabel = new JLabel("Saving location is invalid");
							
							JFrame error = new JFrame("error");
							
							error.getContentPane().add(errorLabel);
							
							error.pack();
							
							error.setVisible(true);
						}
					}
				}
		);
		
		// the fields for showing the lists of the files
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 5, 336, 334);
		add(scrollPane);
		
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		
		Document doc;
		try {
				// get the http protocol
				doc = Jsoup.connect(webAddress).get();
				
				for(int i = 0; i < imageList.size(); i++)
				{
					// get elements representing the files
					Elements elems = doc.select("img[src~=(?i)\\.(" + imageList.get(i) + ")]");
					
					// put the elements up on the panel
					putElems(elems, panel);
				}
				
				for(int i = 0; i < hrefList.size(); i++)
				{
					// get elements representing the files
					Elements elems = doc.select("a[href$=." + hrefList.get(i) + "]");
					// put the elements up on the panel
					putElems(elems, panel);
				}											

		} catch (IOException e) {
			e.printStackTrace();
			
			// open error window if there is exception
			JLabel errorLabel = new JLabel("Can't connect to the website with the address provided");
			
			JFrame error = new JFrame("error");
			
			error.getContentPane().add(errorLabel);
			
			error.pack();
			
			error.setVisible(true);
		}

	}
	
	/** 
	 * method for putting the element on the panel
	 * 
	 * @param elems  the elements
	 * @param panel  the panel
	 */
	public void putElems(Elements elems, JPanel panel)
	{
		// check for every element on the list
		for (Element elem : elems) {
			// get the url of the element as an image
			String urlstr = elem.attr("src");
			
			// initiate the image object for the icon
			BufferedImage image = null;
			
			// if the element is not an image
			if(urlstr.equals(""))
			{
				// get the element as a href
				urlstr = elem.attr("href");
				
				// get the icon for the right types
				if(urlstr.indexOf("zip") >= 0)
				{
					image = zipImage;
				}
				else
				{
					image = pdfImage;
				}
				
				// make the url object to a vaid url
				if(urlstr.indexOf(webAddress)<=0)
				{
					urlstr = webAddress + urlstr;
				}
			}
			else
			{
				// make the url object to a vaid url
				if(urlstr.indexOf(webAddress)<=0)
				{
					urlstr = webAddress + urlstr;
				}
				
				try {
					// get the icon for the image
					image = ImageIO.read(new URL(urlstr));
				} catch (MalformedURLException e) {
					
					// open error window if there is exception
					JLabel errorLabel = new JLabel("Can't get image elements for the panel");
					
					JFrame error = new JFrame("error");
					
					error.getContentPane().add(errorLabel);
					
					error.pack();
					
					error.setVisible(true);
					
					e.printStackTrace();
				} catch (IOException e) {
					
					// open error window if there is exception
					JLabel errorLabel = new JLabel("Can't get image elements for the panel");
					
					JFrame error = new JFrame("error");
					
					error.getContentPane().add(errorLabel);
					
					error.pack();
					
					error.setVisible(true);
					
					e.printStackTrace();
				}
			}
			
			// panel for each file
			JPanel panel1 = new JPanel(new FlowLayout());
			panel.add(panel1);
			
			// check box for choosing to download the files or not
			JCheckBox checkDum = new JCheckBox("");
			panel1.add(checkDum);	
			
			// the icon of the files
			JLabel Icon = new JLabel(new ImageIcon(image.getScaledInstance(30, 30, Image.SCALE_FAST)));
			panel1.add(Icon);
			
			// the name of the files
			String elemName = urlstr.substring( urlstr.lastIndexOf('/')+1, urlstr.length() );
			JLabel elemNameLabel = new JLabel(elemName);
			panel1.add(elemNameLabel);
			
			// the string object for adding or removing the url from the download list
			final String urlstr_ex = urlstr;
			
			// check boxes action
			checkDum.addActionListener(new ActionListener()
					{
						public void actionPerformed(ActionEvent e2)
						{
							// if is checked, add the files to the download list
							if(checkDum.isSelected())
							{
								downloadList.add(urlstr_ex);
							}
							// else remove files from the download list
							else
							{
								downloadList.remove(urlstr_ex);
							}
						}
					}
			);
			
			// repaint the panel
			panel.revalidate();
			
		}		
	}
	
	/**
	 * The method is for closing the composing window.
	 */
	public void closeFrame()
	{
		// the frame
		JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
		
		// close the frame
		frame.setVisible(false);
	}

}
