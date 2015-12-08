package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import main.AltUI;
import main.PlateNotFoundException;
import main.Runner;
import main.SegmentSizeException;

public class ThreadedProcessing extends Thread {
	private static Object waiter = new Object();

	public ThreadedProcessing() {
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(Runnable arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(String arg0) {
		super(arg0);
		// TODO Auto-generated constructor stub
	}
	public ThreadedProcessing(String arg0, int location) {
		super(arg0);
		this.location = location;
		ThreadedProcessing.realTime.set(false);
		// TODO Auto-generated constructor stub
	}
	public ThreadedProcessing(ThreadGroup arg0, Runnable arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(ThreadGroup arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(Runnable arg0, String arg1) {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(ThreadGroup arg0, Runnable arg1, String arg2) {
		super(arg0, arg1, arg2);
		// TODO Auto-generated constructor stub
	}

	public ThreadedProcessing(ThreadGroup arg0, Runnable arg1, String arg2,
			long arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}

	public static ThreadLocal<Boolean> realTime = new ThreadLocal<Boolean>() {

		@Override
		protected Boolean initialValue() {
			return Boolean.FALSE;
		}

	};

	public int location;
	public boolean done = false;
	/**
	 * This method should hold the code that needs to be runned in this tread
	 */
	public void run(){


		BufferedImage currentBuffer = FileAccess.getImage(FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		//System.out.println("Thread started with name " + Thread.currentThread().getName() + " And location " + location);
		
		try {
			AltUI.updateScreen(location, Processing.ScaleThreadIcon(currentBuffer));
			AltUI.setToolTop(location, ("Plate number " + Thread.currentThread().getName()));//Allows the user to easily see what that plate it
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
		}
		BufferedImage procesBuffer = Processing.copyImage(currentBuffer);
		procesBuffer = Processing.histogramEqualisation(procesBuffer, String.valueOf(location));
		FileAccess.writeAfterHistogram(procesBuffer, Thread.currentThread().getName());
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
		}
		BufferedImage extrabuffer = Processing.copyImage(procesBuffer);
		BufferedImage extraOriginal = Processing.copyImage(currentBuffer);
		procesBuffer = Processing.colorFilter(procesBuffer, String.valueOf(location));
		FileAccess.writeFirstColorFilter(procesBuffer, Thread.currentThread().getName());
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
		}
		procesBuffer = Processing.blobDetection(procesBuffer, String.valueOf(location));

		FileAccess.writeBlobDetection(procesBuffer, Thread.currentThread().getName());
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
		}
		int[] coords = Processing.findMinMaxOfBlob(procesBuffer, Color.RED);
		currentBuffer = Processing.cutimage(currentBuffer, coords[0], coords[1], (coords[2]-coords[0]), (coords[3]-coords[1]));
		FileAccess.writeOnlyPlate(currentBuffer, Thread.currentThread().getName());
		try {
			AltUI.updateScreen(location, Processing.ScaleThreadIcon(currentBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if ((float)currentBuffer.getWidth()/(float)currentBuffer.getHeight()<3.5){
			/*try {
				throw new PlateNotFoundException();
			} catch (PlateNotFoundException e) {
				System.out.println("ERROR " +  Thread.currentThread().getName());
				//If this is the case we need to rerun an alternative colorfilter which I have to setup and run through the first part again until we find the numberplate
			}*/
			//I have removed my exceptions from the program since it did not allow to me to continue properly
			extrabuffer = Processing.secondColorFilter(extrabuffer, String.valueOf(location));
			FileAccess.writeFirstColorFilter(extrabuffer, Thread.currentThread().getName());
			if(!AltUI.notWait){
				ThreadedProcessing.wait(location);
			}

			extrabuffer = Processing.blobDetection(extrabuffer, String.valueOf(location));

			FileAccess.writeBlobDetection(extrabuffer, Thread.currentThread().getName());
			if(!AltUI.notWait){
				ThreadedProcessing.wait(location);
			}
			coords = Processing.findMinMaxOfBlob(extrabuffer, Color.RED);
			
			currentBuffer = Processing.cutimage(extraOriginal, coords[0], coords[1], (coords[2]-coords[0]), (coords[3]-coords[1]));
			FileAccess.writeOnlyPlate(currentBuffer, Thread.currentThread().getName());
			try {
				AltUI.updateScreen(location, Processing.ScaleThreadIcon(currentBuffer));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if(!AltUI.notWait){
				ThreadedProcessing.wait(location);
			}
			extrabuffer = Processing.copyImage(currentBuffer);
			currentBuffer = Processing.thirdBlackFilter(currentBuffer, String.valueOf(location));
			FileAccess.writePlateBlackColorFilter(currentBuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
			if(!AltUI.notWait){
				ThreadedProcessing.wait(location);
			}
			
			currentBuffer = Processing.blobDetection(currentBuffer, String.valueOf(location));
		}else{
			extrabuffer = Processing.copyImage(currentBuffer);
			currentBuffer = Processing.blackFilter(currentBuffer, String.valueOf(location));
			FileAccess.writePlateBlackColorFilter(currentBuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
			currentBuffer = Processing.areaSmooth(currentBuffer);
			currentBuffer = Processing.blobDetection(currentBuffer, String.valueOf(location));
		}


		
		FileAccess.writePlateBlobDetection(currentBuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		BufferedImage[] segmented= Processing.segMent(currentBuffer);

		int i = 0;
		while (i<segmented.length){
			segmented[i] = Processing.getMono(segmented[i]);
			i++;
		}
		FileAccess.writeSegmented(segmented, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		BufferedImage[] templates = FileAccess.getTemplates();

		double[][] chars= new double[6][];
		double percentage = 1;

		i=0;
		boolean exception = false;
		while (i<6){


			segmented[i] = Processing.largeAreaSmooth(segmented[i]);
			if (segmented[i]==null){
				exception = true;
				break;
			}

			i++;
		}
		if (exception) {
			exception = false;
			// In here we need to run a different blackfilter
			BufferedImage temp = Processing.copyImage(extrabuffer);
			Runner.updateScreen(extrabuffer,this.location);
			extrabuffer = Processing.secondBlackFilter(extrabuffer, String.valueOf(location));
			FileAccess.writePlateBlackColorFilter(extrabuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));

			extrabuffer = Processing.areaSmooth(extrabuffer);
			extrabuffer = Processing.blobDetection(extrabuffer, String.valueOf(location));
			FileAccess.writePlateBlobDetection(extrabuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
			segmented= Processing.segMent(extrabuffer);

			i = 0;
			while (i<segmented.length){
				segmented[i] = Processing.getMono(segmented[i]);
				i++;
			}
			FileAccess.writeSegmented(segmented, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
			templates = FileAccess.getTemplates();
			//It might be worth looking into a connected algorithm if time allows before running the next step
			chars= new double[6][];
			percentage = 1;
			i=0;
			while (i<6){
				segmented[i] = Processing.largeAreaSmooth(segmented[i]);
				if (segmented[i]==null){
					exception = true;
					break;
				}
				i++;
			}
			if (exception){
				extrabuffer = Processing.copyImage(temp);
				exception = false;
				// In here we need to run a different blackfilter
				Runner.updateScreen(extrabuffer,this.location);
				extrabuffer = Processing.fourthBlackFilter(extrabuffer, String.valueOf(location));
				FileAccess.writePlateBlackColorFilter(extrabuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));

				extrabuffer = Processing.areaSmooth(extrabuffer);
				extrabuffer = Processing.blobDetection(extrabuffer, String.valueOf(location));
				FileAccess.writePlateBlobDetection(extrabuffer, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
				segmented= Processing.segMent(extrabuffer);

				i = 0;
				while (i<segmented.length){
					segmented[i] = Processing.getMono(segmented[i]);
					i++;
				}
				FileAccess.writeSegmented(segmented, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
				templates = FileAccess.getTemplates();
				//It might be worth looking into a connected algorithm if time allows before running the next step
				chars= new double[6][];
				percentage = 1;
				i=0;
				while (i<6){
					segmented[i] = Processing.largeAreaSmooth(segmented[i]);
					if (segmented[i]==null){
						exception = true;
						break;
					}
					i++;
				}
				
			}
		}

		
		FileAccess.writeSegmented(segmented, FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		i=0;
		while (i<6){

			chars[i] = Processing.getChar(segmented[i], templates);
			percentage = percentage*chars[i][1];
			i++;

		}
		String temp = String.valueOf((char)chars[0][0]) + String.valueOf((char)chars[1][0]) + String.valueOf((char)chars[2][0]) + String.valueOf((char)chars[3][0]) + String.valueOf((char)chars[4][0]) + String.valueOf((char)chars[5][0]);
		/*if (Integer.parseInt(Thread.currentThread().getName())==26){
			System.out.println("I am " + Math.round(percentage*100) + "% confident that number plate " + Thread.currentThread().getName() + " is \t" + temp);
			}*/
		//System.out.println("I am " + Math.round(percentage*100) + "%\tconfident that number plate " + Thread.currentThread().getName() + " is \t" + temp);
		if (temp.equalsIgnoreCase(AltUI.correct[Integer.parseInt(Thread.currentThread().getName())-1])){
			System.out.print("I am " + Math.round(percentage*100) + "%\tconfident that number plate " + Thread.currentThread().getName() + " is \t" + temp);
			System.out.println("\tThis is the correct numberplate");
		}else{
			System.out.print("I am " + Math.round(percentage*100) + "%\tconfident that number plate " + Thread.currentThread().getName() + " is \t" + temp);
			System.out.println("\tHowever this is not the correct numberplate");
		}
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
		}
		try {
			Thread.sleep(15);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		done = true;
		synchronized(AltUI.next){
			AltUI.next.notify();;
		}
	}
	private static void wait(int location){
		try {
			switch (location){
			case 1:
				synchronized (AltUI.proc1) {
					AltUI.proc1.wait();
				}
				break;
			case 2:
				synchronized (AltUI.proc2) {
					AltUI.proc2.wait();
				}
				break;
			case 3:
				synchronized (AltUI.proc3) {
					AltUI.proc3.wait();
				}
				break;
			case 4:
				synchronized (AltUI.proc4) {
					AltUI.proc4.wait();
				}
				break;
			default:
				break;
			}
			//AltUI.proc1.wait();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
