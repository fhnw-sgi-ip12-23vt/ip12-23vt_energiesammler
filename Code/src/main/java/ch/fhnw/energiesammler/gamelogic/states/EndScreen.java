package ch.fhnw.energiesammler.gamelogic.states;

import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.ui.GameMenu;

/**
 * EndScreen for the game. It shows the Statistics for the now finished run
 */
public class EndScreen extends GameStateBase {
  GameMenu gameMenu;
  int nextPage = 0;

  public EndScreen(Platformer p, InputManager i, Level l) {
    super(p, i, l);
    gameMenu = p.gameMenu;
  }

  @Override
  public void draw() {
    inputManager.update();
    if (nextPage == 0) {
      gameMenu.displayDoorBalance();
    }
    if (nextPage == 1) {
      gameMenu.displayScore();
    }
    if (nextPage == 2) {
      gameMenu.displayTakeHomeMessage();
    }
  }

  @Override
  public void processInput() {
    if (nextPage > 0 && inputManager.pressed(GameCommand.BUTTON_A)) {
      nextPage -= 1;
      draw();
    }
    if (nextPage < 2 && inputManager.pressed(GameCommand.BUTTON_B)) {
      nextPage += 1;
      draw();
    }
    if (inputManager.pressed(GameCommand.BUTTON_B) && nextPage == 2) {
      platformer.gameState = new HomeScreen(platformer, inputManager, level);
    }
  }
}