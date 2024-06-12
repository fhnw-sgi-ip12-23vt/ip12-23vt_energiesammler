package ch.fhnw.energiesammler.controls;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;



/**
 * Testing InputManager methods if they behave correctly.
 */
@RunWith(Parameterized.class)
public class InputManagerTest {

  private final GameCommand command;
  private InputManager inputManager;

  public InputManagerTest(GameCommand command) {
    this.command = command;
  }

  /**
   * All GameCommands combined.
   *
   * @return Array with all GameCommand values
   */
  @Parameterized.Parameters(name = "{index}: Test with GameCommand={0}")
  public static Collection<Object[]> data() {
    return Arrays.stream(GameCommand.values())
        .map(command -> new Object[] {command})
        .toList();
  }

  @Before
  public void setUp() {
    inputManager = new InputManager();
  }


  @Test
  public void testInputManagerCreation() {
    assertNotNull(inputManager);
  }

  @Test
  public void testKeyPressedAndHeld() {
    inputManager.handleKeyboardInput(command.getKeyBoard(), true);
    assertTrue(command.name() + " should be pressed", inputManager.pressed(command));
    assertTrue(command.name() + " should be held", inputManager.hold(command));
    inputManager.update();
    assertFalse(command.name() + " pressed state should reset", inputManager.pressed(command));
    assertTrue(command.name() + " should still be held after update",
        inputManager.hold(command));
  }

  @Test
  public void testKeyReleased() {
    inputManager.handleKeyboardInput(command.getKeyBoard(), true);
    inputManager.handleKeyboardInput(command.getKeyBoard(), false);
    assertFalse(command.name() + " should not be held", inputManager.hold(command));
    assertTrue(command.name() + " should be released", inputManager.released(command));
    inputManager.update();
    assertFalse(command.name() + " released state should reset",
        inputManager.released(command));
  }

  @Test
  public void testResetInputStates() {
    inputManager.handleKeyboardInput(command.getKeyBoard(), true);
    inputManager.handleKeyboardInput(command.getKeyBoard(), false);
    inputManager.resetInputStates();
    assertFalse("All commands should be reset", inputManager.pressed(command));
    assertFalse("All commands should be reset", inputManager.hold(command));
    assertFalse("All commands should be reset", inputManager.released(command));
  }

  @Test
  public void testSimultaneousCommands() {
    inputManager.handleKeyboardInput(GameCommand.UP.getKeyBoard(), true);
    inputManager.handleKeyboardInput(GameCommand.DOWN.getKeyBoard(), true);

    assertTrue("UP should be pressed", inputManager.pressed(GameCommand.UP));
    assertTrue("DOWN should be pressed", inputManager.pressed(GameCommand.DOWN));

    assertTrue("UP should be held", inputManager.hold(GameCommand.UP));
    assertTrue("DOWN should be held", inputManager.hold(GameCommand.DOWN));

    inputManager.update();

    assertFalse("UP pressed state should reset", inputManager.pressed(GameCommand.UP));
    assertFalse("DOWN pressed state should reset", inputManager.pressed(GameCommand.DOWN));

    assertTrue("UP should still be held after update", inputManager.hold(GameCommand.UP));
    assertTrue("DOWN should still be held after update", inputManager.hold(GameCommand.DOWN));
  }

  @Test
  public void testGPIOPressedAndHeld() {
    inputManager.handleJoystickInput(command, true);
    assertTrue(command.name() + " should be pressed", inputManager.pressed(command));
    assertTrue(command.name() + " should be held", inputManager.hold(command));
    inputManager.update();
    assertFalse(command.name() + " pressed state should reset", inputManager.pressed(command));
    assertTrue(command.name() + " should still be held after update",
        inputManager.hold(command));
  }

  @Test
  public void testGPIOReleased() {
    inputManager.handleJoystickInput(command, true);
    inputManager.handleJoystickInput(command, false);
    assertFalse(command.name() + " should not be held", inputManager.hold(command));
    assertTrue(command.name() + " should be released", inputManager.released(command));
    inputManager.update();
    assertFalse(command.name() + " released state should reset",
        inputManager.released(command));
  }
}
