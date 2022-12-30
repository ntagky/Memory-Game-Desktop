package ui;

import Classes.HighscoreData;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * <b><i>HighscoresUI</i> class is in charge for displaying the top scores.</b>
 */
public class HighscoresUI {

    private static final String highscoreFile = "highscoreFile.txt";
    private JPanel highscorePanel;
    private HighscoreData highscoreData;
    private JPanel menuPanel;
    private JLabel[] nameLabel;
    private JLabel[] scoreLabel;
    private JLabel[] stepsLabel;
    private int sizeX;
    private int sizeY;
    private String[] names;
    private int[] scores;
    private int[] steps;
    private JFrame frame;

    /**
     * Makes a panel with the top 10 players.
     * @param frame Current frame.
     * @param sizeX Frames width.
     * @param sizeY Frames height.
     * @param menuPanel Menu panel.
     * @param highscoreData Data with top high scores.
     */
    public HighscoresUI(JFrame frame, int sizeX, int sizeY, JPanel menuPanel, HighscoreData highscoreData){
        this.menuPanel = menuPanel;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.frame = frame;
        this.highscoreData = highscoreData;

        getData();

        highscorePanel = new JPanel(new GridLayout(12, 1));
        highscorePanel.setBounds(0, 0, sizeX, sizeY);
        nameLabel = new JLabel[10];
        scoreLabel = new JLabel[10];
        stepsLabel = new JLabel[10];
        makeTitleBar();

        for (int i=0; i<10; i++)
            makeBoardPanel(i);

        highscorePanel.setBackground(Color.cyan);
        highscorePanel.setVisible(true);
        frame.add(highscorePanel);
    }

    /**
     * Cleaning the leaderboard file.
     */
    private void cleanLdrbrd(){
        getData();
        updateLeaderboard();
    }

    /**
     * Retrieve data from file.
     */
    private void getData(){
        names = highscoreData.getNames();
        scores = highscoreData.getScores();
        steps = highscoreData.getSteps();
    }

    /**
     * Update the leaderboard panel.
     */
    private void updateLeaderboard(){
        for (int i=0; i<10; i++){
            nameLabel[i].setText(names[i]);
            scoreLabel[i].setText(String.valueOf(scores[i]));
            stepsLabel[i].setText(String.valueOf(steps[i]));
        }
    }

    /**
     * Creates the panel's header.
     */
    private void makeTitleBar(){
        JPanel firstRowPanel = new JPanel(new BorderLayout());

        JButton back2MenuBtn = new JButton(MenuUI.texts.getString("menu"));
        back2MenuBtn.setPreferredSize(new Dimension(sizeX/5, 0)); //doesn't matter the height's value
        back2MenuBtn.addActionListener(e -> {
            highscorePanel.setVisible(false);
            menuPanel.setVisible(true);
        });
        firstRowPanel.add(back2MenuBtn, BorderLayout.LINE_START);

        JLabel highscoreLabel = new JLabel(MenuUI.texts.getString("highscores"), JLabel.CENTER);
        highscoreLabel.setFont(highscoreLabel.getFont().deriveFont((float)highscoreLabel.getFont().getSize()+4));
        firstRowPanel.add(highscoreLabel, BorderLayout.CENTER);

        JButton cleanLeaderboad = new JButton(MenuUI.texts.getString("deleteLdrbrd"));
        cleanLeaderboad.setPreferredSize(new Dimension(sizeX/5, 0));
        cleanLeaderboad.addActionListener(e -> {
            int option = JOptionPane.showOptionDialog(frame, MenuUI.texts.getString("areUsure"), MenuUI.texts.getString("deleteLdrbrd!"), JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if (option == 0)
                if (new File(highscoreFile).delete()){
                    highscoreData.cleanLeaderboard();
                    cleanLdrbrd();
                }

        });
        firstRowPanel.add(cleanLeaderboad, BorderLayout.LINE_END);

        highscorePanel.add(firstRowPanel);
    }

    /**
     * Passes data to panel.
     * @param position The current row to be created.
     */
    private void makeBoardPanel(int position){
        int pixelsX;
        //Position in scoreboard
        JPanel rowPanel = new JPanel(null);
        JLabel numberLabel = new JLabel(Integer.toString(position + 1), JLabel.CENTER);
        numberLabel.setFont(new Font(null));
        numberLabel.setBounds(0, 0, sizeX/6, sizeY/12);
        numberLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        rowPanel.add(numberLabel);

        //Name of player
        pixelsX = sizeX/6;
        nameLabel[position] = new JLabel(names[position], JLabel.CENTER);
        nameLabel[position].setBounds(pixelsX, 0, sizeX/6*3, sizeY/12);
        nameLabel[position].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        nameLabel[position].setFont(new Font(null));
        rowPanel.add(nameLabel[position]);

        //Player's score
        pixelsX+=sizeX/6*3;
        scoreLabel[position] = new JLabel(String.valueOf(scores[position]), JLabel.CENTER);
        scoreLabel[position].setBounds(pixelsX, 0, sizeX/6, sizeY/12);
        scoreLabel[position].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        scoreLabel[position].setFont(new Font(null));
        rowPanel.add(scoreLabel[position]);

        //Steps taken to complete the game
        pixelsX+=sizeX/6;
        stepsLabel[position] = new JLabel(String.valueOf(steps[position]), JLabel.CENTER);
        stepsLabel[position].setBounds(pixelsX, 0, sizeX/6, sizeY/12);
        stepsLabel[position].setBorder(BorderFactory.createLineBorder(Color.BLACK));
        stepsLabel[position].setFont(new Font(null));
        rowPanel.add(stepsLabel[position]);

        if (position<3){
            numberLabel.setFont(numberLabel.getFont().deriveFont((float)numberLabel.getFont().getSize()+(3-position)));
            numberLabel.setFont(numberLabel.getFont().deriveFont(Font.BOLD));
            nameLabel[position].setFont(numberLabel.getFont().deriveFont((float)numberLabel.getFont().getSize()+(3-position)));
            nameLabel[position].setFont(numberLabel.getFont().deriveFont(Font.BOLD));
            stepsLabel[position].setFont(numberLabel.getFont().deriveFont((float)numberLabel.getFont().getSize()+(3-position)));
            stepsLabel[position].setFont(numberLabel.getFont().deriveFont(Font.BOLD));
            scoreLabel[position].setFont(numberLabel.getFont().deriveFont((float)numberLabel.getFont().getSize()+(3-position)));
            scoreLabel[position].setFont(numberLabel.getFont().deriveFont(Font.BOLD));
        }

        rowPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        highscorePanel.add(rowPanel);
    }

}
