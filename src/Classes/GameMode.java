package Classes;

/**
 * <b><i>GameMode</i> class is in charge for the data collection.</b>
 */
public class GameMode {

    private int realPlayers;
    private int botPlayers;
    private int totalPlayers;
    private int gameMode;
    private int botDifficulty;
    private int diffrentCards;
    private int totalCards;
    private int rows;
    private int cols;
    private int copies;
    private String[] playerNames;
    private boolean duel;

    /**
     * Constructor passes the parameters' data.
     * @param realPlayers Number of real players.
     * @param gameMode Mode code.
     * @param botPlayers Number of bot players.
     * @param botDifficulty Bot difficulty code.
     */
    public GameMode(int realPlayers, int gameMode, int botPlayers, int botDifficulty){
        this.realPlayers = realPlayers;
        this.gameMode = gameMode;
        this.botPlayers = botPlayers;
        this.botDifficulty = botDifficulty;
        totalPlayers = realPlayers + botPlayers;
        duel = false;
        setUpGame();
    }

    /**
     * Constructor passes the parameters' data, when game mode is "The Ultimate Duel".
     * @param realPlayers Number of real players.
     * @param botPlayers Number of bot players.
     * @param botDifficulty Bot difficulty code.
     */
    public GameMode(int realPlayers, int botPlayers, int botDifficulty){
        this.realPlayers = realPlayers;
        this.botPlayers = botPlayers;
        totalPlayers = 2;
        this.gameMode = -1;
        this.botDifficulty = botDifficulty;
        duel = true;
        setUpGame();
    }

    /**
     * Depending on game mode code, it initializes the mode's data.
     */
    public void setUpGame(){
        if (!duel) {
            if (gameMode == 0) {
                diffrentCards = 12;
                totalCards = 24;
                rows = 4;
                cols = 6;
                copies = 2;
            } else if (gameMode == 1) {
                diffrentCards = 24;
                totalCards = 48;
                rows = 6;
                cols = 8;
                copies = 2;
            } else {
                diffrentCards = 12;
                totalCards = 36;
                rows = 6;
                cols = 6;
                copies = 3;
            }
        }else {
            diffrentCards = 12;
            totalCards = 24;
            rows = 6;
            cols = 4;
            copies = 2;
        }
    }

    /**
     * Set players' names.
     * @param names Players names.
     */
    public void addPlayerNames(String[] names){
        playerNames = new String[totalPlayers];
        playerNames = names;
    }

    /**
     * Get the number of real players.
     * @return The number of real players.
     */
    public int getRealPlayers() {
        return realPlayers;
    }

    /**
     * Get the selected player's name.
     * @param position Selected player's position.
     * @return The selected player's name.
     */
    public String getPlayerNames(int position) {
        return playerNames[position];
    }

    /**
     * Get the players' names.
     * @return The selected players' names.
     */
    public String[] getPlayerNames() {
        return playerNames;
    }

    /**
     * Get the number of bot players.
     * @return the number of bot players.
     */
    public int getBotPlayers() {
        return botPlayers;
    }

    /**
     * Get the code of bot difficulty.
     * @return the code of bot difficulty.
     */
    public int getBotDifficulty() {
        return botDifficulty;
    }

    /**
     * Get the number of total players.
     * @return the number of total players.
     */
    public int getTotalPlayers() {
        return totalPlayers;
    }

    /**
     * Get the number of different cards.
     * @return the number of different cards.
     */
    public int getDifferentCards() {
        return diffrentCards;
    }

    /**
     * Get the number of total cards.
     * @return the number of total cards.
     */
    public int getTotalCards() {
        return totalCards;
    }

    /**
     * Get the number of columns.
     * @return the number of columns.
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows.
     * @return the number of rows.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Get the number of copies.
     * @return the number of copies.
     */
    public int getCopies() {
        return copies;
    }

    /**
     * @return if the game mode is "The Ultimate Duel".
     */
    public boolean isDuel() {
        return duel;
    }

    /**
     * @return if exist bot players.
     */
    public boolean existBots(){ return botPlayers>0; }
}
