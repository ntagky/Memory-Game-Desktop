package logic;

import Classes.PlayersData;

/**
 * <b><i>SelectPlayerLogic</i> class is in charge for the proper function of the class "SelectPlayerUI".</b>
 */
public class SelectPlayerLogic {

    private static final String playerFile = "playerFile.txt";
    private PlayersData playersData;
    private String[] names;

    /**
     * Constructor loads all the player's names from file.
     */
    public SelectPlayerLogic(){
        playersData = new PlayersData(playerFile);

        names = new String[playersData.getNames().size()];
        names = playersData.getNames().toArray(names);
    }

    /**
     * @return The names.
     */
    public String[] getNames() {
        names = playersData.getNames().toArray(names);
        if (names.length > 0)
            return names;
        else
            return new String[]{};
    }

    /**
     * @param name Given name.
     * @return If the given name is valid or not.
     */
    public String validName(String name){
        if (name.length()>11)
            name = name.substring(0, 10);
        if (name.length()<3)
            return null;
        return name;
    }

    /**
     * @param playerName The given names.
     * @return If the given names are valid or not.
     */
    public int validName(String[] playerName){
        for (String name : playerName) {
            if (name == null)
                return -1;
        }

        for (int i=0; i<playerName.length-1; i++)
            for (int j=i+1; j<playerName.length; j++)
                if (playerName[i].equals(playerName[j]))
                    return -2;

        return 1;
    }

    /**
     * @param playerName Selected name.
     * @return If the name exist already.
     */
    public int existName(String playerName){
        getNames();             //refreshing array
        for (int i=0; i<names.length; i++)
            if (playerName.equals(names[i]))
                return i;
        return -1;
    }

    /**
     * Adds player to file.
     * @param name Player's name to be added.
     */
    public void addPlayer(String name){
        playersData.addNewPlayer(name, 0, 0);
    }

}
