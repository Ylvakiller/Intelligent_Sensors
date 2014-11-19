package image;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

/**
 * This class will be the class that opens a picture, stores that in memory and returns that.
 * for now only a stub, more documentation will come soon
 * @author Ylva
 *
 */
public class Opener {
	String fileName = "./InputImages/plate02.jpg";
	
	/**
	 * Default Constructor
	 */
	public Opener(){
		
	}
	
	/**
	 * Constructor with a file name in it.
	 * @param fileName
	 */
	public Opener(String fileName){
		this.fileName = fileName;
	}
	
	/**
	 * Opens an image and displays that on a screen
	 * @throws Exception this is an ImageIO exception, for example when the program cannot read or find the file
	 */
	public void ImageDemo() throws Exception
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame editorFrame = new JFrame("Image Demo");
				editorFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

				BufferedImage image = null;
				try
				{
					image = ImageIO.read(new File(fileName));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(1);
				}
				ImageIcon imageIcon = new ImageIcon(image);
				JLabel jLabel = new JLabel();
				jLabel.setIcon(imageIcon);
				editorFrame.getContentPane().add(jLabel, BorderLayout.CENTER);

				editorFrame.pack();
				editorFrame.setLocationRelativeTo(null);
				editorFrame.setVisible(true);
			}
		});
	}
}
