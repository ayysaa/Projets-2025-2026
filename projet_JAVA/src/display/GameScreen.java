package display;

import controler.CaseClickListener;
import game.Case;
import game.Demineur;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.Display;

/**
 * Gère l'écran principal du jeu de démineur.
 */
public class GameScreen {


    private static final int GRID_WRAPPER_V_GAP = 50;
    private static final int GRID_FRAME_GAP = 2;
    private static final int GRID_GAP = 2;
    private static final Dimension CELL_SIZE = new Dimension(50, 50);

    private static final int BOTTOM_PANEL_H_GAP = 20;
    private static final int BOTTOM_PANEL_V_GAP = 15;
    private static final Dimension BUTTON_SIZE = new Dimension(250, 60);

    private static final Color LABEL_COLOR = Color.WHITE;
    private static final Font BUTTON_FONT = new Font("Arial", Font.BOLD, 22);
    private static final Font CELL_FONT = new Font("Dialog", Font.BOLD, 26);
  
    /**
     * Crée le panneau principal contenant la grille et les boutons.
     *
     * @param frame fenêtre principale
     * @param demineur instance du jeu en cours
     * @param gridWidth largeur de la grille en nombre de cases
     * @param gridHeight hauteur de la grille en nombre de cases
     * @return le panneau de jeu complet pret à l'affichage
     */
    public static JPanel createPanel(JFrame frame, Demineur demineur, int gridWidth, int gridHeight) {
        JPanel gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(Demineur.bgColor);

        // Compteur de mines restantes en haut
        JLabel mineCounterLabel = new JLabel();
        mineCounterLabel.setForeground(Color.WHITE);
        mineCounterLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mineCounterLabel.setHorizontalAlignment(SwingConstants.CENTER);

        updateMineCounter(mineCounterLabel, demineur);

        gamePanel.add(mineCounterLabel, BorderLayout.NORTH);

        // Grille au centre
        JPanel gridWrapper = new JPanel();
        gridWrapper.setLayout(new FlowLayout(FlowLayout.CENTER, 0, GRID_WRAPPER_V_GAP));
        gridWrapper.setBackground(Demineur.bgColor);

        JPanel gridFrame = new JPanel();
        gridFrame.setBackground(Demineur.borderColor);
        gridFrame.setLayout(new FlowLayout(FlowLayout.CENTER, GRID_FRAME_GAP, GRID_FRAME_GAP));

        JPanel gridContainer = new JPanel();
        gridContainer.setLayout(new GridLayout(gridHeight, gridWidth, GRID_GAP, GRID_GAP));
        gridContainer.setBackground(Demineur.borderColor);

        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                Case nCase = demineur.getCase(x, y);
                
                JPanel visual = new JPanel();
                visual.setLayout(new GridBagLayout()); 

                JLabel mineAroundLabel = new JLabel();
                mineAroundLabel.setForeground(LABEL_COLOR);
                mineAroundLabel.setFont(CELL_FONT);
                
                visual.add(mineAroundLabel);
                mineAroundLabel.setVisible(false);

                nCase.setGraphic(visual);

                visual.setBackground(Demineur.defaultCaseColor);
                mineAroundLabel.setText("");

                if (nCase.isDiscovered()) {
                    visual.setBackground(Demineur.discoveredCaseColor);
                    if (nCase.getMineAroundAmount() > 0) {
                        mineAroundLabel.setText(String.valueOf(nCase.getMineAroundAmount()));
                        mineAroundLabel.setVisible(true);
                    }
                } else if (nCase.getMark() != game.Marqueur.EMPTY) {
                    mineAroundLabel.setText(game.Marqueur.getSymbol(nCase.getMark()));
                    mineAroundLabel.setVisible(true);
                }

                visual.setPreferredSize(CELL_SIZE);
                CaseClickListener clickListener = new CaseClickListener(demineur, nCase, frame, mineCounterLabel);
                visual.addMouseListener(clickListener);

                gridContainer.add(visual);
            }
        }

        gridFrame.add(gridContainer);
        gridWrapper.add(gridFrame);
        gamePanel.add(gridWrapper, BorderLayout.CENTER);

        // Boutons en bas
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Demineur.bgColor);
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BOTTOM_PANEL_H_GAP, BOTTOM_PANEL_V_GAP));

        JButton saveQuitButton = new JButton("Sauver et Quitter");
        styleModernButton(saveQuitButton);
        saveQuitButton.addActionListener(new SaveQuitHandler(demineur));

        JButton saveMenuButton = new JButton("Sauver et Menu");
        styleModernButton(saveMenuButton);
        saveMenuButton.addActionListener(new SaveMenuHandler(frame, demineur));

        bottomPanel.add(saveQuitButton);
        bottomPanel.add(saveMenuButton);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);

        return gamePanel;
    }

    private static void styleModernButton(JButton button) {
        button.setFont(BUTTON_FONT);
        button.setPreferredSize(BUTTON_SIZE);
        button.setBackground(Demineur.borderColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    /**
     * Met à jour le texte du compteur de mines restantes.
     *
     * @param label le composant JLabel affichant le compteur
     * @param demineur l'instance du jeu en cours
     */
    private static void updateMineCounter(JLabel label, Demineur demineur) {
        int remaining = demineur.getMineAmount() - demineur.getStarsAmount();
        label.setText("Mines restantes : " + remaining);
    }

    /**
     * Gestionnaire d'événement pour sauvegarder et quitter le jeu.
     */
    private static class SaveQuitHandler implements ActionListener {
        private Demineur demineur;

        /**
         * Crée l'action de sauvegarde et fermeture.
         *
         * @param demineur instance du jeu à sauvegarder
         */
        public SaveQuitHandler(Demineur demineur) { 
            this.demineur = demineur; 
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            demineur.saveGame();
            System.exit(0);
        }
    }

    /**
     * Gestionnaire d'événement pour sauvegarder et retourner au menu.
     */
    private static class SaveMenuHandler implements ActionListener {
        private JFrame frame;
        private Demineur demineur;

        /**
         * Crée l'action de sauvegarde et retour au menu.
         *
         * @param frame fenêtre principale
         * @param demineur instance du jeu à sauvegarder
         */
        public SaveMenuHandler(JFrame frame, Demineur demineur) {
            this.frame = frame;
            this.demineur = demineur;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            demineur.saveGame();
            Display.drawMainMenu(frame);
        }
    }
}