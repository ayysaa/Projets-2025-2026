package game;

import java.awt.Color;

/**
 * Moteur principal du jeu de démineur gérant la grille et la logique de partie.
 */
public class Demineur {
    
    /** Identifiant pour les différents états de la partie. */
    public static final int IN_PROGRESS = 0;
    public static final int VICTORY = 1;
    public static final int DEFEAT = 2;

    private static final int MS_PER_SECOND = 1000;

    private final int width;
    private final int height;
    private final int mineAmount;
    private boolean minesPlaced;
    private int revealedCases;
    private Case[][] grid;
    private int currentState;
    private long startTime;
    private long endTime;

    /** Couleur par défaut d'une case non découverte. */
    public static Color defaultCaseColor = new Color(36, 41, 54);
    /** Couleur d'une case survolée. */
    public static Color hoverCaseColor = new Color(255, 0, 0);
    /** Couleur d'une case découverte. */
    public static Color discoveredCaseColor = new Color(70, 130, 255);
    /** Couleur des bordures de la grille. */
    public static Color borderColor = new Color(86, 97, 122);
    /** Couleur de fond du jeu. */
    public static Color bgColor = new Color(16, 18, 24);

    /**
     * Initialise une nouvelle partie de démineur.
     *
     * @param w largeur de la grille
     * @param h hauteur de la grille
     * @param mAmount nombre de mines à placer
     */
    public Demineur(int w, int h, int mAmount) {
        this.width = w;
        this.height = h;
        this.mineAmount = mAmount;
        this.revealedCases = 0;
        this.grid = new Case[w][h];
        this.createGrid();
        this.currentState = IN_PROGRESS; 
        this.startTime = 0;
        this.endTime = 0;
        this.minesPlaced = false;
    }
    
    /**
     * Renvoie l'état actuel de la partie.
     *
     * @return l'état de la machine (IN_PROGRESS, VICTORY, ou DEFEAT)
     */
    public int getActualState() {
        return this.currentState;
    }

    /**
     * Renvoie la grille complète des cases.
     *
     * @return un tableau à deux dimensions de cases
     */
    public Case[][] getGrid() {
        return this.grid;
    }

    /**
     * Récupère une case spécifique selon ses coordonnées.
     *
     * @param x coordonnée X
     * @param y coordonnée Y
     * @return la case correspondante, ou null si hors limites
     */
    public Case getCase(int x, int y) {
        if (x >= 0 && x < this.grid.length && y >= 0 && y < this.grid[0].length) {
            return this.grid[x][y];
        }
        return null;
    }

    /**
     * Crée la grille en instanciant toutes les cases.
     */
    public void createGrid() {
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                this.grid[x][y] = new Case(x, y);
            }
        }
    }

    /**
     * Utilise l'algorithme de Fisher-Yates pour placer les mines aléatoirement,
     * en garantissant que la première case cliquée (safeX, safeY) ne contient pas de mine.
     *
     * @param safeX coordonnée X de la case cliquée en premier
     * @param safeY coordonnée Y de la case cliquée en premier
     */
    public void placeMines(int safeX, int safeY) {
        int totalMines = this.mineAmount;
        int totalCells = this.width * this.height;

        boolean[] mineFlags = new boolean[totalCells];

        // Crée un tableau 1D pour attibuer les mines
        for (int x = 0; x < totalCells; x++) {
            if (x < totalMines) {
                mineFlags[x] = true;
            }
        }
        
        // Fisher-Yates (source: https://fr.wikipedia.org/wiki/Mélange_de_Fisher-Yates)
        for (int i = totalCells - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            boolean temp = mineFlags[i];
            mineFlags[i] = mineFlags[j];
            mineFlags[j] = temp;
        }

        int safeIndex = safeY * this.width + safeX;
        if (mineFlags[safeIndex]) {
            for (int i = 0; i < totalCells; i++) {
                if (!mineFlags[i] && i != safeIndex) {
                    mineFlags[i] = true;
                    mineFlags[safeIndex] = false;
                    break;
                }
            }
        }

        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                int index = y * this.width + x;
                if (mineFlags[index]) {
                    this.grid[x][y].setMine();
                }
            }
        }
        
        // Pour toute les cases non miné, calcule le nombre de mine adjacente
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                if (!this.grid[x][y].isMine()) {
                    this.grid[x][y].setMineAroundAmount(countMinesAround(this.getCase(x, y)));
                }
            }
        }
    }
    
    /**
     * Calcule le nombre de mines adjacentes à une case.
     *
     * @param nCase la case à analyser
     * @return le nombre de mines autour
     */
    public int countMinesAround(Case nCase) {
        int[] coords = nCase.getCoords();
        int x = coords[0];
        int y = coords[1];
        int count = 0;

        // Parcours des cases de (x-1, y-1) à (x+1, y+1)
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                // vérifie de ne pas aller hors de la grille
                if (i >= 0 && i < width && j >= 0 && j < height) {
                    if (grid[i][j].isMine()) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Découvre une case et propage la découverte si aucune mine n'est autour.
     * Déclenche la défaite ou la victoire selon la case.
     *
     * @param nCase la case cliquée par le joueur
     */
    public void revealCase(Case nCase) {
        if (nCase == null) {
            return;
        }

        if (nCase.isDiscovered()) {
            return;
        }

        // empêche de cliquer sur une case marquée dangereuse
        if (Marqueur.isStars(nCase.getMark())) {
            return;
        }

        if (this.revealedCases == 0 && !this.minesPlaced) {
            int[] coords = nCase.getCoords();
            this.placeMines(coords[0], coords[1]);
            this.startTime = System.currentTimeMillis();
        }

        nCase.setVisible();

        // si c'est une mine, le joueur perd
        if (nCase.isMine()) {
            this.lose();
            return;
        }

        this.revealedCases++;

        if (nCase.getMineAroundAmount() == 0) {
            int[] coords = nCase.getCoords();
            int x = coords[0];
            int y = coords[1];

            for (int i = x - 1; i <= x + 1; ++i) {
                for (int j = y - 1; j <= y + 1; ++j) {
                    Case safeReveal = getCase(i, j);
                    if (safeReveal != null) {
                        revealCase(safeReveal);
                    }
                }
            }
        }

        if (this.winCheck()) {
            this.win();
        }
    }

    /**
     * Fait évoluer le marqueur de la case (Rien -> Étoile -> Doute -> Rien).
     *
     * @param x coordonnée X de la case
     * @param y coordonnée Y de la case
     */
    public void nextMark(int x, int y) {
        Case nCase = this.getCase(x, y);
        if (nCase == null) {
            return;
        }
        if (nCase.isDiscovered()) {
            return;
        }
        nCase.cycleMark();
    }

    /**
     * Vérifie si le nombre de cases découvertes correspond à l'objectif de victoire.
     *
     * @return true si la partie est gagnée, false sinon
     */
    public boolean winCheck() {
        return (this.revealedCases == this.width * this.height - this.mineAmount);
    }

    /**
     * Déclenche la fin de partie par une victoire.
     */
    public void win() {
        System.out.println("[DEBUG] Gagné");
        this.currentState = VICTORY;
        this.endTime = System.currentTimeMillis();
        SaveManager.deleteSave();
    }

    /**
     * Déclenche la fin de partie par une défaite.
     */
    public void lose() {
        System.out.println("[DEBUG] Perdu");
        this.currentState = DEFEAT;
        this.endTime = System.currentTimeMillis();
        SaveManager.deleteSave();
    }

    /**
     * Renvoie le temps de jeu écoulé.
     *
     * @return le temps en secondes
     */
    public int getElapsedTime() {
        if (this.startTime == 0) {
            return 0;
        }
        if (this.currentState == IN_PROGRESS) {
            return (int) (System.currentTimeMillis() - this.startTime) / MS_PER_SECOND;
        }
        return (int) (this.endTime - this.startTime) / MS_PER_SECOND;
    }

    /**
     * Renvoie le nombre de cases découvertes jusqu'à présent.
     *
     * @return le nombre de cases révélées
     */
    public int getRevealedCases() {
        return this.revealedCases;
    }

    /**
     * Renvoie le nombre total de cases sûres à découvrir.
     *
     * @return nombre total de cases - nombre de mines
     */
    public int getTotalCases() {
        return this.width * this.height - this.mineAmount;
    }

/**
     * Sauvegarde la partie en cours.
     */
    public void saveGame() {
        // empêcher la sauvegarde si la partie est gagnée ou perdue
        if (this.currentState == IN_PROGRESS) {
            SaveManager.save(this);
        } else {
            System.out.println("[DEBUG] Partie terminée, sauvegarde ignorée.");
        }
    }

    /**
     * Renvoie la largeur de la grille.
     *
     * @return la largeur en nombre de cases
     */
    public int getWidth() { 
        return this.width;
    }

    /**
     * Renvoie la hauteur de la grille.
     *
     * @return la hauteur en nombre de cases
     */
    public int getHeight() { 
        return this.height;
    }

    /**
     * Renvoie le nombre de mines configuré pour la partie.
     *
     * @return le nombre total de mines
     */
    public int getMineAmount() { 
        return this.mineAmount;
    }

    /**
     * Renvoie l'horodatage de début de la partie.
     *
     * @return le temps de départ en millisecondes
     */
    public long getStartTime() { 
        return this.startTime;
    }

    /**
     * Indique si les mines ont déjà été générées sur la grille.
     *
     * @return true si les mines sont placées, false sinon
     */
    public boolean isMinesPlaced() { 
        return this.minesPlaced;
    }
    
    /**
     * Force la valeur du nombre de cases découvertes.
     *
     * @param n nouvelle valeur pour les cases révélées
     */
    public void setRevealedCases(int n) {
        this.revealedCases = n;
    }

    /**
     * Définit manuellement l'horodatage de début de partie.
     *
     * @param t temps en millisecondes
     */
    public void setStartTime(long t) {
        this.startTime = t;
    }

    /**
     * Met à jour le statut indiquant si les mines ont été placées.
     *
     * @param b true si elles sont placées
     */
    public void setMinesPlaced(boolean b) {
        this.minesPlaced = b;
    }

    /**
     * Calcule le nombre de cases marquées avec une étoile,
     * 
     * @return le nombre de marqueurs de type étoile présents sur la grille
     */
    public int getStarsAmount() {
        int count = 0;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (grid[x][y].getMark() == Marqueur.STARS) {
                    count++;
                }
            }
        }

        return count;
    }
}