package videojatekbolt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FelhasznaloTarolo {
    
    private List<Felhasznalo> felhasznalok;

    public FelhasznaloTarolo() {
        felhasznalok = new ArrayList();
        init();
    }
    
        
    public void regisztracio(){
        Scanner scan  =  new Scanner(System.in);
        System.out.println("Regisztráció");
        String fnev = "?";
        
        boolean letezikIlyenNevuFelhasznalo = true;
        
        while(letezikIlyenNevuFelhasznalo){
            //letezikIlyenNevuFelhasznalo = false;
            System.out.print("Felhasználó név: ");
            fnev = scan.nextLine();
            int i = 0;
            while(i < this.felhasznalok.size() && !fnev.equals(this.felhasznalok.get(i).getFelhasznaloNev())){
                i++;
            }
            if(i == this.felhasznalok.size()){
                letezikIlyenNevuFelhasznalo = false;
            }else{
                System.out.println("Ez a felhasználónév már foglalt!");
            }
        }

        String jelszo = "";
        String megerosit = "megerosites";
 
        int kor = -1;
        while(!jelszo.equals(megerosit)){
            System.out.print("Jelszó: ");
            jelszo = scan.nextLine();
            System.out.print("Jelszó mégegyszer: ");
            megerosit = scan.nextLine();
            if(!jelszo.equals(megerosit)){
                System.out.println("A két jelszó nem egyezik meg!");
            }
        }
        /*
        try {
          */  
        while(kor != -1){
            System.out.println("Kor: ");
            String sKor = scan.nextLine();
            kor = Integer.parseInt(sKor);

            if(kor == -1){
                System.out.println("Nem szám!");
            }
            if(kor<0){
                kor=10;
            }
        }
        /*
        }catch(ParseException ex){
            System.out.println("Nem szám");
        }*/
        System.out.println("Sikeres regisztráció!");
        
        this.felhasznalok.add(new Felhasznalo(fnev, jelszo,"", 0));
    }
    
    public void ban(Felhasznalo jatekos){
        jatekos.setBan(true);
    }
    
    public void felhasznalokKilistazas(){
        for(Felhasznalo felh : this.felhasznalok){
            System.out.println(felh);
            //System.out.println(felh.getFelhasznaloNev());//véglegesnél csak a felhasználó nevet irja ki
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
                        //new Jatek(sor, sor, 0, 0, sor, 0)
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
