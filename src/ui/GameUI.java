package ui;

import logic.GameLogic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * <b><i>GameUI</i> class is in charge for the game's user interface.</b>
 */
public class GameUI extends SelectPlayerUI {

    private JPanel gamePanel;
    private JButton[][] cardsButtons;
    private Image[][] resized;
    private URL[][] cards;
    private JLabel[] scoreLabel;

    private int counter;
    private int duelCounter;
    private int selectedRow;
    private int selectedCol;

    private Image backCardImage;

    private int row;
    private int col;
    private int diffCards;
    private int copies;
    private int totalPlayers;
    private boolean duel;

    private boolean flag;
    private boolean correctBotGuess;
    private URL previousCard;
    private boolean botPlaying;
    private int playerPlaying;
    private boolean[][] opened;

    private JLabel[] nameLabel;

    private GameLogic gameLogic;

    /**
     * Constructor setting up the game.
     */
    public GameUI(){

        selectGameBttn.addActionListener(e -> {
            if (valid > 0) {
                setUpGame();
            }
        });

        newGame.addActionListener(e -> {
            if (currentPanel == gamePanel){
                String[] buttons = {texts.getString("newGame"), texts.getString("cancel")};
                int x = JOptionPane.showOptionDialog(frame, texts.getString("exitGame"), texts.getString("escape"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, buttons, null);
                if (x == 0){
                    gamePanel.setVisible(false);
                    makeFrameSmall();
                    startGamePanel.setVisible(true);
                }
            }else {
                currentPanel.setVisible(false);
                startGamePanel.setVisible(true);
            }

        });
    }

    /**
     * Makes the frame depending on game mode data.
     */
    private void setUpGame(){
        getModeData();

        gameLogic = new GameLogic(mode, mode.getPlayerNames(), highscoreData, duel);
        cards = gameLogic.initCards(row, col, diffCards, copies);
        loadCards();

        counter =0;
        duelCounter =0;
        playerPlaying=0;

        makeMasterPanel();
        currentPanel = gamePanel;

        //design
        makePlayersPanel();
        cardsButtons = new JButton[row][col];
        for (int i=0; i<row; i++) {
            makeEachRowPanel(i);
        }

        flag = true; //used to dont open cards when on delay
        botPlaying = false;

        makeFrameFullscreen();
        gamePanel.setVisible(true);
        frame.add(gamePanel);

        showCards();
    }

    /**
     * Created the master panel.
     */
    private void makeMasterPanel(){
        gamePanel = new JPanel();
        gamePanel.setBounds(0, 0, (int) screenSize.getWidth(), (int) screenSize.getHeight());
        gamePanel.setBackground(Color.BLACK);
        GridLayout masterLayout = new GridLayout(row+2, 1, 0, 5);
        gamePanel.setLayout(masterLayout);
    }

    /**
     * Created the heading panel with the names and scores.
     */
    private void makePlayersPanel(){
        JPanel firstRowPanel = new JPanel(new GridLayout(1, mode.getTotalPlayers()));
        JPanel[] eachPlayerPanel = new JPanel[ mode.getTotalPlayers()];
        nameLabel = new JLabel[mode.getTotalPlayers()];
        scoreLabel = new JLabel[ mode.getTotalPlayers()];
        GridLayout gridLayout = new GridLayout(2, 1);
        for (int i=0; i< mode.getTotalPlayers(); i++){
            if (i< mode.getRealPlayers())
                nameLabel[i] = new JLabel(String.valueOf(mode.getPlayerNames(i)), JLabel.CENTER);
            else
                nameLabel[i] = new JLabel(mode.getPlayerNames(i), JLabel.CENTER);
            nameLabel[i].setFont(nameLabel[i].getFont().deriveFont((float)nameLabel[i].getFont().getSize()+10));
            nameLabel[i].setFont(nameLabel[i].getFont().deriveFont(Font.ITALIC));
            scoreLabel[i] = new JLabel("1000", JLabel.CENTER);
            scoreLabel[i].setFont(nameLabel[i].getFont());
            eachPlayerPanel[i] = new JPanel(gridLayout);
            eachPlayerPanel[i].setBackground(Color.white);
            eachPlayerPanel[i].add(nameLabel[i]);
            eachPlayerPanel[i].add(scoreLabel[i]);
            eachPlayerPanel[i].setBorder(BorderFactory.createLineBorder(Color.BLACK));
            if (i==0){
                nameLabel[0].setFont(nameLabel[0].getFont().deriveFont((float)nameLabel[0].getFont().getSize()+5));
                nameLabel[0].setFont(nameLabel[0].getFont().deriveFont(Font.BOLD));
                scoreLabel[0].setFont(nameLabel[0].getFont());
                if (!duel)
                    gameLogic.addStepToPlayer(0);
            }
            firstRowPanel.add(eachPlayerPanel[i]);
        }
        gamePanel.add(firstRowPanel);
    }

    /**
     * Created the table panel with the cards.
     * @param row The number of table's rows.
     */
    private void makeEachRowPanel(int row){
        JPanel rowPanel = new JPanel(new GridLayout(1, col, 0, 0));
        rowPanel.setBackground(null);
        backCardImage = null;
        Image img = null;
        try {
            img = ImageIO.read(getClass().getResource("/res/Image/backCards.jpg"));
            backCardImage = img.getScaledInstance((int) (screenSize.getWidth())/col, (int) screenSize.getHeight()/this.row, Image.SCALE_DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int j=0; j<col; j++){
            cardsButtons[row][j] = new JButton();
            if (img != null)
                cardsButtons[row][j].setIcon(new ImageIcon(backCardImage));
            cardsButtons[row][j].setActionCommand(String.valueOf(row*100 + j));
            cardsButtons[row][j].setBorder(BorderFactory.createLineBorder(Color.black));
            rowPanel.add(cardsButtons[row][j]);
        }

        gamePanel.add(rowPanel);
    }

    /**
     * Makes the frame fullscreen.
     */
    private void makeFrameFullscreen(){
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /**
     * Loading the cards from directory and store them.
     */
    private void loadCards(){
        resized = new Image[mode.getRows()][mode.getCols()];
        opened = new boolean[row][col];
        for (int i=0; i<mode.getRows(); i++)
            for (int j=0; j<mode.getCols(); j++) {
                try {
                    Image img;
                    img = ImageIO.read(cards[i][j]);
                    resized[i][j] = img.getScaledInstance((int) (screenSize.getWidth()) / 8, (int) screenSize.getHeight() / 8, Image.SCALE_DEFAULT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                opened[i][j] = false;
            }
    }

    /**
     * Finds which button was pressed and reveals the card icon.
     * @param event Button pressed.
     */
    private void revealCard(ActionEvent event){
        if (flag && !botPlaying){
            String position = event.getActionCommand();
            selectedRow = Integer.valueOf(position) / 100;
            selectedCol = Integer.valueOf(position) % 100;

            if (!gameLogic.selectSameCard(counter%copies, selectedRow, selectedCol)){
                cardsButtons[selectedRow][selectedCol].setIcon(new ImageIcon(resized[selectedRow][selectedCol]));
                boolean correct = gameLogic.openCard(counter % copies, selectedRow, selectedCol);
                previousCard = cards[selectedRow][selectedCol];

                if (counter%copies==copies-1){
                    scoreLabel[playerPlaying].setText(gameLogic.endPlayerRound(playerPlaying, correct));
                    if (correct){
                        int[] correctRows = gameLogic.getPreviousRow();
                        int[] correctCols = gameLogic.getPreviousCol();
                        for (int i=0; i<copies; i++){
                            cardsButtons[correctRows[i]][correctCols[i]].setEnabled(false);
                            opened[correctRows[i]][correctCols[i]] = true;
                        }
                        gameOver(gameLogic.addRevealedCard(correctRows, correctCols));
                        previousCard = null;
                        if (duel){
                            changePlayer();
                            changeDuelPlayer(playerPlaying==0);
                        }
                    }else{
                        turnDownCards(gameLogic.getPreviousRow(), gameLogic.getPreviousCol());
                    }
                }else
                if (duel){
                    duelCounter++;
                    if (botPlaying)
                        flag = false;
                    changePlayer();
                    if (!correctBotGuess){
                        changeDuelPlayer(playerPlaying==0);
                        enableSpecificCard(selectedRow, selectedCol);
                    }else
                        changePlayer();
                }
                counter++;
            }
        }
    }

    /**
     * Turn down the opened cards.
     * @param wrongRows Opened cards' rows.
     * @param wrongCol Opened cards' columns.
     */
    private void turnDownCards(int[] wrongRows, int[] wrongCol){
        flag = false;

        for (int i=0; i<row; i++)
            for (int j=0; j<col; j++)
                if (duel && !opened[i][j])
                    cardsButtons[i][j].setEnabled(true);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<copies; i++)
                    cardsButtons[wrongRows[i]][wrongCol[i]].setIcon(new ImageIcon(backCardImage));
                flag = true;
                if (!duel){
                    changePlayer();
                }else{
                    changeDuelPlayer(playerPlaying==0);
                }
            }
        }, 3000);

    }

    /**
     * Revealing the cards for a couple of seconds before the game begins.
     */
    private void showCards()
    {

        class ShowCards extends TimerTask {

            private final int row;
            private final int col;

            public ShowCards(int row, int col) {
                this.row = row;
                this.col = col;
            }

            @Override
            public void run() {
                for (int i=0; i<row; i++)
                    for (int j=0; j<col; j++){
                        cardsButtons[i][j].setIcon(new ImageIcon(backCardImage));
                        cardsButtons[i][j].addActionListener(GameUI.this::revealCard);
                    }
                if (duel)
                    for (int i=0; i<row; i++)
                        for (int j=0; j<col/2; j++) {
                            cardsButtons[i][j].setEnabled(true);
                            cardsButtons[i][j + (col / 2)].setEnabled(false);
                        }
            }
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                for (int i=0; i<row; i++)
                    for (int j=0; j<col; j++)
                        cardsButtons[i][j].setIcon(new ImageIcon(resized[i][j]));

            }
        }, 2000);

        timer.schedule(new ShowCards(row, col), 4000);

    }

    /**
     * Enable the specific card.
     * @param specificRow Card's row.
     * @param specificCol Card's column.
     */
    private void enableSpecificCard(int specificRow, int specificCol){
        cardsButtons[specificRow][specificCol].setEnabled(true);
    }

    /**
     * Change player playing when game mode is "The Ultimate Duel".
     * @param firstPlayer first player playing.
     */
    private void changeDuelPlayer(boolean firstPlayer){
        for (int i=0; i<row; i++)
            for (int j=0; j<col/2; j++) {
                cardsButtons[i][j].setEnabled(firstPlayer);
                cardsButtons[i][j + (col / 2)].setEnabled(!firstPlayer);
                if (opened[i][j])
                    cardsButtons[i][j].setEnabled(false);
                if (opened[i][j + (col / 2)])
                    cardsButtons[i][j + (col / 2)].setEnabled(false);
            }
    }

    /**
     * Change player playing, adding steps to player.
     */
    private void changePlayer(){
        if (totalPlayers>1){
            changeLabelsStyle();
        }

        if (!duel)
            gameLogic.addStepToPlayer(playerPlaying);
        else{
            if (duelCounter%(copies-1)==0){
                gameLogic.addStepToPlayer(playerPlaying);
            }
        }

        if (gameLogic.changePlayer()){
            botPlaying = true;
            botIsPlaying(0);
            botPlaying = false;
        }
    }

    /**
     * Changes font of the player playing, so it's easier to see who is playing.
     */
    private void changeLabelsStyle(){
        nameLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont().deriveFont((float)nameLabel[playerPlaying].getFont().getSize()-5));
        nameLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont().deriveFont(Font.ITALIC));
        scoreLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont());
        playerPlaying= (playerPlaying + 1)%totalPlayers;
        nameLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont().deriveFont((float)nameLabel[playerPlaying].getFont().getSize()+5));
        nameLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont().deriveFont(Font.BOLD));
        scoreLabel[playerPlaying].setFont(nameLabel[playerPlaying].getFont());
    }

    /**
     * Is in charge for the bot player. Choosing a card combination and revealing the cards.
     * @param counter The player playing position.
     */
    private void botIsPlaying(int counter) {

        boolean playAgain;
        int[][] selections;
        Timer timer = new Timer();
        class OpenBotsCards extends TimerTask {

            private final int position;
            private boolean duel;
            private final int[][] selections;

            public OpenBotsCards(int position, int[][] selections) {
                this.position = position;
                this.selections = selections;
                this.duel = false;
            }

            public OpenBotsCards(int position, int[][] selections, boolean duel) {
                this.position = position;
                this.selections = selections;
                this.duel = duel;
            }

            @Override
            public void run() {
                cardsButtons[selections[position][0]][selections[position][1]].setIcon(new ImageIcon(resized[selections[position][0]][selections[position][1]]));
                if (duel)
                    cardsButtons[selections[position][0]][selections[position][1]].setEnabled(true);
                try {
                    Thread.sleep((position+1)*1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (!duel){
            do {
                selections = gameLogic.botSelections();

                for (int i = 0; i < copies; i++)
                    timer.schedule(new OpenBotsCards(i, selections), (i + 1) * 1000);

                playAgain = gameLogic.correctCombination(selections);
                if (!playAgain) {
                    try {
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    int[] rows = new int[copies];
                    int[] cols = new int[copies];
                    for (int i = 0; i < copies; i++) {
                        rows[i] = selections[i][0];
                        cols[i] = selections[i][1];
                    }
                    turnDownCards(rows, cols);
                } else {
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < copies; i++)
                        cardsButtons[selections[i][0]][selections[i][1]].setEnabled(false);
                    try {
                        Thread.sleep(1000);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    gameOver(gameLogic.addRevealedCard());
                }

                scoreLabel[playerPlaying].setText(gameLogic.endPlayerRound(playerPlaying, playAgain));

            }while (playAgain);

        }else{
            selections = gameLogic.botSelection(previousCard);
            this.counter++;
            duelCounter++;
            timer.schedule(new OpenBotsCards(0, selections, duel),(counter + 1) * 2000);
            if (previousCard!=null){
                correctBotGuess = gameLogic.correctCombination(selections, previousCard);
                previousCard = null;
                if (!correctBotGuess) {
                    int[] rows = new int[copies];
                    int[] cols = new int[copies];
                    rows[0] = selections[0][0];
                    cols[0] = selections[0][1];
                    rows[1] = selectedRow;
                    cols[1] = selectedCol;
                    turnDownCards(rows, cols);
                    gameLogic.addStepToPlayer(playerPlaying);
                    scoreLabel[playerPlaying].setText(gameLogic.endPlayerRound(playerPlaying, correctBotGuess));
                    botIsPlaying(1);
                }else {
                    final int row = selections[0][0];
                    final int col = selections[0][1];
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            cardsButtons[row][col].setEnabled(false);
                            cardsButtons[selectedRow][selectedCol].setEnabled(false);
                            opened[row][col] = true;
                            opened[selectedRow][selectedCol] = true;
                            gameOver(gameLogic.addRevealedCard());
                        }
                    }, 2000);
                }
            }else {
                gameLogic.setPreviousRow(selections[0][0]);
                gameLogic.setPreviousCol(selections[0][1]);
                changePlayer();
                changeDuelPlayer(playerPlaying==0);
            }
        }
    }

    /**
     * Checks whenever the game ends.
     * @param flag If the game ended.
     */
    private void gameOver(boolean flag){
        if (flag){
            String[] options = new String[]{texts.getString("menu"), texts.getString("leaderboard")};
            int selection = JOptionPane.showOptionDialog(frame, texts.getString("winner") + " " + gameLogic.findTheWinner(), texts.getString("gameOver"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
            if (selection == 0){
                gamePanel.setVisible(false);
                makeFrameSmall();
                showMenu();
            }else{
                playerFrame.setVisible(true);
            }
        }
    }

    /**
     * Get's data from "GameMode" and pass it to the local variables.
     */
    private void getModeData(){
        row = mode.getRows();
        col = mode.getCols();
        copies = mode.getCopies();
        diffCards = mode.getDifferentCards();
        totalPlayers = mode.getTotalPlayers();
        duel = mode.isDuel();
    }

}