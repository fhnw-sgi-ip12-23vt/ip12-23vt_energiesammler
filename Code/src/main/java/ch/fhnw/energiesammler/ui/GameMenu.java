package ch.fhnw.energiesammler.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.entities.Door;
import ch.fhnw.energiesammler.entities.Enemy;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.gamelogic.states.GameState;
import ch.fhnw.energiesammler.utils.TextEmLocale;
import ch.fhnw.energiesammler.utils.TextLocale;

import processing.core.PApplet;
import processing.core.PConstants;

public class GameMenu {
  private float textMid;
  private float textSmall;
  private float textBig;
  PApplet pApplet;
  Platformer platformer;
  TextLocale textLocale;
  TextEmLocale emLocale;

  public GameMenu(Platformer platformer, TextLocale textLocale, TextEmLocale emLocale) {
    this.pApplet = platformer;
    this.platformer = platformer;
    this.textLocale = textLocale;
    this.emLocale = emLocale;
    textSmall = AppConfig.getValue("textSmall", 20f);
    textMid = AppConfig.getValue("textMiddle", 50f);
    textBig = AppConfig.getValue("textBig", 120f);
  }

  public void displayFPS(String text, float x, float y) {
    platformer.strokeWeight(0);
    platformer.fill(255);
    platformer.textSize(textSmall);
    platformer.textAlign(0);
    platformer.text(text, x, y);
  }

  public void displayBatteryBlank(float a, float b, float c, float d) {
    platformer.strokeWeight(0);
    platformer.fill(255);
    platformer.rect(a, b, c, d);
  }

  public void displayBatteryCollected(float a, float b, float c, float d) {
    platformer.strokeWeight(0);
    platformer.fill(0, 255, 0);
    platformer.rect(a, b, c, d);
  }

  public void displayBatteryText(String text, float x, float y) {
    platformer.fill(0);
    platformer.textSize(textSmall);
    platformer.textAlign(PConstants.CENTER, PConstants.CENTER);
    platformer.text(text, x, y);
  }

  public void displayCollectEnergy(String text, float x, float y) {
    platformer.textSize(textSmall);
    platformer.fill(0, 255, 0);
    platformer.textAlign(3);
    platformer.text(text, x, y);
  }

  public void displayRemoveEnergy(String text, float x, float y) {
    platformer.textSize(textSmall);
    platformer.fill(255, 0, 0);
    platformer.textAlign(3);
    platformer.text(text, x, y);
  }

  public void displayDoorText(String text, float x, float y) {
    // Maximal zulässige Zeichen pro Zeile
    int maxCharsPerLine = 30;

    // Den Text in mehrere Zeilen aufteilen
    String[] lines = splitTextIntoLines(text, maxCharsPerLine);

    // Hintergrundrechtecke für jede Zeile zeichnen
    platformer.fill(255, 255, 255);
    platformer.rectMode(3);
    for (int i = 0; i < lines.length; i++) {
      platformer.rect(x, (float) (y - 8 + (i * textSmall * 1.2)), lines[i].length() * 12,
          textSmall);
    }

    // Text zeilenweise zeichnen
    platformer.textSize(textSmall);
    platformer.fill(0, 0, 0);
    platformer.textAlign(3);
    for (int i = 0; i < lines.length; i++) {
      platformer.text(lines[i], x, (float) (y + (i * textSmall * 1.2)));
    }

    platformer.rectMode(0);
  }

  // Methode, um den Text in mehrere Zeilen aufzuteilen
  private String[] splitTextIntoLines(String text, int maxCharsPerLine) {
    List<String> lines = new ArrayList<>();
    int length = text.length();
    int start = 0;

    while (start < length) {
      int end = Math.min(length, start + maxCharsPerLine);

      // Wenn das Ende des Textes erreicht ist oder der nächste Charakter nach dem Endpunkt kein Leerzeichen ist
      if (end == length || text.charAt(end) == ' ') {
        lines.add(text.substring(start, end).trim());
        start = end + 1; // Startpunkt für die nächste Zeile, nach dem Leerzeichen
      } else {
        // Suchen Sie das letzte Leerzeichen innerhalb der maximalen Zeichenanzahl
        int lastSpace = text.lastIndexOf(' ', end);
        if (lastSpace > start) {
          lines.add(text.substring(start, lastSpace).trim());
          start = lastSpace + 1; // Startpunkt für die nächste Zeile, nach dem Leerzeichen
        } else {
          // Kein Leerzeichen gefunden, Wort wird auf die nächste Zeile übertragen
          lines.add(text.substring(start, end).trim());
          start = end; // Startpunkt für die nächste Zeile
        }
      }
    }

    return lines.toArray(new String[0]);
  }

  /**
   * Displays the game Over Screen.
   */
  public void displayGameOver() {
    platformer.textAlign(0);
    platformer.fill(0, 187);
    platformer.rect(platformer.view_x, platformer.view_y, platformer.displayWidth,
        platformer.displayHeight);
    platformer.fill(255);
    platformer.textSize(textBig);
    platformer.text(textLocale.getText("gameOver"), platformer.view_x + 40,
        platformer.view_y + 220);
    platformer.textSize(textMid);
    platformer.text(textLocale.getText("pressButton"), platformer.view_x + 40,
        platformer.view_y + 320);
  }

  /**
   * Displays the first EndScreen with the gate the player went through.
   */
  public void displayDoorBalance() {
    platformer.fill(0);
    platformer.background(255);
    platformer.textSize(textMid);
    platformer.textAlign(PApplet.CENTER, PApplet.CENTER);
    platformer.text(textLocale.getText("balance"), platformer.displayWidth / 2f,
        platformer.displayHeight / 8f);

    //Button Info
    buttonBlueInfo();

    platformer.textSize(textSmall);
    platformer.textAlign(PApplet.LEFT, PApplet.TOP);

    int charPerLine = 25;
    int cols = Platformer.DOORCOUNTPERLEVEL; // Anzahl der Spalten
    int rows = platformer.getAllDoors().size() / cols; // Anzahl der Zeilen
    float colMiddleWidthSpace = (float) platformer.displayWidth / cols;
    float colEdgeWidthSpace =
        (platformer.displayWidth - (textSmall * charPerLine + colMiddleWidthSpace / 5)) / 4;
    float maxY = (float) platformer.displayHeight / 4;

    // Gewählte Türen anzeigen
    if (!platformer.getAllDoors().isEmpty()) {
      for (int row = 0; row < rows; row++) {
        float maxRowHeight = 0;

        for (int col = 0; col < cols; col++) {
          int i = row * cols + col;
          if (i < platformer.getAllDoors().size()) {
            Door door = platformer.getAllDoors().get(i);
            float x = colEdgeWidthSpace + (colMiddleWidthSpace * col);
            float y = maxY;

            if (!platformer.getUsedDoors().isEmpty() && platformer.getUsedDoors().size() >= row + 1) {
              if (platformer.getUsedDoors().contains(door)) {
                platformer.fill(0); // Schwarz
              } else {
                platformer.fill(150, 150, 150); // Grau
              }

              float yOffset = drawWrappedText(door.getText(), x, y, charPerLine);
              // Türen Kosten Farben definieren
              Door sameColDoor = col == 0 ? platformer.getAllDoors().get(i + 1) : platformer.getAllDoors().get(i - 1);
              if (door.getCost() < sameColDoor.getCost()) {
                platformer.fill(0, 255, 0); // Grün
              } else if (door.getCost() > sameColDoor.getCost()) {
                platformer.fill(255, 0, 0); // Rot
              } else {
                platformer.fill(0); // Schwarz
              }

              platformer.text(door.getCost() + " kWh", x + colMiddleWidthSpace / 3, y + yOffset);

              if (yOffset > maxRowHeight) {
                maxRowHeight = yOffset;
              }
            } else {
              platformer.fill(0); // Schwarz
              platformer.text("???", x, y);
            }
          }
        }

        maxY += maxRowHeight + textSmall; // Add a margin between rows
      }
    }
  }

  public void displayTakeHomeMessage() {
    platformer.fill(0);
    platformer.background(255);
    platformer.textSize(textMid);
    platformer.textAlign(PApplet.CENTER, PApplet.CENTER);
    platformer.text(textLocale.getText("endMessage"), platformer.displayWidth / 2f,
        platformer.displayHeight / 8f);
    platformer.textSize(textSmall);
    drawWrappedText(emLocale.getText("em1"), platformer.displayWidth / 2f,
        platformer.displayHeight / 8f + 150, 35);

    //Button Info
    showBothButtons();

    platformer.textSize(textSmall);
    platformer.textAlign(3);

  }

  private float drawWrappedText(String text, float x, float y, int maxCharsPerLine) {
    platformer.textSize(textSmall);
    String[] words = text.split(" ");
    StringBuilder line = new StringBuilder();
    float yOffset = textSmall;

    for (String word : words) {
      if (line.length() + word.length() + 1 > maxCharsPerLine) {
        platformer.text(line.toString(), x, y + yOffset);
        line = new StringBuilder();
        yOffset += textSmall; // Höhe einer Textzeile, kann je nach Schriftgröße angepasst werden
      }
      if (line.length() > 0) {
        line.append(" ");
      }
      line.append(word);
    }
    // Letzte Zeile zeichnen
    if (line.length() > 0) {
      platformer.text(line.toString(), x, y + yOffset);
      yOffset += textSmall; // Hinzufügen der Höhe der letzten Zeile
    }
    return yOffset; // Rückgabe der neuen Y-Offset Position
  }

  /**
   * Displays the Second EndScreen with the collected energie.
   */
  public void displayScore() {
    // Header
    platformer.fill(0);
    platformer.background(255);
    platformer.textSize(textMid);
    platformer.textAlign(PApplet.CENTER, PApplet.CENTER);
    platformer.text(textLocale.getText("highScore"), platformer.displayWidth / 2f,
        platformer.displayHeight / 8f);

    //Button Info
    showBothButtons();

    platformer.textSize(textSmall);
    platformer.textAlign(3);

    // Bilanz ausrechnen
    String bilanz = "";
    for (Map.Entry<Enemy, Integer> enemy : platformer.getEnemiesTurnedOff().entrySet()) {
      bilanz += enemy.getValue() + " " + textLocale.getText(enemy.getKey().getName()) + " "
          + textLocale.getText("turnedOff") + " -> " + enemy.getKey().getDamage() * (-1) + "kWh \n";
    }
    bilanz += "-----------------------------------------------------------\n";
    bilanz += "                               "
        + platformer.getDf().format(platformer.drawer.getDisplayer().highscore) + " kWh";
    platformer.text(bilanz, platformer.displayWidth / 2f, platformer.displayHeight / 4f);

    // Ziel Energie ausrechnen
    platformer.text(textLocale.getText("energyDelivered"), platformer.displayWidth / 2f,
        platformer.displayHeight / 2f);
    if (platformer.getUpdater().collectedEnergy < 0) {
      platformer.getUpdater().collectedEnergy = 0;
    }
    String exitedTheLevelWith =
        "+ " + platformer.getDf().format(platformer.getUpdater().collectedEnergy) + " kWh\n";
    platformer.text(exitedTheLevelWith, platformer.displayWidth / 2f,
        (platformer.displayHeight / 2f) + 25);

    // End-Score ausrechnen
    String score = textLocale.getText("score") + " "
        + platformer.getDf().format(platformer.getUpdater().collectedEnergy
            + platformer.drawer.getDisplayer().highscore) + " kWh";
    platformer.textSize(textMid);
    platformer.text(score, platformer.displayWidth / 2f,
        platformer.displayHeight * 3f / 4 - 10);
  }

  private void showBothButtons() {
    buttonBlueInfo();
    buttonGreenInfo();
  }

  private void buttonBlueInfo() {
    pApplet.fill(0, 0, 255);
    pApplet.circle(pApplet.displayWidth - 200, pApplet.displayHeight - 100, 40);
    pApplet.fill(0);
    platformer.textSize(textSmall);
    platformer.textAlign(PConstants.LEFT, PConstants.CENTER);
    platformer.text(textLocale.getText("next"), pApplet.displayWidth - 170,
            pApplet.displayHeight - 105);
    platformer.textSize(textMid);
  }

  private void buttonGreenInfo() {
    //Buttons
    pApplet.fill(0, 255, 0);
    pApplet.circle(pApplet.displayWidth - 200, pApplet.displayHeight - 50, 40);
    pApplet.fill(0);
    platformer.textSize(textSmall);
    platformer.textAlign(PConstants.LEFT, PConstants.CENTER);
    platformer.text(textLocale.getText("back"), pApplet.displayWidth - 170,
            pApplet.displayHeight - 55);
  }


  public void displayMenu(GameState menuState) {
    platformer.drawer.drawHomeScreenMenuBar();

    pApplet.textSize(textMid);
    pApplet.textAlign(PConstants.CENTER, PConstants.CENTER);

    switch (menuState) {
      case CHARACTER_SCREEN -> {
        menuGameStart();
      }
      case LANGUAGE_SELECTION -> {
        menuLanguage();
      }
      default -> {
        menuGameStart();
      }
    }
  }


  private void menuGameStart() {
    pApplet.fill(57, 244, 51);
    pApplet.text(textLocale.getText("play"), platformer.menuBarLanguage.center_x,
        platformer.menuBarPlay.center_y);
    pApplet.fill(0);
    pApplet.text(textLocale.getText("language"), platformer.menuBarLanguage.center_x,
        platformer.menuBarLanguage.center_y);
    platformer.menuBarPlay.choose();
    platformer.menuBarLanguage.unchoose();
  }

  private void menuLanguage() {
    pApplet.fill(0);
    pApplet.text(textLocale.getText("play"), platformer.menuBarLanguage.center_x,
        platformer.menuBarPlay.center_y);
    pApplet.fill(57, 244, 51);
    pApplet.text(textLocale.getText("language"), platformer.menuBarLanguage.center_x,
        platformer.menuBarLanguage.center_y);
    platformer.menuBarLanguage.choose();
    platformer.menuBarPlay.unchoose();
  }

  /**
   * Darstellung des SprachsMenu.
   *
   * @param languages Liste aller verfügbaren Sprachen.
   * @param cur       Aktuelle Sprachwahl im Menu.
   */
  public void displayLanguageSelection(List<String> languages, int cur) {
    platformer.drawHomeScreen();

    int size = languages.size();
    for (int i = 0; i < size; i++) {
      pApplet.fill(0);
      pApplet.textSize(textMid);
      if (i == cur) {
        pApplet.fill(0, 255, 0);
        pApplet.textSize(textMid);
      }
      pApplet.text(languages.get(i), pApplet.displayWidth / 2,
          (i + size) * (pApplet.displayHeight / (size + 5)));
    }
  }
}
