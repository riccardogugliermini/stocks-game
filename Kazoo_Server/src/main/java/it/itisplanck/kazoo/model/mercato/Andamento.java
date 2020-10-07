package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * @author Nicola Bovolato
 * L'andamento calcola la variazione della societa in modo casuale
 */
public class Andamento implements Serializable {
	
	private static final long serialVersionUID = 4274437627328623457L;
	
	private Curva andamentoCurva;
	private double randomizzatoreVariazione;
	private int toRepeat=0;
	private int repeating;
	private boolean ascending;
	
	/**
	 * Metodo costruttore con due parametri
	 * @param andamentoCurva Curva di andamento
	 * @param randomizzatoreVariazione Il randomizzatore da applicare alla curva (compreso tra 0 e 1)
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
		
		if(toRepeat==0||repeating==0) { 
			toRepeat=(int) ((int)1+Math.random()*5);
			repeating=toRepeat;
			if(Math.random()<0.5) ascending=true;
			else ascending=false;
			
			//Utils.write(toRepeat);
		}
		if(ascending) {
			double min=getAndamentoCurva().getVariazione();
			andamentoCurva.setVariazione(min+(getAndamentoCurva().getMAX_VARIAZIONE()-min)*Math.pow(Math.random(), randomizzatoreVariazione*10));
			//Utils.write("ascending");
		}
		else {
			double max=getAndamentoCurva().getVariazione();
			andamentoCurva.setVariazione(getAndamentoCurva().getMIN_VARIAZIONE()+(max-getAndamentoCurva().getMIN_VARIAZIONE())*Math.pow(Math.random(), randomizzatoreVariazione*0.5));
			//Utils.write("descending");
		}
		//Utils.write(andamentoCurva.getVariazione());
		repeating--;
		
		//andamentoCurva.respectLimits();
	}
	
	public Curva getAndamentoCurva() {
		return andamentoCurva;
	}
	
	public double getRandomizzatoreVariazione() {
		return randomizzatoreVariazione;
	}
	
}
