package Carta;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

import com.mysql.jdbc.Statement;

import DB.JDBC;

public class PilaDeCartas {
	
	private Stack<Carta> pilaCartas;
	
	public PilaDeCartas() {
		this.pilaCartas = new Stack<>();
	}
	
	public PilaDeCartas(Stack<Carta> pdc) {
		this.pilaCartas = pdc;
	}
	
	public void addTodosNumerosPalo(Palo p) {
		
		for(int i=1; i < 13; i++) {
			this.pilaCartas.push(new Carta(i, p));
		}
		
	}
	
	public void sort() {
		
		for(int i = 0; i < (this.getCantCartas() * 2); i++) {
			
			int posCarta_1 = (int) (Math.random() * this.getCantCartas());	
			int posCarta_2 = (int) (Math.random() * this.getCantCartas());
			
			if(posCarta_1 != posCarta_2) {
				Carta carta_1 = this.pilaCartas.get(posCarta_1);
				Carta carta_2 = this.pilaCartas.get(posCarta_2);
				
				this.pilaCartas.setElementAt(carta_1, posCarta_2);
				this.pilaCartas.setElementAt(carta_2, posCarta_1);
			}
			
		}
	}
	
	public void mostrarMazo() {
		
		for(int i = 0; i < this.getCantCartas(); i++) {
			Carta c = this.pilaCartas.get(i);
			System.out.println(c.getNumero() + " " + c.getNombrePalo());
		}
	}
	
	public Carta getFirstCarta() {
		return this.pilaCartas.pop();
	}
	
	public void pushCarta(Carta c) {
		this.pilaCartas.push(c);
	}
	
	public boolean mazoVacio() {
		return this.pilaCartas.empty();
	}
	
	public int getCantCartas() {
		return this.pilaCartas.size();
	}
	
	public Stack<Carta> getStackCartas() {
		return this.pilaCartas;
	}
	
	public void setStackCartas(Stack<Carta> mazo) {
		this.pilaCartas = mazo;
	}
	
	public Stack<Carta> getMazoCartasJDBC() {
		
		Statement stmt = null;
		Stack<Carta> mazo = new Stack<Carta>();
		
		try {
			//STEP 4: Execute a query
			System.out.println("Obteniendo cartas...\n");
			stmt = (Statement) JDBC.conn.createStatement();
			String sql = "SELECT c.numero_carta AS numero, pc.nombre_palo AS palo FROM PalosCartas pc INNER JOIN Cartas c ON c.id_palocarta = pc.id_palo";
			ResultSet rs = stmt.executeQuery(sql);
			
			//STEP 5: Extract data from result set
			while(rs.next()) {
				
				//Retrieve by column name
				Palo p = new Palo(rs.getString("palo"));
				Carta c = new Carta(rs.getInt("numero"), p); 
				mazo.push(c);
			}
			
			//STEP 6: Clean-up environment
			rs.close();
			stmt.close();
			
		}	catch(SQLException se) {
			//Handle errors for JDBC
			se.printStackTrace();
			
		}	catch(Exception e) {
			e.printStackTrace();
			
		}	finally {
			//finally block used to close resources
			try {
				if(stmt!=null)
					stmt.close();
				
			}	catch(SQLException se2) { }// nothing we can do
			
		}//end finally try
		
		return mazo;
	}
}
