# Entwicklungsanleitung Energiesammler

## Einrichtung

### Materialliste
* Raspberry Pi
* Micro SD-Karte, mindestens 16Gb
* PC mit einem SD-Kartensteckplatz (eventuell benötigen Sie einen SD-Kartenadapter)
* Stromversorgung (5V, 2 oder 3A)
* Monitor, Tastatur, Maus

### Schritt 1: Raspberry Pi vorbereiten [20min]
*	[Raspberry Pi Imager](https://www.raspberrypi.org/blog/raspberry-pi-imager-imaging-utility/) herunterladen und installieren
*	[Pi4J-Basic-OS](https://pi4j-download.com/latest.php?flavor=basic) herunterladen
*	Starten Sie den Imager und folgen Sie diesen Schritten:
*	Klicken Sie auf `Betriebssystem (OS)" -> "OS WÄHLEN`
*	Wählen Sie `Pi4J-Basic-OS (32-bit)`
*	Stecken Sie Ihre SD-Karte in Ihren Computer oder in einen SD-Kartenleser (via USB)
*	Klicken Sie auf `SD-Karte" -> "SD-KARTE WÄHLEN`.
*	Wählen Sie die SD-Karte aus
*	Öffnen Sie ein Terminalfenster und geben Sie `java -version` ein. Java wird gestartet und zeigt Ihnen die installierte Version an.
*	Jetzt ist Ihre SD-Karte bereit für den Einsatz in einem Raspberry Pi


### Schritt 2: Vorbereiten eines Entwicklungscomputers [10min]
* Downloaden und installieren Sie [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
* Downloaden und installieren Sie [VNC Viewer](https://www.realvnc.com/en/connect/download/viewer/). Dies ermöglicht einen Zugriff auf den Desktop des Raspberry Pi
* Da auf den Raspy [Java 17](https://adoptium.net/?variant=openjdk17&jvmVariant=hotspot) läuft, wollen wir dasselbe auf dem Computer auch benutzen
Wenn java 17 installier ist: `IntelliJ -> Settings -> Build, Execution,Deployment -> Build Tools -> Maven -> Importing -> JDK for Importert: "JDK 17""`

In einem Terminal-Window eingeben
```shell
java -version
```

Das sollte diese Ausgabe erzeugen
```
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment Temurin-17.0.9+9 (build 17.0.9+9)
OpenJDK 64-Bit Server VM Temurin-17.0.9+9 (build 17.0.9+9, mixed mode)
```

### Schritt 3: Verbinden mit dem Raspberry Pi [15min]
Der Rechner und der Raspberry Pi müssen sich im gleichen WLAN befinden.
Eine einfache Lösung dafür ist, mit einem Smartphone einen Hotspot mit diesen Parametern zu erstellen:

- ssid: `Energiesammler`
- password: `Energiesammler24!`

Das Pi4J-Basis-OS Image ist so konfiguriert, dass es sich automatisch mit diesem Hotspot verbindet.
Verbinden Sie Ihren Entwickler-PC auch mit dem Hotspot Pi4J-Spot.

Verbinden über SSH
Geben Sie folgendes in ein Terminal des Entwicklerrechners ein:

Die ip.number wird auf dem Desktop des Raspberry Pi angezeigt.

```shell
ssh pi@pi4j.local
```
Als passwort muss `pi4j` genutzt werden.

Verbinden über VNC
Über die gleiche IP-Adresse kann eine VNC-Verbindung zum Raspberry Pi hergestellt werden. Der VNC-Client zeigt ein Fenster an, das den Zugriff auf den gesamten Desktop des Raspberry Pi ermöglicht.

`Öffne VNC Viewer -> Datei -> Neue Verbindung -> VNC Server: <ipü.number> -> OK -> Passwort: pi4j'`


### Schritt 4: Source Code öffnen [10min]

Bei [Ropsitory Energiesammler](https://github.com/) finden sie folgendes den Source Code.
Um den Code auf die IDE zu klonen gehen sie wei folgt vor:

`<> Code -> Local -> HTTPS -> kopiere den Link`

Diesen Link dann bei IntelliJ einfügen:

`File -> New -> Project from Version Controll -> Fügen Sie den Link bei URL:, ein`


### Schritt 5: Code Ausführen [1min]

Dieses Projekt verwendet Maven, um die Applikationen zu bauen und entweder lokal auf dem Laptop oder auf dem Raspberry Pi auszuführen.

- mit `Run local` auf dem Laptop starten. Hier werden die Artifakte gebaut und das .jar File wird aufgerufen.
- mit `Run on Pi (DRM)` auf dem RaspPi starten.Hier werden die Artifakte gebaut, auf den Rasperry Pi kopiert und das .jar File wird aufgerufen.
- mit `JumpNRunApp` auf der IDE starten. Hier wird die Applikation auf der IDE aufgerufen.

### Debug: Bugs fixxen
- Start von `Debug on Pi` via des Run-Knopfs
- Warten bis die Konsolenausgabe meldet Listening for transport dt_socket at address: 5005 (Attach debugger)
- Starten von `Attach to Pi Debugger` mit dem Debug-Knopf
- Erst danach erscheint das GUI auf dem RaspPi-Bildschirm

### Schritt 6: Spiel dauerlauf [15min]
Mit dem "Run on Pi (DRM)" wird das Spiel zwar auf den Raspy kopiert und ausgeführt, schliesst jedoch wenn man bei Intellij das Programm stoppt.
Um das Spiel dauerhaft auf dem Raspy laufen zu lassen muss man folgenden Schritten nachgehen:
- mit `Run on Pi (DRM)` das Programm wird auf den Raspy kopiert und ausgeführt. Hier kannst du noch testen ob alles funktioniert.
- Programmausführung auf IntelliJ stoppen.
- Um mit dem Raspy zu verbinden muss die IP Adresse evtl angepasst werden:
```shell
ssh pi@pi4j.local
```
Als Passwort muss `pi4j` genutzt werden.

```shell
DISPLAY=:0 XAUTHORITY=/home/pi/.Xauthority sudo -b java -XX:+UseZGC -Xmx1G    --module-path /home/pi/deploy --module-path /home/pi/processing/processing/core/library:/home/pi/deploy --module ch.fhnw.energiesammler/ch.fhnw.energiesammler.JumpNRunApp -Dmonocle.cursor.enabled=false
```

## Einstellungen
Um die Variablen in den angegeben Klassen zu finden empfielt sich die Suchfunktion mittels `ctrl + F` 

### Einstellungen im [pom.xml](pom.xml)
Unter Properties:
- `launcher.class`: gibt an, welche Applikation gestartet werden soll. Im `pom.xml` ist bereits eine Liste von Kandidaten enthalten. Man muss nur bei der jeweils gewünschte Applikation die Kommentare entfernen.
- `pi.hostname`: Der Hostname des Raspberry Pis, z.B. `pi4j`, wird auf dem Monitor des Pis eingeblendet.
- `pi.ipnumber`: Die aktuelle IP-Nummer des Raspberry Pi, z.B. `192.168.1.2`, wird für SCP/SSH benötigt und wird ebenfalls auf dem Monitor angezeigt

### Spieler Einstellungen 
* Gravität: In der Klasse [app.properties](src/main/resources/app.properties) -> _gravity.player_
* Laufgeschwindigkeit: In der Klasse [app.properties](src/main/resources/app.properties) -> _speed.player_
* Sprungkraft: In der Klasse [app.properties](src/main/resources/app.properties) -> _jump.player_
* Bilder (Frau): Im Package [resources/img/female](src/main/resources/img/female)
* Bilder (Mann): Im Package [resources/img/male](src/main/resources/img/male)

### Gegner Einstellungen
* Energieabzug:
  * Ofen: [app.properties](src/main/resources/app.properties) -> _dmg.oven_
  * Mixer: [app.properties](src/main/resources/app.properties) -> _dmg.mixer_
  * Glühbirne: [app.properties](src/main/resources/app.properties) -> _dmg.bulb_
* Geschwindigkeit:
  * Ofen: [app.properties](src/main/resources/app.properties) -> _speed.oven_
  * Mixer: [app.properties](src/main/resources/app.properties) -> _speed.mixer_
  * Glühbirne: [app.properties](src/main/resources/app.properties) -> _speed.bulb_
* Erscheindungsort: [Map1](src/main/resources/files/maps/map1.csv), [Map2](src/main/resources/files/maps/map2.csv), [Map3](src/main/resources/files/maps/map3.csv)
  * Ofen: Setze eine `O`
  * Mixer: Setze eine `M`
  * Glühbirne: Setze eine `B`
* Bild: Im Package [resources/img/enemy](src/main/resources/img/enemy)
  * Ofen: die Bilder [oven_run1](src/main/resources/img/enemy/oven_run1.png), [oven_run2](src/main/resources/img/enemy/oven_run2.png), [oven_neutral1](src/main/resources/img/enemy/oven_neutral1.png)
  * Mixer: die Bilder [mixer_run1](src/main/resources/img/enemy/mixer_run1.png), [mixer_run2](src/main/resources/img/enemy/mixer_run2.png), [mixer_neutral1](src/main/resources/img/enemy/mixer_neutral1.png)
  * Glühbirne: die Bilder [bulb_run1](src/main/resources/img/enemy/bulb_run1.png), [bulb_run2](src/main/resources/img/enemy/bulb_run2.png), [bulb_neutral1](src/main/resources/img/enemy/bulb_neutral1.png)

### Energiegewinner Einstellungen
* Energiezuschuss
  * Blitz: [app.properties](src/main/resources/app.properties) -> _energy.lightning_
  * Windmühle: [app.properties](src/main/resources/app.properties) -> _energy.windmill_
  * Sonnenkollektor: [app.properties](src/main/resources/app.properties) -> _energy.solar_
* Bild: Im Package [resources/img/energySource](src/main/resources/img/energySource)
  * Blitz: [lightning](src/main/resources/img/energySource/lightning.png)
  * Windmühle: [windmill](src/main/resources/img/energySource/windmill.png)
  * Sonnenkollektor: [solarpannel](src/main/resources/img/energySource/solarpannel.png)
  * Batterieanzeige: [battery](src/main/resources/img/energySource/battery.png)
  * Batterie zum aufsammeln: [batteryCollect](src/main/resources/img/energySource/batteryCollect.png)
* Erscheinungsort: [Map1](src/main/resources/files/maps/map1.csv), [Map2](src/main/resources/files/maps/map2.csv), [Map3](src/main/resources/files/maps/map3.csv)
  * Blitz: Setze ein/e `l`
  * Windmühle: Setze ein/e `w`
  * Sonnenkollektor: Setze ein/e `s`
  * Batterie zum aufsammeln: Setze ein/e `b`

### Aufgaben Einstellungen
  * Erweiterung der Aufgaben: In dem Package  [tasks](src/main/resources/tasks) müssen in allen Sprachen die Aufgaben erweitert weden.
  * Ändern des Energieverbauchs der Aufgaben: Im File [task.properties](src/main/resources/task.properties) müssen die Werte der Tasks geändert werden.

### Sprachen Einstellungen
Erweiterung der Sprachen: In dem Package  [tasks](src/main/resources/tasks) müssen weitere Files mit dem Muster task_<sprache>.properties hinzugefügt werden.

### Level Einstellungen [Map1](src/main/resources/files/maps/map1.csv), [Map2](src/main/resources/files/maps/map2.csv), [Map3](src/main/resources/files/maps/map3.csv)
Man kann nun Level löschen oder hinzufügen. Die einzige Bedingung ist, dass der Name der Map-Datei `mapX.csv` benennt wird. X ist dabei eine Zahl die der Zahlenfolge entspricht (Darf keine Unterbrüche von Zahlen geben).
Eine Level wird anhand erlesener Chars gestaltet:
Zum Merken: Blöcke sind Zahlen, Gegner grosse Chars, andere Entities kleine Chars.
* Nichts: Ofen: Setze ein/e `0`
* Dreckuntergrund: Ofen: Setze ein/e `1`
* Holz vertikal: Ofen: Setze ein/e `2`
* Haus: Setze ein/e `h`
* Tür: Setze ein/e `d`
* Blitz: Setze ein/e `l`
* Windmühle: Setze ein/e `w`
* Sonnenkollektor: Setze ein/e `s`
* Batterie zum aufsammeln: Setze ein/e `b`
* Ofen: Setze eine `O`
* Mixer: Setze eine `M`
* Glühbirne: Setze eine `B`

### Steuerung Einstellungen
Um die Steuerung von Joystick, buttons oder Tastatur zu ändern, muss in dder Klasse [GameCommand](src/main/java/ch/fhnw/energiesammler/controls/GameCommand.java)
den Inhalt der Klammern von UP, DOWN usw... ändern. Die ersten Parameter sind für die Tastatur, während die zweiten Parameter für die GPIO eingänge sind wie für Joystick und Buttons.


### Raspberry Pi autologin in Hotspot
```shell
ssh pi@pi4j.local
```
Als passwort muss `pi4j` genutzt werden.

Mit dem folgendem Befehl finden sie die Datei, mit welchem sie die SSID und das Paswort einstellen können:`
sudo nano /etc/wpa_supplicant# root@pi4j:/etc/wpa_supplicant#`

### Ernergiesammler Autostart
```shell
ssh pi@pi4j.local
```
Als passwort muss `pi4j` genutzt werden.

Mit dem folgendem Befehl finden sie die Datei, mit welchem sie die Autostartkonfigurationen einstellen können:`
sudo nano /home/pi/.config/autostart/energiesammler.desktop`






