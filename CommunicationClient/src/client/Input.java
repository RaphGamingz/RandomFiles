package client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Input extends KeyAdapter {
	public void keyReleased(KeyEvent e) {
	    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
	    	System.out.println("enter");
	    	ClientWindow.send();
	    }
	}
}