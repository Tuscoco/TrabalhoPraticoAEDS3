package algorithms.patternMatching.BruteForce;

/*
 * Classe que implementa o algoritmo de força bruta para busca de padrões em strings.
 */
public class BruteForce {

    public static boolean search(String text, String pattern) {
        int n = text.length();
        int m = pattern.length();

        for (int i = 0; i <= n - m; i++) {
            int j;

            for (j = 0; j < m; j++) {
                if (text.charAt(i + j) != pattern.charAt(j)) {
                    break;
                }
            }

            if (j == m) {
                return true;
            }
        }

        return false;
    }
}
