import javax.swing.JFrame;

/**
 * the class for running the program
 */
public class DownloadGUI {
	public static void main(String[] args) {
		// create the JFrame for showing the program
		JFrame frame = new JFrame("DownloadGUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// create the program content
		DownloadComponent comp = new DownloadComponent();
		
		// add the content to the frame
		frame.add(comp);		
		
		// size the frame
		frame.setSize(390, 150);
		
		// show the program on the JFrame
		frame.setVisible(true);
	}

}
