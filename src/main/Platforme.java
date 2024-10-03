package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import MyException.DataInvalidException;
import MyException.NoPathFoundException;
import fr.ulille.but.sae_s2_2024.AlgorithmeKPCC;
import fr.ulille.but.sae_s2_2024.Chemin;
import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import fr.ulille.but.sae_s2_2024.MultiGrapheOrienteValue;
import fr.ulille.but.sae_s2_2024.Trancon;
import graph.MonLieu;
import graph.MonTroncon;
import graph.TypeCout;
import graph.Voyageur;
import java.util.Collections;
  
import java.util.Scanner;  
public class Platforme {
    private final String DELIMITER;
    
    private ArrayList<String[]> dataSplit;
    private ArrayList<String[]> dataSplitSort;
    private ArrayList<String[]> dataCorrespondance;
    
    private ArrayList<MonLieu> lieux;
    private ArrayList<MonTroncon> troncons;

    private MultiGrapheOrienteValue[] graphes;
    private MultiGrapheOrienteValue[] graphes2;

    private HashMap<TypeCout, List<Chemin>> kpccHashMap;
    private Scanner scanner;

    public Platforme(){
        this.DELIMITER = ";";
        this.lieux = new ArrayList<MonLieu>();
        this.troncons = new ArrayList<MonTroncon>();
        this.graphes = new MultiGrapheOrienteValue[3];
        this.graphes2 = new MultiGrapheOrienteValue[3];
        this.dataSplit = new ArrayList<String[]>();
        this.dataSplitSort = new ArrayList<String[]>();
        this.dataCorrespondance = new ArrayList<>();
        this.kpccHashMap = new HashMap<TypeCout, List<Chemin>>();
        this.scanner = new Scanner(System.in);
    }

    public ArrayList<String[]> getDataSplit() {
        return dataSplit;
    }

    public ArrayList<String[]> getDataSplitSort() {
        return dataSplitSort;
    }

    public String getDELIMITER() {
        return DELIMITER;
    }

    public ArrayList<MonLieu> getLieux() {
        return lieux;
    }

    public ArrayList<MonTroncon> getTroncons() {
        return troncons;
    }

    public MultiGrapheOrienteValue[] getGraphes() {
        return graphes;
    }

    public HashMap<TypeCout, List<Chemin>> getKpccHashMap() {
        return kpccHashMap;
    }
  
    /**Ecrit dans une fichier csv l'historique avec comme nom de fichier, le nom de la personne
     * @param v
     * @param p
     */
    public static void csvW(Voyageur v, Platforme p, List<Chemin> lc) {
        String fichierCSV = "res/Historique/" + v.getNom() + ".csv";
    
        try (PrintWriter writer = new PrintWriter(new FileWriter(fichierCSV, true));
             BufferedReader br = new BufferedReader(new FileReader(fichierCSV))) {

            String s = p.afficherListeCheminsDetailleCSV(lc);

            writer.println(s.substring(0, s.length()-1));
    
            System.out.println("Données écrites avec succès dans " + fichierCSV);
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier CSV : " + e.getMessage());
        }
    }    

    
    /**Lis un csv et fais une List de tableau de chaine avec
     * @param filename
     * @return
     */
    private ArrayList<String[]> csvRead(String filename) {
        ArrayList<String[]> arr = new ArrayList<>();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line;
            String[] tokenizedLine;
    
            while ((line = bufferedReader.readLine()) != null) {
                tokenizedLine = line.split(this.DELIMITER);
                arr.add(tokenizedLine);
            }
            
        } catch (FileNotFoundException e) {
            System.err.println("Le fichier spécifié est introuvable : " + e.getMessage());
        } catch (IOException e) {
            System.err.println(e.getStackTrace());
        }
    
        return arr;
    }

    /**Tri les données et utilise la methodes split, cela permet d'avoir des données plus claires
     * @param moda
     * @throws IOException 
     */
    private void splitAndTri(List<ModaliteTransport> modalites){
        this.dataSplit = this.csvRead("res/CSV/Data.csv");
    
        for(String[] s : this.dataSplit){
            for (ModaliteTransport moda : modalites) {
                if(s[2].toUpperCase().equals(moda.toString())){
                    this.dataSplitSort.add(s);
                }
            }
        }

        this.dataCorrespondance = this.csvRead("res/CSV/Correspondance.csv");
    }

    /**Renvoie un MonLieu a partir d'un String
     * @param lieu
     * @return
     */
    public MonLieu findLieu(String lieu){
        for (MonLieu l : this.lieux) {
            if (l.getNom().equals(lieu)) {
                return l;
            }
        }
        return null;
    }

    /**Crée les hashmap contenant en clé les TypeCout et en valeur des double correspondant a la valeur des cout
     * @param prix
     * @param co2
     * @param temps
     * @return
     */
    public HashMap<TypeCout, Double> createMapCout(String prix, String co2, String temps){
        HashMap<TypeCout, Double> modaWithNull =  new HashMap<TypeCout, Double>();
        modaWithNull.put(TypeCout.PRIX, Double.valueOf(prix));
        modaWithNull.put(TypeCout.CO2, Double.valueOf(co2));
        modaWithNull.put(TypeCout.TEMPS, Double.valueOf(temps));
        return modaWithNull;
    }

    /**Crée tout les lieux demander
     * 
     */
    public void createLieu(){
        HashSet<String> hString = new HashSet<String>();

        for(int i=0; i < this.dataSplitSort.size(); i++){
            hString.add(this.dataSplitSort.get(i)[0]);
            hString.add(this.dataSplitSort.get(i)[1]);
        }

        for (String s : hString) {
            this.lieux.add(new MonLieu(s));
        }

        Collections.sort(this.lieux);
    }

    /**Crée et rajoute les conrrespondances dans la clase {@link MonLieu}
     * 
     */
    public void createCorrespondances(){
        for (MonLieu l : this.lieux) {
            ArrayList<String[]> current = new ArrayList<>();
            for (int i = 0; i < this.dataCorrespondance.size(); ++i) {
                if(l.getNom().equals(this.dataCorrespondance.get(i)[0])){
                    String[] correspondance = new String[5];
                    correspondance[0] = this.dataCorrespondance.get(i)[1];
                    correspondance[1] = this.dataCorrespondance.get(i)[2];
                    correspondance[2] = this.dataCorrespondance.get(i)[3];
                    correspondance[3] = this.dataCorrespondance.get(i)[4];
                    correspondance[4] = this.dataCorrespondance.get(i)[5];
                    current.add(correspondance);
                }
            }
            l.setCorrespondances(current);
        }
    }
    

    /**Crée tout les troncons demander (dans les deux sens)
     * 
     */
    public void createTroncon(){
        for(int i=0; i < this.dataSplitSort.size(); i++){
            MonLieu l1 = findLieu(this.dataSplitSort.get(i)[0]);
            MonLieu l2 = findLieu(this.dataSplitSort.get(i)[1]);

            this.troncons.add(new MonTroncon((Lieu)l1, (Lieu)l2, ModaliteTransport.valueOf(this.dataSplitSort.get(i)[2].toUpperCase()), createMapCout((this.dataSplitSort.get(i)[3]),(this.dataSplitSort.get(i)[4]), (this.dataSplitSort.get(i)[5]))));
            this.troncons.add(new MonTroncon((Lieu)l2, (Lieu)l1, ModaliteTransport.valueOf(this.dataSplitSort.get(i)[2].toUpperCase()), createMapCout((this.dataSplitSort.get(i)[3]),(this.dataSplitSort.get(i)[4]), (this.dataSplitSort.get(i)[5]))));
        }
    }

    /**Va créer tout les graphes grace aux lieux et troncons
     * 
     */
    public void createGraphes(){
        MultiGrapheOrienteValue graphPrix = new MultiGrapheOrienteValue();
        MultiGrapheOrienteValue graphCO2 = new MultiGrapheOrienteValue();
        MultiGrapheOrienteValue graphTemps = new MultiGrapheOrienteValue();

        for (MonLieu lieu : this.lieux) {
            graphPrix.ajouterSommet(lieu);
            graphCO2.ajouterSommet(lieu);
            graphTemps.ajouterSommet(lieu);
        }

        for (MonTroncon troncon : this.troncons) {
            graphPrix.ajouterArete(troncon, troncon.getCout().get(TypeCout.PRIX));
            graphCO2.ajouterArete(troncon, troncon.getCout().get(TypeCout.CO2));
            graphTemps.ajouterArete(troncon, troncon.getCout().get(TypeCout.TEMPS));
        }

        this.graphes[0] = graphPrix;
        this.graphes[1] = graphCO2;
        this.graphes[2] = graphTemps;
    }

    /**Permet de créer les chemins via l'algorithm kpcc des trois critere de base
     * @param l1
     * @param l2
     */
    public void createkpcc(MonLieu l1, MonLieu l2) throws NoPathFoundException {
    
        if (l1 == null || l2 == null) {
            throw new NoPathFoundException("Point non trouvé");
        }
    
        this.kpccHashMap.put(TypeCout.PRIX, AlgorithmeKPCC.kpcc(this.graphes[0], l1, l2, 10)); 
        this.kpccHashMap.put(TypeCout.CO2, AlgorithmeKPCC.kpcc(this.graphes[1], l1, l2, 10));
        this.kpccHashMap.put(TypeCout.TEMPS, AlgorithmeKPCC.kpcc(this.graphes[2], l1, l2, 10));
    }
    
    /**Renvoie la liste des chemin qui correspond au critere et qui sont dans la liste des chemin de base
     * @param c
     * @param v
     * @return
     */
    public ArrayList<Chemin> critere(List<Chemin> c, Voyageur v) {
        List<Chemin> kpccFirst = kpccHashMap.get(v.getTypeFirst());
        List<Chemin> kpccSecond = kpccHashMap.get(v.getTypeSecond());
        
        if (kpccFirst == null || kpccFirst.isEmpty()) {
            System.err.println("Aucun chemin trouvé pour le critère 1.");
            return new ArrayList<>();
        }
    
        Set<Chemin> res = new HashSet<>();
        
        for (Chemin cheminC : c) {
            for (Chemin cheminKpccFirst : kpccFirst) {
                if (cheminKpccFirst.poids() <= v.getValueTypeFirst() &&
                    compareAretes(cheminKpccFirst.aretes(), cheminC.aretes())) {
                    
                    if (kpccSecond == null || kpccSecond.isEmpty()) {
                        res.add(cheminC);
                    } else {
                        for (Chemin cheminKpccSecond : kpccSecond) {
                            if (cheminKpccSecond.poids() <= v.getValueTypeSecond() &&
                                compareAretes(cheminKpccSecond.aretes(), cheminC.aretes())) {
                                
                                res.add(cheminC);
                            }
                        }
                    }
                }
            }
        }
    
        return new ArrayList<>(res);
    }
    
    /**
     * Compare deux listes d'arêtes pour vérifier si elles sont égales en termes de contenu.
     * @param aretes1
     * @param aretes2
     * @return
     */
    private boolean compareAretes(List<Trancon> aretes1, List<Trancon> aretes2) {
        if (aretes1.size() != aretes2.size()) {
            return false;
        }
    
        for (int i = 0; i < aretes1.size(); i++) {
            Trancon t1 = aretes1.get(i);
            Trancon t2 = aretes2.get(i);
            if (!t1.equals(t2)) { 
                return false;
            }
        }
    
        return true;
    }

    /**
     * @param l1
     * @param l2
     * @throws DataInvalidException
     * @throws NoPathFoundException
     */
    public void cheminMultiModa(MonLieu l1, MonLieu l2) throws DataInvalidException, NoPathFoundException {
        List<Chemin> chemins = this.getKpccHashMap().get(TypeCout.CO2);
    
        if (chemins == null || chemins.isEmpty()) {
            throw new NoPathFoundException("Aucun chemin trouvé pour le coût CO2.");
        }
    
        for (Chemin c : chemins) {
            List<Trancon> aretes = c.aretes();
    
            if (aretes.isEmpty()) {
                throw new DataInvalidException("Chemin sans arêtes.");
            } else {
                ModaliteTransport previousModalite = aretes.get(0).getModalite();
    
                for (int i = 0; i < aretes.size(); ++i) {
                    ModaliteTransport currentModalite = aretes.get(i).getModalite();
                    if (!currentModalite.equals(previousModalite)) {
                        double incPrix = ((MonTroncon) aretes.get(i)).getCout().get(TypeCout.PRIX);
                        double incCO2 = ((MonTroncon) aretes.get(i)).getCout().get(TypeCout.CO2);
                        double incTemps = ((MonTroncon) aretes.get(i)).getCout().get(TypeCout.TEMPS);
    
                        double plusPrix = Double.parseDouble(((MonLieu) ((MonTroncon) aretes.get(i)).getDepart()).getOneCorrespondances(previousModalite)[0]) / 2;
                        double plusCO2 = Double.parseDouble(((MonLieu) ((MonTroncon) aretes.get(i)).getDepart()).getOneCorrespondances(previousModalite)[1]) / 2;
                        double plusTemps = Double.parseDouble(((MonLieu) ((MonTroncon) aretes.get(i)).getDepart()).getOneCorrespondances(previousModalite)[2]) / 2;
    
                        ((MonTroncon) aretes.get(i)).getCout().put(TypeCout.PRIX, (incPrix + plusPrix));
                        ((MonTroncon) aretes.get(i)).getCout().put(TypeCout.CO2, (incCO2 + plusCO2));
                        ((MonTroncon) aretes.get(i)).getCout().put(TypeCout.TEMPS, (incTemps + plusTemps));
                    }
                    previousModalite = currentModalite;
                }
            }
        }

        MultiGrapheOrienteValue graphPrix = new MultiGrapheOrienteValue();
        MultiGrapheOrienteValue graphCO2 = new MultiGrapheOrienteValue();
        MultiGrapheOrienteValue graphTemps = new MultiGrapheOrienteValue();
    
        for (MonLieu lieu : this.lieux) {
            graphPrix.ajouterSommet(lieu);
            graphCO2.ajouterSommet(lieu);
            graphTemps.ajouterSommet(lieu);
        }
    
        for (MonTroncon troncon : this.troncons) {
            graphPrix.ajouterArete(troncon, troncon.getCout().get(TypeCout.PRIX));
            graphCO2.ajouterArete(troncon, troncon.getCout().get(TypeCout.CO2));
            graphTemps.ajouterArete(troncon, troncon.getCout().get(TypeCout.TEMPS));
        }
    
        this.graphes2[0] = graphPrix;
        this.graphes2[1] = graphCO2;
        this.graphes2[2] = graphTemps;
    
        if (l1 == null || l2 == null) {
            throw new NoPathFoundException("Lieu de départ ou d'arrivée invalide.");
        }
    
        this.kpccHashMap.put(TypeCout.PRIX, AlgorithmeKPCC.kpcc(this.graphes2[0], l1, l2, 10));
        this.kpccHashMap.put(TypeCout.CO2, AlgorithmeKPCC.kpcc(this.graphes2[1], l1, l2, 10));
        this.kpccHashMap.put(TypeCout.TEMPS, AlgorithmeKPCC.kpcc(this.graphes2[2], l1, l2, 10));
    }
    

    /**Initialise la platforme (kpcc a faire apar)
     * @param moda
     * @throws NoPathFoundException 
     * @throws DataInvalidException 
     */
    public void init(List<ModaliteTransport> modalites){

        this.splitAndTri(modalites);

        this.createLieu();
        this.createTroncon();
        this.createGraphes();
        this.createCorrespondances();
    }

    public String init2(Voyageur v, MonLieu l1, MonLieu l2) throws NoPathFoundException, DataInvalidException{
        this.createkpcc(v.getDepart(), v.getArrive());
        this.cheminMultiModa(l1, l2);
        return afficherListeCheminsDetaille(this.critere(this.getKpccHashMap().get(v.getTypeFirst()), v));
    }

    /**Affiche un chemin exemple: "Chemin 1: Depart: villeD, Arriver: villeB, Modalite: TRAIN, Cout: {CO2=2.4, TEMPS=40.0, PRIX=22.0} | Depart: villeB, Arriver: villeA, Modalite: TRAIN, Cout: {CO2=1.7, TEMPS=80.0, PRIX=60.0} | Poids: 82.0"
     * @param chemin
     * @param index
     */
    public String afficherChemin(Chemin chemin, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("Chemin ").append(index).append(": ");
        for (Trancon troncon : chemin.aretes()) {
            sb.append("Depart: ").append(((MonLieu) troncon.getDepart()).getNom())
              .append(", Arriver: ").append(((MonLieu) troncon.getArrivee()).getNom())
              .append(", Modalite: ").append(troncon.getModalite())
              .append(", Cout: ").append(((MonTroncon) troncon).getCout())
              .append(" | ");
        }
        sb.append("Poids: ").append(chemin.poids());
        return sb.toString();
    }
    
    /**
     * @param chemins
     * @return
     */
    public String afficherListeChemins(List<Chemin> chemins) {
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Chemin chemin : chemins) {
            sb.append(afficherChemin(chemin, index)).append("\n");
            index++;
        }
        return sb.toString();
    }
    
    /**
     * @param chemin
     * @return
     */
    public String afficherCheminResume(Chemin chemin) {
        StringBuilder sb = new StringBuilder();
        List<Trancon> aretes = chemin.aretes();
    
        if (aretes.isEmpty()) {
            sb.append("Chemin vide");
        } else {
            sb.append(((MonLieu) aretes.get(0).getDepart()).getNom());
            for (Trancon troncon : aretes) {
                sb.append(" -> ").append(((MonLieu) troncon.getArrivee()).getNom());
            }
            sb.append(" | Poids: ").append(chemin.poids());
        }
    
        return sb.toString();
    }
    
    /**
     * @param chemins
     * @return
     */
    public String afficherListeCheminsResume(List<Chemin> chemins) {
        StringBuilder sb = new StringBuilder();
        for (Chemin chemin : chemins) {
            sb.append(afficherCheminResume(chemin)).append("\n");
        }
        return sb.toString();
    }
    
    /**
     * @param chemin
     * @return
     */
    public String afficherCheminFiltre(Chemin chemin) {
        StringBuilder sb = new StringBuilder();
        List<Trancon> aretes = chemin.aretes();
    
        if (aretes.isEmpty()) {
            sb.append("Chemin vide");
        } else {
            sb.append(((MonLieu) aretes.get(0).getDepart()).getNom());
            ModaliteTransport previousModalite = aretes.get(0).getModalite();
    
            for (Trancon troncon : aretes) {
                ModaliteTransport currentModalite = troncon.getModalite();
                if (!currentModalite.equals(previousModalite)) {
                    sb.append(" -> ").append(((MonLieu) troncon.getArrivee()).getNom());
                    sb.append(" [Changement de modalité: ").append(currentModalite).append("] ");
                    previousModalite = currentModalite;
                }
            }
    
            sb.append(" -> ").append(((MonLieu) aretes.get(aretes.size() - 1).getArrivee()).getNom());
        }
        return sb.toString();
    }
    
    /**
     * @param chemins
     * @return
     */
    public String afficherListeCheminsFiltres(List<Chemin> chemins) {
        if (chemins == null) {
            return "La liste de chemins est vide.";
        }
        StringBuilder sb = new StringBuilder();
        for (Chemin chemin : chemins) {
            sb.append(afficherCheminFiltre(chemin)).append("\n");
        }
        return sb.toString();
    }
    
    /** Affiche un chemin avec les informations détaillées et les changements de modalité.
     * @param chemin
     * @param index
     * @return
     */
    public String afficherCheminDetaille(Chemin chemin, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("Chemin ").append(index).append(": ");
        List<Trancon> aretes = chemin.aretes();
    
        if (aretes.isEmpty()) {
            sb.append("Chemin vide");
        } else {
            sb.append("Depart: ").append(((MonLieu) aretes.get(0).getDepart()).getNom());
    
            ModaliteTransport previousModalite = aretes.get(0).getModalite();
    
            double totalCO2 = 0.0;
            double totalTemps = 0.0;
            double totalPrix = 0.0;
    
            for (Trancon troncon : aretes) {
                ModaliteTransport currentModalite = troncon.getModalite();
                if (!currentModalite.equals(previousModalite)) {
                    sb.append(" -> ").append(((MonLieu) troncon.getDepart()).getNom())
                      .append(" [Changement de modalité: ").append(currentModalite).append("] ");
                    previousModalite = currentModalite;
                }
    
                double coutCO2 = ((MonTroncon) troncon).getCout().get(TypeCout.CO2);
                double coutTemps = ((MonTroncon) troncon).getCout().get(TypeCout.TEMPS);
                double coutPrix = ((MonTroncon) troncon).getCout().get(TypeCout.PRIX);
    
                totalCO2 += coutCO2;
                totalTemps += coutTemps;
                totalPrix += coutPrix;
    
                sb.append(" -> ").append(((MonLieu) troncon.getArrivee()).getNom())
                  .append(", Modalite: ").append(currentModalite)
                  .append(", Cout: {CO2=").append(coutCO2)
                  .append(", TEMPS=").append(coutTemps)
                  .append(", PRIX=").append(coutPrix)
                  .append("} | ");
            }
            sb.append("Total: {CO2=").append(totalCO2)
              .append(", TEMPS=").append(totalTemps)
              .append(", PRIX=").append(totalPrix)
              .append("}");
        }
        return sb.toString();
    }

    /**Affiche une liste de chemins avec les informations détaillées et les changements de modalité.
     * @param chemins
     * @return
     */
    public String afficherListeCheminsDetaille(List<Chemin> chemins) {
        if (chemins == null || chemins.isEmpty()) {
            return "La liste de chemins est vide.";
        }
    
        Set<String> cheminStrings = new HashSet<>();
    
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Chemin chemin : chemins) {
            String cheminString = chemin.toString();
            if (!cheminStrings.contains(cheminString)) {
                sb.append(afficherCheminDetaille(chemin, index)).append("\n");
                cheminStrings.add(cheminString);
                index++;
            }
        }
    
        return sb.toString();
    }

    public String afficherListeCheminsDetailleCSV(List<Chemin> chemins) {
        if (chemins == null || chemins.isEmpty()) {
            return "La liste de chemins est vide.";
        }
    
        Set<String> cheminStrings = new HashSet<>();
    
        StringBuilder sb = new StringBuilder();
        int index = 1;
        for (Chemin chemin : chemins) {
            String cheminString = chemin.toString();
            if (!cheminStrings.contains(cheminString)) {
                sb.append(afficherCheminDetaille(chemin, index)).append(";");
                cheminStrings.add(cheminString);
                index++;
            }
        }
    
        return sb.toString();
    }
    
    public String toString(){
        return "Lieux: " + this.lieux + " Troncons: " + this.troncons;
    }
}