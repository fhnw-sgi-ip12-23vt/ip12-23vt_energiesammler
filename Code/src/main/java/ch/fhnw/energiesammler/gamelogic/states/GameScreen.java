package ch.fhnw.energiesammler.gamelogic.states;

import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;

/**
 * Logik für anzeigen und Input des Spiel Bildschirms.
 */
public class GameScreen extends GameStateBase {
  GameState menuChoice;

  public GameScreen(Platformer p, InputManager i, Level l) {
    super(p, i, l);
    menuChoice = GameState.CONTINUE;
  }

  @Override
  protected void setup() {
    platformer.gameScreenSetup();
  }

  @Override
  public void draw() {
    inputManager.update();
    if (platformer.isPaused) {
      platformer.drawPauseScreen(menuChoice);
    } else {
      platformer.drawGameScreen();
    }
  }

  @Override
  public void processInput() {
    if (platformer.drawer.getTutorial()) {
      if (inputManager.pressed(GameCommand.BUTTON_B)) {
        platformer.drawer.increaseTutorialScreen();
      }
      if (inputManager.pressed(GameCommand.BUTTON_A)) {
        platformer.drawer.decreaseTutorialScreen();
      }
    } else if (!platformer.getUpdater().isGameOver) {
      // Pause
      if (platformer.isPaused) {
        if (inputManager.pressed(GameCommand.DOWN) || inputManager.pressed(GameCommand.UP)) {
          if (menuChoice == GameState.HOME_SCREEN) {
            menuChoice = GameState.CONTINUE;
          } else if (menuChoice == GameState.CONTINUE) {
            menuChoice = GameState.HOME_SCREEN;
          }
        }

        if (inputManager.pressed(GameCommand.BUTTON_A) || inputManager.pressed(GameCommand.BUTTON_B)) {
          if (menuChoice == GameState.HOME_SCREEN) {
            platformer.gameState = new HomeScreen(platformer, inputManager, level);
          }
          platformer.isPaused = false;
        }

        // Spiel spielen
      } else {
        if (inputManager.pressed(GameCommand.RIGHT)) {
          platformer.movePlayerX(1);
        }
        if (inputManager.pressed(GameCommand.LEFT)) {
          platformer.movePlayerX(-1);
        }
        if (inputManager.pressed(GameCommand.UP)) {
          if (platformer.getUpdater().isInFrontOfDoor() & platformer.getUpdater().isOnPlatform(platformer.getPlayer(),
              platformer.level.getPlatforms())) {
            platformer.getUpdater().enterDoor();
          }
        }

        if (inputManager.pressed(GameCommand.BUTTON_B)) {
          if (platformer.getUpdater().isOnPlatform(platformer.getPlayer(),
              platformer.level.getPlatforms())) {
            platformer.movePlayerY(-1);
          }
        }

        if (inputManager.released(GameCommand.RIGHT)) {
          platformer.movePlayerX(0);
        }
        if (inputManager.released(GameCommand.LEFT)) {
          platformer.movePlayerX(0);
        }

        if (inputManager.pressed(GameCommand.BUTTON_A)) {
          platformer.pauseScreenSetup();
          platformer.isPaused = true;
        }
      }

    } else if (platformer.getUpdater().isGameOver) {
      if (inputManager.pressed(GameCommand.BUTTON_A) || inputManager.pressed(GameCommand.BUTTON_B)) {
        platformer.gameState = new EndScreen(platformer, inputManager, level);
      }
    }
  }
}