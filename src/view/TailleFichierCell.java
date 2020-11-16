
package view;

import model.Fichier;

/**
 *
 * @author marce
 */
public class TailleFichierCell extends FichierCell {

    @Override
    String texte(Fichier elem) {
        return ""+elem.getSize();
    }
    
}