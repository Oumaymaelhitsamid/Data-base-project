package test;

import window.AccesWindow;
import window.FirstWindow;

import java.awt.*;

public class TestGange {
	public static void main(String[] args) {
		// Choose here for the number of offer to buy a product
		int NUMBER_OF_OFFER = 5;
		FirstWindow frame = new FirstWindow(NUMBER_OF_OFFER);
		frame.setVisible(true);
	}
}
