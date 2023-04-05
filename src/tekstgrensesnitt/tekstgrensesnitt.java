package tekstgrensesnitt;

import DAO.ansattDAO;
import DAO.avdelingDAO;
import DAO.prosjektDAO;
import DAO.prosjektDeltakelseDAO;
import entities.Ansatt;
import entities.Avdeling;
import entities.Prosjekt;
import jakarta.persistence.criteria.CriteriaBuilder;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.*;

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
            for (Ansatt a:ansatte)
                ansIder.put(a.getUserName(),a.getAnsId());
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
            for(Avdeling a:avdelinger)
                avdIder.put("a"+a.getAvdId(),a.getAvdId());
            visAvdelingerMeny(avdelinger);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running=false;
                    break;
                case "2":
                    Avdeling ny = leggTilAvdelingRutine();
                    break;
                default:
                    if (avdIder.containsKey(input))
                        administrerAvdelingMeny(avdIder.get(input));
            }
        }
    }
    private void prosjekterMeny() {
        List<Prosjekt> prosjekter = proDAO.finnProsjekter();
        boolean running = true;
        while (running) {
            visProsjekterMeny(prosjekter);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    Prosjekt ny = leggTilProsjektRutine();
                    if (ny != null)
                        proDAO.createProsjekt(ny);
                    break;
                case "2":
                    Prosjekt prosjekt = velgProsjektMeny(prosjekter);
                    administrerProsjektMeny(prosjekt);
                    break;
                case "3":
                    running = false;
            }
        }
    }
    private int velgAvdelingMeny(List<Avdeling> avdelinger) {
        boolean running = true;
        while (running) {
            Avdeling avdeling;
            visVelgAvdelingMeny(avdelinger);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    Map<String, Avdeling> map = new HashMap<>();
                    for (Avdeling a : avdelinger)
                        map.put(a.getAvdId() + "", a);
                    System.out.println("Vennligst returner avdelingId");
                    int forsok = 3;
                    while (forsok > 0) {
                        forsok--;
                        input = scanner.nextLine();
                        if (!map.containsKey(input))
                            System.out.println("'" + input + "'" + " er ikke ett gyldig avdelingsnummer. " + forsok + " forsøk gjenstår");
                        else {
                            return Integer.parseInt(input);
                        }
                    }
                    System.out.println("Avbrytes pga innlesingsfeil");
                    break;
                case "2":
                    System.out.println("vennligst snevre inn greiene");
                    scanner.nextLine();
                    break;
                case "3":
                    running = false;
            }
        }
        return -1;
    }

    private Prosjekt velgProsjektMeny(List<Prosjekt> prosjekter) {
        return null;
    }

    private void administrerAnsattMeny(int ansId) {
        boolean running = true;
        while (running) {
            Ansatt ansatt = ansDAO.finnAnsatt(ansId);
            visAdministrerAnsattMeny(ansatt);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    if (ansDAO.slettAnsatt(ansId))
                        return;
                    break;
                case "2":
                    int avdId = velgAvdelingMeny(avdDAO.finnAvdelinger());
                    if (avdId != -1)
                        ansDAO.flyttAnsatt(ansId, avdId);
                    break;

                case "3":
                    running = false;
            }
        }
    }

    private void administrerAvdelingMeny(int avdId) {
        System.out.println("avdelingsadministrasjon");
    }

    private void administrerProsjektMeny(Prosjekt prosjekt) {
    }

    private Ansatt leggTilAnsattRutine() {
        Ansatt ny = new Ansatt();
        String fornavn;
        String etternavn;
        LocalDate ansDato;
        String stilling;
        int mndLonn;
        int forsok;

        System.out.println("Hva er den ansattes fornavn");
        ny.setFornavn(scanner.nextLine());
        System.out.println("Hva er den ansattes etternavn?");
        ny.setEtternavn(scanner.nextLine());
        System.out.println("Hva er den ansattes stillingstittel?");
        ny.setStilling(scanner.nextLine());
        System.out.println("Vennligst returner ansettelsesdato på formatet 'YYYY-MM-DD'");
        forsok = 3;
        while (forsok > 0) {
            String input = "";
            try {
                input = scanner.nextLine();
                ny.setAnsDato(LocalDate.parse(input));
                forsok = 0;
            } catch (DateTimeException e) {
                forsok--;
                System.out.println("'"+input+"'" + " er ikke en gyldig dato.");
                if (forsok > 0)
                    System.out.println("Vennligst returner en dato på format 'YYYY-MM-DD'\"" + forsok + " forsok gjenstår.");
                else {
                    System.out.println("Innlesing avbrytes pga gjentatte brukerfeil");
                    return null;
                }
            }
        }
        System.out.println("Vennligst returner den ansattes månedslønn");
        forsok = 3;
        while (forsok > 0) {
            String input = "";
            try {
                input = scanner.nextLine();
                ny.setMndLonn(Integer.parseInt(input));
                forsok = 0;
            } catch (NumberFormatException e) {
                forsok--;
                System.out.println(input + " er ikke en gyldig input.");
                if (forsok > 0)
                    System.out.println("Vennligst returner en gyldig heltallsverdi. " + forsok + " forsok gjenstår.");
                else {
                    System.out.println("Innlesing avbrytes pga gjentatte brukerfeil");
                    return null;
                }
            }
        }
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
        if (kandidater.isEmpty()){
            System.out.println("Databasen inneholder ingen ansatte som kan være sjef i ny avdeling");
            return null;
        }
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner en AnsattId for å velge en sjef til ny avdeling, eller returner '1' for å avbryte");
            for (Ansatt a : ansatte)
                System.out.println("\tAnsattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
            String input = scanner.nextLine();
            switch (input){
                case "1":
                    running=false;
                default:
                    if (kandidater.containsKey(input)) {
                        sjef = ansDAO.finnAnsatt(kandidater.get(input));
                        System.out.println("Vennligst oppgi navn på ny avdeling");
                        ny.setNavn(scanner.nextLine());
                        System.out.println("Returner '1' for å bekrefte ny avdeling, '2' for å forkaste, '3' for å forsøke på nytt");
                        System.out.println("\tNavn: "+ny.getNavn()+", Sjef: "+sjef.getFornavn()+" "+sjef.getEtternavn()+", AnsattId a"+sjef.getAnsId());
                        input=scanner.nextLine();
                        switch (input){
                            case "1":
                                avdDAO.addAvdeling(ny, sjef.getAnsId());
                                running=false;
                                break;
                            case "2":
                                running=false;
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
        return null;
    }

    private void visHovedMeny() {
        System.out.println("Hovedmeny - Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\t1. Administrer ansatte");
        System.out.println("\t2. Administrer avdelinger");
        System.out.println("\t3. Administrer prosjekter");
        System.out.println("\t4. Avslutt programmet");
    }

    private void visAnsatteMeny(List<Ansatt> ansatte) {
        System.out.println("Databasen inneholder " + ansatte.size() + " ansatte.");
        for (Ansatt a : ansatte)
            System.out.println("\tansattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å legge til en ansatt");
        System.out.println("\tReturner en ansattId for å slette/administrere vedkommende.");
    }

    private void visAvdelingerMeny(List<Avdeling> avdelinger) {
        System.out.println("Databasen inneholder " + avdelinger.size() + " avdelinger");
        System.out.println("Vennligst velg en av følgende og trykk 'enter'");
        for (Avdeling a : avdelinger)
            System.out.println("\tAvdelingId: a" + a.getAvdId() + ", Navn: " + a.getNavn() + ", Sjef: " + a.getSjef().getFornavn() + " " + a.getSjef().getEtternavn() + ", #ansatte: " + a.getAnsatte().size());
        System.out.println("\tReturner '1' for å returnere til forrige meny");
        System.out.println("\tReturner '2' for å legge til en avdeling");
        System.out.println("\tReturner en AvdelingId for å slette/administrere vedkommende");
    }

    private void visProsjekterMeny(List<Prosjekt> prosjekter) {
        boolean erTom = prosjekter.size() == 0;
        if (erTom) {
            System.out.println("Databasen inneholder ingen prosjekter. Vennligst velg en av følgende og trykk 'enter'");
            System.out.println("\t1. Returner '1' for å legge til ett prosjekt");
            System.out.println("\t1. Returner '2' for å returnere til forrige meny");
        } else {
            System.out.println("Databasen inneholder " + prosjekter.size() + " prosjekter. Vennligst velg en av følgende og trykk 'enter'");
            System.out.println("\t1. Returner '1' for å legge til ett prosjekt");
            System.out.println("\t1. Returner '2' for velge ut ett prosjekt å administrere");
            System.out.println("\t1. Returner '3' for å returnere til forrige meny");
        }
    }

    private void visVelgAnsattMeny(boolean alle, List<Ansatt> ansatte) {
        if (alle) {
            System.out.println("Her er en liste over alle ansatte");
        } else
            System.out.println("Her er ditt utvalg");
        for (Ansatt a : ansatte) {
            System.out.println("\tansattId: " + a.getUserName() + ", Navn: " + a.getFornavn() + " " + a.getEtternavn() + ", Avdeling: " + a.getAvdeling().getNavn());
        }
        System.out.println("Vennligst returner");
        System.out.println("\t1. Returner '1' for å velge ut en ansatt på 'ansattId'");
        System.out.println("\t2. Returner '2' for å snevre inn listen");
        System.out.println("\t3. Returner '3' for å returnere til forrige meny");
    }

    private void visVelgAvdelingMeny(List<Avdeling> avdelinger) {
        System.out.println("Her er en liste over alle avdelinger");
        for (Avdeling a : avdelinger) {
            System.out.println("\tAvdelingId: " + a.getAvdId() + ", Navn: " + a.getNavn() + ", Sjef: " + a.getSjef().getFornavn() + " " + a.getSjef().getEtternavn() + ", #ansatte: " + a.getAnsatte().size());
        }
        System.out.println("Vennligst returner");
        System.out.println("\t1. Returner '1' for å velge ut en avdeling på 'AvdelingId'");
        System.out.println("\t2. Returner '2' for å snevre inn listen");
        System.out.println("\t3. Returner '3' for å returnere til forrige meny");
    }

    private void visVelgProsjektMeny() {
    }

    private void visAdministrerAnsattMeny(Ansatt ansatt) {

        System.out.println("Ansatt " + ansatt.getUserName() + ": " + ansatt.getFornavn() + " " + ansatt.getEtternavn());
        System.out.println("\tStilling: " + ansatt.getStilling() + ", Avdeling: " + ansatt.getAvdeling().getNavn());
        System.out.println("\tMånedslønn: " + ansatt.getMndLonn() + ", Ansettelsesdato: " + ansatt.getAnsDato());
        System.out.println("Vennligst velg noe");
        System.out.println("\t 1. Slett den ansatte");
        System.out.println("\t 2. Plasser den ansatte i en annen avdeling");
        System.out.println("\t 3. Returner til forrige meny");

    }

    private void visAdministrerAvdelingMeny() {

    }

    private void visAdministrerProsjektMeny() {
    }

    private int velgAnsattMeny(List<Ansatt> ansatte) {
        boolean alle = true;
        boolean running = true;
        while (running) {
            Ansatt ansatt;
            visVelgAnsattMeny(alle, ansatte);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    Map<String, Integer> map = new HashMap<>();
                    for (Ansatt a : ansatte)
                        map.put(a.getUserName(), a.getAnsId());
                    System.out.println("Vennligst returner den ansattes brukernavn");
                    int forsok = 3;
                    while (forsok > 0) {
                        forsok--;
                        input = scanner.nextLine();
                        if (!map.containsKey(input))
                            System.out.println("'" + input + "'" + " er ikke ett gyldig brukernavn. " + forsok + " forsøk gjenstår");
                        else
                            return map.get(input);
                    }
                    System.out.println("Avbrytes pga innlesingsfeil");
                    break;
                case "2":
                    System.out.println("vennligst snevre inn greiene");
                    scanner.nextLine();
                    break;
                case "3":
                    running = false;
            }
        }
        return -1;
    }
}
