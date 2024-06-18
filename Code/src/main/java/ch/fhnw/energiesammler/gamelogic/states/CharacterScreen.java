package ch.fhnw.energiesammler.gamelogic.states;

import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.ui.GameMenu;

public class CharacterScreen extends GameStateBase {
  GameMenu gameMenu;

  public CharacterScreen(Platformer p, InputManager i, Level l) {
    super(p, i, l);
    gameMenu = p.gameMenu;
    platformer.characterChoice = true;
  }

  @Override
  protected void setup() {
    platformer.characterScreenSetup();
  }

  @Override
  public void draw() {
    inputManager.update();
    platformer.drawCharacterScreen();
  }

  @Override
  public void processInput() {
    if (inputManager.pressed(GameCommand.LEFT)) {
      platformer.characterChoice = true;
    }
    if (inputManager.pressed(GameCommand.RIGHT)) {
      platformer.characterChoice = false;
    }
    if (inputManager.pressed(GameCommand.BUTTON_B)) {
      platformer.gameState = new GameScreen(platformer, inputManager, level);
    }
    if (inputManager.pressed(GameCommand.BUTTON_A)) {
      platformer.gameState = new HomeScreen(platformer, inputManager, level);
    }
  }
}
