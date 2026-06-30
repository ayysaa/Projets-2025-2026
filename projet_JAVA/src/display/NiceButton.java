package display;

import java.awt.Color;
import java.awt.Cursor;
import javax.swing.JButton;

/**
 * Bouton personnalisé pour le design.
 */
public class NiceButton extends JButton {

    private static final Color BG_COLOR = new Color(86, 97, 122); // Gris-bleu (Demineur.borderColor)
    private static final Color TEXT_COLOR = Color.WHITE;

    /**
     * Crée un bouton very nice.
     *
     * @param text le texte à afficher
     */
    public NiceButton(String text) {
        super(text);
        setBackground(BG_COLOR);
        setForeground(TEXT_COLOR);
        setOpaque(true);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setFocusPainted(false); // permet de supprimer le rectangle de "focus" lorsqu'on clique sur un bouton;
    }
}