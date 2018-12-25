package ba.unsa.etf.rpr;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        GeografijaDAO.getInstance();
        System.out.println("Gradovi su:\n" + ispisiGradove());
        glavniGrad();
        GeografijaDAO.removeInstance();
    }

    public static void glavniGrad () {
        System.out.println("Unesite naziv drzave: ");
        Scanner sc = new Scanner(System.in);
        String ime = sc.nextLine();
        Grad grad = GeografijaDAO.getInstance().glavniGrad(ime);

        if(grad == null) {
            System.out.println("Nepostojeca drzava");
            return;
        }

        System.out.println("Glavni grad drzave " + ime + " je " + grad.getNaziv() + ".");
    }

    public static String ispisiGradove () {
        ArrayList<Grad> gradovi = GeografijaDAO.getInstance().gradovi();
        String rezultat = "";
        for (Grad g : gradovi) {
            rezultat = rezultat + g + "\n";
        }
        return rezultat;
    }
}
