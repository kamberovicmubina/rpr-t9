package ba.unsa.etf.rpr;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class IzgledController implements Initializable {
    @FXML private Spinner<Integer> idSpinner;
    @FXML private Spinner<Integer> brStSpinner;
    @FXML private TextField nazivGradaTxt;
    @FXML private TextField nazivDrzaveGradTxt;
    private GeografijaDAO ge;
    @FXML private Spinner<Integer> idDrzavaSpinner;
    @FXML private TextField glavniGradTxt;
    @FXML private TextField nazivDrzavaTxt;
    @FXML private TextField glavniGradUpis;
    @FXML private TextArea tekst;
    private boolean gNaziv = false, gDrzava = false, gId, dNaziv = false, dGlGrad = false;

    public IzgledController (GeografijaDAO ge) {
        this.ge = ge;
    }

    @Override
    public void initialize (URL url, ResourceBundle rb) {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        idSpinner.setValueFactory(valueFactory);

        SpinnerValueFactory<Integer> valueFactory2 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100000000);
        brStSpinner.setValueFactory(valueFactory2);

        SpinnerValueFactory<Integer> valueFactory3 = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10000);
        idDrzavaSpinner.setValueFactory(valueFactory3);

        nazivGradaTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String o, String n) {
                    if (validanNaziv(nazivGradaTxt.getText())) {
                        nazivGradaTxt.getStyleClass().removeAll("poljeNijeIspravno");
                        nazivGradaTxt.getStyleClass().add("poljeIspravno");
                        gNaziv = true;
                    } else {
                        nazivGradaTxt.getStyleClass().removeAll("poljeIspravno");
                        nazivGradaTxt.getStyleClass().add("poljeNijeIspravno");
                        gNaziv = false;
                    }
            }
        });

        nazivDrzaveGradTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String o, String n) {
                    if (validanNaziv(nazivDrzaveGradTxt.getText()) && ge.nadjiDrzavu(nazivDrzaveGradTxt.getText()) != null) {
                        nazivDrzaveGradTxt.getStyleClass().removeAll("poljeNijeIspravno");
                        nazivDrzaveGradTxt.getStyleClass().add("poljeIspravno");
                        gDrzava = true;
                    } else {
                        nazivDrzaveGradTxt.getStyleClass().removeAll("poljeIspravno");
                        nazivDrzaveGradTxt.getStyleClass().add("poljeNijeIspravno");
                        gDrzava = false;
                    }
            }
        });

        nazivDrzavaTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String o, String n) {
                if (validanNaziv(nazivDrzavaTxt.getText())) {
                    nazivDrzavaTxt.getStyleClass().removeAll("poljeNijeIspravno");
                    nazivDrzavaTxt.getStyleClass().add("poljeIspravno");
                    dNaziv = true;
                } else {
                    nazivDrzavaTxt.getStyleClass().removeAll("poljeIspravno");
                    nazivDrzavaTxt.getStyleClass().add("poljeNijeIspravno");
                    dNaziv = false;
                }
            }
        });

        glavniGradTxt.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> obs, String o, String n) {
                if (validanNaziv(glavniGradTxt.getText())) {
                    glavniGradTxt.getStyleClass().removeAll("poljeNijeIspravno");
                    glavniGradTxt.getStyleClass().add("poljeIspravno");
                    dGlGrad = true;
                } else {
                    glavniGradTxt.getStyleClass().removeAll("poljeIspravno");
                    glavniGradTxt.getStyleClass().add("poljeNijeIspravno");
                    dGlGrad = false;
                }
            }
        });
    }

    public boolean validanNaziv (String n) {
        for (char c : n.toCharArray())
        {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        boolean imaSlovo = false;
        for (int i = 0; i < n.length(); i++) {
            if (((n.charAt(i) >= 'a' && n.charAt(i) <= 'z') || (n.charAt(i) >= 'A' && n.charAt(i) <= 'Z'))) {
                imaSlovo = true;
                break;
            }
        }
        return !n.trim().isEmpty() && n.trim().length() <= 20 && imaSlovo;
    }

    public boolean validanGrad () {
        return gNaziv && gDrzava;
    }

    public boolean validnaDrzava () {
        return dNaziv && dGlGrad; // provjeriti za glavni grad da li postoji(validcija)
    }

    public void azuriraj () {
        ArrayList<Grad> gradovi = ge.gradovi();
        for (Grad g : gradovi) {
            if (validanGrad() && g.getId() == idSpinner.getValue()) {
                Grad noviGrad = new Grad();
                noviGrad.setNaziv(nazivGradaTxt.getText());
                noviGrad.setBrojStanovnika(brStSpinner.getValue());
                Drzava d = ge.nadjiDrzavu(nazivDrzaveGradTxt.getText());
                noviGrad.setDrzava(d);
                ge.izmijeniGrad(noviGrad);
            }
        }
    }

    public void dodajGrad () {
        if (validanGrad()) {
            // osigurali smo da grad nece biti validan ako vec nismo prije unijeli drzavu
            Grad g = new Grad(idSpinner.getValue(), nazivGradaTxt.getText(), brStSpinner.getValue(), null);
            Drzava d = ge.nadjiDrzavu(nazivDrzaveGradTxt.getText());
            g.setDrzava(d);
            ge.dodajGrad(g);
          /*  if (ge.nadjiDrzavu(nazivDrzaveGradTxt.getText()) != null) { // vec je registrovana drzava
                g.setDrzava(ge.nadjiDrzavu(nazivDrzaveGradTxt.getText()));
                return;
            }
            Drzava d = new Drzava();
            d.setId(idDrzavaSpinner.getValue());
            d.setNaziv(nazivDrzavaTxt.getText());*/

        }
    }

    public void dodajDrzavu () {
        if (validnaDrzava()) {
            Drzava d = new Drzava();
            d.setId(idDrzavaSpinner.getValue());
            d.setNaziv(nazivDrzavaTxt.getText());
            ArrayList<Grad> gradovi = ge.gradovi();
            for (Grad g : gradovi) {
                if (g.getId() == idSpinner.getValue()) {
                    d.setGlavniGrad(g);
                    ge.getDrzave().add(d);
                    return;
                }
            }
            Grad g = new Grad(idSpinner.getValue(), nazivGradaTxt.getText(), brStSpinner.getValue(), null);
            ge.dodajGrad(g);
            d.setGlavniGrad(g);
            ge.getDrzave().add(d);
        }
    }


    public void obrisiDrzavu () {
        if (validnaDrzava() && ge.nadjiDrzavu(nazivDrzavaTxt.getText()) != null) {
            ge.obrisiDrzavu(nazivDrzavaTxt.getText());
        } else {
            nazivDrzavaTxt.getStyleClass().removeAll("Polje ispravno");
            nazivDrzavaTxt.getStyleClass().add("Polje nije ispravno");
            dNaziv = false;
        }
    }

    public void sviGradovi () {
        ArrayList<Grad> gradovi = GeografijaDAO.getInstance().gradovi();
        String rezultat = "";
        if (gradovi != null) {
            for (Grad g : gradovi) {
                rezultat = rezultat + g.toString() + "\n";
            }
            tekst.setText(rezultat);
        } else {
            tekst.setText("Nema gradova");
        }
    }

    public void ispisiGlGrad () {
        Drzava d = ge.nadjiDrzavu(glavniGradUpis.getText());
        if (d == null) {
            glavniGradUpis.getStyleClass().add("Polje nije ispravno");
            return;
        }
        tekst.setText(d.getGlavniGrad().getNaziv());
    }

}
