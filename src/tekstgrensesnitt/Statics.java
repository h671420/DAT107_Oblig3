package tekstgrensesnitt;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class Statics {
    static Scanner scanner = Tekstgrensesnitt.scanner;

    static LocalDate getDateInput2(String datoString) {
        LocalDate dato = null;
        boolean running = true;
        while (running) {
            System.out.println("Innlesing av "+datoString);
            System.out.println("Returner '1' for å avbryte, '2' for å bruke dags dato eller returner en dato på formatet 'YYYY-MM-DD'");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                case "2":
                    dato = LocalDate.now();
                    running=false;
                    break;
                default:
                    try {
                        dato = LocalDate.parse(input);
                        running = false;
                    } catch (DateTimeException e) {
                        System.out.println("'" + input + "' er ikke en gyldig dato.");
                        break;
                    }
            }
        }
        return dato;
    }
    static Integer getIntegerInput2(String enEllerEt, String object){
        Integer mndlonn = null;
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner "+enEllerEt+" "+object+", eller returner '1' for å avbryte");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    mndlonn = null;
                    break;
                default:
                    try {
                        mndlonn = Integer.parseInt(input);
                        if (mndlonn <= 0)
                            throw new NumberFormatException();
                        running = false;
                    } catch (NumberFormatException e) {
                        System.out.println("'" + input + "' er ikke "+enEllerEt+" gyldig "+object);
                    }
            }
        }
        return mndlonn;
    }
    static String getStringInput2(int minimumslengde, String enEllerEt, String object, boolean navn){
        String svar = null;
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner "+enEllerEt+" "+object+", eller returner '1' for å avbryte");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (input.length() < minimumslengde) {
                        System.out.println("'" + input + "' er ikke "+enEllerEt +" "+ object);
                        break;
                    } else {
                        running = false;
                        svar = input.toLowerCase();
                        if (navn) {
                            String[] svarArr = svar.split(" ");
                            svar = "";
                            for (String s : svarArr)
                                svar += s.substring(0, 1).toUpperCase() + s.substring(1) + " ";
                            svar = svar.substring(0, svar.length() - 1);
                        }
                    }
            }
        }
        return svar;
    }
    static String getStringInput(int minimumslengde, String enEllerEt, String object, boolean navn) {
        String svar = null;
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner "+enEllerEt+" "+object);
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    break;
                default:
                    if (input.length() < minimumslengde) {
                        System.out.println("'" + input + "' er ikke "+enEllerEt +" "+ object);
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
                        System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte '" + svar + "'");
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
    static Integer getIntegerInput(String enEllerEt, String object) {
        Integer mndlonn = null;
        boolean running = true;
        while (running) {
            System.out.println("Vennligst returner "+enEllerEt+" gyldig "+object+", eller returner '1' for å avbryte");
            String input = scanner.nextLine();
            switch (input) {
                case "1":
                    running = false;
                    mndlonn = null;
                    break;
                default:
                    try {
                        mndlonn = Integer.parseInt(input);
                        if (mndlonn <= 0)
                            throw new NumberFormatException();
                        System.out.println("Returner '1' for å avbryte, '2' for å forsøke på nytt eller trykk enter for å bekrefte '" + mndlonn + "'");
                        input = scanner.nextLine();
                        switch (input) {
                            case "1":
                                mndlonn = null;
                                running = false;
                                break;
                            case "2":
                                break;
                            case "":
                                running = false;
                                break;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("'" + input + "' er ikke "+enEllerEt+" gyldig "+object);
                    }
            }
        }
        return mndlonn;
    }
    static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
        System.out.println("\n");
    }

    static public String deler() {
        return "--------------------------------------------------------------------------------------------------------------";
    }
    public static String hoyrePad(String input, int bredde){
        while (input.length()<bredde)
            input=input+" ";
        return input;
    }
}
