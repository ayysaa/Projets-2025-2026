package game;

/**
 * Gère les différents marqueurs.
 */
public class Marqueur {

    /** Identifiants pour les différents types de marqueurs. */
    public static final int EMPTY = 0;
    public static final int STARS = 1;
    public static final int DOUBT = 2;

    private static final String SYMBOL_EMPTY = "";
    private static final String SYMBOL_STARS = "\u2605";
    private static final String SYMBOL_DOUBT = "?";

    /**
     * Détermine le prochain état du marqueur dans le cycle (Vide -> Étoile -> Doute -> Vide).
     *
     * @param mark l'identifiant du marqueur actuel
     * @return l'identifiant du marqueur suivant
     */
    public static int next(int mark) {
        if (mark == EMPTY) {
            return STARS;
        }
        if (mark == STARS) {
            return DOUBT;
        }
        return EMPTY;
    }

    /**
     * Renvoie le symbole en text associé à un identifiant de marqueur.
     *
     * @param mark l'identifiant du marqueur
     * @return la chaine de caractères représentant le marqueur
     */
    public static String getSymbol(int mark) {
        if (mark == EMPTY) {
            return SYMBOL_EMPTY;
        }
        if (mark == STARS) {
            return SYMBOL_STARS;
        }
        return SYMBOL_DOUBT;
    }

    /**
     * Vérifie si le marqueur correspond à une étoile (indication de mine).
     *
     * @param mark l'identifiant du marqueur à vérifier
     * @return true si le marqueur est une étoile, false sinon
     */
    public static boolean isStars(int mark) {
        return mark == STARS;
    }
}