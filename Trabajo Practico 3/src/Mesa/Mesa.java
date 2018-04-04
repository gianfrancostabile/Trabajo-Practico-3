package Mesa;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import com.mysql.jdbc.CallableStatement;

import Carta.PilaDeCartas;
import DB.JDBC;
import Persona.Jugador;
import Persona.Repartidor;

public class Mesa {
	
	private Repartidor repartidor;
	private ArrayList<Jugador> listaJugadores;
	private PilaDeCartas mazo = new PilaDeCartas();

	public Mesa() {
		
		this.mazo.setStackCartas(this.mazo.getMazoCartasJDBC());
		this.repartidor = new Repartidor(mazo, "Juan", "Perez", 54);
		
		this.listaJugadores = new ArrayList<Jugador>();
		
		Jugador jugador_1 = new Jugador("Lucas", "Langa", 22, repartidor);
		jugador_1.addObserver(repartidor);
		this.listaJugadores.add(jugador_1);
		
		Jugador jugador_2 = new Jugador("Luis", "Otorrino", 23, repartidor);
		jugador_2.addObserver(repartidor);
		this.listaJugadores.add(jugador_2);
		
		Jugador jugador_3 = new Jugador("Pedro", "Filantropo", 27, repartidor);
		jugador_3.addObserver(repartidor);
		this.listaJugadores.add(jugador_3);
		
		Jugador jugador_4 = new Jugador("Fabricio", "Esturdi", 44, repartidor);
		jugador_4.addObserver(repartidor);
		this.listaJugadores.add(jugador_4);
	}

	public Jugador getJugadorGanador() {
		
		Jugador ganador = new Jugador();
		
		for(Jugador jug : this.listaJugadores) {
			
			if(ganador.getPuntosTotales() < jug.getPuntosTotales()) {
				ganador = jug;
			}
		}
		
		return ganador;
	}
	
	
	public int addPartidaJDBC(int id_repartidor) {
		
		int id_partida = 0;
		CallableStatement stmt = null;
		
		try {
			System.out.println("Guardando la partida...");
			String sql = "CALL addPartida(?, ?);";
			
			stmt = (CallableStatement) JDBC.conn.prepareCall(sql);
			stmt.setInt(1, id_repartidor);
			stmt.registerOutParameter(2, Types.INTEGER);

			stmt.executeQuery();
			
			id_partida = stmt.getInt(2);
			
			stmt.close();
			
		}	catch(Exception e) {
			e.getStackTrace();
			
		}	finally {
			
			try {
				if(stmt != null) {
					stmt.close();
				}
			}	catch(SQLException se) {}
		
		}
		
		return id_partida;
	}
	
	public int addGanadorPartidaJDBC(int id_jugador, int id_partida, double puntos) {
		int id_ganador_partida = 0;
		CallableStatement stmt = null;
		
		try {
			
			System.out.println("Guardando al ganador de la partida...");
			String sql = "CALL addGanadorPartida(?, ?, ?, ?);";
			
			stmt = (CallableStatement) JDBC.conn.prepareCall(sql);
			stmt.setInt(1, id_jugador);
			stmt.setInt(2, id_partida);
			stmt.setDouble(3, puntos);
			stmt.registerOutParameter(4, Types.INTEGER);
			
			stmt.executeQuery();
			
			id_ganador_partida = stmt.getInt(4);
			
			stmt.close();
			
		}	catch (Exception e) {
			e.getStackTrace();
		
		}	finally {
			try {
				
				if(stmt != null) {
					stmt.close();
				}
			}	catch(SQLException se) {}
			
		}
		
		return id_ganador_partida;
	}
	
	public void saveAll(Jugador ganador, Repartidor repartidor) {

		int id_jugador = ganador.addJugadorJDBC(ganador);
		int id_repartidor = repartidor.addRepartidorJDBC(repartidor);
		int id_partida = this.addPartidaJDBC(id_repartidor);
		int id_ganador_partida = this.addGanadorPartidaJDBC(id_jugador, id_partida, ganador.getPuntosTotales());
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public ArrayList<Jugador> getListaJugadores() {
		return listaJugadores;
	}

	public PilaDeCartas getMazo() {
		return mazo;
	}

	
}
