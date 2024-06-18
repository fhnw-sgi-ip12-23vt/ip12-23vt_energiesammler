package ch.fhnw.energiesammler.gamelogic.states;

import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;

public abstract class GameStateBase {
  protected Platformer platformer;
  protected InputManager inputManager;
  protected Level level;

  protected GameStateBase(Platformer p, InputManager i, Level l) {
    this.platformer = p;
    this.inputManager = i;
    this.level = l;

    inputManager.resetInputStates();
    this.setup();
  }

  protected void setup() {}

  public abstract void draw();

  public abstract void processInput();

}