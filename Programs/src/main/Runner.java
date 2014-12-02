package main;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JLabel;

import image.Processing;
import image.FileAccess;

import javax.swing.JTextArea;

/**
 * This is the class that will start the program, uses for it are debugging and in the end.
 * @author Ylva
 *
 */
public class Runner {

	/**
	 * This is the label that the picture is rendered upon
	 */
	public static JLabel picLabel;
	
	/**
	 * This is where the program can communicate to the user, by using menuOutput.append(string) you can send a string to the user
	 */
	public static JTextArea menuOutput;
	
	/**
	 * Main method, runs the whole program
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame  jf = new JFrame("NumberPlate Recogniser");
		System.out.println("Started");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(null);
		jf.setSize(1100,600);
		
		menuOutput = new JTextArea();
		menuOutput.setBounds(10, 11, 241, 536);
		jf.getContentPane().add(menuOutput);
		menuOutput.setLineWrap(true);
		
		menuOutput.append("Started\n");
		jf.setLocationRelativeTo(null);
		picLabel = null;
		BufferedImage buffer = FileAccess.getImage();
		try {
			picLabel = new JLabel(FileAccess.getImageIcon(buffer));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		picLabel.setBounds(258, 11, 816, 536);
		
		
		jf.getContentPane().add(picLabel);
		picLabel.setVisible(true);

		jf.setVisible(true);
		
		
		Processing.histogramEqualisation(buffer);
		//FileAccess.writeImage(buffer);
		Processing.ColorFilter(buffer);
		menuOutput.append("End");
	}
	
	
}
