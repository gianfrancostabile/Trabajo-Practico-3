package Carta;

public class Carta {
	
	private int numero;
	private Palo palo;
	
	public Carta(int n, Palo p) {
		this.numero = n;
		this.palo = p;
	}
	
	public int getNumero() {
		return this.numero;
	}
	
	public String getNombrePalo() {
		return this.palo.nombre;
	}
}
