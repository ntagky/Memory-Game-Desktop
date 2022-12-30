package logic;

import Classes.*;

import java.net.URL;

/**
 * <b><i>GameLogic</i> class is in charge for the proper function of the class "GameUI".</b>
 */
public class GameLogic {

    private Score[] scores;
    private URL[][] cards;
    private int queue;
    private int maxPosition;
    private boolean[] botsTurn;
    private int[] previousRow;
    private int[] previousCol;
    private int copies;
    private boolean duel;
    private int revealedCards;
    private int differentCards;
    private int totalPlayers;
    private boolean botPlayers;
    private boolean[][] availableToOpen;
    private HighscoreData highscoreData;
    private BotIntelligence botIntelligence;
    private static final String playerFile = "playerFile.txt";

    /**
     * Constructor initialize some of the game mode's necessary data.
     * @param mode GameMode object with game's data.
     * @param names Players names
     * @param highscoreData HighscoreData object
     * @param duel Is the game "The Ultimate Duel"
     */
    public GameLogic(GameMode mode, String[] names, HighscoreData highscoreData, boolean duel){
        this.totalPlayers = mode.getTotalPlayers();
        this.botPlayers = mode.existBots();
        this.highscoreData = highscoreData;
        this.duel = duel;

        queue = 0;
        int row = mode.getRows();
        int col = mode.getCols();
        availableToOpen = new boolean[row][col];
        for(int i=0; i<row; i++)
            for (int j=0; j<col; j++)
                availableToOpen[i][j] = true;

        if (botPlayers){
            botIntelligence = new BotIntelligence(mode.getBotDifficulty(), row, col, mode.getCopies());
            botsTurn = new boolean[totalPlayers];
            for (int i=0; i<totalPlayers; i++)
                botsTurn[i] = !(i<mode.getRealPlayers());
        }

        scores = new Score[totalPlayers];
        for (int i=0; i<totalPlayers; i++){
            scores[i] = new Score(names[i]);
        }

    }

    /**
     * Initialize the needed cards.
     * @param row Number of table's rows.
     * @param col Number of table's columns.
     * @param diffCards Number of different cards.
     * @param copies Number of same cards.
     * @return the cards shuffled.
     */
    public URL[][] initCards(int row, int col, int diffCards, int copies){
        this.copies =copies;
        previousRow = new int[copies];
        previousCol = new int[copies];
        this.differentCards = diffCards;
        Cards myCards = new Cards(row, col, diffCards, copies, duel);
        cards = myCards.getCards();
        if (botPlayers)
            botIntelligence.getCards(cards);
        return cards;
    }

    /**
     * Stores the opened card's position and checks if the opened cards are same.
     * @param counter The number of opened cards.
     * @param row Opened card's row.
     * @param col Opened card's column.
     * @return if the combination is correct, or false if the opened cards are not as many as the copies.
     */
    public boolean openCard(int counter, int row, int col) {
        previousRow[counter] = row;
        previousCol[counter] = col;

        if (duel && botPlayers)
            botIntelligence.openedCard(cards[row][col].toString(), row, col);

        if (counter == copies-1) {
            if (botPlayers){
                for (int i=0; i<counter+1; i++)
                    botIntelligence.openedCard(cards[previousRow[i]][previousCol[i]].toString(), previousRow[i], previousCol[i]);
            }
            return correctCombination();
        }
        return false;
    }

    /**
     * Checks if the selected cards are the same.
     * @return if the combination is same.
     */
    private boolean correctCombination(){
        for (int i = 0; i < copies - 1; i++)
            if (!cards[previousRow[i]][previousCol[i]].equals(cards[previousRow[i + 1]][previousCol[i + 1]]))
                return false;
        return true;
    }

    /**
     * Checks if the selected cards are the same and bot is playing.
     * @param selections Card's position on table.
     * @return if the combination is same.
     */
    public boolean correctCombination(int selections[][]) {
        for (int i=0; i<copies-1; i++)
            if (!cards[selections[i][0]][selections[i][1]].equals(cards[selections[i + 1][0]][selections[i + 1][1]]))
                return false;

        for (int i=0; i<copies; i++)
            availableToOpen[selections[i][0]][selections[i][1]] = false;
        botIntelligence.revealedCard(availableToOpen);
        return true;
    }

    /**
     * Checks if the selected cards are the same and game mode is "The Ultimate Duel".
     * @param selections Card's position on table.
     * @param card Selected card URL.
     * @return if the combination is same.
     */
    public boolean correctCombination(int selections[][], URL card){
        return cards[selections[0][0]][selections[0][1]].equals(card);
    }

    /**
     * Checks if the selected card is opened before in the same round.
     * @param counter The number of opened cards.
     * @param row Opened card's row.
     * @param col Opened card's column.
     * @return if the card is opened before in the same round.
     */
    public boolean selectSameCard(int counter, int row, int col){
        if (counter>0){
            for (int i=0; i<counter; i++){
                if (previousRow[i] == row && previousCol[i] == col)
                    return true;
            }
        }
        return false;
    }

    /**
     * Get the opened cards' columns.
     * @return the opened cards' columns.
     */
    public int[] getPreviousCol() {
        return previousCol;
    }

    /**
     * Set the opened card's columns.
     * @param column The opened card's columns.
     */
    public void setPreviousCol(int column){
        previousCol[0] = column;
    }

    /**
     * Get the opened cards' row.
     * @return the opened cards' row.
     */
    public int[] getPreviousRow() {
        return previousRow;
    }

    /**
     * Set the opened card's row.
     * @param row The opened card's row.
     */
    public void setPreviousRow(int row){ previousRow[0] = row;}

    /**
     * Adds +1 to revealed cards. Makes the opened cards' unavailable to open.
     * @param rows Opened cards' row.
     * @param cols Opened cards' columns.
     * @return if the all the cards are opened.
     */
    public boolean addRevealedCard(int[] rows, int[] cols){
        revealedCards++;
        for (int i=0; i<rows.length; i++)
            availableToOpen[rows[i]][cols[i]] = false;
        if (botPlayers)
            botIntelligence.revealedCard(availableToOpen);
        return revealedCards == differentCards;
    }

    /**
     * Adds +1 to revealed cards.
     * @return if the all the cards are opened.
     */
    public boolean addRevealedCard(){
        revealedCards++;
        return revealedCards == differentCards;
    }

    /**
     * Changes the player's score.
     * @param position Player's position.
     * @param correct If the combination was correct.
     * @return the score number as a String.
     */
    public String endPlayerRound(int position, boolean correct){
        scores[position].addPoints(correct);
        return Integer.toString(scores[position].getPoints());
    }

    /**
     * Adds +1 to the player's step.
     * @param position Player's position.
     */
    public void addStepToPlayer(int position){
        scores[position].addStep();
    }

    /**
     * Finds the winner by comparing the scores.
     * @return The winner's position.
     */
    public String findTheWinner(){
        int max = scores[0].getPoints();
        maxPosition = 0;
        for (int i=1; i<totalPlayers; i++){
            if (scores[i].getPoints()>max || (scores[i].getPoints()==max && scores[i].getSteps()<scores[maxPosition].getSteps())){
                max = scores[i].getPoints();
                maxPosition = i;
            }
        }

        if (!botPlayers)
            addScoreData();
        else
        if (!botsTurn[maxPosition])
            addScoreData();
        return scores[maxPosition].getName();
    }

    /**
     * Changes the queue.
     * @return If the player playing is bot.
     */
    public boolean changePlayer(){
        queue= (queue + 1)%totalPlayers;
        if (botPlayers)
            return botsTurn[queue];
        return false;
    }

    /**
     * Bot chooses cards.
     * @return The position of the selected cards.
     */
    public int[][] botSelections(){
        int[][] selection = botIntelligence.chooseCards();
        for (int i=0; i<copies; i++)
            botIntelligence.openedCard(cards[selection[i][0]][selection[i][1]].toString(), selection[i][0], selection[i][1]);
        return selection;
    }

    /**
     * Bot chooses a card, when game mode is "The Ultimate Duel".
     * @param card The opened card
     * @return The position of the selected card.
     */
    public int[][] botSelection(URL card){
        int[][] selection = botIntelligence.chooseCard(card);
        botIntelligence.openedCard(cards[selection[0][0]][selection[0][1]].toString(), selection[0][0], selection[0][1]);
        return selection;

    }

    /**
     * Add winner's score data to files.
     */
    private void addScoreData(){
        int position = highscoreData.inLeaderboard(scores[maxPosition].getPoints(), scores[maxPosition].getSteps());
        if (position>-1)
            highscoreData.addToLeaderboard(scores[maxPosition].getName(), scores[maxPosition].getPoints(), scores[maxPosition].getSteps(), position);
        new PlayersData(playerFile).updatePlayerStats(scores[maxPosition]);
    }

}