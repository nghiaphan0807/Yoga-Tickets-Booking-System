package utiliti;

import java.awt.Dimension;

import javax.swing.JComponent;

public class Utilities {

	public static String parseString(Object obj) {
		try {
			return String.valueOf(obj);
		} catch(Exception e) {
			
		}
		return "";
	}
	
	public static void setCompoenentSize(JComponent component, int width, int height) {
		component.setPreferredSize(new Dimension(width, height));
		component.setMinimumSize(new Dimension(width, height));
		component.setMaximumSize(new Dimension(width, height));
	}
	
}
