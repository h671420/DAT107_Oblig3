package tekstgrensesnitt;

import DAO.*;
import entities.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

import static tekstgrensesnitt.Statics.*;

public class Tekstgrensesnitt {
    //TODO: legge inn menyvalg 5 "legg til en ny medarbeider" i avdelingadministrasjon (evt stjel en ansatt + map for å velge en å kvitte seg med
    //TODO: legg inn en 'tøm avdeling' rutine som kjøres når en avdeling slettes

    //TODO: se til at ingen deprecated Statics metoder brukes
    //TODO: se til at alle kall på velgentitet() har optimale liste
    //TODO: få inn rolle i prosjekt entitetsmeny info

    //<editor-folding desc="objektvariabler"
    private final AnsattDAO ansDAO = new AnsattDAO();
    private final AvdelingDAO avdDAO = new AvdelingDAO();
    private final ProsjektDAO proDAO = new ProsjektDAO();
    private final ProsjektDeltakelseDAO proDDao = new ProsjektDeltakelseDAO();
    static Scanner scanner = new Scanner(System.in);

    //</editor-folding>
    public static void main(String[] args) {
        Tekstgrensesnitt tk = new Tekstgrensesnitt();
        tk.hovedMeny();
    }

    public void hovedMeny() {
        boolean running = true;
        while (running) {
            visHovedMeny();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    entiteterMeny(Ansatt.class);
                    break;
                case "2":
                    entiteterMeny(Avdeling.class);
                    break;
                case "3":
                    entiteterMeny(Prosjekt.class);
                    break;
                case "4":
                    System.out.println("Ha en fortsatt fin dag!");
                    running = false;
            }
        }
    }

    private void visHovedMeny() {
        clearConsole();
        System.out.println("Velkommen til DBAdmin!\n\nVennligst velg en av følgende:");
        System.out.println("\t1. Administrer ansatte");
        System.out.println("\t2. Administrer avdelinger");
        System.out.println("\t3. Administrer prosjekter");
        System.out.println("\t4. Avslutt DBAdmin");
    }

    private <T extends asd> void entiteterMeny(Class<T> entitetsKlasse) {
        List<T> entiteter;
        Dao dao = null;
        Runnable leggTil = null;
        Consumer<Integer> administrer = null;
        switch (entitetsKlasse.getSimpleName()) {
            case "Ansatt":
                dao = new AnsattDAO();
                leggTil = this::leggTilAnsattRutine;
                administrer = this::administrerAnsattMeny;
                break;
            case "Avdeling":
                dao = new AvdelingDAO();
                leggTil = this::leggTilAvdelingRutine;
                administrer = this::administrerAvdelingMeny;
                break;
            case "Prosjekt":
                dao = new ProsjektDAO();
                leggTil = this::leggTilProsjektRutine;
                administrer = this::administrerProsjektMeny;
                break;
            case "ProsjektDeltakelse":
                dao = new ProsjektDeltakelseDAO();
                break;
        }
        boolean running = true;
        while (running) {
            entiteter = dao.finnAlle();
            visEntiteterMeny(entiteter, entitetsKlasse);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    leggTil.run();
                    break;
                default:
                    Map<String, Integer> entitetId = new HashMap<>();
                    for (T t : entiteter)
                        entitetId.put(t.getEntId(), t.getId());
                    if (entitetId.containsKey(input))
                        administrer.accept(entitetId.get(input));
                    break;
            }
        }
    }

    private <T extends asd> void visEntiteterMeny(List<T> entiteter, Class<T> entitetsKlasse) {
        clearConsole();
        String enTallNy = "";
        String enTall = "";
        String flerTall = "";
        switch (entitetsKlasse.getSimpleName()) {
            case "Ansatt":
                enTallNy = "en ny ansatt";
                enTall = "en ansatt";
                flerTall = "ansatte";
                break;
            case "Avdeling":
                enTallNy = "en ny avdeling";
                enTall = "en avdeling";
                flerTall = "avdelinger";
                break;
            case "Prosjekt":
                enTallNy = "et nytt prosjekt";
                enTall = "et prosjekt";
                flerTall = "prosjekt";
                break;
            case "ProsjektDeltakelse":
                enTallNy = "en ny prosjektdeltakelse";
                enTall = "en prosjektdeltakelse";
                flerTall = "prosjektdeltakelser";
                break;
        }
        if (entiteter.size() == 0)
            System.out.println("Databasen inneholder ingen " + flerTall + "\n\nVennligst velg en av følgende:");
        if (entiteter.size() == 1)
            System.out.println("Databasen inneholder kun " + enTall + "\n\nReturner ID for å administrere, eller velg en av følgende:");
        if (entiteter.size() > 1)
            System.out.println("Databasen inneholder " + entiteter.size() + " " + flerTall + "\n\nReturner en ID for å administrere, eller velg en av følgende:");
        System.out.println("\t1. Returner til hovedmeny");
        System.out.println("\t2. Registrer " + enTallNy);
        if (!entiteter.isEmpty())
            System.out.println(deler());
        for (T t : entiteter)
            System.out.println("\t" + t.entiteterMenyInfo());
    }

    private void administrerAnsattMeny(int ansId) {
        boolean running = true;
        while (running) {
            Ansatt ansatt = ansDAO.finn(ansId);
            visAdministrerAnsattMeny(ansatt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    try {
                        ansDAO.slett(ansatt);
                        running = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("prosjektdeltakelse_ans_id_fkey"))
                            System.out.println("Kan ikke slette den ansatte pga deltakelse i prosjekt");
                        else if (e.getMessage().contains("avdeling_sjef_id_fkey"))
                            System.out.println("Den ansatte er en avdelingsleder og kan ikke slettes");
                        else
                            e.printStackTrace();
                        scanner.nextLine();
                    }

                    break;
                case "3":
                    if (ansatt.erSjef()) {
                        System.out.println(ansatt.getFornavn() + " " + ansatt.getEtternavn() + " er sjef for avdeling " + ansatt.getAvdeling().getNavn() + ", og kan derfor ikke bytte avdeling.");
                        scanner.nextLine();
                        break;
                    }
                    Avdeling avdeling = velgEntitetRutine(avdDAO.finnAlle(), Avdeling.class); //TODO: lage en daometode som returnere avdelinger bortsett fra den som den ansatte allerede er tilknyttet
                    if (avdeling != null)
                        ansDAO.flyttAnsatt(ansatt, avdeling);
                    break;
                case "4":
                    administrerAnsattDetaljerMeny(ansatt.getId());
                    break;
                case "5":
                    leggTilDeltakelseRutine(Ansatt.class,ansatt);
                    break;
                default:
                    Map<String,Integer> proIder = new HashMap<>();
                    for (ProsjektDeltakelse pd:ansatt.getProsjekter())
                        proIder.put(pd.getProsjekt().getEntId(), pd.getId());
                    if (proIder.containsKey(input))
                        administrerProsjektDeltakelseMeny(proIder.get(input));


/*                case "4":
                    administrerProsjektDeltakelserMeny(ansatt.getId(), null);
                    break;
                case "5":
                    administrerAnsattDetaljerMeny(ansatt.getId());
                    break;*/
            }
        }
    }

    private void visAdministrerAnsattMeny(Ansatt ansatt) {
        clearConsole();
        System.out.println("Generell administrasjon av ansatt " + ansatt.getNavn() + ", ID: " + ansatt.getEntId());
        for (String s : ansatt.administrasjonsMenyInfo())
            System.out.println(s);
        System.out.println("\nVennligst returner en av følgende");
        System.out.println("\t1. Returner til ansattemeny");
        System.out.println("\t2. Slett ansatt");
        System.out.println("\t3. Bytt avdeling");
        System.out.println("\t4. Rediger navn, lønn eller stillingsbeskrivelse");
        if (!ansatt.getProsjekter().isEmpty())
            System.out.println("\t5. Legg til en ny prosjektdeltakelse, eller returner en prosjektID for å administrere en eksisterende prosjektdeltakelse");
        else
            System.out.println("\t5. Legg til en ny prosjektdeltakelse");

    }

    private void administrerAnsattDetaljerMeny(int ansId) {
        boolean running = true;
        while (running) {
            Ansatt ansatt = ansDAO.finn(ansId);
            visAdministrerAnsattDetaljerMeny(ansatt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    ansatt.setFornavn(Statics.getStringInput2(2, "et", "fornavn", true));
                    if (ansatt.getFornavn()!=null)
                        ansDAO.oppdater(ansatt);
                    break;
                case "3":
                    ansatt.setEtternavn(Statics.getStringInput2(2, "et", "etternavn", true));
                    if (ansatt.getEtternavn()!=null)
                        ansDAO.oppdater(ansatt);
                    break;
                case "4":
                    ansatt.setAnsDato(Statics.getDateInput2("ny ansettelsesdato"));
                    if (ansatt.getAnsDato()!=null)
                        ansDAO.oppdater(ansatt);
                    break;
                case "5":
                    ansatt.setMndLonn(Statics.getIntegerInput2("en", "ny månedslønn"));
                    if (ansatt.getMndLonn()!=null)
                        ansDAO.oppdater(ansatt);
                    break;
                case "6":
                    ansatt.setStilling(Statics.getStringInput2(4, "en","ny stillingsbeskrivelse",true));
                    if (ansatt.getStilling()!=null)
                        ansDAO.oppdater(ansatt);
                    break;
            }
        }

    }

    private void visAdministrerAnsattDetaljerMeny(Ansatt ansatt) {
        clearConsole();
        System.out.println("Administrasjon detaljer for ansatt " + ansatt.getNavn() + ", ID: " + ansatt.getEntId());
        for (String s : ansatt.administrasjonsMenyInfo())
            System.out.println(s);
        System.out.println("\nVennligst returner en av følgende");
        System.out.println("\t1. Returner til generell administrasjon");
        System.out.println("\t2. Rediger fornavn");
        System.out.println("\t3. Rediger etternavn");
        System.out.println("\t4. Rediger ansettelsesdato");
        System.out.println("\t5. Juster månedslønn");
        System.out.println("\t6. Rediger stillingsbeskrivelse");
    }

    private void administrerAvdelingMeny(int avdId) {
        boolean running = true;
        while (running) {
            Avdeling avdeling = avdDAO.finn(avdId);
            visAdministrerAvdelingMeny(avdeling);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    System.out.println("Avdelingens ansatte vil bli overført til 'Nyansatte' hvis avdelingen slettes" +
                            "\nVennligst bekreft sletting ved å returnere 'SLETT', trykk enter for å avbryte. ");
                    input = scanner.nextLine();
                    if (input.equals("SLETT")) {
                        avdDAO.slett(avdeling);
                        running = false;
                    }
                    break;
                case "3":
                    /*String navn = Statics.getStringInput(4, "et", "avdelingsnavn", true);
                    if (navn != null)
                        avdDAO.endreNavn(avdId, navn);*/
                    avdeling.setNavn(Statics.getStringInput(4, "et", "avdelingsnavn", true));
                    if (avdeling.getNavn() != null)
                        avdDAO.oppdater(avdeling);
                    break;
                case "4":
                    Ansatt nySjef = velgEntitetRutine(ansDAO.finnAnsatteIkkeSjef(), Ansatt.class);
                    if (nySjef != null)
                        avdDAO.endreSjef(avdId, nySjef.getId());
                    break;
                case "5":
                    List<Ansatt> kandidatAnsatte = avdeling.getAnsatte(); //TODO: finne måte å hente ansatt fra avdeling som ikke er sjef
                    kandidatAnsatte.remove(avdeling.getSjef());
                    List<Avdeling> kandidatAvdelinger = avdDAO.finnAlle();
                    kandidatAvdelinger.remove(avdeling);
                    if (kandidatAnsatte.isEmpty()) {
                        System.out.println("Avdelingen har ingen ansatte som kan omplasseres");
                        break;
                    }
                    if (kandidatAvdelinger.isEmpty()) {
                        System.out.println("Det finnes ingen andre avdelinger");
                        break;
                    }
                    Ansatt ansatt = velgEntitetRutine(kandidatAnsatte, Ansatt.class);
                    Avdeling nyAvdeling = velgEntitetRutine(kandidatAvdelinger, Avdeling.class);
                    if (ansatt != null && nyAvdeling != null)
                        ansDAO.flyttAnsatt(ansatt, nyAvdeling);
                    break;
            }
        }
    }

    private void visAdministrerAvdelingMeny(Avdeling avdeling) {
        clearConsole();
        System.out.println("Administrasjon av avdeling");
        for (String s : avdeling.administrasjonsMenyInfo())
            System.out.println(s);
        System.out.println();
        System.out.println("Vennligst returner en av følgende");
        System.out.println("\t1. Returner til avdelingermeny");
        System.out.println("\t2. Slett avdeling");
        System.out.println("\t3. Rediger avdelingsnavn");
        System.out.println("\t4. Finn en ny avdelingsleder");
        System.out.println("\t5. Flytt en ansatt til en annen avdeling");
    }

    private void administrerProsjektMeny(int prosjId) {
        boolean running = true;
        while (running) {
            Prosjekt prosjekt = proDAO.finn(prosjId);
            visAdministrerProsjektMeny(prosjekt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    try {
                        proDAO.slett(prosjekt);
                        running = false;
                    } catch (Exception e) {
                        if (e.getMessage().contains("prosjektdeltakelse_prosj_id_fkey"))
                            System.out.println("Kan ikke slette prosjektet siden ansatte er tilknyttet");
                        else
                            e.printStackTrace();
                        scanner.nextLine();
                    }
                    break;
                case "3":
                    prosjekt.setNavn(Statics.getStringInput(4, "et", "prosjektnavn", true));
                    if (prosjekt.getNavn() != null)
                        proDAO.oppdater(prosjekt);
                    break;
                case "4":
                    prosjekt.setBeskrivelse(Statics.getStringInput(5, "en", "prosjektbeskrivelse", false));
                    if (prosjekt.getBeskrivelse() != null)
                        proDAO.oppdater(prosjekt);
                    break;
                case "5":
                    leggTilDeltakelseRutine(Prosjekt.class,prosjekt);
//                    administrerProsjektDeltakelserMeny(null, prosjekt.getId());
                    break;
                default:
                    Map<String,Integer> ansIder = new HashMap<>();
                    for (ProsjektDeltakelse pd:prosjekt.getDeltakelser())
                        ansIder.put(pd.getAnsatt().getEntId(),pd.getId());
                    if (ansIder.containsKey(input))
                        administrerProsjektDeltakelseMeny(ansIder.get(input));
            }
        }
    }

    private void visAdministrerProsjektMeny(Prosjekt prosjekt) {
        clearConsole();
        System.out.println("Administrasjon av prosjekt");
        for (String s : prosjekt.administrasjonsMenyInfo())
            System.out.println(s);
        System.out.println("Vennligst returner en av følgende");
        System.out.println("\t1. Returner til prosjektermeny");
        System.out.println("\t2. Slett prosjekt");
        System.out.println("\t3. Rediger prosjektnavn");
        System.out.println("\t4. Rediger prosjektbeskrivelse");
        if (!prosjekt.getDeltakelser().isEmpty())
            System.out.println("\t5. Legg til ett prosjektmedlem, eller returner en ansattID for å redigere deltakelse");
        else
            System.out.println("\t5. Legg til ett prosjektmedlem");
    }

    private void administrerProsjektDeltakelseMeny(Integer prosjektDeltakelseId) {
        boolean running = true;
        while (running) {
            ProsjektDeltakelse pd = proDDao.finn(prosjektDeltakelseId);
            visAdministrerProsjektDeltakelseMeny(pd);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    proDDao.slett(pd);
                    running = false;
                    break;
                case "3":
                    pd.setRolle(Statics.getStringInput(4, "en", "ny rolle", false));
                    if (pd.getRolle() != null)
                        proDDao.oppdater(pd);
                    break;
                case "4":
                    pd.setTimer(Statics.getIntegerInput("et", "timeantall"));
                    if (pd.getTimer() != null)
                        proDDao.oppdater(pd);
                    break;
            }
        }

    }

    private void visAdministrerProsjektDeltakelseMeny(ProsjektDeltakelse pd) {
        clearConsole();
        for (String s : pd.administrasjonsMenyInfo())
            System.out.println(s);
        System.out.println("\nVennligst returner en av følgende");
        System.out.println("\t1. Returner til forrige meny");
        System.out.println("\t2. Slett deltakelse");
        System.out.println("\t3. Endre rolle");
        System.out.println("\t4. Rediger timer");
    }

    private <T extends asd> T velgEntitetRutine(List<T> entiteter, Class<T> entitetsKlasse) {
        String enTall = "";
        String flerTall = "";
        switch (entitetsKlasse.getSimpleName()) {
            case "Ansatt":
                enTall = "en ansatt";
                flerTall = "ansatte";
                break;
            case "Avdeling":
                enTall = "en avdeling";
                flerTall = "avdelinger";
                break;
            case "Prosjekt":
                enTall = "ett prosjekt";
                flerTall = "prosjekt";
                break;
            case "ProsjektDeltakelse":
                enTall = "en prosjektdeltakelse";
                flerTall = "prosjektdeltakelser";
                break;
        }
        if (entiteter.isEmpty()) {
            System.out.println("Ingen aktuelle " + flerTall + " eksisterer");
            scanner.nextLine();
            return null;
        }
        T entitet = null;
        Map<String, T> entitetId = new HashMap<>();
        for (T t : entiteter)
            entitetId.put(t.getEntId(), t);
        boolean running = true;
        while (running) {
            System.out.println("Velg " + enTall + " - returner id eller '1' for å avbryte");
            for (T t : entiteter)
                System.out.println("\t"+t.entiteterMenyInfo());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (entitetId.containsKey(input)) {
                        entitet = entitetId.get(input);
                        running = false;
                    }
            }
        }
        return entitet;
    }

    private void leggTilAnsattRutine() {
        List<Ansatt> ansatte = ansDAO.finnAlle();
        Ansatt ny = new Ansatt();
        boolean avbrutt = false;
        String rutineHeader = "\nInnlesing av informasjon om ny ansatt";
        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        ny.setFornavn(getStringInput2(2, "et", "fornavn", true));
        if (ny.getFornavn() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader = "\nNavn: " + ny.getFornavn();

        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        ny.setEtternavn(getStringInput2(2, "et", "etternavn", true));
        if (ny.getEtternavn() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader += " " + ny.getEtternavn();

        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        ny.setStilling(getStringInput2(4, "en", "stilling", true));
        if (ny.getStilling() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader += " Stilling: " + ny.getStilling();

        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        if (avdDAO.finnAlle().isEmpty()){
            ny.setAvdeling(avdDAO.finn(1));
        } else {
            ny.setAvdeling(velgEntitetRutine(avdDAO.finnAlle(), Avdeling.class));
            if (ny.getAvdeling() == null) {
                System.out.print("Registrering ble avbrutt");
                scanner.nextLine();
                return;
            }
        }
        rutineHeader += " Avdeling: " + ny.getAvdeling().getNavn();

        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        ny.setMndLonn(getIntegerInput2("en", "månedslønn"));
        if (ny.getMndLonn() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader += " Månedslønn: " + ny.getMndLonn();

        visEntiteterMeny(ansatte, Ansatt.class);
        System.out.println(rutineHeader);
        ny.setAnsDato(getDateInput2("en ansettelsesdato"));
        if (ny.getAnsDato() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader += " Ansettelsesdato: " + ny.getAnsDato();

        boolean running = true;
        while (running) {
            visEntiteterMeny(ansatte, Ansatt.class);
            System.out.println(rutineHeader);
            System.out.println("\t" + "1. for å forkaste");
            System.out.println("\t" + "2. for legge til databasen");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    System.out.print("Registrering ble avbrutt");
                    scanner.nextLine();
                    break;
                case "2":
                    running = false;
                    ansDAO.leggTil(ny);
                    break;
            }
        }
    }

    private Avdeling leggTilAvdelingRutine() {
        List<Avdeling> avdelinger = avdDAO.finnAlle();
        Avdeling ny = new Avdeling();
        boolean avbrutt = false;
        String rutineHeader = "\nInnlesing av informasjon om ny avdeling";
        visEntiteterMeny(avdelinger, Avdeling.class);
        System.out.println(rutineHeader);
        ny.setNavn(getStringInput2(4, "et", "avdelingsnavn", true));
        if (ny.getNavn() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return null;
        }
        rutineHeader = "\nAvdelingsnavn: " + ny.getNavn();

        visEntiteterMeny(avdelinger, Avdeling.class);
        System.out.println(rutineHeader);
        ny.setSjef(velgEntitetRutine(ansDAO.finnAnsatteIkkeSjef(), Ansatt.class)); //TODO: finne en måte å lirke 'sjef' inn i stedet for 'ansatt'
        if (ny.getSjef() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return null;
        }
        rutineHeader += " Sjef: " + ny.getSjef().getNavn();

        boolean running = true;
        while (running) {
            visEntiteterMeny(avdelinger, Avdeling.class);
            System.out.println(rutineHeader);
            System.out.println("\t" + "1. for å forkaste");
            System.out.println("\t" + "2. for legge til databasen");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    System.out.print("Registrering ble avbrutt");
                    scanner.nextLine();
                    break;
                case "2":
                    running = false;
                    avdDAO.leggTil(ny);
                    break;
            }
        }
    return ny;
    }

    private void leggTilProsjektRutine() {
        List<Prosjekt> prosjekter = proDAO.finnAlle();
        Prosjekt ny = new Prosjekt();
        boolean avbrutt = false;
        String rutineHeader = "\nInnlesing av informasjon om nytt prosjekt";
        visEntiteterMeny(prosjekter, Prosjekt.class);
        System.out.println(rutineHeader);
        ny.setNavn(getStringInput2(4, "et", "prosjektnavn", true));
        if (ny.getNavn() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader = "\nProsjektnavn: " + ny.getNavn();

        visEntiteterMeny(prosjekter, Prosjekt.class);
        System.out.println(rutineHeader);
        ny.setBeskrivelse(getStringInput2(5, "en", "prosjektbeskrivelse", false));
        if (ny.getBeskrivelse() == null) {
            System.out.print("Registrering ble avbrutt");
            scanner.nextLine();
            return;
        }
        rutineHeader += " Beskrivelse: " + ny.getBeskrivelse();

        boolean running = true;
        while (running) {
            visEntiteterMeny(prosjekter, Prosjekt.class);
            System.out.println(rutineHeader);
            System.out.println("\t" + "1. for å forkaste");
            System.out.println("\t" + "2. for legge til databasen");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    System.out.print("Registrering ble avbrutt");
                    scanner.nextLine();
                    break;
                case "2":
                    running = false;
                    proDAO.leggTil(ny);
                    break;
            }
        }
    }

    private <T> void leggTilDeltakelseRutine(Class<T> entitetsKlasse, T entitet) {
        ProsjektDeltakelse ny = new ProsjektDeltakelse();
        String rutineHeader;
        boolean running = true;
        switch (entitetsKlasse.getSimpleName()) {
            case "Ansatt":
                ny.setAnsatt((Ansatt) entitet);
                ny.setProsjekt(velgEntitetRutine(proDAO.finnAlle(), Prosjekt.class));//TODO: må sile ut prosjekter som den ansatte allerede deltar i
                if (ny.getProsjekt() == null) {
                    System.out.print("Registrering ble avbrutt");
                    scanner.nextLine();
                    return;
                }
                rutineHeader = "\nAnsatt: " + ny.getAnsatt().getNavn() + " Prosjekt: " + ny.getProsjekt().getNavn();

                visAdministrerAnsattMeny((Ansatt) entitet);
                System.out.println(rutineHeader);
                ny.setRolle(Statics.getStringInput2(4, "en", "rolle", true));
                if (ny.getRolle() == null) {
                    System.out.print("Registrering ble avbrutt");
                    return;
                }
                rutineHeader += " Rolle: " + ny.getRolle();

                visAdministrerAnsattMeny((Ansatt) entitet);
                System.out.println(rutineHeader);
                ny.setTimer(Statics.getIntegerInput2("et", "timeantall"));
                if (ny.getTimer() == null) {
                    System.out.print("Registrering ble avbrutt");
                    return;
                }
                rutineHeader += " #Timer: " + ny.getTimer();
                while (running) {
                    visAdministrerAnsattMeny((Ansatt) entitet);
                    System.out.println(rutineHeader);
                    System.out.println("\t" + "1. for å forkaste");
                    System.out.println("\t" + "2. for legge til databasen");
                    String input = scanner.nextLine();
                    switch (input) {
                        case "1":
                            running = false;
                            System.out.print("Registrering ble avbrutt");
                            scanner.nextLine();
                            break;
                        case "2":
                            running = false;
                            proDDao.leggTil(ny);
                            break;
                    }
                }
                break;
            case "Prosjekt":
                ny.setAnsatt(velgEntitetRutine(ansDAO.finnAlle(), Ansatt.class));//TODO: må sile ut ansatte som allerede deltar i prosjektet
                ny.setProsjekt((Prosjekt) entitet);
                if (ny.getAnsatt() == null) {
                    System.out.print("Registrering ble avbrutt");
                    return;
                }
                rutineHeader = "\nAnsatt: " + ny.getAnsatt().getNavn() + " Prosjekt: " + ny.getProsjekt().getNavn();
//TODO: fikse slik at alle entitetet kommer ut sortert på id
                visAdministrerProsjektMeny((Prosjekt) entitet);
                System.out.println(rutineHeader);
                ny.setRolle(Statics.getStringInput2(4, "en", "rolle", true));
                if (ny.getRolle() == null) {
                    System.out.print("Registrering ble avbrutt");
                    return;
                }
                rutineHeader += " Rolle: " + ny.getRolle();

                visAdministrerProsjektMeny((Prosjekt) entitet);
                System.out.println(rutineHeader);
                ny.setTimer(Statics.getIntegerInput2("et", "timeantall"));
                if (ny.getTimer() == null) {
                    System.out.print("Registrering ble avbrutt");
                    return;
                }
                rutineHeader += " #Timer: " + ny.getTimer();
                while (running) {
                    visAdministrerProsjektMeny((Prosjekt) entitet);
                    System.out.println(rutineHeader);
                    System.out.println("\t" + "1. for å forkaste");
                    System.out.println("\t" + "2. for legge til databasen");
                    String input = scanner.nextLine();
                    switch (input) {
                        case "1":
                            running = false;
                            System.out.print("Registrering ble avbrutt");
                            scanner.nextLine();
                            break;
                        case "2":
                            running = false;
                            proDDao.leggTil(ny);
                            break;
                    }
                }
        }
    }

    private <T extends asd> boolean slettPrompt(T entity) {
        boolean running = true;
        boolean slett = false;
        while (running) {
            System.out.println("Returner 'SLETT' for å bekrefte sletting av " + entity.getClass().getSimpleName() + " '" + entity.getEntId() + ": " + entity.getNavn() + "', eller returner '1' for å avbryte");

            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "SLETT":
                    slett = true;
                    running = false;
                    break;
            }
        }
        return slett;
    }
}
