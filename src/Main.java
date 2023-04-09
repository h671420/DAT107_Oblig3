import DAO.AnsattDAO;
import DAO.AvdelingDAO;
import DAO.ProsjektDAO;
import DAO.ProsjektDeltakelseDAO;
import tekstgrensesnitt.grensesnittentiteter.AnsattGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.AvdelingGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.ProsjektGrensesnitt;
import tekstgrensesnitt.grensesnittentiteter.ProsjektDeltakelseGrensesnitt;

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
//        ProsjektGrensesnitt pro = new ProsjektGrensesnitt("mobilapp", "ny fancy mobilapp");
//        ProsjektGrensesnitt pro2 = new ProsjektGrensesnitt("digitalisering", "digitalisering av noen saker");
//        ProsjektGrensesnitt pro3 = new ProsjektGrensesnitt("nedbemanning", "dessverre dårlige tider");
//        ansattDAO.flyttAnsatt(4,3);
//        ansattDAO.flyttAnsatt(6,2);
//        ansattDAO.flyttAnsatt(7,2);
//        prosjektDAO.addProsjekt(pro);
//        prosjektDAO.addProsjekt(pro2);
//        prosjektDAO.addProsjekt(pro3);
    }

    private static void leggTilNoenAvdelinger() {
//        avdelingDAO.addAvdeling(new Avdeling("Kontoret"), 2);
//        avdelingDAO.addAvdeling(new Avdeling("Lager"), 3);
//        avdelingDAO.addAvdeling(new Avdeling("Gården"), 5);
    }

    private static void leggTilNoenAnsatte() {
//        AnsattGrensesnitt jonny = new AnsattGrensesnitt("Jonny", "Cakes");
//        AnsattGrensesnitt rocky = new AnsattGrensesnitt("Rocky", "Rock");
//        AnsattGrensesnitt asgeir = new AnsattGrensesnitt("Asgeir", "Slettemoen");
//        AnsattGrensesnitt roar = new AnsattGrensesnitt("Roar", "Larsen");
//        AnsattGrensesnitt timmy = new AnsattGrensesnitt("Timmy", "Teigen");
//        AnsattGrensesnitt lars = new AnsattGrensesnitt("Lars", "Larsen");
//        ansattDAO.addAnsatt(jonny, 1);
//        ansattDAO.addAnsatt(rocky, 1);
//        ansattDAO.addAnsatt(asgeir, 1);
//        ansattDAO.addAnsatt(roar, 1);
//        ansattDAO.addAnsatt(timmy, 1);
//        ansattDAO.addAnsatt(lars, 1);
    }

    private static void visAvdelinger() {
        System.out.println("Databasens avdelinger");
        for (AvdelingGrensesnitt a : avdelingDAO.finnAlle())
            System.out.println("\t" + a);
    }

    private static void visAnsatte() {
        System.out.println("Databasens ansatte");
        for (AnsattGrensesnitt a : ansattDAO.finnAlle())
            System.out.println("\t" + a);
    }

    private static void visProsjekter() {
        System.out.println("Databasens Prosjekter");
        for (ProsjektGrensesnitt p : prosjektDAO.finnAlle())
            System.out.println("\t" + p);
    }

    private static void visProsjektDeltakelser() {
        System.out.println("Databasens ProsjektDeltakelser");
        for (ProsjektDeltakelseGrensesnitt pd : prosjektDeltakelseDAO.finnAlle())
            System.out.println("\t" + pd);
    }
}