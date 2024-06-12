package ch.fhnw.energiesammler.ui;

import ch.fhnw.energiesammler.AppConfig;
import ch.fhnw.energiesammler.controls.InputManager;
import ch.fhnw.energiesammler.gamelogic.GameLogic;
import ch.fhnw.energiesammler.gamelogic.Level;
import ch.fhnw.energiesammler.gamelogic.Pictures;
import ch.fhnw.energiesammler.gamelogic.Platformer;
import ch.fhnw.energiesammler.gamelogic.states.GameState;
import ch.fhnw.energiesammler.gamelogic.states.GameStateBase;
import ch.fhnw.energiesammler.utils.TextLocale;

import processing.core.PApplet;
import processing.core.PConstants;

/**
 * Zeichnen des Spiels.
 */
public class Drawer {
  PApplet pApplet;
  Platformer platformer;
  Level level;
  Pictures pictures;
  GameLogic gameLogic;
  Displayer displayer;
  TextLocale textLocale;
  boolean tutorial;
  int tutorialScreen;
  int numbersTutorial;
  private float textSmall;
  private float textMid;
  private float textBig;

  /**
   * Konstruktor für Drawer.
   *
   * @param pApplet    PApplet zum darstellen.
   * @param platformer Platformer für standart Elemente.
   * @param pictures   Pictures für Bilder.
   */
  public Drawer(PApplet pApplet, Platformer platformer, Level level, Pictures pictures,
                TextLocale textLocale) {
    this.pApplet = pApplet;
    this.platformer = platformer;
    this.level = level;
    this.pictures = pictures;
    this.textLocale = textLocale;

    this.displayer = new Displayer(this.pApplet, this.platformer, this.level, this);
    this.gameLogic = new GameLogic(this.platformer, this.pApplet, this.level, this);
    this.tutorial = true;
    this.tutorialScreen = 1;
    this.numbersTutorial = 4;
    textSmall = AppConfig.getValue("textSmall", 20f);
    textMid = AppConfig.getValue("textMiddle", 50f);
    textBig = AppConfig.getValue("textBig", 120f);
  }

  public void draw(GameStateBase gameState) {
    gameState.processInput();
    gameState.draw();
  }

  /**
   * Bildschirm für Spiel zeichnen.
   */
  public void drawGameScreen() {
    if (tutorial) {
      drawTutorial();
    } else {
      pApplet.background(pictures.background);

      gameLogic.scroll();

      displayer.displayAll();
      gameLogic.updateAll();

      if (!gameLogic.isGameOver) {
        gameLogic.collectEnergy();
        gameLogic.collectBattery();
        gameLogic.fallOffCliff();
      }
    }
  }

  private void drawTutorial() {
    switch (tutorialScreen) {
      case 1:
        pApplet.background(pictures.tutorial1);
        break;
      case 2:
        pApplet.background(pictures.tutorial2);
        break;
      case 3:
        pApplet.background(pictures.tutorial3);
        break;
      case 4:
        pApplet.background(pictures.tutorial4);
        break;
      default:
        tutorial = false;
        break;
    }

    int i = 0;
    int whitespace = 25;
    int spaceLeft = pApplet.displayWidth / 2 - ((numbersTutorial / 2) * whitespace
        + ((numbersTutorial / 2) - 1) * whitespace + (numbersTutorial % 2) * whitespace);
    pApplet.strokeWeight(1);
    pApplet.fill(0, 255, 0);
    while (i < tutorialScreen) {
      pApplet.circle(spaceLeft + (2 * i) * whitespace, pApplet.displayHeight - 100, whitespace);
      i++;
    }
    pApplet.fill(255);
    while (i < numbersTutorial) {
      pApplet.circle(spaceLeft + (2 * i) * whitespace, pApplet.displayHeight - 100, whitespace);
      i++;
    }

    drawBlueButton();

    if (tutorialScreen >= 2) {
      drawGreenButton();
    }
  }

  private void drawGreenButton() {
    pApplet.fill(0, 255, 0);
    pApplet.circle(pApplet.displayWidth - 200, pApplet.displayHeight - 50, 40);
    pApplet.fill(0);
    platformer.textSize(textSmall);
    platformer.textAlign(PConstants.LEFT, PConstants.CENTER);
    platformer.text(textLocale.getText("back"), pApplet.displayWidth - 170,
        pApplet.displayHeight - 55);
  }

  private void drawBlueButton() {
    pApplet.fill(0, 0, 255);
    pApplet.circle(pApplet.displayWidth - 200, pApplet.displayHeight - 100, 40);
    pApplet.fill(0);
    platformer.textSize(textSmall);
    platformer.textAlign(PConstants.LEFT, PConstants.CENTER);
    platformer.text(textLocale.getText("next"), pApplet.displayWidth - 170,
        pApplet.displayHeight - 105);
  }

  /**
   * Draw the HomeScreen.
   */
  public void drawHomeScreen() {
    pApplet.background(pictures.menuBackground);
  }

  public void drawHomeScreenMenuBar() {
    drawHomeScreen();
    platformer.menuBarPlay.display();
    platformer.menuBarLanguage.display();
  }

  public void drawPauseScreen(GameState menuState) {
    pApplet.textSize(textMid);
    pApplet.textAlign(PConstants.CENTER, PConstants.CENTER);
    platformer.menuBarHomeScreen.display();
    platformer.menuBarContinue.display();

    switch (menuState) {
      case HOME_SCREEN -> {
        menuHomeScreen();
      }
      case CONTINUE -> {
        menuContinue();
      }
      default -> {
        menuHomeScreen();
      }
    }
  }

  private void menuContinue() {
    pApplet.fill(0);
    pApplet.text(textLocale.getText("mainMenu"), platformer.menuBarHomeScreen.center_x,
        platformer.menuBarHomeScreen.center_y);
    pApplet.fill(57, 244, 51);
    pApplet.text(textLocale.getText("continue"), platformer.menuBarContinue.center_x,
        platformer.menuBarContinue.center_y);
    platformer.menuBarHomeScreen.unchoose();
    platformer.menuBarContinue.choose();
  }

  private void menuHomeScreen() {
    pApplet.fill(57, 244, 51);
    pApplet.text(textLocale.getText("mainMenu"), platformer.menuBarHomeScreen.center_x,
        platformer.menuBarHomeScreen.center_y);
    pApplet.fill(0);
    pApplet.text(textLocale.getText("continue"), platformer.menuBarContinue.center_x,
        platformer.menuBarContinue.center_y);
    platformer.menuBarContinue.unchoose();
    platformer.menuBarHomeScreen.choose();
  }

  public void drawEndScreen(InputManager inputManager) {
    displayer.displayEndScreen(inputManager);
  }

  public GameLogic getUpdater() {
    return gameLogic;
  }

  public boolean getTutorial() {
    return tutorial;
  }

  /**
   * Nächster Tutorial Screen anzeigen.
   */
  public void increaseTutorialScreen() {
    tutorialScreen++;
    if (tutorialScreen >= 5) {
      tutorial = false;
    }
  }

  /**
   * Vorherigen Tutorial Screen anzeigen.
   */
  public void decreaseTutorialScreen() {
    if (tutorialScreen >= 2) {
      tutorialScreen--;
    }
  }

  public Displayer getDisplayer() {
    return displayer;
  }

  public void drawCharacterScreen() {
    pApplet.background(pictures.backgroundCharacterauswahl);
    if (platformer.characterChoice) {
      platformer.menuMale.choose();
      platformer.menuFemale.unchoose();
    } else {
      platformer.menuMale.unchoose();
      platformer.menuFemale.choose();
    }
    platformer.menuMale.display();
    platformer.menuFemale.display();
    platformer.fill(63, 154, 19);
    platformer.textAlign(PConstants.CENTER, PConstants.CENTER);
    platformer.textSize(textMid);
    platformer.text(textLocale.getText("selectCharacter"), (float) platformer.displayWidth / 2,
        1 * ((float) platformer.displayHeight / 6));
    platformer.fill(253, 35, 25);
    drawBlueButton();
    drawGreenButton();
  }
}
