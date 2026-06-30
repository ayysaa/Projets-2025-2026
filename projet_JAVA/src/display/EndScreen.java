package display;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import main.Display;

/**
 * Gère les écrans de fin de partie (victoire et défaite).
 */
public class EndScreen {

    private static final Color VICTORY_BG_COLOR = new Color(200, 255, 200);
    private static final Color VICTORY_TITLE_COLOR = new Color(0, 128, 0);
    private static final Color DEFEAT_BG_COLOR = new Color(255, 200, 200);
    private static final Color DEFEAT_TITLE_COLOR = Color.RED;

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
    private static final Font STATS_FONT = new Font("Arial", Font.PLAIN, 18);

    private static final Dimension BUTTON_SIZE = new Dimension(200, 50);

    private static final int TOP_OUTER_MARGIN = 200;
    private static final int CONTENT_GAP = 10;
    private static final int LAYOUT_ROWS = 4;
    private static final int LAYOUT_COLS = 1;

    /**
     * Crée l'écran de victoire.
     *
     * @param frame fenêtre principale
     * @param seconds temps écoulé en secondes
     * @param revealedCases nombre de cases découvertes
     * @param totalToExplore nombre total de cases à découvrir
     * @return le panneau de victoire prêt à être affiché
     */
    public static JPanel createVictoryPanel(JFrame frame, int seconds, int revealedCases, int totalToExplore) {
        return createBasePanel(
            frame, 
            VICTORY_BG_COLOR, 
            "VICTOIRE !", 
            VICTORY_TITLE_COLOR, 
            "Temps écoulé : " + seconds + " secondes", 
            "Score : " + revealedCases + " sur " + totalToExplore + " cases"
        );
    }

    /**
     * Crée l'écran de défaite.
     *
     * @param frame fenêtre principale
     * @param seconds temps écoulé en secondes
     * @param revealedCases nombre de cases découvertes
     * @param totalToExplore nombre total de cases à découvrir
     * @return le panneau de défaite prêt à être affiché
     */
    public static JPanel createDefeatPanel(JFrame frame, int seconds, int revealedCases, int totalToExplore) {
        return createBasePanel(
            frame, 
            DEFEAT_BG_COLOR, 
            "DOMMAGE...", 
            DEFEAT_TITLE_COLOR, 
            "Temps écoulé : " + seconds + " secondes", 
            "Progression : " + revealedCases + " sur " + totalToExplore + " cases"
        );
    }

    /**
     * Méthode utilitaire pour créer la structure du panneau de fin.
     *
     * @param frame fenêtre principale
     * @param bgColor couleur de fond
     * @param titleText texte du titre principal
     * @param titleColor couleur du titre
     * @param timeText texte affichant les statistiques de temps
     * @param scoreText texte affichant les statistiques de score
     * @return le panneau structuré
     */
    private static JPanel createBasePanel(JFrame frame, Color bgColor, String titleText, Color titleColor, String timeText, String scoreText) {
        JPanel basePanel = new JPanel();
        basePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, TOP_OUTER_MARGIN));
        basePanel.setBackground(bgColor);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new GridLayout(LAYOUT_ROWS, LAYOUT_COLS, CONTENT_GAP, CONTENT_GAP));
        contentPanel.setBackground(bgColor);

        JLabel title = new JLabel(titleText);
        title.setFont(TITLE_FONT);
        title.setForeground(titleColor);
        contentPanel.add(title);

        JLabel timeStats = new JLabel(timeText);
        timeStats.setFont(STATS_FONT);
        contentPanel.add(timeStats);

        JLabel scoreStats = new JLabel(scoreText);
        scoreStats.setFont(STATS_FONT);
        contentPanel.add(scoreStats);

        NiceButton backButton = new NiceButton("Retour au menu");
        backButton.setPreferredSize(BUTTON_SIZE);
        backButton.addActionListener(new BackToMenuHandler(frame));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        buttonPanel.add(backButton);
        contentPanel.add(buttonPanel);

        basePanel.add(contentPanel);
        return basePanel;
    }

    /**
     * Gestionnaire pour retourner au menu principal.
     */
    private static class BackToMenuHandler implements ActionListener {
        private JFrame frame;

        /**
         * Crée l'action de retour au menu.
         * * @param frame la fenêtre principale
         */
        public BackToMenuHandler(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Display.drawMainMenu(frame);
        }
    }
}