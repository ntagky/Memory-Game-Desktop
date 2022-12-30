package Classes;

import java.io.*;
import java.util.ArrayList;

/**
 * <b><i>PlayersData</i> class is in charge for saving the best player's game stats in a file.</b>
 */
public class PlayersData implements Serializable {

    private static final long serialVersionUID = 1822466709791987793L;
    transient String filename;
    private ArrayList<String> names;
    private ArrayList<Integer> scores;
    private ArrayList<Integer> steps;

    /**
     * Constructor initialize the variables.
     * @param filename Path to file.
     */
    public PlayersData(String filename){
        this.filename = filename;
        names = new ArrayList<>();
        scores = new ArrayList<>();
        steps = new ArrayList<>();

        File file = new File(filename);
        if (file.exists())
            getDataFromFile();
        else
            setDataToFile();
    }

    /**
     * Getting data from file and pass to the local variables.
     */
    private void getDataFromFile(){
        try (ObjectInputStream in = new ObjectInputStream(
                new BufferedInputStream(
                        new FileInputStream(filename)))){
            PlayersData data;
            data = (PlayersData) in.readObject();
            names = data.names;
            scores = data.scores;
            steps = data.steps;
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    /**
     * Setting data to file from the local variables.
     */
    private void setDataToFile(){
        try (ObjectOutputStream out = new ObjectOutputStream(
                new BufferedOutputStream(
                        new FileOutputStream(filename, false)))){

            out.writeObject(this);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Adding a new player in file.
     * @param name Player's name.
     * @param score Player's score.
     * @param step Player's step.
     */
    public void addNewPlayer(String name, int score, int step){
        names.add(name);
        scores.add(score);
        steps.add(step);
        setDataToFile();
    }

    /**
     * Update player's data.
     * @param playerStats "Score" class data.
     */
    public void updatePlayerStats(Score playerStats){
        int position = names.indexOf(playerStats.getName());
        if (steps.get(position) == 0){
            scores.set(position, playerStats.getPoints());
            steps.set(position, playerStats.getSteps());
        }else {
            if (playerStats.getPoints() > scores.get(position)) {
                scores.set(position, playerStats.getPoints());
                steps.set(position, playerStats.getSteps());
            } else if (playerStats.getPoints() == scores.get(position))
                if (playerStats.getSteps() < steps.get(position))
                    steps.set(position, playerStats.getSteps());
        }
        setDataToFile();
    }

    /**
     * @return Names on leaderboard.
     */
    public ArrayList<String> getNames() {
        return names;
    }

    /**
     * @return Scores on leaderboard.
     */
    public ArrayList<Integer> getScores() {
        return scores;
    }

    /**
     * @return Steps on leaderboard.
     */
    public ArrayList<Integer> getSteps() {
        return steps;
    }

}