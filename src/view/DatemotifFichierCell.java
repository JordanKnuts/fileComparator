
package view;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import model.Fichier;

/**
 *
 * @author marce
 */
public class DatemotifFichierCell extends FichierCell{

    public DatemotifFichierCell() {
    }@Override
    String texte(Fichier elem) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy  HH:mm:ss");
        return ""+getLastModificationFileBuilder(elem.getPath()).format(formatter);
    }

    private LocalDateTime getLastModificationFileBuilder(Path path) {
        LocalDateTime d = null ;
        try {
            //Path path = Paths.get(path).toRealPath();
            d = lastModificationTime(path)/*.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))*/;
        } catch (IOException e) {
            d=null;
//d = "Le fichier (dossier) " + e.getMessage() + " n'existe pas";
        } return d;
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

    

    
}
