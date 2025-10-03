import java.util.ArrayList;


public class Ordine {
	private State stato;
	private static Menu menu = new Menu();
public static Integer numeroOrdineTotale = 1;
	private Integer numTavolo;
	private Integer numeroOrdine;
	private ArrayList<Pietanze> piatti = new ArrayList<Pietanze>();
	public Ordine() {
		this.stato=new NoState();
	}
	public Ordine(Integer numT) {
		numeroOrdine=numeroOrdineTotale;
		this.stato=new NoState();
		this.numTavolo=numT;
	}
	
	public void setState(State state) {
		this.stato=state;
	}
	
	public State getState() {
		return this.stato;
	}
	
	public Integer getNumOrd() {
		return numeroOrdine;
	}
	public void setNumOrd() {
		this.numeroOrdineTotale++;
	}
	public Integer getNumTavolo() {
		return numTavolo;
	}
	public void aggiungiPietanza(String nomeP, Integer qnt) {
		for(Pietanze element : menu.getAllFood()) {
			if(element.getNome().equals(nomeP)&&qnt!=0) {//se la quantita' e' 0 non lo puoi aggiungere
				piatti.add(element.makeCopy(qnt));
			}
		}
	}
	public ArrayList<Pietanze> getPietanze(){
		return piatti;
	}
	
	public void checkPietanza(Pietanze pietanza, Integer newQnt) {
		Integer tmp;
		Boolean trovato = false;
		for(int i=0; i<this.piatti.size();i++) {
			if(pietanza.getNome().equals(piatti.get(i).getNome())) {
				tmp = piatti.get(i).getQnt();
				piatti.remove(i);
				pietanza.addQnt(tmp);
				piatti.add(pietanza);
				trovato = true;
			}
				
		}
		if(!trovato) {
			pietanza.setQnt(newQnt);
			piatti.add(pietanza);
		}
		
	}
	public void showP() {
		System.out.println("NUmero tavolo "+this.numTavolo);
		for(Pietanze el : piatti) {
			System.out.println(el.getNome()+" dentro a show\n");
		}
	}
}