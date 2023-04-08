package tekstgrensesnitt;

import DAO.*;
import entities.*;
import jakarta.persistence.RollbackException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;

public class Tekstgrensesnitt {

    private final AnsattDAO ansDAO = new AnsattDAO();
    private final AvdelingDAO avdDAO = new AvdelingDAO();
    private final ProsjektDAO proDAO = new ProsjektDAO();
    private final ProsjektDeltakelseDAO proDDao = new ProsjektDeltakelseDAO();
    static Scanner scanner = new Scanner(System.in);

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
                    System.out.println("Ha en fortsatt fin dag");
                    running = false;
            }
        }
    }

    private void visHovedMeny() {
        System.out.println("Hovedmeny - Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\t1. Administrer ansatte");
        System.out.println("\t2. Administrer avdelinger");
        System.out.println("\t3. Administrer prosjekter");
        System.out.println("\t4. Avslutt programmet");
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
            Map<String, Integer> entitetId = new HashMap<>();
            for (T t : entiteter)
                entitetId.put(t.getEntId(), t.getId());
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
                    if (entitetId.containsKey(input))
                        administrer.accept(entitetId.get(input));
                    break;
            }
        }
    }

    private <T extends asd> void visEntiteterMeny(List<T> entiteter, Class<T> entitetsKlasse) {
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
        System.out.println("Administrasjonemeny " + flerTall + "\n");
        if (entiteter.size() == 0)
            System.out.println("Ingen " + flerTall + " registrert");
        else if (entiteter.size() == 1)
            System.out.println("Kun " + enTall + " registrert");
        else
            System.out.println(entiteter.size() + " " + flerTall + " registrert;");
        for (T t : entiteter)
            System.out.println(t.info());
        System.out.println("Returner en av følgende og trykk 'enter'");
        System.out.println("\t'1' for å returnere til forrige meny");
        System.out.println("\t'2' for å registrere " + enTall);
        if (entiteter.size() != 0)
            System.out.println("\tReturner id for å administrere " + enTall + ".");
    }

    private <T extends asd> T velgEntitet(List<T> entiteter, Class<T> entitetsKlasse) {
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
            System.out.println("Velg en " + entitetsKlasse.getSimpleName().toLowerCase() + " - returner id eller '1' for å avbryte");
            for (T t : entiteter)
                System.out.println(t.info());
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

    private void administrerAnsattMeny(int ansId) {
        boolean running = true;
        while (running) {
            Ansatt ansatt = ansDAO.finnAnsatt(ansId);
            visAdministrerAnsattMeny(ansatt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    try {
                        ansDAO.slettAnsatt(ansId);
                        running = false;
                    } catch (RollbackException e) {
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
//                    Avdeling avdeling = velgAvdelingRutine(avdDAO.finnAlle()); //TODO: lage en daometode som returnere avdelinger bortsett fra den som den ansatte allerede er tilknyttet
                    Avdeling avdeling = velgEntitet(avdDAO.finnAlle(), Avdeling.class);
                    if (avdeling != null)
                        ansDAO.flyttAnsatt(ansId, avdeling.getId()); //TODO: legge inn logikk slik at vi ikke forsøker å flytte på en sjef
                    break;
                case "4":
                    administrerProsjektTilknyttingerMeny(ansatt.getId(), null);
                    break;
                case "5":
                    administrerAnsattDetaljerMeny(ansatt.getId());
                    break;
            }
        }
    }

    private void administrerAnsattDetaljerMeny(int ansId) {
        boolean running = true;
        while (running) {
            Ansatt ansatt = ansDAO.finnAnsatt(ansId);
            visAdministrerAnsattDetaljerMeny(ansatt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    String fornavn = Statics.getStringInput(2, "et", "fornavn", true);
                    if (fornavn != null)
                        ansDAO.endreFornavn(ansatt.getId(), fornavn);
                    break;
                case "3":
                    String etternavn = Statics.getStringInput(2, "et", "etternavn", true);
//                            2,
//                            "Vennligst returner et etternavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
//                            "er ikke ett gyldig etternavn",
//                            true);
                    if (etternavn != null)
                        ansDAO.endreEtternavn(ansatt.getId(), etternavn);
                    break;
                case "4":
                    LocalDate nydato = Statics.getDateInput("en ny ansettelsesdato");
                    if (nydato != null)
                        ansDAO.endreAnsettelsesDato(ansatt.getId(), nydato);
                    break;
                case "5":
                    Integer nylonn = Statics.getIntegerInput("en", "månedslønn");
                    if (nylonn != null)
                        ansDAO.endreMaanedsLonn(ansatt.getId(), nylonn);
                    break;
                case "6":
                    String stilling = Statics.getStringInput(4, "en", "stilling", true);
//                            4,
//                            "Vennligst returner en stillingsbeskrivelse, minimumlengde " + 4 + " bokstaver, eller returner '1' for å avbryte",
//                            "er ikke en gyldig stillingsbeskrivelse",
//                            false);
                    if (stilling != null)
                        ansDAO.endreStilling(ansatt.getId(), stilling);
                    break;

            }
        }

    }

    private void administrerAvdelingMeny(int avdId) {
        boolean running = true;
        while (running) {
            Avdeling avdeling = avdDAO.finnAvdeling(avdId);
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
                        avdDAO.slettAvdeling(avdId);
                        running = false;
                    }
                    break;
                case "3":
                    String navn = Statics.getStringInput(4, "et", "avdelingsnavn", true);
                    if (navn != null)
                        avdDAO.endreNavn(avdId, navn);
                    break;
                case "4":
//                    Ansatt nySjef = velgAnsattRutine(ansDAO.finnAnsatteIkkeSjef());
                    Ansatt nySjef = velgEntitet(ansDAO.finnAnsatteIkkeSjef(), Ansatt.class);
                    if (nySjef != null)
                        avdDAO.endreSjef(avdId, nySjef.getId());
                    break;
                case "5":
                    List<Ansatt> kandidatAnsatte = avdeling.getAnsatte();
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
//                    Ansatt ansatt = velgAnsattRutine(kandidatAnsatte);
                    Ansatt ansatt = velgEntitet(kandidatAnsatte, Ansatt.class);
//                    Avdeling nyAvdeling = velgAvdelingRutine(kandidatAvdelinger);
                    Avdeling nyAvdeling = velgEntitet(kandidatAvdelinger, Avdeling.class);
                    if (ansatt != null && nyAvdeling != null)
                        ansDAO.flyttAnsatt(ansatt.getId(), nyAvdeling.getId());
                    break;
            }
        }
    }

    private void administrerProsjektMeny(int prosjId) {
        boolean running = true;
        while (running) {
            Prosjekt prosjekt = proDAO.finnProsjekt(prosjId);
            visAdministrerProsjektMeny(prosjekt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    try {
                        proDAO.slettProsjekt(prosjId);
                    } catch (RollbackException e) {
                        if (e.getMessage().contains("prosjektdeltakelse_ans_id_fkey"))
                            System.out.println("Kan ikke slette prosjektet siden ansatte er tilknyttet");
                        else
                            e.printStackTrace();
                        scanner.nextLine();
                    }

                    running = false;
                    break;
                case "3":
                    String navn = Statics.getStringInput(4, "et", "prosjektnavn", true);
                    if (navn != null)
                        proDAO.endreNavn(prosjId, navn);
                    break;
                case "4":
                    String beskrivelse = Statics.getStringInput(5, "en", "prosjektbeskrivelse", false);
                    if (beskrivelse != null)
                        proDAO.endreBeskrivelse(prosjId, beskrivelse);
                    break;
                case "5":
                    administrerProsjektTilknyttingerMeny(null, prosjekt.getId());
                    break;
            }
        }
    }

    private void administrerProsjektTilknyttingerMeny(Integer ansId, Integer proId) {
        Prosjekt prosjekt;
        Ansatt ansatt;
        boolean running = true;
        if (ansId != null) {
            while (running) {
                ansatt = ansDAO.finnAnsatt(ansId);
                visAdministrerProsjektDeltakelserMeny(ansatt, null);
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        running = false;
                        break;
                    case "2":
                        List<Prosjekt> kandidatProsjekter = proDAO.finnAlle();
                        for (ProsjektDeltakelse p : ansatt.getProsjekter())
                            if (kandidatProsjekter.contains(p.getProsjekt()))
                                kandidatProsjekter.remove(p.getProsjekt());
                        prosjekt = velgEntitet(kandidatProsjekter, Prosjekt.class);
                        if (prosjekt != null)
                            leggTilDeltakelseRutine(ansatt, prosjekt);
                        break;
                    default:
                        Map<String, Integer> proIder = new HashMap<>();
                        for (ProsjektDeltakelse pd : ansatt.getProsjekter())
                            proIder.put("p" + pd.getProsjekt().getId(), pd.getId());
                        if (proIder.containsKey(input))
                            administrerProsjektDeltakelseMeny(proIder.get(input));
                        break;
                }
            }
        } else {
            while (running) {
                prosjekt = proDAO.finnProsjekt(proId);
                visAdministrerProsjektDeltakelserMeny(null, prosjekt);
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        running = false;
                        break;
                    case "2":
                        List<Ansatt> kandidatAnsatte = ansDAO.finnAlle();
                        for (ProsjektDeltakelse p : prosjekt.getDeltakelser())
                            if (kandidatAnsatte.contains(p.getProsjekt()))
                                kandidatAnsatte.remove(p.getProsjekt());
                        if (kandidatAnsatte.isEmpty()) {
                            System.out.println(("Det finnes ingen ansatte som kan tilknyttes prosjektet"));
                            break;
                        }
//                        ansatt = velgAnsattRutine(kandidatAnsatte);
                        ansatt = velgEntitet(kandidatAnsatte, Ansatt.class);
                        if (ansatt != null)
                            leggTilDeltakelseRutine(ansatt, prosjekt);
                        break;
                    default:
                        Map<String, Integer> proIder = new HashMap<>();
                        for (ProsjektDeltakelse pd : prosjekt.getDeltakelser())
                            proIder.put(pd.getAnsatt().getBrukernavn(), pd.getId());
                        if (proIder.containsKey(input))
                            administrerProsjektDeltakelseMeny(proIder.get(input));
                        break;
                }
            }
        }
    }

    private void administrerProsjektDeltakelseMeny(Integer prosjektDeltakelseId) {
        boolean running = true;
        while (running) {
            ProsjektDeltakelse pd = proDDao.finnProsjektDeltakelse(prosjektDeltakelseId);
            visAdministrerProsjektTilknyttingerMeny(pd);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    proDDao.slett(prosjektDeltakelseId);
                    running = false;
                    break;
                case "3":
                    String nyRolle = Statics.getStringInput(4, "en", "ny rolle", false);
                    if (nyRolle != null)
                        proDDao.endreRolle(prosjektDeltakelseId, nyRolle);
                    break;
                case "4":
                    Integer nyTimer = Statics.getIntegerInput("et", "timeantall");
                    if (nyTimer != null)
                        proDDao.endreTimer(prosjektDeltakelseId, nyTimer);
                    break;
            }
        }

    }

    private void leggTilAnsattRutine() {
        boolean running = true;
        while (running) {
            Ansatt ny = new Ansatt();
            ny.setFornavn(Statics.getStringInput(2, "et", "fornavn", true));
            if (ny.getFornavn() == null)
                return;
            ny.setEtternavn(Statics.getStringInput(2, "et", "etternavn", true));
            if (ny.getEtternavn() == null)
                return;
            ny.setAnsDato(Statics.getDateInput("en ansettelsesdato"));
            if (ny.getAnsDato() == null)
                return;
            ny.setMndLonn(Statics.getIntegerInput("en", "månedslønn"));
            if (ny.getMndLonn() == null)
                return;
            ny.setStilling(Statics.getStringInput(4, "en", "stillingsbeskrivelse", false));
            if (ny.getStilling() == null)
                return;
            Avdeling avdeling;
            if (avdDAO.finnAlle().size() == 0)
                avdeling = avdDAO.finnAvdeling(1);
            else
//                avdeling = velgAvdelingRutine(avdDAO.finnAlle());
                avdeling = velgEntitet(avdDAO.finnAlle(), Avdeling.class);
            ny.setAvdeling(avdeling);
            System.out.println(ny.info());
            System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte ny ansatt");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    break;
                default:
                    ansDAO.addAnsatt(ny, avdeling.getId());
                    running = false;
                    break;
            }
        }
    }

    private void leggTilAvdelingRutine() {
        boolean running = true;
        while (running) {
            Avdeling ny = new Avdeling();
//            ny.setSjef(velgAnsattRutine(ansDAO.finnAnsatteIkkeSjef())); //TODO:vurdere å endre på avdDao slik at den bare tar en ny avdeling som parameter
            ny.setSjef(velgEntitet(ansDAO.finnAnsatteIkkeSjef(), Ansatt.class));
            if (ny.getSjef() == null)
                return;
            ny.setNavn(Statics.getStringInput(2, "et", "avdelingsnavn", false));
            if (ny.getNavn() == null)
                return;
            System.out.println(ny.info());
            System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte ny avdeling");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    break;
                default:
                    avdDAO.addAvdeling(ny, ny.getSjef().getId());
                    running = false;
                    break;
            }
        }
    }

    private void leggTilProsjektRutine() {
        boolean running = true;
        while (running) {
            Prosjekt ny = new Prosjekt();
            String navn = Statics.getStringInput(2, "et", "prosjektnavn", false);
            if (navn == null)
                return;
            ny.setNavn(navn);
            String beskrivelse = Statics.getStringInput(5, "en", "prosjektbeskrivelse", false);
            if (beskrivelse == null)
                return;
            ny.setBeskrivelse(beskrivelse);
            System.out.println(ny); //TODO: passende utskrift av prosjekt
            System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte ny avdeling");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    break;
                default:
                    proDAO.addProsjekt(ny);
                    running = false;
                    break;
            }
        }
    }

    private void leggTilDeltakelseRutine(Ansatt ansatt, Prosjekt prosjekt) {
        String rolle = "tufs";
        int timer = 3;
        proDDao.addProsjektDeltakelse(ansatt.getId(), prosjekt.getId(), rolle, timer);
    }

    private void visAdministrerAnsattMeny(Ansatt ansatt) {
        System.out.println("Administrer ansatt meny");
        System.out.println("Ansatt " + ansatt.getBrukernavn() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
        System.out.println("\tStilling: " + ansatt.getStilling() + ", Avdeling: " + ansatt.getAvdeling().getNavn());
        System.out.println("\tMånedslønn: " + ansatt.getMndLonn() + ", Ansettelsesdato: " + ansatt.getAnsDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette den ansatte");
        System.out.println("\tReturner '3' for å bytte avdeling");
        System.out.println("\tReturner '4' for å administrere den ansattes prosjekttilknytninger");
        System.out.println("\tReturner '5' for å endre den ansattes detaljinformasjon");
    }

    private void visAdministrerAnsattDetaljerMeny(Ansatt ansatt) {
        System.out.println("Ansatt " + ansatt.getBrukernavn() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
        System.out.println("\tStilling: " + ansatt.getStilling() + ", Månedslønn: " + ansatt.getMndLonn() + ", Ansettelsesdato: " + ansatt.getAnsDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å endre fornavn");
        System.out.println("\tReturner '3' for å endre etternavn");
        System.out.println("\tReturner '4' for endre ansattelsesdato");
        System.out.println("\tReturner '5' for å endre månedslønn");
        System.out.println("\tReturner '6' for å endre stillingsbeskrivelse");
    }

    private void visAdministrerAvdelingMeny(Avdeling avdeling) {
        System.out.println("Avdeling Id: a" + avdeling.getId() + ", Navn: " + avdeling.getNavn() + ", Sjef: " + avdeling.getSjef().getBrukernavn() + ", " + avdeling.getSjef().getFornavn() + " " + avdeling.getSjef().getEtternavn() + " " + avdeling.getAnsatte().size() + " ansatte");//TODO: lage metode i Avdeling for slik utskrift
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette avdelingen");
        System.out.println("\tReturner '3' for å endre avdelingsnavn");
        System.out.println("\tReturner '4' for å endre sjef");
        System.out.println("\tReturner '5' for å omplassere avdelingens ansatte");
    }

    private void visAdministrerProsjektMeny(Prosjekt prosjekt) {
        System.out.println("ProsjektId: a" + prosjekt.getId() + ", Navn: " + prosjekt.getNavn() + ", Beskrivelse: " + prosjekt.getBeskrivelse());//TODO: lage metode i prosjekt for slik utskrift
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette prosjektet");
        System.out.println("\tReturner '3' for å endre prosjektets navn");
        System.out.println("\tReturner '4' for å endre prosjektets beskrivelse");
        System.out.println("\tReturner '5' for å administrere prosjektmedlemmer");
    }

    private void visAdministrerProsjektDeltakelserMeny(Ansatt ansatt, Prosjekt prosjekt) {
        //TODO: legge inn for motsatt tilfelle, at prosjekt!=null
        List<ProsjektDeltakelse> deltakelser;
        if (ansatt != null) {
            deltakelser = ansatt.getProsjekter();
            System.out.println(ansatt.info());
            if (deltakelser.size() == 0)
                System.out.println("Den ansatte deltar ikke i noen prosjekt");
            else
                System.out.println("Den ansatte deltar i følgende prosjekt:");
            for (ProsjektDeltakelse d : deltakelser)
                System.out.println(d.prosjektInfo());
            System.out.println("vennligst velg");
            System.out.println("\tReturner '1' for å returnere til forrige meny");
            System.out.println("\tReturner '2' for å registrere en ny deltakelse");
            if (deltakelser.size() != 0)
                System.out.println("\tReturner en ProsjektId for å oppdatere / slette deltakelse.");
        } else {
            deltakelser = prosjekt.getDeltakelser();
            System.out.println(prosjekt.info());
            if (deltakelser.size() == 0)
                System.out.println("Prosjektet har ingen medlemmer");
            else
                System.out.println("Prosjektmedlemmer:");
            for (ProsjektDeltakelse d : deltakelser)
                System.out.println("\t" + d.ansattInfo());
            System.out.println("Vennligst velg");
            System.out.println("\tReturner '1' for å returnere til forrige meny");
            System.out.println("\tReturner '2' for å registrere en ny deltakelse");
            if (deltakelser.size() != 0)
                System.out.println("\tReturner en ansatts brukernavn for å oppdatere / slette deltakelse.");
        }
    }

    private void visAdministrerProsjektTilknyttingerMeny(ProsjektDeltakelse pd) {
        System.out.println(pd.info());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette deltakelse");
        System.out.println("\tReturner '3' for å endre rolle");
        System.out.println("\tReturner '4' for å endre timer");
    }

    private <T extends asd> boolean slettPrompt(T entity) {
        boolean running = true;
        boolean slett=false;
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
