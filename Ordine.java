import java.util.ArrayList;

// Assumiamo che Menu, Pietanze, State, NoState siano definite in file separati
public class Ordine {
    
    // Lo stato è gestito tramite il pattern State
    private State stato; 
    
    // Rimuoviamo il campo Menu statico, rendendo il Menu un singleton gestito
    // o passato come dipendenza esterna se necessario. 
    // Per mantenere la logica originale, assumiamo che Menu sia accessibile globalmente.
    private static Menu menu = new Menu(); 

    // ✅ CORREZIONE CHIAVE: Uso dell'ID statico come contatore
    public static int numeroOrdineTotale = 1; 
    
    private final int numTavolo;
    private final int numeroOrdine;
    private final ArrayList<Pietanze> piatti = new ArrayList<>();

    // Costruttore vuoto, anche se non idealmente usato in un sistema reale.
    public Ordine() {
        this(0); // Chiama il costruttore principale con un valore di default
    }
    
    // ✅ CORREZIONE CHIAVE: Costruttore per la creazione di un nuovo ordine
    public Ordine(int numT) {
        // Assegna l'ID corrente e poi incrementa per il prossimo ordine.
        this.numeroOrdine = numeroOrdineTotale++; 
        this.stato = new NoState();
        this.numTavolo = numT;
    }
    
    public void setState(State state) {
        this.stato = state;
    }
    
    public State getState() {
        return this.stato;
    }
    
    public int getNumOrd() {
        return numeroOrdine;
    }
    
    public int getNumTavolo() {
        return numTavolo;
    }
    
    // Migliorato: usa .equals() e garantisce l'uso del metodo makeCopy()
    public void aggiungiPietanza(String nomeP, int qnt) {
        if (qnt <= 0) return; // Non aggiunge se la quantità è zero o negativa
        
        for(Pietanze element : menu.getAllFood()) {
            // ✅ CORREZIONE: Uso corretto di .equals()
            if(element.getNome().equals(nomeP)) { 
                // Assumiamo che makeCopy(qnt) ritorni una nuova Pietanza con la quantità specificata.
                piatti.add(element.makeCopy(qnt)); 
                return;
            }
        }
    }
    
    public ArrayList<Pietanze> getPietanze(){
        return piatti;
    }
    
    // Riscritto il metodo checkPietanza per maggiore robustezza
    // Se la logica è semplicemente aggiungere al carrello: 
    // public void checkPietanza(Pietanze pietanza, int newQnt) { ... }
    
    // Se la logica è MODIFICARE la quantità di una pietanza già ordinata:
    public void checkPietanza(Pietanze pietanza, int newQnt) {
        
        // Cerca la pietanza esistente nell'ordine
        for (int i = 0; i < this.piatti.size(); i++) {
            Pietanze p = piatti.get(i);
            if (pietanza.getNome().equals(p.getNome())) {
                // Trovato: aggiorna la quantità
                if (newQnt > 0) {
                    p.addQnt(newQnt); // Assumendo che addQnt aggiunga alla quantità esistente
                } else {
                    piatti.remove(i); // Rimuove se la quantità è 0 o negativa (gestione complessa)
                }
                return; 
            }
        }
        
        // Se non è stata trovata, aggiunge la nuova pietanza
        if (newQnt > 0) {
            pietanza.setQnt(newQnt);
            piatti.add(pietanza);
        }
    }
    
    public void showP() {
        System.out.println("Numero tavolo " + this.numTavolo);
        for(Pietanze el : piatti) {
            System.out.println(el.getNome() + " dentro a show\n");
        }
    }
}