
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;

/**
 * A guessing game which has various difficulties
 *
 * @author seymour
 */
public class Game extends javax.swing.JFrame {

    // Global variables
    BufferedImage correct, incorrect, firstTry, prince;
    JFrame frame = new JFrame();
    
    boolean hints;
    String text, str, userHint;
    int randNum, maxAmount, maxNum, hintChoice, diff, reset, difficulty;
    int userGuess = -1;
    int attempts = 0;
    String[] types = { "Easy: 1-10", "Medium: 1-50", "Hard: 1-100", "Extreme: 1-1000000", "Custom" }; // Difficulty types
    
    ImageIcon goodbye = new ImageIcon("./img/goodbye.png");
    ImageIcon error = new ImageIcon("./img/error.png");
    ImageIcon yes = new ImageIcon("./img/yes.png");
    ImageIcon thinking = new ImageIcon("./img/thinking.png");
    
    
    public Game() {
        initComponents();
        
        // Loading images
        File correctLoc = new File("./img/correct.png");
        File incorrectLoc = new File("./img/incorrect.png");
        File firstTryLoc = new File("./img/firstTry.png");
        File princeLoc = new File("./img/prince.png");
        
        // Assigning images to their variables
        try {
            correct = ImageIO.read(correctLoc);
            incorrect = ImageIO.read(incorrectLoc);
            firstTry = ImageIO.read(firstTryLoc);
            prince = ImageIO.read(princeLoc);
        }
        catch (Exception e) {
            System.out.println("An error has occurred. One or more images does not exist.");
        }
        
        // Start the guessing
        run();
    }
    
    public void run() {
        hints = false;
        userGuess = -1;
        attempts = 0;
        
        difficulty = JOptionPane.showOptionDialog(null, "Choose a difficulty level!", "Game Select", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, thinking, types, types[0]);
        
        switch(difficulty) { // Sets the max number amount and max number of guesses
            case 0: // Easy
                maxNum = 10;
                maxAmount = 3;
                break;
            case 1: // Medium
                maxNum = 50;
                maxAmount = 5;
                break;
            case 2: // Hard
                maxNum = 100;
                maxAmount = 10;
                break;
            case 3: // Expert
                maxNum = 1000000;
                maxAmount = 1;
                break;
            case 4: // Custom
                String[] customs = { "What is the highest number?", "How many guesses do you get?" };
                int i = 0;
                while (i < 2) {
                    try {
                        str = input(customs[i]);
                        if (str == null) { // Closes the program
                            JOptionPane.showMessageDialog(frame, "See you later!", "", JOptionPane.ERROR_MESSAGE, goodbye);
                            System.exit(0);
                        } else {
                            if (i == 0) {
                                maxNum = Math.abs(Integer.parseInt(str));
                                if (maxNum == 0) {
                                    JOptionPane.showMessageDialog(frame, "Number cannot be zero!", "Bad input!", JOptionPane.ERROR_MESSAGE, error);
                                } else i++;
                            } else {
                                maxAmount = Math.abs(Integer.parseInt(str));
                                if (maxAmount == 0) {
                                    JOptionPane.showMessageDialog(frame, "Number cannot be zero!", "Bad input!", JOptionPane.ERROR_MESSAGE, error);
                                } else i++;
                            }
                        }
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(frame, "That's not a number!", "Bad input!", JOptionPane.ERROR_MESSAGE, error);
                    }
                }
                break;
            default: // Closes the program
                JOptionPane.showMessageDialog(frame, "See you later!", "", JOptionPane.ERROR_MESSAGE, goodbye);
                System.exit(0);
        }
        
        // Ask for hints (not for Expert mode)
        if (maxAmount != 1) hintChoice = JOptionPane.showConfirmDialog(null, "Enable hints?", "Game Select", JOptionPane.YES_NO_OPTION, 0, thinking);
        hints = hintChoice == JOptionPane.YES_OPTION; // Enables hints if hintChoice is "YES"
        randNum = (int) (Math.random() * maxNum) + 1; // Generate a random number depending on your chosen difficulty level
        
        
        // Guessing input
        while (attempts < maxAmount) {
            text = "Guess a number between 1 and " + maxNum + "\n(" + (maxAmount - attempts) + ((attempts + 1) == maxAmount ? " guess" : " guesses") + " left)";
            try {
                // ---HINTS SETUP---
                if (hints == false) { // Hints disabled
                    str = input((attempts >= 1 ? "Incorrect! " : "") + text);
                } else { // Hints enabled
                    diff = Math.abs(userGuess - randNum); // Each difficulty has its own range for hot/cold except easy and expert
                    switch(difficulty) {
                        case 0: // Easy
                            userHint = (userGuess > randNum ? "Too high! " : "Too low! ");
                            break;
                        case 1: // Medium
                            userHint = (diff <= 4 ? "Hot! " : "Cold! ");
                            break;
                        case 2: // Hard
                            userHint = (diff <= 7 ? "Hot! " : "Cold! ");
                            break;
                        case 3: // Expert
                            break;
                        case 4: // Custom
                            userHint = (diff <= (maxNum / 5 / 2) ? "Hot! " : "Cold! ");
                            break;
                    }
                    str = input((attempts >= 1 ? userHint : "") + text);
                }
                
                // ---GUESSING---
                if (str == null) {// If the input is "Cancel" or X, the stop the guessing
                    JOptionPane.showMessageDialog(frame, "See you later!", "", JOptionPane.ERROR_MESSAGE, goodbye);
                    System.exit(0);
                    break;
                } else if (str != null) { // If it's not "Cancel" or X...
                    if ("sharkalien".equals(str.toLowerCase())) { // Cheat code
                        break;
                    }
                    userGuess = Integer.parseInt(str); // Try to convert it to a number
                    if (userGuess > maxNum || userGuess < 1) { // If the guess is larger than the max number, then alert them
                        JOptionPane.showMessageDialog(frame, "You number is " + (userGuess < 1 ? "too low" : "too high") + "!", "Bad input!", JOptionPane.ERROR_MESSAGE, error);
                    } else { // Add one to the attempts if there are no problems with the guess
                        attempts++;
                    }
                }
                if (userGuess == randNum) break; // If the guess is the same as the random number, the stop the loop
            }
            catch(Exception e) {
                JOptionPane.showMessageDialog(frame, "That's not a valid number!", "Bad input!", JOptionPane.ERROR_MESSAGE, error); // Let the user know their input is invalid
            }
        }
        
    }
    
    public String input(String txt) {
        return JOptionPane.showInputDialog(txt);
    }
    
    public void replay() {
        try {
            Thread.sleep(3000);
        } catch (Exception e) {
            
        }
        
        reset = JOptionPane.showConfirmDialog(null, "Play again?", "Game", JOptionPane.YES_NO_OPTION, 0, yes);
            
        if (reset == JOptionPane.YES_OPTION) {
            run();
            repaint();
        }
    }
    
    public void paint(Graphics g) {
        // Makes background gray to prevent the weird transparent window issue
        Color gray = new Color(214, 217, 223);
        g.setColor(gray);
        g.fillRect(0, 0, 400, 400);
        
        // Guess checking
        if ("sharkalien".equals(str.toLowerCase())) { // secret
            label1.setText("It was a dark and stormy night...");
            g.drawImage(prince, 5, 15, 300, 300, this);
        } else {
            if (userGuess == randNum) {
                if (attempts == 1) { // Guessed on first try
                    if (difficulty == 3) {
                        label1.setText("One in a million, baby!");
                    } else {
                        label1.setText("First try, idiot!");
                    }
                    g.drawImage(firstTry, 3, 30, this);
                }
                else { // Guessed correct, but not on first try
                    String result;
                    switch (attempts) {
                        case 2:
                            result = "Pretty good!";
                            break;
                        case 3:
                            result = "Keep it up!";
                            break;
                        case 4:
                            result = "Good, but you can do better.";
                            break;
                        case 5:
                            result = "Satisfactory.";
                            break;
                        case 6:
                            result = "Hmm...";
                            break;
                        case 7:
                            result = "Never give up.";
                            break;
                        case 8:
                            result = "You can do better.";
                            break;
                        case 9:
                            result = "A pitiful, but successful attempt.";
                            break;
                        default:
                            result = "Pathetic.";
                    }
                    result += " Answer guessed in " + attempts + (attempts == 1 ? " attempt." : " attempts.");
                    label1.setText(result);
                    g.drawImage(correct, 20, 50, this);
                }
            } else {
                label1.setText("Sorry, try again! The correct number was " + randNum + ".");
                g.drawImage(incorrect, 70, 30, this);
            }
            
            // Asks the user if they want to play again.
            // For some reason, brings up twice, but only the first time.
            replay();
        }
        
    }
    
    //<editor-fold desc=" Default NetBeans code " defaultstate="collapsed">
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label1 = new java.awt.Label();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Number Guessing Game");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setResizable(false);

        label1.setAlignment(java.awt.Label.CENTER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(label1, javax.swing.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(276, Short.MAX_VALUE)
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Game.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Game().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private java.awt.Label label1;
    // End of variables declaration//GEN-END:variables
    //</editor-fold>
}
