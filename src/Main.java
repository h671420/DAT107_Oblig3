import DAO.AnsattDAO;
import DAO.AvdelingDAO;
import DAO.ProsjektDAO;
import DAO.ProsjektDeltakelseDAO;
import entities.Ansatt;
import entities.Avdeling;
import entities.Prosjekt;
import entities.ProsjektDeltakelse;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static AnsattDAO ansattDAO = new AnsattDAO();
    static AvdelingDAO avdelingDAO = new AvdelingDAO();
    static ProsjektDAO prosjektDAO = new ProsjektDAO();
    static ProsjektDeltakelseDAO prosjektDeltakelseDAO = new ProsjektDeltakelseDAO();

    public static void main(String[] args) {
//            leggTilNoenAnsatte();
//            leggTilNoenAvdelinger();
//        leggTilNoenProsjekter();
//        registrerNoenProsjektDeltakelser();

//        prosjektDAO.slettProsjekt(prosjektDAO.finnProsjekt(2));

//        visAnsatte();
//        visAvdelinger();
//        visProsjekter();
//        visProsjektDeltakelser();

//        ansattDAO.slettAnsatt(10);
//        System.out.println(ansattDAO.finnAnsatt(10));
        System.out.println(prosjektDeltakelseDAO.finnProsjektDeltakelse(4));

    }

    private static void registrerNoenProsjektDeltakelser() {
        prosjektDeltakelseDAO.addProsjektDeltakelse(2, 1, "prosjektsjef", 3);
        prosjektDeltakelseDAO.addProsjektDeltakelse(3, 2, "kaffehenter", 3);
//        prosjektDeltakelseDAO.createProsjektDeltakelse(4, 3, "medarbeider", 3);
    }

    private static void leggTilNoenProsjekter() {
        Prosjekt pro = new Prosjekt("mobilapp", "ny fancy mobilapp");
        Prosjekt pro2 = new Prosjekt("digitalisering", "digitalisering av noen saker");
        Prosjekt pro3 = new Prosjekt("nedbemanning", "dessverre dårlige tider");
//        ansattDAO.flyttAnsatt(4,3);
//        ansattDAO.flyttAnsatt(6,2);
//        ansattDAO.flyttAnsatt(7,2);
        prosjektDAO.addProsjekt(pro);
        prosjektDAO.addProsjekt(pro2);
        prosjektDAO.addProsjekt(pro3);
    }

    private static void leggTilNoenAvdelinger() {
//        avdelingDAO.addAvdeling(new Avdeling("Kontoret"), 2);
//        avdelingDAO.addAvdeling(new Avdeling("Lager"), 3);
//        avdelingDAO.addAvdeling(new Avdeling("Gården"), 5);
    }

    private static void leggTilNoenAnsatte() {
        Ansatt jonny = new Ansatt("Jonny", "Cakes");
        Ansatt rocky = new Ansatt("Rocky", "Rock");
        Ansatt asgeir = new Ansatt("Asgeir", "Slettemoen");
        Ansatt roar = new Ansatt("Roar", "Larsen");
        Ansatt timmy = new Ansatt("Timmy", "Teigen");
        Ansatt lars = new Ansatt("Lars", "Larsen");
        ansattDAO.addAnsatt(jonny, 1);
        ansattDAO.addAnsatt(rocky, 1);
        ansattDAO.addAnsatt(asgeir, 1);
        ansattDAO.addAnsatt(roar, 1);
        ansattDAO.addAnsatt(timmy, 1);
        ansattDAO.addAnsatt(lars, 1);
    }

    private static void visAvdelinger() {
        System.out.println("Databasens avdelinger");
        for (Avdeling a : avdelingDAO.finnAlle())
            System.out.println("\t" + a);
    }

    private static void visAnsatte() {
        System.out.println("Databasens ansatte");
        for (Ansatt a : ansattDAO.finnAlle())
            System.out.println("\t" + a);
    }

    private static void visProsjekter() {
        System.out.println("Databasens Prosjekter");
        for (Prosjekt p : prosjektDAO.finnAlle())
            System.out.println("\t" + p);
    }

    private static void visProsjektDeltakelser() {
        System.out.println("Databasens ProsjektDeltakelser");
        for (ProsjektDeltakelse pd : prosjektDeltakelseDAO.finnAlle())
            System.out.println("\t" + pd);
    }
}