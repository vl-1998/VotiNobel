package it.polito.tdp.nobel.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.nobel.db.EsameDAO;

public class Model {
	private List <Esame> esami;
	private double bestMedia=0.0; //tiene traccia della media migliore incontrata, in partenza è nulla
	private Set <Esame> bestSoluzione = null; //gli esami che soddisfano
	
	public Model () {
		EsameDAO dao= new EsameDAO();
		this.esami= dao.getTuttiEsami();
	}

	
	public Set<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		//impostiamo la chiamata della ricorsione
		
		Set <Esame> parziale = new HashSet<>();
		cerca1 (parziale, 0, numeroCrediti);
		//cerca2 (parziale, 0, numeroCrediti); E' VERAMENTE TROPPO LENTA
		
		
		return bestSoluzione;
	}

	//funzione ricorsiva
	//COMPLESSITA 2^N
	private void cerca1 (Set <Esame> parziale, int L, int m) {
		
		//Casi terminali
		int crediti = sommaCrediti(parziale); //mi restituisce il numero di crediti totali di parziale
		if (crediti>m) {
			return;
		}
		
		if (crediti==m) { //potrebbe essere una soluzione interessante perche rispetta il vincolo dei crediti
			double media = calcolaMedia (parziale);
			if (media > bestMedia) {
				// devo sovrascrivere la soluzione migliore
				bestSoluzione = new HashSet <>(parziale);
				bestMedia = media; //tengo traccia della migliore soluzioen incontrata
			}
		}
		
		//sicuramente, crediti < m
		if (L== esami.size()) {
			return ; //non ci sono più esami da considerare
		}
		
		
		//generiamo i sottoproblemi, decidiamo se inserire esami [L] oppure no. Provo entrambe le cose
		
		//provo ad aggiungerlo
		parziale.add(esami.get(L)); //aggiungo a parziale l'L-esimo esame
		cerca1 (parziale, L+1, m);
		parziale.remove(esami.get(L));
		
		//provo a non aggiungerlo
		cerca1 (parziale, L+1, m); //perche ho fatto backtracking
		
	}
	
	
	
	
	
	//DEFINISCO UN CERCA 2
	//COMPLESSITA N! (per questo molto piu lento)
	//potrei migliorarlo evitando di considerare sia e1 e2 sia e2 e1
	private void cerca2 (Set <Esame> parziale, int L, int m) {
		
		//Casi terminali
		int crediti = sommaCrediti(parziale); //mi restituisce il numero di crediti totali di parziale
		if (crediti>m) {
			return;
		}
		
		if (crediti==m) { //potrebbe essere una soluzione interessante perche rispetta il vincolo dei crediti
			double media = calcolaMedia (parziale);
			if (media > bestMedia) {
				// devo sovrascrivere la soluzione migliore
				bestSoluzione = new HashSet <>(parziale);
				bestMedia = media; //tengo traccia della migliore soluzioen incontrata
			}
		}
		
		//sicuramente, crediti < m
		if (L== esami.size()) {
			return ; //non ci sono più esami da considerare
		}
		
		//generazione dei sottoproblemi, ad ogni livello aggiungiamo un corso, bisogna decidere quale
		for (Esame e: esami) { //a partire dal primo provo ad aggiungerlo alla soluzione parziale, poi lo tolgo e provo ad aggiungere il secondo
			//controllo che l'esame non sia gia contenuto in parziali
			if (!parziale.contains(e)) { //cerco di aggiunger alla mia soluzione parziale solo esami in posizione successiva rispetto a quella corrente
				parziale.add(e);
				//provo tutte le soluzioni che inizino con il prio esame in esami
				cerca2 (parziale, L+1, m);
				parziale.remove(e);
			}
		}
	}
	
	
	


	public double calcolaMedia(Set<Esame> parziale) {// media pesata. Lo definisco public in modo da poterlo richiamare nel controllore
														//e stampare la media
		int crediti = 0;
		int somma = 0;
		
		for (Esame e : parziale) {
			crediti += e.getCrediti();
			somma += (e.getVoto())*e.getCrediti();
		}
		return somma/crediti;
	}


	private int sommaCrediti(Set<Esame> parziale) {
		int somma=0;
		
		for (Esame e : parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}
}
