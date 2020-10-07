package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * @author Nicola Bovolato
 * L'implementazione di una curva di variazione di prezzo dell'azione
 */

public class Curva implements Serializable {
	
	private static final long serialVersionUID = 5052022663188576502L;
	
	private int MAX_VARIAZIONE, MIN_VARIAZIONE;
	
	private double variazione;

	/**
	 * Metodo costruttore con due parametri
	 * @param MIN_VARIAZIONE Il valore minimo che si deve generare
	 * @param MAX_VARIAZIONE Il valore massimo che si deve generare
	 */
	public Curva(int MIN_VARIAZIONE,int MAX_VARIAZIONE) {
		
		this.MIN_VARIAZIONE=MIN_VARIAZIONE;
		this.MAX_VARIAZIONE=MAX_VARIAZIONE;
		
		randomize();
	}
	
	/**
	 * Crea un randomizzatore di base per l'Azione
	 */
	
	private void randomize() {
		this.variazione = MIN_VARIAZIONE + (Math.random() * (MAX_VARIAZIONE - MIN_VARIAZIONE));
	}

	public double getVariazione() {
		return this.variazione;
	}
	
	public int getMAX_VARIAZIONE() {
		return this.MAX_VARIAZIONE;
	}

	public int getMIN_VARIAZIONE() {
		return this.MIN_VARIAZIONE;
	}

	public void setVariazione(double variazione) {
		this.variazione = variazione;
	}
	
	public void setMIN_VARIAZIONE(int min){
		MIN_VARIAZIONE=min;
	}
	
	public void setMAX_VARIAZIONE(int max){
		MAX_VARIAZIONE=max;
	}
	
	/*public void respectLimits() {
		if(variazione>MAX_VARIAZIONE) variazione=MAX_VARIAZIONE;
		else if(variazione<MIN_VARIAZIONE) variazione=MIN_VARIAZIONE;
	}*/
	
	
	
}
