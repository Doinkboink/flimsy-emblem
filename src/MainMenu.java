import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.imageio.ImageIO;

public class MainMenu {
	private BufferedImage title;
	private String substate;
	private int selection;
	
	public MainMenu() {
		try {
			title = ImageIO.read(new File("img/mainmenu/title.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		substate = "title";
		selection = 0;
		//do stuffs
	}
	
	private BufferedImage im = new BufferedImage(Game.PWIDTH, Game.PHEIGHT, BufferedImage.TYPE_INT_ARGB);
	public BufferedImage getImage() {
		Graphics2D g = (Graphics2D)im.getGraphics();
		g.setBackground(new Color(255, 255, 255, 0));
		g.clearRect(0, 0, Game.PWIDTH, Game.PHEIGHT);
		
		g.drawImage(title, 0, 0, null);
		
		g.setColor(Color.BLACK);
		g.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 60));
		g.drawString("Fire Emblem", 50, 80);
		g.setFont(new Font("Copperplate Gothic Bold", Font.PLAIN, 120));
		g.drawString("EDGELORD", 90, 180);
		Font fb32 = new Font("Copperplate Gothic Bold", Font.PLAIN, 32);
		Font fl32 = new Font("Copperplate Gothic Light", Font.PLAIN, 32);
		switch (substate) {
		case "title":
			g.setFont(new Font("Copperplate Gothic Light", Font.PLAIN, 50));
			g.drawString("Press", 100, 400);
			g.drawString("Start", 100, 450);
			break;
		case "main":
			for (int i = 0; i < 4; i++) {
				g.setFont(i==selection?fb32:fl32);
				g.drawString(new String[]{"New Game", "Load Save", "Options", "Quit"}[i], 85 + 5 * i, 310 + 80 * i);
			}
			break;
		case "options":
			for (int i = 0; i < 4; i++) {
				g.setFont(i==selection?fb32:fl32);
				g.drawString(new String[]{"Music", "Sound", "Effect", "Back"}[i], 85 + 5 * i, 310 + 80 * i);
			}
			g.setFont(new Font("Copperplate Gothic Light", Font.ITALIC, 32));
			g.drawString(Game.audio.onMusic()?"On":"Off", 85 + 
					g.getFontMetrics(selection==0?fb32:fl32).stringWidth("Music  "), 310);
			g.drawString(Game.audio.onSound()?"On":"Off", 90 + 
					g.getFontMetrics(selection==1?fb32:fl32).stringWidth("Sound  "), 390);
			g.drawString(Game.effect?"On":"Off", 95 + 
					g.getFontMetrics(selection==2?fb32:fl32).stringWidth("Effect  "), 470);
			break;
		default:
			System.err.println("Error in MainMenu: Substate not Found");
			break;
		}
		
		return im;
	}
	
	public void handleKeys(KeyPackage k) {
		if (k.button != ' ') {
			Game.audio.playSound(Audio.Sounds.MENU);
			switch (substate) {
			case "title":
				if (k.button == 'Z' || k.button == 'E') {
					selection = 0;
					substate = "main";
				}
				break;
			case "main":
				if (k.button == 'Z' || k.button == 'E') {
					switch (selection) {
					case 0://TODO
						//should create a battle and sidebar,
						//but those would be hidden in the background
						//overlaid by a cutscene with solid backdrop
						break;
					case 1://TODO
						break;
					case 2:
						selection = 0;
						substate = "options";
						break;
					case 3:
						System.exit(0);
						break;
					default:
						break;
					}
				}
				if (k.button == 'X')substate = "title";
				break;
			case "options":
				if (k.button == 'Z' || k.button == 'E') {
					switch (selection) {
					case 0:
						Game.audio.toggleMusic();
						break;
					case 1:
						Game.audio.toggleSound();
						break;
					case 2:
						Game.effect = !Game.effect;
						break;
					case 3:
						PrintStream p;
						try {
							p = new PrintStream(new File("save/options.txt"));
							p.println(Game.audio.onMusic());
							p.println(Game.audio.onSound());
							p.println(Game.effect);
							p.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
						selection = 0;
						substate = "main";
						break;
					default:
						break;
					}
				}
				if (k.button == 'X') {
					PrintStream p;
					try {
						p = new PrintStream(new File("save/options.txt"));
						p.println(Game.audio.onMusic());
						p.println(Game.audio.onSound());
						p.println(Game.effect);
						p.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
					selection = 0;
					substate = "main";
				}
				break;
			default:
				System.err.println("Error in MainMenu: Substate not Found");
				break;
			}
		}
		else {
			switch (substate) {
			case "title":
				break;
			case "main":
			case "options":
				if (k.U) {
					Game.audio.playSound(Audio.Sounds.MENU);
					selection += 3;
					selection %= 4;
				}
				if (k.D) {
					Game.audio.playSound(Audio.Sounds.MENU);
					selection += 1;
					selection %= 4;
				}
				break;
			default:
				System.err.println("Error in MainMenu: Substate not Found");
				break;
			}
		}
	}
}
