package local.milan.scoreBoard;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class SimpleScoreboard {

	private Scoreboard scoreboard;

	private Vector<String> textVector;
	Objective obj;
	int prevSize;
	String title;
	Random r = new Random();

	public SimpleScoreboard(String title) {
		this.title = title;
		this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		this.textVector = new Vector<String>();
		obj = scoreboard.registerNewObjective((title.length() > 16 ? title.substring(0, 15) : title), "dummy", title);
		obj.setDisplaySlot(DisplaySlot.SIDEBAR);

	}

	public void blankLine() {
		add(" ");
	}

	public void add(String text) {
		while (textVector.contains(text)) {
			text += " ";
		}

		if (text.length() > 16) {
			for (int i = 0; i < (int) (text.length() / 16) + 1; i++) {
				add(text.substring(16 * i, Math.min(16 * (i + 1), text.length())));
			}
			return;
		}
		textVector.add(text);
	}

	public void setName(int index, int score) {
		String s = textVector.get(index);
		scoreboard.getTeam("team" + index).setPrefix(s);
	}

	private String randomColor() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append('§');
			sb.append(r.nextInt(10));
		}
		return sb.toString();
	}

	public void build() {

		if (textVector.size() != this.prevSize) {// re-initialize the scoreboard
			scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
			obj = scoreboard.registerNewObjective((title.length() > 16 ? title.substring(0, 15) : title), "dummy",
					title);
			obj.setDisplaySlot(DisplaySlot.SIDEBAR);
			for (int i = 0; i < textVector.size(); i++) {
				Team t = scoreboard.registerNewTeam("team" + i);
				String entryColor = randomColor();
				t.addEntry(entryColor);
				t.setPrefix(textVector.get(i));
				obj.getScore(entryColor).setScore(textVector.size() - i);
			}
		}
		for (int i = 0; i < this.textVector.size(); i++) {
			setName(i, this.textVector.size() - i);

		}

	}

	public void clearScoreBoard() {
		textVector.clear();

	}

	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	public void send(Player... players) {
		for (Player p : players)
			if (p != null && p.isOnline()) {
				p.setScoreboard(scoreboard);
			}
	}

}