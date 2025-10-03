import java.util.*;
public class Serviamo {
	private List<Cuoco> cuochi = new ArrayList<>();
	
	public void add(Cuoco c) {
		System.out.println("Aggiungo un cuoco alla lista...");
		this.cuochi.add(c);
	}
	
	public void remove(Cuoco c) {
		this.cuochi.remove(c);
	}
	
	public void aggiungiOrdine(Vector<String> nomeP, Vector<Integer> qntP, Tavolo tavolo, Ordine ordine) {
		for(Cuoco cuoco : this.cuochi) {
			cuoco.updateTODO(tavolo, ordine);
		}
		
	}
}
