package com.example.martin.connectfour;

/**
 * Created by Martin on 18.02.2017.
 */

import java.util.HashMap;

import static java.lang.Long.bitCount;

class Spielfeld {

    private long p1 = 0b0;
    private long p2 = 0b0;

    Spielfeld() {}
    Spielfeld(long p1, long p2){
        this.p1 = p1;
        this.p2 = p2;
    }

    public long getlong(int Spieler){
        return (Spieler == 1) ? p1 : p2;
    }

    public int feld(int reihe, int spalte) {
        if((this.p1>>(8*reihe+spalte) & 1) == 1) return 1;
        else if((this.p2>>(8*reihe+spalte) & 1) == 1) return 2;
        else return 0;
    }

    public boolean full() {
        if((bitCount(this.p1) + bitCount(this.p2)) == 42) return true;
        return false;
    }

    public void spielstand() {
        for (int i = 5; i >= 0; i--) {
            System.out.print("|");
            for (int j = 0; j < 7; j++) {
                int field = j+8*i;
                if(((this.p1 >> field) & 1) == 1){
                    System.out.print("x");
                }
                else if(((this.p2 >> field) & 1) == 1){
                    System.out.print("o");
                }
                else{
                    System.out.print(" ");
                }
            }
            System.out.print("|"+System.lineSeparator());
        }
        System.out.println("+-------+");
    }
    public Spielfeld zug(int spieler, int spalte) {

        boolean t = false;
        if (spalte > 6) {
            return null;
        }
        for(int i=0; i<6; i++){
            if(this.feld(i, spalte) == 0){
                if(spieler == 1){
                    this.p1 = this.p1 | (1L << (spalte+8*i));
                    t=true;
                    i=6;
                }
                else if(spieler == 2){
                    this.p2 = this.p2 | (1L << (spalte+8*i));
                    t=true;
                    i=6;
                }
            }
        }
        if(!t) return null;
        return this;
    }

    public boolean sieg(int spieler) {
        // TODO: Implementieren Sie hier die Angabe
        long player = (spieler==1) ? this.p1 : this.p2;

        long row = (player >> 0) & (player >> 1) & (player >> 2) & (player >> 3);
        if(bitCount(row) > 0) return true;

        long column = (player >> 0) & (player >> 8) & (player >> 16) & (player >> 24);
        if(bitCount(column) > 0) return true;

        long diagonalR = (player >> 0) & (player >> 9) & (player >> 18) & (player>> 27);
        if(bitCount(diagonalR) > 0) return true;

        long diagonalL = (player >> 0) & (player >> 7) & (player >> 14) & (player>> 21);
        if(bitCount(diagonalL) > 0) return true;

        return false;
    }

    public int wert1(int spieler) {
        return this.count(spieler, 1) + this.count(spieler, 8) + this.count(spieler, 9) + this.count(spieler, 7);
    }
    private int count(int spieler, int n){
        long player = (spieler==1) ? this.p1 : this.p2;

        long zweier = (player << (0*n)) & (player << (1*n));
        long dreier = (player << (0*n)) & (player << (1*n)) & (player << (2*n));
        long vierer = (player << (0*n)) & (player << (1*n)) & (player << (2*n)) & (player << (3*n));

        return (bitCount(zweier)+bitCount(dreier)*100+bitCount(vierer)*100000) ;
    }

    public int wert(int spieler) {
        // TODO: Implementieren Sie hier die Angabe
        if (spieler == 1) {
            return wert1(1) - wert1(2);
        } else {
            return wert1(2) - wert1(1);
        }
    }

    public int negamax(int spieler, int tiefe, HashMap<Spielfeld, Integer> moves) {
        // TODO: Implementieren Sie hier die Angabe

        int gegner = (spieler == 1) ? 2 : 1;
        int maxwert = Integer.MIN_VALUE;

        if (tiefe == 0) {
            return wert(spieler);
        }

        for (int i = 0; i < 7; i++) {
            Spielfeld newlong = new Spielfeld(this.p1, this.p2);
            if (newlong.zug(spieler, i) != null) {
                int value;
                if(newlong.sieg(spieler)){
                    value = Integer.MAX_VALUE;
                }
                else if(moves.containsKey(newlong)){
                    value = moves.get(newlong);
                }
                else {
                    value = -newlong.negamax(gegner, tiefe - 1, moves);
                    moves.put(newlong, value);
                }
                if (value > maxwert) {
                    maxwert = value;
                }
            }
        }
        return maxwert;
    }

    public int bester(int spieler, int tiefe) {

        int gegner = (spieler == 1) ? 2 : 1;
        int maxwert = Integer.MAX_VALUE;
        int col = 0;

        if (tiefe == 0) {
            return wert(spieler);
        }

        for (int i = 0; i < 7; i++) {
            Spielfeld newlong = new Spielfeld(this.p1, this.p2);
            if (newlong.zug(spieler, i) != null) {
                if(newlong.sieg(spieler)){
                    return i;
                }
                int value = newlong.negamax(gegner, tiefe, new HashMap<Spielfeld, Integer>());
                if (value < maxwert) {
                    maxwert = value;
                    col = i;
                }
            }
        }

        return col;
    }

    @Override
    public int hashCode() {

        long c1=0x97e2a1430e3ab551L;
        long c2=0xddd7aaa5a1ccca9bL;
        return (int)((c1*this.p1+c2*this.p2)>>32);
    }

    @Override
    public boolean equals(Object obj) {
        Spielfeld other = (Spielfeld)obj;
        if(this.p1 == other.p1 && this.p2 == other.p2){
            return true;
        }
        return false;
    }

}
