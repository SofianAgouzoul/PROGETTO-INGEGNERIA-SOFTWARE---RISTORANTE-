import java.io.*;
import java.util.*;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.showMessageDialog;

// Assumiamo che Ordine, State, OrdineConsegnato, OrdineRicevuto e Pietanze siano classi definite.
public class Tavolo {
    
    // Uso 'final' per i campi che non cambiano riferimento e il 'diamond operator' per chiarezza.
    private final ArrayList<Ordine> ordini = new ArrayList<>();
    
    // statusOrdine è l'oggetto Ordine utilizzato per contenere lo stato complessivo (State) e il riepilogo finale.
    private final Ordine statusOrdine; 
    
    private Integer numTavolo;
    private boolean chiusuraTavolo = false;
    private Double tot;
    private int statusCounter = 0; // Usiamo 'int' per i contatori

    public Tavolo() {
        // Inizializza l'oggetto Ordine che funge da status holder
        this.statusOrdine = new Ordine(); 
        this.tot = 0.0;
    }
    
    // === Gestione Ordini ===

    public void addOrdine(Ordine newOrder) {
        this.ordini.add(newOrder);
        
        // ❌ RIMOZIONE DEL BUG: Non è più necessario chiamare setNumOrd().
        // La gestione dell'ID ordine è ora nel costruttore di Ordine.
        // this.statusOrdine.setNumOrd(); 
    }
    
    public void setStatusOrdine(State stato) {
        
        // Logica per determinare se tutti gli ordini sono stati evasi
        if (stato instanceof OrdineConsegnato) {
            this.statusCounter++;
            System.out.println("Status counter: " + statusCounter + "\nordini.size: " + ordini.size());
            
            // Aggiorna lo stato complessivo solo quando TUTTI gli ordini sono stati consegnati.
            if (this.statusCounter == this.ordini.size())
                this.statusOrdine.setState(stato);
                
        } else if (stato instanceof OrdineRicevuto) {
            // Aggiorna lo stato complessivo quando un nuovo ordine è ricevuto
            this.statusOrdine.setState(stato);
        }
    }

    // === Logica di Chiusura e Conto ===

    public void setChiusura() {
        this.chiusuraTavolo = true;
        this.resocontoOrdini(); // Calcola il conto finale prima di chiudere
    }
    
    public void resocontoOrdini() {
        // Pulisce il riepilogo prima di ricrearlo
        this.statusOrdine.getPietanze().clear();
        
        for (Ordine ord : ordini) { 
            for (Pietanze pietanzaInOrdine : ord.getPietanze()) {
                // Aggrega le quantità di pietanze identiche nell'oggetto statusOrdine
                this.statusOrdine.checkPietanza(pietanzaInOrdine, pietanzaInOrdine.getQnt());
            }
        }
        
        // Calcola il conto dopo l'aggregazione
        this.contoTotale();
    }

    private void contoTotale() {
        
        double sumtot = 0.0;
        
        // 🟢 CORREZIONE MAGGIORE: Eliminata la logica inefficiente e buggata (confronto stringhe ==)
        // Calcoliamo il totale scorrendo semplicemente la lista delle pietanze aggregate
        // nell'ordine finale (statusOrdine). Assumiamo che ogni Pietanza abbia il prezzo corretto.
        
        for (Pietanze p : this.statusOrdine.getPietanze()) {
             // Il totale è la somma di (Prezzo * Quantità) per ogni Pietanza
             sumtot += p.getPrezzo() * p.getQnt();
        }
        
        this.tot = sumtot;
    }
    
    // === Getters e Setters restanti ===
    
    public Double getTot() {
        return tot;
    }
    
    public boolean hasOrders() {
        return !ordini.isEmpty();
    }
    
    public boolean getChiusura() {
        return this.chiusuraTavolo;
    }
    
    public void setNonChiuso() {
        this.chiusuraTavolo = false;
    }
    
    public int getNumTav() {
        return this.numTavolo;
    }
    
    public void setNumTav(int numT) {
        this.numTavolo = numT;
    }

    public State getStatusOrdine() {
        return this.statusOrdine.getState();
    }
    
    public Ordine getOrdineFinale() {
        return this.statusOrdine; // L'oggetto Ordine usato come riepilogo/stato
    }
    
    public Ordine lastOrder() {
        if (ordini.isEmpty()) return null; 
        return ordini.get(ordini.size() - 1);
    }
    
    public ArrayList<Ordine> getOrdine() {
        return ordini;
    }
}