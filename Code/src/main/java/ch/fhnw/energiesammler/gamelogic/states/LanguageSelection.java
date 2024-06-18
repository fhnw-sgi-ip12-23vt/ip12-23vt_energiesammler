package ch.fhnw.energiesammler.gamelogic.states;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.fhnw.energiesammler.controls.GameCommand;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.ui.GameMenu;
import ch.fhnw.energiesammler.utils.TaskLocaleUtil;

/**
 * Game Zustand Sprachen Auswahl.
 * Hier wird die Sprache des Spiels dargetellt und behandelt.
 */
public class LanguageSelection extends GameStateBase {

  private final GameMenu gameMenu;
  private final Map<String, String> languageMap;
  private final List<String> languageCodeList;
  private int currentLanguagePointer;

  /**
   * Konstruktor des Game Zustand Sprach Auswahl.
   *
   * @param p Platformer Objekt.
   * @param i InputManager Objekt.
   * @param l Level Objekt.
   */
  public LanguageSelection(Platformer p, InputManager i, Level l) {
    super(p, i, l);
    gameMenu = p.gameMenu;
    currentLanguagePointer = 0;
    languageMap = TaskLocaleUtil.getResourceBundles("task", platformer.getGameLanguage());
    languageCodeList = new ArrayList<>(languageMap.keySet());
  }

  @Override
  public void draw() {
    gameMenu.displayLanguageSelection(languageMap.values().stream().toList(),
        currentLanguagePointer);
    inputManager.update();

  }

  @Override
  public void processInput() {
    // Implementierung für Language Screen Eingabelogik
    if (inputManager.pressed(GameCommand.UP)) {
      currentLanguagePointer =
          currentLanguagePointer > 0 ? currentLanguagePointer - 1 : languageCodeList.size() - 1;
    }
    if (inputManager.pressed(GameCommand.DOWN)) {
      currentLanguagePointer =
          currentLanguagePointer < languageCodeList.size() - 1 ? currentLanguagePointer + 1 : 0;
    }
    if (inputManager.pressed(GameCommand.BUTTON_B)) {
      platformer.setGameLanguage(languageCodeList.get(currentLanguagePointer));
      platformer.gameScreenSetup();
      platformer.gameState = new HomeScreen(platformer, inputManager, level);
    }
  }
}