package it.itisplanck.kazoo.model.mercato;

import java.io.Serializable;

/**
 * Classe Società che contiene l'{@link Azione}, l'{@link Andamento} e la capitalizzazione
 * @author Luca Polese
 * @version 1.0
 */
public class Societa implements Serializable {
	
	private static final long serialVersionUID = -1487633451272032979L;
	
	private Azione azione;
	private Andamento andamento;
	private double capitalizzazione;
	
	/**
	 * Metodo Costruttore della {@link Societa}
	 * @param azione {@link Azione} da inserire
	 * @param andamento {@link Andamento} da inserire
	 */
	public Societa(Azione azione, Andamento andamento) {
		this.azione=azione;
		this.andamento=andamento;
		
		azione.setQuotazione(andamento.getAndamentoCurva().getVariazione());
		capitalizzazione=azione.getQuotazione()*azione.getQuantita();
	}
	
	/**
	 * Metodo Getter per la capitalizzazione
	 * @return capitalizzazione Capitalizzazione della {@link Societa}
	 */
	public double getCapitalizzazione() {
		return capitalizzazione;
	}

	/**
	 * Metodo Getter dell'{@link Azione}
	 * @return azione {@link Azione} da ritornare
	 */
	public Azione getAzione() {
		return azione;
	}

	/**
	 * Metodo Getter dell'{@link Andamento}
	 * @return andamento {@link Andamento} da ritornare
	 */
	public Andamento getAndamento() {
		return andamento;
	}
	
	/**
	 * Metodo pere l'update della Capitalizzazione
	 */
	public void updateCapitalizzazione() {
		azione.setQuotazione(andamento.getAndamentoCurva().getVariazione());
		capitalizzazione=azione.getQuotazione() * azione.getQuantita();
	}
	
	/**
	 * Metodo per la vendita di un {@link Azione}
	 * @param toSell Intero
	 * @return boolean
	 */
	public boolean sell(int toSell) {
		if(toSell > 0 && toSell<=azione.getQuantita()) {
			azione.setQuantita(azione.getQuantita()-toSell);
			updateCapitalizzazione();
			return true;
		}
		else return false;
	}
	
	/**
	 * Metodo per comprare un' {@link Azione}
	 * @param toBuy Intero
	 * @return boolean
	 */
	public boolean buy(int toBuy) {
		if(toBuy>0) {
			azione.setQuantita(azione.getQuantita()+toBuy);
			updateCapitalizzazione();
			return true;
		}
		return false;
	}
	
	/**
	 * Metodo per il confronto fra oggetti
	 */
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Societa)) return false;
		Societa soc = (Societa)obj;
		return soc.getAzione().getNome().equals(azione.getNome());
	}
	
}
