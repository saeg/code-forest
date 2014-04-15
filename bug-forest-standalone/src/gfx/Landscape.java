package gfx;

import java.awt.*;
import java.util.*;

public class Landscape
{
	// Deklaration der Variablen
	private int start;			// Gibt die Starthöhe der Landschaft vor
	private int change;			// Entscheidet, ob die Richtung der Landschaftsentwicklung geändert wird
	private int faktor;			// Entscheidet über Addition oder Substraktion
	private int last;			// Speichert die letzte Höhe der letzten gezeichneten Linie
	private int plus;			// Wieviel wird addiert oder subtraiert

	private final int mapsize = 700;	// Konstante für die Größe des Arrays

	// Variable zur Erzeugung einer Zufallszahl
	Random rnd = new Random ();

	// Array zum Speichern der Höhenpunkte
	public int [] map;

	// Array zum Speichern der Farbwerte der jeweiligen Höhenpunkte
	public Color [] colors;

	/** Construktormethode */

	Landscape ()
	{
		// Initialisierung der Variable plus
		faktor = 1;

		// Initialisierung der Arraygröße von map und colors
		map = new int [mapsize];
		colors = new Color [mapsize];

		// Aufruf der Methode generateLandscape
		generateLandscape ();
	}

	/** Diese Methode initialisiert das Array map mit Integerzahlen. Diese liefern später die
	oberen y - Werte der Linien, die die Landschaft bilden. Hierzu wird zu Beginn der Funktion
	ein Startwert zwischen 250 und 350. Diese Zahl wird als erste Position im Array gespeichert.
	Nun werden alle Felder des Arrays abgelaufen und der Wert plus wird zur letzten Position
	entweder hizugezählt oder abgezogen. Die eingeschlagene Richtung wird dabei in 70% der Fälle
	beibehahlten, in 30% der Fälle wechselt die Richtung. Diese Positionen werden nun im Array
	gespeichert */

	public void generateLandscape ()
	{
		// Initialisierung von plus
		plus = 1;

		// Initialisierung des Startwertes
		start = Math.abs(300 + (rnd.nextInt() % 50));

		// Speichern des Startwertes an der ersten Stelle des Arrays
		map [0] = start;

		// Initialisierung der Startfarbwerte
		int greenvalue = 200;
		int redvalue = Math.abs(rnd.nextInt() % 200);
		int bluevalue = Math.abs(rnd.nextInt() % 201);

		// Speichern des ersten Startwertes für das Farbenarray
		colors [0] = new Color (redvalue, greenvalue, bluevalue);

		// Loop zur Initialisierung aller anderen Arrayfelder
		for (int i = 1; i < mapsize; i ++)
		{
			// Speichern der letzten Arrayposition für Höhe und Farbe
			last = map [i - 1];

			// Entscheidet, ob die eingeschlagene Richtung gewechselt wird
			change = Math.abs(rnd.nextInt() % 10);

			// Wenn change > 7 ist, dann ändert sich die Richtung und möglicherweise plus
			if (change > 8)
			{
				// Ändern der Richtung
				faktor = - (faktor);

				// Wieviel wird addiert bzw. substrahiert
				plus = 1 + Math.abs(rnd.nextInt() % 2);
			}

			// Wird ein bestimmter Wert unter- bzw. überschritten, dann wird die Richtung geändert
			if (last > 350 || last < 120)
			{
				// Ändern der Richtung
				faktor = - (faktor);
			}

			// Hält die Farbwerte immer in einem bestimmten Rahmen
			if (greenvalue > 240)
			{
				// Wenn Farbwert zu groß wird, erniedrigen des Wertes
				greenvalue -= 10;
			}
			else if (greenvalue < 100)
			{
				// Wenn Farbwert zu klein wird erhöhen des Farbwertes
				greenvalue += 10;
			}

			// Werte für das Feld an i - Stelle werden berechnet
			map [i] = last + (faktor * plus);

			/** Um die Farbewerte für zunehmende Höhe heller werden zu lassen, wird der Faktor
			umgekehrt. Dies ist wegen dem umgekehrten Koordinatensystem von Java nötig */
			greenvalue = greenvalue + (-faktor * plus);
			colors [i] = new Color (redvalue, greenvalue, bluevalue);
		}
	}

	/** Die Funktion paintMap zeichnet die Landschaft. Dazu werden die Felder des Arrays durchlaufen
	und eine Linie wird an x - Position = Arrayindex und von der y - Position = Wert an Arrayindex
	bis zum Boden wird dann gezeichnet */
	public void paintMap (Graphics g)
	{
		// Loop zeichnet für alle Felder des Arrays eine Linie
		for (int index = 0; index < mapsize; index ++)
		{
			// Definition der Farbe gemäß dem colors - Array (grün - türkies - blau)
			g.setColor (colors [index]);

			// Linie zeichnen
			g.drawLine (index, map[index], index, 400);
		}
	}
}
