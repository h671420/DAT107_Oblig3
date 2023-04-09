package tekstgrensesnitt;

import DAO.*;
import jakarta.persistence.RollbackException;
import tekstgrensesnitt.grensesnittentiteter.*;

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
                    entiteterMeny(AnsattGrensesnitt.class);
                    break;
                case "2":
                    entiteterMeny(AvdelingGrensesnitt.class);
                    break;
                case "3":
                    entiteterMeny(ProsjektGrensesnitt.class);
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
            case "AnsattGrensesnitt":
                dao = new AnsattDAO();
                leggTil = this::leggTilAnsattRutine;
                administrer = this::administrerAnsattMeny;
                break;
            case "AvdelingGrensesnitt":
                dao = new AvdelingDAO();
                leggTil = this::leggTilAvdelingRutine;
                administrer = this::administrerAvdelingMeny;
                break;
            case "ProsjektGrensesnitt":
                dao = new ProsjektDAO();
                leggTil = this::leggTilProsjektRutine;
                administrer = this::administrerProsjektMeny;
                break;
            case "ProsjektDeltakelseGrensesnitt":
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
            AnsattGrensesnitt ansattGrensesnitt = ansDAO.finnAnsatt(ansId);
            visAdministrerAnsattMeny(ansattGrensesnitt);
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
                    if (ansattGrensesnitt.erSjef()) {
                        System.out.println(ansattGrensesnitt.getFornavn() + " " + ansattGrensesnitt.getEtternavn() + " er sjef for avdeling " + ansattGrensesnitt.getAvdeling().getAvdelingsNavn() + ", og kan derfor ikke bytte avdeling.");
                        scanner.nextLine();
                        break;
                    }
//                    Avdeling avdeling = velgAvdelingRutine(avdDAO.finnAlle()); //TODO: lage en daometode som returnere avdelinger bortsett fra den som den ansatte allerede er tilknyttet
                    AvdelingGrensesnitt avdelingGrensesnitt = velgEntitet(avdDAO.finnAlle(), AvdelingGrensesnitt.class);
                    if (avdelingGrensesnitt != null)
                        ansDAO.flyttAnsatt(ansId, avdelingGrensesnitt.getAvdId()); //TODO: legge inn logikk slik at vi ikke forsøker å flytte på en sjef
                    break;
                case "4":
                    administrerProsjektTilknyttingerMeny(ansattGrensesnitt.getAnsId(), null);
                    break;
                case "5":
                    administrerAnsattDetaljerMeny(ansattGrensesnitt.getAnsId());
                    break;
            }
        }
    }

    private void administrerAnsattDetaljerMeny(int ansId) {
        boolean running = true;
        while (running) {
            AnsattGrensesnitt ansattGrensesnitt = ansDAO.finnAnsatt(ansId);
            visAdministrerAnsattDetaljerMeny(ansattGrensesnitt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    String fornavn = Statics.getStringInput(2, "et", "fornavn", true);
                    if (fornavn != null)
                        ansDAO.endreFornavn(ansattGrensesnitt.getAnsId(), fornavn);
                    break;
                case "3":
                    String etternavn = Statics.getStringInput(2, "et", "etternavn", true);
//                            2,
//                            "Vennligst returner et etternavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
//                            "er ikke ett gyldig etternavn",
//                            true);
                    if (etternavn != null)
                        ansDAO.endreEtternavn(ansattGrensesnitt.getAnsId(), etternavn);
                    break;
                case "4":
                    LocalDate nydato = Statics.getDateInput("en ny ansettelsesdato");
                    if (nydato != null)
                        ansDAO.endreAnsettelsesDato(ansattGrensesnitt.getAnsId(), nydato);
                    break;
                case "5":
                    Integer nylonn = Statics.getIntegerInput("en", "månedslønn");
                    if (nylonn != null)
                        ansDAO.endreMaanedsLonn(ansattGrensesnitt.getAnsId(), nylonn);
                    break;
                case "6":
                    String stilling = Statics.getStringInput(4, "en", "stilling", true);
//                            4,
//                            "Vennligst returner en stillingsbeskrivelse, minimumlengde " + 4 + " bokstaver, eller returner '1' for å avbryte",
//                            "er ikke en gyldig stillingsbeskrivelse",
//                            false);
                    if (stilling != null)
                        ansDAO.endreStilling(ansattGrensesnitt.getAnsId(), stilling);
                    break;

            }
        }

    }

    private void administrerAvdelingMeny(int avdId) {
        boolean running = true;
        while (running) {
            AvdelingGrensesnitt avdelingGrensesnitt = avdDAO.finnAvdeling(avdId);
            visAdministrerAvdelingMeny(avdelingGrensesnitt);
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
                    AnsattGrensesnitt nySjef = velgEntitet(ansDAO.finnAnsatteIkkeSjef(), AnsattGrensesnitt.class);
                    if (nySjef != null)
                        avdDAO.endreSjef(avdId, nySjef.getAnsId());
                    break;
                case "5":
                    List<AnsattGrensesnitt> kandidatAnsatte = avdelingGrensesnitt.getAnsatte();
                    kandidatAnsatte.remove(avdelingGrensesnitt.getSjef());
                    List<AvdelingGrensesnitt> kandidatAvdelinger = avdDAO.finnAlle();
                    kandidatAvdelinger.remove(avdelingGrensesnitt);
                    if (kandidatAnsatte.isEmpty()) {
                        System.out.println("Avdelingen har ingen ansatte som kan omplasseres");
                        break;
                    }
                    if (kandidatAvdelinger.isEmpty()) {
                        System.out.println("Det finnes ingen andre avdelinger");
                        break;
                    }
//                    Ansatt ansatt = velgAnsattRutine(kandidatAnsatte);
                    AnsattGrensesnitt ansattGrensesnitt = velgEntitet(kandidatAnsatte, AnsattGrensesnitt.class);
//                    Avdeling nyAvdeling = velgAvdelingRutine(kandidatAvdelinger);
                    AvdelingGrensesnitt nyAvdelingGrensesnitt = velgEntitet(kandidatAvdelinger, AvdelingGrensesnitt.class);
                    if (ansattGrensesnitt != null && nyAvdelingGrensesnitt != null)
                        ansDAO.flyttAnsatt(ansattGrensesnitt.getAnsId(), nyAvdelingGrensesnitt.getAvdId());
                    break;
            }
        }
    }

    private void administrerProsjektMeny(int prosjId) {
        boolean running = true;
        while (running) {
            ProsjektGrensesnitt prosjektGrensesnitt = proDAO.finnProsjekt(prosjId);
            visAdministrerProsjektMeny(prosjektGrensesnitt);
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
                    administrerProsjektTilknyttingerMeny(null, prosjektGrensesnitt.getProsjektId());
                    break;
            }
        }
    }

    private void administrerProsjektTilknyttingerMeny(Integer ansId, Integer proId) {
        ProsjektGrensesnitt prosjektGrensesnitt;
        AnsattGrensesnitt ansattGrensesnitt;
        boolean running = true;
        if (ansId != null) {
            while (running) {
                ansattGrensesnitt = ansDAO.finnAnsatt(ansId);
                visAdministrerProsjektDeltakelserMeny(ansattGrensesnitt, null);
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        running = false;
                        break;
                    case "2":
                        List<ProsjektGrensesnitt> kandidatProsjekter = proDAO.finnAlle();
//                        for (ProsjektDeltakelseGrensesnitt p : ansattGrensesnitt.getProsjektDeltakelser())
//                            if (kandidatProsjekter.contains(p.getProsjekt()))
//                                kandidatProsjekter.remove(p.getProsjekt());
                        prosjektGrensesnitt = velgEntitet(kandidatProsjekter, ProsjektGrensesnitt.class);
                        if (prosjektGrensesnitt != null)
                            leggTilDeltakelseRutine(ansattGrensesnitt, prosjektGrensesnitt);
                        break;
                    default:
                        Map<String, Integer> proIder = new HashMap<>();
//                        for (ProsjektDeltakelseGrensesnitt pd : ansattGrensesnitt.getProsjektDeltakelser())
//                            proIder.put("p" + pd.getProsjekt().getProsjektId(), pd.getId());
                        if (proIder.containsKey(input))
                            administrerProsjektDeltakelseMeny(proIder.get(input));
                        break;
                }
            }
        } else {
            while (running) {
                prosjektGrensesnitt = proDAO.finnProsjekt(proId);
                visAdministrerProsjektDeltakelserMeny(null, prosjektGrensesnitt);
                String input = scanner.nextLine();
                switch (input) {
                    case "1":
                        running = false;
                        break;
                    case "2":
                        List<AnsattGrensesnitt> kandidatAnsatte = ansDAO.finnAlle();
//                        for (ProsjektDeltakelseGrensesnitt p : prosjektGrensesnitt.getProsjektDeltakelser())
//                            if (kandidatAnsatte.contains(p.getProsjekt()))
//                                kandidatAnsatte.remove(p.getProsjekt());
                        if (kandidatAnsatte.isEmpty()) {
                            System.out.println(("Det finnes ingen ansatte som kan tilknyttes prosjektet"));
                            break;
                        }
//                        ansatt = velgAnsattRutine(kandidatAnsatte);
                        ansattGrensesnitt = velgEntitet(kandidatAnsatte, AnsattGrensesnitt.class);
                        if (ansattGrensesnitt != null)
                            leggTilDeltakelseRutine(ansattGrensesnitt, prosjektGrensesnitt);
                        break;
                    default:
                        Map<String, Integer> proIder = new HashMap<>();
//                        for (ProsjektDeltakelseGrensesnitt pd : prosjektGrensesnitt.getProsjektDeltakelser())
//                            proIder.put(pd.getAnsatt().getBrukerNavn(), pd.getId());
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
            ProsjektDeltakelseGrensesnitt pd = proDDao.finnProsjektDeltakelse(prosjektDeltakelseId);
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
            AnsattGrensesnitt ny = new AnsattGrensesnitt();
            ny.setFornavn(Statics.getStringInput(2, "et", "fornavn", true));
            if (ny.getFornavn() == null)
                return;
            ny.setEtternavn(Statics.getStringInput(2, "et", "etternavn", true));
            if (ny.getEtternavn() == null)
                return;
            ny.setAnsettelsesDato(Statics.getDateInput("en ansettelsesdato"));
            if (ny.getAnsettelsesDato() == null)
                return;
            ny.setMaanedsLonn(Statics.getIntegerInput("en", "månedslønn"));
            if (ny.getMaanedsLonn() == null)
                return;
            ny.setStilling(Statics.getStringInput(4, "en", "stillingsbeskrivelse", false));
            if (ny.getStilling() == null)
                return;
            AvdelingGrensesnitt avdelingGrensesnitt;
            if (avdDAO.finnAlle().size() == 0)
                avdelingGrensesnitt = avdDAO.finnAvdeling(1);
            else
//                avdeling = velgAvdelingRutine(avdDAO.finnAlle());
                avdelingGrensesnitt = velgEntitet(avdDAO.finnAlle(), AvdelingGrensesnitt.class);
            ny.setAvdeling(avdelingGrensesnitt);
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
                    ansDAO.addAnsatt(ny, avdelingGrensesnitt.getAvdId());
                    running = false;
                    break;
            }
        }
    }

    private void leggTilAvdelingRutine() {
        boolean running = true;
        while (running) {
            AvdelingGrensesnitt ny = new AvdelingGrensesnitt();
//            ny.setSjef(velgAnsattRutine(ansDAO.finnAnsatteIkkeSjef())); //TODO:vurdere å endre på avdDao slik at den bare tar en ny avdeling som parameter
            ny.setSjef(velgEntitet(ansDAO.finnAnsatteIkkeSjef(), AnsattGrensesnitt.class));
            if (ny.getSjef() == null)
                return;
            ny.setAvdelingsNavn(Statics.getStringInput(2, "et", "avdelingsnavn", false));
            if (ny.getAvdelingsNavn() == null)
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
                    avdDAO.addAvdeling(ny, ny.getSjef().getAnsId());
                    running = false;
                    break;
            }
        }
    }

    private void leggTilProsjektRutine() {
        boolean running = true;
        while (running) {
            ProsjektGrensesnitt ny = new ProsjektGrensesnitt();
            String navn = Statics.getStringInput(2, "et", "prosjektnavn", false);
            if (navn == null)
                return;
            ny.setProsjektNavn(navn);
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

    private void leggTilDeltakelseRutine(AnsattGrensesnitt ansattGrensesnitt, ProsjektGrensesnitt prosjektGrensesnitt) {
        String rolle = "tufs";
        int timer = 3;
        proDDao.addProsjektDeltakelse(ansattGrensesnitt.getAnsId(), prosjektGrensesnitt.getProsjektId(), rolle, timer);
    }

    private void visAdministrerAnsattMeny(AnsattGrensesnitt ansattGrensesnitt) {
        System.out.println("Administrer ansatt meny");
        System.out.println("Ansatt " + ansattGrensesnitt.getBrukerNavn() + ": " + ansattGrensesnitt.getFornavn() + " " + ansattGrensesnitt.getEtternavn());
        System.out.println("\tStilling: " + ansattGrensesnitt.getStilling() + ", Avdeling: " + ansattGrensesnitt.getAvdeling().getAvdelingsNavn());
        System.out.println("\tMånedslønn: " + ansattGrensesnitt.getMaanedsLonn() + ", Ansettelsesdato: " + ansattGrensesnitt.getAnsettelsesDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette den ansatte");
        System.out.println("\tReturner '3' for å bytte avdeling");
        System.out.println("\tReturner '4' for å administrere den ansattes prosjekttilknytninger");
        System.out.println("\tReturner '5' for å endre den ansattes detaljinformasjon");
    }

    private void visAdministrerAnsattDetaljerMeny(AnsattGrensesnitt ansattGrensesnitt) {
        System.out.println("Ansatt " + ansattGrensesnitt.getBrukerNavn() + ": " + ansattGrensesnitt.getFornavn() + " " + ansattGrensesnitt.getEtternavn());
        System.out.println("\tStilling: " + ansattGrensesnitt.getStilling() + ", Månedslønn: " + ansattGrensesnitt.getMaanedsLonn() + ", Ansettelsesdato: " + ansattGrensesnitt.getAnsettelsesDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å endre fornavn");
        System.out.println("\tReturner '3' for å endre etternavn");
        System.out.println("\tReturner '4' for endre ansattelsesdato");
        System.out.println("\tReturner '5' for å endre månedslønn");
        System.out.println("\tReturner '6' for å endre stillingsbeskrivelse");
    }

    private void visAdministrerAvdelingMeny(AvdelingGrensesnitt avdelingGrensesnitt) {
        System.out.println("Avdeling Id: a" + avdelingGrensesnitt.getAvdId() + ", Navn: " + avdelingGrensesnitt.getAvdelingsNavn() + ", Sjef: " + avdelingGrensesnitt.getSjef().getBrukerNavn() + ", " + avdelingGrensesnitt.getSjef().getFornavn() + " " + avdelingGrensesnitt.getSjef().getEtternavn() + " " + avdelingGrensesnitt.getAnsatte().size() + " ansatte");//TODO: lage metode i Avdeling for slik utskrift
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette avdelingen");
        System.out.println("\tReturner '3' for å endre avdelingsnavn");
        System.out.println("\tReturner '4' for å endre sjef");
        System.out.println("\tReturner '5' for å omplassere avdelingens ansatte");
    }

    private void visAdministrerProsjektMeny(ProsjektGrensesnitt prosjektGrensesnitt) {
        System.out.println("ProsjektId: a" + prosjektGrensesnitt.getProsjektId() + ", Navn: " + prosjektGrensesnitt.getProsjektNavn() + ", Beskrivelse: " + prosjektGrensesnitt.getBeskrivelse());//TODO: lage metode i prosjekt for slik utskrift
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette prosjektet");
        System.out.println("\tReturner '3' for å endre prosjektets navn");
        System.out.println("\tReturner '4' for å endre prosjektets beskrivelse");
        System.out.println("\tReturner '5' for å administrere prosjektmedlemmer");
    }

    private void visAdministrerProsjektDeltakelserMeny(AnsattGrensesnitt ansattGrensesnitt, ProsjektGrensesnitt prosjektGrensesnitt) {
        //TODO: legge inn for motsatt tilfelle, at prosjekt!=null
        List<ProsjektDeltakelseGrensesnitt> deltakelser;
        if (ansattGrensesnitt != null) {
            deltakelser = ansattGrensesnitt.getProsjektDeltakelser();
            System.out.println(ansattGrensesnitt.info());
            if (deltakelser.size() == 0)
                System.out.println("Den ansatte deltar ikke i noen prosjekt");
            else
                System.out.println("Den ansatte deltar i følgende prosjekt:");
            for (ProsjektDeltakelseGrensesnitt d : deltakelser)
                System.out.println(d.info());
            System.out.println("vennligst velg");
            System.out.println("\tReturner '1' for å returnere til forrige meny");
            System.out.println("\tReturner '2' for å registrere en ny deltakelse");
            if (deltakelser.size() != 0)
                System.out.println("\tReturner en ProsjektId for å oppdatere / slette deltakelse.");
        } else {
            deltakelser = prosjektGrensesnitt.getProsjektDeltakelser();
            System.out.println(prosjektGrensesnitt.info());
            if (deltakelser.size() == 0)
                System.out.println("Prosjektet har ingen medlemmer");
            else
                System.out.println("Prosjektmedlemmer:");
            for (ProsjektDeltakelseGrensesnitt d : deltakelser)
                System.out.println("\t" + d.info());
            System.out.println("Vennligst velg");
            System.out.println("\tReturner '1' for å returnere til forrige meny");
            System.out.println("\tReturner '2' for å registrere en ny deltakelse");
            if (deltakelser.size() != 0)
                System.out.println("\tReturner en ansatts brukernavn for å oppdatere / slette deltakelse.");
        }
    }

    private void visAdministrerProsjektTilknyttingerMeny(ProsjektDeltakelseGrensesnitt pd) {
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
