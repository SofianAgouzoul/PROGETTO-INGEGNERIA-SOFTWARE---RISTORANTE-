import java.util.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class Chef extends Frame implements Cuoco{

	
	private ArrayList<Tavolo> reference = new ArrayList<Tavolo>();
	private ArrayList<String> listaPrimiPiatti = new ArrayList<String>();
	private ArrayList<Ordine> listaOrdini = new ArrayList<Ordine>();
	private JTextArea textArea = new JTextArea(50,40);
	private JPanel textPanel = new JPanel();
	private JButton nextOrd = new JButton("Ordine concluso");
	private Integer ordineSelezionato;
	private JPanel botPanel = new JPanel(new FlowLayout());
	private JButton btnStorico = new JButton("Storico ordini");
	
	
	public Chef() {
		Border blackline, raisedetched, loweredetched,raisedbevel, loweredbevel, empty;
	    
		blackline = BorderFactory.createLineBorder(Color.black);
		raisedetched = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
		loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		raisedbevel = BorderFactory.createRaisedBevelBorder();
		loweredbevel = BorderFactory.createLoweredBevelBorder();
		empty = BorderFactory.createEmptyBorder();
		
		frame.setTitle("Gestionale Ristorante-Chef");

		panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(new Color(210,210,210));
		
		
		
		JScrollPane scrollPanel = new JScrollPane(panel);
		JScrollPane textScrollPanel = new JScrollPane(textPanel);
		textScrollPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		textArea.setFont(new Font("Courier", Font.BOLD, 15));
		textArea.setEditable(false);
		
		textPanel.add(textArea);
		textPanel.setBackground(new Color(1,121,111));
		textPanel.setBorder(BorderFactory.createCompoundBorder(raisedetched,loweredetched));
		
		
		nextOrd.addActionListener(e -> {
			cucina(this.ordineSelezionato);
			nextOrd.setEnabled(false);
			frame.revalidate();
		});
		
		
		btnStorico.addActionListener(e -> {
			textArea.setText("");
			for(Ordine ord : listaOrdini) {
				textArea.append("Ordine numero: "+ord.getNumOrd()+"\tTavolo: "+ord.getNumTavolo()+"\n");
				for(Pietanze p : ord.getPietanze()) {
					textArea.append("-"+p.getNome()+" x"+p.getQnt()+"\n");
				}
				textArea.append("\t|----------|\n");
			}
		});
		botPanel.add(btnStorico);
		botPanel.setBackground(new Color(210,210,210));
		
		frame.add(botPanel, BorderLayout.SOUTH);
		frame.add(scrollPanel, BorderLayout.WEST);
		frame.add(textScrollPanel, BorderLayout.CENTER);
		frame.add(nextOrd, BorderLayout.EAST);
		
		System.out.println("Chef creato!");
		Menu menu = new Menu();
		for(Pietanze element : menu.getPrimiPiatti()) {
			listaPrimiPiatti.add(element.getNome());
		}
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@Override
	public void updateTODO(Tavolo tav, Ordine ordine) {
		Boolean trovato=false;
		Ordine ordTmp = new Ordine(ordine.getNumTavolo());
		for(Tavolo t : this.reference) {
			if(t.getNumTav()==tav.getNumTav()) {
				trovato=true;
			}
		}
		if(!trovato) {
			reference.add(tav);
		}
		for(Pietanze elem : ordine.getPietanze()) {
			for(String nomePietanza : listaPrimiPiatti) {
				if(nomePietanza==elem.getNome()) {
					ordTmp.aggiungiPietanza(nomePietanza, elem.getQnt());
				}
			}
		}
		if(!ordTmp.getPietanze().isEmpty()) {
			listaOrdini.add(ordTmp);
			JButton btnTmp = new JButton("Ordine numero: "+ordine.getNumOrd());
			btnTmp.addActionListener(e -> {
				textArea.setText("Ordine numero: "+ordine.getNumOrd()+"\n");
				for(Pietanze p : ordTmp.getPietanze()) {
					textArea.append(p.getNome()+" x"+p.getQnt()+"\n");
				}
				this.ordineSelezionato=ordine.getNumTavolo();
				if(ordTmp.getState() instanceof OrdineConsegnato)
					nextOrd.setEnabled(true);
			});
			panel.add(btnTmp);
			frame.revalidate();
			tav.setStatusOrdine(new OrdineRicevuto());
		}
		
	}
	
	public void cucina(Integer numTavolo) {
		System.out.println("Lo Chef sta preparando gli ingredienti");
		Integer delete=0;
		Boolean changed=false;
		for(Ordine ord : this.listaOrdini) {
			if(ord.getNumTavolo()==numTavolo) {
				ord.setState(new OrdineConsegnato());
			}
		}
		for(Integer i=0;i<reference.size();i++) {
			if(reference.get(i).getNumTav()==numTavolo) {
				reference.get(i).setStatusOrdine(new OrdineConsegnato());
				changed=true;
				delete=i;
			}
		}
		if(changed) {
			reference.remove(delete);
			}
	}
	
	

}