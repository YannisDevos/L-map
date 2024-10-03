package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import MyException.DataInvalidException;
import MyException.MyInvalidChoiceException;
import MyException.MyInvalidNameException;
import MyException.NoPathFoundException;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import graph.TypeCout;
import graph.Voyageur;

public class ClaireTest3 {
    public static void main(String[] args) throws MyInvalidNameException, MyInvalidChoiceException, NoPathFoundException, DataInvalidException {
        List<ModaliteTransport> md = new ArrayList<>();
        md.add(ModaliteTransport.TRAIN);
        md.add(ModaliteTransport.AVION);
        Voyageur v = new Voyageur("Claire", md, TypeCout.TEMPS, 150, TypeCout.CO2, 5);
        Platforme p = new Platforme();

        p.init(v.getMd());

        v.setDepart(p.getLieux().get(0));
        v.setArrive(p.getLieux().get(1));

        p.init2(v, v.getDepart(), v.getArrive());

        p.afficherListeCheminsFiltres(p.getKpccHashMap().get(v.getTypeFirst()));

        System.out.println(p.afficherListeCheminsDetaille(p.critere(p.getKpccHashMap().get(v.getTypeFirst()), v)));

        Platforme.csvW(v, p, p.critere(p.getKpccHashMap().get(v.getTypeFirst()), v));

        ArrayList<String[]> arr = new ArrayList<>();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("res" + System.getProperty("file.separator") + "Historique" + System.getProperty("file.separator") + v.getNom() + ".csv"))) {
            String line;
            String[] tokenizedLine;
    
            while ((line = bufferedReader.readLine()) != null) {
                tokenizedLine = line.split(";");
                arr.add(tokenizedLine);
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier spécifié est introuvable : " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }

        System.out.println(arr.get(0)[0].equals(" Chemin 1: Depart: A -> B, Modalite: TRAIN, Cout: {CO2=0.212, TEMPS=25.0, PRIX=16.0} | Total: {CO2=0.212, TEMPS=25.0, PRIX=16.0}"));
        System.out.println(arr.get(0)[1].equals(" Chemin 2: Depart: A -> B, Modalite: TRAIN, Cout: {CO2=0.509, TEMPS=50.0, PRIX=16.0} | Total: {CO2=0.509, TEMPS=50.0, PRIX=16.0}"));
    }
}