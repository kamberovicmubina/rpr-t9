package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private Connection conn = null;
    private PreparedStatement drzavaStatement, glavniGradStm, gradByNazivStm, gradByIdStm, editGradStm, drzavaByNazivStm,
            deleteGradByDrzavaIdStm, deleteDrzavaByNaziv, ubaciDrzavuStm, ubaciGradStm;
    private static void initialize() {
        instance = new GeografijaDAO();
    }
    public static GeografijaDAO getInstance () {
        return instance;
    }
    private GeografijaDAO () {
        try {
            //conn = DriverManager.getConnection("jdbc:sqlite:baza.db" );
            conn = DriverManager.getConnection("\"jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB\",\"MK18290\",\"r2Cjv03n\"");
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

        }
        pripremiUpite();
    }

    private void pripremiUpite () {
        try {
            glavniGradStm = conn.prepareStatement("select g.id, g.naziv, g.broj_stanovnika, d.id, d.naziv from grad g inner join drzava d on g.id = d.glavni_grad where d.naziv=?");
            gradByNazivStm = conn.prepareStatement("select id, naziv from grad where naziv = ?");
            gradByIdStm = conn.prepareStatement("select id,naziv,broj_stanovnika from grad where id =?");
            drzavaByNazivStm = conn.prepareStatement("select id,naziv,glavni_grad from drzava where naziv = ?");
            deleteGradByDrzavaIdStm = conn.prepareStatement("DELETE FROM grad WHERE drzava = ?");
            deleteDrzavaByNaziv = conn.prepareStatement("DELETE FROM drzava WHERE naziv = ?");
            ubaciDrzavuStm = conn.prepareStatement("INSERT INTO drzava VALUES(?,?,?)");
            ubaciGradStm = conn.prepareStatement("INSERT INTO grad VALUES(?,?,?,?)");
            editGradStm = conn.prepareStatement("UPDATE grad SET naziv = ?, broj_stanovnika=? WHERE id = ?");

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    Grad glavniGrad(String drzava) {
        ArrayList<Grad> gradovi = gradovi();
        if (gradovi == null) {
            return null;
        }
        for (Grad g : gradovi) {
            if (g.getDrzava().getNaziv().equals(drzava)) {
                return g;
            }
        }
       return null;
    }
    void obrisiDrzavu(String drzava) {
        try {
            deleteDrzavaByNaziv.clearParameters();
            deleteDrzavaByNaziv.setString(1, drzava);
            ResultSet rs = deleteDrzavaByNaziv.executeQuery();
        } catch (SQLException e) {

        }

    }
    ArrayList<Grad> gradovi() {
        ArrayList<Grad> gradovi = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement("select g.id, g.naziv, g.broj_stanovnika, d.id, d.naziv, d.glavni_grad" +
                    "from grad g, drzava d where g.drzava = d.id;");
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Grad g = new Grad (rs.getInt(1), rs.getString(2), rs.getInt(3), null);
                Drzava d = new Drzava (rs.getInt(4), rs.getString(5), null);
                if (rs.getInt(1) == rs.getInt(6)) {
                    d.setGlavniGrad(g);
                } else {
                    gradByIdStm.setInt(1, rs.getInt(6));
                    ResultSet rs2 = gradByIdStm.executeQuery();
                    d.setGlavniGrad(new Grad (rs.getInt(6), rs2.getString(2), rs2.getInt(3), d));

                }
                g.setDrzava(d);
                gradovi.add(g);
            }
        } catch (SQLException e) {

        }
        gradovi.sort(new Comparator<Grad>() {
            @Override
            public int compare(Grad o1, Grad o2) {
                Integer a=o2.getBrojStanovnika();
                Integer b=o1.getBrojStanovnika();
                return a.compareTo(b);
            }
        });
        return gradovi;
    }
    void dodajGrad(Grad grad){

        try {
            ubaciGradStm.clearParameters();
            ubaciGradStm.setInt(1, grad.getId());
            ubaciGradStm.setString(2, grad.getNaziv());
            ubaciGradStm.setInt(3, grad.getBrojStanovnika());
            ubaciGradStm.setInt(4, grad.getDrzava().getId());
            ubaciGradStm.executeUpdate();
        } catch(SQLException e) {

        }
    }
    void dodajDrzavu(Drzava drzava) {

        try  {
            ubaciDrzavuStm.clearParameters();
            ubaciDrzavuStm.setInt(1, drzava.getId());
            ubaciDrzavuStm.setString(2, drzava.getNaziv());
            ubaciDrzavuStm.setInt(3, drzava.getGlavniGrad().getId());
            ubaciDrzavuStm.executeUpdate();
        } catch (SQLException e) {

        }
    }
    void izmijeniGrad(Grad grad) {

        try {
            editGradStm.clearParameters();
            editGradStm.setString(1, grad.getNaziv());
            editGradStm.setInt(2, grad.getBrojStanovnika());
            editGradStm.setInt(3, grad.getId());
            editGradStm.executeUpdate();
        } catch(SQLException e) {

        }
    }
    Drzava nadjiDrzavu(String drzava) {
        return null;
    }
    static void removeInstance () {
        instance = null;
    }
}
