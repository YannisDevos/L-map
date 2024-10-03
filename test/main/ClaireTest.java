package main;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import MyException.NoPathFoundException;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import graph.TypeCout;
import graph.Voyageur;

public class ClaireTest {

    private Voyageur voyageur;
    private Platforme plat;
    private String[] data = new String[]{
        "villeA;villeB;Train;60;1.7;80",
        "villeB;villeD;Train;22;2.4;40",
        "villeA;villeC;Train;42;1.4;50",
        "villeB;villeC;Train;14;1.4;60",
        "villeC;villeD;Avion;110;150;22",
        "villeC;villeD;Train;65;1.2;90"
        };

    @Test
    @DisplayName("ClaireTest")
    public void test1() throws NoPathFoundException{
        plat = new Platforme();
        voyageur = new Voyageur();

        plat.init(ModaliteTransport.TRAIN);
        plat.createkpcc("villeA", "villeD");
        List<Chemin> chemins = plat.critere(plat.getKpccHashMap().get(voyageur.getType()), voyageur, TypeCout.CO2, 4.5);
        assertEquals(chemins.get(0), plat.getKpccHashMap().get(TypeCout.CO2).get(1));
    }
}
