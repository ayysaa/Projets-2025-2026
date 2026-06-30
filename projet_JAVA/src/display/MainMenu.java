package display;

import game.Demineur;
import game.SaveManager;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.Display;

/**
 * Menu principal du jeu de démineur.
 */
public class MainMenu {

    private static final Color BG_COLOR = Color.DARK_GRAY;
    private static final Color TITLE_COLOR = Color.WHITE;
    
    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 50);
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 28);
    
    private static final Dimension BUTTON_SIZE = new Dimension(300, 70);

    private static final int TOP_OUTER_MARGIN = 150;
    private static final int LAYOUT_ROWS = 4;
    private static final int LAYOUT_COLS = 1;
    private static final int LAYOUT_H_GAP = 10;
    private static final int LAYOUT_V_GAP = 20;
    private static final String GAME_TITLE = "The Great Démineur";

    /**
     * Crée le panneau du menu principal.
     *
     * @param frame fenêtre principale
     * @return le panneau du menu prêt à être affiché
     */
    public static JPanel createPanel(JFrame frame) {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, TOP_OUTER_MARGIN));
        menuPanel.setBackground(BG_COLOR);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS, LAYOUT_H_GAP, LAYOUT_V_GAP));
        contentPanel.setBackground(BG_COLOR);

        JLabel titleLabel = new JLabel(GAME_TITLE, SwingConstants.CENTER);
        titleLabel.setFont(TITLE_FONT);
        titleLabel.setForeground(TITLE_COLOR);
        contentPanel.add(titleLabel);

        NiceButton newGameButton = new NiceButton("Nouvelle partie");
        newGameButton.setFont(BUTTON_FONT);
        newGameButton.setPreferredSize(BUTTON_SIZE);
        newGameButton.addActionListener(new NewGameHandler(frame));
        contentPanel.add(newGameButton);

        NiceButton resumeButton = new NiceButton("Reprendre la partie");
        resumeButton.setFont(BUTTON_FONT);
        resumeButton.setPreferredSize(BUTTON_SIZE);
        if (SaveManager.saveExists()) {
            resumeButton.addActionListener(new ResumeHandler(frame));
        } else {
            resumeButton.setEnabled(false);
        }
        contentPanel.add(resumeButton);

        NiceButton quitButton = new NiceButton("Quitter");
        quitButton.setFont(BUTTON_FONT);
        quitButton.setPreferredSize(BUTTON_SIZE);
        quitButton.addActionListener(new MenuQuitHandler());
        contentPanel.add(quitButton);

        menuPanel.add(contentPanel);
        return menuPanel;
    }

    /**
     * Gestionnaire d'événement pour lancer une nouvelle partie.
     */
    private static class NewGameHandler implements ActionListener {
        private JFrame frame;

        public NewGameHandler(JFrame frame) { 
            this.frame = frame; 
        }

        @Override
        public void actionPerformed(ActionEvent e) { 
            Display.drawConfig(frame); 
        }
    }

    /**
     * Gestionnaire d'événement pour reprendre une partie sauvegardée.
     */
    private static class ResumeHandler implements ActionListener {
        private JFrame frame;

        public ResumeHandler(JFrame frame) { 
            this.frame = frame; 
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Demineur d = SaveManager.load();
            if (d != null) {
                Display.drawGame(frame, d, d.getWidth(), d.getHeight());
            }
        }
    }

    /**
     * Gestionnaire d'événement pour quitter l'application.
     */
    private static class MenuQuitHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) { 
            System.exit(0); 
        }
    }
}