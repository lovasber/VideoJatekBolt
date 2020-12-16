package videojatekbolt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Keretrendszer {
    private Felhasznalo belepettFelhasznalo;
    private FelhasznaloTarolo felhasznaloTarolo;
    private Bolt bolt;

    public Keretrendszer() {
        this.felhasznaloTarolo = new FelhasznaloTarolo();
        this.bolt = new Bolt();
    }
    
    public static void main(String[] args) {
        Keretrendszer keretrendszer =  new Keretrendszer();
        keretrendszer.start();        
        keretrendszer.save();
        //int a = keretrendszer.szamBekert("Index");
    }
     
    private void belepes() {
        boolean sikeresBelepesE = false;
        while(!sikeresBelepesE){
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));            
                System.out.println("Belépés");
                System.out.print("Felhasználó név: ");
                String fnev="";
                String jelszo="";

                fnev = br.readLine();
                System.out.print("Jelszó: ");
                jelszo = br.readLine();

                int i = 0;
                while(i < this.felhasznaloTarolo.getFelhasznalok().size() && !(this.felhasznaloTarolo.getFelhasznalok().get(i).getFelhasznaloNev().equals(fnev) && this.felhasznaloTarolo.getFelhasznalok().get(i).getJelszo().equals(jelszo))){
                    i++;
                }
                if(i == this.felhasznaloTarolo.getFelhasznalok().size()){
                    System.out.println("Hibás felhasználónév vagy jelszó!");
                }else{
                    this.belepettFelhasznalo =  this.felhasznaloTarolo.getFelhasznalok().get(i);
                    System.out.println("Sikeres belépés, üdvözöljük "+this.felhasznaloTarolo.getFelhasznalok().get(i).getFelhasznaloNev() + "!");
                    sikeresBelepesE = true;
                    menuPontok();
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            } 
        }
    }
    
    private void menuPontok(){
        System.out.println("1 - Új játék vásárlása");
        System.out.println("2 - Játékaim kezelése");
        System.out.println("3 - Pénzfeltöltés");
        System.out.println("4 - Saját profil szerkesztése");
        
        if(this.belepettFelhasznalo.isAdmin()){
            System.out.println("5 - Felhasználók kezelése");
            System.out.println("6 - Játékok kezelése");
        }
        System.out.println("0 - Kilépés");
        
        menuPontFuttatas();
    }
    
    private void ujJatekVasarlas(){
        //játékok kilistázása
        bolt.jatekokKilistazasa();
        //kiválasztás
        int jatekID = szamBekert("Játék sorszáma");
        //létezik e a játék? jó e az id?

        jatekID--;
        Jatek kivalasztottJatek = this.bolt.getJatek(jatekID);
        if(kivalasztottJatek.getAr() >= belepettFelhasznalo.getEgyenleg()){
            System.out.println("Nincs elegendő összeg az egyenlegén. A tranzakció nem hajtható végre");
        }else{
            belepettFelhasznalo.getMegvasaroltJatekok().add(kivalasztottJatek);
            belepettFelhasznalo.setEgyenleg(belepettFelhasznalo.getEgyenleg() - kivalasztottJatek.getAr());
            System.out.println("Sikeres tranzakció!");
        }
    }
    
    
    private void menuPontFuttatas(){
        Scanner scan = new Scanner(System.in);
        int opcio = -1;
        while(opcio != 0){
            opcio = szamBekert("Menüpont sorszáma");
        
        switch(opcio){
            case 1 : //ujJatek vasarlas
                ujJatekVasarlas();
                break;
            case 2 : //jatekaimKezelese
                break;
            case 3 : //penzfeltoltes
                break;
            case 4 : //profil szerkesztées
                break;
            case 5 ://felhsaznalok kezelese
                 if(this.belepettFelhasznalo.isAdmin()){
                 
                 }else{
                    System.out.println("Hibás menüpnt");
                }
                break;
            case 6 : //jatekok kezelese
                if(this.belepettFelhasznalo.isAdmin()){
                 
                 }else{
                    System.out.println("Hibás menüpnt");
                }
                break;
            default:
                System.out.println("Hibás menüpnt");
                break;
            }
        }
    }
    
    private void inicializalas(){
        
    }
    
    private void start(){
        System.out.println("Üdvözüljük!");
        System.out.println("Van már létező profilja?");
        System.out.println("1 - Igen, Belépés");
        System.out.println("2 - Nincs, Regisztráció");
        System.out.println("0 - Kilépés");
        
        int felhasznaloInput = szamBekert("Kérem adja meg a menüpont sorszámát");
        
        switch(felhasznaloInput){
            case 0:
                System.out.println("Viszlát!");
                break;
            case 1:
                belepes();
                break;
            case 2:
                felhasznaloTarolo.regisztracio();
                //TODO:
               //this.belepettFelhasznalo.penzfeltoltes(felhasznaloInput);
                start();
                break;
            default:
                System.out.println("Hibás bevitel!");
                break;
        }
        
    }
    
    private void save(){
        try {
            FileWriter fwFelhasznalok = new FileWriter(new File("felhasznalok.txt"));
            fwFelhasznalok.write("felhasznalonev;jelszo;teljesNev;kor;egyenleg;admin;bannolt\n");
            for(Felhasznalo fh : this.felhasznaloTarolo.getFelhasznalok()){
                fwFelhasznalok.write(fh.fileWriteString());
            }
            fwFelhasznalok.close();
            
            FileWriter fwJatekok = new FileWriter(new File("jatekok.txt"));
            fwJatekok.write("nev;stilus;korhatar;ar;leiras;jatszottOrak\n");
            for (Jatek jatek : this.bolt.getJatekok()) {
                fwJatekok.write(jatek.fileWriteString());
            }
            fwJatekok.close();
        } catch (IOException ex) {
            Logger.getLogger(Keretrendszer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    int szamBekert(String uzenet){                
        int beirtSzam = Integer.MIN_VALUE;
        Scanner scan = new Scanner(System.in);
        while( beirtSzam == Integer.MIN_VALUE){
            try {
                System.out.print(uzenet+": ");
                beirtSzam =  Integer.parseInt(scan.nextLine());                
            } catch (Exception e) {                
                System.out.println("Nem szám!");
                beirtSzam = Integer.MIN_VALUE;
            }                                    
        }        
        return beirtSzam;
    }
    
}
