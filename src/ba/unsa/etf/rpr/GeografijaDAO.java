package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn = null;
    private Statement drzavaStatement, drzaveStatement, gradStatement, glavniGradStatement;
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
        try {
            drzavaStatement = conn.prepareStatement("select id, naziv, glavni_grad from drzava where id=?");
        } catch (SQLException e) {
            e.printStackTrace();
            //createdb
        }
        pripremiUpite();
    }

    private void pripremiUpite () {
        try {
            drzaveStatement = conn.prepareStatement("select id, naziv, glavni_grad from drzava");
            drzavaStatement = conn.prepareStatement("select id, naziv, glavni_grad from drzava where naziv=?");
            gradStatement = conn.prepareStatement("select id, naziv, glavni_grad from grad where id=?");
            glavniGradStatement = conn.prepareStatement("select g.naziv from grad g, drzava d where g.id=d.glavni_grad and d.glavni_grad=?");


        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    Grad glavniGrad(String drzava) {
      //  drzavaStatement.setInt(1);

        ResultSet r = drzaveStatement.executeQuery();
        boolean nalaziSe = false:
        while (r.next()) {
            String ime = r.getString("naziv");
            if (ime.equals(drzava)) {
                drzavaStatement.setString(1,ime); // prvi upitnik u upitu
                ResultSet r2 = drzavaStatement.executeQuery();
                String grad = drzavaStatement.getString("naziv");

            }
            r.close();
        }
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
