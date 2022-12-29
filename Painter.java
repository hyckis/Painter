// 105403506資管3A何宜親

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Painter {
	
	public static void main(String[]args) {
		
		Frame frame = new Frame();		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(840, 680);
		frame.setVisible(true);
		
		// Welcome Message
		JOptionPane.showMessageDialog(null, "Welcome!", "訊息", JOptionPane.INFORMATION_MESSAGE);
		
	}
	
}
