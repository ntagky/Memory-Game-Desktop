package ui;

import javax.swing.*;
import java.awt.*;

/**
 * <b><i>StartGameUI</i> class is in charge for for the mode selection user interface.</b>
 */
public class StartGameUI extends MenuUI {

    protected JPanel startGamePanel;
    private JLabel playersLbl;
    protected JComboBox playersCmb;
    private JLabel modeLbl;
    protected JComboBox modeCmb;
    protected JCheckBox aiChckBox;
    protected JComboBox aiCmb;
    private JLabel botDifficulty;
    protected JComboBox botDifficultyCmb;
    protected JCheckBox dueChckBox;
    protected JButton menuBtn;
    protected JButton gameBtn;
    private int numberOfAI;

    /**
     * Constructor sets the "startGamePanel" visible.
     */
    public StartGameUI(){

        startGamePanel = new JPanel(null);
        startGamePanel.setBounds(0, 0, sizeX, sizeY);
        startGamePanel.setBackground(Color.white);
        makeModePanel();
        startGamePanel.setVisible(false);
        frame.add(startGamePanel);

        startGameBtn.addActionListener(e -> {
            currentPanel = startGamePanel;
            startGamePanel.setVisible(true);
        });
    }

    /**
     * Calls methods to design the panel.
     */
    protected void makeModePanel(){
        makeTitles();
        choosePlayers();
        chooseMode();
        chooseAIPlayers();
        chooseAIDifficulty();
        chooseDuel();
        makeButtons();
    }

    /**
     * Creates title labels.
     */
    private void makeTitles(){
        JLabel customLbl = new JLabel(texts.getString("customGame"), JLabel.CENTER);
        customLbl.setBounds(sizeX/5*2, 0, sizeX/5, sizeY/12);
        startGamePanel.add(customLbl);

        JLabel readyLbl = new JLabel(texts.getString("readyGameMode"), JLabel.CENTER);
        readyLbl.setBounds(sizeX/5*2, (int) (sizeY/6*3.5), sizeX/5, sizeY/12);
        startGamePanel.add(readyLbl);
    }

    /**
     * Creates "Choose players" panel.
     */
    private void choosePlayers(){
        playersLbl = new JLabel(texts.getString("players"), JLabel.CENTER);
        playersLbl.setBounds(sizeX/8, sizeY/6/2, sizeX/4, sizeY/12);
        startGamePanel.add(playersLbl);

        String[] holder = {"1 " + texts.getString("botPlayer"), "2 " + texts.getString("botPlayers"), "3 " + texts.getString("botPlayers")};
        String[] numberPlayers = {"1 " + texts.getString("player"), "2 " + texts.getString("players"), "3 " + texts.getString("players"), "4 " + texts.getString("players")};
        playersCmb = new JComboBox(numberPlayers);
        ((JLabel)playersCmb.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        playersCmb.setBounds(sizeX/8, sizeY/6, sizeX/4, sizeY/12);
        playersCmb.addActionListener(e -> {
            numberOfAI = playersCmb.getSelectedIndex();
            if (numberOfAI > 0){
                aiChckBox.setEnabled(true);
                for (int i=0; i<3; i++) {
                    if (i < numberOfAI) {
                        if (aiCmb.getItemAt(i) == null)
                            aiCmb.addItem(holder[i]);
                    }else
                    if (aiCmb.getItemAt(i) != null){
                        aiCmb.removeItemAt(i);
                        i--; //due to when a list item is removed the other is moved on index lower.
                    }
                }
            }else {
                aiChckBox.setEnabled(false);
                aiChckBox.setSelected(false);
                aiCmb.setEnabled(false);
                botDifficultyCmb.setEnabled(false);
                botDifficulty.setEnabled(false);
                for (int i = 2; i >-1; i--)
                    if (aiCmb.getItemAt(i) != null)
                        aiCmb.removeItemAt(i);
            }
        });
        startGamePanel.add(playersCmb);
    }

    /**
     * Creates "Choose mode" panel.
     */
    private void chooseMode(){
        modeLbl = new JLabel(texts.getString("mode"), JLabel.CENTER);
        modeLbl.setBounds(sizeX/8*5, sizeY/6/2, sizeX/4, sizeY/12);
        startGamePanel.add(modeLbl);

        String[] modeNames = {texts.getString("basicMode"), texts.getString("duoMode"), texts.getString("tripleMode")};
        modeCmb = new JComboBox(modeNames);
        ((JLabel)modeCmb.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        modeCmb.setBounds( sizeX/8*5, sizeY/6, sizeX/4, sizeY/12);
        startGamePanel.add(modeCmb);
    }

    /**
     * Creates "Choose bot players" panel.
     */
    private void chooseAIPlayers(){
        aiChckBox = new JCheckBox(texts.getString("bots"));
        aiChckBox.setHorizontalAlignment(JCheckBox.CENTER);
        aiChckBox.setBounds(sizeX/8, sizeY/6*2, sizeX/4, sizeY/12);
        aiChckBox.setEnabled(false);
        aiChckBox.setToolTipText(texts.getString("botsTip"));
        aiChckBox.addActionListener(e -> {
            if (aiChckBox.isSelected()) {
                aiCmb.setEnabled(true);
                botDifficultyCmb.setEnabled(true);
                botDifficulty.setEnabled(true);
            }else{
                aiCmb.setEnabled(false);
                botDifficultyCmb.setEnabled(false);
                botDifficulty.setEnabled(false);
            }
        });
        startGamePanel.add(aiChckBox);

        aiCmb = new JComboBox();
        aiCmb.setEnabled(false);
        ((JLabel)aiCmb.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        aiCmb.setBounds(sizeX/8, (int) (sizeY/6*2.5), sizeX/4, sizeY/12);
        startGamePanel.add(aiCmb);
    }

    /**
     * Creates "Choose bot difficult" panel.
     */
    private void chooseAIDifficulty(){
        botDifficulty = new JLabel(texts.getString("botsDifficulty"), JLabel.CENTER);
        botDifficulty.setEnabled(false);
        botDifficulty.setBounds(sizeX/8*5, sizeY/6*2, sizeX/4, sizeY/12);
        startGamePanel.add(botDifficulty);

        String[] difficulties = {texts.getString("botModeGF"), texts.getString("botModeKN"), texts.getString("botModeEL")};
        botDifficultyCmb = new JComboBox(difficulties);
        botDifficultyCmb.setEnabled(false);
        ((JLabel) botDifficultyCmb.getRenderer()).setHorizontalAlignment(JLabel.CENTER);
        botDifficultyCmb.setBounds(sizeX/8*5, (int) (sizeY/6*2.5), sizeX/4, sizeY/12);
        botDifficultyCmb.setSelectedIndex(1);
        startGamePanel.add(botDifficultyCmb);
    }

    /**
     * Changes when choosing from custom to duel game mode.
     */
    private void enableCustom(boolean flag){
        playersLbl.setEnabled(flag);
        playersCmb.setEnabled(flag);
        modeLbl.setEnabled(flag);
        modeCmb.setEnabled(flag);

        if (aiChckBox.isSelected()){
            aiCmb.setEnabled(flag);
            botDifficulty.setEnabled(flag);
            botDifficultyCmb.setEnabled(flag);
            aiChckBox.setEnabled(flag);
        }else
        if (playersCmb.getSelectedIndex() > 0)
            aiChckBox.setEnabled(flag);
    }

    /**
     * Creates "Choose -The Ultimate Duel-" panel.
     */
    private void chooseDuel(){
        dueChckBox = new JCheckBox(texts.getString("ultimateDuel"));
        dueChckBox.setBounds(sizeX/5*2, sizeY/6*4, sizeX/5, sizeY/12);
        dueChckBox.setHorizontalAlignment(JCheckBox.CENTER);
        dueChckBox.addActionListener(e -> enableCustom(!dueChckBox.isSelected()));
        startGamePanel.add(dueChckBox);
    }

    /**
     * Creates "Choose players" panel.
     */
    private void makeButtons(){
        menuBtn = new JButton(texts.getString("menu")); //add icon
        menuBtn.setBounds(50, sizeY/6*5, sizeX/5/2, sizeY/12/2);
        menuBtn.addActionListener(e -> {
            startGamePanel.setVisible(false);
            showMenu();
        });
        startGamePanel.add(menuBtn);

        gameBtn = new JButton(texts.getString("startGameBttn"));
        gameBtn.setBounds(sizeX-50-sizeX/5/2, sizeY/6*5, sizeX/5/2, sizeY/12/2);
        gameBtn.addActionListener(e -> startGamePanel.setVisible(false));
        startGamePanel.add(gameBtn);
    }

}
