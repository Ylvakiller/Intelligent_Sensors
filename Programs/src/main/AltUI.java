package main;

import java.awt.Button;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

import image.ThreadedProcessing;

/*
 * This class will hold my attempt at making a multi treaded interface for the program, showing to start 4 numberplate at the same time.
 */
public class AltUI {
	/**
	 * This constructor will start and manage the complete interface
	 */
	public static JTextArea menuOutput;
	
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
					}
				});
				btnNextUpRight.setBounds((buttonPanel.getWidth()-15)/2+10, 5, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextUpRight);
				
				Button btnNextDownLeft = new Button("Next step lower left");
				btnNextDownLeft.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
					}
				});
				btnNextDownLeft.setBounds(5, 35, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextDownLeft);
				
				Button btnNextdownRight = new Button("Next step lower right");
				btnNextdownRight.setBounds((buttonPanel.getWidth()-15)/2+10, 35, (buttonPanel.getWidth()-15)/2, 25);
				buttonPanel.add(btnNextdownRight);

				JPanel textPanel = new JPanel();
				textPanel.setBounds(0, buttonPanel.getHeight(), sidePanel.getWidth(), sidePanel.getHeight()-buttonPanel.getHeight());
				textPanel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null,null));
				sidePanel.add(textPanel);
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setBounds(0, 0, textPanel.getWidth(), textPanel.getHeight());
					scrollPane.setAutoscrolls(true);
					menuOutput = new JTextArea();
					scrollPane.setViewportView(menuOutput);
					menuOutput.setLineWrap(true);
					
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
		
		
		
		topContainer.setVisible(true);
		startProcessing();
	}
	/**
	 * Calling this method will start processing on the different images, in the final program this should get 4 numbers for the 4 plates to process
	 */
	public void startProcessing(){
		//ThreadedProcessing.count = new AtomicInteger(0);
		 proc1 = new ThreadedProcessing("8");
		 proc2 = new ThreadedProcessing("8");
		 proc3 = new ThreadedProcessing("8");
		 proc4 = new ThreadedProcessing("8");
		proc1.start();
		proc2.start();
		proc3.start();
		proc4.start();
	}
	
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
}
