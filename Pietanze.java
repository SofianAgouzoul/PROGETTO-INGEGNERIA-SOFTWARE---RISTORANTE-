public class Pietanze implements Cloneable{

    private String nome;
	private double prezzo;
	private Integer qnt;
	    
	    //COSTRUTTORE
		public Pietanze() {
			
		}
	    public Pietanze(String nP,Double pP) {
			this.nome = nP; /*nP nome Pietanza*/
			this.prezzo = pP; /*pR prezzo pietanza*/
			qnt=0;
	    }
	    
	    public String getNome() {
	    	return this.nome;
	    }
	    public void setNome(String nomeP) {
	    	this.nome=nomeP;
	    }
	    
	    public Double getPrezzo() {
	    	return this.prezzo;
	    }
		public void setQnt(Integer n) {
				this.qnt=n;
		}
		public void addQnt(Integer add) {
			this.qnt+=add;
		}
	    public Integer getQnt() {
	    	return this.qnt;
	    }
	    
	    public Pietanze makeCopy(Integer q) {
	    	Pietanze copy = null;
	    	try {
				copy = (Pietanze) super.clone();
				copy.setQnt(q);
			} catch (CloneNotSupportedException e) {
				System.out.println("Non sono riuscito a copiare "+this.nome);
				e.printStackTrace();
			}
	    	return copy;
	    }
		
}
