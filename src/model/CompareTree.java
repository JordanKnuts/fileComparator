/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 *
 * @author MediaMonster
 */
public class CompareTree {

    public static void compareTree(Fichier A, Fichier B) {

        if (A.estUnDossier()) {
            Dossier dA = (Dossier) A;

            for (Fichier fileA : dA.fichiers()) {
                if (fileA.estUnDossier()) {
                    compareTree(fileA, B);
                }
                searchTree(fileA, B);
            }
        }
    }

    public static void searchTree(Fichier toSearch, Fichier dossier) {

        if (dossier.estUnDossier()) {

            Dossier root = (Dossier) dossier;

            for (Fichier f : root.fichiers()) {

                File a = new File(f.getPath().toString());
                File b = new File(toSearch.getPath().toString());

                String aa = a.getParentFile().getName();
                String bb = b.getParentFile().getName();
                //Conserver le Parent pour la comparaison
                
                
                
                if (f.estUnDossier()) {
                    //f.setEtat(toSearch);
                    searchTree(toSearch, f);

                }
               
                if (aa.equals(bb) &&  f.equals(toSearch)) {
                    f.setEtat(toSearch);
                    //System.out.println(f);
                } else if(f.etat == Etat.UNKNOW && !f.estUnDossier())
                    f.etat=Etat.ORPHAN;
                    
            }

        }

    }

}
