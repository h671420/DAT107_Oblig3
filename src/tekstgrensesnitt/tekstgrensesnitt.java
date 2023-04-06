package tekstgrensesnitt;

import DAO.ansattDAO;
import DAO.avdelingDAO;
import DAO.prosjektDAO;
import DAO.prosjektDeltakelseDAO;
import entities.Ansatt;
import entities.Avdeling;
import entities.Prosjekt;
import entities.ProsjektDeltakelse;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class tekstgrensesnitt {
    private final ansattDAO ansDAO = new ansattDAO();
    private final avdelingDAO avdDAO = new avdelingDAO();
    private final prosjektDAO proDAO = new prosjektDAO();
    private final prosjektDeltakelseDAO proDDao = new prosjektDeltakelseDAO();
    private final Scanner scanner;

    public tekstgrensesnitt(Scanner scanner) {
        this.scanner = scanner;
        hovedMeny();
    }

    public static void main(String[] args) {
        tekstgrensesnitt tk = new tekstgrensesnitt(new Scanner(System.in));
        System.out.println(tk.getClass());
    }

    private void hovedMeny() {
        boolean running = true;
        while (running) {
            visHovedMeny();
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    ansatteMeny();
                    break;
                case "2":
                    avdelingerMeny();
                    break;
                case "3":
                    prosjekterMeny();
                    break;
                case "4":
                    System.out.println("Ha en fortsatt fin dag");
                    running = false;
            }
        }
    }

    private void ansatteMeny() {
        List<Ansatt> ansatte;
        boolean running = true;
        while (running) {
            ansatte = ansDAO.finnAnsatte();
            Map<String, Integer> ansIder = new HashMap<>();
            for (Ansatt a : ansatte)
                ansIder.put(a.getUserName(), a.getAnsId());
            visAnsatteMeny(ansatte);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    Ansatt ny = leggTilAnsattRutine();
                    if (ny != null)
                        ansDAO.addAnsatt(ny, 1);
                    break;
                default:
                    if (ansIder.containsKey(input))
                        administrerAnsattMeny(ansIder.get(input));
            }
        }
    }

    private void avdelingerMeny() {
        List<Avdeling> avdelinger;
        boolean running = true;
        while (running) {
            avdelinger = avdDAO.finnAvdelinger();
            Map<String, Integer> avdIder = new HashMap<>();
            for (Avdeling a : avdelinger)
                avdIder.put("a" + a.getAvdId(), a.getAvdId());
            visAvdelingerMeny(avdelinger);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    leggTilAvdelingRutine();
                    break;
                default:
                    if (avdIder.containsKey(input))
                        administrerAvdelingMeny(avdIder.get(input));
            }
        }
    }

    private void prosjekterMeny() {
        List<Prosjekt> prosjekter;
        boolean running = true;
        while (running) {
            prosjekter = proDAO.finnProsjekter();
            Map<String, Integer> prosIds = new HashMap<>();
            for (Prosjekt p : prosjekter)
                prosIds.put("p" + p.getProsjId(), p.getProsjId());
            visProsjekterMeny(prosjekter);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    leggTilProsjektRutine();
                    break;
                default:
                    if (prosIds.containsKey(input))
                        administrerProsjektMeny(prosIds.get(input));
            }
        }
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
                    ansDAO.slettAnsatt(ansId); // TODO: legge inn logikk slik at vi ikke forsøker å slette ansatt med bindinger
                    running = false;
                    break;
                case "3":
                    if (ansatt.erSjef()) {
                        System.out.println(ansatt.getFornavn() + " " + ansatt.getEtternavn() + " er sjef for avdeling " + ansatt.getAvdeling().getNavn() + ", og kan derfor ikke bytte avdeling.");
                        scanner.nextLine();
                        break;
                    }
                    Avdeling avdeling = velgAvdelingRutine(avdDAO.finnAvdelinger());
                    if (avdeling != null)
                        ansDAO.flyttAnsatt(ansId, avdeling.getAvdId()); //TODO: legge inn logikk slik at vi ikke forsøker å flytte på en sjef
                    break;
                case "4":
                    administrerProsjektTilknyttingerMeny(ansatt, null);
                    break;
                case "5":
                    administrerAnsattDetaljerMeny(ansatt.getAnsId());
                    break;
            }
        }
    }

    private void administrerAnsattDetaljerMeny(int ansId) {
        //TODO: lage metoden
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
                    String fornavn = getStringInput(
                            2,
                            "Vennligst returner et fornavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
                            "er ikke ett gyldig fornavn",
                            true);
                    if (fornavn != null)
                        ansDAO.endreFornavn(ansatt.getAnsId(), fornavn);
                    break;
                case "3":
                    String etternavn = getStringInput(
                            2,
                            "Vennligst returner et etternavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
                            "er ikke ett gyldig etternavn",
                            true);
                    if (etternavn != null)
                        ansDAO.endreEtternavn(ansatt.getAnsId(), etternavn);
                    break;
                case "4":
                    LocalDate nydato = getDateInput();
                    if (nydato != null)
                        ansDAO.endreAnsettelsesDato(ansatt.getAnsId(), nydato);
                    break;
                case "5":
                    Integer nylønn = getLønnInput();
                    if (nylønn != null)
                        ansDAO.endreMånedsLønn(ansatt.getAnsId(), nylønn);
                    break;
                case "6":
                    String stilling = getStringInput(
                            4,
                            "Vennligst returner en stillingsbeskrivelse, minimumlengde " + 4 + " bokstaver, eller returner '1' for å avbryte",
                            "er ikke en gyldig stillingsbeskrivelse",
                            false);
                    if (stilling!=null)
                        ansDAO.endreStilling(ansatt.getAnsId(),stilling);
                    break;

            }
        }

    }

    private Integer getLønnInput() {
        Integer mndlønn=null;
        boolean running=true;
        while (running){
            System.out.println("Vennligst returner månedslønn(positiv heltallsverdi), eller returner '1' for å avbryte");
            String input = scanner.nextLine();
            switch(input){
                case "1":
                    running=false;
                    mndlønn=null;
                    break;
                default:
                    try{
                        mndlønn=Integer.parseInt(input);
                        if (mndlønn<=0)
                            throw new NumberFormatException();
                        System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte '" + mndlønn + "'");
                        input = scanner.nextLine();
                        switch (input) {
                            case "1":
                                mndlønn = null;
                                running = false;
                                break;
                            case "2":
                                break;
                            case "":
                                running = false;
                                break;
                        }
                    }
                    catch(NumberFormatException e){
                        System.out.println("'"+input+"' er ikke en gyldig verdi");
                    }
            }
        }
        return mndlønn;
    }

    private LocalDate getDateInput() {
        LocalDate dato = null;
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner ansettelsesdato på formatet 'YYYY-MM-DD', eller returner '1' for å avbryte");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    try {
                        dato = LocalDate.parse(input);
                        System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte '"+dato+"'");
                        input = scanner.nextLine();
                        switch (input) {
                            case "1":
                                dato = null;
                                running = false;
                                break;
                            case "2":
                                break;
                            case "":
                                running = false;
                                break;
                        }
                    } catch (DateTimeException e) {
                        System.out.println("'"+input+"' er ikke en gyldig dato.");
                        break;
                    }
            }
        }
        return dato;
    }


    private String getStringInput(int lengde, String infoMsg, String errorMsg, boolean navn) {
        String svar = null;
        boolean running = true;
        while (running) {
            System.out.println(infoMsg);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (input.length() < lengde) {
                        System.out.println("'" + input + "' " + errorMsg);
                        break;
                    } else {
                        svar = input.toLowerCase();
                        if (navn) {
                            String[] svarArr = svar.split(" ");
                            svar = "";
                            for (String s : svarArr)
                                svar += s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
                            svar = svar.substring(0, svar.length() - 1);
                        }
                        System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte '"+svar+"'");
                        input = scanner.nextLine();
                        switch (input) {
                            case "1":
                                svar = null;
                                running = false;
                                break;
                            case "2":
                                break;
                            case "":
                                running = false;
                                break;
                        }
                    }
            }
        }
        return svar;
    }


    private void administrerProsjektMeny(int ProsjId) {
        //TODO: lage metoden
    }

    private void administrerAvdelingMeny(int avdId) {
        //TODO: lage metoden
    }

    private void administrerProsjektTilknyttingerMeny(Ansatt ansatt, Prosjekt prosjekt) {
        //TODO: koden nedenfor tar bare for seg at ansatt!=0, må legge inn hvis for prosjekt !=null

        boolean running = true;
        while (running) {
            visadministrerProsjektDeltakelserMeny(ansatt, prosjekt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    List<Prosjekt> prosjekter = proDAO.finnProsjekter();
                    for (ProsjektDeltakelse p : ansatt.getProsjekter())
                        if (prosjekter.contains(p.getProsjekt()))
                            prosjekter.remove(p.getProsjekt());
                    prosjekt = velgProsjektRutine(prosjekter);
                    if (prosjekt != null)
                        leggTilDeltakelseRutine(ansatt, prosjekt);
                    break;
            }
        }
    }

    private Ansatt velgAnsattRutine(List<Ansatt> ansatte) {
        Ansatt ansatt = null;
        Map<String, Ansatt> ansIder = new HashMap<>();
        for (Ansatt a : ansatte)
            ansIder.put(ansatt.getUserName(), a);
        boolean running = true;
        while (running) {
            System.out.println("Velg en ansatt (returner ansattId, eller returner '1' for å avbryte");
            for (Ansatt a : ansatte)
                System.out.println("\tansattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (ansIder.containsKey(input)) {
                        ansatt = ansIder.get(input);
                        running = false;
                    }
            }
        }
        return ansatt;
    }

    private Prosjekt velgProsjektRutine(List<Prosjekt> prosjekter) {
        Prosjekt prosjekt = null;
        Map<String, Prosjekt> prosjektIder = new HashMap<>();
        for (Prosjekt p : prosjekter)
            prosjektIder.put("p" + p.getProsjId(), p);
        boolean running = true;
        while (running) {
            System.out.println("Velg ett prosjekt (returner prosjektId, eller returner '1' for å avbryte");
            for (Prosjekt p : prosjekter)
                System.out.println("\tProsjektId: p" + p.getProsjId() + ", Navn: " + p.getNavn());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (prosjektIder.containsKey(input)) {
                        prosjekt = prosjektIder.get(input);
                        running = false;
                    }
            }
        }
        return prosjekt;
    }

    private Avdeling velgAvdelingRutine(List<Avdeling> avdelinger) {
        Avdeling avdeling = null;
        Map<String, Avdeling> avdelingIder = new HashMap<>();
        for (Avdeling a : avdelinger)
            avdelingIder.put("a" + a.getAvdId(), a);
        boolean running = true;
        while (running) {
            System.out.println("Velg en avdeling (returner AvdelingId, eller returner '1' for å avbryte");
            for (Avdeling a : avdelinger)
                System.out.println("\tAvdelingId: a" + a.getAvdId() + ", Navn: " + a.getNavn());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (avdelingIder.containsKey(input)) {
                        avdeling = avdelingIder.get(input);
                        running = false;
                    }
            }
        }
        return avdeling;
    }

    private Ansatt leggTilAnsattRutine() {
        Ansatt ny = new Ansatt();
        ny.setFornavn(getStringInput(
                2,
                "Vennligst returner et fornavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
                "er ikke ett gyldig fornavn",
                true));
        if (ny.getFornavn()==null)
            return null;
        ny.setEtternavn(getStringInput(
                2,
                "Vennligst returner et etternavn, minimumlengde " + 2 + " bokstaver, eller returner '1' for å avbryte",
                "er ikke ett gyldig etternavn",
                true));
        if (ny.getEtternavn()==null)
            return null;
        ny.setAnsDato(getDateInput());
        if (ny.getAnsDato()==null)
            return null;
        ny.setMndLønn(getLønnInput());
        if (ny.getMndLønn()==null)
            return null;
        ny.setStilling(getStringInput(
                4,
                "Vennligst returner en stillingsbeskrivelse, minimumlengde " + 4 + " bokstaver, eller returner '1' for å avbryte",
                "er ikke en gyldig stillingsbeskrivelse",
                false));
        if (ny.getStilling()==null)
            return null;
        return ny;
    }

    private Avdeling leggTilAvdelingRutine() {
        Ansatt sjef;
        Avdeling ny = new Avdeling();
        List<Ansatt> ansatte = ansDAO.finnAnsatte();
        Map<String, Integer> kandidater = new HashMap<>();
        for (Ansatt a : ansatte)
            if (!a.erSjef())
                kandidater.put(a.getUserName(), a.getAnsId());
        if (kandidater.isEmpty()) {
            System.out.println("Databasen inneholder ingen ansatte som kan være sjef i ny avdeling");
            return null;
        }
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner en AnsattId for å velge en sjef til ny avdeling, eller returner '1' for å avbryte");
            for (Ansatt a : ansatte)
                System.out.println("\tAnsattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                default:
                    if (kandidater.containsKey(input)) {
                        sjef = ansDAO.finnAnsatt(kandidater.get(input));
                        System.out.println("Vennligst oppgi navn på ny avdeling");
                        ny.setNavn(scanner.nextLine());
                        System.out.println("Returner '1' for å bekrefte ny avdeling, '2' for å forkaste, '3' for å forsøke på nytt");
                        System.out.println("\tNavn: " + ny.getNavn() + ", Sjef: " + sjef.getFornavn() + " " + sjef.getEtternavn() + ", AnsattId a" + sjef.getAnsId());
                        input = scanner.nextLine();
                        switch (input) {
                            case "1":
                                avdDAO.addAvdeling(ny, sjef.getAnsId());
                                running = false;
                                break;
                            case "2":
                                running = false;
                                break;
                            case "3":
                                break;
                        }

                    }

            }
        }
        return null;
    }

    private Prosjekt leggTilProsjektRutine() {
        boolean running = true;
        while (running) {
            Prosjekt ny = new Prosjekt();
            System.out.println("Vennligst oppgi prosjektets navn");
            ny.setNavn(scanner.nextLine());
            System.out.println("Vennligst oppgi en beskrivelse av prosjektet");
            ny.setBeskrivelse(scanner.nextLine());
            System.out.println("Navn '" + ny.getNavn() + "', Beskrivelse: '" + ny.getBeskrivelse() + "'");
            System.out.println("Returner '1' for å bekrefte nytt prosjekt, '2' for å forkaste, '3' for å begynne på nytt");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    proDAO.addProsjekt(ny);
                    running = false;
                    break;
                case "2":
                    running = false;
                    break;
                case "3":
                    break;
            }
        }
        return null;
    }

    private void leggTilDeltakelseRutine(Ansatt ansatt, Prosjekt prosjekt) {
        String rolle = "tufs";
        int timer = 3;
        proDDao.addProsjektDeltakelse(ansatt.getAnsId(), prosjekt.getProsjId(), rolle, timer);
    }

    private void visHovedMeny() {
        System.out.println("Hovedmeny - Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\t1. Administrer ansatte");
        System.out.println("\t2. Administrer avdelinger");
        System.out.println("\t3. Administrer prosjekter");
        System.out.println("\t4. Avslutt programmet");
    }

    private void visAnsatteMeny(List<Ansatt> ansatte) {
        if (ansatte.size() == 0)
            System.out.println("Databasen inneholder ingen ansatte");
        else if (ansatte.size() == 1)
            System.out.println("Databasen inneholder " + ansatte.size() + " ansatt.");
        else
            System.out.println("Databasen inneholder " + ansatte.size() + " ansatte.");
        for (Ansatt a : ansatte)
            System.out.println("\tansattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å legge til en ansatt");
        if (ansatte.size() != 0)
            System.out.println("\tReturner en ansattId for å slette/administrere vedkommende.");
    }

    private void visAvdelingerMeny(List<Avdeling> avdelinger) {
        if (avdelinger.size() == 0)
            System.out.println("Databasen inneholder ingen avdelinger");
        else if (avdelinger.size() == 1)
            System.out.println("Databasen inneholder " + avdelinger.size() + " avdeling.");
        else
            System.out.println("Databasen inneholder " + avdelinger.size() + " avdelinger.");
        for (Avdeling a : avdelinger)
            System.out.println("\tAvdelingId: " + a.getAvdId() + ", Navn: " + a.getNavn() + ", Sjef: " + a.getSjef().getFornavn() + " " + a.getSjef().getEtternavn());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å legge til en avdeling");
        if (avdelinger.size() != 0)
            System.out.println("\tReturner en AvdelingId for å slette/administrere avdelingen.");
    }

    private void visProsjekterMeny(List<Prosjekt> prosjekter) {
        if (prosjekter.size() == 0)
            System.out.println("Databasen inneholder ingen prosjekter");
        else if (prosjekter.size() == 1)
            System.out.println("Databasen inneholder " + prosjekter.size() + " prosjekt.");
        else
            System.out.println("Databasen inneholder " + prosjekter.size() + " prosjekt.");
        for (Prosjekt p : prosjekter)
            System.out.println("\tProsjektId: p" + p.getProsjId() + ", Navn: " + p.getNavn() + ", Beskrivelse: " + p.getBeskrivelse());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å legge til ett prosjekt");
        if (prosjekter.size() != 0)
            System.out.println("\tReturner en ProsjektId for å slette/administrere ett prosjekt.");
    }


    private void visAdministrerAnsattMeny(Ansatt ansatt) {
        System.out.println("Ansatt " + ansatt.getUserName() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
        System.out.println("\tStilling: " + ansatt.getStilling() + ", Avdeling: " + ansatt.getAvdeling().getNavn());
        System.out.println("\tMånedslønn: " + ansatt.getMndLønn() + ", Ansettelsesdato: " + ansatt.getAnsDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å slette den ansatte");
        System.out.println("\tReturner '3' for å bytte avdeling");
        System.out.println("\tReturner '4' for å administrere den ansattes prosjekttilknytninger");
        System.out.println("\tReturner '5' for å endre den ansattes detaljinformasjon");
    }

    private void visAdministrerAnsattDetaljerMeny(Ansatt ansatt) {
        System.out.println("Ansatt " + ansatt.getUserName() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
        System.out.println("\tStilling: "+ansatt.getStilling()+", Månedslønn: " + ansatt.getMndLønn() + ", Ansettelsesdato: " + ansatt.getAnsDato());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å endre fornavn");
        System.out.println("\tReturner '3' for å endre etternavn");
        System.out.println("\tReturner '4' for endre ansattelsesdato");
        System.out.println("\tReturner '5' for å endre månedslønn");
        System.out.println("\tReturner '6' for å endre stillingsbeskrivelse");
    }

    private void visadministrerProsjektDeltakelserMeny(Ansatt ansatt, Prosjekt prosjekt) {
        //TODO: legge inn for motsatt tilfelle, at prosjekt!=null
        List<ProsjektDeltakelse> deltakelser;
        if (ansatt != null) {
            deltakelser = ansatt.getProsjekter();
            System.out.println("Ansatt " + ansatt.getUserName() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
            if (deltakelser.size() == 0)
                System.out.println("Den ansatte deltar ikke i noen prosjekt");
            else
                System.out.println("Den ansatte deltar i følgende prosjekt:");
            for (ProsjektDeltakelse d : deltakelser)
                System.out.println("\tProsjektId: " + d.getProsjekt().getProsjId() + ", Navn: '" + d.getProsjekt().getNavn() + "', Rolle: '" + d.getRolle() + ", Timer: " + d.getTimer());
            System.out.println("vennligst velg");
            System.out.println("\tReturner '1' for å returnere til forrige meny");
            System.out.println("\tReturner '2' for å registrere en ny deltakelse");
            if (deltakelser.size() != 0)
                System.out.println("\tReturner en ProsjektId for å oppdatere / slette deltakelse.");
        }


    }
}
