package videojatekbolt;

import java.util.ArrayList;
import java.util.List;

class Felhasznalo {
    private String felhasznaloNev;
    private String jelszo;
    private String teljesNev="";
    private int kor;
    private int egyenleg = 0;
    private boolean admin = false;
    private boolean bannolt = false;
    private List<Jatek> megvasaroltJatekok;

    public Felhasznalo(String felhasznalonev, String jelszo, String teljesNev, int kor) {
        this.felhasznaloNev = felhasznalonev;
        this.jelszo = jelszo;
        this.teljesNev = teljesNev;
        this.kor = kor; 
        this.megvasaroltJatekok = new ArrayList();
    }

    public Felhasznalo(String felhasznalonev, String jelszo, String teljesNev, int kor,int egyenleg, boolean adminE, boolean bannoltE, List<Jatek> megvasaroltJatekok) {
        this.felhasznaloNev = felhasznalonev;
        this.jelszo = jelszo;
        this.teljesNev = teljesNev;
        this.kor = kor;
        this.egyenleg = egyenleg;
        this.admin = adminE;
        this.bannolt = bannoltE;
        this.megvasaroltJatekok = megvasaroltJatekok;
    }
    
    
    public void jatekokKilistazasa(){
        int index = 1;
        for (int i = 0; i < this.megvasaroltJatekok.size(); i++) {
            System.out.println(index +". "+this.megvasaroltJatekok.get(i));
            index++;
        }
    }
      
    
    public String getFelhasznaloNev() {
        return felhasznaloNev;
    }

    public String getJelszo() {
        return jelszo;
    }

    public String getTeljesNev() {
        return teljesNev;
    }

    public int getKor() {
        return kor;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isBanned() {
        return bannolt;
    }

    public void setTeljesNev(String teljesNev) {
        this.teljesNev = teljesNev;
    }

    public void setBan(boolean bannolt) {
        this.bannolt = bannolt;
    }
    
    public void penzfeltoltes(int osszeg){
        this.egyenleg += osszeg;
    }
    
    public void jatszas(Jatek jatek){
        
    }
    
    public void kijelentkezes(){
        
    }
    
    public String fileWriteString(){
        String ret = this.felhasznaloNev+";"+this.jelszo+";"+this.teljesNev+";"+this.kor+";"+this.egyenleg+";"+this.admin+";"+this.bannolt+";";
        for(int i = 0; i< this.megvasaroltJatekok.size(); i++){
            if(i < this.megvasaroltJatekok.size()-1){
                ret += this.megvasaroltJatekok.get(i).fileWriteString()+",";
            }else{
                ret += this.megvasaroltJatekok.get(i).fileWriteString();
            }            
        }

        return ret;
    }
    
    @Override
    public String toString() {
        return "Felhasználó név: " + felhasznaloNev + " jelszo=" + jelszo + ", teljesNev=" + teljesNev + ", kor=" + kor + ", egyenleg=" + egyenleg + ", admin=" + admin + ", bannolt=" + bannolt + '}';
    }

    public int getEgyenleg() {
        return egyenleg;
    }

    public boolean isBannolt() {
        return bannolt;
    }

    public List<Jatek> getMegvasaroltJatekok() {
        return megvasaroltJatekok;
    }

    public void setEgyenleg(int egyenleg) {
        this.egyenleg = egyenleg;
    }
    
    
        
}
