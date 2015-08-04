package main;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * This class will hold my attempt at making a multi treaded interface for the program, showing to start 4 numberplate at the same time.
 */
public class AltUI {
	/**
	 * This constructor will start and manage the complete interface
	 */
	
	public AltUI() {
		JFrame topContainer = new JFrame("Experimental interface for the numberplate recogniser");
		topContainer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		topContainer.setSize(1920,1080);
		topContainer.getContentPane().setLayout(null);
		
		/*JPanel panel = new JPanel();
		panel.setBounds(0, 0, 233, 802);
		topContainer.getContentPane().add(panel);
		*/
		JPanel sidePanel = new JPanel();
		sidePanel.setBounds(5, 5, 275, 1030);
		topContainer.getContentPane().add(sidePanel);
		sidePanel.setLayout(null);
		
		JPanel plateContainer = new JPanel();
		plateContainer.setBounds(290, 50, 1610, 960);
		topContainer.getContentPane().add(plateContainer);
		plateContainer.setLayout(null);
		
		JPanel plate1 = new JPanel();
		plate1.setBounds(0, 0, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
		plateContainer.add(plate1);
		
		JPanel plate2 = new JPanel();
		plate2.setBounds((plateContainer.getWidth()-10)/2+10, 0, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
		plateContainer.add(plate2);
		
		JPanel plate3 = new JPanel();
		plate3.setBounds(0, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
		plateContainer.add(plate3);
		
		JPanel plate4 = new JPanel();
		plate4.setBounds((plateContainer.getWidth()-10)/2+10, (plateContainer.getHeight()-10)/2+10, (plateContainer.getWidth()-10)/2, (plateContainer.getHeight()-10)/2);
		plateContainer.add(plate4);
		
		
		
		
		plate1.setVisible(true);
		plate2.setVisible(true);
		plate3.setVisible(true);
		plate4.setVisible(true);
		plateContainer.setVisible(true);
		sidePanel.setVisible(true);
		topContainer.setVisible(true);
		
		
		
		
		while (true){
			System.out.println("width: " + topContainer.getWidth());

			System.out.println("Height: " + topContainer.getHeight());
		}
	}
}
