package image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;

import main.AltUI;

public class ThreadedProcessing extends Thread {
	public static AtomicInteger count;

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
	/**
	 * This method should hold the code that needs to be runned in this tread
	 */
	public static ThreadLocal<Boolean> realTime = new ThreadLocal<Boolean>() {

	    @Override
	    protected Boolean initialValue() {
	        return Boolean.FALSE;
	    }

	};
	public void run(){
		
		int location = this.increment();
		System.out.println(location);
		
		/*if(location%2==0){
		ThreadedProcessing.realTime.set(true);
		}*/
		ThreadedProcessing.realTime.set(false);
		//AltUI.numbers[location] = Integer.parseInt(Thread.currentThread().getName());
		//FileAccess files = new FileAccess(Integer.parseInt(Thread.currentThread().getName()));
		BufferedImage currentBuffer = FileAccess.getImage(FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		try {
			AltUI.updateScreen(location, Processing.ScaleThreadIcon(currentBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedImage procesBuffer = Processing.copyImage(currentBuffer);
		procesBuffer = Processing.histogramEqualisation(procesBuffer, String.valueOf(location));
		if(!AltUI.notWait){
		ThreadedProcessing.wait(location);
		}
		/*while (!AltUI.continue1){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		procesBuffer = Processing.colorFilter(procesBuffer, String.valueOf(location));
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
			}
		procesBuffer = Processing.blobDetection(procesBuffer, String.valueOf(location));
		if(!AltUI.notWait){
			ThreadedProcessing.wait(location);
			}
		int[] coords = Processing.findMinMaxOfBlob(procesBuffer, Color.RED);
		currentBuffer = Processing.cutimage(currentBuffer, coords[0], coords[1], (coords[2]-coords[0]), (coords[3]-coords[1]));
		try {
			AltUI.updateScreen(location, Processing.ScaleThreadIcon(currentBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		currentBuffer = Processing.blackFilter(currentBuffer, String.valueOf(location));
		currentBuffer = Processing.blobDetection(currentBuffer, String.valueOf(location));
		Processing.segMent(currentBuffer);
		//System.out.println("Currentely showing the first image...");
		/*int i = 0;
		while (i<10){
			System.out.println("Thread " + this.getName() +" is at " + i);
			i++;
			try {
				ThreadedProcessing.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	}

	public synchronized int increment() {
		if(count==null){
			count = new AtomicInteger(0);
		}
		return count.incrementAndGet();

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
