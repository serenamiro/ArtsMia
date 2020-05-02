package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	public Graph<ArtObject, DefaultWeightedEdge> grafo;
	public Map<Integer, ArtObject> idMap;
	
	public Model() {
		idMap = new HashMap<Integer, ArtObject>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		// CREAZIONE DEI VERTICI
		Graphs.addAllVertices(this.grafo, idMap.values());
		
		// CREAZIONE DEGLI ARCHI -- approccio 1
		// doppio ciclo for sui vertici: dati due vertici, controllo se sono collegati
		// non giunge al termine perchè ci sono troppi vertici
		/*
		for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				// devo collegare a1 con a2? se si, li collego con il relativo peso
				// controllo se non esiste già l'arco visto che il grafo non è orientato
				int peso = dao.getPeso(a1, a2);
				if(peso>0) {
					if(!this.grafo.containsEdge(a1, a2)) 
						Graphs.addEdge(this.grafo, a1, a2, peso);
				}
			}
		}
		*/
		
		// CREAZIONE DEGLI ARCHI -- approccio 2
		// mi faccio dare dal db tutte le adiacenze
		for(Adiacenza a : dao.getAdiacenze()) {
			if(a.getPeso() > 0) {
				Graphs.addEdge(this.grafo, idMap.get(a.getObj1()), idMap.get(a.getObj2()), a.getPeso());
			}
		}
		
		System.out.format("Grafo creato con %d vertici e %d nodi", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
