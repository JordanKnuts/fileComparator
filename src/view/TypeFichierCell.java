
package view;

import model.Fichier;

/**
 *
 * @author marce
 */
public class TypeFichierCell extends FichierCell{

    @Override
    String texte(Fichier elem) {
        
        if(elem.estUnDossier()){

            return "D";
        }
        else{
           return  "F";
        }
        
        
    }

}
