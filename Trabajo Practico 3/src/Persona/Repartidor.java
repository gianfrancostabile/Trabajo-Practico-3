package Persona;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Observable;
import java.util.Observer;

import com.mysql.jdbc.CallableStatement;

import Carta.Carta;
import Carta.PilaDeCartas;
import DB.JDBC;

public class Repartidor implements Observer{
	
	private PilaDeCartas mazo;
	private String nombre;
	private String apellido;
	private int edad;
	
	public Repartidor(PilaDeCartas mazo, String nombre, String apellido, int edad) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		
		this.mazo = mazo;
	}
	
	public synchronized Carta pedirCarta() throws Exception {
		
		if(!this.estadoMazo()) {
			return this.mazo.getFirstCarta();
		
		}	else {
			throw new Exception("Mazo vacio");
		}
	}
	
	@Override
	public synchronized void update(Observable jugador, Object carta) {

		if(carta instanceof Carta && jugador instanceof Jugador) {
			Carta cartita = (Carta)carta;
			Jugador jug = (Jugador)jugador;
			System.out.println(jug.getNombre() + " " + jug.getApellido() + " recibio: " + cartita.getNumero() + " de " + cartita.getNombrePalo());
		}
		
	}
	
	public synchronized boolean estadoMazo() {
		return this.mazo.mazoVacio();
	}
	
	public void mezclarMazo() {
		this.mazo.sort();
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public String getApellido() {
		return this.apellido;
	}
	
	public int getEdad() {
		return this.edad;
	}
	
	public int addRepartidorJDBC(Repartidor rep) {
		int id_repartidor = 0;
		CallableStatement stmt = null;
		
		try {
			System.out.println("Guardando al repartidor...");
			String sql = "CALL addRepartidor(?, ?, ?, ?);";
			
			stmt = (CallableStatement) JDBC.conn.prepareCall(sql);
			stmt.setString(1, rep.getNombre());
			stmt.setString(2, rep.getApellido());
			stmt.setInt(3, rep.getEdad());
			stmt.registerOutParameter(4, Types.INTEGER);
			
			stmt.executeQuery();
			id_repartidor = stmt.getInt(4);
			
			stmt.close();
			
		}	catch(Exception e) {
			e.getStackTrace();
		
		}	finally {
			
			try {
				if(stmt != null) {
					stmt.close();
				}
			}	catch(SQLException se2) {}
			
		}
		
		return id_repartidor;
	}
}
