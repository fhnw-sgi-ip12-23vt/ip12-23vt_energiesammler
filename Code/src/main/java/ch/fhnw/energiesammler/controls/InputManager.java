package ch.fhnw.energiesammler.controls;

import java.util.HashSet;
import java.util.Set;

/**
 * zentraInput Manger Klasse welchen den gesmaten Input des Spiels manged.
 */
public class InputManager {
  private final Set<GameCommand> holdCommands;
  private final Set<GameCommand> pressedCommands;
  private final Set<GameCommand> releasedCommands;

  /**
   * Default Konstruktor mit neuen Hashset Initialisierung von
   * hold, pressed, released Commands.
   */
  public InputManager() {
    holdCommands = new HashSet<>();
    pressedCommands = new HashSet<>();
    releasedCommands = new HashSet<>();
  }

  /**
   * KeyBoard Input Handler.
   *
   * @param keyCode   KeyCode von der Tastatur.
   * @param isPressed Ob KeyCode gedrückt ist.
   */
  public void handleKeyboardInput(int keyCode, boolean isPressed) {
    handleCommandEvents(GameCommand.fromKeyCode(keyCode), isPressed);
  }

  /**
   * GPIO (JoyStick und Buttons) Input Handler.
   *
   * @param command   GameCommand direkt empfangen.
   * @param isPressed Ob GameCommand gedrückt ist.
   */
  public void handleJoystickInput(GameCommand command, boolean isPressed) {
    handleCommandEvents(command, isPressed);
  }

  /**
   * Behandlung der GameCommands und der gängigen Events pressed, hold und released.
   *
   * @param command   GameCommand
   * @param isPressed Ob GameCommand gedrückt ist (Event)
   */
  private void handleCommandEvents(GameCommand command, boolean isPressed) {
    if (command != null) {

      if (isPressed) {
        if (!holdCommands.contains(command)) {
          pressedCommands.add(command);
        }
        holdCommands.add(command);
      } else {
        holdCommands.remove(command);
        releasedCommands.add(command);
      }
    }
  }

  /**
   * Überprüft, ob ein bestimmter GameCommand gerade gedrückt wurde.
   * Diese Methode sollte verwendet werden, um einmalige Aktionen auszulösen,
   * die nur im Moment des Tastendrucks erfolgen sollen.
   *
   * @param command Der GameCommand, dessen Status überprüft werden soll.
   * @return true, wenn der Command gerade gedrückt wurde, sonst false.
   */
  public boolean pressed(GameCommand command) {
    return pressedCommands.contains(command);
  }

  /**
   * Überprüft, ob ein bestimmter GameCommand aktuell gehalten wird.
   * Diese Methode eignet sich für kontinuierliche Aktionen, die ausgeführt werden sollen,
   * solange eine Taste gedrückt gehalten wird.
   *
   * @param command Der GameCommand, dessen Status überprüft werden soll.
   * @return true, wenn der Command gehalten wird, sonst false.
   */
  public boolean hold(GameCommand command) {
    return holdCommands.contains(command);
  }

  /**
   * Überprüft, ob ein bestimmter GameCommand gerade losgelassen wurde.
   * Diese Methode sollte verwendet werden, um Aktionen auszulösen, die im Moment des Loslassens
   * einer Taste erfolgen sollen. <br><br>
   * Wichtig: Aufgrund eines Timing-Problems mit GameState-Wechseln
   * kann es vorkommen, dass diese Methode direkt nach einem Wechsel des GameState
   * unerwartet true zurückgibt.
   *
   * @param command Der GameCommand, dessen Status überprüft werden soll.
   * @return true, wenn der Command gerade losgelassen wurde, sonst false.
   */
  public boolean released(GameCommand command) {
    return releasedCommands.contains(command);
  }

  /**
   * Aktualisiert den Zustand der GameCommands.
   * Diese Methode sollte einmal pro Frame aufgerufen werden,
   * um die Zustände von 'pressed' und 'released' zurückzusetzen.
   */
  public void update() {
    pressedCommands.clear();
    releasedCommands.clear();
  }

  /**
   * Setzt alle Eingabezustände zurück. Diese Methode kann beim Wechsel zwischen verschiedenen
   * GameStates verwendet werden, um sicherzustellen, dass keine unerwünschten Eingabezustände
   * von einem GameState zum nächsten übertragen werden.
   */
  public void resetInputStates() {
    pressedCommands.clear();
    holdCommands.clear();
    releasedCommands.clear();
  }

}
