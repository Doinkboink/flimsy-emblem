import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Game extends JPanel{
	/*
	 * Game cycle goes as thus:
	 * Repaint: Does whatever it wants, when it wants, FPS doesn't matter THAT much
	 * Ticks: 60 every second where they go in the order
	 * -Key input update
	 * -Relay keys to components
	 * -All components update their frames or animations if they should
	 */

	public static final int PWIDTH = 960;
	public static final int PHEIGHT = 640;
	
	public static Audio audio;
	public static boolean effect;//transitions... and maybe other things
	
	/*
	 * Has multiple relatively independent components
	 * that can influence one another
	 */
	private MainMenu mainmenu;
	/* private Cutscene cutscene
	 * private WorldMap worldmap;
	 * private Sidebar sidebar;
	 * private Notification notif; //"You got a [Boots]." "An iron sword broke!"
	 * private Textbox Textbox; //Iron sord display stats, etc
	 * private InGameMenu ingamemenu;
	 * ...also add anything else if they come up
	 */

	private ActionListener actionlistener;
	private KeyListener keylistener;
	private Timer timer;
	
	private int Ucd, Dcd, Lcd, Rcd;
	private boolean Udown, Ddown, Ldown, Rdown, Zdown, Xdown, Cdown, Sdown, Edown;
	private KeyPackage keypackage;
	
	public Game() {
		Scanner optionscanner;
		try {
			optionscanner = new Scanner(new File("save/options.txt"));
			audio = new Audio(optionscanner.nextBoolean(), optionscanner.nextBoolean());
			effect = optionscanner.nextBoolean();
			optionscanner.close();
			
			audio.playMusic(Audio.Musics.TEMP2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mainmenu = new MainMenu();
		
		actionlistener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				update();
				repaint();
			}
		};
		timer = new Timer(16, actionlistener);
		timer.setRepeats(true);
		timer.start();
		
		keypackage = new KeyPackage();
		
		keylistener = new KeyListener() {
			@Override
			public void keyTyped(KeyEvent arg0) {
				//Don't use this one
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (!Udown)keypackage.U = true;
					Udown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					if (!Ddown)keypackage.D = true;
					Ddown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (!Ldown)keypackage.L = true;
					Ldown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (!Rdown)keypackage.R = true;
					Rdown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_Z) {
					if (!Zdown)keypackage.button = 'Z';
					Zdown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_X) {
					if (!Xdown)keypackage.button = 'X';
					Xdown = true;
				}if (e.getKeyCode() == KeyEvent.VK_C) {
					if (!Cdown)keypackage.button = 'C';
					Cdown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_S) {
					if (!Sdown)keypackage.button = 'S';
					Sdown = true;
				}
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					if (!Edown)keypackage.button = 'E';
					Edown = true;
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					Ucd = 0;
					Udown = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					Dcd = 0;
					Ddown = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					Lcd = 0;
					Ldown = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					Rcd = 0;
					Rdown = false;
				}
				if (e.getKeyCode() == KeyEvent.VK_Z)Zdown = false;
				if (e.getKeyCode() == KeyEvent.VK_X)Xdown = false;
				if (e.getKeyCode() == KeyEvent.VK_C)Cdown = false;
				if (e.getKeyCode() == KeyEvent.VK_S)Sdown = false;
				if (e.getKeyCode() == KeyEvent.VK_ENTER)Edown = false;
			}	
		};
		addKeyListener(keylistener);
		
		setFocusable(true);
	}
	
	private BufferedImage canvas = new BufferedImage(960, 640, BufferedImage.TYPE_INT_ARGB);
	public void paint(Graphics gg) {
		Graphics2D g = (Graphics2D)canvas.getGraphics();
		g.setBackground(new Color(192, 108, 64));//just for testing/debug purposes
		g.clearRect(0, 0, 960, 640);//clears screen; should become obsolete eventually
		gg.clearRect(0, 0, 960, 640);
		
		if (mainmenu != null) {
			g.drawImage(mainmenu.getImage(), 0, 0, null);
		}
		
		gg.drawImage(canvas, 0, 0, null);//done such that the equivalent of VSync works
	}
	
	public void update() {
		updateKeys();
	}
	
	public void updateKeys() {
		if (Ucd > 0)Ucd--;
		if (Ucd == 0 && Udown) {
			Ucd = 8;
			keypackage.U = true;
		}
		if (Dcd > 0)Dcd--;
		if (Dcd == 0 && Ddown) {
			Dcd = 8;
			keypackage.D = true;
		}
		if (Lcd > 0)Lcd--;
		if (Lcd == 0 && Ldown) {
			Lcd = 8;
			keypackage.L = true;
		}
		if (Rcd > 0)Rcd--;
		if (Rcd == 0 && Rdown) {
			Rcd = 8;
			keypackage.R = true;
		}
		
		//now that we've dealt with all the keys we can send them off
		if (mainmenu != null) {
			mainmenu.handleKeys(keypackage);
		}
		
		//now reset the keypackage for the next frame--default style
		keypackage = new KeyPackage();
	}
}
