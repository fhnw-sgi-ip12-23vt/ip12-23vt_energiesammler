package ch.fhnw.energiesammler.controls;

import java.util.HashMap;
import java.util.Map;

import processing.core.PConstants;

/**
 * Alle mögliche Spiel-Befehle im Spiel definiert.
 */
public enum GameCommand {
  UP(PConstants.UP, 19),
  DOWN(PConstants.DOWN, 6),
  LEFT(PConstants.LEFT, 5),
  RIGHT(PConstants.RIGHT, 13),
  BUTTON_A(PConstants.ENTER, 23),
  BUTTON_B(32, 24);

  private static final Map<Integer, GameCommand> keyCodeToCommandMap = new HashMap<>();

  static {
    for (GameCommand command : GameCommand.values()) {
      keyCodeToCommandMap.put(command.getKeyBoard(), command);
    }
  }

  private final int keyboardKey;
  private final int gpioPin;

  /**
   * init Tastatur und GPIO.
   *
   * @param keyboardKey KeyCode der Tastatur.
   * @param gpioPin     PIN Nummer des GPIO.
   */
  GameCommand(int keyboardKey, int gpioPin) {
    this.keyboardKey = keyboardKey;
    this.gpioPin = gpioPin;
  }

  /**
   * Gibt den GameCommand zurück vom KeyCode der Tastatur.
   *
   * @param keyCode Tastatur Taste.
   * @return definierten GameCommand.
   */
  public static GameCommand fromKeyCode(int keyCode) {
    return keyCodeToCommandMap.get(keyCode);
  }

  public int getKeyBoard() {
    return keyboardKey;
  }

  public int getGPIO() {
    return gpioPin;
  }
}
