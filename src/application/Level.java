package application;


import java.util.ArrayList;
public class Level {
	private ArrayList<double[]> hitboxes = new ArrayList<>();
			
	public void setHitbox(double x1, double y1, double x2, double y2) {
		hitboxes.add(new double[]{ x1, y1, x2, y2 });
	}
	public void drawLevel() {
		
	}
}
