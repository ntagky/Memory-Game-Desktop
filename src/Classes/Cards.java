package Classes;

import java.net.URL;
import java.util.Random;

/**
 * <b><i>Cards</i> class is in charge for the initialization and shuffle of cards.</b>
 * The cards are saved in a 2d char array.
 */
public class Cards {

    /**
     * 2d char array where the cards are saved.
     */
    private URL[][] cards;
    private URL[] images;
    private int row;
    private int column;
    private int diffCards;
    private int copies;

    /**
     * Constructor initializes the card's array with the given parameters and calls the <i>shuffleCards</i> method.
     * @param row number of table's rows.
     * @param column number of table's columns.
     * @param diffCards number of different cards.
     * @param copies number of same cards.
     * @param duel If the game mode is "The Ultimate Duel".
     */
    public Cards(int row, int column, int diffCards, int copies, boolean duel){
        cards = new URL[row][column];
        this.row =row;
        this.column = column;
        this.diffCards = diffCards;
        this.copies = copies;

        getImages();

        if (duel)
            shuffle4Duel();
        else
            shuffleCards();
    }

    /**
     * Getting cards from directory.
     */
    private void getImages() {
        images = new URL[24];
        images[0] = getClass().getResource("/res/Image/adobephotoshop.png");
        images[1] = getClass().getResource("/res/Image/androidos.png");
        images[2] = getClass().getResource("/res/Image/arduino.png");
        images[3] = getClass().getResource("/res/Image/atomeditor.png");
        images[4] = getClass().getResource("/res/Image/bitcoin.png");
        images[5] = getClass().getResource("/res/Image/blockchain.png");
        images[6] = getClass().getResource("/res/Image/c.png");
        images[7] = getClass().getResource("/res/Image/cplusplus.png");
        images[8] = getClass().getResource("/res/Image/csharp.png");
        images[9] = getClass().getResource("/res/Image/css3.png");
        images[10] = getClass().getResource("/res/Image/firebase.png");
        images[11] = getClass().getResource("/res/Image/github.png");
        images[12] = getClass().getResource("/res/Image/html5.png");
        images[13] = getClass().getResource("/res/Image/intellij.png");
        images[14] = getClass().getResource("/res/Image/java.png");
        images[15] = getClass().getResource("/res/Image/javascript.png");
        images[16] = getClass().getResource("/res/Image/nodejs.png");
        images[17] = getClass().getResource("/res/Image/notepadplusplus.png");
        images[18] = getClass().getResource("/res/Image/php.png");
        images[19] = getClass().getResource("/res/Image/python.png");
        images[20] = getClass().getResource("/res/Image/ruby.png");
        images[21] = getClass().getResource("/res/Image/sql.png");
        images[22] = getClass().getResource("/res/Image/stackoverflow.png");
        images[23] = getClass().getResource("/res/Image/swift.png");
    }

    /**
     * @return the 2d URL array where the cards are saved.
     */
    public URL[][] getCards() {
        return cards;
    }

    /**
     * Shuffling the cards to make each game unique and realistic.
     */
    private void shuffleCards(){
        int[] selectedCards = new int[diffCards];
        for (int i=0; i<diffCards; i++)
            selectedCards[i] = 0;

        int number;
        for (int i=0; i<row; i++){
            for (int j=0; j<column; j++){
                Random random = new Random();
                do{
                    number = random.nextInt(diffCards);
                }while (selectedCards[number]==copies);
                cards[i][j]=  images[number];
                selectedCards[number]++;
            }
        }
    }

    /**
     * Shuffling the cards to make each game unique and realistic, when game mode is "The Ultimate Duel".
     */
    private void shuffle4Duel(){
        int[] selectedCards = new int[diffCards];
        int[] totalCards = new int[diffCards];
        for (int i=0; i<diffCards; i++){
            selectedCards[i] = 0;
            totalCards[i] = 0;
        }

        int number;
        for (int i=0; i<row; i++){
            for (int j=0; j<column/2; j++){
                Random random = new Random();
                do{
                    number = random.nextInt(diffCards);
                }while (selectedCards[number]==1);
                cards[i][j]=  images[number];
                selectedCards[number]++;
                totalCards[number]++;
            }
        }

        for (int i=0; i<row; i++){
            for (int j=0; j<column/2; j++){
                Random random = new Random();
                do{
                    number = random.nextInt(diffCards);
                }while (totalCards[number]==2);
                cards[i][j+(column/2)]=  images[number];
                totalCards[number]++;
            }
        }


    }

}