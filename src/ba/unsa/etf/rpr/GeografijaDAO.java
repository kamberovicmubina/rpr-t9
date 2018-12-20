package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn = null;
    private Statement drzavaStatement;
    private static void initialize() {
        instance = new GeografijaDAO();
    }
    public static GeografijaDAO getInstance () {
        return instance;
    }
    private GeografijaDAO () {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db" );
           // statement = conn.createStatement();
            /*String strSelect = "select naziv, broj_stanovnika from Grad";
            System.out.println("The SQL query is: " + strSelect);
            System.out.println();

            ResultSet rset = statement.executeQuery(strSelect);
            System.out.println("The records selected are:");
            int rowCount = 0;
            while(rset.next()) {
                String title = rset.getString("title");
                double price = rset.getDouble("price");
                int    qty   = rset.getInt("qty");
                System.out.println(title + ", " + price + ", " + qty);
                ++rowCount;
            }
            System.out.println("Total number of records = " + rowCount);*/
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pripremiUpite();
    }

    private void pripremiUpite () {
        drzavaStatement = conn.prepareStatement("select id, naziv, glavniGrad from drzava where id=?");

    }
    Grad glavniGrad(String drzava) {
        return null;
    }
    void obrisiDrzavu(String drzava) {

    }
    ArrayList<Grad> gradovi() {
        return null;
    }
    void dodajGrad(Grad grad){

    }
    void dodajDrzavu(Drzava drzava) {

    }
    void izmijeniGrad(Grad grad) {

    }
    Drzava nadjiDrzavu(String drzava) {
        return null;
    }
    static void removeInstance () {
        instance = null;
    }
}
