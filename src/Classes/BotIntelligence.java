package Classes;

import java.net.URL;
import java.util.Random;

/**
 * <b><i>BotIntelligence</i> class is in charge for the computer thinking.</b>
 * It takes decisions for which cards will be selected.
 */
public class BotIntelligence {

    private int iq;
    private int row;
    private int col;
    private int copies;
    private URL[][] cards;
    private String[][] memorizeCards;
    private boolean[][] availableToOpen;

    /**
     * Constructor initialize some of the game mode's necessary data.
     * @param iq Bot's difficulty.
     * @param row Number of table's rows.
     * @param col Number of table's columns.
     * @param copies Number of same cards.
     */
    public BotIntelligence(int iq, int row, int col, int copies){
        this.iq = iq;
        this.row = row;
        this.col = col;
        this.copies = copies;

        availableToOpen = new boolean[row][col];
        memorizeCards = new String[row][col];
        for(int i=0; i<row; i++)
            for (int j=0; j<col; j++){
                availableToOpen[i][j] = true;
                memorizeCards[i][j] = " ";
            }
    }

    /**
     * Save the playing cards' URL in "cards" array.
     * @param cards Card's URL.
     */
    public void getCards(URL[][] cards){
        this.cards = cards;
    }

    /**
     * Receive an array with the available to open cards, so it will help computer open the right cards.
     * @param availableToOpen Which cards are available to opened.
     */
    public void revealedCard(boolean[][] availableToOpen){
        this.availableToOpen = availableToOpen;
    }

    /**
     * Receive a card's info and depending on bot's IQ level, stores the data.
     * @param imageName res.Image's name.
     * @param row Number of table's rows.
     * @param col Number of table's columns.
     */
    public void openedCard(String imageName, int row, int col){
        switch (iq){
            case 1:
                if (new Random().nextInt(2) == 1)
                    memorizeCards[row][col] = imageName;
                break;
            case 2:
                memorizeCards[row][col] = imageName;
                break;
        }
    }

    /**
     * Choosing a card depending on its IQ.
     * @return the position of the selected cards.
     */
    public int[][] chooseCards(){
        if (iq > 0)
            return checkForComb();
        else
            return getRandomCards();

    }

    /**
     * Checks if there's a ready combination in its data. If not, it opens a random cards and then
     * checks again, if this card is equaled with any of its saved cards.
     * @return the position of the selected cards.
     */
    private int[][] checkForComb(){

        int[][] combinations = new int[copies][2];
        int counter;

        //Checking for already combinations
        for (int j=0; j<row*col; j++){
            counter=0;
            if (!memorizeCards[j/col][j%col].equals(" ") && availableToOpen[j/col][j%col]){
                combinations[counter][0] = j/col;
                combinations[counter][1] = j%col;
                counter++;
                int k=j+1;
                while (k<row*col && counter!=copies){
                    if (memorizeCards[j/col][j%col].equals(memorizeCards[k/col][k%col])){
                        combinations[counter][0] = k/col;
                        combinations[counter][1] = k%col;
                        counter++;
                    }
                    k++;
                }
                if (counter==copies){
                    return combinations;
                }

            }
        }

        //Opening a card and checking if a similar is already showed
        int[][] cardsPosition = getRandomCard();
        combinations[0][0] = cardsPosition[0][0];
        combinations[0][1] = cardsPosition[0][1];

        boolean[] combinationCreated = new boolean[copies-1];
        for (int i=0; i<copies-1; i++)
            combinationCreated[i] = false;

        boolean flag = true;
        for (int k=1; k<copies; k++){
            loop:
            for (int i=0; i<row; i++){
                for (int j=0; j<col; j++){
                    if (memorizeCards[i][j].equals(cards[cardsPosition[0][0]][cardsPosition[0][1]])){
                        for (int z=0; z<k; z++)
                            if (combinations[z][0] == i && combinations[z][1] == j)
                                flag = false;

                        if (flag){
                            combinations[k][0] = i;
                            combinations[k][1] = j;
                            availableToOpen[i][j] = false;
                            combinationCreated[k-1] = true;
                            break loop;
                        }
                    }
                }
            }
            if (!combinationCreated[k-1])
                break;
        }

        for (int i=1; i<copies; i++)
            if (!combinationCreated[i-1]){
                cardsPosition = getRandomCard();
                combinations[i][0] = cardsPosition[0][0];
                combinations[i][1] = cardsPosition[0][1];
            }

        for (int i=0; i<copies; i++)
            availableToOpen[combinations[i][0]][combinations[i][1]] = true;

        return combinations;
    }

    /**
     * When game mode is "The Ultimate Duel",
     * checks if the opened, by the user, card is equaled with any of its stored cards.
     * If not, gets a random card.
     * @param card Card's URL
     * @return the position of the selected card.
     */
    public int[][] chooseCard(URL card){
        if (iq > 0){
            return checkForComb(card);
        }else
            return getRandomCard(col/2);

    }

    /**
     * Checks if the opened, by the user, card is equaled with any of its stored cards.
     * @param card Card's URL
     * @return the position of the selected cards.
     */
    private int[][] checkForComb(URL card){
        int n[][] = new int[1][2];
        for (int i=0; i<row; i++)
            for (int j=col/2; j<col; j++){
                if (card != null && memorizeCards[i][j].equals(card.toString())){
                    n[0][0] = i;
                    n[0][1] = j;
                    return n;
                }
            }
        return getRandomCard(col/2);
    }

    /**
     * Get's a random, available to open, card.
     * @return the position of this card.
     */
    private int[][] getRandomCard(){
        Random random = new Random();
        int n[][] = new int[1][2];
        int x;
        do {
            x = random.nextInt(row*col);
        }while (!availableToOpen[x/col][x%col]);
        availableToOpen[x/col][x%col] = false;
        n[0][0] = x/col;
        n[0][1] = x%col ;
        return n;
    }

    /**
     * Get's a random, available to open, card depending on the separated column.
     * @return the position of this card.
     */
    private int[][] getRandomCard(int column){

        Random random = new Random();
        int n[][] = new int[1][2];
        int x;
        do {
            x = random.nextInt(row*column);
        }while (!availableToOpen[x/column][x%column + column]);
        availableToOpen[x/column][x%column+ column] = false;
        n[0][0] = x/column;
        n[0][1] = x%column + column;
        return n;
    }

    /**
     * Get's, as many as the number of the copies, random cards.
     * @return the position of these cards.
     */
    private int[][] getRandomCards(){

        Random random = new Random();
        int[][] combinations = new int[copies][2];
        int n;
        for (int i=0; i<copies; i++){
            do {
                n = random.nextInt(row*col);
            }while (!availableToOpen[n/col][n%col]);
            combinations[i][0] = n/col;
            combinations[i][1] = n%col;
            availableToOpen[n/col][n%col] = false;

        }

        for (int i=0; i<copies; i++)
            availableToOpen[combinations[i][0]][combinations[i][1]] = true;

        return combinations;
    }

}