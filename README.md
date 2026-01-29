# Bound-In-Stone
**Bound In Stone** is a rhythm-based JavaFX game originally made as a school project and expanded into a full personal coding project.  
The level design, timing, and visuals are all synced to music, with gameplay built around beats, movement patterns, and precision.

The main song used is **Deadlocked by F-777**, and most of the mechanics are designed around its beat structure.

---

## Requirements

You need:

- Java JDK 21 or newer  
- JavaFX SDK 21.0.9 (or compatible)
- Eclipse (recommended, since this project was made in it)

Download JavaFX from:  
https://openjfx.io

---

## How to Run

1. Open the project in Eclipse.
2. Right-click the project → **Run As → Run Configurations…**
3. Select your main class.
4. Go to the **Arguments** tab.
5. In **VM arguments**, paste:

--module-path "Path to lib folder of JavaFX"
--add-modules javafx.controls,javafx.graphics,javafx.fxml,javafx.media

Make sure the path points to your JavaFX SDK `lib` folder.

6. Click **Apply**, then **Run**.

---

## Controls / Gameplay

- The game is rhythm-based and synced to music.
- Walls, orbs, and effects move based on beat timing.
- Movement and interaction are timing-focused rather than reaction-focused.
- Difficulty is designed to be challenging.

Spacebar, WASD, and Shift are used in the game.

---

## Notes

- This project uses JavaFX for graphics, animation, and audio.
- All level design math and movement paths were created manually.
- Most motion is beat-driven rather than frame-driven.
