import java.util.*;
public class Menu implements IteratoreContainer{
	private ArrayList<Pietanze> allMenu = new ArrayList<Pietanze>();
	private ArrayList<Pietanze> MenuPizze = new ArrayList<Pietanze>();
	private ArrayList<Pietanze> MenuBibite = new ArrayList<Pietanze>();
	private ArrayList<Pietanze> MenuPrimiPiatti= new ArrayList<Pietanze>();
	
	public Menu(){
		this.MenuPizze.add(new Pietanze("Margherita", 4.50));
		this.MenuPizze.add(new Pietanze("Marinara", 4.00));
		this.MenuPizze.add(new Pietanze("Diavola", 5.00));
		this.MenuPizze.add(new Pietanze("Bufalina", 6.00));
		this.MenuPizze.add(new Pietanze("Crocchè", 6.50));
		this.MenuPizze.add(new Pietanze("Primavera", 4.00));
		
		this.MenuBibite.add(new Pietanze("Vino bianco", 3.00));
		this.MenuBibite.add(new Pietanze("Acqua Frizzante", 1.00));
		this.MenuBibite.add(new Pietanze("Acqua Naturale", 0.50));
		this.MenuBibite.add(new Pietanze("Birra", 2.50));
		this.MenuBibite.add(new Pietanze("Fanta", 1.50));
		this.MenuBibite.add(new Pietanze("Coca Cola", 1.50));
		
		this.MenuPrimiPiatti.add(new Pietanze("Spaghetti con le vongole",15.50));
		this.MenuPrimiPiatti.add(new Pietanze("Risotto carnaroli",12.50));
		this.MenuPrimiPiatti.add(new Pietanze("Pasta ai pomodorini",10.00));
		this.MenuPrimiPiatti.add(new Pietanze("Tagliatelle di nonna Pina",100.00));
		
		this.allMenu.addAll(this.MenuPizze);
		this.allMenu.addAll(this.MenuPrimiPiatti);
		
		}
	
	public ArrayList<Pietanze> getAllFood(){
		return this.allMenu;
	}
	public ArrayList<Pietanze> getPizze(){
		return this.MenuPizze;
	}
	public ArrayList<Pietanze> getBibite(){
		return this.MenuBibite;
	}
	public ArrayList<Pietanze> getPrimiPiatti(){
		return this.MenuPrimiPiatti;
	}
	@Override
	public IteratoreMenu getIterator(){
		return new NameIterator();
	}
	
	public class NameIterator implements IteratoreMenu {
		int i=0,j=0,k=0;
		
		@Override
		public boolean hasNextP() {
			if(i < MenuPizze.size()) {
				return true;
			}
			return false;
		}
		
		@Override
		public boolean hasNextB() {
			if(j < MenuBibite.size()){
				return true;
			}
			return false;
		}
		
		
		@Override
		public boolean hasNextPP() {
			if(k < MenuPrimiPiatti.size()){
				return true;
			}
			return false;
		}
		
		@Override
		public Object nextP() {
			if(this.hasNextP()) {
				return MenuPizze.get(i++).getNome();
			}
			return null;
		}
		
		@Override 
		public Object nextB() {
			if(this.hasNextB()) {
				return MenuBibite.get(j++).getNome();
			}
			return null;
		}
		
		@Override 
		public Object nextPP() {
			if(this.hasNextPP()) {
				return MenuPrimiPiatti.get(k++).getNome();
			}
			return null;
		}
		
      }
	
    public double prendiContoPietanze(int indici[]){
    int i;
    double sumtot;
    sumtot=0;
    for(i=0;i<indici.length;i++){
        sumtot+=this.MenuPizze.get(indici[i]).getPrezzo();
        }
        return sumtot;
    }
    
    public double prendiContoBevande(int indici[]){
        int i;
        double sumtot;
        sumtot=0;
        for(i=0;i<indici.length;i++){
            sumtot+=this.MenuBibite.get(indici[i]).getPrezzo();
            }
            return sumtot;
        }
    
    public double prendiContoPrimi(int indici[]) {
    	int i;
    	double sumtot; 
    	sumtot = 0;
    	for(i=0;i<indici.length;i++) {
    		sumtot+=this.MenuPrimiPiatti.get(indici[i]).getPrezzo();
    	}
    	return sumtot;
    	
    }
    
    public double contoTot(double costoPizza, double costoBibita, double costoPrimoPiatto){
            return costoPizza+costoBibita+costoPrimoPiatto;
    }
    
    

}
