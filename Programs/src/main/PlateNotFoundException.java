package main;

public class PlateNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 677340682792209863L;

	public PlateNotFoundException() {
		super("The plate was not found, you need an alternative way to find the plate");
	}

	public PlateNotFoundException(String arg0) {
		super(arg0);
	}

	public PlateNotFoundException(Throwable arg0) {
		super(arg0);
	}

	public PlateNotFoundException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public PlateNotFoundException(String arg0, Throwable arg1, boolean arg2,boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
