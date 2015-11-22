package main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import image.FileAccess;
import image.Processing;

/**
 * This is the class that will start the program, uses for it are debugging and in the end.
 * @author Ylva
 *
 */
public class Runner {

	/**
	 * This is the label that the picture is rendered upon
	 */
	public static JLabel picLabel_1;
	/**
	 * This label is to show the user which image number is being showed now
	 */
	public static JLabel label;
	/**
	 * The current image number being processed
	 */
	private static int count;
	/**
	 * This is where the program can communicate to the user, by using menuOutput.append(string) you can send a string to the user
	 */
	public static JTextArea menuOutput;

	/**
	 * A boolean that keeps track on whether or not the button is pressed
	 */
	public static boolean buttonPressed  = true;;

	/**
	 * This will decide which interface to use, either the normal one or the experimental multitreaded version.
	 */
	public static boolean altUI = true;
	/**
	 * Main method, runs the whole program
	 * @param args
	 */
	public static void main(String[] args) {
		if (altUI){
			AltUI userInterface = new AltUI();
		}else{
			count =2;
			FileAccess.setFileNumber(count);

			JFrame  jf = new JFrame("NumberPlate Recogniser");
			System.out.println("Started");
			jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			jf.setSize(1600,900);
			jf.getContentPane().setLayout(null);

			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 11, 241, 537);
			scrollPane.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			scrollPane.setAutoscrolls(true);
			jf.getContentPane().add(scrollPane);

			menuOutput = new JTextArea();
			scrollPane.setViewportView(menuOutput);
			menuOutput.setLineWrap(true);
			jf.setLocationRelativeTo(null);
			BufferedImage buffer = FileAccess.getImage("01");
			try {
				picLabel_1 = new JLabel(FileAccess.getImageIcon(buffer));
				picLabel_1.setBounds(261, 0, 1313, 851);
			} catch (Exception e) {
				e.printStackTrace();
			}
			jf.getContentPane().add(picLabel_1);


			JButton btnNextImage = new JButton("Next Image");
			btnNextImage.setBounds(20, 559, 89, 23);
			jf.getContentPane().add(btnNextImage);

			JLabel lblImageNumber = new JLabel("Image number");
			lblImageNumber.setBounds(119, 563, 96, 14);
			jf.getContentPane().add(lblImageNumber);

			label = new JLabel("0");
			label.setBounds(212, 563, 27, 14);
			label.setHorizontalAlignment(SwingConstants.CENTER);
			jf.getContentPane().add(label);
			picLabel_1.setVisible(true);
			label.setText(""+count);
			jf.setVisible(true);
			btnNextImage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					buttonPressed = true;
				}
			});
			scrollPane.setAutoscrolls(true);
			while (true){
				try {
					Thread.sleep(1);                 //Apparently, not having this delay screws the whole program up...
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
				if (buttonPressed){

					if (count>26){
						menuOutput.append("Done processing all images\n");
					}else{
						menuOutput.append("\nNow processing image number " + count + "\n");
						label.setText(""+count);
						processImageNumber(count);
						menuOutput.append("Done processing image number " + count + "\n");

					}
					count++;
					buttonPressed = false;
				}else{}
			}
		}
	}
	/**
	 * Will process a certain image defined in the number
	 * @param number the image number to process
	 */
	private static void processImageNumber(int number){
		FileAccess.setFileNumber(number);
		BufferedImage buffer = FileAccess.getImage(FileAccess.getFileNumber(number));
		//BufferedImage temp = Processing.cutimage(buffer);
		try {
			Processing.findNumberPlate(buffer, FileAccess.getFileNumber(number));
		} catch (PlateNotFoundException e) {
			menuOutput.append("No number plate found\nNeed alternative method to find the numberplate\n");
			e.printStackTrace();
		}
	}
	
	
	public static void updateScreen(BufferedImage icon, int number){
		if (altUI){
			AltUI.updateScreen(number, Processing.ScaleThreadIcon(icon));
		}else{
			Runner.picLabel_1.setIcon(Processing.ScaledImageIcon(icon));
		}
	}
	
	protected void setMenuOutput(){
		
	}
}
