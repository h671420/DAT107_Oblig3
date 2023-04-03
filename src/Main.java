import DAO.ansattDAO;
import DAO.avdelingDAO;
import entities.Ansatt;
import entities.Avdeling;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    static ansattDAO ansattDAO = new ansattDAO();
    static avdelingDAO avdelingDAO = new avdelingDAO();

    public static void main(String[] args) {
        Ansatt jonny =new Ansatt("Jonny", "Cakes");
        Ansatt  rocky = new Ansatt("Rocky", "Rock");
        Ansatt asgeir = new Ansatt("Asgeir", "Slettemoen");
        Ansatt roar = new Ansatt("Roar", "Larsen");
        Ansatt timmy = new Ansatt("Timmy", "Teigen");
        Ansatt lars = new Ansatt("Lars", "Larsen");

//        ansattDAO.addAnsatt(jonny,1);
//        ansattDAO.addAnsatt(rocky,1);
//        ansattDAO.addAnsatt(asgeir,1);
//        ansattDAO.addAnsatt(roar,1);
//        ansattDAO.addAnsatt(timmy,1);
//        ansattDAO.addAnsatt(lars,1);


//        ansattDAO.slettAnsatt(13);
//        ansattDAO.slettAnsatt(12);
//        ansattDAO.slettAnsatt(11);
//        ansattDAO.slettAnsatt(10);
//        ansattDAO.slettAnsatt(9);
//        ansattDAO.slettAnsatt(8);
//        avdelingDAO.addAvdeling(new Avdeling("Kontoret"), 2);
//        avdelingDAO.addAvdeling(new Avdeling("Lager"), 3);
//        avdelingDAO.addAvdeling(new Avdeling("Gården"), 5);
//        avdelingDAO.slettAvdeling(2);
          ansattDAO.flyttAnsatt(7,2);
        visAnsatte();
        visAvdelinger();


    }

    private static void visAvdelinger() {
        System.out.println("Databasens avdelinger");
        for (Avdeling a : avdelingDAO.finnAvdelinger())
            System.out.println("\t" + a);
    }

    private static void visAnsatte() {
        System.out.println("Databasens ansatte");
        for (Ansatt a : ansattDAO.finnAnsatte())
            System.out.println("\t" + a);
    }
}