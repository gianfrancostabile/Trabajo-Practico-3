package Mesa;

import DB.JDBC;
import Persona.Jugador;

public class Main {

	private static JDBC data_base;

	public static void main(String[] args) {

		data_base = JDBC.getInstance();

		Mesa mesa = new Mesa();
		mesa.getRepartidor().mezclarMazo();

		for (Jugador jug : mesa.getListaJugadores()) {
			new Thread(jug).start();
		}

		while(Thread.activeCount() > 1) {

		}

		Jugador ganador = mesa.getJugadorGanador();
		System.out.println("\n\nEl ganador es " + ganador.getNombre() + " " + ganador.getApellido() + " con " + ganador.getPuntosTotales() + " puntos.\n");

		mesa.saveAll(ganador, mesa.getRepartidor());

		data_base.closeJDBC();
	}
}
