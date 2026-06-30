package display;

import game.Demineur;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import main.Display;

/**
 * Menu de configuration du démineur.
 */
public class ConfigScreen {

    private static final int MIN_SIZE = 4;
    private static final int MAX_SIZE = 30;
    private static final int DEFAULT_WIDTH = 10;
    private static final int DEFAULT_HEIGHT = 10;
    private static final int DEFAULT_MINE_AMOUNT = 30;

    private static final int WIDTH_COUNTER = 0;
    private static final int HEIGHT_COUNTER = 1;
    private static final int MINE_COUNTER = 2;

    private static final Color BG_COLOR = new Color(220, 235, 255);
    private static final Color PRIMARY_COLOR = new Color(30, 80, 160);

    private static final Font TITLE_FONT = new Font("Arial", Font.BOLD, 40);
    private static final Font TEXT_FONT = new Font("Arial", Font.PLAIN, 18);
    private static final Font VALUE_FONT = new Font("Arial", Font.BOLD, 18);
    private static final Font PLAY_BUTTON_FONT = new Font("Arial", Font.BOLD, 16);

    private static final Dimension SMALL_BUTTON_SIZE = new Dimension(45, 35);
    private static final Dimension LARGE_BUTTON_SIZE = new Dimension(60, 35);
    private static final Dimension MAIN_BUTTON_SIZE = new Dimension(180, 50);
    private static final Dimension VALUE_LABEL_SIZE = new Dimension(40, 35);

    private static final int TOP_OUTER_MARGIN = 200;
    private static final int CONTENT_V_GAP = 10;
    private static final int BUTTON_H_GAP = 20;
    private static final int COUNTER_H_GAP = 10;

    /**
     * Configuration de la partie.
     */
    private static class Config {
        int width;
        int height;
        int mineAmount;

        /**
         * Crée une nouvelle configuration.
         *
         * @param width largeur
         * @param height hauteur
         * @param mineAmount nombre de mines
         */
        Config(int width, int height, int mineAmount) {
            this.width = width;
            this.height = height;
            this.mineAmount = mineAmount;
        }
    }

    /**
     * Crée le panneau de configuration.
     *
     * @param frame fenêtre principale
     * @return le panneau prêt à être affiché
     */
    public static JPanel createPanel(JFrame frame) {
        JPanel basePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, TOP_OUTER_MARGIN));
        basePanel.setBackground(BG_COLOR);

        JPanel contentPanel = new JPanel(new GridLayout(5, 1, CONTENT_V_GAP, CONTENT_V_GAP));
        contentPanel.setBackground(BG_COLOR);

        JLabel title = new JLabel("CONFIGURATION");
        title.setFont(TITLE_FONT);
        title.setForeground(PRIMARY_COLOR);
        contentPanel.add(title);

        Config config = new Config(DEFAULT_WIDTH, DEFAULT_HEIGHT, DEFAULT_MINE_AMOUNT);

        JLabel widthValue = createValueLabel();
        JLabel heightValue = createValueLabel();
        JLabel mineValue = createValueLabel();

        updateDisplayedValues(config, widthValue, heightValue, mineValue);

        contentPanel.add(createCounterRow(
            "Largeur",
            widthValue,
            new CounterAction(config, widthValue, heightValue, mineValue, WIDTH_COUNTER, -10),
            new CounterAction(config, widthValue, heightValue, mineValue, WIDTH_COUNTER, -1),
            new CounterAction(config, widthValue, heightValue, mineValue, WIDTH_COUNTER, 1),
            new CounterAction(config, widthValue, heightValue, mineValue, WIDTH_COUNTER, 10)
        ));

        contentPanel.add(createCounterRow(
            "Hauteur",
            heightValue,
            new CounterAction(config, widthValue, heightValue, mineValue, HEIGHT_COUNTER, -10),
            new CounterAction(config, widthValue, heightValue, mineValue, HEIGHT_COUNTER, -1),
            new CounterAction(config, widthValue, heightValue, mineValue, HEIGHT_COUNTER, 1),
            new CounterAction(config, widthValue, heightValue, mineValue, HEIGHT_COUNTER, 10)
        ));

        contentPanel.add(createCounterRow(
            "Mines",
            mineValue,
            new CounterAction(config, widthValue, heightValue, mineValue, MINE_COUNTER, -10),
            new CounterAction(config, widthValue, heightValue, mineValue, MINE_COUNTER, -1),
            new CounterAction(config, widthValue, heightValue, mineValue, MINE_COUNTER, 1),
            new CounterAction(config, widthValue, heightValue, mineValue, MINE_COUNTER, 10)
        ));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, BUTTON_H_GAP, 0));
        buttonPanel.setBackground(BG_COLOR);

        NiceButton backButton = new NiceButton("Retour au menu");
        backButton.setPreferredSize(MAIN_BUTTON_SIZE);
        backButton.addActionListener(new BackToMenuAction(frame));

        NiceButton playButton = new NiceButton("Jouer !");
        playButton.setPreferredSize(MAIN_BUTTON_SIZE);
        playButton.setBackground(PRIMARY_COLOR); // Surcharge de couleur pour le bouton principal
        playButton.setForeground(Color.WHITE);
        playButton.setFont(PLAY_BUTTON_FONT);
        playButton.addActionListener(new PlayAction(frame, config));

        buttonPanel.add(backButton);
        buttonPanel.add(playButton);
        contentPanel.add(buttonPanel);

        basePanel.add(contentPanel);
        return basePanel;
    }

    /**
     * Crée un label d'affichage.
     *
     * @return un label formaté
     */
    private static JLabel createValueLabel() {
        JLabel valueLabel = new JLabel();
        valueLabel.setPreferredSize(VALUE_LABEL_SIZE);
        valueLabel.setHorizontalAlignment(JLabel.CENTER);
        valueLabel.setFont(VALUE_FONT);
        return valueLabel;
    }

    /**
     * Met à jour les textes des valeurs.
     *
     * @param config configuration actuelle
     * @param widthValue label de largeur
     * @param heightValue label de hauteur
     * @param mineValue label de mines
     */
    private static void updateDisplayedValues(Config config, JLabel widthValue, JLabel heightValue, JLabel mineValue) {
        widthValue.setText(String.valueOf(config.width));
        heightValue.setText(String.valueOf(config.height));
        mineValue.setText(String.valueOf(config.mineAmount));
    }

    /**
     * Crée une ligne avec les boutons + et -.
     *
     * @param name texte affiché
     * @param valueLabel label contenant la valeur
     * @param doubleMinusAction action -10
     * @param minusAction action -1
     * @param plusAction action +1
     * @param doublePlusAction action +10
     * @return un panneau contenant la ligne complète
     */
    private static JPanel createCounterRow(
            String name,
            JLabel valueLabel,
            ActionListener doubleMinusAction,
            ActionListener minusAction,
            ActionListener plusAction,
            ActionListener doublePlusAction) {

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, COUNTER_H_GAP, 0));
        panel.setBackground(BG_COLOR);

        JLabel nameLabel = new JLabel(name + " :");
        nameLabel.setFont(TEXT_FONT);

        NiceButton doubleMinusButton = new NiceButton("−−");
        doubleMinusButton.setPreferredSize(LARGE_BUTTON_SIZE);
        doubleMinusButton.addActionListener(doubleMinusAction);

        NiceButton minusButton = new NiceButton("−");
        minusButton.setPreferredSize(SMALL_BUTTON_SIZE);
        minusButton.addActionListener(minusAction);

        NiceButton plusButton = new NiceButton("+");
        plusButton.setPreferredSize(SMALL_BUTTON_SIZE);
        plusButton.addActionListener(plusAction);

        NiceButton doublePlusButton = new NiceButton("++");
        doublePlusButton.setPreferredSize(LARGE_BUTTON_SIZE);
        doublePlusButton.addActionListener(doublePlusAction);

        panel.add(nameLabel);
        panel.add(doubleMinusButton);
        panel.add(minusButton);
        panel.add(valueLabel);
        panel.add(plusButton);
        panel.add(doublePlusButton);

        return panel;
    }

    /**
     * Gère les boutons des compteurs.
     */
    private static class CounterAction implements ActionListener {
        private final Config config;
        private final JLabel widthValue;
        private final JLabel heightValue;
        private final JLabel mineValue;
        private final int counterType;
        private final int variation;

        /**
         * Crée l'action d'un compteur.
         *
         * @param config configuration à modifier
         * @param widthValue label de largeur
         * @param heightValue label de hauteur
         * @param mineValue label de mines
         * @param counterType type de compteur
         * @param variation valeur de modification
         */
        CounterAction(Config config, JLabel widthValue, JLabel heightValue, JLabel mineValue,
                       int counterType, int variation) {
            this.config = config;
            this.widthValue = widthValue;
            this.heightValue = heightValue;
            this.mineValue = mineValue;
            this.counterType = counterType;
            this.variation = variation;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            if (counterType == WIDTH_COUNTER) {
                config.width = Math.max(MIN_SIZE, Math.min(MAX_SIZE, config.width + variation));
                adjustMineAmount(config);
            } else if (counterType == HEIGHT_COUNTER) {
                config.height = Math.max(MIN_SIZE, Math.min(MAX_SIZE, config.height + variation));
                adjustMineAmount(config);
            } else if (counterType == MINE_COUNTER) {
                int maxMineAmount = getMaxMineAmount(config);
                config.mineAmount = Math.max(1, Math.min(maxMineAmount, config.mineAmount + variation));
            }

            updateDisplayedValues(config, widthValue, heightValue, mineValue);
        }

        /**
         * Ajuste les mines si la grille rétrécit.
         *
         * @param config configuration à corriger
         */
        private void adjustMineAmount(Config config) {
            int maxMineAmount = getMaxMineAmount(config);
            if (config.mineAmount > maxMineAmount) {
                config.mineAmount = maxMineAmount;
            }
        }

        /**
         * Renvoie le nombre max de mines.
         *
         * @param config configuration actuelle
         * @return nombre maximal de mines
         */
        private int getMaxMineAmount(Config config) {
            return config.width * config.height - 1;
        }
    }

    /**
     * Retour au menu.
     */
    private static class BackToMenuAction implements ActionListener {
        private final JFrame frame;

        /**
         * Crée l'action de retour.
         *
         * @param frame fenêtre principale
         */
        BackToMenuAction(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Display.drawMainMenu(frame);
        }
    }

    /**
     * Lancement de la partie.
     */
    private static class PlayAction implements ActionListener {
        private final JFrame frame;
        private final Config config;

        /**
         * Crée l'action de lancement.
         *
         * @param frame fenêtre principale
         * @param config configuration à utiliser
         */
        PlayAction(JFrame frame, Config config) {
            this.frame = frame;
            this.config = config;
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Demineur demineur = new Demineur(config.width, config.height, config.mineAmount);
            Display.drawGame(frame, demineur, config.width, config.height);
        }
    }
}