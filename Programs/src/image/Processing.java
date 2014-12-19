package image;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Runner;

/**
 * This class is meant to do all the filtering of the image
 * @author Ylva
 *
 */
public class Processing {

	/**
	 * This determines if there will be a delay or not (this delay can help visualize what is happening)
	 * Also this delay just looks freaking awesome :)
	 */
	private static boolean delay = false;
	/**
	 * This method does the color filtering
	 * @param buffer the image to process
	 * @return the image after color filtering it
	 */
	private static BufferedImage ColorFilter(BufferedImage buffer){
		Runner.menuOutput.append("Applying color filter\n");
		int x = 0, y = 0;

		int xMax = buffer.getWidth();
		int yMax = buffer.getHeight();
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
			}
			if (delay){
				try {
					Thread.sleep(10);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
			try {
				Runner.picLabel_1.setIcon(FileAccess.getImageIcon(buffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			y=0;
		}
		return buffer;
	}

	/**
	 * This method will do all the histogram equalization
	 * @param buffer the image to process
	 * @return the image after histogram equalization is applied
	 */
	private static BufferedImage histogramEqualisation(BufferedImage buffer){
		Runner.menuOutput.append("Applying Histogram Equalisation\n");
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
		x=1;
		while (x<256){
			redCDF[x] = redCDF[x]+redCDF[x-1];
			greenCDF[x] = greenCDF[x]+greenCDF[x-1];
			blueCDF[x] = blueCDF[x]+blueCDF[x-1];
			x++;
		}
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
		System.out.println("Calculated Histogram information\nProcessing date to image\n");
		x=0;
		y=0;
		while (x<M){
			while(y<N){
				Color C = new Color(buffer.getRGB(x, y));
				Color c2 = new Color(redCDF[C.getRed()], greenCDF[C.getGreen()], blueCDF[C.getBlue()]);
				buffer.setRGB(x, y, c2.getRGB());
				y++;
			}

			if (delay){
				try {
					Thread.sleep(10);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}

			try {
				Runner.picLabel_1.setIcon(FileAccess.getImageIcon(buffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			y=0;
		}
		return buffer;
	}

	/**
	 * This method will detect and render blobs.
	 * The largest blobs are colored in the following order:
	 * red, green, yellow, magenta, cyan and orange
	 * @param buffer the image to process
	 * @return the image after blobdetection has been applied, this image will have the 6 largest blobs marked in different colors
	 */
	private static BufferedImage blobDetection(BufferedImage buffer){
		int x, xMax, y, yMax;
		x=0;
		y=0;
		xMax = buffer.getWidth();
		yMax = buffer.getHeight();
		int amountOfBlobs = 1;//this is the next int that has not been used to identify a blob
		Runner.menuOutput.append("Running Blob Detection\n");
		int[][] blobArray = new int[xMax][yMax];
		int[][] connectedBlobs = new int[(xMax*yMax)/8][1000];
		int[] mass = new int[(xMax*yMax)/8];
		while (x<xMax){
			while (y<yMax){
				if (checkPos(x,y,buffer)){//image is part of a blob
					if (x==0&&y==0){//top left corner
						connectedBlobs = updateConnectedBlobs(amountOfBlobs,amountOfBlobs,connectedBlobs,amountOfBlobs);
						blobArray[x][y] = amountOfBlobs;

						amountOfBlobs++;
					}else{
						if (x==0){
							if (blobArray[x][y-1]==0){	//found previously undetected blob on the left side of the screen
								connectedBlobs = updateConnectedBlobs(amountOfBlobs,amountOfBlobs,connectedBlobs,amountOfBlobs);
								blobArray[x][y] = amountOfBlobs;
								amountOfBlobs++;
							}else{//found blob above
								blobArray[x][y] = blobArray[x][y-1];
							}
						}else{//not on the left side of the screen

							if (y==0){//on the top border
								if(blobArray[x-1][y]==0){//spot to the left is not part of a blob
									if (blobArray[x-1][y+1]==0){//spot down and to the left is not part of a blob
										//no connected blob detected
										connectedBlobs = updateConnectedBlobs(amountOfBlobs,amountOfBlobs,connectedBlobs,amountOfBlobs);
										blobArray[x][y] = amountOfBlobs;
										amountOfBlobs++;
									}else{
										//blob found to bottom left, not to left
										blobArray[x][y] = blobArray[x-1][y+1];
									}
								}else{//blob to the left
									if (blobArray[x-1][y+1]==0){//not a blob to the bottom left
										blobArray[x][y] = blobArray[x-1][y];
									}else{
										//blob found to the bottom left
										if (checkConnected(blobArray[x-1][y],blobArray[x-1][y+1],connectedBlobs)){//the 2 blobs are connected
											blobArray[x][y] = blobArray[x-1][y];
										}else{
											//the 2 blobs are not connected
											connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x-1][y+1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
											blobArray[x][y] = blobArray[x-1][y];//getting one of the values, since they are marked as connected it does not matter which
										}
									}
								}
							}else if (y==yMax-1){	//bottom border
								if(blobArray[x-1][y]==0){//directely left is no blob
									if(blobArray[x-1][y-1]==0){//diagonally left top is no blob
										if(blobArray[x][y-1]==0){//directely up is no blob
											//new blob found
											connectedBlobs = updateConnectedBlobs(amountOfBlobs,amountOfBlobs,connectedBlobs,amountOfBlobs);
											blobArray[x][y] = amountOfBlobs;
											amountOfBlobs++;
										}else{
											//only directely up is a blob
											blobArray[x][y] = blobArray[x][y-1];
										}
									}else{
										//diagonally is a blob, left is no blob
										if(blobArray[x][y-1]==0){//only diagonally top left is blob
											blobArray[x][y] = blobArray[x-1][y-1];
										}else{
											//both up and diagonally to the top left are blobs
											if (checkConnected(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs)){
												//blobs are already marked as connected
												blobArray[x][y] = blobArray[x-1][y-1];
											}else{
												//blobs are not marked as connected
												connectedBlobs = updateConnectedBlobs(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
												blobArray[x][y] = blobArray[x-1][y-1];//getting one of the values, since they are marked as connected it does not matter which
											}
										}
									}
								}else{
									//directely to the left is a blob
									if(blobArray[x-1][y-1]==0){//diagonally is no blob
										if(blobArray[x][y-1]==0){//directely up is no blob
											blobArray[x][y] = blobArray[x-1][y];//setting the current value to the one of the only connected blob
										}else{
											//both directely left is a blob and directely up is a blob
											if (checkConnected(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs)){
												//blobs are already marked as connected
												blobArray[x][y] = blobArray[x][y-1];
											}else{
												//blobs are not marked as connected
												connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
												blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
											}
										}
									}else{
										//directely to the left is a blob, diagonally top left is also a blob
										if(blobArray[x][y-1]==0){//directely up is no blob
											//directely to the left is a blob, diagonally top left is also a blob, up is no blob
											if (checkConnected(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs)){
												//blobs are already marked as connected
												blobArray[x][y] = blobArray[x][y-1];
											}else{
												//blobs are not marked as connected
												connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
												blobArray[x][y] = blobArray[x-1][y-1];//getting one of the values, since they are marked as connected it does not matter which
											}
										}else{
											//all 3 are blobs
											if (checkConnected(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs)){//the blobs to the left and diagonal are connected			*
												if (checkConnected(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs)){//the blobs to the diagonal and up are connected			*
													if (checkConnected(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs)){//all the blobs are connected							*
														blobArray[x][y] = blobArray[x-1][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}else{
														//blobs to the left and diagonal are connected, as are diagonal and top, however left and top are not yet connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}
												}else{
													//diagonal and up are not yet connected, left and diagonal are
													if (checkConnected(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs)){//checking left and up											*
														//only diagonal and up are not connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the 2 blobs as being connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}else{
														//only diagonal and left are marked connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the diagonal and up as connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the left and up as connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}
												}
											}else{
												//left and diagonal are not connected
												if (checkConnected(blobArray[x-1][y-1],blobArray[x-1][y],connectedBlobs)){//checking diagonal and up										*

													if (checkConnected(blobArray[x][y-1],blobArray[x-1][y],connectedBlobs)){//checking left and up											*
														//only left and diagonal are not connected
														connectedBlobs = updateConnectedBlobs(blobArray[x][y-1],blobArray[x-1][y-1],connectedBlobs,amountOfBlobs);
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}else{
														//left and diagonal are not connected, left and up are not connected, diagonal and up are connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs,amountOfBlobs);//marking the left and diagonal as connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the left and up as connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}
												}else{
													//left and diagonal are not connected, diagonal and up are also not connected
													if (checkConnected(blobArray[x][y-1],blobArray[x-1][y],connectedBlobs)){//checking if left and up are connected
														//only left and up are connected, left and diagonal and diagonal and up are not connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs,amountOfBlobs);//marking the left and diagonal as connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the diagonal and up as connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}else{
														//none of the 3 blobs are connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x-1][y-1],connectedBlobs,amountOfBlobs);//marking the left and diagonal as connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y-1],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the diagonal and up as connected
														connectedBlobs = updateConnectedBlobs(blobArray[x-1][y],blobArray[x][y-1],connectedBlobs,amountOfBlobs);//marking the left and up as connected
														blobArray[x][y] = blobArray[x][y-1];//getting one of the values, since they are marked as connected it does not matter which
													}
												}
											}
										}
									}
								}
							}else{//not any border
								int dUp = blobArray[x-1][y-1];//the value at the diagonal to the top left
								int left = blobArray[x-1][y];//the value to the left
								int up = blobArray[x][y-1];//the value up
								int dDown = blobArray[x-1][y+1];//the value at the diagonal to the left and down

								if (up==0){//up is no blob
									if (dUp==0){//dup is no blob
										if (left==0){//left is no blob
											if (dDown==0){//no blobs found anywhere
												connectedBlobs = updateConnectedBlobs(amountOfBlobs,amountOfBlobs,connectedBlobs,amountOfBlobs);
												blobArray[x][y] = amountOfBlobs;
												amountOfBlobs++;
											}else{
												//only to the diagonal down is there a blob
												blobArray[x][y]=dDown;
											}
										}else{
											//left is a blob
											if (dDown==0){
												//only left is a blob
												blobArray[x][y] = left;
											}else{
												//left and dDwon are blobs
												if (checkConnected(left,dDown, connectedBlobs)){//blobs are already marked as connected
													blobArray[x][y]= left;//setting one of the values, since they are connected it does not matter
												}else{
													//blobs have not been marked as connected
													connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);//marking left and dDown as connected
													blobArray[x][y]= left;//setting one of the values, since they are connected it does not matter
												}
											}
										}
									}else{
										//up is no blob dUp is a blob
										if (left==0){//left is not a blob
											if (dDown==0){//dDwon is not a blob
												//only dUp is a blob
												blobArray[x][y] = dUp;
											}else{
												//dUp and dDown are blobs
												if (checkConnected(dUp,dDown,connectedBlobs)){
													//blobs are connected
													blobArray[x][y] = dUp;
												}else{
													//blobs are not yet connected
													connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
													blobArray[x][y] = dUp;
												}
											}
										}else{
											//up is no blob, dUp is a blob, left is a blob
											if (dDown==0){//dDown is not a blob
												//dUp and left are blobs, up and dDown are not blobs
												if (checkConnected(dUp,left, connectedBlobs)){
													//blobs are already connected
													blobArray[x][y] = dUp;
												}else{
													//blobs are not yet connected
													connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
													blobArray[x][y] = dUp;
												}
											}else{
												//up is no blob, dUp left and dDown are blobs
												if (checkConnected(dUp,left, connectedBlobs)){//dup and left are connected
													if (checkConnected(dUp,dDown, connectedBlobs)){//dup and ddown are connected
														if (checkConnected(dDown,left, connectedBlobs)){//all are connected
															blobArray[x][y] = dUp;
														}else{
															//only dDown and left are not yet connected
															connectedBlobs = updateConnectedBlobs(dDown,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}
													}else{
														//dUp and left are connected, dUp and dDown are not connected
														if (checkConnected(dDown,left, connectedBlobs)){//only dUp and dDown are not connected
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}else{
															//only dUp and left are connected
															connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}
													}
												}else{
													//dUp and left are not connected
													if (checkConnected(dUp,dDown,connectedBlobs)){
														//dUp and dDown are connected
														if (checkConnected(left, dDown,connectedBlobs)){
															//only dUp and left are not connected, dUp and dDown aswell as left and dDown are connected
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}else{
															//dUp and left and dDown and left are not connected, dUp and dDown are connected
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dDown,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}
													}else{
														//dUp and left are not connected, dUp and dDown are not connected
														if (checkConnected(left,dDown,connectedBlobs)){
															//only left and dDown are connected
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}else{
															//none of the 3 are connected
															connectedBlobs = updateConnectedBlobs(dDown,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = dUp;
														}
													}
												}
											}
										}
									}
								}else{
									//up is a blob
									//I am glad that a computer can make quicker sense out of this if else statement than me.....
									if (dUp==0){//up is a blob, dUp is not a blob
										if (left==0){//up is a blob,dUp and left are not blobs
											if (dDown==0){//only up is a blob
												blobArray[x][y] = up;
											}else{//up and dDown are blobs
												if (checkConnected(up,dDown,connectedBlobs)){
													//blobs are already connected
													blobArray[x][y] = up;
												}else{
													connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
													blobArray[x][y] = up;
												}
											}
										}else{//up and left are blobs, dUp is not a blob
											if (dDown==0){//only up and left are blobs
												if (checkConnected(up,left,connectedBlobs)){//up and left are connected
													blobArray[x][y] = up;
												}else{
													connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
													blobArray[x][y] = up;
												}
											}else{//up left and dDown are blobs
												if (checkConnected(up,left,connectedBlobs)){//up and left are connected
													if (checkConnected(up,dDown,connectedBlobs)){//up and left aswell as up and dDown are connected
														if (checkConnected(left,dDown,connectedBlobs)){//all are connected
															blobArray[x][y] = up;
														}else{//only left and dDown is not connected
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}else{//up and left are connected, up and dDown are not
														if (checkConnected(left,dDown,connectedBlobs)){//only up and dDown are not connected
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//both up and dDown and left and dDown are not connected
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}
												}else{//up and left are not connected
													if (checkConnected(up,dDown,connectedBlobs)){//up and left are not connected, up and dDown are
														if (checkConnected(left,dDown,connectedBlobs)){//only up and left are not connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//up and left aswell as left and dDown are not connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}else{//up and left and up and dDown are not connected
														if (checkConnected(left,dDown,connectedBlobs)){//only left and dDown are connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//none of the combinations between up left and dDown are connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}
												}
											}
										}
									}else{//up and dUp are blobs
										if (left==0){//up and dUp are blobs, left is not a blob
											if (dDown==0){//up and dUp are blobs, left and dDown are not blobs
												if (checkConnected(up,dUp,connectedBlobs)){//up and dUp are connected
													blobArray[x][y] = up;
												}else{//up and dUp are not connected
													connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
													blobArray[x][y] = up;
												}
											}else{//up, dUp, dDown are blobs
												if (checkConnected(up,dUp,connectedBlobs)){//up and dUp are connected
													if (checkConnected(up,dDown,connectedBlobs)){//up and dUp aswell as up and dDown are connected, dUp and dDown still unclear
														if (checkConnected(dUp,dDown,connectedBlobs)){//all are connected
															blobArray[x][y] = up;
														}else{//up and dUp aswell as up and dDown are connected, dUp and dDown are not connected
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}else{//up and dUp are connected, up and dDown are not connected, dUp and dDown is unclear
														if (checkConnected(dUp,dDown,connectedBlobs)){//up and dUp aswell as dUp and dDown are connected, up and dDown are not connected
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//up and dUp are connected, dUp and dDown aswell as up and dDown are not connected
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}
												}else{//so far only up and dUp are not connected, up and dDown and dUp and dDown still need to be checked
													if (checkConnected(up,dDown,connectedBlobs)){//up and dUp are not connected, up and dDown are connected, dUp and dDown is not yet checked
														if (checkConnected(dUp,dDown,connectedBlobs)){//up and dUp is not connected, the rest is connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//up and dUp aswell as dUp and dDown are not connected, up and dDown is connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}else{//up and dUp aswell as up and dDown are not connected, dUp and dDown is not yet checked
														if (checkConnected(dUp,dDown,connectedBlobs)){//up and dUp aswell as up and dDown are not connected, dUp and dDown is connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//up and dUp aswell as up and dDown aswell as dUp and dDown are not connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}
												}
											}
										}else{//up, dUp and left are all blobs, dDown is not yet checked
											//seriously this is an enormous amount of typing... sigh
											if (dDown==0){//up,dUp and left are blobs, dDown is not a blob
												boolean up_dUp = checkConnected(up,dUp,connectedBlobs);
												boolean up_left = checkConnected(up,left,connectedBlobs);
												boolean dUp_left = checkConnected(dUp,left,connectedBlobs);
												if (up_dUp){//up_dUp is connected
													if(up_left){//up_dUp and up_left are connected
														if (dUp_left){//all are connected
															blobArray[x][y] = up;
														}else{//only dUp_left is not connected
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;															
														}
													}else{//up_dUp is connected, up_left is not connected
														if (dUp_left){//only up_left is not connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;							
														}else{//both up_left and dUp_left are not connected
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;	
														}
													}
												}else{//up_dUp is not connected
													if (up_left){//up_dUp is not connected, up_left is connected, dUp_left is not checked yet
														if (dUp_left){//up_dUp is not connected, the rest is
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//up_dUp and dUp_left are not connected,up_left is connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}else{//up_dUp and up_left are not connected, dUp_left is not checked yet
														if (dUp_left){//only dUp_left is connected, up_dUp and up_left are not connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}else{//neither up_dUp nor up_left nor dUp_left are connected
															connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
															connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
															blobArray[x][y] = up;
														}
													}
												}


											}else{//all are blobs


												boolean up_dUp = checkConnected(up,dUp,connectedBlobs);
												boolean up_left = checkConnected(up,left,connectedBlobs);
												boolean up_dDown =checkConnected(up,dDown,connectedBlobs);
												boolean dUp_left = checkConnected(dUp,left,connectedBlobs);
												boolean dUp_dDown = checkConnected(dUp,dDown,connectedBlobs);
												boolean left_dDown = checkConnected(left,dDown,connectedBlobs);
												//wait a second, I don't need to nest these statements...
												if (!up_dUp){
													connectedBlobs = updateConnectedBlobs(up,dUp,connectedBlobs,amountOfBlobs);
												}
												if (!up_left){
													connectedBlobs = updateConnectedBlobs(up,left,connectedBlobs,amountOfBlobs);
												}
												if (!up_dDown){
													connectedBlobs = updateConnectedBlobs(up,dDown,connectedBlobs,amountOfBlobs);
												}
												if (!dUp_left){
													connectedBlobs = updateConnectedBlobs(dUp,left,connectedBlobs,amountOfBlobs);
												}
												if (!dUp_dDown){
													connectedBlobs = updateConnectedBlobs(dUp,dDown,connectedBlobs,amountOfBlobs);
												}
												if (!left_dDown){
													connectedBlobs = updateConnectedBlobs(left,dDown,connectedBlobs,amountOfBlobs);
												}
												blobArray[x][y] = up;
											}
										}
									}
								}
							}
						}
					}
				}
				if (blobArray[x][y]!=0){
					mass[blobArray[x][y]]++;//increase the mass of this blob
				}
				y++;
			}
			y=0;
			x++;
		}
		connectedBlobs = Processing.findLowestBlobRecursive(connectedBlobs, amountOfBlobs);
		mass = calculateConnectedMass(connectedBlobs, mass);
		int[] largestBlobs = findLargestBlobs(mass,amountOfBlobs);
		Runner.menuOutput.append("Processing blob information to screen:\n");
		x = 0;
		y = 0;
		while (x<xMax){
			while (y<yMax){
				if (checkPos(x,y,buffer)){
					int baseBlob = connectedBlobs[blobArray[x][y]][0];//get base blob
					if (baseBlob == largestBlobs[0]){//this blob is the largest blob
						buffer.setRGB(x, y, Color.RED.getRGB());
					}else if (baseBlob == largestBlobs[1]){//this blob is part of the second largest blob
						buffer.setRGB(x, y, Color.GREEN.getRGB());
					}else if (baseBlob == largestBlobs[2]){//this blob is part of the third largest blob
						buffer.setRGB(x, y, Color.YELLOW.getRGB());
					}else if (baseBlob == largestBlobs[3]){//this blob is part of the fourth largest blob
						buffer.setRGB(x, y, Color.MAGENTA.getRGB());
					}else if (baseBlob == largestBlobs[4]){//this blob is part of the fifth blob
						buffer.setRGB(x, y, Color.CYAN.getRGB());
					}else if (baseBlob == largestBlobs[5]){//this blob is part of the sixth blob
						buffer.setRGB(x, y, Color.ORANGE.getRGB());
					}
				}
				y++;
			}
			
			if (delay){
				try {
					Thread.sleep(10);                 //1000 milliseconds is one second.
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
			
			try {
				Runner.picLabel_1.setIcon(FileAccess.getImageIcon(buffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			x++;
			y = 0;
		}
		return buffer;

	}

	/**
	 * Will make sure that the value for any blob in the connectedBlobs Array[][0] is the absolute lowest value that that blob is connected to
	 * @param connectedBlobs the array to process
	 * @param amountOfBlobs the total amount of blobs
	 * @return the updated connectedBlobs array
	 */
	private static int[][] findLowestBlobRecursive(int [][] connectedBlobs, int amountOfBlobs){
		int i = 0;
		//Now the program needs to calculate the largest mass, in order to do that the connectBlobsArray needs to be further refined
		while (i<amountOfBlobs){
			if (connectedBlobs[i][0]>=i){//only run if the current blob has not been processed
				int [] currentBlobArray = new int[amountOfBlobs];//this will contain all the numbers that have been found for a certain blob
				currentBlobArray[0]=i;

				int j = 0;
				while (currentBlobArray[j]!=0){
					currentBlobArray = calculateTempArray(currentBlobArray,currentBlobArray[j],connectedBlobs);
					j++;
				}
				connectedBlobs = processTempArray(connectedBlobs,currentBlobArray,i);

			}
			i++;
		}
		return connectedBlobs;

	}

	/**
	 * Will update currentBlobArray with any values that it finds in connectedBlobs[j] that are not yet in currentBlobArray
	 * @param currentBlobArray the array that holds all the blob numbers that are part of the current blob
	 * @param j the number in connectedBlobs from which all the connectedBlobs need to be checked
	 * @param connectedBlobs the array that holds all the values for connectedBlobs
	 * @return the updated currentBlobArray
	 */
	private static int[] calculateTempArray(int[] currentBlobArray, int j, int[][] connectedBlobs){
		int i1 = 0;
		while (connectedBlobs[j][i1]!=0){
			int temp = connectedBlobs[j][i1];//will hold the blobnumber that is next part of the blob
			int i2 = 0;
			boolean found = false;
			while (currentBlobArray[i2]!=0){
				if (currentBlobArray[i2]==temp){
					//blob has already been found in the tempArray
					found = true;
				}
				i2++;
			}
			if (!found){
				currentBlobArray[i2] = temp;//place the value in the temp array
			}
			i2 = 0;
			i1++;
		}
		return currentBlobArray;
	}

	/**
	 * Processes the given array with the given int
	 * Will make sure that all the blobs that are in currentBlobArray have i as their value in the connectedBlobsarray
	 * @param connectedBlobs the array to process in
	 * @param currentBlobArray the array to process
	 * @param i the number to process
	 * @return the updated connectedBlobsArray
	 */
	private static int[][] processTempArray(int[][] connectedBlobs, int[] currentBlobArray, int i){
		int count = 0;
		while (currentBlobArray[count]!=0){
			int number = currentBlobArray[count];
			if (connectedBlobs[number][0]>i){
				connectedBlobs[number][0] = i;
			}
			count++;
		}
		return connectedBlobs;
	}

	/**
	 * Will calculate the masses of all the connected blobs, setting the mass of the lowest blob number (base blob) to the total mass of all the connected blobs combined
	 * This will also set the mass of the all the blobs connected to this blob to be 0, i.e. only the base blob will have the mass in it.
	 * 
	 * @param connectedBlobs the array with all the connections between blobs in it
	 * @param mass the mass array with all the masses of the individual blobs in it
	 * @return the updated mass array
	 */
	private static int[] calculateConnectedMass(int[][] connectedBlobs,int[] mass){
		int i =1;
		while (connectedBlobs[i][0]!=0){//end of blob array
			if (connectedBlobs[i][0]!=i){//means that the blob is part of a larger blob
				mass[connectedBlobs[i][0]] += mass[i];//add the mass of this blob to the base blob
				mass[i] = 0;//set the mass of this blob to be 0
			}else{
				//means that this is a base blob
			}
			i++;
		}
		return mass;
	}


	/**
	 * will calculated an array of length 6 which the 6 largest blobs in them
	 * @param mass the mass array that holds the masses of all the blobs, should already have been updated in {@link #calculateConnectedMass(int[][], int[]) calculateConnectedMass} method
	 * @return an array with the 6th largest blobs where the [0] it the largest
	 */
	private static int[] findLargestBlobs(int[] mass,int amountOfBlobs){
		int i=1;
		int[] largestBlobs = new int[6];
		while (i<amountOfBlobs){
			if (mass[i]!=0){//means that this is a base blob
				if (mass[i]>mass[largestBlobs[5]]){//if its larger then the now 6th largest blob then it needs to be in the array
					if (mass[i]>mass[largestBlobs[4]]){

						largestBlobs[5] =largestBlobs[4];//we can already move this blobnumber to the next place
						if (mass[i]>mass[largestBlobs[3]]){

							largestBlobs[4] =largestBlobs[3];//we can already move this blobnumber to the next place
							if (mass[i]>mass[largestBlobs[2]]){

								largestBlobs[3] =largestBlobs[2];//we can already move this blobnumber to the next place
								if (mass[i]>mass[largestBlobs[1]]){

									largestBlobs[2] =largestBlobs[1];//we can already move this blobnumber to the next place
									if (mass[i]>mass[largestBlobs[0]]){
										largestBlobs[1] =largestBlobs[0];
										largestBlobs[0] = i;//save current blob number as the largest blob
									}else{//means this blob should be at spot 1
										largestBlobs[1] = i;//save current blob number as the second largest blob
									}
								}else{//means this blob should be at spot 2
									largestBlobs[2] = i;//save current blob number as the third largest blob
								}
							}else{//means this blob should be at spot 3
								largestBlobs[3] = i;//save current blob number as the fourth largest blob
							}
						}else{//means this blob should be at spot 4
							largestBlobs[4] = i;//save current blob number as the fifth largest blob
						}
					}else{//means this blob should be at spot 5
						largestBlobs[5] = i;
					}
				}
			}
			i++;
		}
		return largestBlobs;
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

	/**
	 * Will make sure that in the connected array there is a link between the 2 blobs
	 * @param one the first variable to update
	 * @param two the second variable to update
	 * @param connected the array with all the connected values
	 * @return the updated array
	 */
	private static int[][] updateConnectedBlobs(int one, int two, int[][] connected, int nextCount){

		int i = 0;
		while (i<=nextCount){
			if (connected[one][i]==two){//blobs have already been marked as connected

				return connected;
			}else{
			}
			if (connected[one][i]==0){//found first empty place in the array 
				connected[one][i] = two;
				break;
			}
			i++;
		}
		i = 0;
		while (i<=nextCount){
			if (connected[two][i]==0){//found first empty place in the array 
				connected[two][i] = one;
				break;
			}
			i++;
		}
		return connected;
	}
	/**
	 * This method will check if the 2 blobnumbers are already connected.
	 * @param one the first variable to check for
	 * @param two the second variable to check for
	 * @param connected the connected array
	 * @return true if the blobs are connected, false otherwise
	 */
	private static boolean checkConnected(int one, int two, int[][] connected){
		int i =0;
		while (connected[one][i]!=0){
			if (connected[one][i]==two){
				return true;
			}
			i++;
		}
		return false;
	}
	
	/**
	 * Will find the minimum and maximum coordinates of the blob that has the color specified
	 * @param buffer the image to check in
	 * @param color the color for which to return the minimum coordinates
	 * @return an integer array with the following things in it: xMin, yMin, xMax, yMax
	 */
	private static int[] findMinMaxOfBlob(BufferedImage buffer, Color color){
		int x = 0, xMin = buffer.getWidth(), xMax = 0, y = 0, yMin = buffer.getHeight(), yMax = 0;
		while (x<buffer.getWidth()){
			while (y<buffer.getHeight()){
				if (buffer.getRGB(x, y)==color.getRGB()){
					if (x<xMin){
						xMin = x;
					}
					if (x>xMax){
						xMax = x;
					}
					
					if (y<yMin){
						yMin = y;
					}
					if (y>yMax){
						yMax = y;
					}
				}
				y++;
			}
			y=0;
			x++;
		}
		int[] returnArray = new int[4];
		returnArray[0] =xMin;
		returnArray[1] =yMin;
		returnArray[2] =xMax;
		returnArray[3] =yMax;
		return returnArray;
	}
	
	/**
	 * Method designed to find the numberplate and return only that portion of the original picture
	 * This method will do the steps from histogram equalization, through color filter and onto blobdetection and then use those results to return an image with only the numberplate in it
	 * @param original the image to process 
	 */
	public static void findNumberPlate(BufferedImage original){
		BufferedImage buffer = Processing.copyImage(original);//get the image twice in order to be able to process it and then use the results in the original image
		buffer = Processing.histogramEqualisation(buffer);
		FileAccess.writeAfterHistogram(buffer);
		buffer = Processing.ColorFilter(buffer);
		FileAccess.writeFirstColorFilter(buffer);
		buffer = Processing.blobDetection(buffer);
		FileAccess.writeBlobDetection(buffer);
		int[] coords = Processing.findMinMaxOfBlob(buffer, Color.RED);
		int x = 0, y = 0;
		System.out.println("xmin\t" + coords[0]);
		System.out.println("ymin\t" + coords[1]);
		System.out.println("xmax\t" + coords[2]);
		System.out.println("ymax\t" + coords[3]);
		original = Processing.cutimage(original, coords[0], coords[1], (coords[2]-coords[0]), (coords[3]-coords[1]));
		try {
			Runner.picLabel_1.setIcon(FileAccess.getImageIcon(original));
		} catch (Exception e) {
			e.printStackTrace();
		}
		FileAccess.writeOnlyPlate(original);
		
	}
	
	/**
	 * This method is in order to get a second bufferedImage, this because just making a new bufferedImage and doing new = old then the new one will actually just point to the old one instead of making a complete new object.
	 * This method will first make a new BuferredImage type with the same settings as the old image and will then paint the old image on it (bassically)
	 * @param source the image to copy
	 * @return a new bufferedImage which is the same as the source
	 */
	private static BufferedImage copyImage(BufferedImage source){
	    BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
	    Graphics g = b.getGraphics();
	    g.drawImage(source, 0, 0, null);
	    g.dispose();
	    return b;
	}
	
	/**
	 * Cuts an image out of the original image and returns that
	 * @param source the original image from which to cut (segment)
	 * @param x the X coordinate to start
	 * @param y the Y coordinate to start
	 * @param width the width of the image to cut from the original
	 * @param height the height of the image to cut from the original
	 * @return a new image which has bee cut from the old image
	 */
	public static BufferedImage cutimage(BufferedImage source, int x, int y, int width, int height){
		BufferedImage b = source.getSubimage(x, y, width, height);
		return b;
	}
	
}
