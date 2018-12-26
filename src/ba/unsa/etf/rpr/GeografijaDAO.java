package ba.unsa.etf.rpr;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;

public class GeografijaDAO {
    private static GeografijaDAO instance = null;
    private static Connection conn;
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
            conn = null;
            conn = DriverManager.getConnection("jdbc:sqlite:baza.db" );
            //conn = DriverManager.getConnection("\"jdbc:oracle:thin:@ora.db.lab.ri.etf.unsa.ba:1521:ETFLAB\",\"MK18290\",\"r2Cjv03n\"");
            Statement statement = null;
            try {
                statement = conn.createStatement();
                statement.execute("select id, naziv, glavni_grad from drzava;");
            } catch (Exception e) {
                Statement statement2=null;
                statement2 = conn.createStatement();
                statement2.execute("CREATE TABLE grad(id integer primary key, naziv text, broj_stanovnika integer)");
                statement2.execute("CREATE TABLE drzava(id integer primary key, naziv text, glavni_grad integer unique references grad(id))");
                statement2.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (1, \"Pariz\", 2200000, 1);");
                statement2.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (2, \"London\", 8136000, 2);");
                statement2.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (3, \"Bec\", 1868000, 3);");
                statement2.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (4, \"Manchester\", 510746, 4);");
                statement2.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (5, \"Graz\", 283869, 5);");
                statement2.execute("insert into drzava (id, naziv, glavni_grad) values (1, \"Francuska\", 1);");
                statement2.execute("insert into drzava (id, naziv, glavni_grad) values (2, \"Velika Britanija\", 2);");
                statement2.execute("insert into drzava (id, naziv, glavni_grad) values (3, \"Austrija\", 3);");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }







            // kreiraj bazu
            /*try {
                Statement statement = null;
                statement = conn.createStatement();
                statement.execute("CREATE TABLE drzava(id INTEGER PRIMARY KEY ,naziv varchar(255) not null, broj_stanovnika integer, glavni_grad integer )");
                statement.execute("CREATE TABLE grad(id integer primary key, naziv varchar(255), broj_stanovnika INTEGER,drzava integer)");

                /*statement.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (1, \"Pariz\", 2200000, 1);");
                statement.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (2, \"London\", 8136000, 2);");
                statement.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (3, \"Bec\", 1868000, 3);");
                statement.execute("insert into grad (id, naziv, broj_stanovnika, drzava) values (4, \"Manchester\", 510746, 4);");
                statement = conn.prepareStatement("insert into grad (id, naziv, broj_stanovnika, drzava) values (5, \"Graz\", 283869, 5);");

                statement = conn.prepareStatement("insert into drzava (id, naziv, glavni_grad) values (1, \"Francuska\", 1);");
                statement.executeQuery();
                statement = conn.prepareStatement("insert into drzava (id, naziv, glavni_grad) values (2, \"Velika Britanija\", 2);");
                statement.executeQuery();
                statement = conn.prepareStatement("insert into drzava (id, naziv, glavni_grad) values (3, \"Austrija\", 3);");
                statement.executeQuery();
            } catch (SQLException f) {
                f.printStackTrace();
            }


        try {
            drzavaStatement = conn.prepareStatement("select id, naziv, glavni_grad from drzava where id=?");
        } catch (SQLException e) {
            e.printStackTrace();

        }*/
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
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
            e.printStackTrace();
        }
    }
    Drzava nadjiDrzavu(String drzava) {
        Drzava d = new Drzava();
        try {
            drzavaByNazivStm.clearParameters();
            drzavaByNazivStm.setString(1, drzava);
            ResultSet rs = drzavaByNazivStm.executeQuery();
            while (rs.next()) {
                d.setId(rs.getInt(1));
                d.setNaziv(rs.getString(2));;
                Grad g = new Grad ();
                glavniGradStm.clearParameters();
                glavniGradStm.setString(1, rs.getString(2));
                ResultSet rs2 = glavniGradStm.executeQuery();
                while (rs2.next()) {
                    g.setId(rs2.getInt(1));
                    g.setNaziv(rs2.getString(2));
                    g.setBrojStanovnika(rs2.getInt(3));
                    g.setDrzava(d);
                }
                d.setGlavniGrad(g);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return d;
    }
    static void removeInstance () {
        instance = null;
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        conn = null;
    }
}
