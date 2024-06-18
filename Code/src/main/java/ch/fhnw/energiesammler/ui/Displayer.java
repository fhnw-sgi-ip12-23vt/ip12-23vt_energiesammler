package ch.fhnw.energiesammler.ui;

import java.util.ArrayList;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.entities.AnimatedSprite;
import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Sprite;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.gamelogic.states.EndScreen;

import processing.core.PApplet;

/**
 * Darstellen der verschiedenen Spielelemente.
 */
public class Displayer {
  private final ArrayList<Sprite> batteries;
  private final PApplet pApplet;
  private final Platformer platformer;
  private final Level level;
  private final Drawer drawer;
  public float highscore;
  private float textSmall;
  private float textMid;
  private float textBig;


  /**
   * Konstruktor für Displayer.
   *
   * @param pApplet    pApplet zum darstellen.
   * @param platformer platformer für standart Elemente.
   * @param level      Level für Elemente im Level.
   * @param drawer     Drawer zum zeichnen der Elemente.
   */
  public Displayer(PApplet pApplet, Platformer platformer, Level level, Drawer drawer) {
    this.pApplet = pApplet;
    this.platformer = platformer;
    this.level = level;
    this.drawer = drawer;
    batteries = new ArrayList<>();
    addBattery();
    textSmall = AppConfig.getValue("textSmall", 20f);
    textMid = AppConfig.getValue("textMiddle", 50f);
    textBig = AppConfig.getValue("textBig", 120f);
  }

  /**
   * Alle Elemente darstellen.
   */
  public void displayAll() {
    displayEntities();
    platformer.getPlayer().display();
    platformer.textSize(textMid);
    platformer.textAlign(platformer.RIGHT, platformer.CENTER);
    String score = String.valueOf(platformer.getDf().format(highscore));
    String scorePrefix = "00";
    if (score.length() < 4) {
      scorePrefix += "0";
    }
    platformer.text(scorePrefix + score, platformer.view_x + platformer.displayWidth - 50,
        platformer.view_y + 50);

    platformer.textSize(textSmall);
    if (drawer.getUpdater().isGameOver) {
      platformer.getPlayer().change_x = 0;
      platformer.gameMenu.displayGameOver();
    }
    if (drawer.getUpdater().displayLostWinEnergy) {
      drawer.getUpdater().energyGravity += 3;

      if (drawer.getUpdater().getEnergy > 0) {
        platformer.gameMenu.displayCollectEnergy("+" + drawer.getUpdater().getEnergy + "kWh",
            platformer.getPlayer().center_x,
            platformer.getPlayer().getTop() - (20 + drawer.getUpdater().energyGravity));
      } else {
        platformer.gameMenu.displayRemoveEnergy(drawer.getUpdater().getEnergy + "kWh",
            platformer.getPlayer().center_x,
            platformer.getPlayer().getTop() - (20 + drawer.getUpdater().energyGravity));
      }

      if (pApplet.millis() > drawer.getUpdater().timeSecond + 1000) {
        drawer.getUpdater().displayLostWinEnergy = false;
      }
    }
    if (drawer.getUpdater().isInFrontOfDoor()) {
      Door currentDoor =
          (Door) drawer.getUpdater().checkCollisionList(platformer.getPlayer(), level.levelDoors)
              .get(0);
      platformer.gameMenu.displayDoorText(currentDoor.getText(), currentDoor.center_x,
          currentDoor.getTop() - 20);
    }
  }

  /**
   * Alle Entitäten im Level darstellen.
   */
  public void displayEntities() {
    for (Sprite h : level.allHouses) {
      h.display();
    }
    for (Sprite d : level.levelDoors) {
      d.display();
    }
    for (Sprite e : level.enemies) {
      e.display();
    }
    for (Sprite b : level.batteries) {
      b.display();
    }
    fillUpBattery();
    for (Sprite s : level.getPlatforms()) {
      s.display();
    }
    for (Sprite es : level.getEnergySources()) {
      es.display();
      ((AnimatedSprite) es).updateAnimation();
    }
  }

  public void displayEndScreen(InputManager inputManager) {
    platformer.gameState = new EndScreen(platformer, inputManager, level);
  }

  /**
   * Adding the High score.
   *
   * @param points wir zu dem Oben angezeigten high-score dazugerechnet.
   */
  public void addToHighscore(float points) {
    highscore += points;
  }

  private void fillUpBattery() {

    //Positionen der Batterien setzen
    for (int i = 0; i < batteries.size(); i++) {
      batteries.get(i).center_x = platformer.view_x + 60 + 60 * (2 * i);
      batteries.get(i).center_y = platformer.view_y + 60;
      platformer.gameMenu.displayBatteryBlank(batteries.get(i).getLeft() + 6,
          batteries.get(i).getTop() + 6, 94, batteries.get(i).getH() - 6);
    }

    int i = 0;

    //Volle Batterien füllen
    while (i < (int) drawer.getUpdater().collectedEnergy / drawer.getUpdater().batterySize) {
      platformer.gameMenu.displayBatteryCollected(batteries.get(i).getLeft() + 6,
          batteries.get(i).getTop() + 6, 94, batteries.get(i).getH() - 6);
      i++;
    }

    //Letzte nicht volle Batterie Fülle anzeigen
    if (i < batteries.size()) {
      platformer.gameMenu.displayBatteryCollected(batteries.get(i).getLeft() + 6,
          batteries.get(i).getTop() + 6,
          ((drawer.getUpdater().collectedEnergy - i * drawer.getUpdater().batterySize)
              / (float) drawer.getUpdater().batterySize) * 90f, batteries.get(i).getH() - 6);
    }

    //Alle Batterien darstellen
    for (Sprite battery : batteries) {
      battery.display();
    }

    //Energie als Text anzeigen
    platformer.gameMenu.displayBatteryText(
        String.valueOf(platformer.getDf().format(drawer.getUpdater().collectedEnergy)),
        batteries.get(0).center_x, batteries.get(0).center_y - 10);

    platformer.gameMenu.displayBatteryText("kWh", batteries.get(0).center_x,
        batteries.get(0).center_y + 10);

  }

  /**
   * Neue Batterie zur Liste hinzufügen.
   */
  public void addBattery() {
    batteries.add(new Sprite(pApplet, "energySource/battery.png", 1f, 30, 30));
  }
}
