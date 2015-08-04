package main;

import image.ThreadedProcessing;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

/*
 * This class will hold my attempt at making a multi treaded interface for the program, showing to start 4 numberplate at the same time.
 */
public class AltUI {
	/**
	 * This constructor will start and manage the complete interface
	 */
	public static JTextArea menuOutput;
	
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
				plate1.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,null));
				plateContainer.add(plate1);

				JPanel plate2 = new JPanel();
				plate2.setBounds((plateContainer.getWidth()-10)/2+10, 0, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate2.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,null));
				plateContainer.add(plate2);

				JPanel plate3 = new JPanel();
				plate3.setBounds(0, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate3.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,null));
				plateContainer.add(plate3);

				JPanel plate4 = new JPanel();
				plate4.setBounds((plateContainer.getWidth()-10)/2+10, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
				plate4.setBorder(new EtchedBorder(EtchedBorder.RAISED, null,null));
				plateContainer.add(plate4);
			}
		}
		topContainer.setVisible(true);
		startProcessing();
		while (true){

		}
	}
	/**
	 * Calling this method will start processing on the different images, in the final program this should get 4 numbers for the 4 plates to process
	 */
	public void startProcessing(){
		ThreadedProcessing proc1 = new ThreadedProcessing("1");
		ThreadedProcessing proc2 = new ThreadedProcessing("2");
		ThreadedProcessing proc3 = new ThreadedProcessing("3");
		ThreadedProcessing proc4 = new ThreadedProcessing("4");
		proc1.start();
		proc2.start();
		proc3.start();
		proc4.start();
	}
}
