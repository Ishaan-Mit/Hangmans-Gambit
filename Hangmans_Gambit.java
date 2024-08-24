import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

/**
 * Made by Ishaan Mittal
 */
public class Hangmans_Gambit {
    private static JFrame frame;

    private final static int WIDTH = 325;
    private final static int HEIGHT = 350;

    private static JButton startButton;

    private static JLabel title;
    private static JLabel hangman;
    private static JLabel wordDisplay;
    private static JPanel buttonsPanel;

    private static JButton[] JButtons = new JButton[26];
    private final static String[] WORDS = /* Not Case Sensitive */ {"UNDER THE FLOOR", "IRON SKEWER", "BAMBOO SWORD", "WALL PAPER", "FILMING LOCATION", "CONFERENCE ROOM", "ELEVATOR", "DELUXE ROOMS", "CAUSE OF DEATH", "FIRE GRENADE", "VIRTUAL", "KILL US ALL", "OCTAGON"};
    
    private final static String[] ARTS = /* A lot of HTML */ {
        /* 0empty */    "<html><p>_________<br>||<br>||<br>||<p/><html/>",
        /* 1+head */    "<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||<br>||<p/><html/>",
        /* 2+torso */   "<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||&emsp;|<br>||<p/><html/>",
        /* 3+arm */     "<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||&ensp;&nbsp;/|<br>||<p/><html/>",
        /* 4+otherArm */"<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||&ensp;&nbsp;/|\\<br>||<p/><html/>",
        /* 5+leg */     "<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||&ensp;&nbsp;/|\\<br>||&ensp;&nbsp;/<p/><html/>",
        /* 6+otherLeg */"<html><p>_________<br>||&#8202&#8201&nbsp;&ensp;o<br>||&ensp;&nbsp;/|\\<br>||&ensp;&nbsp;/&nbsp;\\<p/><html/>"};
    
    private final static String[] LETTERS = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};

    private final static String chosenWord = WORDS[(int)((Math.random()*WORDS.length))];
    private static String[] word = new String[chosenWord.length()]; /* The word */
    private static String[] shown = new String[chosenWord.length()];

    private static String shownWord = "";
    private static int currentMan = 0;

    public Hangmans_Gambit() {
        initGame();
    }

    public static void initGame() {
        frame = new JFrame("HANGMAN'S GAMBIT"); /* Frame */
        frame.setSize(WIDTH+15, HEIGHT+38);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        startButton = new JButton("START GAME!");
        startButton.setBounds(-10, -10, WIDTH+20, HEIGHT+20);
        frame.add(startButton);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){  
                startButton.setText("Starting...");
                startButton.setVisible(false);
                startGame();
            }
        });
    }

    public static void startGame() {
        title = new JLabel("HANGMAN'S GAMBIT", JLabel.CENTER);
        title.setBounds(0, 5, WIDTH, 25);
        title.setFont(new Font("Comic Sans", Font.BOLD, 22));
        frame.add(title);
        
        hangman = new JLabel(ARTS[currentMan], JLabel.CENTER); /*  Hangman Box */
        hangman.setFont(new Font("Comic Sans", Font.PLAIN, 18));
        hangman.setBounds(0, 30, WIDTH, 100);
        frame.add(hangman);
        
        for (int i = 0; i < chosenWord.length(); i++) { /* Make the word and shown */
            if (chosenWord.substring(i, i+1).equals(" ")){
                shown[i] = " ";
            }
            else {
                shown[i] = "_";
            }
            word[i] = chosenWord.substring(i, i+1).toUpperCase();
        }
        for (String i : shown) {
            shownWord += i + " ";
        }
        wordDisplay = new JLabel(shownWord, JLabel.CENTER);
        wordDisplay.setBounds(0, 135, WIDTH, 35);
        wordDisplay.setFont(new Font("Comic Sans", Font.PLAIN, 18));
        frame.add(wordDisplay);
        
        makeButtons();
    }

    public static void makeButtons() {
        buttonsPanel = new JPanel(new GridLayout(4, 7, 5, 5)); /* Area for Buttons */
        buttonsPanel.setBounds(5, 180, WIDTH-10, 165);
        
        for (int row = 0; row < 4; row ++) { /* The buttons */
            for (int column = 0; column < 7; column ++) {
                int selection = row*7+column;
                if (selection>25) {break;}
                if (selection == 21) {buttonsPanel.add(new JButton("")).setVisible(false);}
                
                JButton button = new JButton(LETTERS[selection].toUpperCase());
                JButtons[selection] = button;
                
               /* for (String correct : word) {if (button.getText().equalsIgnoreCase(correct)) {button.setBackground(Color.green);}} */

                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e){
                        buttonPressed(button.getText());
                        button.setVisible(false);
                        randomize();
                    }
                });

                button.setBorder(BorderFactory.createLineBorder(Color.black));
                
                buttonsPanel.add(button);
            }
        }
        frame.add(buttonsPanel);
        randomize();
    }

    private static void buttonPressed(String input) {
        boolean correct = false;
        for (int i = 0; i < word.length; i ++) {
            if (input.equals(word[i])) {
                shown[i] = input;
                correct = true;
            }
        }
        if (!correct) {
            currentMan++;
            hangman.setText(ARTS[currentMan]);
        }
        shownWord = "";
        for (String i : shown) {
            shownWord += i + " ";
        }
        wordDisplay.setText(shownWord);
    }

    public static void randomize() {
        boolean atLeastOne = false;
        boolean done = true;

        for (String item : shown) {
            if (item.equals("_")) {
                done = false;
            }
        }

        while (!atLeastOne && !done) {
            for (JButton button : JButtons) {
                button.setEnabled(false);
                
                for (String correctLetter : word) {
                    if (button.isVisible() && Math.random()*10 < 2.0 && button.getText().equalsIgnoreCase(correctLetter)) {
                        button.setEnabled(true);
                        atLeastOne = true;
                    }
                }
                
                if (Math.random()*10 < 4.0) {
                    button.setEnabled(true);
                }
            }
        }
        if (currentMan >= 6 || done) {
            buttonsPanel.setVisible(false);
        }
    }
    public static void main(String[] args) {
        new Hangmans_Gambit(); 
    }
}