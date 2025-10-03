import javax.swing.*;
import java.awt.*;

public abstract class Frame {
	protected JPanel panel = new JPanel(new BorderLayout());
	protected JButton btnSala = new JButton();
	protected JButton btnCliente = new JButton();
	protected JButton btnChef = new JButton();
	protected JButton btnPizzayolo = new JButton();
	protected JButton btnCassiere = new JButton(); 
	protected ImageIcon img = new ImageIcon("icon.png");
	protected JFrame frame = new JFrame();
	protected JLabel label = new JLabel();
	protected JMenuBar menuBar = new JMenuBar();
    
    public Frame(){
    frame.setSize(800,600);
    frame.setIconImage(img.getImage());
    frame.setResizable(false);
    frame.setVisible(true);
    frame.setLocationRelativeTo(null);
    
    }
}
