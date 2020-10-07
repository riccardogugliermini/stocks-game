package it.itisplanck.kazoo.view.game;

import java.util.Vector;

import it.itisplanck.kazoo.model.mercato.Mercato;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Series;

/**
 * @author Affori Jacopo
 * Classe che rappresenta il grafico nella schermata di gioco del giocatore
 */
public class GraficoAndamento {
	
	private final NumberAxis xAxis = new NumberAxis("Tempo (Secondi)", 0, 60, 10);
	private final NumberAxis yAxis = new NumberAxis("Valore società (€)", 0, 1000, 10);
	private final LineChart<Number, Number> lineChart;
	private XYChart.Series<Number, Number> series;
	private int previousTime=0;
	private String previousSocieta=null;
	
	/**
	 * Metodo costruttore
	 * @param stage La finestra in cui creare il grafico
	 */
	public GraficoAndamento(GameStage stage) {
		xAxis.setMinorTickCount(2);
		yAxis.setMinorTickCount(0);
		lineChart = new LineChart<>(xAxis, yAxis);
		lineChart.setCreateSymbols(false);
		series = new XYChart.Series<>();
		series.setName("Valore Azioni");
		lineChart.getData().add(series);
		//lineChart.resize(Control.USE_COMPUTED_SIZE, Control.USE_COMPUTED_SIZE);
		//lineChart.setHei
		xAxis.setAutoRanging(false);
		yAxis.setAutoRanging(false);
	}
	
	/**
	 * Permette di visualizzare l'andamento di un'azienda
	 * @param nomeSocieta Il nome dell'azienda da visualizzare
	 */
	public void arrayToSeries(String nomeSocieta) {
		Vector<Integer> points=Mercato.getAndamento(nomeSocieta);
		if(points == null || points.isEmpty()) return;
		
		int nPoints = points.size();
		
		yAxis.setLowerBound(Mercato.getSocieta().get(Mercato.searchForSocieta(nomeSocieta)).getAndamento().getAndamentoCurva().getMIN_VARIAZIONE());
		yAxis.setUpperBound(Mercato.getSocieta().get(Mercato.searchForSocieta(nomeSocieta)).getAndamento().getAndamentoCurva().getMAX_VARIAZIONE());
		
		if(previousSocieta!=null&&!previousSocieta.equals(nomeSocieta)) {
			series.getData().clear();
			int j=0;
			if(previousTime>=60) j=previousTime-60;
			for(int i = 0; i < nPoints; i++) {
				series.getData().add(new XYChart.Data<>(j, points.get(i)));
				j++;
			}
		}else{
			if(previousTime>=60) {
				xAxis.setLowerBound(previousTime-60);
				xAxis.setUpperBound(previousTime);
			}
			series.getData().add(new XYChart.Data<>(previousTime, points.get(nPoints-1)));
		}
		
		previousSocieta=nomeSocieta;
		previousTime++;
		
		//int j;
		/*if(nPoints == 240) {
			j = previousTime + 1;
			previousTime += 1;
			xAxis.setLowerBound(j);
			xAxis.setUpperBound(j + 240);
			xAxis.setMinorTickCount(1);
		} else j = 0;*/
		//series.getData().clear();
		
		/*for(int i = 0; i < nPoints; i++) {
			series.getData().add(new XYChart.Data<>(j, points.get(i)));
			j += 1;
		}*/
		
	}
	
	/**
	 * Ritorna il grafico
	 */
	public LineChart<Number, Number> getLineChart () {
		return lineChart;
	}
	
	/**
	 * Ritorna i componenti del grafico
	 */
	public XYChart.Series<Number, Number> getSeries() {
		return series;
	}
	
	/**
	 * Imposta i componenti del grafico
	 * @param series I componenti {@link Series} da impostare
	 */
	public void setSeries(XYChart.Series<Number, Number> series) {
		this.series = series;
	}
	
}
