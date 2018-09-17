import java.awt.Dimension;

import javax.swing.JFrame;

public class MainClass {
	public static Game game;
	//public static Audio audio;
	public static JFrame theframe;
	public static void main(String[] args) throws Exception {
		theframe = new JFrame("Flimsy Emblem Revisited: Electric Boogaloo");
		theframe.setResizable(false);
		
		//Scanner s = new Scanner(new File("save/audio.txt"));
		//audio = new Audio(s.nextBoolean(), s.nextBoolean());
		//s.close();

		game = new Game();
		theframe.add(game);
		game.setPreferredSize(new Dimension(Game.PWIDTH, Game.PHEIGHT));//sets size of inside
		
		theframe.pack();//expands outside to do the same
		theframe.setVisible(true);
		theframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
