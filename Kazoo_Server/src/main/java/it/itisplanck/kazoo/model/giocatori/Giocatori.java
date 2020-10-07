package it.itisplanck.kazoo.model.giocatori;

import java.util.Vector;

import it.itisplanck.kazoo.Main;
import javafx.application.Platform;
/**
 * 
 * @author Nicola Bovolato
 * Implementa un insieme di giocatori
 */
public class Giocatori {
	
	private static Vector<Giocatore> giocatori;
	private static int giocatoriMassimi, saldoIniziale;

	/**
	 * Metodo per inizializzare le variabili principali
	 * @param giocatoriMassimi I giocatori massimi che potranno partecipare alla partita
	 * @param saldoIniziale Il saldo iniziale che avra' ogni utente al join della partita
	 */
	public static void init(int giocatoriMassimi, int saldoIniziale) {
		giocatori = new Vector<Giocatore>();
		Giocatori.giocatoriMassimi = giocatoriMassimi;
		Giocatori.saldoIniziale = saldoIniziale;
	}
	
	/**
	 * Aggiunge un nuovo giocaotre
	 * @param nuovoGiocatore nome del nuovo giocatore
	 * @return ritorna 2 se si e' raggiungo il limite di giocatori, 1 se esiste gia' un giocatore con lo stesso nome, altrimenti 0
	 */
	public static int addGiocatore(String nuovoGiocatore) {
		Giocatore G = new Giocatore(nuovoGiocatore);
		G.setSaldo(saldoIniziale);
		if(giocatori.size() >= giocatoriMassimi) return 2;
		else if(getGiocatore(nuovoGiocatore) != null) return 1;
		giocatori.add(G);
		Platform.runLater(() -> Main.getGameStage().setPlayers(giocatori.size(), giocatoriMassimi));
		/*
		 if(searchForGiocatore(G.getNome())>=0) {
			for(int i=0;i<giocatori.size();i++) G.addAvversario(new GiocatoreAvversario(giocatori.get(i).getNome(),giocatori.get(i).getSaldo()));
			for(int i=0;i<giocatori.size();i++) giocatori.get(i).addAvversario(new GiocatoreAvversario(G.getNome(),G.getSaldo()));
			giocatori.add(G);
		}*/
		return 0;
	}
	
	/**
	 * Rimuove un giocatore
	 * @param playerName il nome del gioatore da eliminare
	 */
	public static void removeGiocatore(String playerName) {
		Giocatore g = null;
		for(Giocatore g0 : giocatori)
			if(g0.getNome().equals(playerName)) g = g0;
		if(g != null) {
			giocatori.remove(g);
			Main.getGameStage().setPlayers(giocatori.size(), giocatoriMassimi);
		}
	}
	
	public static int getSaldoIniziale() {
		return saldoIniziale;
	}
	
	public static int getGiocatoriMassimi() {
		return giocatoriMassimi;
	}
	
	public static Vector<Giocatore> getGiocatori() {
		return giocatori;
	}
	
	public static Giocatore getGiocatore(String nome) {
		for(Giocatore p : giocatori)
			if(p.getNome().equals(nome)) return p;
		return null;
	}
	/**
	 * Cerca un giocatore nel Vector in base al suo nome
	 * @param nome Il nome del giocatore da cercare
	 * @return se il giocatore e' stato trovato
	 */
	public static int searchForGiocatore(String nome) {
		for(int i=0;i<giocatori.size();i++) {
			if(giocatori.get(i).getNome().equals(nome)) return i;
		}
		return -1;
	}
	/*
	public void updateGiocatori() {
		for(int i=0;i<giocatori.size();i++) {
			for(int j=0;j<giocatori.size();j++) {
				int pos=giocatori.get(i).searchForAvversario(giocatori.get(j).getNome());
				if(pos>=0) giocatori.get(i).getAvversari().get(pos).setSaldo(giocatori.get(j).getSaldo());
			}
		}
	}*/
	/**
	 * Elabora e ritorna i giocatori avversari di un giocatore
	 * @param G il giocatore interessato
	 * @return Il vettore di giocatori avversari di G
	 */
	public static Vector<GiocatoreAvversario> getAvversariOf(Giocatore G){
		Vector<GiocatoreAvversario> avversari=new Vector<GiocatoreAvversario>();
		int pos=searchForGiocatore(G.getNome());
		for(int i=0;i<giocatori.size();i++) {
			if(i!=pos) avversari.add(new GiocatoreAvversario(giocatori.get(i).getNome(),giocatori.get(i).getSaldo()));
		}
		return avversari;
	}
	/**
	 * Aggiorna lo stato nel server di un giocatore con un' operazioni di compravendita effettuata nel client
	 * @param G il giocatore da aggiornare
	 */
	/*public static void updateGiocatore(Giocatore G) {
		int pos=searchForGiocatore(G.getNome());
		if(pos>=0&&G.isInGioco()){
			for(int i=0;i<G.getSocietaAcquistate().size();i++) {
				int posAzioniClientinServer=giocatori.get(pos).searchForSocietaAcquistate(G.getSocietaAcquistate().get(i).getAzione().getNome());
				if(posAzioniClientinServer<0) giocatori.get(pos).buy(G.getSocietaAcquistate().get(i).getAzione().getNome(), G.getSocietaAcquistate().get(i).getAzione().getQuantita());
				else if(G.getSocietaAcquistate().get(i).getAzione().getQuantita()>giocatori.get(pos).getSocietaAcquistate().get(posAzioniClientinServer).getAzione().getQuantita()) {
					giocatori.get(pos).buy(G.getSocietaAcquistate().get(i).getAzione().getNome(), G.getSocietaAcquistate().get(i).getAzione().getQuantita()-giocatori.get(pos).getSocietaAcquistate().get(posAzioniClientinServer).getAzione().getQuantita());
				}
				else if(G.getSocietaAcquistate().get(i).getAzione().getQuantita()<giocatori.get(pos).getSocietaAcquistate().get(posAzioniClientinServer).getAzione().getQuantita()) {
					giocatori.get(pos).buy(G.getSocietaAcquistate().get(i).getAzione().getNome(),giocatori.get(pos).getSocietaAcquistate().get(posAzioniClientinServer).getAzione().getQuantita()- G.getSocietaAcquistate().get(i).getAzione().getQuantita());
				}
			}
		}
	}*/
	
}
