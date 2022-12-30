package ui;
import Classes.GameMode;
import logic.SelectPlayerLogic;

import javax.swing.*;
import java.awt.*;

/**
 *  <b><i>SelectPlayerUI</i> class is in charge for the selection player user interface.</b>
 */
public class SelectPlayerUI extends StartGameUI {

    protected JPanel selectPlayersPanel;
    protected JComboBox<String>[] comboBoxes;
    private int realPlayers;
    private int botPlayers;
    private int totalPlayers;
    private int gameMode;
    private int botDifficulty;
    protected JButton selectBackBttn;
    protected JButton selectGameBttn;
    private SelectPlayerLogic playerLogic;
    protected int valid;
    protected GameMode mode;
    private boolean messageShowed;

    /**
     * Constructor setting up the user interface depending on previous selections.
     */
    public SelectPlayerUI(){

        selectGameBttn = new JButton(texts.getString("startGameBttn")); //because we want to use it on GameUI.java
        messageShowed = false;

        gameBtn.addActionListener(e -> {

            playerLogic = new SelectPlayerLogic();

            selectPlayersPanel = new JPanel();
            selectPlayersPanel.setBounds(0, 0, sizeX, sizeY);
            selectPlayersPanel.setBackground(Color.CYAN);
            selectPlayersPanel.setVisible(true);

            //Game settings
            if (!dueChckBox.isSelected()){
                totalPlayers = playersCmb.getSelectedIndex()+1;
                botPlayers = 0;
                realPlayers = totalPlayers;
                gameMode = modeCmb.getSelectedIndex();
                if (aiChckBox.isSelected()){
                    botPlayers = aiCmb.getSelectedIndex()+1;
                    realPlayers -= botPlayers;
                    botDifficulty = botDifficultyCmb.getSelectedIndex();
                }
                mode = new GameMode(realPlayers, gameMode, botPlayers, botDifficulty);
            }else {
                gameMode = -1;
                totalPlayers = 2;
                int botDifficulty = -1;
                String[] options = {texts.getString("botAnswer0"), texts.getString("botAnswer1")};
                int result;
                do {
                    result = JOptionPane.showOptionDialog(frame, texts.getString("botQuestion0"), texts.getString("ultimateDuel"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);
                    if (result == 0){
                        realPlayers = 2;
                        botPlayers = 0;
                    }else if (result ==1){
                        String[] botOptions = {texts.getString("botModeGF"), texts.getString("botModeKN"), texts.getString("botModeEL")};
                        do {
                            botDifficulty = JOptionPane.showOptionDialog(frame, texts.getString("botQuestion1"), texts.getString("ultimateDuel"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, botOptions, null);
                        }while (botDifficulty==-1);
                        realPlayers = botPlayers = 1;
                    }
                    mode = new GameMode(realPlayers, botPlayers, botDifficulty);
                }while (result==-1);
            }

            selectPlayersPanel.setLayout(new GridLayout(realPlayers+1, 1));

            comboBoxes = new JComboBox[realPlayers];
            for (int i=0; i<realPlayers; i++)
                makeSelectPlayer(i);
            makeSelectButtons(realPlayers+1);

            currentPanel = selectPlayersPanel;
            selectPlayersPanel.setVisible(true);
            frame.add(selectPlayersPanel);
        });
    }

    /**
     * Make panels for each real player, where you can enter the player's name.
     * @param player The number of real players.
     */
    private void makeSelectPlayer(int player){
        JPanel playerPanel = new JPanel(new GridLayout(2, 1));
        playerPanel.setBackground(Color.white);
        JLabel lbl = new JLabel(texts.getString("choosePlayerLbl") + " " + (player+1) + ":", JLabel.CENTER);
        lbl.setVerticalAlignment(JLabel.BOTTOM);
        playerPanel.add(lbl);

        JPanel smallSelectPlayerPnl = new JPanel(new FlowLayout());
        String[] playersNames = playerLogic.getNames();
        comboBoxes[player] = new JComboBox<>();
        comboBoxes[player].setPreferredSize(new Dimension(100, 30));
        if (playersNames.length == 0)       //no previous players
            comboBoxes[player].setEnabled(false);
        else
            for (String playersName : playersNames)
                comboBoxes[player].addItem(playersName);
        comboBoxes[player].setSelectedIndex(-1);
        smallSelectPlayerPnl.add(comboBoxes[player]);


        JButton smallNewPlayerBttn = new JButton(texts.getString("newPlayer"));
        smallNewPlayerBttn.setPreferredSize(new Dimension(100, 30));
        smallNewPlayerBttn.addActionListener(e -> {
            String name;
            name = JOptionPane.showInputDialog(frame, texts.getString("requestName"));

            if (name!=null){
                name = playerLogic.validName(name);
                if (name!=null){
                    int exist = playerLogic.existName(name);
                    if (exist > -1)
                        comboBoxes[player].setSelectedIndex(exist);
                    else {
                        comboBoxes[player].addItem(name);
                        comboBoxes[player].setSelectedIndex(comboBoxes[player].getItemCount()-1);
                        if (!comboBoxes[player].isEditable())
                            comboBoxes[player].setEnabled(true);
                        playerLogic.addPlayer(name);
                    }
                }else
                    JOptionPane.showMessageDialog(frame, texts.getString("invalidTxt1"));
            }
        });
        smallSelectPlayerPnl.add(smallNewPlayerBttn);

        playerPanel.add(smallSelectPlayerPnl);
        selectPlayersPanel.add(playerPanel);
    }

    /**
     * Makes buttons to go back to "StartGameUI" and "GameUI" interface.
     * @param rows Total rows for design use.
     */
    private void makeSelectButtons(int rows){
        JPanel buttonsPanel = new JPanel(null);
        buttonsPanel.setBackground(Color.white);

        selectBackBttn = new JButton(texts.getString("backBttn"));
        selectBackBttn.setBounds(50, sizeY/6*5 - (sizeY/rows*(rows-1)), sizeX/5/2, sizeY/12/2);
        selectBackBttn.addActionListener(e -> {
            selectPlayersPanel.setVisible(false);
            startGamePanel.setVisible(true);
        });
        buttonsPanel.add(selectBackBttn);


        selectGameBttn.setBounds(sizeX-50-sizeX/5/2, sizeY/6*5 - (sizeY/rows*(rows-1)), sizeX/5/2, sizeY/12/2);
        selectGameBttn.addActionListener(e -> {
            String[] playerName = new String[totalPlayers];
            for(int i=0; i<realPlayers; i++)
                playerName[i] = (String) comboBoxes[i].getSelectedItem();
            for (int i=realPlayers; i<totalPlayers; i++)
                playerName[i] = texts.getString("botPlayer") + i;

            valid = playerLogic.validName(playerName);

            if (valid > 0){
                mode.addPlayerNames(playerName);
                selectPlayersPanel.setVisible(false);
            }else {
                if (!messageShowed){
                    messageShowed = true;
                    switch (valid){
                        case -2:
                            JOptionPane.showMessageDialog(frame, texts.getString("invalidTxt2"), texts.getString("invalidName"), JOptionPane.WARNING_MESSAGE);
                            break;
                        case -1:
                            JOptionPane.showMessageDialog(frame, texts.getString("invalidTxt0"), texts.getString("invalidName"), JOptionPane.WARNING_MESSAGE);
                            break;
                    }
                }
            }
        });
        buttonsPanel.add(selectGameBttn);
        selectPlayersPanel.add(buttonsPanel);
    }

}