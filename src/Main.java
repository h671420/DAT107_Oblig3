import DAO.Dao;
import entities.Ansatt;
import entities.Avdeling;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Main {
    public static void main(String[] args) {

        Dao dao = new Dao();


//        dao.addAnsatt(new Ansatt(),1);
//        dao.addAvdeling(new Avdeling(),2);


//        dao.slettAnsatt(1);
//        dao.slettAvdeling(1);
        dao.finnAvdelinger().forEach(System.out::println);
        dao.finnAnsatte().forEach(System.out::println);
    }
}