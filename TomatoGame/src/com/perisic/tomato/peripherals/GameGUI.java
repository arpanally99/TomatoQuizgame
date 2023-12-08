package com.perisic.tomato.peripherals;



import java.awt.event.ActionEvent;

import java.awt.event.ActionListener;

import java.awt.image.BufferedImage;

import java.util.Timer;

import java.util.TimerTask;



import javax.swing.ImageIcon;

import javax.swing.JButton;

import javax.swing.JFrame;

import javax.swing.JLabel;

import javax.swing.JOptionPane;

import javax.swing.JPanel;

import javax.swing.JScrollPane;

import javax.swing.JTextArea;



import com.perisic.tomato.engine.GameEngine;



/**

 * A Simple Graphical User Interface for the Six Equation Game.

 * 

 * @author Arpan Lama

 *

 */

public class GameGUI extends JFrame implements ActionListener {



	private static final long serialVersionUID = -107785653906635L;



	private JLabel questArea = null;

	private GameEngine myGame = null;

	private BufferedImage currentGame = null;

	private JTextArea infoArea = null;

	private JLabel timerLabel = new JLabel("Time Remaining: 2:00"); // Initial value, adjust as needed

	private JLabel levelLabel = new JLabel("Level: 1"); // Initial value, adjust as needed

	private Timer sessionTimer = new Timer(true);



	/**

	 * Method that is called when a button has been pressed.

	 */

	@Override

	public void actionPerformed(ActionEvent e) {

		int solution = Integer.parseInt(e.getActionCommand());

		boolean correct = myGame.checkSolution(solution);

		int score = myGame.getScore();

		if (correct) {

			System.out.println("Correct solution entered!");

			currentGame = myGame.nextGame();

			ImageIcon ii = new ImageIcon(currentGame);

			questArea.setIcon(ii);

			infoArea.setText("Good!  Score: " + score);

			updateLevelLabel(); // Update the level label after completing a level

		} else {

			System.out.println("Not Correct");

			infoArea.setText("Oops. Try again!  Score: " + score);

		}

	}



	/**

	 * Initializes the game.

	 * 

	 * @param player

	 */

	private void initGame(String player) {

		setSize(690, 500);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setTitle("What is the missing value?");

		JPanel panel = new JPanel();



		myGame = new GameEngine(player);

		currentGame = myGame.nextGame();



		infoArea = new JTextArea(1, 40);

		infoArea.setEditable(false);

		infoArea.setText("What is the value of the tomato?   Score: 0");



		JScrollPane infoPane = new JScrollPane(infoArea);

		panel.add(infoPane);



		questArea = new JLabel(new ImageIcon(currentGame));

		JScrollPane questPane = new JScrollPane(questArea);

		panel.add(questPane);



		for (int i = 0; i < 10; i++) {

			JButton btn = new JButton(String.valueOf(i));

			panel.add(btn);

			btn.addActionListener(this);

		}



		panel.add(timerLabel); // Add the timer label to the panel

		panel.add(levelLabel); // Add the level label to the panel



		getContentPane().add(panel);

		panel.repaint();



		// To start the timer when the GameGUI is created

		updateTimerLabel();

	}



	/**

	 * Default player is null.

	 */

	public GameGUI() {

		super();

		initGame(getName());

		setVisible(true);

	}



	/**

	 * Use this to start GUI, e.g., after login.

	 * 

	 * @param player

	 */

	public GameGUI(String player) {

		super();

		initGame(player);

		setVisible(true);

	}



	/**

	 * Main entry point into the equation game. Can be used without login for

	 * testing.

	 * 

	 * @param args not used.

	 */

	public static void main(String[] args) {

		GameGUI myGUI = new GameGUI();

		myGUI.setVisible(true);

	}



	private void updateTimerLabel() {

		sessionTimer.scheduleAtFixedRate(new TimerTask() {

			int seconds = 120; // Initial value, to adjust as needed



			@Override

			public void run() {

				int minutes = seconds / 60;

				int remainingSeconds = seconds % 60;

				timerLabel.setText(String.format("Time Remaining: %d:%02d", minutes, remainingSeconds));



				if (seconds == 0) {

					this.cancel();

					// Handling session timeout (e.g., end the game)

					JOptionPane.showMessageDialog(null, "Game Over!");

					dispose(); // To close the GameGUI

				} else {

					seconds--;

				}

			}

		}, 0, 1000);

	}



	private void updateLevelLabel() {

		if (myGame.crossedLevel()) {

			levelUpPrompt();

			sessionTimer.cancel(); // To cancel the timer when crossing a level

			// To Start the timer again for the new level

			int secondsForNewLevel = myGame.getSecondsForCurrentLevel();

			startNewLevel(secondsForNewLevel);

		}

	}



	private void startNewLevel(int seconds) {

		myGame.nextLevel();

		levelLabel.setText("Level: " + myGame.getCurrentLevel());

		sessionTimer = new Timer(true);

		updateTimerLabel();

	}



	private void levelUpPrompt() {

		int currentLevel = myGame.getCurrentLevel();

		JOptionPane.showMessageDialog(this, "Congratulations! You've reached Level " + currentLevel + "!");

	}

}