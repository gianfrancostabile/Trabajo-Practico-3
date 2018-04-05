package Mesa;

import DB.JDBC;
import Persona.Jugador;

public class Main {

	private static JDBC dataBase;
	private static Mesa mesa;

	public static void main(String[] args) {

		dataBase = JDBC.getInstance();

		mesa = new Mesa();

		for (Jugador jug : mesa.getListaJugadores()) {
			new Thread(jug).start();
		}

		while(Thread.activeCount() > 1) {

		}

		Jugador ganador = mesa.getJugadorGanador();
		System.out.println("\n\nEl ganador es " + ganador.getNombre() + " " + ganador.getApellido() + " con " + ganador.getPuntosTotales() + " puntos.\n");

		mesa.saveAll(ganador, mesa.getRepartidor());

		dataBase.closeJDBC();
	}
}
