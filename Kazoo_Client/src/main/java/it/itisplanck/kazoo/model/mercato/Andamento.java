package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * L'Andamento permette di calcolare la variazione dell'Societa in modo casuale
 * @author Luca Polese
 * @version 1.0
 */
public class Andamento implements Serializable {
	
	private static final long serialVersionUID = 4274437627328623457L;
	
	private Curva andamentoCurva;
	private double randomizzatoreVariazione;
	
	/**
	 * Metodo costruttore con due parametri
	 * @param andamentoCurva La Curva che servirà a determinare la variazione masima e minima
	 * @param randomizzatoreVariazione Il randomizzatore che andrà  applicato alla Curva (es. 0) 
	 */
	public Andamento(Curva andamentoCurva, double randomizzatoreVariazione) {
		this.andamentoCurva=andamentoCurva;
		if(randomizzatoreVariazione >= 0 && randomizzatoreVariazione <= 1) this.randomizzatoreVariazione = randomizzatoreVariazione;
		else this.randomizzatoreVariazione = 0.5;
	}
	
	/**
	 * Elabora il randomizzatore della Curva 
	 */
	public void randomize() {
		if(randomizzatoreVariazione > 0.5)
			andamentoCurva.setVariazione(
					andamentoCurva.getVariazione() * randomizzatoreVariazione
					+ andamentoCurva.getMIN_VARIAZIONE()
					+ Math.random() * (andamentoCurva.getMAX_VARIAZIONE() - andamentoCurva.getMIN_VARIAZIONE())
			);
		else if(randomizzatoreVariazione < 0.5)
			andamentoCurva.setVariazione(
					andamentoCurva.getVariazione()
					- andamentoCurva.getMIN_VARIAZIONE()
					+ Math.random() * (andamentoCurva.getMAX_VARIAZIONE() - andamentoCurva.getMIN_VARIAZIONE() * randomizzatoreVariazione)
			);
	}
	
	/**
	 * Metodo Getter dell'andamento della Curva
	 * @return andamentoCurva Andamento Curva
	 */
	public Curva getAndamentoCurva() {
		return andamentoCurva;
	}
	
}
