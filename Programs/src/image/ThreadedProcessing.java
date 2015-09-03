package image;

import java.awt.image.BufferedImage;

import main.AltUI;

public class ThreadedProcessing extends Thread {

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
	public void run(){
		//FileAccess files = new FileAccess(Integer.parseInt(Thread.currentThread().getName()));
		BufferedImage currentBuffer = FileAccess.getImage(FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		try {
			AltUI.updateScreen(Integer.parseInt(Thread.currentThread().getName()), Processing.ScaleThreadIcon(currentBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedImage procesBuffer = Processing.copyImage(currentBuffer);
		procesBuffer = Processing.histogramEqualisation(procesBuffer, Thread.currentThread().getName());
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
}
