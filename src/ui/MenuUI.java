package ui;

import Classes.HighscoreData;
import Classes.PlayersData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * <b><i>MenuUI</i> class is in charge for creating and displaying the Menu</b>
 */
public class MenuUI {

    private static final String playerFile = "playerFile.txt";
    private static final String highscoreFile = "highscoreFile.txt";

    protected JPanel currentPanel;
    protected JFrame frame;
    protected JFrame playerFrame;
    private JPanel menuPanel;
    private JTabbedPane tabbedPane;
    protected JButton startGameBtn;
    protected int sizeX;
    protected int sizeY;
    protected HighscoreData highscoreData;
    protected JMenuItem newGame;
    protected Dimension screenSize;
    protected Locale currentLocale;
    public static ResourceBundle texts;

    /**
     * Constructor creates the frame.
     */
    public MenuUI() {
        initLocale();
        makeFrame();
        makeMenuBar();
        makeMenuPanel();
        frame.setVisible(true);
    }

    /**
     * Initialize locale to make the app international.
     */
    private void initLocale() {
        Locale[] supportedLocales = {
                new Locale("en", "US"),
                new Locale("en", "GR"),
        };
        Locale operationsLocale = Locale.getDefault();
        for (Locale supportedLocale : supportedLocales) {
            if (supportedLocale.equals(operationsLocale))
                currentLocale = supportedLocale;
        }
        if (currentLocale == null)
            currentLocale = new Locale("en", "US");

        texts = ResourceBundle.getBundle("res.i18n.Texts", currentLocale);
    }

    /**
     * Creates the Frame.
     */
    private void makeFrame() {
        frame = new JFrame();
        frame.setTitle(texts.getString("memoryGame"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null);
        makeFrameSmall();
        menuPanel = new JPanel(null);
        menuPanel.setBounds(0, 0, sizeX, sizeY);
        menuPanel.setBackground(Color.WHITE);
        currentPanel = menuPanel;
    }

    /**
     * Creates the menu bar.
     */
    private void makeMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu(texts.getString("menu"));
        menu.setMnemonic(KeyEvent.VK_M);
        menuBar.add(menu);

        JMenuItem jMenu = new JMenuItem(texts.getString("menu"));
        jMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_MASK));
        jMenu.addActionListener(e -> {
            currentPanel.setVisible(false);
            makeFrameSmall();
            menuPanel.setVisible(true);
        });
        menu.add(jMenu);

        JMenu newGame = new JMenu(texts.getString("new"));
        newGame.setMnemonic(KeyEvent.VK_N);
        menu.add(newGame);

        this.newGame = new JMenuItem(texts.getString("newGame"));
        this.newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_MASK));
        newGame.add(this.newGame);      //listener on gameui panel
        menu.addSeparator();

        JMenuItem menuItem = new JMenuItem(texts.getString("exit"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_MASK));
        menuItem.addActionListener(e -> System.exit(0));
        menu.add(menuItem);

        highscoreData = new HighscoreData(highscoreFile);
        JMenu playersMenu = new JMenu(texts.getString("stats"));
        JMenuItem pesrsonalStats = new JMenuItem(texts.getString("personalBest"));
        pesrsonalStats.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, KeyEvent.CTRL_MASK));
        playersMenu.add(pesrsonalStats);
        makeStatsFrame();
        pesrsonalStats.addActionListener(e -> {
            makeStatsFrame();
            playerFrame.setVisible(true);
        });
        menuBar.add(playersMenu);
    }

    /**
     * Adding components to frame.
     */
    private void makeMenuPanel() {
        makeLogo();
        makeStartButton();
        makeLeaderboardBtn();
        frame.add(menuPanel);
    }

    /**
     * Creates the logo.
     */
    private void makeLogo() {
        JLabel logoLabel = new JLabel(new ImageIcon(new ImageIcon(getClass().getResource("/res/Image/memory.ngsversion.1438028331698.adapt.1900.1.png")).getImage().getScaledInstance(sizeX / 4 * 2, sizeY / 6, Image.SCALE_DEFAULT)));
        logoLabel.setBounds(sizeX / 2 - sizeX / 4, (int) (sizeY / 6 * 0.5), sizeX / 4 * 2, sizeY / 6);
        menuPanel.add(logoLabel);
    }

    /**
     * Creates the Start button.
     */
    private void makeStartButton() {
        startGameBtn = new JButton(texts.getString("startBttn"));
        startGameBtn.setBounds(sizeX / 2 - sizeX / 4 + 50, sizeY / 6 * 2, sizeX / 4 * 2 - 100, sizeY / 6);
        startGameBtn.addActionListener(e -> clearMenu());
        menuPanel.add(startGameBtn);
    }

    /**
     * Creates the Leaderboard button.
     */
    private void makeLeaderboardBtn() {
        JButton settingsBtn = new JButton(texts.getString("leaderboard"));
        settingsBtn.setBounds(sizeX / 2 - sizeX / 4 + 50, (int) (sizeY / 6 * 3.5), sizeX / 4 * 2 - 100, sizeY / 6);
        settingsBtn.addActionListener(e -> {
            clearMenu();
            new HighscoresUI(frame, sizeX, sizeY, menuPanel, highscoreData);
        });
        menuPanel.add(settingsBtn);
    }

    /**
     * Sets menu panel visible.
     */
    protected void showMenu() {
        menuPanel.setVisible(true);
    }

    /**
     * Set menu panel invisible.
     */
    private void clearMenu() {
        menuPanel.setVisible(false);
    }

    /**
     * Makes frame small, exit fullscreen.
     */
    protected void makeFrameSmall() {
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        sizeX = (int) screenSize.getWidth() / 3 * 2;
        sizeY = (int) screenSize.getHeight() / 3 * 2;
        frame.setSize(sizeX, sizeY);
        frame.setLocationRelativeTo(null);
    }

    /**
     * Creates a new frame with the files' data.
     */
    private void makeStatsFrame() {
        playerFrame = new JFrame();
        playerFrame.setVisible(false);
        playerFrame.setTitle(texts.getString("personalBest"));
        playerFrame.setResizable(false);
        playerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        playerFrame.setSize(sizeX / 2, sizeY / 2);
        playerFrame.setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        makePersonalPanel();
        makeLeaderboardPanel();
        playerFrame.add(tabbedPane);
    }

    /**
     * Creates a panel with personal best performances.
     */
    private void makePersonalPanel() {
        PlayersData playersData = new PlayersData(playerFile);
        JPanel personal = new JPanel();
        personal.setLayout(new BoxLayout(personal, BoxLayout.Y_AXIS));

        int totalPlayers = playersData.getNames().size();
        String[] names = new String[totalPlayers];
        playersData.getNames().toArray(names);
        Integer[] scores = new Integer[totalPlayers];
        playersData.getScores().toArray(scores);
        Integer[] steps = new Integer[totalPlayers];
        playersData.getSteps().toArray(steps);

        int played = 0;
        for (int i = 0; i < totalPlayers; i++)
            if (steps[i] > 0)
                played++;

        personal.setPreferredSize(new Dimension(playerFrame.getWidth(), 45 * played));

        JPanel line;
        JScrollPane scrollPane = new JScrollPane(personal, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setValue(playerFrame.getHeight());
        scrollPane.getVerticalScrollBar().setValue(0);

        for (int i = 0; i < totalPlayers; i++) {
            if (steps[i] > 0) {
                line = new JPanel(new GridLayout(1, 3));
                line.setSize(playerFrame.getWidth(), 15);
                JLabel nlbl = new JLabel(names[i], JLabel.CENTER);
                JLabel sclbl = new JLabel(String.valueOf(scores[i]), JLabel.CENTER);
                JLabel stlbl = new JLabel(String.valueOf(steps[i]), JLabel.CENTER);
                line.add(nlbl);
                line.add(sclbl);
                line.add(stlbl);
                personal.add(line);
            }
        }

        tabbedPane.add(texts.getString("personal"), scrollPane);
    }

    /**
     * Creates a panel with top best performances.
     */
    private void makeLeaderboardPanel() {
        highscoreData = new HighscoreData(highscoreFile);
        JPanel leaderboard = new JPanel();
        tabbedPane.add(texts.getString("leaderboard"), leaderboard);
        String[] names = highscoreData.getNames();
        int[] scores = highscoreData.getScores();
        int[] steps = highscoreData.getSteps();
        GridLayout gridLayout = new GridLayout(10, 3);
        leaderboard.setLayout(gridLayout);
        for (int i = 0; i < 10; i++) {
            JLabel nlbl = new JLabel(names[i], JLabel.CENTER);
            JLabel sclbl = new JLabel(String.valueOf(scores[i]), JLabel.CENTER);
            JLabel stlbl = new JLabel(String.valueOf(steps[i]), JLabel.CENTER);
            leaderboard.add(nlbl);
            leaderboard.add(sclbl);
            leaderboard.add(stlbl);
            if (names[i].equals("Empty Position")) {
                sclbl.setText("-");
                stlbl.setText("-");
            }
        }
    }
}

