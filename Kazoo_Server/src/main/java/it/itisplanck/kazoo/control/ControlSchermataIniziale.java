package it.itisplanck.kazoo.control;

import java.net.SocketException;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.giocatori.Giocatori;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.view.config.CompaniesChoice;
import it.itisplanck.kazoo.view.config.MainStage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;

/**
 * Classe ControlSchermataIniziale per il controllo degli eventi collegati alla classe {@link MainStage}
 * @author Luca Polese
 * @version 1.0
 */
public class ControlSchermataIniziale implements EventHandler<ActionEvent> {
	
	private MainStage mainstage;
	private Button sent;
	private ObservableList<Societa> tableData = FXCollections.observableArrayList();
	
	/**
	 * Metodo Costruttore della classe {@link ControlSchermataIniziale}
	 * @param stage Stage
	 */
	public ControlSchermataIniziale (MainStage stage) {
		this.mainstage = stage;
		this.sent = stage.getButton();
	}
	
	/**
	 * Azione da compiere quando uno specifico evento, del tipo in cui questo evento è registrato, è invocato<br>
	 * Vengono fatti i controlli sui dati inseriti nella schermata principale del server 
	 * @param event Evento da gestire
	 */
	@Override
	public void handle(ActionEvent event) {
		if(event.getSource() == sent) {
			if(!portaValida()) {
				new Alert(AlertType.WARNING, "Porta inserita non valida").showAndWait();
				return;
			} else if(mainstage.getMaxPlayers() < 2) {
				new Alert(AlertType.WARNING, "Il numero minimo di giocatori è 2!").showAndWait();
				return;
			} else if(mainstage.getDefaultPrice() < 1) {
				new Alert(AlertType.WARNING, "Il saldo può essere minimo 1!").showAndWait();
				return;
			}
			
			// In baso al bottone radio cliccato, crea il mercato con le societa
			if(mainstage.getDisplayPane().isDefaultSelected()) Main.creaMercatoDefault();
			else Main.creaMercatoPersonalizzato();
			
			if(Mercato.getSocieta().isEmpty()) {
				new Alert(AlertType.WARNING, "Non hai inserito alcuna societa.").showAndWait();
				return;
			}
			
			try {
				Main.createChannel(mainstage.getPorta());
			} catch(SocketException e) {
				new Alert(AlertType.WARNING, "Porta inserita già in uso").showAndWait();
				return;
			}
			
			// Imposta due variabili essenziali nel gioco
			Giocatori.init(mainstage.getMaxPlayers(), mainstage.getDefaultPrice());
			
			Main.createGameStage();
			
		}
	}
	
	/**
	 * Viene aggiunto l'ascoltatore di bottoni
	 */
	public void addControl() {
		sent.setOnAction(this);
	}
	
	/**
	 * Metodo per l'aggiunta di una nuova {@link Societa}
	 */
	public void add() {
		
		Societa nuova = mainstage.getDisplayPane().getNuovaSocieta();
		if(nuova == null) {
			new Alert(AlertType.WARNING, "Nome della società  non specificato.").showAndWait();
			return;
		}
		for(Societa s : Main.getSocieta()) {
			if(s.getAzione().getNome().equalsIgnoreCase(nuova.getAzione().getNome())) {
				new Alert(AlertType.WARNING, "Nome della società  già  presente.").showAndWait();
				return;
			}
		}
		
		Main.setSocieta(nuova);
		tableData.add(nuova);
		
		CompaniesChoice displayPane = mainstage.getDisplayPane();
		displayPane.clearFields();
		displayPane.getTable().getItems().setAll(tableData);
		displayPane.getTable().refresh();
		
	}
	
	/**
	 * Metodo per la rimozione di una  {@link Societa}
	 */
	public void remove() {
		
		Societa soc = mainstage.getDisplayPane().getTable().getSelectionModel().getSelectedItem();
		mainstage.getDisplayPane().getTable().getItems().remove(soc);
		Main.getSocieta().remove(soc);
		tableData.remove(soc);
		mainstage.getDisplayPane().getTable().refresh();
		
	}

	/**
	 * Controllo della validità della porta inserita
	 * @return boolean
	 */
	private boolean portaValida() {
		int porta = mainstage.getPorta();
		return !(porta < 1024 || porta > 65535);
	}

}
