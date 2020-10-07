package it.itisplanck.kazoo.view.game;

import java.text.DecimalFormat;
import java.util.Vector;

import it.itisplanck.kazoo.model.mercato.Societa;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.SortType;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
/**
 * Tabella contenete degli oggetti di tipo Societa, estende TableView.
 * @author Riccardo Gugliermini
 *
 */
public class ActionsTable extends TableView<Societa> {
	
	private TableColumn<Societa, String> colonnaNome;
	private TableColumn<Societa, Integer> colonnaQuantita;
	private TableColumn<Societa, Double> colonnaCosto;
	
	private DecimalFormat formatter;
	
	@SuppressWarnings("unchecked")
	/**
	 * Metodo costruttuore, costruisce una tabella con all'interno gli elementi dell'oggetto di tipo Vector, passato al metodo.
	 * @param societa Vector, contenente oggetti di classe Societa, questi vengono visualizzati nella tabella.
	 */
	public ActionsTable(Vector<Societa> societa) {
		formatter = new DecimalFormat("#.##");
		
		this.setPrefHeight(300);
		this.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		colonnaNome = new TableColumn<Societa, String>("Nome");
		colonnaQuantita = new TableColumn<Societa, Integer>("Quantità");
		colonnaCosto = new TableColumn<Societa, Double> ("Valore per azione (€)");
		
		colonnaNome.setCellValueFactory(
				p -> new SimpleStringProperty(p.getValue().getAzione().getNome())
		);
		
		colonnaQuantita.setCellValueFactory(
				p -> new SimpleIntegerProperty(p.getValue().getAzione().getQuantita()).asObject()
		);
		//colonnaQuantita.setSortable(false);
		
		colonnaCosto.setCellValueFactory(
				p -> new SimpleDoubleProperty(p.getValue().getAzione().getQuotazione()).asObject()
		);
		colonnaCosto.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
			
			@Override
			public String toString(Double object) {
				return formatter.format(object.doubleValue()).replaceAll(",", ".");
			}

			@Override
			public Double fromString(String string) { return null; } // Non server perchè la tabella non è modificabile
			
		}));
		colonnaCosto.setSortType(SortType.DESCENDING);
		getColumns().addAll(colonnaNome, colonnaQuantita, colonnaCosto);
		//getSortOrder().add(colonnaCosto);
		getSortOrder().add(colonnaNome);
		
		update(societa);
	}
	
	/**
	 * Metodo che, chiamato dal server, effettua l'aggiornameto della tabella.
	 * @param societa Vector, contenente le nuove società che devono essere visualizzate.
	 */
	public void update(Vector<Societa> societa) {
		ObservableList<Societa> data = FXCollections.observableArrayList();
		data.addAll(societa);
		setItems(data);
		getSortOrder().clear();
		colonnaCosto.setSortType(SortType.DESCENDING);
		//getSortOrder().add(colonnaCosto);
		getSortOrder().add(colonnaNome);
		
		refresh();
	}
}