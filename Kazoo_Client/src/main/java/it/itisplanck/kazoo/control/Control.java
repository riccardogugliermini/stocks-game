package it.itisplanck.kazoo.control;

import java.text.DecimalFormat;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.giocatori.Giocatore;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import it.itisplanck.kazoo.view.NumberSpinner;
import it.itisplanck.kazoo.view.game.GameStage;
import it.itisplanck.kazoo.view.game.GraficoAndamento;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;

public class Control implements EventHandler<ActionEvent> {
	
	private GameStage stage;
	private Giocatore player;
	private Button buyButton;
	private Button sellButton;
	private Button darkButton;
	private Button lightButton;
	private TableView<Societa> mercato;
	private TableView<Societa> mieAzioni;
	private NumberSpinner qtaBuySpinner;
	private NumberSpinner qtaSellSpinner;
	private GraficoAndamento grafico;
	
	private DecimalFormat formatter;
	
	public Control(GameStage stage) {
		formatter = new DecimalFormat("#.##");
		this.stage = stage;
		this.player = stage.getGiocatore();
		this.sellButton = stage.getBottomBar().getSell();
		this.buyButton = stage.getTopBar().getBuy();
		this.mercato = stage.getTopBar().getMercato();
		this.mieAzioni = stage.getBottomBar().getActions();
		this.qtaBuySpinner = stage.getTopBar().getQta();
		this.qtaSellSpinner=stage.getBottomBar().getQta();
		this.grafico = stage.getTopBar().getGrafico();
		this.darkButton = stage.getTopBar().getDarkMode();
		this.lightButton = stage.getTopBar().getLightMode();
	}
	
	@Override
	public void handle(ActionEvent event) {
		// TODO Auto-generated method stub
		if (event.getSource() == buyButton ) {
			int qta = stage.getTopBar().getQta().getNumber();
			Societa selected = mercato.getSelectionModel().getSelectedItem();
			if(selected == null) return;
			
			if (qta > selected.getAzione().getQuantita()) {
				new Alert(AlertType.WARNING, "Quantità da comprare non valida.").showAndWait();
				return;
			} else if(Main.getGiocatore().getSaldo() < (selected.getAzione().getQuotazione() * qta)) {
				new Alert(AlertType.WARNING, "Non hai abbastanza soldi per comprare.").showAndWait();
				return;
			}
			
			player.buy(selected, qta);
		}
		
		if (event.getSource() == sellButton ) {
			int qta = stage.getBottomBar().getQta().getNumber();
			Societa selected = mieAzioni.getSelectionModel().getSelectedItem();
			if(selected == null) return;
			
			if (qta > selected.getAzione().getQuantita()) {
				new Alert(AlertType.WARNING, "Quantità da vendere non valida.").showAndWait();
				return;
			}
			player.sell(selected, qta);	
		}
		
		if (event.getSource() == qtaSellSpinner.getIncrementButton() || event.getSource() == qtaSellSpinner.getDecrementButton()) {
			if (event.getSource() == qtaSellSpinner.getIncrementButton()) 
				qtaSellSpinner.increment();
			if (event.getSource() == qtaSellSpinner.getDecrementButton())
				qtaSellSpinner.decrement();
			//int i = mercato.getSelectionModel().getSelectedIndex();
		}
		
		
		if (event.getSource() == qtaBuySpinner.getIncrementButton() || event.getSource() == qtaBuySpinner.getDecrementButton()) {
			if (event.getSource() == qtaBuySpinner.getIncrementButton()) 
				qtaBuySpinner.increment();
			if (event.getSource() == qtaBuySpinner.getDecrementButton())
				qtaBuySpinner.decrement();
			int i = mercato.getSelectionModel().getSelectedIndex();
			int quantita = qtaBuySpinner.getNumber();
			//int i = mercato.getSelectionModel().getSelectedIndex();
			if(quantita <= Mercato.getSocieta().get(i).getAzione().getQuantita() && quantita > 0)
				stage.getTopBar().getTotale().setText(formatter.format(Mercato.getSocieta().get(i).getCapitalizzazione()).replaceAll(",", ".") + " €");
		}
		if (event.getSource() == lightButton) {
			stage.getScene().getStylesheets().clear();
			stage.getScene().getStylesheets().add("it/itisplanck/kazoo/view/game/style-light.css");
			//stage.show();
		}
		
		if (event.getSource() == darkButton) {
			stage.getScene().getStylesheets().clear();
			stage.getScene().getStylesheets().add("it/itisplanck/kazoo/view/game/style.css");
			//tage.show();
		}
	}
	
	public void addControl() {
		buyButton.setOnAction(this);
		sellButton.setOnAction(this);
		darkButton.setOnAction(this);
		lightButton.setOnAction(this);
		qtaBuySpinner.getIncrementButton().setOnAction(this);
		qtaBuySpinner.getDecrementButton().setOnAction(this);
		qtaSellSpinner.getIncrementButton().setOnAction(this);
		qtaSellSpinner.getDecrementButton().setOnAction(this);
		
		//qtaBuySpinner.getIncrementButton().action
		mercato.setOnMouseClicked(new EventHandler<MouseEvent>() {
			 public void handle(MouseEvent event) {
				 
				 Societa soc = mercato.getSelectionModel().getSelectedItem();
				  
				 stage.getTopBar().getQta().setAccessibleText("" + soc.getAzione().getQuantita());
				 stage.getTopBar().getQta().setNumber(soc.getAzione().getQuantita());
				
				 double quantita = qtaBuySpinner.getNumber().doubleValue();
				 stage.getTopBar().getTotale().setText(Double.toString(soc.getAzione().getQuotazione() * quantita));
				 //stage.getTopBar().getCapitalizzazione().setText(Double.toString(soc.getCapitalizzazione()));;
				 stage.getTopBar().getCapitalizzazione().setText(new DecimalFormat("#.##").format(Mercato.getSocieta().get(0).getCapitalizzazione()).replaceAll(",", ".") + " €");
				 //stage.getTopBar().getTotale().setText(Double.toString(soc.getCapitalizzazione()));
				 stage.getBottomBar().update();
				 grafico.arrayToSeries(soc.getAzione().getNome());
			 }
		});
		mieAzioni.setOnMouseClicked(new EventHandler<MouseEvent>() {
			 public void handle(MouseEvent event) {
				 Societa soc = mieAzioni.getSelectionModel().getSelectedItem();
				 
				 stage.getBottomBar().getQta().setAccessibleText("" +soc.getAzione().getQuantita());
				 stage.getBottomBar().getQta().setNumber(soc.getAzione().getQuantita());
				 
				 stage.getBottomBar().update();
			 }
		});
	}
	
}
