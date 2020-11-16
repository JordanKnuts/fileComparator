package viewmodel;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.TreeItem;
import model.CompareTree;
import model.Etat;
import model.Fichier;
import model.FileBuilder;
import model.Type;

public class ViewModel {

    private Fichier fichierLeft;
    private Fichier fichierRight;

    private final StringProperty pathLeft = new SimpleStringProperty("");
    private final StringProperty pathRight = new SimpleStringProperty("");

    private final ObjectProperty<TreeItem<Fichier>> dirLeft = new SimpleObjectProperty<>();
    private final ObjectProperty<TreeItem<Fichier>> dirRight = new SimpleObjectProperty<>();

    private final BooleanProperty all = new SimpleBooleanProperty(false);
    private final BooleanProperty newerLeft = new SimpleBooleanProperty(false);
    private final BooleanProperty newerRight = new SimpleBooleanProperty(false);
    private final BooleanProperty orphan = new SimpleBooleanProperty(false);
    private final BooleanProperty same = new SimpleBooleanProperty(false);
    private final BooleanProperty folders = new SimpleBooleanProperty(false);

    Set<Etat> etat_Set = new HashSet<>();
    Set<Type> type_Set = new HashSet<>();

    public ViewModel() {
        Path rootLeft = Paths.get("./TestBC/RootBC_Left");
        Path rootRight = Paths.get("./TestBC/RootBC_Right");

        pathLeft.set(rootLeft.toAbsolutePath().toString());
        pathRight.set(rootRight.toAbsolutePath().toString());

        fichierLeft = new FileBuilder().buildTree(pathLeft.get());
        fichierRight = new FileBuilder().buildTree(pathRight.get());

        makingTreeRoot();
        initListeners();
    }

    public void makeNewerR() {
        if (all.getValue() == true) {
            all.setValue(Boolean.FALSE);
        }
        updateEtat(Etat.NEWER);
        updateTreeRoot(fichierRight);
        dirRight.set(makeTreeRoot(fichierRight));
        updateEtat(Etat.NEWER);
        updateEtat(Etat.OLDER);
        updateTreeRoot(fichierLeft);
        dirLeft.set(makeTreeRoot(fichierLeft));
        updateEtat(Etat.OLDER);

    }

    public void makeNewerL() {
        if (all.getValue() == true) {
            all.set(false);
        }
        updateEtat(Etat.NEWER);
        updateTreeRoot(fichierLeft);
        dirLeft.set(makeTreeRoot(fichierLeft));
        updateEtat(Etat.NEWER);
        updateEtat(Etat.OLDER);
        updateTreeRoot(fichierRight);
        dirRight.set(makeTreeRoot(fichierRight));
        updateEtat(Etat.OLDER);
    }

    public void updateSetRefresh() {
        updateTreeRoot(fichierLeft);
        updateTreeRoot(fichierRight);
        makingTreeRoot();
    }

    public void removeOrphan() {
        if (all.getValue() == true) {
            all.set(false);
        }

        if (getEtat_Set().contains(Etat.ORPHAN)) {
            updateEtat(Etat.ORPHAN);
        }
    }

    public void addOrphan() {
        if (all.getValue() == true) {
            all.set(false);
        }
        if (!getEtat_Set().contains(Etat.ORPHAN)) {
            updateEtat(Etat.ORPHAN);
        }
    }

    public void removeSame() {
        if (all.getValue() == true) {
            all.set(false);
        }

        if (getEtat_Set().contains(Etat.SAME)) {
            updateEtat(Etat.SAME);
        }
    }

    public void addSame() {

        all.set(false);

        if (!getEtat_Set().contains(Etat.SAME)) {
            updateEtat(Etat.SAME);
        }
    }

    private void initListeners() {
        pathLeft.addListener((o, old, newV) -> {
            fichierLeft = new FileBuilder().buildTree(newV);
            fichierRight = new FileBuilder().buildTree(pathRight.get());
            makingTreeRoot();
        });

        pathRight.addListener((o, old, newV) -> {
            fichierRight = new FileBuilder().buildTree(newV);
            fichierLeft = new FileBuilder().buildTree(pathLeft.get());
            makingTreeRoot();
        });

        orphan.addListener((o, old, action) -> {
            if (action) {

                boolean toDo = true;

                if (getEtat_Set().contains(Etat.SAME)) {
                    updateEtat(Etat.SAME);
                }

                if (orphan.getValue() == true && newerRight.getValue() == true && toDo) {
                    addOrphan();
                    makeNewerR();
                    toDo = false;
                }
                if (newerLeft.getValue() == true && orphan.getValue() == true && toDo) {
                    addOrphan();
                    makeNewerL();
                    toDo = false;

                }
                if (orphan.getValue() == true && newerRight.getValue() == false && toDo) {
                    addOrphan();
                    updateSetRefresh();
                    toDo = false;
                }
                if (orphan.getValue() == true && newerLeft.getValue() == false && toDo) {
                    addOrphan();
                    updateSetRefresh();
                    toDo = false;
                }
                if (orphan.getValue() == false && newerRight.getValue() == true && toDo) {
                    removeOrphan();
                    makeNewerR();
                    toDo = false;

                }
                if (orphan.getValue() == false && newerLeft.getValue() == true && toDo) {
                    removeOrphan();
                    makeNewerL();
                    toDo = false;

                }
                if (orphan.getValue() == false && newerRight.getValue() == false && toDo) {
                    removeOrphan();
                    updateSetRefresh();
                    toDo = false;
                }
                if (orphan.getValue() == false && newerLeft.getValue() == false && toDo) {
                    removeOrphan();
                    updateSetRefresh();
                    toDo = false;
                }
            }
        });

        same.addListener((o, old, action) -> {
            if (action) {

                boolean toDo = true;

                if (getEtat_Set().contains(Etat.ORPHAN)) {
                    updateEtat(Etat.ORPHAN);
                }

                if (newerRight.getValue() == true && same.getValue() == true && toDo) {
                    addSame();
                    makeNewerR();
                    toDo = false;
                }
                if (newerLeft.getValue() == true && same.getValue() == true && toDo) {
                    addSame();
                    makeNewerL();
                    toDo = false;

                }
                if (same.getValue() == true && newerRight.getValue() == false && toDo) {
                    addSame();
                    updateSetRefresh();
                    toDo = false;
                }
                if (same.getValue() == true && newerLeft.getValue() == false && toDo) {
                    addSame();
                    updateSetRefresh();
                    toDo = false;
                }
                if (same.getValue() == false && newerRight.getValue() == true && toDo) {
                    removeSame();
                    makeNewerR();
                    toDo = false;

                }
                if (same.getValue() == false && newerLeft.getValue() == true && toDo) {
                    removeSame();
                    makeNewerL();
                    toDo = false;

                }
                if (same.getValue() == false && newerRight.getValue() == false && toDo) {
                    removeSame();
                    updateSetRefresh();
                    toDo = false;
                }

                if (same.getValue() == false && newerLeft.getValue() == false && toDo) {
                    removeSame();
                    updateSetRefresh();
                    toDo = false;
                }
            }
        });

        newerLeft.addListener((o, old, action) -> {
            if (action) {

                if (newerLeft.getValue() == true) {
                    makeNewerL();

                } else if (newerLeft.getValue() == false) {

                    updateSetRefresh();

                }
            }
        });

        newerRight.addListener((o, old, action) -> {
            if (action) {

                all.set(false);
                if (newerRight.getValue() == true) {
                    makeNewerL();

                } else if (newerRight.getValue() == false) {

                    updateSetRefresh();

                }
            }
        });

        folders.addListener((o, old, action) -> {
            if (action) {
                all.set(false);
                updateType(Type.DOSSIER);
                updateSetRefresh();

            } else {
                updateType(Type.DOSSIER);
                updateSetRefresh();
            }
        });

        all.addListener((o, old, action) -> {
            if (action) {
                folders.set(false);
                getType_Set().clear();

                orphan.set(false);
                same.set(false);
                newerRight.set(false);
                newerLeft.set(false);
                getEtat_Set().clear();

                updateSetRefresh();
            }

        });

    }

    public void setPathLeft(String s) {
        pathLeft.set(s);
    }

    public void setPathRight(String s) {
        pathRight.set(s);
    }

    public Set<Etat> getEtat_Set() {
        return etat_Set;
    }

    public Set<Type> getType_Set() {
        return type_Set;
    }

    public void updateEtat(Etat e) {
        if (etat_Set.contains(e)) {
            etat_Set.remove(e);
        } else {
            etat_Set.add(e);
        }
    }

    public void updateType(Type t) {
        if (type_Set.contains(t)) {
            type_Set.remove(t);
        } else {
            type_Set.add(t);
        }

    }

    //Property pour tout les élément binder
    public ObjectProperty dirLeftProperty() {
        return dirLeft;
    }

    public ObjectProperty dirRightProperty() {
        return dirRight;
    }

    public StringProperty pathLeftProperty() {
        return pathLeft;
    }

    public StringProperty pathRightProperty() {
        return pathRight;
    }

    public BooleanProperty isFolderSelectedProperty() {
        return folders;
    }

    public BooleanProperty allProperty() {
        return all;
    }

    public BooleanProperty sameProperty() {
        return same;
    }

    public BooleanProperty orphanProperty() {
        return orphan;
    }

    public BooleanProperty newerLeftProperty() {
        return newerLeft;
    }

    public BooleanProperty newerRightProperty() {
        return newerRight;
    }

    private void makingTreeRoot() {
        CompareTree.compareTree(fichierRight, fichierLeft);
        CompareTree.compareTree(fichierLeft, fichierRight);

        dirLeft.set(makeTreeRoot(fichierLeft));
        dirRight.set(makeTreeRoot(fichierRight));
    }

    private TreeItem<Fichier> makeTreeRoot(Fichier root) {

        TreeItem<Fichier> res = new TreeItem<>(root);
        res.setExpanded(true);

        if (root.estUnDossier()) {
            root.fichiers().forEach(elem -> {
                if (elem.getSelected()) {
                    TreeItem<Fichier> treeItem = makeTreeRoot(elem);
                    if (elem.getSelectedType()) {
                        res.getChildren().add(treeItem);
                    }
                }

                elem.setSelectedEtat(etat_Set);
                elem.setSelectedType(type_Set);
            });
        }

        return res;
    }

    public void updateTreeRoot(Fichier root) {

        if (root.estUnDossier()) {

            for (Fichier fichier : root.fichiers()) {

                if (fichier.estUnDossier()) {

                    updateTreeRoot(fichier);
                }

                fichier.setSelectedEtat(etat_Set);
                fichier.setSelectedType(type_Set);

            }
        }
    }

}
