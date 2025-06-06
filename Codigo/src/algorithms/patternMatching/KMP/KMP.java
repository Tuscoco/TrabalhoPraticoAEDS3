package algorithms.patternMatching.KMP;


/*
 * Classe que implementa o algoritmo KMP (Knuth-Morris-Pratt) para busca de padrões em strings.
 */
public class KMP {

    /*
     * Montagem da tabela de prefixos para determinar o salto de caracteres
     */
    private static int[] computePrefixTable(String pattern){

        int m = pattern.length();
        int[] lps = new int[m];
        int j = 0; 
        int i = 1;

        lps[0] = 0; 

        while(i < m){

            if (pattern.charAt(i) == pattern.charAt(j)){

                lps[i++] = ++j;

            }else{

                if(j != 0){

                    j = lps[j - 1];

                }else{

                    lps[i++] = j;

                } 

            }

        }

        return lps;

    }

    /*
     * Método que conta quantas vezes o padrão aparece no texto
     */
    public static int matchPatternAt(String text, String pattern){

        int count = 0;
        int n = text.length();
        int m = pattern.length();
        int[] lps = computePrefixTable(pattern);

        int i = 0; 
        int j = 0; 

        while(i < n){

            if(pattern.charAt(j) == text.charAt(i)){

                j++;
                i++;

            }

            if(j == m){

                count++;
                j = lps[j - 1];

            }else if(i < n && pattern.charAt(j) != text.charAt(i)){

                if(j != 0){

                    j = lps[j - 1];

                }else{

                    i = i + 1;

                }

            }

        }

        return count;

    }

    /*
     * Método que verifica se o padrão existe no texto
     * Se existir pelo menos uma vez, retorna true
     * Se não estiver em nenhum local do texto, retorna false
     */
    public static boolean search(String text, String pattern){

        int n = text.length();
        int m = pattern.length();
        int[] lps = computePrefixTable(pattern);

        int i = 0; 
        int j = 0; 

        while(i < n){

            if(pattern.charAt(j) == text.charAt(i)){

                j++;
                i++;

            }

            if(j == m){

                return true;

            }else if(i < n && pattern.charAt(j) != text.charAt(i)){

                if (j != 0){

                    j = lps[j - 1];

                }else{

                    i = i + 1;

                }

            }

        }

        return false;

    }

}