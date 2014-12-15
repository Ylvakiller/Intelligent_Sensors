package image;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * This class will be the class that opens a picture, stores that in memory and returns that.
 * for now only a stub, more documentation will come soon
 * @author Ylva
 *
 */
public class FileAccess {
	static String fileName = "./InputImages/plate05.jpg";
	JPanel editorPanel;
	/**
	 * Default Constructor
	 */
	public FileAccess(){
		
	}
	
	/**
	 * Constructor with a file name in it.
	 * @param fileName
	 */
	public FileAccess(String fileName){
		FileAccess.fileName = fileName;
	}
	
	/**
	 * Constructor with the JPanel for the image in it
	 * @param imagePanel
	 */
	public FileAccess(JPanel imagePanel){
		editorPanel = imagePanel;
	}
	
	/**
	 * Returns the loaded imageIcon for a image file to be used in a JPanel
	 * @return	the imageIcon for the fileName specified in the constructor
	 * @throws Exception imageIO exception if image does not exist or is unreadable
	 */
	public static ImageIcon getImageIcon(BufferedImage image) throws Exception
	{
		ImageIcon imageIcon = new ImageIcon(image);
		return imageIcon;
	}
	
	/**
	 * This method is to return only a buffered image
	 * @return A buffered image
	 */
	public static BufferedImage getImage(){
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
	
	/**
	 * Writes an imageFile in the OutputImages folder called test
	 * @param buffer the bufferImage to be written to
	 */
	public static void writeImage(BufferedImage buffer){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		File f = new File("./OutputImages/test.png");
		ImageOutputStream ios = null;
		try {
			ios = ImageIO.createImageOutputStream(f);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		writer.setOutput(ios);
		try {
			writer.write(buffer);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			ios.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
