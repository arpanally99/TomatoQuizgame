package com.perisic.tomato.engine;

import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class where the games are coming from.
 */
public class GameEngine {
String thePlayer = null;

// Added fields for level management
private int currentLevel = 1; // Initial level
private int score = 0;

// Added timer and seconds remaining for the current level
private Timer sessionTimer = new Timer(true);
private int secondsRemaining;

/**
* Each player has their own game engine.
*
* @param player
*/
public GameEngine(String player) {
thePlayer = player;
initializeTimer();
}

GameServer theGames = new GameServer();
Game current = null;

/**
* Retrieves a game. This basic version only has two games that alternate.
*/
public BufferedImage nextGame() {
current = theGames.getRandomGame();
resetTimer();
return current.getImage();
}

/**
* Checks if the parameter i is a solution to the game URL. If so, score is
* increased by one.
*
* @param i
* @return
*/
public boolean checkSolution(int i) {
if (i == current.getSolution()) {
score++;
return true;
} else {
return false;
}
}

/**
* Retrieves the score.
*
* @return
*/
public int getScore() {
return score;
}

// New methods for level management

/**
* Checks if the user has crossed the current level.
*
* @return true if crossed, false otherwise
*/
public boolean crossedLevel() {
return score % 5 == 0;
}

/**
* Gets the time limit for the current level in seconds.
*
* @return time limit in seconds
*/
public int getSecondsForCurrentLevel() {
// For simplicity, let's say each level has a fixed time limit
switch (currentLevel) {
case 1:
return 240; // 4 minutes for level 1
case 2:
return 120; // 2 minutes for level 2
case 3:
return 30; // 0.5 minutes (30 seconds) for level 3
default:
return 0; // No time limit for unknown levels
}
}

/**
* Moves to the next level.
*/
public void nextLevel() {
currentLevel++;
resetTimer();
}

/**
* Gets the current level.
*
* @return current level
*/
public int getCurrentLevel() {
return currentLevel;
}

private void initializeTimer() {
// To schedule the timer task to update the timer label every second
sessionTimer.scheduleAtFixedRate(new TimerTask() {
@Override
public void run() {
if (secondsRemaining > 0) {
secondsRemaining--;
} else {
// To end the game or move to the next level when time runs out
if (currentLevel < 3) {
nextLevel();
} else {
// Handle session timeout (e.g., end the game)
sessionTimer.cancel();
System.out.println("Game Over!");
// To close the GameGUI or perform other necessary actions
}
}
}
}, 0, 1000);
}

private void resetTimer() {
secondsRemaining = getSecondsForCurrentLevel();
}
}
