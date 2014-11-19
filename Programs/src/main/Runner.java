package main;

import javax.swing.JFrame;
import javax.swing.JLabel;
import image.Opener;

import javax.swing.JTextArea;

/**
 * This is the class that will start the program, uses for it are debugging and in the end.
 * @author Ylva
 *
 */
public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		JFrame  jf = new JFrame("NumberPlate Recogniser");
		
		jf.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		jf.getContentPane().setLayout(null);
		jf.setSize(1100,600);
		
		JTextArea menuOutput = new JTextArea();
		menuOutput.setBounds(10, 11, 241, 536);
		jf.getContentPane().add(menuOutput);
		menuOutput.setLineWrap(true);
		menuOutput.append("Started");
		jf.setLocationRelativeTo(null);
		Opener test = new Opener();
		JLabel picLabel = null;
		try {
			picLabel = new JLabel(test.getImage());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		picLabel.setBounds(258, 11, 816, 536);
		jf.getContentPane().add(picLabel);
		picLabel.setVisible(true);

		menuOutput.append("End");

		jf.setVisible(true);
	}
}
