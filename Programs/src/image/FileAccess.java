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
	private static String folderNameTemplates = "./Templates/";
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

	/**
	 * Will write an image in the folder ./OutputImages/BlobDetection/plate
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
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
	
	/**
	 * Will write an image in the folder ./OutputImages/BlobDetection/plate
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
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

	/**
	 * Will return a correct string equivalent of the given number
	 * @param number The number to get the String from
	 * @return The correct String version of this number with a "0" padding if number<10
	 */
	public static String getFileNumber(int number){
		String temp;
		if (number<10){
			temp = "0"  + String.valueOf(number);
		}else{
			temp = String.valueOf(number);
		}
		return temp;
	}

	
	/**
	 * Will write the output of the 6 segments to files
	 * @param buffer An array holding the segments
	 * @param FileNumber The number of the plate that the segments come from
	 */
	public static void writeSegmented(BufferedImage[] buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		for(int i = 0; i < buffer.length; i++){
			String temp = "./OutputImages/Segmented/plate" + FileNumber +"-" + i +  ".png";
			File f = new File(temp);
			ImageOutputStream ios = null;
			try {
				ios = ImageIO.createImageOutputStream(f);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			writer.setOutput(ios);
			try {
				writer.write(buffer[i]);
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
	
	/**
	 * This method should return an array with all bufferedImages of the templates
	 * @return An array with all the templates in it
	 */
	public static BufferedImage[] getTemplates(){
		BufferedImage[] templates = new BufferedImage[36];
		int i =0;
		while (i<36){
			if(i<10){
				try
				{
					String temp = folderNameTemplates + (char)(i+48) + ".png";
					templates[i] = ImageIO.read(new File(temp));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}else{
				try
				{
					String temp = folderNameTemplates + (char)(i+55) + ".png";
					
					templates[i] = ImageIO.read(new File(temp));
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			i++;
		}
		return templates;
	}
	
	
	/**
	 * Will write an image in the folder ./OutputImages/PlateBlobDetection/plate
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
	public static void writePlateBlobDetection(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/PlateBlobDetection/plate" + FileNumber + ".png";
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
	 * Will write an image in the folder ./OutputImages/PlateBlackFilter/plate
	 * The image will be named "plate01.png" for the first plate
	 * the 01 is the platenumber, can be 01 till any number as long as there are input images of that plate number
	 * @param buffer the image to write
	 */
	public static void writePlateBlackColorFilter(BufferedImage buffer, String FileNumber){
		Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("png");
		ImageWriter writer = (ImageWriter)writers.next();
		String temp = "./OutputImages/PlateBlackFilter/plate" + FileNumber + ".png";
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
}








