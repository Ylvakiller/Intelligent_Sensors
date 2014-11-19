package image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class will be the class that opens a picture, stores that in memory and returns that.
 * for now only a stub, more documentation will come soon
 * @author Ylva
 *
 */
public class Opener {
	String fileName = "./InputImages/plate02.jpg";
	JPanel editorPanel;
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
	 * Constructor with the JPanel for the image in it
	 * @param imagePanel
	 */
	public Opener(JPanel imagePanel){
		editorPanel = imagePanel;
	}
	
	/**
	 * Returns the loaded imageIcon for a image file to be used in a JPanel
	 * @return	the imageIcon for the fileName specified in the constructor
	 * @throws Exception	imageIO exception if image does not excist or is unreadable
	 */
	public ImageIcon getImageIcon() throws Exception
	{
		ImageIcon imageIcon = new ImageIcon(this.getImage());
		return imageIcon;
	}
	
	/**
	 * This method is to return only a buffered image
	 * @return
	 */
	public BufferedImage getImage(){
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
		return image;
	}
}
