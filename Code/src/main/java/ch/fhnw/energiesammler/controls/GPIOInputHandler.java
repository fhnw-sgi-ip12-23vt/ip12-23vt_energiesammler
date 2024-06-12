package ch.fhnw.energiesammler.controls;

import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;

/**
 * PI4J GPIO Handler Klasse.
 */
public class GPIOInputHandler {

  private final InputManager inputManager;
  private final Context pi4j;

  /**
   * Erstellung der verschiedenen GPIOs und Verknüpfung mit dem inputManager.
   *
   * @param inputManager inputManager des Spiels.
   */
  public GPIOInputHandler(InputManager inputManager) {
    this.inputManager = inputManager;

    // Erstellen des Pi4J-Kontextes
    pi4j = Pi4J.newAutoContext();

    // Konfiguration der GPIO-Pins
    // Buttons
    configureGPIO(GameCommand.BUTTON_A, "Button A (Grün)"); // Button A
    configureGPIO(GameCommand.BUTTON_B, "Button B (Blau)"); // Button B

    // Joystick
    configureGPIO(GameCommand.UP, "JoyStick UP");  // Pin für "Hoch"
    configureGPIO(GameCommand.DOWN, "JoyStick DOWN");  // Pin für "Runter"
    configureGPIO(GameCommand.RIGHT, "JoyStick RIGHT");  // Pin für "Rechts"
    configureGPIO(GameCommand.LEFT, "JoyStick LEFT");  // Pin für "Links"

  }

  /**
   * Konfiguration des GPIO.
   *
   * @param command  GameCommand um den Spielbefehl zu binden.
   * @param gpioName GPIO Nummer die mit der Hardware Verknüpft ist.
   */
  private void configureGPIO(GameCommand command, String gpioName) {
    DigitalInput gpioHardware = pi4j.create(DigitalInput.newConfigBuilder(pi4j)
        .id("GPIO" + command.getGPIO())
        .name(gpioName)
        .address(command.getGPIO())
        .pull(PullResistance.PULL_DOWN)
        .debounce(3000L)
        .provider("pigpio-digital-input"));

    gpioHardware.addListener(e -> inputManager.handleJoystickInput(command, e.state() == DigitalState.HIGH));
  }

  /**
   * Komformes schliessen des PI4J Contexts Um zukünftige Probleme zu vermeiden.
   */
  public void shutdown() {
    if (pi4j != null) {
      pi4j.shutdown();
    }
  }
}
