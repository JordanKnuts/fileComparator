package view;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Separator;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import model.CompareTree;
import model.Etat;
import model.Fichier;
import model.FileBuilder;
import model.Type;
import viewmodel.ViewModel;


public class View extends Application {

    private ViewModel viewModel;

    private final Label labelL = new Label();
    private final Label labelR = new Label();
    
    InputStream input = getClass().getResourceAsStream("file.png");
    private final Image fileIcon = new Image(input, 25, 25, true, true);
    private final Button fichierL = new Button("",new ImageView(fileIcon));
    private final Button fichierR = new Button("",new ImageView(fileIcon));
    

    private final TreeTableView treeTableViewLeft = new TreeTableView();
    private final TreeTableView treeTableViewRight = new TreeTableView();

    private final ToggleButton all = new ToggleButton("All");
    private final ToggleButton newerL = new ToggleButton("Newer Left");
    private final ToggleButton newerR = new ToggleButton("Newer Right");
    private final ToggleButton orphan = new ToggleButton("Orphan");
    private final ToggleButton same = new ToggleButton("Same");
    private final ToggleButton folder = new ToggleButton("Folders Only");

    ToggleGroup allGroup = new ToggleGroup();
    ToggleGroup newerGroup = new ToggleGroup();
    ToggleGroup orphanSameGroup = new ToggleGroup();
    ToggleGroup folderOnly = new ToggleGroup();


    @Override
    public void start(Stage primaryStage) {
        viewModel = new ViewModel();

        labelL.setPadding(new Insets(10));
        labelR.setPadding(new Insets(10));

        Separator separatorOne = new Separator(Orientation.VERTICAL);
        Separator separatorTwo = new Separator(Orientation.VERTICAL);
        Separator separatorThree = new Separator(Orientation.VERTICAL);

        all.setToggleGroup(allGroup);
        newerL.setToggleGroup(newerGroup);
        newerR.setToggleGroup(newerGroup);
        orphan.setToggleGroup(orphanSameGroup);
        same.setToggleGroup(orphanSameGroup);
        folder.setToggleGroup(folderOnly);
        
        
        
        
       

        HBox treeItem = new HBox();
        treeItem.setSpacing(5);
        treeItem.getChildren().addAll(all, separatorOne, newerL, newerR, separatorTwo, orphan, same, separatorThree, folder);
        treeItem.setAlignment(Pos.CENTER);

        List<Etat> etats = Arrays.asList(Etat.values());
        HBox bottom = new HBox();

        bottom.setSpacing(10);
        bottom.setAlignment(Pos.CENTER);
        for (Etat etat : etats) {
            Label tf = new Label(etat.toString());
            tf.setPadding(new Insets(10));
            tf.getStylesheets().add("view/css.css");
            tf.getStyleClass().add(etat.toString());
            bottom.getChildren().add(tf);

        }

        treeTableViewLeft.getColumns().setAll(getTreeTableColumn());
        treeTableViewRight.getColumns().setAll(getTreeTableColumn());

        HBox top = new HBox();
        Separator separator1 = new Separator(Orientation.VERTICAL);

        HBox pathAndSearchL = new HBox();
        pathAndSearchL.getChildren().addAll(labelL, fichierL);

        VBox topLeft = new VBox();
        topLeft.getChildren().addAll(pathAndSearchL, treeTableViewLeft);

        HBox pathAndSearchR = new HBox();
        pathAndSearchR.getChildren().addAll(labelR, fichierR);

        VBox topRight = new VBox();
        topRight.getChildren().addAll(pathAndSearchR, treeTableViewRight);

        top.setPadding(new Insets(10));
        top.setSpacing(10);
        top.getChildren().addAll(topLeft, separator1, topRight);

        VBox root = new VBox();
        root.getChildren().addAll(treeItem, top, bottom);
        Scene scene = new Scene(root, 1000, 600);

        initSearchFile(primaryStage);

        primaryStage.setTitle("TreeTableView");
        primaryStage.setScene(scene);
        configViewModelBindings();
        primaryStage.show();

    }

    public void initSearchFile(Stage primaryStage) {
        DirectoryChooser dir_chooser = new DirectoryChooser();
        fichierL.setOnAction(e -> {
            File file = dir_chooser.showDialog(primaryStage);
            if (file != null) {
                viewModel.setPathLeft(file.getAbsolutePath());
                
            }
        });

        fichierR.setOnAction(e -> {
            File file = dir_chooser.showDialog(primaryStage);
            if (file != null) {
                viewModel.setPathRight(file.getAbsolutePath());
                
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    //Binding des boutons, des path et du treeTable pour l'actualiser dans le VM
    
    private void configViewModelBindings() {

        labelL.textProperty().bind(viewModel.pathLeftProperty());
        labelR.textProperty().bind(viewModel.pathRightProperty());

        treeTableViewLeft.rootProperty().bind(viewModel.dirLeftProperty());
        treeTableViewRight.rootProperty().bind(viewModel.dirRightProperty());

        all.selectedProperty().bindBidirectional(viewModel.allProperty());
        folder.selectedProperty().bindBidirectional(viewModel.isFolderSelectedProperty());
        same.selectedProperty().bindBidirectional(viewModel.sameProperty());
        orphan.selectedProperty().bindBidirectional(viewModel.orphanProperty());
        newerL.selectedProperty().bindBidirectional(viewModel.newerLeftProperty());
        newerR.selectedProperty().bindBidirectional(viewModel.newerRightProperty());

    }



    private static List<TreeTableColumn<Fichier, Fichier>> getTreeTableColumn() {
        TreeTableColumn<Fichier, Fichier> nameCol = new TreeTableColumn<>("Nom"),
                typeCol = new TreeTableColumn<>("Type"),
                datemotif = new TreeTableColumn<>("Date Motif"),
                sizeCol = new TreeTableColumn<>("Taille");

        nameCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        typeCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        datemotif.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));
        sizeCol.setCellValueFactory(r -> new SimpleObjectProperty<>(r.getValue().getValue()));

        nameCol.setPrefWidth(250);

        nameCol.setCellFactory((param) -> {
            return new NomFichierCell();
        });

        typeCol.setPrefWidth(250);
        typeCol.setCellFactory((param) -> {
            return new TypeFichierCell();
        });
        datemotif.setPrefWidth(250);
        datemotif.setCellFactory((param) -> {
            return new DatemotifFichierCell();
        });
        sizeCol.setPrefWidth(250);
        sizeCol.setCellFactory((param) -> {
            return new TailleFichierCell();
        });

        return Arrays.asList(nameCol, typeCol, datemotif, sizeCol);
    }

}
