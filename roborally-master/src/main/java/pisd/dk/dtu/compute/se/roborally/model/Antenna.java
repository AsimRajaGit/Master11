/**
 * ...
 *
 *
 * @author Gustav Kirkholt
 *
 */

package pisd.dk.dtu.compute.se.roborally.model;

import pisd.dk.dtu.compute.se.designpatterns.observer.Subject;

/**
 * This class holds the necessary information to create the Antenna
 *
 * @author Gustav Kirkholt, s164765@student.dtu.dk
 */
public class Antenna extends Subject {

    public final Board board;

    public final int x;
    public final int y;

    public Antenna(Board board, int x, int y) {
        this.board = board;
        this.x = x;
        this.y = y;
    }
}
