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
                    String regVagynem = "";
                    Scanner scan = new Scanner(System.in);
                    do {                      
                        System.out.print("Szeretne regisztrálni? (y/n): ");
                        regVagynem = scan.nextLine();
                    } while (!(regVagynem.toLowerCase().equals("y") || regVagynem.toLowerCase().equals("n")));
                    if(regVagynem.toLowerCase().equals("y")){                        
                        regisztracio();                      
                        sikeresBelepesE = true;                  
                    }                          
                    
                }else{
                    if(this.felhasznaloTarolo.getFelhasznalok().get(i).isBanned()){
                        System.out.println("Sikertelen belépés, ez a fiók bannolva lett.");
                    }else{
                        this.belepettFelhasznalo =  this.felhasznaloTarolo.getFelhasznalok().get(i);
                        System.out.println("Sikeres belépés, üdvözöljük "+this.felhasznaloTarolo.getFelhasznalok().get(i).getFelhasznaloNev() + "!");
                        sikeresBelepesE = true;
                        menuPontFuttatas();
                    }
                    
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
        
        
        if(this.belepettFelhasznalo.isAdmin()){
            System.out.println("4 - Felhasználók kezelése");
            System.out.println("5 - Játékok kezelése");
        }
        System.out.println("0 - Kilépés");                
    }
    
    private void ujJatekVasarlasMain(){
        //játékok kilistázása
        bolt.jatekokKilistazasa();
        //kiválasztás
        int jatekID = -1;
        while(!(jatekID > 0 && jatekID <= bolt.getJatekok().size() )){
            jatekID = szamBekert("Játék sorszáma");
            if((jatekID < 0 || jatekID > bolt.getJatekok().size() )){
                System.out.println("Rossz sorszám");                
            }
        }
                        
        jatekID--;
        
        Jatek kivalasztottJatek = this.bolt.getJatek(jatekID);
        boolean megvanEaFelhasznalonak = false;
        for(Jatek jatek : belepettFelhasznalo.getMegvasaroltJatekok()){
            if(jatek.getNev().equals(kivalasztottJatek.getNev())){
                megvanEaFelhasznalonak = true;
            }
        }
        if(!megvanEaFelhasznalonak){
             if(kivalasztottJatek.getAr() >= belepettFelhasznalo.getEgyenleg()){
            System.out.println("Nincs elegendő összeg az egyenlegén. A tranzakció nem hajtható végre");
            }else{
                belepettFelhasznalo.getMegvasaroltJatekok().add(kivalasztottJatek);
                belepettFelhasznalo.setEgyenleg(belepettFelhasznalo.getEgyenleg() - kivalasztottJatek.getAr());
                System.out.println("Sikeres tranzakció!");
            }
        }else{
            System.out.println("Ez a játék már szerepel a felhasználó játékai között!");
        }
               
    }
    
    
    private void menuPontFuttatas(){        
        int opcio = -1;
        while(opcio != 0){
           menuPontok();
            opcio = szamBekert("Menüpont sorszáma");
        
            switch(opcio){
                case 1 : //ujJatek vasarlas
                    ujJatekVasarlasMain();
                    break;
                case 2 : //jatekaimKezelese
                    jatekKezelesMain();
                    break;
                case 3 : //penzfeltoltes
                    penzfeltoltesMain();
                    break;
                case 4 ://felhasznalok kezelese
                     if(this.belepettFelhasznalo.isAdmin()){
                         felhasznalokKezeleseMain();
                     }else{
                        System.out.println("Hibás menüpnt");
                    }
                    break;
                case 5 : //jatekok kezelese
                    if(this.belepettFelhasznalo.isAdmin()){
                         jatekokKezeleseMain();
                     }else{
                        System.out.println("Hibás menüpnt");
                    }
                    break;
                case 0 : //ujJatek vasarlas
                    System.out.println("Kilépés");
                    break;
                default:
                    System.out.println("Hibás menüpnt");
                    break;
            }
        }
    }
    
    
    private void start(){
       
         int felhasznaloInput = -1;
        
        while(felhasznaloInput != 0){        
        System.out.println("Van már létező profilja?");
        System.out.println("1 - Igen, Belépés");
        System.out.println("2 - Nincs, Regisztráció");
        System.out.println("0 - Kilépés\n");
        
            felhasznaloInput = szamBekert("Kérem adja meg a menüpont sorszámát");
            switch(felhasznaloInput){
            case 0:
                System.out.println("Viszlát!");
                break;
            case 1:
                belepes();
                break;
            case 2:
                regisztracio();                
                start();
                felhasznaloInput = 0;
                break;
            default:
                System.out.println("Hibás bevitel!");
                break;
            }
        }
    }
    
    private void save(){
        try {
            FileWriter fwFelhasznalok = new FileWriter(new File("felhasznalok.txt"));
            fwFelhasznalok.write("felhasznalonev;jelszo;teljesNev;kor;egyenleg;admin;bannolt\n");
            for(Felhasznalo fh : this.felhasznaloTarolo.getFelhasznalok()){
                fwFelhasznalok.write(fh.fileWriteString()+"\n");
            }
            fwFelhasznalok.close();
            
            FileWriter fwJatekok = new FileWriter(new File("jatekok.txt"));
            fwJatekok.write("nev;stilus;korhatar;ar;leiras;jatszottOrak\n");
            for (Jatek jatek : this.bolt.getJatekok()) {
                fwJatekok.write(jatek.fileWriteString()+"\n");
            }
            fwJatekok.close();
        } catch (IOException ex) {
            Logger.getLogger(Keretrendszer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int szamBekert(String uzenet){                
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

    private void penzfeltoltesMain() {
        System.out.println("Töltse fel egyenlegét!");
        int osszeg = -1;
        while(osszeg <= 0){
            osszeg = szamBekert("Írja be a feltölteni kívánt összeget"); 
            if(osszeg < 0){
                System.out.println("Negatív összeg, kérem adja meg újra!");
            }
        }
        Scanner scan = new Scanner(System.in);
        String bankkartyaSzam="";
        String kartyaTulajNeve="";
        int lejaratiDatumHonap;
        int lejaratiDatumEv;
        String cvc="";

        do {                
            System.out.print("Bankkártya száma: ");
            bankkartyaSzam = scan.nextLine();  
        } while (!(bankkartyaSzam.length()>0 && bankkartyaSzam.length()==16 && bankkartyaSzam.matches("[0-9]+") ));

        do {                
            System.out.print("Kártya tulajdonos neve: ");
            kartyaTulajNeve = scan.nextLine();
        } while (kartyaTulajNeve.length() == 0);

        do {                
            lejaratiDatumHonap = szamBekert("Lejárati dátum hónapja (hh)");
        } while (!(lejaratiDatumHonap > 0 && lejaratiDatumHonap <= 12));

        do {                
            lejaratiDatumEv = szamBekert("Lejárati dátum éve (éé)");
        } while (!(lejaratiDatumEv > 0 && lejaratiDatumEv <= 99));

        do {                
            System.out.print("3 jegyű CVC kód: ");
            cvc = scan.nextLine();
        } while (cvc.length() != 3 || !cvc.matches("[0-9]+"));

        this.belepettFelhasznalo.penzfeltoltes(osszeg);
        System.out.println("Sikeres pénzfeltöltés! Jelenlegi egyenlege: " + belepettFelhasznalo.getEgyenleg());
        
    }

    private void jatekKezelesMain() {
        
        int valasztottJatekIndex = -1;                
        
        if(!this.belepettFelhasznalo.getMegvasaroltJatekok().isEmpty()){  
            this.belepettFelhasznalo.jatekokKilistazasa();
            String jatekVagyNem = "";
            Scanner scan = new Scanner(System.in);
            do {      
                System.out.println("Szeretne-e játszani?(y/n): ");     
                jatekVagyNem = scan.nextLine();
            } while (!(jatekVagyNem.toLowerCase().equals("y") || jatekVagyNem.toLowerCase().equals("n")));

            if(jatekVagyNem.equals("y")){            
                do {            
                    valasztottJatekIndex = szamBekert("Játék sorszáma");
                } while (valasztottJatekIndex > belepettFelhasznalo.getMegvasaroltJatekok().size() || valasztottJatekIndex <= 0);

                System.out.println("A választott játék: "+this.belepettFelhasznalo.getMegvasaroltJatekok().get(valasztottJatekIndex-1).getNev());
                int jatszaniKivantOraSzam = szamBekert("Ennyi órát szeretnék játszani");
                this.belepettFelhasznalo.getMegvasaroltJatekok().get(valasztottJatekIndex-1).jatszas(jatszaniKivantOraSzam);
            }
        }else{
            System.out.println("Nincs játék a könyvtárban");
        }
    }

    private void felhasznalokKezeleseMain() {
        System.out.println("Felhasználók kezelése");
        Scanner scan = new Scanner(System.in);
        int opcio = -1;
        do {            
            System.out.println("1. Felhasználók kilistázása");
            System.out.println("2. Felhasználó bannolása");
            System.out.println("3. Felhasználó Vissza aktiválása");
            System.out.println("0. Vissza");
            opcio = szamBekert("Menüpont sorszáma");
            
            switch(opcio){
                case 1:
                    this.felhasznaloTarolo.felhasznalokKilistazas();
                break;
                case 2:
                    this.felhasznaloTarolo.felhasznalokKilistazas();
                    System.out.println();

                    String banFnev = "";
                    do {        
                        System.out.print("Bannolni kívánt felhasználó felhasználóneve: ");
                        banFnev = scan.nextLine();
                    } while ( !this.felhasznaloTarolo.ban(banFnev));                   
                break;
                case 3:
                   this.felhasznaloTarolo.felhasznalokKilistazas();
                    System.out.println();
                   String unbanFnev = "";
                    do {        
                        System.out.print("Aktiválni kívánt felhasználó felhasználóneve: ");
                        unbanFnev = scan.nextLine();
                    } while ( !this.felhasznaloTarolo.unban(unbanFnev));
                break;
                case 0:
                    System.out.println();
                break;
                
            }
        } while (opcio != 0);
        
    }

    private void jatekokKezeleseMain() {
        int opcio =  -1;
        do {            
            System.out.println("1. Minden játék kilistázása");
            System.out.println("2. Új játék létrehozása");
            System.out.println("3. Játék törlése");
            System.out.println("0. Vissza");
            System.out.println();
            opcio  = szamBekert("Menüpont sorszáma");
            switch(opcio){
                case 1://jatekok listazasa
                    this.bolt.jatekokKilistazasa();
                break;
                case 2://uj jatek letrehozasa
                    ujJatekLetrehozMain();                    
                break;
                case 3://uj jatek módosítása
                    this.bolt.jatekokKilistazasa();
                    int jatekSorszam = -1;
                    do {                        
                        jatekSorszam = szamBekert("Törölni kívánt játék sorszáma");
                    } while (!(jatekSorszam>0 && jatekSorszam < bolt.getJatekok().size()));
                    this.bolt.jatekTorles(jatekSorszam);
                break;
                case 0:
                    System.out.println();
                break;
            }
        } while (opcio != 0);
        
        
    }   

    private void ujJatekLetrehozMain() {
        Scanner scan = new Scanner(System.in);                    
        String nev;
        do {         
            System.out.print("Játék neve: ");
             nev = scan.nextLine();
             if(nev.length() == 0){
                 System.out.println("Adja meg a játék nevét!");
             }
        } while (nev.length() == 0);


        String stilus = "";
         do {       
             System.out.print("Játék stílusa: ");
             stilus = scan.nextLine();
             if(stilus.length() == 0){
                 System.out.println("Adja meg a játék stílusát!");
             }
        } while (stilus.length() == 0);
         
         String leiras = "";
         do {       
             System.out.print("Játék leírása: ");
             leiras = scan.nextLine();
             if(leiras.length() == 0){
                 System.out.println("Adja meg a játék leírását!");
             }
        } while (leiras.length() == 0);

        int korhatar = szamBekert("Korhatár");
        int ar = szamBekert("Ár");
        this.bolt.ujJatek(nev, stilus, korhatar, ar,leiras);
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
            while(i < this.felhasznaloTarolo.getFelhasznalok().size() && !fnev.equals(this.felhasznaloTarolo.getFelhasznalok().get(i).getFelhasznaloNev())){
                i++;
            }
            if(i == this.felhasznaloTarolo.getFelhasznalok().size()){
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
        
        
       Felhasznalo ujFelhasznalo = new Felhasznalo(fnev, jelszo,"", 0);
        this.belepettFelhasznalo = ujFelhasznalo;
        penzfeltoltesMain();

        System.out.println("Sikeres regisztráció!");
        this.felhasznaloTarolo.getFelhasznalok().add(ujFelhasznalo);
        menuPontFuttatas();
    }
   
    
}
