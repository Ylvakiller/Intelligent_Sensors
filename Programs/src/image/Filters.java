package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.Runner;

/**
 * This class is ment to do all the filtering of the image
 * @author Ylva
 *
 */
public class Filters {
	
	/**
	 * This method does the color filtering
	 * @param buffer the image to process
	 */
	public static void ColorFilter(BufferedImage buffer){
		int x = 0, y = 0;

		int xMax = buffer.getWidth();
		int yMax = buffer.getHeight();
		System.out.println("xmax " + xMax);
		Color C;
		
		while (x<xMax){
			while(y<yMax){
				C = new Color(buffer.getRGB(x, y));
				int red = C.getRed();
				int green = C.getGreen();
				int blue = C.getBlue();
				if (red>190&&green<200&&green>140&&blue<100){
					buffer.setRGB(x, y, Color.white.getRGB());
				}else{
					buffer.setRGB(x, y, Color.black.getRGB());
				}
				y++;
			}
			try {
			    Thread.sleep(30);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			try {
				Runner.picLabel.setIcon(Opener.getImageIcon(buffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			y=0;
		}
	}
	
	/**
	 * This method will do all the histogram equalization
	 * @param buffer the image to process
	 */
	public static void histogramEqualisation(BufferedImage buffer){
		Runner.menuOutput.append("Starting Histogram Equalisation\n");
		int CDFminRed;
		int CDFminBlue;
		int CDFminGreen;
		int M = buffer.getWidth();
		int N = buffer.getHeight();
		int L = 256;
		//formula: round(((CDF(v)-CDFmin)/((M*N)-CDFmin))*(L-1))
		/*
		 *The next arrays will first hold counts of the amount a certain value is found, then the CDF and then a scaled CDF
		 */
		int[] redCDF = new int[256];
		int[] greenCDF = new int[256];	
		int[] blueCDF = new int[256];
		int x = 0;
		int y = 0;
		while(x<M){
			while(y<N){
				Color C = new Color(buffer.getRGB(x, y));
				int red = C.getRed();
				int green = C.getGreen();
				int blue = C.getBlue();
				redCDF[red]++;
				greenCDF[green]++;
				blueCDF[blue]++;
				y++;
			}
			y=0;
			x++;
		}

		Runner.menuOutput.append("Counted all instances, calculating CDF");
		
	}
}
