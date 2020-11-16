package model;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author MediaMonster
 */
public abstract class Fichier {

    //private String nom;
    protected Path path;
    private  LocalDateTime lastModification;
    protected  Etat etat = Etat.UNKNOW;
    protected boolean selected;
    protected boolean selectedType;
    
    public Fichier(Path path, LocalDateTime lastModification) {
        this.path = path;
        this.lastModification = lastModification;
        this.selected = true;
        this.selectedType = true;
    }
    
    protected LocalDateTime getLastModification() {
       return lastModification;
    }

    public Boolean getSelected() {
        return selected;
    }
    
    public Boolean getSelectedType() {
        return selectedType;
    }
    
    public abstract void setSelectedEtat(Set<Etat> e);
    
    public abstract void setSelectedType(Set<Type> t);

    public String getNom() {
        return path.getFileName().toString();
    }

    public Path getPath() {
        return path;
    }

    protected String formatAffichage(int decalage) {
        String res = "";
        for (int i = 0; i < decalage; ++i) {
            res += "\t";
        }
        return res += " ";
    }

    @Override
    public String toString() {
        return formatAffichage(0);
    }

    public abstract long getSize();

    public abstract Etat getEtat();

    public abstract boolean estUnDossier();
    
    public abstract void setEtat(Fichier f);

    public boolean isCorrespondant(Path A) {
        return this.equals(A);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof Fichier) {
            Fichier f = (Fichier) o;
            if (f.getNom().equals(this.getNom())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
    
    public int compareTo(Object o) {
        if (o instanceof Fichier)
        { 
        Fichier c = (Fichier)o;
        return getNom().compareTo(c.getNom());
        }
        else 
        {
        throw new RuntimeException("cant compare different types");
        }
    }
    

    public boolean isCorrespondant(){
        return true;
    }
    public boolean isNotCorrespondant(){
        return false;
    }
    public abstract Iterable<Fichier> fichiers();

    
    
//    @Override
//    public int compareTo(Fichier f) {
//        return this.getPath().compareTo(f.getPath());
//    }

//    public Etat isOrphan(Path A) {
//        return Etat.ORPHAN;
//    }
//
//    public Etat isSame(Path A) {
//        return Etat.SAME;
//    }
//
//    public Etat isNewer(Path A) {
//        return Etat.NEWER;
//    }
//
//    public Etat isOlder(Path A) {
//        return Etat.OLDER;
//    }
//
//    public Etat isPartialSame(Path A) {
//        return Etat.PARTIAL_SAME;
//    }

//    
//    public void Compare(Fichier A, Fichier B){
//        FileBuilder fb = null;
//        if(fb.Fichier A.getNom().equals(fb.Fichier B.getNom())){
//            for(Fichier f: fb.buildTree(path)){
//                if(f.Type == DOSSIER){
//                    
//                }
//                
//            }
//        }
//    } 
}
