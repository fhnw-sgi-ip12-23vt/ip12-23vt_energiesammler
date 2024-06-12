package ch.fhnw.energiesammler.gamelogic.states;

import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.ui.GameMenu;

public class HomeScreen extends GameStateBase {
  GameMenu gameMenu;
  GameState menuChoice;

  public HomeScreen(Platformer p, InputManager i, Level l) {
    super(p, i, l);
    gameMenu = p.gameMenu;
    menuChoice = GameState.CHARACTER_SCREEN;
  }

  @Override
  protected void setup() {
    platformer.homeScreenSetup();
  }

  @Override
  public void draw() {
    inputManager.update();
    gameMenu.displayMenu(menuChoice);
  }

  @Override
  public void processInput() {
    if (inputManager.pressed(GameCommand.DOWN) || inputManager.pressed(GameCommand.UP)) {
      if (menuChoice == GameState.HOME_SCREEN) {
        menuChoice = GameState.CHARACTER_SCREEN;
      } else if (menuChoice == GameState.LANGUAGE_SELECTION) {
        menuChoice = GameState.CHARACTER_SCREEN;
      } else if (menuChoice == GameState.CHARACTER_SCREEN) {
        menuChoice = GameState.LANGUAGE_SELECTION;
      }
    }

    //released weil der Prozess im gleichen FrameRate beide
    if (inputManager.pressed(GameCommand.BUTTON_A) || inputManager.pressed(GameCommand.BUTTON_B)) {
      if (menuChoice == GameState.LANGUAGE_SELECTION) {
        platformer.gameState = new LanguageSelection(platformer, inputManager, level);
      } else if (menuChoice == GameState.CHARACTER_SCREEN) {
        platformer.gameState = new CharacterScreen(platformer, inputManager, level);
      }
    }
  }
}