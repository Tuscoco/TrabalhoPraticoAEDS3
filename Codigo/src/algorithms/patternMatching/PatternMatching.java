package algorithms.patternMatching;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import algorithms.patternMatching.KMP.KMP;
import algorithms.patternMatching.BruteForce.BruteForce;

import model.Musica;
import repository.CRUDI;

/*
 * Chamada dos algoritmos de casamento de padrões: KMP e Força Bruta.
 */
public class PatternMatching {
    
    private int tipo;

    public PatternMatching(int tipo){

        this.tipo = tipo;

    }

    public List<Musica> search(String padrao, String arquivo) throws IOException{

        if(tipo == 1){

            List<Musica> lista = CRUDI.readAll("data/database/" + arquivo);
            List<Musica> listaPesquisa = new ArrayList<>();

            for(Musica x : lista){

                //KMP
                if(KMP.search(x.toString(), padrao)){

                    listaPesquisa.add(x);

                }

            }

            return listaPesquisa;

        }else{

            List<Musica> lista = CRUDI.readAll("data/database/" + arquivo);
            List<Musica> listaPesquisa = new ArrayList<>();

            for(Musica x : lista){

                //Força Bruta
                if(BruteForce.search(x.toString(), padrao)){

                    listaPesquisa.add(x);

                }

            }

            return listaPesquisa;

        }

    }

}
