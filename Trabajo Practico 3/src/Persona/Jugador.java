package Persona;

import java.sql.SQLException;
import java.sql.Types;
import java.util.Observable;
import java.util.Random;

import com.mysql.jdbc.CallableStatement;

import Carta.Carta;
import Carta.PilaDeCartas;
import DB.JDBC;

public class Jugador extends Observable implements Runnable{
	
	private Repartidor repartidor;
	
	private PilaDeCartas cartasObtenidas = new PilaDeCartas();
	
	private String nombre = "";
	private String apellido = "";
	private int edad = 18;
	private double puntosTotales = 0;
	
	public Jugador(String nombre, String apellido, int edad, Repartidor repartidor) {
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.repartidor = repartidor;
	}
	
	public Jugador() {}

	public synchronized double sumarPuntos() {
		double cant = 0;
		
		for(int i = 0; i < this.cartasObtenidas.getCantCartas(); i++) {
			
			Carta carta = this.cartasObtenidas.getFirstCarta();
			double puntosCarta = 0;
			
			switch(carta.getNombrePalo()) {
				case "Oro" : puntosCarta = carta.getNumero() * 2;
				break;
				
				case "Espada" : puntosCarta = carta.getNumero() * 1.5;
				break;
				
				case "Copa" : puntosCarta = carta.getNumero();
				break;
				
				case "Basto" : puntosCarta = carta.getNumero() * (-1);
				break;
			}
			
			cant = cant + puntosCarta;
		}
		
		return cant;
	}
	
	/*Este método se encargara de recibir una carta de la pila de cartas que posea el repartidor */
	@Override
	public void run() {
		
		while(!this.repartidor.estadoMazo()) {

			try {
				
				Carta carta_obtenida = this.repartidor.pedirCarta();
				this.pushCartaToPila(carta_obtenida);
				
				this.setChanged();
				this.notifyObservers(carta_obtenida);	
				
			} 	catch (Exception e) { 
				
			}	finally {
					
					try {
						Thread.sleep(new Random().nextInt(1000));
					} catch (InterruptedException e) { }
			}
		}
		
		this.puntosTotales = this.sumarPuntos();
		System.out.println("\n" + this.nombre + " " + this.apellido + " sumo: " + this.puntosTotales + " puntos.");
	}

	public void pushCartaToPila(Carta c) {
		this.cartasObtenidas.pushCarta(c);
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
	
	public void setPuntosTotales(int puntos) {
		this.puntosTotales = puntos;
	}
	
	public double getPuntosTotales() {
		return this.puntosTotales;
	}
	
	public int addJugadorJDBC(Jugador jug) {
		
		CallableStatement stmt = null;
		int id_jugador = 0;
		
		try {
			System.out.println("Guardando al jugador ganador...");
			
			String sql = "CALL addJugador(?, ?, ?, ?);";
			
			stmt = (CallableStatement) JDBC.conn.prepareCall(sql);
			
			stmt.setString(1, jug.getNombre());
			stmt.setString(2, jug.getApellido());
			stmt.setInt(3, jug.getEdad());
			stmt.registerOutParameter(4, Types.INTEGER);
			
			stmt.executeQuery();
			id_jugador = stmt.getInt(4); 

			//STEP 6: Clean-up environment
			stmt.close();
			
		}	catch(Exception e) {
			e.printStackTrace();
			
		}	finally {
			//finally block used to close resources
			try {
				if(stmt!=null)
					stmt.close();
				
			}	catch(SQLException se2) { }// nothing we can do
			
		}
		
		return id_jugador;
	}
}
