package videojatekbolt;

import java.util.logging.Level;
import java.util.logging.Logger;

class Jatek {
    private String nev;
    private String stilus;
    private int korhatar;
    private int ar;
    private String leiras = "";
    private int jatszottOrak = 0;

    public Jatek(String nev, String stilus, int korhatar, int ar) {
        this.nev = nev;
        this.stilus = stilus;
        this.korhatar = korhatar;
        this.ar = ar;  
    }
    
    public Jatek(String nev, String stilus, int korhatar, int ar,String leiras,int jatszottOrak) {
        this.nev = nev;
        this.stilus = stilus;
        this.korhatar = korhatar;
        this.ar = ar;
        this.leiras = leiras;
        this.jatszottOrak = jatszottOrak;
    }

    public String getNev() {
        return nev;
    }

    public String getStilus() {
        return stilus;
    }

    public int getKorhatar() {
        return korhatar;
    }

    public int getAr() {
        return ar;
    }

    public String getLeiras() {
        return leiras;
    }

    public int getJatszottOrak() {
        return jatszottOrak;
    }

    public void setLeiras(String leiras) {
        this.leiras = leiras;
    }

    public void setJatszottOrak(int jatszottOrak) {
        this.jatszottOrak = jatszottOrak;
    }

    public String fileWriteString() {
        return  nev + "*" + stilus + "*" + korhatar + "*" + ar + "*" + leiras + "*" + jatszottOrak;
    }

    @Override
    public String toString() {
        return "Jatek{" + "nev=" + nev + ", stilus=" + stilus + ", korhatar=" + korhatar + ", ar=" + ar + ", leiras=" + leiras + ", jatszottOrak=" + jatszottOrak + '}';
    }

    
    
    
    public void jatszas(int oraSzam) {     
        this.jatszottOrak += oraSzam; 
        System.out.println("Jó játékot!");
        try {
            for (int i = oraSzam; i >= 1; i--) {
                System.out.println(i);
                Thread.sleep(1000);
            }
            
        } catch (InterruptedException ex) {
            Logger.getLogger(Jatek.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Vége a játéknak!");
        
    }

    
}
