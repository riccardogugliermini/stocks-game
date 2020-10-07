package it.itisplanck.kazoo.view.config;

import it.itisplanck.kazoo.model.mercato.Andamento;
import it.itisplanck.kazoo.model.mercato.Azione;
import it.itisplanck.kazoo.model.mercato.Curva;
import it.itisplanck.kazoo.model.mercato.Societa;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Classe per la scelta nel Server delle impostazioni riguardanti le società
 * @author Luca Polese
 * @version 1.0
 */
public class CompaniesChoice {
	private BorderPane borderOnce;
	private GridPane secondaryPane,thirdPane;
	private HBox hb;
	private final ToggleGroup group;
	
	private Text companies,companyName,companyValue,companyMinValue,companyMaxValue,companyAmount;
	
	private TextField tfCompanyName;
	private NumberSpinner tfCompanyValue,tfCompanyMinValue,tfCompanyMaxValue,tfCompanyAmount;
	
	private Button play, add, remove;
	private RadioButton btnDefault,btnPersonalized;
	
	private DisplayPane table;
	private VBox vb;
	
	/**
	 * Metodo Costruttore per la parte della GUI_Server <br>
	 * Qui si creano le {@link Societa} con relative informazioni 
	 * @param mainStage Stage Principale
	 */
	public CompaniesChoice(Stage mainStage){
		borderOnce = new BorderPane();
		secondaryPane = new GridPane();
		thirdPane = new GridPane();
		vb = new VBox();
		vb.setSpacing(10);
		play = new Button("Avvia server");
		add = new Button("Aggiungi");
		remove = new Button("Rimuovi");
		
		companies = new Text("Società :");
		companies.setStyle("-fx-font-weight:bold;");
		btnDefault = new RadioButton("Predefinite");
		btnPersonalized = new RadioButton("Personalizzate");
		group = new ToggleGroup();
		btnDefault.setSelected(true);
		btnDefault.setToggleGroup(group);
		btnPersonalized.setToggleGroup(group);
		
		companyName = new Text("Nome");
		companyValue = new Text("Valore Iniziale");
		companyMinValue = new Text("Valore Minimo");
		companyMaxValue = new Text("Valore Massimo");
		companyAmount = new Text("Quantità ");
		
		tfCompanyName = new TextField();
		tfCompanyValue = new NumberSpinner();
		tfCompanyMinValue = new NumberSpinner();
		tfCompanyMaxValue = new NumberSpinner();
		tfCompanyAmount = new NumberSpinner();
		
		table = new DisplayPane();
		
		secondaryPane.add(companies, 0, 0);
		secondaryPane.add(btnDefault, 1, 0);
		secondaryPane.add(btnPersonalized, 2, 0);
		secondaryPane.setVgap(25.0);
		secondaryPane.setHgap(25.0);
		secondaryPane.setAlignment(Pos.CENTER);
		
		thirdPane.setHgap(30);
		
		thirdPane.add(companyName,0,0);
		thirdPane.add(tfCompanyName,0,1);
		thirdPane.add(companyMinValue,0,2);
		thirdPane.add(tfCompanyMinValue,0,3);
		thirdPane.add(companyMaxValue,1,2);
		thirdPane.add(tfCompanyMaxValue,1,3);
		thirdPane.add(companyAmount,1,0);
		thirdPane.add(tfCompanyAmount,1,1);
		thirdPane.add(companyValue,0,4);
		thirdPane.add(tfCompanyValue,0,5);
		BorderPane bp = new BorderPane();
		bp.setLeft(add);
		bp.setRight(remove);
		thirdPane.add(bp,1,5);
		thirdPane.setAlignment(Pos.CENTER);
		VBox.setMargin(thirdPane, new Insets(10, 0, 0, 0));
		
		vb.getChildren().addAll(thirdPane, table.getTable());
		
		borderOnce.setTop(secondaryPane);
		btnPersonalized.selectedProperty().addListener((o, oldValue, newValue) -> {
		    if (newValue) {
		        borderOnce.setCenter(vb);
		        mainStage.setHeight(630);
		        mainStage.setWidth(550);
		    } else {
		        borderOnce.setCenter(null);
		        mainStage.setHeight(280);
		    }
		});
		HBox playBox = new HBox();
		playBox.getChildren().add(play);
		playBox.setAlignment(Pos.CENTER);
		HBox.setMargin(play, new Insets(10, 0, 0, 0));
		borderOnce.setBottom(playBox);
	    
	    hb = new HBox();
	    hb.setAlignment(Pos.CENTER);
	    hb.getChildren().add(borderOnce);
	}
	
	/**
	 * Controllo se il bottone "Società Predefinite" è selezionato
	 * @return boolean
	 */
	public boolean isDefaultSelected() {
		return btnDefault.isSelected();
	}
	
	/**
	 * Metodo per settare i {@link TextField} ed i {@link Number Spinner} al loro valore iniziale
	 */
	public void clearFields() {
		tfCompanyName.clear();
		tfCompanyValue.setNumber(0);
		tfCompanyMinValue.setNumber(0);
		tfCompanyMaxValue.setNumber(0);
		tfCompanyAmount.setNumber(0);
	}
	
	/**
	 * Metodo Getter del Pannello {@link HBox}
	 * @return hb {@link HBox}
	 */
	public HBox getPane() {
		return this.hb;
	}
	
	/**
	 * Metod Getter della {@link TableView} di {@link Societa}
	 * @return table Tabella
	 */
	public TableView<Societa> getTable() {
		return table.getTable();
	}
	
	/**
	 * Metodo Getter del {@link Button} di gioco
	 * @return play Bottone per avviare il gioco
	 */
	public Button getButton() {
		return play;
	}
	
	/**
	 * Metodo Getter del {@link Button} di Aggiunta di <br>
	 * una {@link Societa} in {@link TableView}
	 * @return add Bottone per l'aggiunta della {@link Societa}
	 */
	public Button getAdd() {
		return add;
	}
	
	/**
	 * Metodo Getter del {@link Button} di Rimozione di <br>
	 * una {@link Societa} in {@link TableView}
	 * @return add Bottone per la rimozione della {@link Societa}
	 */
	public Button getRemove() {
		return remove;
	}
	
	/**
	 * Metodo Getter del nome della {@link Societa}
	 * @return tfCompanyName Nome della {@link Societa}
	 */
	public String getCompanyName() {
		return tfCompanyName.getText();
	}

	/**
	 * Metodo Getter del valore di {@link Azione} di una {@link Societa}
	 * @return tfCompanyName Nome della {@link Societa}
	 */
	public int getCompanyValue(){
		if(tfCompanyValue.getNumber()<getCompanyMaxValue()&&tfCompanyValue.getNumber()>getCompanyMinValue()) return tfCompanyValue.getNumber();
		else return (getCompanyMaxValue()+getCompanyMinValue())/2;
	}
	
	/**
	 * Metodo Getter del valore minimo {@link Azione} di una {@link Societa}
	 * @return tfCompanyMinValue Valore minimo
	 */
	public int getCompanyMinValue(){
		return tfCompanyMinValue.getNumber();
	}
	
	/**
	 * Metodo Getter del valore minimo {@link Azione} di una {@link Societa}
	 * @return tfCompanyMaxValue Valore massimo
	 */
	public int getCompanyMaxValue(){
		return tfCompanyMaxValue.getNumber();
	}
	
	/**
	 * Metodo Getter della quantità di{@link Azione} di una {@link Societa}
	 * @return tfCompanyAmount Quantità di {@link Azione}
	 */
	public int getCompanyAmount(){
		return tfCompanyAmount.getNumber();
	}
	
	/**
	 * Metodo Getter che ritorna la nuova {@link Societa} creata
	 * @return societa nuova Società
	 */
	public Societa getNuovaSocieta() {
		if(getCompanyName().trim().equals("")) return null;
		if(getCompanyMaxValue()<getCompanyMinValue()){
			return new Societa(new Azione(getCompanyName(),getCompanyValue(), getCompanyAmount()),
					new Andamento(new Curva(getCompanyMaxValue(), getCompanyMinValue()), Math.random()));
		}
		else return new Societa(new Azione(getCompanyName(),getCompanyValue(), getCompanyAmount()),
				new Andamento(new Curva(getCompanyMinValue(), getCompanyMaxValue()), Math.random()));
	}
}
