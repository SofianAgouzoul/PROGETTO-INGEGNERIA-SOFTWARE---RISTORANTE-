import java.util.*;

public class MandaComande {
	
	private ArrayList<Admin> admins = new ArrayList<Admin>();
	
	public void add(Admin c) {
		admins.add(c);
	}
	public void remove(Admin c) {
		admins.remove(c);
	}
	public void allertaComanda(Tavolo tav) {
		for(Admin adm : admins) {
			adm.aggiungiComanda(tav);
		}
	}
	
}