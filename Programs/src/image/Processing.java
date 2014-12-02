package image;

import java.awt.Color;
import java.awt.image.BufferedImage;

import main.Runner;

/**
 * This class is meant to do all the filtering of the image
 * @author Ylva
 *
 */
public class Processing {
	
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
				if (red>160&&(green>198||(green<160&&green>140)||(green<105&&green>80))&&blue<190){
					buffer.setRGB(x, y, Color.white.getRGB());
				}else{
					buffer.setRGB(x, y, Color.black.getRGB());
				}
				y++;
			}/*
			try {
			    Thread.sleep(5);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}*/
			try {
				Runner.picLabel.setIcon(FileAccess.getImageIcon(buffer));
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
		Runner.menuOutput.append("Counted all instances, calculating CDF\n");
		x=1;
		while (x<256){
			redCDF[x] = redCDF[x]+redCDF[x-1];
			greenCDF[x] = greenCDF[x]+greenCDF[x-1];
			blueCDF[x] = blueCDF[x]+blueCDF[x-1];
			x++;
		}
		Runner.menuOutput.append("Calculated CDF (unscaled)\n");
		Runner.menuOutput.append("Calcualting values for formula to scale CDF\n");
		int CDFMinRed;
		int CDFMinGreen;
		int CDFMinBlue;
		int i=0;
		
		while (i<256){
			if (redCDF[i]!=0){
				break;
			}
			i++;
		}
		CDFMinRed = redCDF[i];		//lowest nonzero red value
		i=0;
		
		while (i<256){
			if (greenCDF[i]!=0){
				break;
			}
			i++;
		}
		CDFMinGreen = greenCDF[i];
		
		i=0;
		while (i<256){
			if (blueCDF[i]!=0){
				break;
			}
			i++;
		}
		CDFMinBlue = blueCDF[i];
		
		Runner.menuOutput.append("Calculated minimum values for CDF\n");
		i =0;
		while (i<256){
			if (redCDF[i]!=0){
				redCDF[i] = (int) Math.round((((double)redCDF[i]-(double)CDFMinRed)/((double)(M*N)-(double)CDFMinRed)*255));
			}
			if (greenCDF[i]!=0){
				greenCDF[i] = (int) Math.round((((double)greenCDF[i]-(double)CDFMinGreen)/((double)(M*N)-(double)CDFMinGreen)*255));
			}
			if (blueCDF[i]!=0){
				blueCDF[i] = (int) Math.round((((double)blueCDF[i]-(double)CDFMinBlue)/((double)(M*N)-(double)CDFMinBlue)*255));
			}
			i++;
		}
		Runner.menuOutput.append("Scaled cdf, processing image\n");
		

		x=0;
		y=0;
		while (x<M){
			while(y<N){
				Color C = new Color(buffer.getRGB(x, y));
				Color c2 = new Color(redCDF[C.getRed()], greenCDF[C.getGreen()], blueCDF[C.getBlue()]);
				buffer.setRGB(x, y, c2.getRGB());
				y++;
			}
			try {
			    Thread.sleep(5);                 //1000 milliseconds is one second.
			} catch(InterruptedException ex) {
			    Thread.currentThread().interrupt();
			}
			try {
				Runner.picLabel.setIcon(FileAccess.getImageIcon(buffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			y=0;
		}	
	}
	
	/**
	 * This method will detect and render blobs
	 * @param buffer the image to process
	 */
	public static void blobDetection(BufferedImage buffer){
		int x, xMax, y, yMax;
		x=0;
		y=0;
		xMax = buffer.getWidth();
		yMax = buffer.getHeight();
		Runner.menuOutput.append("Starting blobDetection");
		int[][] blobArray = new int[xMax][yMax];
		int[] connectedBlobs = new int[(xMax*yMax)/4];
		while (x<xMax){
			while (y<yMax){
				if (x==0){	//left border
					if (y==0){	//top left corner
						
					}else{
						//check only directely above
					}
				}else {	
					if (y==0){//top row
						
					}else if (y==yMax-1){	//bottom row
						
					}else{	//any middle place
						
					}
				}
				y++;
			}
			y=0;
			x++;
		}
	}
	
	/**
	 * This will check if a certain place is white
	 * @param x the X coordinate to check
	 * @param y the Y coordinate to check
	 * @return true if the value of the coordinates to be checked is 255
	 */
	private static boolean checkPos(int x, int y, BufferedImage buffer){
		Color c = new Color(buffer.getRGB(x, y));
		if (c.getRed()==255&&c.getGreen()==255&&c.getBlue()==255){
			return true;
		}
		return false;
	}
}
