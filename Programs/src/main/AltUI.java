package main;

import java.awt.Button;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import image.ThreadedProcessing;

import javax.swing.JToggleButton;

/*
 * This class will hold my attempt at making a multi treaded interface for the program, showing to start 4 numberplate at the same time.
 */
public class AltUI {
	/**
	 * This constructor will start and manage the complete interface
	 */

	private static JLabel picLabel_1;
	private static JLabel picLabel_2;
	private static JLabel picLabel_3;
	private static JLabel picLabel_4;
	public static int[] numbers= {0,0,0,0};
	public static boolean continue1 = false;
	public static ThreadedProcessing proc1;
	public static ThreadedProcessing proc2;
	public static ThreadedProcessing proc3;
	public static ThreadedProcessing proc4;
	public static ThreadedProcessing[] threadArray = new ThreadedProcessing[4];
	public static boolean notWait = false;

	public AltUI() {
		//This is the top container, anything that has directely to do with thise container will be on this indentation level
		JFrame topContainer = new JFrame("Experimental interface for the numberplate recogniser");
		topContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		topContainer.setSize(1920,1080);
		topContainer.getContentPane().setLayout(null);
		{
			// Up next is the side panel, this will include all the buttons and actionlisteners
			JPanel sidePanel = new JPanel();
			sidePanel.setBounds(5, 5, 275, 1030);
			topContainer.getContentPane().add(sidePanel);
			sidePanel.setLayout(null);
			{
				JPanel buttonPanel = new JPanel();
				buttonPanel.setBounds(0, 0, sidePanel.getWidth(), 275);
				buttonPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,null));
				sidePanel.add(buttonPanel);
				buttonPanel.setLayout(null);

				Button btnNextUpLeft = new Button("Next step top left");
				btnNextUpLeft.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						synchronized (AltUI.proc1) {
							AltUI.proc1.notify();
						}
					}
				});
				btnNextUpLeft.setBounds(5, 5, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextUpLeft);

				Button btnNextUpRight = new Button("Next step top right");

				btnNextUpRight.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						synchronized (AltUI.proc2) {
							AltUI.proc2.notify();
						}
					}
				});
				btnNextUpRight.setBounds((buttonPanel.getWidth()-15)/2+10, 5, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextUpRight);

				Button btnNextDownLeft = new Button("Next step lower left");
				btnNextDownLeft.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						synchronized (AltUI.proc3) {
							AltUI.proc3.notify();
						}
					}
				});
				btnNextDownLeft.setBounds(5, 35, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextDownLeft);

				Button btnNextdownRight = new Button("Next step lower right");
				btnNextdownRight.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						synchronized (AltUI.proc4) {
							AltUI.proc4.notify();
						}
					}
				});
				btnNextdownRight.setBounds((buttonPanel.getWidth()-15)/2+10, 35, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextdownRight);

				Button btnNextAll = new Button("Next step all");
				btnNextAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						synchronized(AltUI.proc1){
							AltUI.proc1.notify();
						}
						synchronized(AltUI.proc2){
							AltUI.proc2.notify();
						}
						synchronized(AltUI.proc3){
							AltUI.proc3.notify();
						}
						synchronized(AltUI.proc4){
							AltUI.proc4.notify();
						}
					}
				});
				btnNextAll.setActionCommand("NextStepAll");
				btnNextAll.setBounds(5, 66, 130, 25);
				buttonPanel.add(btnNextAll);

				JToggleButton tglbtnKeepGoingAll = new JToggleButton("Keep Going All");
				tglbtnKeepGoingAll.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						notWait= !notWait;
					}
				});
				tglbtnKeepGoingAll.setBounds(140, 68, 130, 23);
				buttonPanel.add(tglbtnKeepGoingAll);

				JPanel textPanel = new JPanel();
				textPanel.setBounds(0, buttonPanel.getHeight(), sidePanel.getWidth(), sidePanel.getHeight()-buttonPanel.getHeight());
				textPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,null));
				sidePanel.add(textPanel);
				textPanel.setLayout(null);

				JScrollPane scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(3, 3, 269, 749);
				textPanel.add(scrollPane_1);

				final JTextArea textArea = new JTextArea();
				scrollPane_1.setViewportView(textArea);
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(0, 0, textPanel.getWidth(), textPanel.getHeight());
					scrollPane.setAutoscrolls(true);

					OutputStream out = new OutputStream() {
						@Override
						public void write(final int b) throws IOException {
							textArea.append((String.valueOf((char) b)));
						}

						@Override
						public void write(byte[] b, int off, int len) throws IOException {
							textArea.append((new String(b, off, len)));
						}

						@Override
						public void write(byte[] b) throws IOException {
							textArea.append(new String(b, 0, b.length));
						}
					};

					System.setOut(new PrintStream(out, true));
					System.setErr(new PrintStream(out, true));

				}
			}

			//This is a container which will hold all the different plates, at the time of this writing (4-8-2015) this is meant for a 2x2 grid of numberplates
			JPanel plateContainer = new JPanel();
			plateContainer.setBounds(290, 50, 1610, 960);
			topContainer.getContentPane().add(plateContainer);
			plateContainer.setLayout(null);

			{
				//These are all seperate plates
				JPanel plate1 = new JPanel();
				plate1.setBounds(0, 0, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				//plate1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,null));
				plateContainer.add(plate1);
				plate1.setVisible(true);				

				JPanel plate2 = new JPanel();
				plate2.setBounds((plateContainer.getWidth()-10)/2+10, 0, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate2.setBorder(null);
				plateContainer.add(plate2);
				plate2.setVisible(true);

				JPanel plate3 = new JPanel();
				plate3.setBounds(0, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate3.setBorder(null);
				plateContainer.add(plate3);
				plate3.setVisible(true);

				JPanel plate4 = new JPanel();
				plate4.setBounds((plateContainer.getWidth()-10)/2+10, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate4.setBorder(null);
				plateContainer.add(plate4);
				plate4.setVisible(true);



				try {
					picLabel_1 = new JLabel();
					picLabel_1.setBounds(0, 0, plate1.getWidth(), plate1.getHeight());
					plate1.add(picLabel_1);
					picLabel_1.setVisible(true);

					picLabel_2 = new JLabel();
					picLabel_2.setBounds(0, 0, plate1.getWidth(), plate1.getHeight());
					plate2.add(picLabel_2);
					picLabel_2.setVisible(true);

					picLabel_3 = new JLabel();
					picLabel_3.setBounds(0, 0, plate1.getWidth(), plate1.getHeight());
					plate3.add(picLabel_3);
					picLabel_3.setVisible(true);

					picLabel_4 = new JLabel();
					picLabel_4.setBounds(0, 0, plate1.getWidth(), plate1.getHeight());
					plate4.add(picLabel_4);
					picLabel_4.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			plateContainer.setVisible(true);
		}


		OutputStream out = new OutputStream() {

			@Override
			public void write(int arg0) throws IOException {
				Runner.menuOutput.append(String.valueOf(arg0));
				
			}
			
		};
		System.out.println("Testing menuoutput");
		topContainer.setVisible(true);
		AltUI.showOnScreen(2, topContainer); //Use this for multi monitor setups to chang eon which screen the program starts
		startProcessing();
	}
	/**
	 * Calling this method will start processing on the different images, in the final program this should get 4 numbers for the 4 plates to process
	 */
	public void startProcessing(){
		//ThreadedProcessing.count = new AtomicInteger(0);
		try {
			proc1 = new ThreadedProcessing("2");
			proc2 = new ThreadedProcessing("16");
			proc3 = new ThreadedProcessing("4");
			proc4 = new ThreadedProcessing("25");
			proc1.start();
			Thread.sleep(1);//these 1 millisecond delay are there to remove a bug that would happen once every 100 runs or so
			proc2.start();//This bug would cause multiple thread to have the same location identifier, this should not happen due to the way the syncronised atomicInteger is implemented
			Thread.sleep(1);//However the bug still occurred, therefore I have added these 1 millisecond delays which do not really impact the performance (it gives the total program an extra run time of 3 milliseconds)
			proc3.start();//Due to the size of this program and the fact that there are plenty of other parts which ones optimized would have a far greater impact on runtime I intend to keep it like it is for now
			Thread.sleep(1);
			proc4.start();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Will update a specific section of the screen.
	 * A better way would be to let the runner thread update, this would increase performance
	 * @param section The section to update
	 * @param buffer The imageIcon to place on the screen
	 */
	public static void updateScreen(int section, ImageIcon buffer){
		//System.out.println(section);
		switch (section){
		case 1:
			picLabel_1.setIcon(buffer);
			break;
		case 2:
			picLabel_2.setIcon(buffer);
			break;
		case 3:
			picLabel_3.setIcon(buffer);
			break;
		case 4:
			picLabel_4.setIcon(buffer);
			break;
		}
	}
	
	/**
	 * This method is usefull in multi monitor displays, it allows the program to start on a different screen, handy for debugging and testing
	 * @param screen the number of the screen to display on. (0 is primary, starts counting up from there)
	 * If the screen param is not found it will instead go to the default monitor
	 * @param frame The frame to place at the correct screen
	 */
	public static void showOnScreen(int screen, JFrame frame){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gd = ge.getScreenDevices();
		if (screen>-1&&screen<gd.length){
			frame.setLocation(gd[screen].getDefaultConfiguration().getBounds().x,gd[screen].getDefaultConfiguration().getBounds().y);
		}else if (gd.length>0){
			frame.setLocation(gd[0].getDefaultConfiguration().getBounds().x,gd[0].getDefaultConfiguration().getBounds().y);
		}else{
			throw new RuntimeException("No Screens Found!");
		}
	}
}
