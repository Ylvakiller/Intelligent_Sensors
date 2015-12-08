package main;

public class SegmentSizeException extends Exception {



	private static final long serialVersionUID = 8031073026192282869L;

	public SegmentSizeException() {
		super("The plate was not found, you need an alternative way to find the plate");
	}

	public SegmentSizeException(String arg0) {
		super(arg0);
	}

	public SegmentSizeException(Throwable arg0) {
		super(arg0);
	}

	public SegmentSizeException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public SegmentSizeException(String arg0, Throwable arg1, boolean arg2,boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
