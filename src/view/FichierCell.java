
package view;


import javafx.scene.control.cell.TextFieldTreeTableCell;
import model.Fichier;

public abstract class FichierCell extends TextFieldTreeTableCell<Fichier, Fichier> {

    private static final String CSSPATH = "view/css.css";

    public FichierCell() {
        getStylesheets().add(CSSPATH);
    }
    @Override
    public void updateItem(Fichier elem, boolean isEmpty) {
        super.updateItem(elem, isEmpty);
        if (elem == null) {
            return;
        }
        this.setText(texte(elem));
        this.getStyleClass().set(0, elem.getEtat().toString());
    }

    abstract String texte(Fichier elem);

}

