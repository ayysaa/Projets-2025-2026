package game;

import java.io.*;

// Format du fichier de sauvegarde (une valeur par ligne) :
// width
// height
// mineAmount
// revealedCases
// startTime (millisecondes)
// puis pour chaque case, une ligne : x,y,mine,discovered,mineAroundAmount,mark

/**
 * Gère la sauvegarde et le chargement des parties de démineur.
 */
public class SaveManager {

    private static final String SAVE_FILE = "save.txt";
    private static final String DATA_SEPARATOR = ",";

    /**
     * Sauvegarde l'état complet du Demineur dans save.txt.
     *
     * @param demineur l'instance du jeu à sauvegarder
     */
    public static void save(Demineur demineur) {
        try {
            FileWriter fileWriter = new FileWriter(SAVE_FILE);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            bufferedWriter.write(String.valueOf(demineur.getWidth()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(demineur.getHeight()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(demineur.getMineAmount()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(demineur.getRevealedCases()));
            bufferedWriter.newLine();
            bufferedWriter.write(String.valueOf(demineur.getStartTime()));
            bufferedWriter.newLine();

            Case[][] grid = demineur.getGrid();
            for (int x = 0; x < demineur.getWidth(); x++) {
                for (int y = 0; y < demineur.getHeight(); y++) {
                    Case c = grid[x][y];
                    // format : x,y,mine,discovered,mineAroundAmount,mark
                    bufferedWriter.write(x + DATA_SEPARATOR + y + DATA_SEPARATOR + c.isMine() + DATA_SEPARATOR + c.isDiscovered() + DATA_SEPARATOR + c.getMineAroundAmount() + DATA_SEPARATOR + c.getMark());
                    bufferedWriter.newLine();
                }
            }

            bufferedWriter.close();
            System.out.println("[Save] Partie sauvegardée dans " + SAVE_FILE);
        } catch (IOException e) {
            System.out.println("[Save] Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge la sauvegarde et retourne un Demineur reconstruit, ou null si échec.
     *
     * @return le jeu de démineur chargé, ou null en cas d'erreur ou d'absence de sauvegarde
     */
    public static Demineur load() {
        try {
            FileReader fileReader = new FileReader(SAVE_FILE);
            BufferedReader bufferedReader = new BufferedReader(fileReader);

            int width = Integer.parseInt(bufferedReader.readLine());
            int height = Integer.parseInt(bufferedReader.readLine());
            int mineAmount = Integer.parseInt(bufferedReader.readLine());
            int revealedCases = Integer.parseInt(bufferedReader.readLine());
            long startTime = Long.parseLong(bufferedReader.readLine());

            Demineur demineur = new Demineur(width, height, mineAmount);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] dataParts = line.split(DATA_SEPARATOR);
                int x = Integer.parseInt(dataParts[0]);
                int y = Integer.parseInt(dataParts[1]);
                boolean isMine = Boolean.parseBoolean(dataParts[2]);
                boolean isDiscovered = Boolean.parseBoolean(dataParts[3]);
                int mineAroundAmount = Integer.parseInt(dataParts[4]);
                int mark = Integer.parseInt(dataParts[5]);

                Case c = demineur.getCase(x, y);
                if (isMine) {
                    c.setMine();
                }
                if (isDiscovered) {
                    c.setVisible();
                }
                c.setMineAroundAmount(mineAroundAmount);
                c.setMark(mark);
            }

            bufferedReader.close();

            demineur.setRevealedCases(revealedCases);
            demineur.setStartTime(startTime);
            demineur.setMinesPlaced(true);

            System.out.println("[DEBUG] Partie chargée depuis " + SAVE_FILE);
            return demineur;

        } catch (FileNotFoundException e) {
            System.out.println("[DEBUG] Aucune sauvegarde trouvée.");
            return null;
        } catch (IOException e) {
            System.out.println("[DEBUG] Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    /**
     * Vérifie si un fichier de sauvegarde existe.
     *
     * @return true si le fichier existe, false sinon
     */
    public static boolean saveExists() {
        File file = new File(SAVE_FILE);
        return file.exists();
    }

    /**
     * Supprime la sauvegarde (appelé en fin de partie).
     */
    public static void deleteSave() {
        File file = new File(SAVE_FILE);
        if (file.exists()) {
            file.delete();
            System.out.println("[DEBUG] Sauvegarde supprimée.");
        }
    }
}