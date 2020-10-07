package it.itisplanck.kazoo.model.giocatori;

import java.io.Serializable;
import java.util.Vector;



import it.itisplanck.kazoo.Main;
import it.itisplanck.kazoo.Utils;
import it.itisplanck.kazoo.model.mercato.Azione;
import it.itisplanck.kazoo.model.mercato.Mercato;
import it.itisplanck.kazoo.model.mercato.Societa;
import javafx.application.Platform;
/**
 * 
 * @author Nicola Bovolato
 * Implementazione di un giocatore in borsa
 */
public class Giocatore implements Serializable {
	
	private static final long serialVersionUID = -2407386438868057161L;
	
	private String nome;
	private double saldo;
	private Vector<Societa> societaAcquistate;
	private boolean inGioco;
	
	public Giocatore(String nome) {
		this.nome = nome;
		societaAcquistate = new Vector<Societa>();
		inGioco = false;
	}
	
	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}
	
	public boolean isInGioco() {
		return inGioco;
	}
	
	public void addSocieta(Societa S) {
		societaAcquistate.add(S);
	}
	
	public Vector<Societa> getSocietaAcquistate() {
		return societaAcquistate;
	}
	
	public String getNome() {
		return nome;
	}
	
	public double getSaldo() {
		return saldo;
	}
	/**
	 * Cerca una societa acquistata dal giocatore
	 * @param nome Il nome della societa
	 * @return La posizione della societa nel rispettivo Vector (-1 se non trovata)
	 */
	public int searchForSocietaAcquistate(String nome) {
		for(int i = 0; i < societaAcquistate.size(); i++)
			if(societaAcquistate.get(i).getAzione().getNome().equals(nome))
				return i;
		return -1;
	}
	
	/**
	 * Simula l'acquisto di un numero di azioni di una societa dal mercato
	 * @param nomeS Nome della societa
	 * @param quantita Numero di azioni da acquistare
	 * @return se l'operazione e' stata eseguita con successo
	 */
	public synchronized boolean buy(String nomeS,int quantita) {
		try {
			Mercato.getMutex().acquire();
			
			Societa S = Mercato.getSocieta(nomeS);
			int posAcquistate = searchForSocietaAcquistate(nomeS);
			int posMercato = Mercato.searchForSocieta(nomeS);
			double capitalizzazioneIniziale = 0;
			if(posMercato >= 0) {
				if(Mercato.getSocieta().get(posMercato).getAzione().getQuantita() >= quantita) {
					S.sell(quantita);
					if(posAcquistate >= 0) {
						capitalizzazioneIniziale = societaAcquistate.get(posAcquistate).getCapitalizzazione();
						societaAcquistate.get(posAcquistate).buy(quantita);
					} else {
						Societa nuova = new Societa(new Azione(S.getAzione().getNome(),new Double(0),quantita),S.getAndamento());
						nuova.updateCapitalizzazione();
						societaAcquistate.add(nuova);
						posAcquistate = searchForSocietaAcquistate(nomeS);
					}
					saldo -= societaAcquistate.get(posAcquistate).getCapitalizzazione() - capitalizzazioneIniziale;
					Mercato.getMutex().release();
					Utils.write(societaAcquistate.get(0));
					return true;
				}
			}
			Mercato.getMutex().release();
		} catch (InterruptedException e) {}
		return false;
		//TODO: aggiorna View Saldo e Tabella Azioni Acquistate
	}
	/**
	 * Simula la vendita di un numero di azioni di una societa al mercato
	 * @param nomeS Nome della societa
	 * @param quantita Numero di azioni da vendere
	 * @return se l'operazione e' stata eseguita con successo
	 */
	//TODO: Aggiorna sta roba
	public synchronized boolean sell(String nomeS,int quantita) {
		try {
			Mercato.getMutex().acquire();
			
			int posVendute=searchForSocietaAcquistate(nomeS);
			int posMercato=Mercato.searchForSocieta(nomeS);
			if(posMercato >= 0 && posVendute >= 0) {
				if(societaAcquistate.get(posVendute).getAzione().getQuantita() >= quantita) {
					double capitalizzazioneIniziale=societaAcquistate.get(posVendute).getCapitalizzazione();
					if(societaAcquistate.get(posVendute).getAzione().getQuantita() == quantita) {
						societaAcquistate.get(posVendute).sell(quantita);
						saldo+=capitalizzazioneIniziale-societaAcquistate.get(posVendute).getCapitalizzazione();
						societaAcquistate.remove(posVendute);
					} else {
						societaAcquistate.get(posVendute).sell(quantita);
						saldo+=capitalizzazioneIniziale-societaAcquistate.get(posVendute).getCapitalizzazione();
					}
					checkIfInGioco();
					
					Mercato.getMutex().release();
					return true;
				}
			}
			Mercato.getMutex().release();			
		} catch (InterruptedException e) {}
		return false;
		
	}
	
	/**
	 * Aggiorna lo stato in gioco del giocatore in base al suo saldo
	 */
	public synchronized void checkIfInGioco() {
		if(saldo < 0 || societaAcquistate == null) {
			inGioco = false;
			Platform.runLater(()->{Main.kick();});
		}
	}
	
	public void updateSocietaAcquistate() {
		for(int i=0;i<societaAcquistate.size();i++) societaAcquistate.get(i).updateCapitalizzazione();
		}
	
	@Override
	public boolean equals(Object object) {
		if(object == null || !(object instanceof Giocatore)) return false;
		Giocatore gioc = (Giocatore)object;
		return nome.equals(gioc.getNome());
	}
	
}
