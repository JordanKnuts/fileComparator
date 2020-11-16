
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import static java.util.stream.Collectors.toList;

public class Dossier extends Fichier {

    private final Type type = Type.DOSSIER;

    private final List<Fichier> contenu = new ArrayList<>();

    public Dossier(Path path, LocalDateTime lastModification) {
        super(path, lastModification);

    }

    public Type getType() {
        return type;
    }
    

    public void addFichier(Fichier f) {
        contenu.add(f);
    }

    public Iterable<Fichier> fichiers() {
        return contenu;
    }

    @Override
    public long getSize() {
        long res = 0;
        for (Fichier f : contenu) {
            res += f.getSize();
        }
        return res;
    }
    
    public void setSelectedType(Set<Type> t){
        this.selectedType = true;

    }

    public void setSelectedEtat(Set<Etat> e) {
        if (e.isEmpty()) {
            this.selected = true;
        } else {
            boolean childSelected = contenu.stream().anyMatch(f -> f.selected == true);
            this.selected = childSelected;
        }
    }

    @Override
    public Etat getEtat() {
        if (etat == etat.UNKNOW) {
            List<Etat> listEtat = contenu.stream().map(e -> e.getEtat()).collect(toList());
            boolean isSame = listEtat.stream().allMatch(e -> e == Etat.SAME);
            boolean isOrphan = listEtat.stream().allMatch(e -> e == Etat.ORPHAN);

            if (isOrphan || contenu.isEmpty()) {
                etat = etat.ORPHAN;
            } else if (isSame) {
                etat = etat.SAME;
            } else {
                etat = etat.PARTIAL_SAME;
            }
        }
        return etat;

    }

    @Override
    protected String formatAffichage(int decalage) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        StringBuilder res = new StringBuilder();
        res.append(super.formatAffichage(decalage))
                .append(getNom() + " D ")
                .append(getLastModification().format(formatter))
                .append(" " + getSize())
                .append("  " + getEtat())
                .append("  " + getSelected())
                .append("\n");
        for (Fichier f : contenu) {
            res.append(f.formatAffichage(decalage + 1));

        }
        return res.toString();
    }

//    @Override
//    public String getEtat(Etat e) {
//        
//    }
    @Override
    public LocalDateTime getLastModification() {

        if (contenu.isEmpty()) {
            return super.getLastModification();
        }
        return contenu.stream().map(d -> d.getLastModification())
                .max(((d1, d2) -> d1.compareTo(d2)))
                .get();

    }

    @Override
    public boolean estUnDossier() {
        return true;
    }

    @Override
    public void setEtat(Fichier dossier) {

        List<Etat> listEtat = contenu.stream().map(e -> e.getEtat()).collect(toList());

        boolean containsNewer = listEtat.stream().anyMatch(e -> e == Etat.NEWER);
        boolean containsSame = listEtat.stream().anyMatch(e -> e == Etat.SAME);
        boolean isNewer = containsNewer && containsSame;

        if (etat == etat.UNKNOW) {

            if (isNewer) {
                etat = etat.NEWER;
                dossier.etat = etat.OLDER;
            }
        }

    }
}
