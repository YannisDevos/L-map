package main;

import java.util.Scanner;

import MyException.DataInvalidException;
import MyException.MyInvalidChoiceException;
import MyException.MyInvalidNameException;
import MyException.NoPathFoundException;
import graph.MonLieu;
import graph.Voyageur;

public class Main {
    public static void main(String[] args) throws NoPathFoundException, MyInvalidNameException, MyInvalidChoiceException, DataInvalidException {
        Scanner sc = new Scanner(System.in);
        Voyageur v = new Voyageur();
        Platforme p = new Platforme();

        v.init();
        p.init(v.getMd());
        
        System.out.println("Quel est point départ ? ");

        for (MonLieu ml : p.getLieux()) {
            System.out.println(ml.getNom());
        }

        String depart = sc.nextLine();
        if (depart == null) {
            throw new MyInvalidNameException("Le nom de ville ne peut pas être null !");
        }
        
        for (int i = 0; i < depart.length(); i++) {
            if(depart.charAt(i) >= '1' && depart.charAt(i) <= '9') {
                throw new MyInvalidNameException("Le nom de la ville ne doit contenir que des lettres !");                
            }   
        }

        int cpt = 0;
        for (MonLieu lm : p.getLieux()) {
            if (!lm.getNom().equals(depart)) {
                cpt++;
            }
        }

        if (cpt == p.getLieux().size()) {
            throw new MyInvalidNameException("Le nom que vous avez rentrer n'est pas bon!");                
        }

        v.setDepart(p.findLieu(depart));

//-------------------------------------------------------

        System.out.println("Quel est point arriver ? ");

        for (MonLieu ml : p.getLieux()) {
            if(!ml.getNom().equals(depart)) {
                System.out.println(ml.getNom());
            }
        }

        String arriver = sc.nextLine();
        if (arriver == null) {
            throw new MyInvalidNameException("Le nom de ville ne peut pas être null !");
        }

        for (int i = 0; i < arriver.length(); i++) {
            if(arriver.charAt(i) >= '1' && arriver.charAt(i) <= '9') {
                throw new MyInvalidNameException("Le nom de la ville ne doit contenir que des lettres !");                
            }   
        }

        cpt = 0;
        for (MonLieu lm : p.getLieux()) {
            if (!lm.getNom().equals(arriver)) {
                cpt++;
            }
        }
        
        if (cpt == p.getLieux().size()) {
            throw new MyInvalidNameException("Le nom que vous avez rentrer n'est pas bon!");                
        }
        
        v.setArrive(p.findLieu(arriver));

        System.out.println(p.init2(v, v.getDepart(), v.getArrive()));

        Platforme.csvW(v, p, p.critere(p.getKpccHashMap().get(v.getTypeFirst()), v));
    }
}
    