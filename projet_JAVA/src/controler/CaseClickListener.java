package controler;

import game.Case;
import game.Demineur;
import game.Marqueur;
import java.awt.Color;
import java.awt.event.*;
import javax.swing.*;
import main.Display;

/**
 * Gestionnaire d'événements de clic sur une case du démineur.
 */
public class CaseClickListener extends MouseAdapter {

    private static final Color MINE_CLICKED_COLOR = Color.ORANGE;
    private static final Color MINE_REVEALED_COLOR = Color.RED;
    
    private static final int MINE_REVEAL_DELAY_MS = 1200;
    private static final int DEFEAT_SCREEN_DELAY_MS = 2500;
    private static final int VICTORY_SCREEN_DELAY_MS = 2000;

    private Case nCase;
    private Demineur demineur;
    private JPanel visual;
    private JFrame frame;
    private JLabel mineCounterLabel;

    /**
     * Crée un nouveau listener pour une case spécifique.
     *
     * @param demineur l'instance du jeu
     * @param nCase la case associée
     * @param frame la fenêtre principale
     */

    public CaseClickListener(Demineur demineur, Case nCase, JFrame frame, JLabel mineCounterLabel) {
        this.nCase = nCase;
        this.demineur = demineur;
        this.visual = nCase.graphic;
        this.frame = frame;
        this.mineCounterLabel = mineCounterLabel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.println("Clique sur la case " + this.nCase.getCoords()[0] + " " + this.nCase.getCoords()[1]);
        System.out.println("Mines autour : " + this.nCase.getMineAroundAmount());

        // AJOUT : bloque toute interaction si la partie est terminée
        if (demineur.getActualState() != Demineur.IN_PROGRESS) {
            return;
        }

        if (SwingUtilities.isRightMouseButton(e)) {
            if (!nCase.isDiscovered()) {
                demineur.nextMark(this.nCase.getCoords()[0], this.nCase.getCoords()[1]);
                int remaining = demineur.getMineAmount() - demineur.getStarsAmount();
                mineCounterLabel.setText("Mines restantes : " + remaining);
                JLabel label = (JLabel) visual.getComponent(0);
                label.setText(Marqueur.getSymbol(nCase.getMark()));
                label.setVisible(true);
                visual.repaint();
            }
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (!nCase.isDiscovered()) {
                demineur.revealCase(this.nCase);

                Case[][] grid = demineur.getGrid();
                for (int i = 0; i < grid.length; i++) {
                    for (int j = 0; j < grid[i].length; j++) {
                        Case c = grid[i][j];
                        if (c.isDiscovered() && !c.isMine() && c.graphic != null) {
                            c.graphic.setBackground(Demineur.discoveredCaseColor);
                            JLabel label = (JLabel) c.graphic.getComponent(0);
                            if (c.getMineAroundAmount() > 0) {
                                label.setText(String.valueOf(c.getMineAroundAmount()));
                                label.setVisible(true);
                            } else {
                                label.setText("");
                            }
                            c.graphic.repaint();
                        }
                    }
                }

                if (demineur.getActualState() == Demineur.DEFEAT) {
                    // AJOUT : affiche uniquement la mine cliquée en orange
                    this.visual.setBackground(MINE_CLICKED_COLOR);
                    this.visual.repaint();

                    // AJOUT : après 1 seconde, affiche toutes les autres mines
                    Timer timerMines = new Timer(MINE_REVEAL_DELAY_MS, new ShowMinesHandler(frame, demineur, this.nCase));
                    timerMines.setRepeats(false);
                    timerMines.start();

                } else if (demineur.getActualState() == Demineur.VICTORY) {
                    Timer timer = new Timer(VICTORY_SCREEN_DELAY_MS, new VictoryTimerHandler(frame, demineur));
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        if (!nCase.isDiscovered()) {
            this.visual.setBackground(Demineur.hoverCaseColor);
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {
        if (!nCase.isDiscovered()) {
            this.visual.setBackground(Demineur.defaultCaseColor);
        }
    }

    // AJOUT : affiche toutes les autres mines, puis attend avant EndScreen
    /**
     * Gestionnaire pour afficher les mines restantes après une défaite.
     */
    private static class ShowMinesHandler implements ActionListener {
        private JFrame frame;
        private Demineur demineur;
        private Case triggeredMine;

        /**
         * Crée le gestionnaire d'affichage des mines.
         *
         * @param frame fenêtre principale
         * @param demineur instance du jeu
         * @param triggeredMine la case ayant causé la défaite
         */
        public ShowMinesHandler(JFrame frame, Demineur demineur, Case triggeredMine) {
            this.frame = frame;
            this.demineur = demineur;
            this.triggeredMine = triggeredMine;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Case[][] grid = demineur.getGrid();
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    Case c = grid[i][j];
                    // toutes les autres mines (pas celle qui a sauté)
                    if (c.isMine() && c != triggeredMine && c.graphic != null) {
                        c.graphic.setBackground(MINE_REVEALED_COLOR);
                        c.graphic.repaint();
                    }
                }
            }

            // attend encore 1.5 secondes avant le EndScreen
            Timer timerFin = new Timer(DEFEAT_SCREEN_DELAY_MS, new DefeatTimerHandler(frame, demineur));
            timerFin.setRepeats(false);
            timerFin.start();
        }
    }

    /**
     * Gestionnaire pour lancer l'écran de défaite après un délai.
     */
    private static class DefeatTimerHandler implements ActionListener {
        private JFrame frame;
        private Demineur demineur;

        /**
         * Crée le gestionnaire de délai de défaite.
         *
         * @param frame fenêtre principale
         * @param demineur instance du jeu
         */
        public DefeatTimerHandler(JFrame frame, Demineur demineur) {
            this.frame = frame;
            this.demineur = demineur;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Display.drawDefeatMenu(frame, demineur.getElapsedTime(), demineur.getRevealedCases(), demineur.getTotalCases());
        }
    }

    /**
     * Gestionnaire pour lancer l'écran de victoire après un délai.
     */
    private static class VictoryTimerHandler implements ActionListener {
        private JFrame frame;
        private Demineur demineur;

        /**
         * Crée le gestionnaire de délai de victoire.
         *
         * @param frame fenêtre principale
         * @param demineur instance du jeu
         */
        public VictoryTimerHandler(JFrame frame, Demineur demineur) {
            this.frame = frame;
            this.demineur = demineur;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Display.drawVictoryMenu(frame, demineur.getElapsedTime(), demineur.getRevealedCases(), demineur.getTotalCases());
        }
    }
}