
package view;

import model.Fichier;

/**
 *
 * @author marce
 */
public class NomFichierCell extends FichierCell {

    @Override
    String texte(Fichier elem) {
        return elem.getNom();
    }
}
    

