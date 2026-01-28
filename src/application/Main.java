package application;

//import stuff
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import javafx.scene.media.AudioClip;
import java.util.ArrayList;
import javafx.scene.effect.Glow;
import javafx.scene.effect.BlendMode;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.paint.CycleMethod;
import javafx.scene.shape.ArcType;
import javafx.scene.effect.DropShadow;
import java.util.Random;
import javafx.scene.paint.LinearGradient;
import javafx.scene.effect.Bloom;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.InnerShadow;
import javafx.scene.effect.Lighting;
import javafx.scene.effect.Light;

//Forgive me for not knowing how to use other files it has caused this single file to be over 1000 lines long
public class Main extends Application {

	//Randomize
	private Random random = new Random();
    //Constants
	//I should have more for the magic vars but
    private final int WIDTH = 600;
    private final int HEIGHT = 600;

    //Game Vars (too many)
    private double playerX = 275; 
    private double playerY = 275; 
    private double playerSpeed = 5; 
    private int UpLevel = 2;
    private int RightLevel = 3;
    private String PersonSpeaking;
    private String DisplayText = ""; 
    private String FullWrappedText = ""; 
    private int charIndex = 0; 
    private int slowTyper = 0; 
    private boolean Paused = false;
    private int PauseCooldown = 0;
    private boolean CanMove = true;
    private boolean MotherSpoken = false;
    private boolean GameStart = false;
	Boolean NextText = false;
	private int GameState = 0; //0 = menu and 1 = playing
	private double pictureChange = 0;
	private double pictureChangeLoop = 0;
	private double pictureSize = 400;  
	//why do I even have to write private?
	int MomDialogueStep = 0;
	boolean NeighbourSpoken = false;
	int AnimationLoop = 0;
	int MasterDialogue = 0;
	int PhaseTime = 0;
	int PhaseCooldown = 0;
	int TeleportCooldown = 0;
	double PortalSize = -4;
	double PortalChange = 0;
	double PortalChangeLoop = 0;
	int CurrentLevel = -1;
	double BPM = 135;
	double secondsPerBeat = 60.0 / BPM;
	double BeatIntensity = 0;
	double Beats = 0;
	double Loop = 0;
	boolean GameOver = false;
	int Deaths = 0;
	String dialogueText = "";
	boolean SpokenToMaster = false;

	//NEW AND OPTIMIZED ART ASSETS!!! AND ORGANIZED!!
	//Feels like when you organize the wires behind your desk or something.
    //Gradients (Global)
	//dumbahh shii wont work and now i gotta redo the glow probably.
    private final RadialGradient blueGrad = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(0.4, Color.CYAN), new Stop(1, Color.DARKBLUE));
    private final RadialGradient orangeGrad = new RadialGradient(0, 0, 0.5, 0.5, 0.5, true, CycleMethod.NO_CYCLE, new Stop(0, Color.WHITE), new Stop(0.4, Color.YELLOW), new Stop(1, Color.DARKRED));
    private final LinearGradient dangerGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.RED), new Stop(0.5, Color.web("#FF4444")), new Stop(1, Color.RED));
    private final LinearGradient greenGrad = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, new Stop(0, Color.web("#004d00")), new Stop(0.5, Color.LIME), new Stop(1, Color.web("#004d00")));   
    
    //Effects (Global)
    private final DropShadow glow = new DropShadow(10, Color.WHITE);
    private final DropShadow outerBlue = new DropShadow(40, Color.CYAN);
    
    private final Bloom bloom = new Bloom(0.1);
    private final DropShadow outerGlow = new DropShadow(25, Color.RED);
    private final InnerShadow innerGlow = new InnerShadow(20, Color.DARKRED);
    
    private final DropShadow neonGlow = new DropShadow(20, Color.LIME);
    private static final BoxBlur glitchEffect = new BoxBlur(5, 5, 1);

    //Im just throwing stuff at te wall a this point and hope it sticks.
    private final Color GLOW_RED = Color.rgb(255, 0, 0, 0.15); 
    private final Color CORE_RED = Color.rgb(255, 50, 50, 1.0); 
    
    private final Color GLOW_CYAN = Color.rgb(0, 255, 255, 0.15);
    private final Color CORE_WHITE = Color.WHITE;
    
    //This block runs ONCE when the game starts to wire the effects together.
    //We never touch .setInput() inside the game loop again.
    {
        glow.setSpread(0.1);
        outerBlue.setInput(glow); // White Wall Chain

        outerGlow.setInput(bloom); // Red Wall Chain
        innerGlow.setInput(outerGlow);
    }
    
	//Strings for respawn (AI generated strings ofc)
	String[] stage1Lines = {
    "Spaced out for a second there. Gotta focus.",
        "Okay, bad plan. Let's not do that.",
        "Did I just fall asleep standing up? Weird."
    };

    String[] stage2Lines = {
        "I could have sworn I just left town...",
        "Wait... I’ve seen this before. Major déjà vu.",
        "My head is spinning. Why does everything feel so familiar?",
        "Okay, vivid imagination today. Shake it off."
    };
    String[] stage3Lines = {
        "Why do I keep picturing myself dying? Get a grip!",
        "It feels so real... I can almost feel the phantom pain.",
        "Am I cursed? I feel like I'm stuck in a loop.",
        "I’m losing my mind. Just... one foot in front of the other."
    };
	
	//arrays
	ArrayList<Double> BlueXList = new ArrayList<>();
	ArrayList<Double> BlueYList = new ArrayList<>();
	ArrayList<Double> OrangeXList = new ArrayList<>();
	ArrayList<Double> OrangeYList = new ArrayList<>();
	//TODO
	
	// Music System
    MediaPlayer mediaPlayer; 
    boolean musicPlaying = false;
	String Music = "";
	String Music2 = "";
	
	//Transition vars
	Boolean Transitioning = false;
	double TransitionCounter = 0;
	private int nextUpLevel = 0;
    private int nextRightLevel = 0;
    private double nextPlayerX = 0;
    private double nextPlayerY = 0;
	
    //Interaction vars
    private int drawText = -1;
    private int talkCooldown = 0;
    

    //Input Vars
    private boolean goUp, goDown, goLeft, goRight, interact, pause, teleport;

    //Images/font
    private Image MasterImg;
    private Image PlayerImg;
    private Image PlayerLeft;
    private Image PlayerRight;
    private Image ExclamationImg;
    private Image TextboxImg;
    private Image LevelOne;
    private Image LevelTwo;
    private Image PausedImg;
    private Image LevelThree;
    private Image LogoImg;
    private Image MomImg;
    private Image Outside;
    private Image Nextdoor;
    private Image Neighbour;
    private Image TownSquare;
    private Image Fountain;
    private Image Villager1;
    private Image Villager2;
    private Image Villager3;
    private Image Player1;
    private Image Player2;
    private Image Player3;
    private Image OutsideTown;
    private Image Camp;
    private Image Campfire;
    private Font pixelFont;

    @Override
    public void start(Stage primaryStage) {
        try {
        	//Makes the scene (the form)
            BorderPane root = new BorderPane();
            Scene scene = new Scene(root, WIDTH, HEIGHT);
            
            Image icon = new Image(getClass().getResourceAsStream("/GameCover.png")); 
            
            //Taskbar image
            primaryStage.getIcons().add(icon);
            
            //title
            primaryStage.setTitle("Bound In Stone");

            //makes the canvas to draw on
            Canvas canvas = new Canvas(WIDTH, HEIGHT);
            GraphicsContext gc = canvas.getGraphicsContext2D(); 
            root.setCenter(canvas);

            //Inputs
            scene.setOnKeyPressed(event -> {
                KeyCode code = event.getCode();
                if (code == KeyCode.W) goUp = true;
                if (code == KeyCode.S) goDown = true;
                if (code == KeyCode.A) goLeft = true;
                if (code == KeyCode.D) goRight = true;
                if (code == KeyCode.SPACE) interact = true;
                if (code == KeyCode.ESCAPE) pause = true;
                if (code == KeyCode.SHIFT) teleport = true;
            });

            scene.setOnKeyReleased(event -> {
                KeyCode code = event.getCode();
                if (code == KeyCode.W) goUp = false;
                if (code == KeyCode.S) goDown = false;
                if (code == KeyCode.A) goLeft = false;
                if (code == KeyCode.D) goRight = false;
                if (code == KeyCode.SPACE) interact = false;
                if (code == KeyCode.ESCAPE) pause = false;
                if (code == KeyCode.SHIFT) teleport = false;
            });

            //Load images
            try {
                MasterImg = new Image(getClass().getResourceAsStream("/Master.jpg"));
                PlayerImg = new Image(getClass().getResourceAsStream("/Player.png"));
                ExclamationImg = new Image(getClass().getResourceAsStream("/Exclamation.png"));
                TextboxImg = new Image(getClass().getResourceAsStream("/Textbox.png"));
                LevelOne = new Image(getClass().getResourceAsStream("/Bedroom.png"));
                LevelTwo = new Image(getClass().getResourceAsStream("/Upstairs.png"));
                PausedImg = new Image(getClass().getResourceAsStream("/Pause.png"));
                LevelThree = new Image(getClass().getResourceAsStream("/Downstairs.png"));
                PlayerLeft = new Image(getClass().getResourceAsStream("/LookLeft.png"));
                PlayerRight = new Image(getClass().getResourceAsStream("/Lookright.png"));
                LogoImg = new Image(getClass().getResourceAsStream("/GameLogo.png"));
                MomImg =new Image(getClass().getResourceAsStream("/TheMom.png"));
                Outside =new Image(getClass().getResourceAsStream("/Outside.jpg"));
                Nextdoor =new Image(getClass().getResourceAsStream("/NextDoor.png"));
                Neighbour =new Image(getClass().getResourceAsStream("/Neighbour.png"));
                TownSquare =new Image(getClass().getResourceAsStream("/TownSquare.png"));
                Fountain =new Image(getClass().getResourceAsStream("/Fountain.png"));
                Villager1 = new Image(getClass().getResourceAsStream("/Villager1.png"));
                Villager2 = new Image(getClass().getResourceAsStream("/Villager2.png"));
                Villager3 = new Image(getClass().getResourceAsStream("/Villager3.png"));
                Player1 = new Image(getClass().getResourceAsStream("/Player1.png"));
                Player2 = new Image(getClass().getResourceAsStream("/Player2.png"));
                Player3 = new Image(getClass().getResourceAsStream("/Player3.png"));
                OutsideTown = new Image(getClass().getResourceAsStream("/OutsideTown.png"));
                Camp = new Image(getClass().getResourceAsStream("/Camp.png"));
                Campfire = new Image(getClass().getResourceAsStream("/Campfire.gif"));
                // Load font at sizre 20
                
                pixelFont = Font.loadFont(getClass().getResourceAsStream("/VT323-Regular.ttf"), 20);            
            } catch (Exception e) {
                System.out.println("ERROR: Move images to src folder!");
                e.printStackTrace();
            }
            

            //the game loop/tick (60 tps i think)
            AnimationTimer gameLoop = new AnimationTimer() {
                @Override
                public void handle(long now) {
                	if (mediaPlayer != null) {
                        double songTime = mediaPlayer.getCurrentTime().toSeconds();
                        Beats = songTime / secondsPerBeat;
                        double beatProgress = Beats - Math.floor(Beats);
                        BeatIntensity = 1.0 - beatProgress;   // pulse
                    }
                	if (GameState == 0) {
                        drawMenu(gc);
                    } else if (GameState == 1) {
                    	CheckPause(gc);
                    	if(Paused == false) {
                            checkLevelScrolling(gc);
                            checkBoundaries();
                            LevelLogic();
                            drawGame(gc);	
                            Transition(gc);
                            if (talkCooldown > 0) {
                                talkCooldown -= 1;
                            }
                    	}
                    	if(Paused == false && CanMove == true && TransitionCounter == 0) {
                    		updateMovement();
                    	}
                    } else {
                    	CheckPause(gc);
                    	if(Paused == false) {
                    		Transition(gc);
                            checkBoundaries();
                            LevelLogic();
                            drawGame(gc);	
                    	}
                    	if(Paused == false && CanMove == true && TransitionCounter == 0) {
                    		updateMovement();
                    	}
                    }
                    
                }
            };

            gameLoop.start();
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Methods
    private void LevelLogic() {
    	//start music for first few levels
    	if (UpLevel < 5 && RightLevel < 5 && UpLevel > -10 && RightLevel > -10) {
    		if (!Music.equals("Start")) {
    			playMusic("Pixie Makeover.mp3");
    			Music = "Start";
    			Music2 = "";
    		}
    	}
    	//Logic for lvl 0
    	if (UpLevel == 0 && RightLevel == 0 && drawText == -1) {
    		if (GameStart == false) {
    			WriteText("Tutorial", "Welcome to the game, The controls are WASD to move, and space to interact. Try to interact with the painting on the wall.");
    			GameStart = true;
    		}
    	}
    	//Logic for lvl 1
    	if (UpLevel == 0 && RightLevel == 1 && drawText == -1 && TransitionCounter == 0) {
    		if (MotherSpoken == false) {
    			WriteText("Mom", "Is that you Honey?");
    			MotherSpoken = true;
    		}
    		if (MotherSpoken == true && drawText == -1 && NextText == false) {
    			WriteText("Mom", "Come downstairs, you dont want to be late for today");
    			NextText = true;
    		}
    	}
    	//Logic for lvl 2
    	if (UpLevel == 1 && RightLevel == 1 && drawText == -1 && TransitionCounter == 0) {
    	    if (MomDialogueStep == 0) {
    	        WriteText("Mom", "Good morning! I was about to come drag you out of bed myself.");
    	        MomDialogueStep += 1;
    	    }
    	    else if (MomDialogueStep == 1) {
    	        WriteText("Mom", "I can't believe it's finally here. The Ascension... Time flies so fast.");
    	        MomDialogueStep += 1;
    	    }
    	    else if (MomDialogueStep == 2) {
    	        WriteText("Mom", "The Master is at the Training Camp. Head OUTSIDE and go RIGHT.");
    	        MomDialogueStep += 1;
    	    }
    	    else if (MomDialogueStep == 3) {
    	        WriteText("Mom", "Be careful out there, okay? Make us proud.");
    	        MomDialogueStep += 1;
    	    }
    	}
    	 
    	if (UpLevel == -100 && RightLevel == -100 && drawText == -1 && TransitionCounter == 0) {
    		if (!Music2.equals("Start")) {
    			System.out.println("hi");
    			playMusic("Deadlocked.mp3");
    			Music2 = "Start";
    			Music = "";
    		}
    	}
    	
    	//The animation
    	//TODO    	
    	if (UpLevel == 1 && RightLevel == 3) {
    		if (SpokenToMaster == true) {
    			AnimationLoop = 83;
    			MasterDialogue = 7;
    			SpokenToMaster = false;
    			playerX = 320;
    			playerY = 400;
    		}
    		CanMove = false;
    		if (AnimationLoop == 0) {
    			playerX = 275;
    			playerY = 600;
    		}
    		if (AnimationLoop < 33) {
    			playerX = 275;
    			playerY -= 3;
    		}
    		if (AnimationLoop == 33 && MasterDialogue == 0) {
    			MasterDialogue += 1;
    			WriteText("Master", "You're late. The others have already arrived. Take your spot. Now.");
    		}
    		if (AnimationLoop < 51 && AnimationLoop > 33) {
    			playerX += 2.598;
    			playerY -= 1.5;
    		}
    		if (AnimationLoop < 76 && AnimationLoop > 51) {
    			playerY -= 3;
    		}
    		if (AnimationLoop == 76 && MasterDialogue == 1) {
    			MasterDialogue += 1;
    			WriteText("Master", "Listen closely. The Music controls the Stone. When you see RED walls, they are unstable matter. Avoid them.");
    		}
    		if (AnimationLoop == 77 && MasterDialogue == 2) {
    			MasterDialogue += 1;
    			WriteText("Master", "The WHITE walls are solid matter. They cannot hurt you, but they can push you around.");
    		}
    		if (AnimationLoop == 78 && MasterDialogue == 3) {
    			MasterDialogue += 1;
    			WriteText("Master", "The GREEN walls are energy. You cannot outrun them... you must accept them.");
    		}
    		if (AnimationLoop == 79 && MasterDialogue == 4) {
    			MasterDialogue += 1;
    			WriteText("Master", "Press SPACE to Phase your soul. You will become intangible for a moment.");
    		}
    		if (AnimationLoop == 81 && MasterDialogue == 5) {
    			MasterDialogue += 1;
    			WriteText("Master", "But be warned: Phasing takes a toll. You cannot do it constantly. Watch your cooldown.");
    		}
    		if (AnimationLoop == 82 && MasterDialogue == 6) {
    			MasterDialogue += 1;
    			WriteText("Master", "Finally, the BLUE and ORANGE orbs are tears in space. Press SHIFT near them to teleport between.");
    		}
    		if (AnimationLoop == 83 && MasterDialogue == 7) {
    			MasterDialogue += 1;
    			WriteText("Master", "Dodge the Red. Phase the Green. Teleport the Blue and Orange.");
    		}
    		if (AnimationLoop == 90) {
    			nextUpLevel = -100;
    			nextRightLevel = -100;
    			TransitionCounter = 100;
    			nextPlayerX = 275;
    		    nextPlayerY = 275;
    		    stopMovement();
    		}
    		if (drawText == -1) {
    			AnimationLoop += 1;
    		}

    	}
    	if (GameOver == true) {
    		playerX = 275;
    		playerY = 500;
			UpLevel = 2;
			RightLevel = 2;
		    playerSpeed = 5;
		    GameState = 1;
			BeatIntensity = 0;
			Beats = 0;
			Loop = 0;
			AnimationLoop = 0;
			MasterDialogue = 0;
			PortalChangeLoop = 0;
			Deaths++;
			WriteTextForRespawn();
			WriteText("Me", dialogueText);
			GameOver = false;
		}
    	if (UpLevel == -100 && RightLevel == -100) {
    		if (TransitionCounter == 49) {
    			playerX = 275;
    			playerY = 275;
    		}
    		CanMove = true;
    		TutorialLogic();
    	}
    }

	private void CheckPause(GraphicsContext gc) {
    	//check if game is paused
    	if (PauseCooldown > 0) {
    		PauseCooldown--;
    	}
    	if (pause && Paused == false && PauseCooldown == 0) {
    		Paused = true;
    		PauseCooldown = 20;
    		gc.drawImage(PausedImg, 0, 0, 600, 600);
    		gc.fillText("GAME PAUSED", 210, 450);
    	}
    	if (pause && Paused == true && PauseCooldown == 0) {
    		Paused = false;
    		PauseCooldown = 20;
    	}
    }
    private void updateMovement() {
        //Code for moving player
        if (goUp && goRight) {
            playerY -= playerSpeed/Math.sqrt(2);
            playerX += playerSpeed/Math.sqrt(2);
        } else if (goUp && goLeft) {
            playerY -= playerSpeed/Math.sqrt(2);
            playerX -= playerSpeed/Math.sqrt(2);
        } else if (goDown && goLeft) {
            playerY += playerSpeed/Math.sqrt(2);
            playerX -= playerSpeed/Math.sqrt(2);
        } else if (goDown && goRight) {
            playerY += playerSpeed/Math.sqrt(2);
            playerX += playerSpeed/Math.sqrt(2);
        } else {
            if (goUp) playerY -= playerSpeed;
            if (goDown) playerY += playerSpeed;
            if (goLeft) playerX -= playerSpeed;
            if (goRight) playerX += playerSpeed;
        }
    }

    private void checkLevelScrolling(GraphicsContext gc) {
        if (TransitionCounter > 0) return;
        //left
        if (playerX < -20) {
            nextPlayerX = 500;
            nextPlayerY = playerY;
            nextRightLevel = RightLevel - 1;
            nextUpLevel = UpLevel;
            TransitionCounter = 100; 
            if (UpLevel == 2 && RightLevel == 2) {
                nextPlayerY = 510;
            }
        }
        //right
        if (playerX > WIDTH) {
            nextPlayerX = 50;
            nextPlayerY = playerY;
            nextRightLevel = RightLevel + 1;
            nextUpLevel = UpLevel;
            TransitionCounter = 100;
            if (UpLevel == 2 && RightLevel == 2) {
                nextPlayerY = 385;
            }
        }
        //top
        if (playerY > HEIGHT) {
            nextPlayerY = 50;
            nextPlayerX = playerX; // Default
            nextUpLevel = UpLevel + 1;
            nextRightLevel = RightLevel;
            
            if (UpLevel == 0 && RightLevel == 1) {
                nextPlayerX = 30;
            }
            if (UpLevel == 1 && RightLevel == 1) {
                nextPlayerX = 178;
                nextPlayerY = 436;
            }
            TransitionCounter = 100;
            
        }
        //Bottom
        if (playerY < -20) {
            nextPlayerY = 500;
            nextPlayerX = playerX; // Default
            nextUpLevel = UpLevel - 1;
            nextRightLevel = RightLevel;

            if (UpLevel == 1 && RightLevel == 1) {
                nextPlayerX = 325;
            }
            TransitionCounter = 100;
        }
        //Level Specific transitions.
        
        if (UpLevel == 2 && RightLevel == 1) {
        	if (playerY < 400) {
        		nextPlayerY = 525;
                nextPlayerX = 138; // Default
                nextUpLevel = UpLevel - 1;
                nextRightLevel = RightLevel;
                TransitionCounter = 100;
        	}
        }
        if (UpLevel == 2 && RightLevel == 3) {
        	if (playerY < 300) {
        		nextPlayerY = 525;
                nextPlayerX = 138; // Default
                nextUpLevel = UpLevel - 1;
                nextRightLevel = RightLevel;
                TransitionCounter = 100;
        	}
        }
    }
    
    private void checkBoundaries() {
    	//TODO
    	//System.out.println(playerX + ", " + playerY);
        if (UpLevel == 100 && RightLevel == 100) {
            if (playerX < 25) playerX = 25;
            if (playerX > 535) playerX = 535;
            if (playerY < 20) playerY = 20;
            if (playerY > 535) playerY = 535;
        }
        if (UpLevel == 0 && RightLevel == 0) {
        	if (playerX < 25) playerX = 25;
            if (playerX > 535 && playerY < 200) playerX = 535;
            if (playerX > 535 && playerY > 250) playerX = 535;
            if (playerY < 20) playerY = 20;
            if (playerY > 535) playerY = 535;
            //|||
            if (playerX < 160 && playerY < 270) playerX = 160;
            // ----
            if (playerX < 160 && playerY < 280) playerY = 280;
            if (playerX < 260 && playerY < 80) playerY = 80;
            if (playerX < 265 && playerY < 75) playerX = 265;
            if (playerX > 350 && playerY < 100) playerY = 100;
            if (playerX > 345 && playerY < 90) playerX = 345;                              
        }
        if (UpLevel == 0 && RightLevel == 1) {
        	if (playerX < 25 && playerY < 200) playerX = 25;
            if (playerX < 25 && playerY > 250) playerX = 25;
        	if (playerX > 335) playerX = 335;
            if (playerY < 20) playerY = 20;
            if (playerY > 535 && playerX < 305) playerY = 535;
            if (playerX > 245 && playerY > 60 && playerX < 255) playerX = 245;
            if (playerX > 290 && playerY > 60 && playerX < 310) playerX = 310;
            if (playerX > 250 && playerY > 60 && playerX < 310) playerY = 60;
            
        }
        if (UpLevel == 1 && RightLevel == 1) {
            if (playerX < 15) playerX = 15;
            if (playerX > 465) playerX = 465;
            if (playerY < 210 && playerX > 105) playerY = 210;
            if (playerX > 50 && playerX < 60 && playerY < 270) playerX = 50;
            if (playerX > 105 && playerX < 115 && playerY < 270) playerX = 115;
            if (playerX > 50 && playerX < 115 && playerY < 270) playerY = 270;
            if (playerX < 25 && playerY < 275) playerX = 25;
            Hitbox(225, 330, 455, 475);
            Hitbox(265, 485, 360, 549);
            Hitbox(165, 527, 660, 660);
            Hitbox(0, 527, 110, 660);

        }
        if (UpLevel == 2 && RightLevel == 1) {
            if (playerY < 428 && playerX > 378) playerY = 428;
            if (playerY > 535) playerY = 535;
        	Hitbox(0, 0, 168, 428);
        	Hitbox(183, 0, 459, 428);
        	Hitbox(183, 0, 459, 428);
        	Hitbox(438, 418, 514, 477);
        }
        if (UpLevel == 2 && RightLevel == 0) {
            if (playerX < 25) playerX = 25;
            if (playerY < 438) playerY = 438;
            if (playerY > 535) playerY = 535;
        }
        if (UpLevel == 2 && RightLevel == 2) {
            if (playerY < 180) playerY = 180;
            if (playerY > 535) playerY = 535;
            Hitbox(-35,-35,185,190);
            Hitbox(365,-20,700,190);
            Hitbox(-35,-25,90,228);
            Hitbox(-35,300,30,460);
            Hitbox(520,300,700,460);
            Hitbox(520,300,700,460);
            
            //fountain
            Hitbox(205, 190, 355, 240);
            Hitbox(175, 220, 385, 285);
            Hitbox(155, 265, 405, 325);
            Hitbox(175, 305, 385, 370);
            Hitbox(205, 350, 355, 400);
            
        }
        if (UpLevel == 2 && RightLevel == 3) {
        	if(playerY > 510) playerY = 510;
        	if(playerX > 480) playerX = 480;
        	Hitbox(330, 440, 600, 600);
        	Hitbox(-30, 430, 115, 650);
        	Hitbox(-30, -30, 215, 315);
        }
        if (UpLevel == -100 && RightLevel == -100) {
        	if(playerY > 550) playerY = 550;
        	if(playerY < 0) playerY = 0;
        	if(playerX > 550) playerX = 550;
        	if(playerX < 0) playerX = 0;
        	
        }
        

    }
    private void drawGame(GraphicsContext gc) {
        // Clear screen
        gc.clearRect(0, 0, WIDTH, HEIGHT);
        //Black backgruond 
        gc.setFill(Color.rgb(0, 0, 0));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        
        handleDialogueInput();
        //all the level drawings
        if (UpLevel == 0 && RightLevel == 0) {
            gc.drawImage(LevelOne, 0, 0, 600, 600);	
            createInteraction(gc, 320, 40, "Painting", "A painting of Starry night", null); 
            gc.drawImage(PlayerImg, (int)playerX, (int)playerY, 50, 50);
        }
        if (UpLevel == 0 && RightLevel == 1) {
            gc.drawImage(LevelTwo, 0, 0, 600, 600);	
        }
        if (UpLevel == 1 && RightLevel == 1) {
            gc.drawImage(LevelThree, 0, 0, 600, 600);
            gc.drawImage(MomImg, 250, 200, 50, 50);
            Hitbox(200,200,300,250);
        }
        if (UpLevel == 2 && RightLevel == 1) {
            gc.drawImage(Outside, 0, 0, 600, 600);
            gc.setFill(Color.rgb(68, 68, 79));
            gc.fillRect(550, 543, 40, 40);
        }
        if (UpLevel == 2 && RightLevel == 0) {
            gc.drawImage(Nextdoor, 0, 0, 600, 600);
            Hitbox(320-52,430-2,370+2,480+2);
            createInteraction(gc, 320, 430, "Neighbour", "Can you believe the price of fertilizer these days? Oh, you're off to the training camp? thats in the other direction.", Neighbour); 
        }
        if (UpLevel == 2 && RightLevel == 2) {
        	gc.drawImage(TownSquare, 0, 0, 600, 600);
        	createInteraction(gc, 400, 245, "Townsperson", "The water is so clear today! It's a sign. The Ascension is going to be perfect. Are you ready to join the sky?", Villager1);
            createInteraction(gc, 100, 250, "Old Man", "Funny... I swear I just saw you walk past here five minutes ago. But you just woke up, right? My memory must be going...", Villager2);
            createInteraction(gc, 500, 400, "Boy", "I tried to visit the Master earlier, but he kicked me out! Watch out for flying rocks if you go that way.", Villager3);
        }
        if (UpLevel == 2 && RightLevel == 3) {
        	gc.drawImage(OutsideTown, 0, 0, 600, 600);
        }
        if (UpLevel == 1 && RightLevel == 3) {
        	gc.drawImage(Camp, 0, 0, 600, 600);
        	gc.drawImage(Campfire, 280, 160, 40, 40);
        }
        if (UpLevel == 1 && RightLevel == 3) {
        	gc.drawImage(MasterImg, 275 ,250 , 50, 50);
        	gc.drawImage(Player1, 230 ,400 , 50, 50);
        	gc.drawImage(Player2, 150 , 340 , 50, 50);
        	gc.drawImage(Player3, 400 ,340 , 50, 50);
        	//gc.drawImage(PlayerImg, 320 ,400 , 50, 50);
        	//TODO
        }
        
        if (UpLevel == -100 && RightLevel == -100) {
            drawChallengeLevel(gc);
        }
        
        if (PhaseTime > 0) {
        	if (PhaseTime > 25) {
        		gc.setGlobalAlpha((PhaseTime-25)* 0.12 + 0.4); 
        	} else if (PhaseTime < 5) {
        		gc.setGlobalAlpha(PhaseTime * -0.12 + 1); 
        	} else {
        		gc.setGlobalAlpha(0.4); 
        	}
        		
        }
        //draw player
        if (goLeft == true && goRight == true) {
        	gc.drawImage(PlayerImg, (int)playerX, (int)playerY, 50, 50);
        } else if(goLeft == true && CanMove == true) {
        	gc.drawImage(PlayerLeft, (int)playerX, (int)playerY, 50, 50);
        }
        else if(goRight == true && CanMove == true) {
        	gc.drawImage(PlayerRight, (int)playerX, (int)playerY, 50, 50);
        } else {
        	gc.drawImage(PlayerImg, (int)playerX, (int)playerY, 50, 50);
        }
        gc.setGlobalAlpha(1); 
        
        //Draw in front of the player for the effect
        if (UpLevel == 2 && RightLevel == 2) {
        	gc.drawImage(Fountain, 0, 0, 600, 600);
        }
        
        drawText(gc);
    }

	//Handles closing text
    private void handleDialogueInput() {
        // If the box is open AND we press space AND cooldown is done
        if (drawText == 1 && interact && talkCooldown == 0) {
            
            // If text is still typing, Fast Forward
            if (charIndex < FullWrappedText.length()) {
                charIndex = FullWrappedText.length(); 
                DisplayText = FullWrappedText; 
                talkCooldown = 10; 
            } 
            // If text is done, Close the box
            else {
                drawText = -1;
                talkCooldown = 20;
            }
        }
    }

    //opening and walking away
    private void createInteraction(GraphicsContext gc, double targetX, double targetY, String name, String dialogue, Image npcImage) {
        //draw the thing
        if (npcImage != null) {
            gc.drawImage(npcImage, targetX, targetY, 50, 50);
        } else {
            //draw nothing
        }

        //check distance
        boolean isNear = (Math.abs(playerX - targetX) < 65) && (Math.abs(playerY - targetY) < 65);

        //draw Exclamation
        if (isNear && drawText == -1) {
            gc.drawImage(ExclamationImg, playerX +2 , playerY - 50, 45, 45); 
        }

        //interaction
        
        //open box
        if (isNear && interact && talkCooldown == 0 && drawText == -1) {
            drawText = 1;
            PersonSpeaking = name + ": ";
            FullWrappedText = wrapText(dialogue, 45);
            charIndex = 0; 
            DisplayText = "";
            talkCooldown = 20; 
        }
        
        //close if away
        if (drawText == 1 && PersonSpeaking.startsWith(name) && !isNear) {
            drawText = -1;
            talkCooldown = 5;
        }
        
        Hitbox(targetX-50,targetY-50,targetX+50,targetY+50);
    }
    private void drawText(GraphicsContext gc) {
        if (drawText == 1) {
            gc.drawImage(TextboxImg, 20, 420, 560, 150);
            
            //working typewriter that took too long
            //Check if we still have letters to type
            if (charIndex < FullWrappedText.length()) {
             
                slowTyper += 2;
                if (slowTyper > 1) { 
                    charIndex++;
                    slowTyper = 0;
                }
            }
            
            //creating the substring
            DisplayText = FullWrappedText.substring(0, charIndex);

            //drawing it
            gc.setFill(Color.WHITE);
            gc.setFont(pixelFont);
            //draw the PersonSpeaking separate so it doesn't get typed out
            gc.fillText(PersonSpeaking, 70, 470); 
            gc.fillText(DisplayText, 70, 500);
        }
    }
    //wrapText code i found online
    private String wrapText(String text, int charLimitPerLine) {
        StringBuilder sb = new StringBuilder(text);
        int i = 0;
        while (i + charLimitPerLine < sb.length()) {
            // Find the last space within the limit
            int lastSpace = sb.lastIndexOf(" ", i + charLimitPerLine);
            if (lastSpace != -1 && lastSpace > i) {
                sb.replace(lastSpace, lastSpace + 1, "\n"); // Replace space with newline
                i = lastSpace + 1;
            } else {
                // No space found? Just cut it (or find the next space)
                i += charLimitPerLine;
            }
        }
        return sb.toString();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    public void WriteText(String name, String text) {
        // Only start a new conversation if the box is currently closed
        // This prevents the text from resetting 60 times a second while you stand there
        if (drawText == -1) {
            
            drawText = 1; // Open the box
            PersonSpeaking = name + ": ";
            
            // Prepare the typewriter data
            FullWrappedText = wrapText(text, 45); // Wrap it to fit
            charIndex = 0; 
            DisplayText = "";
            
            // Set cooldown so we don't accidentally skip immediately
            talkCooldown = 20; 
        }
    }
    private void Hitbox(double x1, double y1, double x2, double y2) {
        //Check if player is in the box
        if (playerX > x1 && playerX < x2 && playerY > y1 && playerY < y2) {
            
            //Calculate how far the player is from each edge
            double distLeft = playerX - x1;
            double distRight = x2 - playerX;
            double distTop = playerY - y1;
            double distBottom = y2 - playerY;

            //to find the smallest distance
            double min = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));

            // push the player out the closest way
            if (min == distLeft) playerX = x1;
            else if (min == distRight) playerX = x2;
            else if (min == distTop) playerY = y1;
            else if (min == distBottom) playerY = y2;
        }
    }
    private void Transition(GraphicsContext gc) {
    	double temp = 0;
    	if (TransitionCounter > 0){
    		TransitionCounter -= 1;
    	} else {
    		Transitioning = false;
    	}
    	
    	if (TransitionCounter > 50) {
    		temp = 1 - ((TransitionCounter - 50)/50);
    	}
    	if (TransitionCounter < 50){
        	temp = TransitionCounter/50;
        }
    	if (TransitionCounter == 50){
        	temp = 1;
        }
    	
    	//the thing was crashing bc of floating point error
    	if (temp > 1) temp = 1; 

    	if (TransitionCounter == 50){
        	temp = 1;
        	//teleport play at 50
        	playerX = nextPlayerX;
        	playerY = nextPlayerY;
        	RightLevel = nextRightLevel;
        	UpLevel = nextUpLevel;
        }
    	//draw the rectangle
    	gc.setFill(Color.rgb(0, 0, 0, temp)); 
        gc.fillRect(0,0,600,600);
    	
    }
    private void drawMenu(GraphicsContext gc) {
        //black Background
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        //draw Title
        pictureChangeLoop += 0.03;
        if (pictureChangeLoop > 359) {
        	pictureChangeLoop = 0;
        }
        //Pulsing effect
        //i felt so big brain using the sin curve
        pictureChange = Math.sin(pictureChangeLoop);
        pictureSize += pictureChange;
        gc.drawImage(LogoImg, 300 - (pictureSize/2), 200 - (pictureSize/2), pictureSize, pictureSize);
        
        
        //subtitle thing font
        gc.setFill(Color.WHITE);
        gc.setFont(pixelFont);
        
        //blinking effect
        if ((System.currentTimeMillis() / 500) % 2 == 0) {
            gc.fillText("PRESS SPACE TO BEGIN", 210, 450);
        }
        
        //me
        gc.fillText("Created by Arav", 20, 580);

        //stant the real game
        if (interact) {
            GameState = 1; // Switch to gameplay
        }
    }
    
    //Also not my code, i dont know how to add music
    public void playMusic(String musicFile) {
        //formatting the path to the file
        // We use a try-catch because audio files can be finicky
        try {
            String path = getClass().getResource("/" + musicFile).toExternalForm();
            Media sound = new Media(path);

            //Stop old music if it's playing
            if (mediaPlayer != null) {
                mediaPlayer.stop();
            }

            //Load the new song
            mediaPlayer = new MediaPlayer(sound);

            //Set it to Loop forever (Indefinite)
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            
            //Set Volume (0.0 to 1.0)
            mediaPlayer.setVolume(0.5); 

            //Play
            mediaPlayer.play();
            musicPlaying = true;

        } catch (Exception e) {
            System.out.println("Music file not found: " + musicFile);
        }
    }
    public void playSound(String soundFile) {
        try {
            String path = getClass().getResource("/" + soundFile).toExternalForm();
            AudioClip clip = new AudioClip(path);
            clip.play();
        } catch (Exception e) {
            System.out.println("Sound effect missing: " + soundFile);
        }
    }
    private void WriteTextForRespawn() {
    	dialogueText = "";
    	//If 1 death then this message, and so on for the other deaths
        if (Deaths == 1) {
            dialogueText = "Whoa... head rush. Need to stay hydrated.";
        } 
        else if (Deaths == 2) {
            dialogueText = "Ugh, weird daydream. Where was I?";
        }
        
        //deaths 3 -5
        else if (Deaths <= 5) {
            int randomIndex = random.nextInt(stage1Lines.length); 
            dialogueText = stage1Lines[randomIndex];
        } 
        
        //deaths 6 - 15
        else if (Deaths <= 15) {
            int randomIndex = random.nextInt(stage2Lines.length);
            dialogueText = stage2Lines[randomIndex];
        } 
        
        //16+ deaths
        else {
            int randomIndex = random.nextInt(stage3Lines.length);
            dialogueText = stage3Lines[randomIndex];
        }
    }

    //I added this during the optimization phase, its better than the other walls, no GPU usage really. 
    //Took inspiration form geometry dash, wanted to start making the level design easier.
    private void DrawNeonBlock(double x, double y, double w, double h, Color glowColor, Color coreColor, boolean isDangerous, boolean isGlowing, GraphicsContext gc) {
    	//Sorry, no comments for the reader of this :(
        gc.setGlobalBlendMode(BlendMode.ADD);
        double glowSize = 20 + (BeatIntensity * 10);
        if (isGlowing == true) {
            gc.setFill(glowColor);
            gc.fillRect(x - glowSize, y - glowSize, w + (glowSize*2), h + (glowSize*2));
            gc.fillRect(x - (glowSize/2), y - (glowSize/2), w + glowSize, h + glowSize);
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            	
        }
        gc.setFill(coreColor);
        gc.fillRect(x, y, w, h);
        gc.setStroke(Color.WHITE);
        gc.setLineWidth(2);
        gc.strokeRect(x, y, w, h);
        if (isDangerous) {
            gc.setStroke(Color.rgb(255, 0, 0, 0.5));
            gc.setLineWidth(4);
            gc.strokeRect(x + 5, y + 5, w - 10, h - 10);
        }
        if (isDangerous) {
            checkWallCollision(x, y, h, w, true);
        } else {
             Hitbox(x - 51, y - 50, x + w + 1, y + h);
        }
    }
    //NEW AND OPTIMIZED WALLS BABY, EVEN TOOK THE TIME FOR ORGANIZATION
    private void WhiteWall(double WallX, double WallY, double WallH, double WallW, GraphicsContext gc) {
        //The Fast Render
        //gc.setEffect(outerBlue);
        gc.setGlobalAlpha(1);

        gc.setFill(Color.WHITE);
        gc.fillRect(WallX, WallY, WallW, WallH);
        
        //The inner white rect does not need the heavy effect, it sits on top
        gc.setEffect(null); 
        gc.fillRect(WallX + 5, WallY + 5, WallW - 10, WallH - 10);

        //Hitbox Logic
        Hitbox(WallX - 50, WallY - 50, WallX + WallW, WallY + WallH);
    }

    private void RedWall(double WallX, double WallY, double WallH, double WallW, GraphicsContext gc) {
         //Fast Render: No setting thresholds or inputs here. Just use the pre-built effect.
         //gc.setEffect(innerGlow);
         gc.setGlobalAlpha(1);
         gc.setFill(dangerGrad);
         gc.fillRect(WallX, WallY, WallW, WallH);
         
         gc.setEffect(null);
         checkWallCollision(WallX, WallY, WallH, WallW, true);
    }

    private void GreenWall(double WallX, double WallY, double WallH, double WallW, GraphicsContext gc) {
         if (PhaseTime > 0) {
             // Phase Mode
             gc.setGlobalAlpha(0.3);
             gc.setStroke(Color.LIGHTGREEN);
             gc.setLineWidth(3);
             //gc.setEffect(glitchEffect); 
             
             gc.strokeRect(WallX, WallY, WallW, WallH);
             gc.setFill(Color.rgb(0, 255, 0, 0.1)); // Faint fill
             gc.fillRect(WallX, WallY, WallW, WallH);
         } else {
             // Solid Mode
             gc.setGlobalAlpha(1);
             //gc.setEffect(neonGlow);
             gc.setFill(greenGrad);
             gc.fillRect(WallX, WallY, WallW, WallH);
             
             // Optimisation: Drawing stroke on top of fill is cheaper than complex path logic, but keep stroke simple.
             gc.setStroke(Color.WHITE);
             gc.setLineWidth(2);
             gc.strokeRect(WallX, WallY, WallW, WallH);
         }

         //Reset
         gc.setEffect(null);
         gc.setStroke(null); 
         gc.setGlobalAlpha(1); 
         
         if(PhaseTime == 0) {
             checkWallCollision(WallX, WallY, WallH, WallW, false);
         }
    }
	 //I made this because the same thing is copied for the red & green walls
	 private void checkWallCollision(double WallX, double WallY, double WallH, double WallW, boolean alwaysKill) {
	     if (playerX > WallX - 50 && playerX < WallX + WallW && playerY > WallY - 50 && playerY <  WallY + WallH) {
	             
	         double distLeft = playerX - WallX - 50;
	         double distRight = WallX + WallW - playerX;
	         double distTop = playerY - WallY - 50;
	         double distBottom = WallY + WallH - playerY;
	
	         double min = Math.min(Math.min(distLeft, distRight), Math.min(distTop, distBottom));
	
	         //If touching any side, trigger game over
	         if (min == distLeft || min == distRight || min == distTop || min == distBottom) {
	              //GameOver = true;
	        	 //TODO
	         }
	     }
	 }
    private void stopMovement() {
    	//Stops all movement when called
        goUp = false;
        goDown = false;
        goLeft = false;
        goRight = false;
        interact = false;
        pause = false;
    }
    private void AddOrb(double bx, double by, double ox, double oy) {
    	//Add the orb XY when function called and add it to the list to store many orbs
    	BlueXList.add(bx);
    	BlueYList.add(by);
    	OrangeXList.add(ox);
    	OrangeYList.add(oy);
    }
    private void DrawOrbs(GraphicsContext gc) {
        for (int i = 0; i < BlueXList.size(); i++) {
            Double bx = BlueXList.get(i);
            Double by = BlueYList.get(i);
            Double ox = OrangeXList.get(i);
            Double oy = OrangeYList.get(i);

            //draw blue orbs
            //glowish
            gc.setGlobalBlendMode(BlendMode.ADD);
            gc.setGlobalAlpha(0.3); //this makes it transparent 
            gc.setFill(Color.BLUE);
            //pulse just like the logo
            gc.fillOval(bx - 10 - (PortalSize/2), by - 10 - (PortalSize/2), 50 + PortalSize + 20, 50 + PortalSize + 20);
            //center
            gc.setGlobalBlendMode(BlendMode.SRC_OVER); 
            gc.setGlobalAlpha(1.0);//solid again
            gc.setFill(blueGrad); //using the blue gradient we made
            gc.fillOval(bx - (PortalSize/2), by - (PortalSize/2), 50 + PortalSize, 50 + PortalSize);
            //white rings spinnign 
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            gc.strokeArc(bx + 5, by + 5, 40, 40, (PortalChangeLoop * 10), 100, ArcType.OPEN);
            gc.strokeArc(bx + 5, by + 5, 40, 40, (PortalChangeLoop * 10) + 180, 100, ArcType.OPEN);

            //draw orange orbs now
            //glowish
            gc.setGlobalBlendMode(BlendMode.ADD);
            gc.setGlobalAlpha(0.3);//this makes it transparent 
            gc.setFill(Color.ORANGE);
            //pulse just like the logo
            gc.fillOval(ox - 10 - (PortalSize/2), oy - 10 - (PortalSize/2), 50 + PortalSize + 20, 50 + PortalSize + 20);
            //center
            gc.setGlobalBlendMode(BlendMode.SRC_OVER);
            gc.setGlobalAlpha(1.0);//solid again
            gc.setFill(orangeGrad);//using the blue gradient we made
            gc.fillOval(ox - (PortalSize/2), oy - (PortalSize/2), 50 + PortalSize, 50 + PortalSize);
            //white rings spinnign 
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(3);
            //backwards spinning compared to the blue ones
            gc.strokeArc(ox + 5, oy + 5, 40, 40, -(PortalChangeLoop * 10), 100, ArcType.OPEN);
            gc.strokeArc(ox + 5, oy + 5, 40, 40, -(PortalChangeLoop * 10) + 180, 100, ArcType.OPEN);
        }
        
        //clear lists so no overlap for next iteration
        BlueXList.clear(); BlueYList.clear(); OrangeXList.clear(); OrangeYList.clear();
        gc.setEffect(null);
        gc.setGlobalBlendMode(BlendMode.SRC_OVER); //reset
    }
	private void CheckOrbs() {
		//Im not commenting ts
    	for (int i = 0; i < BlueXList.size(); i++) {
    		Double bx = BlueXList.get(i);
    	    Double by = BlueYList.get(i);
    	    Double ox = OrangeXList.get(i);
    	    Double oy = OrangeYList.get(i);
    	    double distBlue = Math.sqrt(Math.pow((playerX + 25) - (bx + 30), 2) + Math.pow((playerY + 25) - (by + 30), 2));
    	    double distOrange = Math.sqrt(Math.pow((playerX + 25) - (ox + 30), 2) + Math.pow((playerY + 25) - (oy + 30), 2));
    	    if (distBlue < 40) {
    	    	if (teleport == true && TeleportCooldown == 0) {
    	    		playerX = ox;
    	    		playerY = oy;
    	    		TeleportCooldown = 20;
    	    		playSound("woosh.mp3");
    	    	}
    	    }
    	    if (distOrange < 40) {
    	    	if (teleport == true && TeleportCooldown == 0) {
    	    		playerX = bx;
    	    		playerY = by;
    	    		TeleportCooldown = 20;
    	    		playSound("woosh.mp3");
    	    	}
    	    }
    	}
	}
	private void DrawBackground(GraphicsContext gc) {
	    gc.setFill(Color.BLACK);
	    gc.fillRect(0, 0, 601, 601); 

	    Color solidMoodColor = Color.BLACK;

	    if (CurrentLevel == 0) {
	        int g = (int)(20 + (BeatIntensity * 60)); 
	        solidMoodColor = Color.rgb(0, g, 20);
	    } 
	    else if (CurrentLevel == 1) {
	        int r = (int)(40 + (BeatIntensity * 60));
	        solidMoodColor = Color.rgb(r, 0, 0);
	    } 
	    else if (CurrentLevel == 2) {
	        int b = (int)(100 + (BeatIntensity * 60));
	        solidMoodColor = Color.rgb(0, 50, b);
	    } 
	    else if (CurrentLevel == 3) {
	        int r = (int)(40 + (BeatIntensity * 60));
	        int b = (int)(60 + (BeatIntensity * 60));
	        solidMoodColor = Color.rgb(r, 0, b);
	    }

	    gc.setFill(solidMoodColor);
	    gc.fillRect(0, 0, 601, 601); 
	    //grid pulse for the tutorial
	    if (CurrentLevel == 0) {
	        gc.setStroke(Color.rgb(0, 255, 50)); 
	        gc.setLineWidth(1 + (BeatIntensity * 3)); 
	        gc.setGlobalAlpha(0.1 + (BeatIntensity * 0.2)); 
	        for(int x = 0; x < 1000; x += 100) gc.strokeLine(x, 0, x, 800);
	        for(int y = 0; y < 800; y += 100) gc.strokeLine(0, y, 1000, y);
	    }
	    gc.setGlobalAlpha(1.0);
	}
    private void drawChallengeLevel(GraphicsContext gc) {
    	//Im not commenting ts
    	DrawBackground(gc);
    	DrawOrbs(gc);
    	if (Beats > 5){
	    	if (Beats > 96 && Loop > 0) {
	    		Loop--;
	    	} else if (Beats <= 96) {
	    		Loop++;
	    	}
	    	if (Loop > 101) {
	    		Loop = 101;
	    	}
	    	DrawNeonBlock(290, 0, 20, (int)Math.pow(1.0677,Loop), GLOW_CYAN, CORE_WHITE, false, true, gc);
            
            // ORB SCROLLING LOGIC
            // If the white wall is shrinking (Drop), move orbs down to simulate forward movement
            if (Beats > 96) {
            	for (int i = BlueYList.size() - 1; i >= 0; i--) {
            	    if (BlueYList.get(i) > 700) {
            	        BlueYList.remove(i);
            	        BlueXList.remove(i);
            	    }
            	}
            	for (int i = OrangeYList.size() - 1; i >= 0; i--) {
            	    if (OrangeYList.get(i) > 700) {
            	        OrangeYList.remove(i);
            	        OrangeXList.remove(i);
            	    }
            	}
            }
    	}

    	if (Beats >= 31 && Beats < 41) {
    		RedWall(0, ((Beats) - 32) * 100, 20, 290, gc);
    	}
    	if (Beats > 34 && Beats < 45) {
    		RedWall(((Beats) - 36) * 100, 300, 300, 20, gc);
    	}
    	if (Beats > 39 && Beats < 49) {
    		RedWall((46 - (Beats)) * 100, 0, 300, 20, gc);
    	}
    	if (Beats > 42 && Beats < 51) { 
    		GreenWall(0, ((Beats) - 44) * 120, 20, 600, gc);
    	}
    	if (Beats > 50 && Beats < 61) {
    		RedWall(0, ((Beats) - 52) * 100, 20, 150, gc);
    		RedWall(450, ((Beats) - 52) * 100, 20, 150, gc);
    	}
    	if (Beats > 54 && Beats < 65) {
    		RedWall((62 - (Beats)) * 100, 0, 150, 20, gc);
    		RedWall((62 - (Beats)) * 100, 450, 150, 20, gc);
    	}
    	if (Beats > 58 && Beats < 67) {
    		GreenWall(0, ((Beats) - 60) * 150, 20, 600, gc);
    		GreenWall(0, (64 - (Beats)) * 150, 20, 600, gc); 
    	}
    	if (Beats > 62 && Beats < 71) {
            RedWall(0, ((Beats) - 64) * 140, 20, 290, gc);
        }
        if (Beats > 66 && Beats < 75) {
            RedWall(310, ((Beats) - 68) * 140, 20, 290, gc);
        }
        if (Beats > 70 && Beats < 78) {
            GreenWall(0, ((Beats) - 72) * 160, 20, 600, gc);
        }
        if (Beats > 72 && Beats < 83) {
            RedWall(0, ((Beats) - 76) * 120, 20, 100, gc); 
            RedWall(0, (81 - (Beats)) * 120, 20, 100, gc); 
            
            RedWall(500, ((Beats) - 76) * 120, 20, 100, gc); 
            RedWall(500, (81 - (Beats)) * 120, 20, 100, gc); 
        }
        
        
        if (Beats > 78 && Beats < 88) {
            RedWall(((Beats) - 80) * 150 - 300, 90, 20, 250, gc);
            RedWall(((Beats) - 80) * 150 - 300, 490, 20, 250, gc);
        }
        if (Beats > 82 && Beats < 92) {
            RedWall(( 88 -(Beats)) * 150, 290, 20, 250, gc);
            RedWall(200, ((Beats) - 84) * 150, 20, 200, gc);
        }
        if (Beats > 86 && Beats < 97) {
            RedWall(0, ( 94 - (Beats)) * 100, 20, 600, gc);   
        }

        if (Beats > 92 && Beats < 103) {
        	RedWall(0, ((Beats) - 94) * 180 - 400, 20, 220, gc);
        	RedWall(380, ((Beats) - 94) * 180 - 400, 20, 220, gc);
        }
        
        if (Beats > 95 && Beats < 106) {
        	RedWall(200, ((Beats) - 96) * 180 - 400, 20, 200, gc);
        }
        
        if (Beats > 98 && Beats < 111) {
        	RedWall(((Beats) - 100) * 200 - 400, 0, 250, 20, gc);
        	RedWall(600 - (((Beats) - 100) * 200) + 400, 350, 250, 20, gc);
        }
        
        if (Beats > 102 && Beats < 113) {
        	GreenWall(0, ((Beats) - 104) * 200 - 300, 20, 600, gc);
        }
        if (Beats > 106 && Beats < 117) {
            RedWall(((Beats) - 108) * 140 - 200, 0, 200, 20, gc);
            RedWall(600 - (((Beats) - 108) * 140) + 200, 580, 200, 20, gc);
        }

        if (Beats > 110 && Beats < 121) {
            GreenWall(0, ((Beats) - 112) * 160 - 250, 20, 600, gc);
        }

        if (Beats > 114 && Beats < 125) {
            RedWall(0, (119 - (Beats)) * 120, 20, 200, gc);
            RedWall(580, (119 - (Beats)) * 120, 20, 200, gc);
        }

        if (Beats > 118 && Beats < 129) {
            RedWall(((Beats) - 120) * 160 - 300, 200, 20, 200, gc);
            RedWall(((Beats) - 120) * 160 - 300, 420, 20, 200, gc);
        }

        if (Beats > 122 && Beats < 133) {
            GreenWall(0, ((Beats) - 124) * 180 - 350, 20, 600, gc);
        }

        if (Beats > 126 && Beats < 137) {
            RedWall((132 - (Beats)) * 150, 0, 250, 20, gc);
            RedWall((132 - (Beats)) * 150, 580, 250, 20, gc);
        }

        if (Beats > 130 && Beats < 141) {
            RedWall(0, ((Beats) - 132) * 150 - 250, 20, 300, gc);
            RedWall(580, ((Beats) - 132) * 150 - 250, 20, 300, gc);
        }

        if (Beats > 134 && Beats < 145) {
            GreenWall(0, (139 - (Beats)) * 200, 20, 600, gc);
        }

        if (Beats > 138 && Beats < 149) {
            RedWall(((Beats) - 140) * 180 - 300, 100, 20, 200, gc);
            RedWall(((Beats) - 140) * 180 - 300, 400, 20, 200, gc);
        }

        if (Beats > 142 && Beats < 153) {
            GreenWall(0, ((Beats) - 144) * 220 - 400, 20, 600, gc);
        }

    }

	private void TutorialLogic() {
		//Im not commenting ts
	    System.out.println(Beats);
		SpokenToMaster = true;
		CurrentLevel = 0;
    	playerSpeed = 7;
		GameState = 2;
        PortalChangeLoop += 0.03;
        if (PortalChangeLoop > 359) {
        	PortalChangeLoop = 0;
        }
        //Pulsing effect
        PortalSize = BeatIntensity * 10;
		if (TeleportCooldown > 0) {
			TeleportCooldown--;
		}
		if (PhaseCooldown == 0) {
			if (interact == true) {
				PhaseTime = 30;
				PhaseCooldown = 90;
			}
		} else {
			PhaseCooldown--;
		}
		if(PhaseTime > 0) {
			PhaseTime--;
		}
		double songTime = mediaPlayer.getCurrentTime().toSeconds();
		Beats = songTime / secondsPerBeat;
		AddOrb(-100,-100,-100,-100); //Orb of doom and despair that is needed and cannot be removed or else the level drawing breaks. Don't question it.
		if (Beats > 11) {
		    // Same math, just clamped so it finishes smoothly instead of snapping
		    double t = ((Beats) - 11) / 9.0;

		    if (t > 1) t = 1;
		    if (t < 0) t = 0;

		    AddOrb(
		        (t * 195) - 75,
		        (Math.sin(PortalChangeLoop)) * 250 + 270,
		        620 - (t * 195),
		        (Math.sin(PortalChangeLoop) * -1) * 250 + 270
		    );
		}

		CheckOrbs();
		
	}
	
}
//stupid 1500 lines of code jusr bc i dont rly know how to use classes and packages that well.