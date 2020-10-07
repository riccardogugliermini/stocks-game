package it.itisplanck.kazoo.view.game;


import java.text.DecimalFormat;

import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.view.NumberSpinner;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

/**
 * Parte superiore della finestra. Visualizza tutte le informazioni sul actionsTable, ugali per tutti i giocatori.
 * @author Riccardo Gugliermini
 *
 */
public class TopBar extends VBox {
	
	private HBox top;
	private VBox commands;
	private HBox compraBox;
	private HBox infoBox;
	
	private ActionsTable actionsTable;
	private GraficoAndamento grafico;
	private NumberSpinner qta;
	private Button buy;
	private Label title;
	private Label capitalizzazione;
	private Label totale;
	private Button darkMode;
	private Button lightMode;
	
	private DecimalFormat formatter;
	
	/**
	 * Metodo costruttore. Produce un HBox contente gli elementi della parte superiore della finestra.
	 * @param stage finestra principale.
	 */
	public TopBar (GameStage stage) {
		formatter = new DecimalFormat("#.##");
		actionsTable = new ActionsTable(Mercato.getSocieta());
		
		qta = new NumberSpinner();
		
		
		buy = new Button ("Compra");
		buy.setAlignment(Pos.TOP_CENTER);
		
		capitalizzazione=new Label();
		capitalizzazione.setPrefWidth(80);
		capitalizzazione.setText(formatter.format(Mercato.getSocieta().get(0).getCapitalizzazione()).replaceAll(",", ".") + " €");
		
		infoBox=new HBox(new Label ("Capitalizzazione: "), capitalizzazione);
		
		totale = new Label();
		totale.setPrefWidth(60);
		totale.setText(new DecimalFormat("#.##").format(Mercato.getSocieta().get(0).getAzione().getQuotazione()).replaceAll(",", ".") + " €");
		
		compraBox = new HBox (new Label ("Totale:"), totale);
		compraBox.setSpacing(10);
		
		commands = new VBox (infoBox,new Label ("quantità da comprare:"), qta, compraBox, buy);
		commands.setSpacing(10);
		
		grafico = new GraficoAndamento (stage);
		grafico.getLineChart().setPadding(new Insets (0,0,10,30));
		
		title = new Label ("BORSA");
		title.setMaxWidth(Double.MAX_VALUE);
		title.setAlignment(Pos.CENTER);
		title.setFont(new Font(40));
		title.setPadding(new Insets (0,0,20,0));
		
		darkMode = new Button("Dark");
		lightMode = new Button("Light");
	
		this.getChildren().add(new BorderPane(title,null,new HBox(darkMode, lightMode),null,null));
		
		top = new HBox ();
		top.setMaxWidth(Double.MAX_VALUE);
		top.getChildren().add(actionsTable);
		top.getChildren().add(commands);
		top.getChildren().add(grafico.getLineChart());
		top.setAlignment(Pos.CENTER_RIGHT);
		top.setSpacing(20);
		
		HBox.setHgrow(actionsTable, Priority.ALWAYS);
		actionsTable.getSelectionModel().select(0);
		
		this.getChildren().add(top);
		
		this.setPadding(new Insets(5, 10, 10, 10));
		grafico.arrayToSeries(Mercato.getSocieta().get(0).getAzione().getNome());
	}
	
	
	
	public Button getLightMode() {
		return lightMode;
	}

	public ActionsTable getMercato () {
		return actionsTable;
	}
	public ActionsTable getActionsTable () {
		return actionsTable;
	}
	public GraficoAndamento getGrafico () {
		return grafico;
	}
	
	public NumberSpinner getQta() {
		return qta;
	}

	public Button getBuy() {
		return buy;
	}
	
	public Label getTotale () {
		return totale;
	}
	
	public Button getDarkMode () {
		return darkMode;
	}
	
	public Label getCapitalizzazione() {
		return capitalizzazione;
	}
	
	/**
	 * Metodo richiamato dal server per eseguire l'update delle informazioni.
	 */
	public void update () {
		Societa selection = actionsTable.getSelectionModel().getSelectedItem();
		actionsTable.update(Mercato.getSocieta());
		actionsTable.getSelectionModel().select(selection);
		if(selection != null) grafico.arrayToSeries(selection.getAzione().getNome());
		//TODO: MODIFICARE LA PRECISIONE DEI DOUBLE E AGGIUNGERE SIMBOLO EURO
		capitalizzazione.setText(formatter.format(selection.getCapitalizzazione()).replaceAll(",", ".") + " €");
		totale.setText(Double.toString(selection.getAzione().getQuotazione()*qta.getNumber()));
	}
	
}
