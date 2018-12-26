package ba.unsa.etf.rpr;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;

import java.util.ArrayList;


public class IzgledController {
    @FXML private Spinner<Integer> idSpinner;
    @FXML private Spinner<Integer> brStSpinner;
    @FXML private TextField nazivGradaTxt;
    @FXML private TextField nazivDrzaveGradTxt;
    private GeografijaDAO ge;

    public IzgledController (GeografijaDAO ge) {
        this.ge = ge;
    }

    public void azuriraj () {
        ArrayList<Grad> gradovi = ge.gradovi();
        for (Grad g : gradovi) {
            if (g.getId() ==  idSpinner.getValue()) {
                ge.izmijeniGrad(g);
            }
        }
    }

    public void dodaj () {
        Grad g = new Grad (idSpinner.getValue(), nazivGradaTxt.getText(), brStSpinner.getValue(), null);
        ge.dodajGrad(g);
        // dodati drzavu gradu
    }

}
