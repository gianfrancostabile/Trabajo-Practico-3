# Trabajo Practico 3

**1) Persistencia ultilizada y por qué se eligió**. -MySQL porque es el unico que sé utilizar mejor.

**2) En caso de agregar una dificultad, explicar la funcionalidad del juego.** -El juego consta de 4 jugadores y 1 repartidor, cada jugador va pidiendo una carta, compitiendo entre ellos quien obtiene una. Esta acción se va a ir realizando siempre, hasta que el repartidor se quede sin cartas. Una vez que no hayan mas cartas que repartir, cada jugador sumarán sus puntos, el punto lo determina el número de la carta y ademas dependiendo del palo se aplicará un bonus. Este bonus determinará en el valor de la carta, X es el numeró de la carta:

    Si la Carta es de:
      -Oro = X * 2.
      -Espada = X * 1.5.
      -Copa = X * 1.
      -Basto = X * (-1).
      
Los valores obtenidos por cada carta se suman si obtendrán su puntaje total. Una vez que todos hayan obtenido su puntaje, el jugador que tenga el mayor número de puntos será el ganador.

**3)Realizar un Diagrama UML y adjuntarlo.** El diagrama se encuentra dentro de la carpeta del proyecto.          (https://github.com/gianfrancostabile/Trabajo-Practico-3/blob/master/Trabajo%20Practico%203/src/UML_TP3_LabV.ucls)
