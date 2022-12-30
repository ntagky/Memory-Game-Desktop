package Classes;

import ui.MenuUI;

import java.io.*;

/**
 * <b><i>HighscoreData</i> class is in charge for saving the high scores in a file.</b>
 */
public class HighscoreData implements Serializable{

    private static final long serialVersionUID = 2660059926176760520L;
    transient String filename;
    private String[] names;
    private int[] scores;
    private int[] steps;

    /**
     * Constructor initialize the variables.
     * @param filename Path to file.
     */
    public HighscoreData(String filename){
        this.filename = filename;
        names = new String[10];
        scores = new int[10];
        steps = new int[10];

        File file = new File(filename);
        if (file.exists())
            getDataFromFile();
        else
            passEmptyStats();
    }

    /**
     * Getting data from file and pass to the local variables.
     */
    public void getDataFromFile(){

        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(filename)))){
            HighscoreData data;
            data = (HighscoreData) in.readObject();
            this.names = data.names;
            this.scores = data.scores;
            this.steps = data.steps;
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Setting data to file from the local variables.
     */
    public void setDataToFile(){
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filename, false)))){
            out.writeObject(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Initialize the variable and sending them to file.
     */
    private void passEmptyStats(){
        for (int i=0; i<10; i++){
            names[i] = MenuUI.texts.getString("emptyPosition");
            scores[i] = 0;
            steps[i] = 0;
        }
        setDataToFile();
    }

    /**
     * Cleaning the leaderboard by passing empty stats.
     */
    public void cleanLeaderboard(){
        passEmptyStats();
    }

    /**
     * Checking if score is in top 10.
     * @param score Player's score.
     * @param steps Player's steps.
     * @return The position to be placed.
     */
    public int inLeaderboard(int score, int steps){
        for (int i=0; i<10; i++)
            if (score >= scores[i])
                if (score > scores[i])
                    return i;
                else {
                    if (steps > this.steps[i]){
                        if  (i<9)
                            return i+1;
                        return -1;
                    }else
                        return i;
                }
        return -1;
    }

    /**
     * Adding stats to leaderboead.
     * @param playerName Player's name.
     * @param score Player's score.
     * @param steps Player's steps.
     * @param position Position in leaderboard.
     */
    public void addToLeaderboard(String playerName, int score, int steps, int position){
        changeLeaderboard(position);
        this.names[position] = playerName;
        this.scores[position] = score;
        this.steps[position] = steps;
        setDataToFile();
    }

    /**
     * Changing positions to pass new data in specific position.
     * @param position Position to be cleaned.
     */
    private void changeLeaderboard(int position){
        for (int i=9; i>position; i--) {
            this.names[i] = this.names[i-1];
            this.scores[i] = this.scores[i-1];
            this.steps[i] = this.steps[i-1];
        }
    }

    /**
     * @return Names on leaderboard.
     */
    public String[] getNames() {
        return names;
    }

    /**
     * @return Scores on leaderboard.
     */
    public int[] getScores() {
        return scores;
    }

    /**
     * @return Steps on leaderboard.
     */
    public int[] getSteps() {
        return steps;
    }

}