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
	private static String fileNameInput = "./InputImages/plate";
	private static String fileNameOutput = "./OutputImages/plate";
	public static String fileNumber = "00";
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
		FileAccess.fileNameInput = fileName;
	}

	/**
	 * Constructor with the JPanel for the image in it
	 * @param imagePanel
	 */
	public FileAccess(JPanel imagePanel){
		editorPanel = imagePanel;
	}
	/**
	 * This constructor will be for processing multiple images at the same time
	 * All the other statics methods will check if the thisFileNumber is not 0, and will otherwise use the fileNumber value,
	 * this to allow backwards compatability between the treaded and untreaded versions of this program
	 * @param number the fileNumber to process
	 */
	public FileAccess(int number){
		FileAccess.setFileNumber(number);
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
	public static BufferedImage getImage(String FileNumber){
		BufferedImage image = null;
		try
		{
			String temp = fileNameInput + FileNumber + ".jpg";
			image = ImageIO.read(new File(temp));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.exit(1);
		}
		return image;
	}

	/**
	 * Writes an imageFile in the OutputImages folder called plate01.png where 01 is the platenumber
	 * @param buffer the bufferImage to be written to
	 */
	public static void writeImage(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = fileNameOutput + FileNumber + ".png";
		File f = new File(temp);
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

	/**
	 * Will write an image in the folder ./OutputImages/AfterHistogram/
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
	public static void writeAfterHistogram(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/AfterHistogram/plate" + FileNumber + ".png";
		File f = new File(temp);
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

	/**
	 * Will write an image in the folder ./OutputImages/FirstColorFilter/
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
	public static void writeFirstColorFilter(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/FirstColorFilter/plate" + FileNumber + ".png";
		File f = new File(temp);
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

	public static void writeBlackColorFilter(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/BlackColorFilter/plate" + FileNumber + ".png";
		File f = new File(temp);
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
	public static void writeBlobDetection(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/BlobDetection/plate" + FileNumber + ".png";
		File f = new File(temp);
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

	/**
	 * Will write an image in the folder ./OutputImages/NumberPlateLocation/
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
	public static void writeOnlyPlate(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/OnlyPlate/plate" + FileNumber + ".png";
		File f = new File(temp);
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

	public static String getFileNumber(int number){
		String temp;
		if (number<10){
			temp = "0"  + String.valueOf(number);
		}else{
			temp = String.valueOf(number);
		}
		return temp;
	}

	public static void setFileNumber(int number){
		if (number<10){
			fileNumber = "0"  + String.valueOf(number);
		}else{
			fileNumber = String.valueOf(number);
		}
	}
}








