package image;

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
	public void run(){
		int location = this.increment();
		System.out.println(location);
		//AltUI.numbers[location] = Integer.parseInt(Thread.currentThread().getName());
		//FileAccess files = new FileAccess(Integer.parseInt(Thread.currentThread().getName()));
		BufferedImage currentBuffer = FileAccess.getImage(FileAccess.getFileNumber(Integer.parseInt(Thread.currentThread().getName())));
		try {
			AltUI.updateScreen(Integer.parseInt(Thread.currentThread().getName()), Processing.ScaleThreadIcon(currentBuffer));
		} catch (Exception e) {
			e.printStackTrace();
		}
		BufferedImage procesBuffer = Processing.copyImage(currentBuffer);
		procesBuffer = Processing.histogramEqualisation(procesBuffer, String.valueOf(location));
		/*try {
			synchronized (AltUI.proc1) {
				AltUI.proc1.wait();
			}
			//AltUI.proc1.wait();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		/*while (!AltUI.continue1){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
		procesBuffer = Processing.colorFilter(procesBuffer, String.valueOf(location));
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
}
