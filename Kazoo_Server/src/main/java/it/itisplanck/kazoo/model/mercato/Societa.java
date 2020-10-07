package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;
/**
 * 
 * @author Nicola Bovolato
 * Implementazione di una Societa Azionaria
 * 
 */
public class Societa implements Serializable {
	
	private static final long serialVersionUID = -1487633451272032979L;
	
	private Azione azione;
	private Andamento andamento;
	private double capitalizzazione;
	
	public Societa(Azione azione, Andamento andamento){
		this.azione = azione;
		this.andamento = andamento;
		//azione.setQuotazione(andamento.getAndamentoCurva().getVariazione());
		capitalizzazione=azione.getQuotazione() * azione.getQuantita();
	}
	
	public double getCapitalizzazione() {
		return capitalizzazione;
	}

	public Azione getAzione() {
		return azione;
	}

	public Andamento getAndamento() {
		return andamento;
	}
	
	/**
	 * Simula l'avanzamento del tempo e quindi modifica il valore della societa
	 */
	public void update() {
		andamento.randomize();
		updateCapitalizzazione();
	}
	

	/**
	 * Aggiorna il valore della societa
	 */
	public void updateCapitalizzazione() {
		azione.setQuotazione(andamento.getAndamentoCurva().getVariazione());
		capitalizzazione=azione.getQuotazione() * azione.getQuantita();
	}
	
	/**
	 * Simula la vendita di un' azione
	 * @param toSell Quantita azioni da vendere
	 * @return se l'operazione e riuscita
	 */
	public boolean sell(int toSell) {
		if(toSell > 0 && toSell <= azione.getQuantita()) {
			azione.setQuantita(azione.getQuantita() - toSell);
			updateCapitalizzazione();
			return true;
		}
		else return false;
	}
	
	/**
	 * Simula l'acquisto di un' azione
	 * @param toBuy Quantita di azioni da acquistare
	 * @return se l'operazione e riuscita
	 */
	public boolean buy(int toBuy) {
		if(toBuy>0) {
			azione.setQuantita(azione.getQuantita()+toBuy);
			updateCapitalizzazione();
			return true;
		}
		return false;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Societa)) return false;
		Societa soc = (Societa)obj;
		return soc.getAzione().getNome().equals(azione.getNome());
	}
}
