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

public class Bolt {

    private List<Jatek> jatekok;
    
    public Bolt() {
        jatekok = new ArrayList();
        inicializalas();
    }
    
    public Jatek getJatek(int index){
        return this.jatekok.get(index);
    }
    
    public void ujJatek(){
        
    }
    
    public void jatekTorles(){
        
    }
    
    public void jatekModositas(){
        
    }
    
    public void jatekokKilistazasa(){
        int i = 1;
        for (Jatek jatek : this.jatekok) {
            System.out.println(i+". " + jatek);
            i++;
        }
    }
    
    public void inicializalas(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(new File("jatekok.txt")));
            br.readLine();
            while(br.ready()){
                String sor = br.readLine();
                String[] spl = sor.split("\\*");
                this.jatekok.add(new Jatek(spl[0], spl[1], Integer.parseInt(spl[2]), Integer.parseInt(spl[3]), spl[4], Integer.parseInt(spl[5])));
                
            }
            br.close();
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getLocalizedMessage());
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

    public List<Jatek> getJatekok() {
        return jatekok;
    }
    
    
    
    
}
