
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

public class FichierSimple extends Fichier {

    private final long size;

    private final Type type = Type.FICHIER;

    public FichierSimple(Path path, long size, LocalDateTime lastModification) {
        super(path, lastModification);
        this.size = size;

    }

    public Type getType() {
        return type;
    }

    @Override
    public long getSize() {
        return size;
    }

    public void setSelectedType(Set<Type> t) {
        if (t.isEmpty()) {
            this.selectedType = true;
        } else {
            this.selectedType = false;

        }
    }

    public void setSelectedEtat(Set<Etat> e) {
        if (e.isEmpty()) {
            this.selected = true;
        } else {
            this.selected = e.stream().anyMatch(ee -> ee == etat);
        }
    }

    @Override
    protected String formatAffichage(int decalage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        return super.formatAffichage(decalage)
                + getNom()
                + "  F  "
                + getLastModification().format(formatter)
                + " " + getSize() + " " + getEtat().toString()
                + " "
                + getSelected()
                + "\n";
    }

    @Override
    public Etat getEtat() {
        return etat;
    }

//    public int compareTo(Fichier o) {
//        return this.getPath().compareTo(o.getPath());    
//    }
    @Override
    public String toString() {
        return formatAffichage(0);
    }

//    @Override
//    public String getEtat(Etat e) {
//        
//        
//    }
    @Override
    public boolean estUnDossier() {
        return false;
    }

    @Override
    public void setEtat(Fichier f) {

        int resD = getLastModification().compareTo(f.getLastModification());

        etat = Etat.ORPHAN;
        if (resD == 0) {
            etat = Etat.SAME;
        } else if (resD < 0) {
            etat = Etat.NEWER;
        } else if (resD > 0) {
            etat = Etat.OLDER;
        }
    }

    @Override
    public Iterable<Fichier> fichiers() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
