package Classes;

/**
 * <b><i>Leaderboard</i> class is in charge for keeping and saving the score.</b>
 *
 */
public class
Score {

    String name;

    /**
     * Number of tries user made to complete the game.
     */
    int steps;
    /**
     * Points that user collect from each correct answer.
     */
    int points;


    /**
     * Constructor initialize points and steps with 0.
     * @param playerName Player's name.
     */
    public Score(String playerName){
        name = playerName;
        steps=0;
        points=1000;
    }

    /**
     * Adding a try and checking if the combination is correct.
     * @param correct The combination that user made.
     */
    public void addPoints(boolean correct){

        if (correct)
            points+=50;
        else{
            points-=15;
            if (points<0)
                points = 0;
        }

    }

    public void addStep(){
        steps++;
    }

    /**
     * @return The points that user collected.
     */
    public int getPoints() {
        return points;
    }

    /**
     * @return The number of tries that user made so far.
     */
    public int getSteps() {
        return steps;
    }

    /**
     * @return Player's name.
     */
    public String getName() {
        return name;
    }
}