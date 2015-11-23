import java.awt.FlowLayout;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.BoxLayout;

/**
 * the class for showing the download process
 */
public class DownloadingWindow extends JPanel {

	/**
	 * the serialVersionUID field.
	 */
	private static final long serialVersionUID = 1L;

	/** 
	 * constructor
	 *
	 * @param downloadList   the list of files to download
	 * @param saveLocation   the saving location
	 * @param threadsNum     the threads number
	 */
	public DownloadingWindow(ArrayList<String> downloadList, String saveLocation, String threadsNum) {
		
		// set the box layout
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		// initiate the threads pool
		ExecutorService pool = Executors.newFixedThreadPool(Integer.valueOf(threadsNum));
		
		// for each file in the download list
		for(int i = 0; i < downloadList.size(); i++)
		{
			// get the url of the files
			String urlstr = downloadList.get(i);
			
			// create the panel for each files
			JPanel panel1 = new JPanel(new FlowLayout());
			add(panel1);
			
			// get the name of the files
			String fileName = urlstr.substring( urlstr.lastIndexOf('/')+1, urlstr.length() );
			
			// the label showing the name of the files
			JLabel elemNameLabel = new JLabel(fileName);
			panel1.add(elemNameLabel);
			
			// the label showing the downloading progress ( waiting when not download yet )
			JLabel progress = new JLabel("Waiting");
			panel1.add(progress);
			
			// the downloading task of the files
			DownloadTask task = new DownloadTask(urlstr, saveLocation, progress, this);
			
			// add the task for the job queue of the pool
			pool.execute(task);
			
		}
		// Step 3: shutdown of the ExecutorService:
		pool.shutdown();
	}
	
}

/**
 * the class for setting up the download task for the files
 */
class DownloadTask implements Runnable {
	/**
	 * the url of the files
	 */
    private String urlstr;
    
    /**
     * the saving locaiton
     */
    private final String toPath;
    
    /**
     * the label showing the progress
     */
    private final JLabel progress;
    
    /**
     * the panel showing the download process
     */
    private final JPanel panel;

    /** 
	 * constructor
	 * 
	 * @param urlstr      the url of the file
	 * @param toPath      the saving locaiton
	 * @param progress    the label showing the progress of the download
	 * @param panel       the panel showing the download process
	 */
    public DownloadTask(String urlstr, String toPath, JLabel progress, JPanel panel) {
        this.urlstr = urlstr;
        this.toPath = toPath;
        this.progress = progress;
        this.panel = panel;
    }
    
    /**
     * the downloading task
     */
    @Override
    public void run() {
        // set the progress download as downloading when the file start to download
    	progress.setText("Downloading");
    	panel.revalidate();
    	panel.repaint();
    	
    	// get the fileName
    	String fileName = urlstr.substring( urlstr.lastIndexOf('/')+1, urlstr.length() );
    	
    	try
    	{
	    	//try to open the url
			URL url = new URL(urlstr);
			
			// initiate the streams for getting the files
			InputStream in = url.openStream();
			OutputStream out = new BufferedOutputStream(new FileOutputStream(toPath + fileName));
			
			// get the files
			for (int b; (b = in.read()) != -1;) {
			out.write(b);
			}
			
			// close the streams
			out.close();
			in.close();
    	} catch (IOException e) {
			e.printStackTrace();
				
			// open error window if there is exception	
			JLabel errorLabel = new JLabel("Can't download file to the save location");
			
			JFrame error = new JFrame("error");
			
			error.getContentPane().add(errorLabel);
			
			error.pack();
			
			error.setVisible(true);
		}
    	finally
    	{
    		// set the progress label as downloaded when the file is downloaded
    		progress.setText("Downloaded");
        	panel.revalidate();
        	panel.repaint();
    	}
    	}
}

