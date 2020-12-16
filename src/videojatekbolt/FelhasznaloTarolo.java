package videojatekbolt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FelhasznaloTarolo {
    
    private final List<Felhasznalo> felhasznalok;

    public FelhasznaloTarolo() {
        felhasznalok = new ArrayList();
        init();
    }
    
        

    
    public boolean ban(String fnev){        
        int index = -1;
        for (int i=0; i < felhasznalok.size(); i++) {
            if(felhasznalok.get(i).getFelhasznaloNev().equals(fnev)){                
                index = i;
            }
        }
        
        if(index != -1){
            this.felhasznalok.get(index).setBan(true);
            return true;
        }
        return false;        
    }
    
    
    
     public boolean unban(String fnev){       
        int index = -1;
        for (int i=0; i < felhasznalok.size(); i++) {
            if(felhasznalok.get(i).getFelhasznaloNev().equals(fnev)){                
                index = i;
            }
        }
        if(index != -1){
            this.felhasznalok.get(index).setBan(false);
            return true;
        }else{
            System.out.println("Nem létezik ilyen felhasználónév");
        }
        return false; 
    }
    
    public void felhasznalokKilistazas(){
        
        int sorszam = 1;
        if(this.felhasznalok.size()<0){
            System.out.println("Nincs felhasználó az adatbázisban");
        }else{
            for(Felhasznalo felh : this.felhasznalok){
            System.out.println(sorszam+". "+felh);
            sorszam++;
            }
        }
        
    }
    
    public Felhasznalo getFelhasznalo(int index){
        return this.felhasznalok.get(index);
    }

    public List<Felhasznalo> getFelhasznalok() {
        return felhasznalok;
    }

    private void init() {
        try {
            //tároló feltöltés
            BufferedReader br = new BufferedReader(new FileReader(new File("felhasznalok.txt")));
            br.readLine();
            while(br.ready()){
                
                String sor = br.readLine();
                String[] spl = sor.split(";");
                List<Jatek> vasaroltJatekok = new ArrayList();
                if(spl.length > 7){
                    String[] jatekokSpl = spl[7].split(",");
                    
                    for(String jatek: jatekokSpl){
                        String[] jatekAdatokSpl = jatek.split("\\*");                        
                        vasaroltJatekok.add(new Jatek(jatekAdatokSpl[0], jatekAdatokSpl[1], Integer.parseInt(jatekAdatokSpl[2]), Integer.parseInt(jatekAdatokSpl[3]),jatekAdatokSpl[4],Integer.parseInt(jatekAdatokSpl[5])));
                    }
                }
                
                this.felhasznalok.add(new Felhasznalo(spl[0], spl[1], spl[2], Integer.parseInt(spl[3]), Integer.parseInt(spl[4]), Boolean.valueOf(spl[5]), Boolean.valueOf(spl[6]),vasaroltJatekok));
                
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        } catch (IOException ex) {
            Logger.getLogger(FelhasznaloTarolo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
}
