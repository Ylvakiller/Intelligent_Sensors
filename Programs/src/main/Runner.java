package main;

import java.awt.Color;
import java.awt.image.BufferedImage;

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
		System.out.println("Started");
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.getContentPane().setLayout(null);
		jf.setSize(1100,600);
		
		JTextArea menuOutput = new JTextArea();
		menuOutput.setBounds(10, 11, 241, 536);
		jf.getContentPane().add(menuOutput);
		menuOutput.setLineWrap(true);
		
		menuOutput.append("Started\n");
		jf.setLocationRelativeTo(null);
		Opener test = new Opener();
		JLabel picLabel = null;
		BufferedImage buffer = test.getImage();
		try {
			picLabel = new JLabel(test.getImageIcon(buffer));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		picLabel.setBounds(258, 11, 816, 536);
		
		
		jf.getContentPane().add(picLabel);
		picLabel.setVisible(true);
		int xMax = buffer.getWidth();
		int yMax = buffer.getHeight();
		System.out.println("xmax " + xMax);
		menuOutput.append("max X : " + xMax + "\n");
		menuOutput.append("max Y : " + yMax + "\n");
		menuOutput.append("Now starting detection of yellow pixels\n");
		Color C;

		jf.setVisible(true);
		int x = 0, y = 0;
		while (x<xMax){
			while(y<yMax){
				C = new Color(buffer.getRGB(x, y));
				int red = C.getRed();
				int green = C.getGreen();
				int blue = C.getBlue();
				if (red>190&&green<200&&green>140&&blue<100){
					buffer.setRGB(x, y, Color.white.getRGB());
				}else{
					buffer.setRGB(x, y, Color.black.getRGB());
				}
				y++;
			}
			try {
				picLabel.setIcon(test.getImageIcon(buffer));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			x++;
			y=0;
		}
		menuOutput.append("End");
	}
}
