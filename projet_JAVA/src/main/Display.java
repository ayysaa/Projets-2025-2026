package main;

import display.*;
import game.Demineur;
import javax.swing.*;

/**
 * Classe principale gérant l'affichage et la navigation entre les différents écrans.
 */
public class Display {

    private static final String WINDOW_TITLE = "The Great Démineur";
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final int WINDOW_LOC_X = 100;
    private static final int WINDOW_LOC_Y = 100;

    /**
     * Point d'entrée de l'application. Initialise la fenêtre principale.
     *
     * @param args arguments de ligne de commande
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocation(WINDOW_LOC_X, WINDOW_LOC_Y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        drawMainMenu(frame);

        frame.setVisible(true);
    }

    /**
     * Affiche le menu principal.
     *
     * @param frame fenêtre principale
     */
    public static void drawMainMenu(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setContentPane(MainMenu.createPanel(frame));
        frame.setVisible(true);
    }

    /**
     * Affiche l'écran de configuration d'une nouvelle partie.
     *
     * @param frame fenêtre principale
     */
    public static void drawConfig(JFrame frame) {
        frame.getContentPane().removeAll();
        frame.setContentPane(ConfigScreen.createPanel(frame));
        frame.setVisible(true);
    }

    /**
     * Affiche l'écran de victoire.
     *
     * @param frame fenêtre principale
     * @param seconds temps écoulé en secondes
     * @param revealedCases nombre de cases découvertes
     * @param totalToExplore nombre total de cases à découvrir
     */
    public static void drawVictoryMenu(JFrame frame, int seconds, int revealedCases, int totalToExplore) {
        frame.getContentPane().removeAll();
        frame.setContentPane(EndScreen.createVictoryPanel(frame, seconds, revealedCases, totalToExplore));
        frame.setVisible(true);
    }

    /**
     * Affiche l'écran de défaite.
     *
     * @param frame fenêtre principale
     * @param seconds temps écoulé en secondes
     * @param revealedCases nombre de cases découvertes
     * @param totalToExplore nombre total de cases à découvrir
     */
    public static void drawDefeatMenu(JFrame frame, int seconds, int revealedCases, int totalToExplore) {
        frame.getContentPane().removeAll();
        frame.setContentPane(EndScreen.createDefeatPanel(frame, seconds, revealedCases, totalToExplore));
        frame.setVisible(true);
    }

    /**
     * Affiche l'écran de la partie en cours.
     *
     * @param frame fenêtre principale
     * @param demineur instance du jeu
     * @param gridWidth largeur de la grille
     * @param gridHeight hauteur de la grille
     */
    public static void drawGame(JFrame frame, Demineur demineur, int gridWidth, int gridHeight) {
        frame.getContentPane().removeAll();
        frame.setContentPane(GameScreen.createPanel(frame, demineur, gridWidth, gridHeight));
        frame.setVisible(true);
    }
}