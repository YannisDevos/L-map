package graph;

import java.util.ArrayList;

import fr.ulille.but.sae_s2_2024.Lieu;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class MonLieu implements Lieu, Comparable<MonLieu>{
    private String nom;
    private ArrayList<String[]> correspondances;
    
    public MonLieu(String nom){
        this.nom = nom;
        this.correspondances = new ArrayList<>();
    }

    public String getNom() {
        return nom;
    }

    public ArrayList<String[]> getCorrespondances(){
        return this.correspondances;
    }

    public void setCorrespondances(ArrayList<String[]> c){
        this.correspondances = c;
    }

    public String[] getOneCorrespondances(ModaliteTransport m1){
        for (String[] tabS : this.correspondances) {
            if(ModaliteTransport.valueOf(tabS[0].toUpperCase()).equals(m1)){
                return new String[]{tabS[2], tabS[3], tabS[4]};
            }
        }
        return new String[]{"0", "0", "0"};
    }

    public String toString(){
        return this.nom;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        MonLieu other = (MonLieu) obj;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        return true;
    }

    @Override
    public int compareTo(MonLieu other) {
        return this.getNom().compareTo(other.getNom());
    }
}