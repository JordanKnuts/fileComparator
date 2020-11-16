/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author MediaMonster
 */
public class FileBuilder {

//    private String path;
//    private Fichier fichierA;
//    private Fichier fichierB;
//    public FileBuilder(String pathA) {
//        this.fichierA = buildTree(pathA);
//        this.fichierB = buildTree(pathB);
//        System.out.println(fichierA);
//        System.out.println(fichierB);
//    }
    public Fichier buildTree(String path) {//PATH EN PARAM
        Dossier d = null;

        File fileParent = new File(path);

        if (fileParent.isDirectory()) {
            
           // Path relD = fileParent.toPath().relativize(Paths.get(path));

            d = new Dossier(fileParent.toPath(), getLastModificationFileBuilder(fileParent.toPath()));
            
            for (File fileOfDossier : fileParent.listFiles()) { 


                if (fileOfDossier.isFile()) {

                    Fichier f = new FichierSimple(fileOfDossier.toPath(), fileOfDossier.length(), getLastModificationFileBuilder(fileOfDossier.toPath()));
                    d.addFichier(f);
                   

                } else {
                    Fichier f = buildTree(fileOfDossier.getPath());
                    d.addFichier(f);
                }
            }

        }

        return d;

    }

    public LocalDateTime lastModificationTime(Path path) throws IOException {
        BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
        LocalDateTime result = attrs.lastModifiedTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        if (Files.isDirectory(path)) {
            try (DirectoryStream<Path> dir = Files.newDirectoryStream(path)) {
                for (Path p : dir) {
                    LocalDateTime tmp = lastModificationTime(p);
                    if (tmp.isAfter(result)) {
                        result = tmp;
                    }
                }
            }
        }
        return result;
    }

    public LocalDateTime getLastModificationFileBuilder(Path path) {

        LocalDateTime d = null;
        try {
            //Path path = Paths.get(path).toRealPath();
            d = lastModificationTime(path)/*.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))*/;
        } catch (IOException e) {
            d = null;
          //d = "Le fichier (dossier) " + e.getMessage() + " n'existe pas";
        }
        return d;
    }

}
