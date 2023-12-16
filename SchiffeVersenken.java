import java.util.Scanner;

public class SchiffeVersenken {

    // Farben für die Konsolenausgabe
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    public static void main(String[] args) {
        int[][] spielfeld = new int[5][5];
        int anzahlSchiffe = 3;

        zeigeStartbildschirm();
        platziereSchiffe(spielfeld, anzahlSchiffe);
        spielStarten(spielfeld);
    }

    private static void zeigeStartbildschirm() {
        System.out.println(ANSI_CYAN + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~~~~~~~~~~~~" + ANSI_RESET + " Ahoi Seemann! " + ANSI_CYAN + "~~~~~~~~~~~~~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~~" + ANSI_RESET + " Willkommen bei Schiffe Versenken! " + ANSI_CYAN + "~~~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~~~~~~~~~~~~~~~~~~" + ANSI_RESET + ANSI_YELLOW + "⚓︎" + ANSI_RESET + ANSI_CYAN + "~~~~~~~~~~~~~~~~~~~~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~~~~~" + ANSI_RESET + "Versuche 3 Schiffe zu treffen." + ANSI_CYAN + "~~~~~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~" + ANSI_RESET + "Drücke Enter, um das Spiel zu starten." + ANSI_CYAN + "~~~~~" + ANSI_RESET);
        System.out.println(ANSI_CYAN + "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + ANSI_RESET);

        new Scanner(System.in).nextLine(); // Warte auf Eingabe
    }

    public static void platziereSchiffe(int[][] spielfeld, int anzahlSchiffe) {
        for (int i = 0; i < anzahlSchiffe; i++) {
            int schiffLaenge = 2;
            boolean horizontal = Math.random() < 0.5;

            boolean platziert = false;
            while (!platziert) {
                int startSpalte = (int) (Math.random() * (5 - schiffLaenge + 1));
                int startZeile = (int) (Math.random() * 5);

                if (horizontal && istPlatzHorizontal(spielfeld, startZeile, startSpalte, schiffLaenge)) {
                    platziereSchiffHorizontal(spielfeld, startZeile, startSpalte, schiffLaenge);
                    platziert = true;
                } else if (!horizontal && istPlatzVertikal(spielfeld, startZeile, startSpalte, schiffLaenge)) {
                    platziereSchiffVertikal(spielfeld, startZeile, startSpalte, schiffLaenge);
                    platziert = true;
                }
            }
        }
    }

    public static boolean istPlatzHorizontal(int[][] spielfeld, int zeile, int spalte, int laenge) {
        if (spalte + laenge > spielfeld[0].length) {
            return false;
        }

        for (int j = spalte - 1; j <= spalte + laenge; j++) {
            if (j >= 0 && j < spielfeld[0].length && (zeile - 1 >= 0 && spielfeld[zeile - 1][j] != 0 ||
                    zeile + 1 < spielfeld.length && spielfeld[zeile + 1][j] != 0 || spielfeld[zeile][j] != 0)) {
                return false; 
            }
        }

        return true;
    }

    public static boolean istPlatzVertikal(int[][] spielfeld, int zeile, int spalte, int laenge) {
        if (zeile + laenge > spielfeld.length) {
            return false;
        }

        for (int i = zeile - 1; i <= zeile + laenge; i++) {
            if (i >= 0 && i < spielfeld.length && (spalte - 1 >= 0 && spielfeld[i][spalte - 1] != 0 ||
                    spalte + 1 < spielfeld[0].length && spielfeld[i][spalte + 1] != 0 || spielfeld[i][spalte] != 0)) {
                return false; 
            }
        }

        return true;
    }

    public static void platziereSchiffHorizontal(int[][] spielfeld, int zeile, int spalte, int laenge) {
        for (int j = 0; j < laenge; j++) {
            spielfeld[zeile][spalte + j] = 1;
        }
    }

    public static void platziereSchiffVertikal(int[][] spielfeld, int zeile, int spalte, int laenge) {
        for (int i = 0; i < laenge; i++) {
            spielfeld[zeile + i][spalte] = 1;
        }
    }

    public static void spielStarten(int[][] spielfeld) {
        Scanner scanner = new Scanner(System.in);

        int anzahlSchiffe = 3;
        int versenkteSchiffe = 0;
        int versuche = 0;
        int zeile, spalte;

        while (versenkteSchiffe < 3) {
            printSpielfeld(spielfeld);

            
            do {
                System.out.print("Gib die Zeile ein (0-4): ");
                zeile = scanner.nextInt();
                if (zeile < 0 || zeile >= spielfeld.length) {
                    System.out.println("Ungültige Zeile. Bitte gib eine Zeile zwischen 0 und 4 ein.");
                }
            } while (zeile < 0 || zeile >= spielfeld.length);

            
            do {
                System.out.print("Gib die Spalte ein (0-4): ");
                spalte = scanner.nextInt();
                if (spalte < 0 || spalte >= spielfeld[0].length) {
                    System.out.println("Ungültige Spalte. Bitte gib eine Spalte zwischen 0 und 4 ein.");
                }
            } while (spalte < 0 || spalte >= spielfeld[0].length);

            if (spielfeld[zeile][spalte] == 1) {
                System.out.println(ANSI_GREEN + "Treffer!" + ANSI_RESET);
                spielfeld[zeile][spalte] = 2; // Markiere das Schiff als getroffen

                // Überprüft ob das Schiff versenkt wurde
                if (schiffVersenkt(spielfeld, zeile, spalte)) {
                    System.out.println(ANSI_GREEN + "Schiff versenkt!" + ANSI_RESET);
                    versenkteSchiffe++;
                }
            } else if (spielfeld[zeile][spalte] == 0) {
                System.out.println(ANSI_RED + "Verfehlt!" + ANSI_RESET);
                spielfeld[zeile][spalte] = 3; // Markiere das Feld als verfehlt
            } else {
                System.out.println("Dieses Feld wurde bereits angespielt. Wähle ein anderes Feld.");
            }

            versuche++;
        }

        printSpielfeld(spielfeld);

        // Überprüfung auf Gewinnbedingung
        if (versenkteSchiffe >= 2) {
            System.out.println("Herzlichen Glückwunsch! Du hast alle Schiffe versenkt!");
            System.out.println("Anzahl Versuche: " + versuche);
        }

        scanner.close();
    }

    public static boolean schiffVersenkt(int[][] spielfeld, int zeile, int spalte) {
        int schiffLaenge = 2;

        // Überprüfe horizontal
        int countHorizontal = 0;
        for (int j = 0; j < schiffLaenge; j++) {
            if (spalte + j < spielfeld[0].length && spielfeld[zeile][spalte + j] == 2) {
                countHorizontal++;
            }
        }

        // Überprüfe vertikal
        int countVertikal = 0;
        for (int i = 0; i < schiffLaenge; i++) {
            if (zeile + i < spielfeld.length && spielfeld[zeile + i][spalte] == 2) {
                countVertikal++;
            }
        }

        // Rückgabe true, wenn alle Teile des Schiffs in horizontaler oder vertikaler Richtung getroffen wurden
        return countHorizontal == schiffLaenge || countVertikal == schiffLaenge;
    }

    public static void printSpielfeld(int[][] spielfeld) {
        System.out.println("  0 1 2 3 4");
        for (int i = 0; i < spielfeld.length; i++) {
            System.out.print(i + " ");
            for (int j = 0; j < spielfeld[i].length; j++) {
                if (spielfeld[i][j] == 2) {
                    System.out.print(ANSI_YELLOW + "S " + ANSI_RESET);
                } else if (spielfeld[i][j] == 3) {
                    System.out.print(ANSI_RED + "X " + ANSI_RESET);
                } else {
                    System.out.print(ANSI_CYAN + ". " + ANSI_RESET);
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}
