package main;

import image.Opener;

/**
 * This is the class that will start the program, uses for it are debugging and in the end.
 * @author Ylva
 *
 */
public class Runner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Opener test = new Opener();
		try {
			test.ImageDemo();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
