package main;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import static model.CompareTree.compareTree;
import model.Fichier;
import model.FileBuilder;
import sun.reflect.generics.tree.Tree;
import view.View;

/**
 *
 * @author MediaMonster
 */
public class Main {

    public static void main(String[] args) {

          Fichier dirLeft = new FileBuilder().buildTree("./TestBC/RootBC_Left");
          
          Fichier dirRight = new FileBuilder().buildTree("./TestBC/RootBC_Right");

          
         compareTree(dirRight, dirLeft);
         compareTree(dirLeft, dirRight);
          
         View.main(args);
         
          // System.out.println((dirLeft));
          // System.out.println(dirRight);

      
    }

}
