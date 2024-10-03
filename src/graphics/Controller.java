package graphics;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import MyException.DataInvalidException;
import MyException.MyInvalidChoiceException;
import MyException.NoPathFoundException;
import fr.ulille.but.sae_s2_2024.ModaliteTransport;
import graph.MonLieu;
import graph.TypeCout;
import graph.Voyageur;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.Platforme;

public class Controller {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private Voyageur v = new Voyageur();
    private Platforme p = new Platforme();

    @FXML
    Label labelName;
    @FXML
    ComboBox<CheckBox> boxM;
    @FXML
    ComboBox<CheckBox> boxC;
    @FXML
    Label labelDuree;
    @FXML
    Label labelCO2;
    @FXML
    Label labelPrix;
    @FXML
    ComboBox<String> boxD;
    @FXML
    ComboBox<String> boxA;
    @FXML
    Label labelItineraire;
    @FXML
    VBox vbButton;

    public static double convertStringToDouble(String str) throws NumberFormatException {
        double result = Double.parseDouble(str);
        return result;
    }

    /**Permet de se deplacer sur la page principale
     * @param event
     * @throws IOException
     */
    
    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("res/IHM/1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**Permet de se deplacer sur la page de selection des critères et de d'initialiser les ComboBox
     * @param event
     * @throws IOException
     */
    
    public void switchToScene2(ActionEvent event) throws IOException {
        CheckBox train = new CheckBox("Train");
        CheckBox bus = new CheckBox("Bus");
        CheckBox avion = new CheckBox("Avion");
        boxM.getItems().addAll(train, bus, avion);

        CheckBox temps = new CheckBox("Temps");
        CheckBox co2 = new CheckBox("CO2");
        CheckBox prix = new CheckBox("Prix");
        boxC.getItems().addAll(temps, co2, prix);

        co2.setStyle("-fx-border-color: #27ab34; -fx-border-width: 2");

        Parent root = FXMLLoader.load(getClass().getResource("res/IHM/2.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**Permet de se deplacer sur la page de selection des villes et de recupere les information precedement rentrer
     * @param event
     * @throws IOException
     */
    
    public void switchToScene3(ActionEvent event) throws IOException {
        ArrayList<ModaliteTransport> arrModa = new ArrayList<>();

        v.setNom(labelName.getText());

        for (CheckBox cb : boxM.getItems()) {
            if (cb.isSelected()) {
                arrModa.add(ModaliteTransport.valueOf(cb.getText().toUpperCase()));
            }
        }

        int cpt = 0;
        for (CheckBox cbc : boxC.getItems()) {
            if (cpt < 2) {
                if (cbc.isSelected()) {
                    if (cpt == 0) {
                        v.setType1(TypeCout.valueOf(cbc.getText().toUpperCase()));
                        try{
                            if (cbc.getText().equals("Temps")) {
                                v.setValueType1(Double.parseDouble(labelDuree.getText()));
                            }else if(cbc.getText().equals("CO2")){
                                v.setValueType1(Double.parseDouble(labelCO2.getText()));
                            }else if (cbc.getText().equals("Prix")) {
                                v.setValueType1(Double.parseDouble(labelPrix.getText()));
                            }
                        }catch (NumberFormatException e) {
                            System.out.println("La chaîne fournie n'est pas un double valide.");
                        }

                    } else if(cpt == 1) {
                        v.setType2(TypeCout.valueOf(cbc.getText().toUpperCase()));

                        try{
                            if (cbc.getText().equals("Temps")) {
                                v.setValueType2(Double.parseDouble(labelDuree.getText()));
                            }else if(cbc.getText().equals("CO2")){
                                v.setValueType2(Double.parseDouble(labelCO2.getText()));
                            }else if (cbc.getText().equals("Prix")) {
                                v.setValueType2(Double.parseDouble(labelPrix.getText()));
                            }
                        }catch (NumberFormatException e) {
                            System.out.println("La chaîne fournie n'est pas un double valide.");
                        }
                    }

                    cpt++;
                }
            }
        }

        p.init(v.getMd());

        for (MonLieu l : p.getLieux()) {
            boxD.getItems().add(l.getNom());
        }

        for (MonLieu l : p.getLieux()) {
            boxA.getItems().add(l.getNom());
        }


        Parent root = FXMLLoader.load(getClass().getResource("res/IHM/3.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**Permet de se deplacer sur la page final ou il est afficher les itinéraire
     * @param event
     * @throws IOException
     * @throws NoPathFoundException
     * @throws DataInvalidException
     */
    
    public void switchToScene4(ActionEvent event) throws IOException, NoPathFoundException, DataInvalidException {

        class MonLieuListener implements ListChangeListener<String> {
            public void onChanged(Change<? extends String> report) {
                if (v.getDepart() != null) {
                    v.setDepart(p.findLieu(report.getList().get(0)));
                }else{
                    v.setArrive(p.findLieu(report.getList().get(0)));
                }
            }
        }

        boxD.getItems().addListener(new MonLieuListener());
        boxA.getItems().addListener(new MonLieuListener());

        p.init2(v, v.getDepart(), v.getArrive());

        labelItineraire.setText(p.afficherCheminFiltre(p.getKpccHashMap().get(v.getTypeFirst()).get(0)));

        Platforme.csvW(v, p, p.critere(p.getKpccHashMap().get(v.getTypeFirst()), v));

        Parent root = FXMLLoader.load(getClass().getResource("res/IHM/4.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**Permet de se deplacer sur la page de l'historique
     * @param event
     * @throws IOException
     */
    
    public void switchToScene5(ActionEvent event) throws IOException {
        ArrayList<String[]> arr = new ArrayList<>();
        
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("res/Historique/" + v.getNom()))) {
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

        ArrayList<Button> arrButton = new ArrayList<>();
        if (!arr.isEmpty()) {
            for(int i = 1; i < arr.size(); ++i){
                arrButton.add(new Button("Itinairaire " + i));
            }
        }

        for (Button button : arrButton) {
            vbButton.getChildren().add(button);
        }

        Parent root = FXMLLoader.load(getClass().getResource("res/IHM/5.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}