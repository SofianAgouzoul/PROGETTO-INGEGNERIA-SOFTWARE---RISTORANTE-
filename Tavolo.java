import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

public class Tavolo{
	    private Integer numTavolo;
	    private boolean chiusuraTavolo = false;
	    private ArrayList<Ordine> ordini  = new ArrayList<Ordine>();
	    private Ordine statusOrdine;
	    private Double tot;
	    private Integer statusCounter=0;
	    
        public Tavolo(){
            this.statusOrdine=new Ordine();
            this.tot=0.0;
            }
	        
	    public Double getTot() {
	    	return tot;
	    }
	    
	    public Boolean hasOrders() {
	    	if(ordini.isEmpty()) {
	    		return false;
	    	}
	    	else {
	    		return true;
	    	}
	    }
	    
	    public boolean getChiusura(){
	        return this.chiusuraTavolo;
	    }
	    
	    public void setChiusura(){
	        this.chiusuraTavolo=true;
	        this.resocontoOrdini();
	    }
	    
	    public void setNonChiuso(){
	        this.chiusuraTavolo=false;
	    }
	    
	    public int getNumTav(){
	        return this.numTavolo;
	    }
	    
	    public void setStatusOrdine(State stato) {
	    	
	    	if(stato instanceof OrdineConsegnato) {
	    		this.statusCounter++;
	    		System.out.println("Status counter: "+statusCounter+"\nordini.size: "+ordini.size());
	    		
	    		if(statusCounter==ordini.size())
	    			this.statusOrdine.setState(stato);
	    	} else if (stato instanceof OrdineRicevuto) {
	    		
	    		this.statusOrdine.setState(stato);
	    		}
	    	
	    }
	    public State getStatusOrdine() {
	    	return this.statusOrdine.getState();
	    }
	    
	    public void setNumTav(int numT) {
	    	this.numTavolo=numT;
	    }
	    
	    public Ordine getOrdineFinale() {
	    	return this.statusOrdine;
	    }
	    
	    public void addOrdine(Ordine newOrder) {
	    	this.ordini.add(newOrder);
	    	this.statusOrdine.setNumOrd();
	    }
	    
	    public Ordine lastOrder() {
	    	return ordini.get(ordini.size()-1);
	    }
	    
	    public ArrayList<Ordine> getOrdine() {
	    	return ordini;
	    }
	    
	    public void resocontoOrdini() {
	    	for(Ordine ord : ordini) { 
	    		
	    		for(Pietanze pietanzaInOrdine : ord.getPietanze()) {
	    			
	    			statusOrdine.checkPietanza(pietanzaInOrdine, pietanzaInOrdine.getQnt());
	    			
	    		}
	    		
	    	}
	    	
	    	this.contoTotale();
	    	
	    }
	    
	    private void contoTotale() {
	    	Menu menu = new Menu();
	    	int i,j;
	    	double sumtot=0;
	    	
	 
	    	for(i=0;i<menu.getPizze().size();i++) {
	    		for(j=0;j<this.statusOrdine.getPietanze().size();j++) {
	    			if(menu.getPizze().get(i).getNome()==this.statusOrdine.getPietanze().get(j).getNome()) {
	    				sumtot=sumtot+(menu.getPizze().get(i).getPrezzo()*this.statusOrdine.getPietanze().get(j).getQnt());
	    			}
	    		}
	    	}
	    	for(i=0;i<menu.getBibite().size();i++) {
	    		for(j=0;j<this.statusOrdine.getPietanze().size();j++) {
	    			if(menu.getBibite().get(i).getNome()==this.statusOrdine.getPietanze().get(j).getNome()) {
	    				sumtot=sumtot+(menu.getBibite().get(i).getPrezzo()*this.statusOrdine.getPietanze().get(j).getQnt());
	    			}
	    		}
	    	}
	    	for(i=0;i<menu.getPrimiPiatti().size();i++) {
	    		for(j=0;j<this.statusOrdine.getPietanze().size();j++) {
	    			if(menu.getPrimiPiatti().get(i).getNome()==this.statusOrdine.getPietanze().get(j).getNome()) {
	    				sumtot=sumtot+(menu.getPrimiPiatti().get(i).getPrezzo()*this.statusOrdine.getPietanze().get(j).getQnt());
	    			}
	    		}
	    	}
	    	this.tot=sumtot;
	    }
	    
	    	
}