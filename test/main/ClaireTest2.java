package main;

import java.util.ArrayList;
import java.util.List;

import MyException.DataInvalidException;
import MyException.MyInvalidChoiceException;
import MyException.MyInvalidNameException;
import MyException.NoPathFoundException;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import graph.TypeCout;
import graph.Voyageur;

public class ClaireTest2 {
    public static void main(String[] args) throws MyInvalidNameException, MyInvalidChoiceException, NoPathFoundException, DataInvalidException {
        List<ModaliteTransport> md = new ArrayList<>();
        md.add(ModaliteTransport.TRAIN);
        md.add(ModaliteTransport.AVION);
        Voyageur v = new Voyageur("Claire", md, TypeCout.TEMPS, 120.0, TypeCout.PRIX, 250);
        Platforme p = new Platforme();

        p.init(v.getMd());

        v.setDepart(p.getLieux().get(0));
        v.setArrive(p.getLieux().get(3));

        p.init2(v, v.getDepart(), v.getArrive());

        p.afficherListeCheminsFiltres(p.getKpccHashMap().get(v.getTypeFirst()));

        System.out.println("Voyage optimal calculer par nous: A -> C -> D");
        System.out.println("Nous voyons que c'est les mêmes");

    }
}
