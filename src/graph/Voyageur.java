package graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import MyException.MyInvalidChoiceException;
import MyException.MyInvalidNameException;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;

public class Voyageur {
    private String nom;
    private List<ModaliteTransport> md;

    private TypeCout type1;
    private TypeCout type2;

    private double valueType1;
    private double valueType2;

    private MonLieu arrive;
    private MonLieu depart;

    private final Scanner sc = new Scanner(System.in);

    public Voyageur(String nom, List<ModaliteTransport> md, TypeCout type1, double valueType1, TypeCout type2, double valueType2, MonLieu arrve, MonLieu depart) {
        this.nom = nom;
        if(md == null){
            this.md = new ArrayList<ModaliteTransport>();
        }else{
            this.md = md;
        }
        this.type1 = type1;
        this.valueType1 = valueType1;
        this.type2 = type2;
        this.valueType2 = valueType2;
        this.arrive = arrve;
        this.depart = depart;
    }

    public Voyageur(String nom, List<ModaliteTransport> md, TypeCout t1, double vt1, TypeCout t2, double vt2) {
        this(nom, md, t1, vt1, t2, vt2, null, null);
    }

    public Voyageur(TypeCout type1, double valueType1, TypeCout type2, double valueType2, MonLieu arrve, MonLieu depart) {
        this(null, null, type1, valueType1, type2, valueType2, arrve, depart);
    }  
    
    public Voyageur(TypeCout type1, double valueType1, TypeCout type2, double valueType2) {
        this(null, null, type1, valueType1, type2, valueType2, null, null);
    }

    public Voyageur() {
        this(null, null, null, 0, null, 0, null, null);
    }
//--------------------------Getteurs-----------------------------------


    public String getNom() {
        return nom;
    }

    public MonLieu getArrive() {
        return arrive;
    }

    public MonLieu getDepart() {
        return depart;
    }

    public List<ModaliteTransport> getMd() {
        return md;
    }
    
    public TypeCout getTypeFirst(){
        return type1;
    }

    public TypeCout getTypeSecond(){
        return type2;
    }

    public double getValueTypeFirst(){
        return valueType1;
    }

    public double getValueTypeSecond(){
        return valueType2;
    }

//--------------------------Setteurs-----------------------------------

    public void setNomMan() throws MyInvalidNameException {
        System.out.print("Quel est votre nom ? ");
        String name = sc.nextLine();
        if (name == null) {
            throw new MyInvalidNameException("Le nom ne peut pas être null !");
        }
        for (int i = 0; i < name.length(); i++) {
            if(name.charAt(i) >= '1' && name.charAt(i) <= '9') {
                throw new MyInvalidNameException("Le nom ne doit contenir que des lettres !");                
            }   
        }

        if (name.length() > 16 || name.length() < 3) {
            throw new MyInvalidNameException("Le nom doit avoir une taille supérieur à 3 et inférieur à 16 !");
        }
        this.nom = name;
    }

    public void setMdMan() throws MyInvalidChoiceException {
        System.out.print("Combien de moyen de transport souhaitez-vous (De 1 à 3 parmis Train | Bus | Avion) ? ");
        int nbChoix = sc.nextInt();
        int choix;

        if (nbChoix > 3 || nbChoix < 1) {
            throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
        }

        System.out.println("Vous devez donc choisir " + nbChoix + " Choix.");

        for (int i = 0; i < nbChoix; i++){
            System.out.println("Veuillez choisir une modalité de transport parmis celle-ci :");
            System.out.println("1. Train");
            System.out.println("2. Bus");
            System.out.println("3. Avion");

            System.out.println();
            System.out.print("Entrez votre choix : ");
            choix = sc.nextInt();

            if (choix > 3 || choix < 1) {
                throw new MyInvalidChoiceException("Le choix est incorrecte !");
            }

            if (choix == 1) {
                this.md.add(ModaliteTransport.TRAIN);
            }

            if (choix == 2) {
                this.md.add(ModaliteTransport.BUS);
            }

            if (choix == 3) {
                this.md.add(ModaliteTransport.AVION);
            }
        }
    }

    public void setTypeMan() throws MyInvalidChoiceException{
        System.out.println("Combien de critère souhaitez-vous (1 à 2 critères) ?");
        int nbCritere = sc.nextInt();

        if (nbCritere > 2 || nbCritere < 1) {
            throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
        }

        System.out.println("Entrez le critère principale (1.Temps | 2.CO2 | 3.Prix)");
        int c1 = sc.nextInt();

        if (c1 > 3 || c1 < 1) {
            throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
        }

        this.type1 = TypeCout.values()[c1-1];
        if (this.type1.equals(TypeCout.PRIX)) {
            System.out.println("Combien d'euros ne doit pas éxceder votre voyage ?");
        }else if (this.type1.equals(TypeCout.CO2)) {
            System.out.println("Combien de kg ne doit pas éxceder votre voyage ?");
        }else if (this.type1.equals(TypeCout.TEMPS)) {
            System.out.println("Combien de temps (en minutes) ne doit pas éxceder votre voyage ?");            
        }

        int vt1 = sc.nextInt();

        if (vt1 < 0) {
            throw new MyInvalidChoiceException("La valeur ne peut pas être négatif !");
        }

        this.valueType1 = vt1;
        

        if (nbCritere == 2) {
            int c2;
            if (c1 == 2) {
                System.out.println("Choisissez votre critere secondaire (1.TEMPS,3.PRIX).");
                c2 = sc.nextInt();
                if (c2 != 1 && c2 != 3) {
                    throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
                }
                try {
                    this.type2 = TypeCout.values()[0];
                } catch (IllegalArgumentException e) {
                    System.out.println("Critère invalide. Veuillez choisir parmi (1.TEMPS,3.PRIX).");
                }

            }else if(c1 == 3){
                System.out.println("Choisissez votre critere secondaire (1.TEMPS,2.CO2)");
                c2 = sc.nextInt();
                if (c2 != 1 && c2 != 2) {
                    throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
                }
                try {
                    this.type2 = TypeCout.values()[2];
                } catch (IllegalArgumentException e) {
                    System.out.println("Critère invalide. Veuillez choisir parmi (1.TEMPS,2.CO2).");
                }

            }else{
                System.out.println("Choisissez votre critere secondaire (2.CO2, 3.PRIX)");
                c2 = sc.nextInt();
                if (c2 != 2 && c2 != 3) {
                    throw new MyInvalidChoiceException("Le nombre de choix est incorrecte !");
                }
                try {
                    this.type2 = TypeCout.values()[1];
                } catch (IllegalArgumentException e) {
                    System.out.println("Critère invalide. Veuillez choisir parmi (2.CO2, 3.PRIX).");
                }
            }

            this.type2 = TypeCout.values()[c2-1];
            if (this.type2.equals(TypeCout.PRIX)) {
                System.out.println("Combien d'euros ne doit pas éxceder votre voyage ?");
            }else if (this.type2.equals(TypeCout.CO2)) {
                System.out.println("Combien de kg ne doit pas éxceder votre voyage ?");
            }else if (this.type2.equals(TypeCout.TEMPS)) {
                System.out.println("Combien de temps (en minutes) ne doit pas éxceder votre voyage ?");            
            }
    
            int vt2 = sc.nextInt();
    
            if (vt2 < 0) {
                throw new MyInvalidChoiceException("La valeur ne peut pas être négatif !");
            }
    
            this.valueType2 = vt2;

        }


    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setMd(List<ModaliteTransport> md) {
        this.md = md;
    }

    public void setType1(TypeCout type1) {
        this.type1 = type1;
    }

    public void setType2(TypeCout type2) {
        this.type2 = type2;
    }

    public void setValueType1(double valueType1) {
        this.valueType1 = valueType1;
    }

    public void setValueType2(double valueType2) {
        this.valueType2 = valueType2;
    }

    public void setArrive(MonLieu arrve) {
        this.arrive = arrve;
    }

    public void setDepart(MonLieu depart) {
        this.depart = depart;
    }

//--------------------------Autres-----------------------------------

     @Override
    public String toString() {
        return "Voyageur [nom=" + nom + ", type1=" + type1 + ", type2=" + type2 + ", arrve=" + arrive + ", depart=" + depart + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Voyageur other = (Voyageur) obj;
        if (nom == null) {
            if (other.nom != null)
                return false;
        } else if (!nom.equals(other.nom))
            return false;
        return true;
    }

    public void init() throws MyInvalidNameException, MyInvalidChoiceException{
        this.setNomMan();
        this.setMdMan();
        this.setTypeMan();
    }
}
