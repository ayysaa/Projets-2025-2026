package game;

import javax.swing.*;

/**
 * Représente une case individuelle dans la grille du démineur.
 */
public class Case {
    
    /** Panneau graphique associé à la case. */
    public JPanel graphic;
    
    /** Label contenant le texte ou l'icône de la case. */
    public JLabel content;
    
    private int x;
    private int y;
    private boolean isMine;
    private boolean isDiscovered;
    private int mineAroundAmount;
    private int mark;

    /**
     * Crée une nouvelle case aux coordonnées spécifiées.
     *
     * @param x coordonnée X de la case dans la grille
     * @param y coordonnée Y de la case dans la grille
     */
    public Case(int x, int y) {
        this.x = x;
        this.y = y;
        this.isDiscovered = false;
        this.isMine = false;
        this.mineAroundAmount = 0;
        this.mark = Marqueur.EMPTY;
    }

    /**
     * Marque la case comme étant découverte par le joueur.
     */
    public void setVisible() {
        this.isDiscovered = true;
    }
    
    /**
     * Place une mine sur cette case.
     */
    public void setMine() {
        this.isMine = true;
    }

    /**
     * Associe un composant graphique à cette case pour l'affichage.
     *
     * @param graphic le panneau JPanel représentant la case
     */
    public void setGraphic(JPanel graphic) {
        this.graphic = graphic;
    }

    /**
     * Définit le nombre de mines adjacentes à cette case.
     *
     * @param mAround le nombre de mines autour de la case
     */
    public void setMineAroundAmount(int mAround) {
        this.mineAroundAmount = mAround;
    }

    /**
     * Vérifie si la case contient une mine.
     *
     * @return true si la case contient une mine, false sinon
     */
    public boolean isMine() {
        return this.isMine;
    }

    /**
     * Vérifie si la case a été découverte.
     *
     * @return true si la case est découverte, false sinon
     */
    public boolean isDiscovered() {
        return this.isDiscovered;
    }

    /**
     * Passe au marqueur suivant (ordre et cycle: Vide -> Étoile -> Doute -> Vide).
     */
    public void cycleMark() {
        this.mark = Marqueur.next(this.mark);
    }

    /**
     * Renvoie le nombre de mines entourant cette case.
     *
     * @return le nombre de mines adjacentes
     */
    public int getMineAroundAmount() {
        return this.mineAroundAmount;
    }

    /**
     * Renvoie l'état du marqueur actuel de la case.
     *
     * @return l'identifiant du marqueur
     */
    public int getMark() {
        return this.mark;
    }

    /**
     * Renvoie les coordonnées de la case dans la grille.
     *
     * @return un tableau d'entiers contenant [x, y]
     */
    public int[] getCoords() {
        return new int[]{this.x, this.y};
    }

    /**
     * Définit directement l'état du marqueur de la case.
     *
     * @param mark le nouvel identifiant du marqueur
     */
    public void setMark(int mark) { 
        this.mark = mark;
    }
}