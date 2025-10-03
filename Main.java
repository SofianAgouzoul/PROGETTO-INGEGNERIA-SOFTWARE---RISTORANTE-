
public class Main {

	public static void main(String[] args) {
                @SuppressWarnings("unused")
		MainFrame start = new MainFrame(); //Frame della GUI

		Menu repository = new Menu(); //Repository per il pattern Iterator //Prende i due menu e li itera
		System.out.println("[MAIN:] Recupero menu da repository:" + "\n");
		
		for(IteratoreMenu iteratorePrimi = repository.getIterator(); iteratorePrimi.hasNextPP();) {
			
			String menu2 = (String)iteratorePrimi.nextPP();

			System.out.println("Menu Primi -> " + menu2 + "\t");
		}
		
		for(IteratoreMenu iteratorePizze = repository.getIterator(); iteratorePizze.hasNextP();) {
			
			String menu = (String)iteratorePizze.nextP();
			
            System.out.println("Menu Pizze -> " + menu + "\t");
            
		}
		
		for(IteratoreMenu iteratoreBevande = repository.getIterator(); iteratoreBevande.hasNextB();) {
			
			String menu1 = (String)iteratoreBevande.nextB();

			System.out.println("Menu Bevande -> " + menu1 + "\t");
		}
	
	}

}
