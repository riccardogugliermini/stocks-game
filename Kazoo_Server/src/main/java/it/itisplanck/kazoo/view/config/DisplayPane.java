package it.itisplanck.kazoo.view.config;

import java.text.DecimalFormat;

import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.model.mercato.Societa;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.StringConverter;
/**
 * Classe per il display della tabella delle società 
 * @author Luca Polese, Emanuele Onnivello
 * @version 1.0
 */
public class DisplayPane {
	
	private TableView<Societa> tBook;
	private TableColumn<Societa, String> tcName;
	private TableColumn<Societa, Integer> tcAmount;
	private TableColumn<Societa, Double> tcPrice;
	private TableColumn<Societa, Integer> tcMinPrice;
	private TableColumn<Societa, Integer> tcMaxPrice;
	
	/**
	 * Metodo Costruttore della Classe {@link DisplayPane}<br>
	 * Permette di creare la tabella con le Società 
	 */
	@SuppressWarnings("unchecked")
	public DisplayPane() {
		tBook=new TableView<Societa>();
		tBook.setEditable(true);
		tBook.setMaxHeight(200);
		tBook.setMinWidth(500);
		tBook.setMaxWidth(500);
		
		tcName=new TableColumn<Societa, String>("Nome");
		tcAmount=new TableColumn<Societa, Integer>("Quantità");
		tcPrice=new TableColumn<Societa, Double>("Val. Iniziale");
		tcMinPrice=new TableColumn<Societa, Integer>("Val. Min");
		tcMaxPrice=new TableColumn<Societa, Integer>("Val. Max");
		
		tBook.getColumns().addAll(tcName, tcAmount, tcPrice, tcMinPrice,tcMaxPrice);
		
		tcName.setCellValueFactory(
				p -> new SimpleStringProperty(p.getValue().getAzione().getNome())
		);
		tcAmount.setCellValueFactory(
				p -> new SimpleIntegerProperty(p.getValue().getAzione().getQuantita()).asObject()
		);
		tcPrice.setCellValueFactory(
				p -> new SimpleDoubleProperty(p.getValue().getAzione().getQuotazione()).asObject()
		);
		tcMinPrice.setCellValueFactory(
				p -> new SimpleIntegerProperty(p.getValue().getAndamento().getAndamentoCurva().getMIN_VARIAZIONE()).asObject()
		);
		tcMaxPrice.setCellValueFactory(
				p -> new SimpleIntegerProperty(p.getValue().getAndamento().getAndamentoCurva().getMAX_VARIAZIONE()).asObject()
		);
		
		
		// Nome societa
		tcName.setCellFactory(TextFieldTableCell.forTableColumn());
		tcName.setOnEditCommit(t -> {
			
			t.getTableView().refresh();
			for(Societa s : Main.getSocieta()) {
				if(s.getAzione().getNome().equalsIgnoreCase(t.getNewValue())) {
					new Alert(AlertType.WARNING, "Nome della societÃ  giÃ  presente.").showAndWait();
					return;
				}
			}
			
			((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow())).getAzione().setNome(t.getNewValue());
	        Main.getSocieta().get(t.getTablePosition().getRow()).getAzione().setNome(t.getNewValue());
		});
		
		// Edit della quantita
		tcAmount.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {

			private Integer save;
			
			@Override
			public String toString(Integer object) {
				save = object;
				return object + "";
			}

			@Override
			public Integer fromString(String string) {
				Integer i;
				try {
					i = Math.abs(Integer.valueOf(string));
				} catch(NumberFormatException e) {
					i = save;
				}
				return i;
			}
			
		}));
		tcAmount.setOnEditCommit(t -> {
		    ((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow())).getAzione().setQuantita(t.getNewValue());
		    Main.getSocieta().get(t.getTablePosition().getRow()).getAzione().setQuantita(t.getNewValue());
		    t.getTableView().refresh();
		});
		
		// Edit del valore
		tcPrice.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Double>() {
			
			private Double save;
			
			@Override
			public String toString(Double object) {
				save = object;
				return new DecimalFormat("#.##").format(object.doubleValue()).replaceAll(",", ".");
			}

			@Override
			public Double fromString(String string) {
				string = string.replaceAll(",", ".");
				Double d;
				try {
					d = Math.abs(Double.valueOf(string));
				} catch(NumberFormatException e) {
					d = save;
				}
				return d;
			}
			
		}));
		tcPrice.setOnEditCommit(t -> {
			t.getTableView().refresh();
			
			Societa societa = ((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow()));
			
			if(societa.getAndamento().getAndamentoCurva().getMIN_VARIAZIONE() < t.getNewValue() &&
					societa.getAndamento().getAndamentoCurva().getMAX_VARIAZIONE() > t.getNewValue()) {
			    societa.getAzione().setQuotazione(t.getNewValue());
			    Main.getSocieta().get(t.getTablePosition().getRow()).getAzione().setQuotazione(t.getNewValue());
			} else
				new Alert(AlertType.WARNING, "Valore iniziale della societÃ  non valido.").showAndWait();
			
		});
		
		// Valore minimo
		tcMinPrice.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
			
			private Integer save;
			
			@Override
			public String toString(Integer object) {
				save = object;
				return save+"";
			}

			@Override
			public Integer fromString(String string) {
				Integer i;
				try {
					i = Integer.valueOf(string);
				} catch(NumberFormatException e) {
					i = save;
				}
				return i;
			}
			
		}));
		tcMinPrice.setOnEditCommit(t -> {
			t.getTableView().refresh();
			
			Societa societa = ((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow()));
			
			if(societa.getAndamento().getAndamentoCurva().getMAX_VARIAZIONE() < t.getNewValue())
				new Alert(AlertType.WARNING, "Valore minimo non valido.").showAndWait();
			else {
				((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow())).getAndamento().getAndamentoCurva().setMIN_VARIAZIONE(t.getNewValue());
				Main.getSocieta().get(t.getTablePosition().getRow()).getAndamento().getAndamentoCurva().setMIN_VARIAZIONE(t.getNewValue());
				t.getTableView().refresh();
			}
		});
		
		// Valore massimo
		tcMaxPrice.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<Integer>() {
			
			private Integer save;
			
			@Override
			public String toString(Integer object) {
				save = object;
				return save+"";
			}

			@Override
			public Integer fromString(String string) {
				Integer i;
				try {
					i = Integer.valueOf(string);
				} catch(NumberFormatException e) {
					i = save;
				}
				return i;
			}
			
		}));
		tcMaxPrice.setOnEditCommit(t -> {
			t.getTableView().refresh();
			
			Societa societa = ((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow()));
			
			if(societa.getAndamento().getAndamentoCurva().getMIN_VARIAZIONE() > t.getNewValue())
				new Alert(AlertType.WARNING, "Valore massimo non valido.").showAndWait();
			else {
				((Societa)t.getTableView().getItems().get(t.getTablePosition().getRow())).getAndamento().getAndamentoCurva().setMAX_VARIAZIONE(t.getNewValue());
				Main.getSocieta().get(t.getTablePosition().getRow()).getAndamento().getAndamentoCurva().setMAX_VARIAZIONE(t.getNewValue());
				t.getTableView().refresh();
			}
			
		});
		/*
		tcName.prefWidthProperty().bind(tBook.widthProperty().multiply(0.33));
		tcAmount.prefWidthProperty().bind(tBook.widthProperty().multiply(0.33));
		tcPrice.prefWidthProperty().bind(tBook.widthProperty().multiply(0.33));
		*/
		tcName.prefWidthProperty().bind(tBook.widthProperty().multiply(0.20));
		tcAmount.prefWidthProperty().bind(tBook.widthProperty().multiply(0.20));
		tcPrice.prefWidthProperty().bind(tBook.widthProperty().multiply(0.20));
		tcMinPrice.prefWidthProperty().bind(tBook.widthProperty().multiply(0.20));
		tcMaxPrice.prefWidthProperty().bind(tBook.widthProperty().multiply(0.20));
		
	}
	
	/**
	 * Metodo Getter per il ritorno dell'intera tabella
	 * @return tBook tabella
	 */
	public TableView<Societa> getTable() {
		return tBook;
	}
	
	/**
	 * Metodo Getter per il ritorno della colonna dei Nomi Azioni
	 * @return tcName colonna dei nomi delle societÃ 
	 */
	public TableColumn<Societa, String> getTcName() {
		return tcName;
	}
	
	/**
	 * Metodo Getter per il ritorno della colonna delle Quantità  Azioni
	 * @return tcAmount colonna delle quantità di azioni delle società
	 */
	public TableColumn<Societa, Integer> getTcAmount() {
		return tcAmount;
	}
	
	/**
	 * Metodo Getter per il ritorno della colonna dei Prezzi Azioni
	 * @return tcAmount colonna delle quantità di azioni delle società
	 */
	public TableColumn<Societa, Double> getTcPrice() {
		return tcPrice;
	}
	
	/**
	 * Metodo Getter per il ritorno della colonna dei Prezzi Minimi
	 * @return tcMinPrice colonna dei prezzi minimi di azioni delle società 
	 */
	public TableColumn<Societa, Integer> getTcMinPrice() {
		return tcMinPrice;
	}
	
	/**
	 * Metodo Getter per il ritorno della colonna dei Prezzi Massimi
	 * @return tcMaxPrice colonna dei prezzi minimi di azioni delle società 
	 */
	public TableColumn<Societa, Integer> getTcMaxPrice() {
		return tcMaxPrice;
	}
	
}
