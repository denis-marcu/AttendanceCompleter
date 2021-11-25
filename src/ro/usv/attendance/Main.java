package ro.usv.attendance;

import com.sun.istack.internal.NotNull;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    private Button btnAddAttendanceTable = new Button("Add attendance table");
    private Button btnAddAttendance = new Button("Add attendance");
    private final FileChooser fileChooser = new FileChooser();
    private ListView<String> csvList = new ListView<>();
    private TableView<Student> tableView = new TableView<>();
    private List<String> sbvBuffer = new ArrayList<>();
    private List<List<String>> csvBuffer = new ArrayList<>();
    private static String completeCsvPath, completeSbvPath;
    private List<String> paths = new ArrayList<>();


    // The observable list for table
    private List<Student> setStudList(List<List<String>> buffer) {
        List<Student> studentEntities = new ArrayList<>();
        for (int i = 0; i < buffer.size(); i++) {
            Student s = new Student();
            s.setNrCrt((buffer.get(i).get(0)));
            s.setFirstName(buffer.get(i).get(1));
            s.setFathersInitial(buffer.get(i).get(2));
            s.setLastName(buffer.get(i).get(3));
            s.setGroup(buffer.get(i).get(4));
            s.setC1(buffer.get(i).get(5));
            s.setC2(buffer.get(i).get(6));
            s.setC3(buffer.get(i).get(7));
            s.setC4(buffer.get(i).get(8));
            s.setC5(buffer.get(i).get(9));
            s.setC6(buffer.get(i).get(10));
            s.setC7(buffer.get(i).get(11));
            s.setC8(buffer.get(i).get(12));
            s.setC9(buffer.get(i).get(13));
            s.setC10(buffer.get(i).get(14));
            s.setC11(buffer.get(i).get(15));
            s.setC12(buffer.get(i).get(16));
            s.setC13(buffer.get(i).get(17));
            s.setC14(buffer.get(i).get(18));
            s.setL1(buffer.get(i).get(19));
            s.setL2(buffer.get(i).get(20));
            s.setL3(buffer.get(i).get(21));
            s.setL4(buffer.get(i).get(22));
            s.setL5(buffer.get(i).get(23));
            s.setL6(buffer.get(i).get(24));
            s.setL7(buffer.get(i).get(25));
            s.setL8(buffer.get(i).get(26));
            s.setL9(buffer.get(i).get(27));
            s.setL10(buffer.get(i).get(28));
            s.setL11(buffer.get(i).get(29));
            s.setL12(buffer.get(i).get(30));
            s.setL13(buffer.get(i).get(31));
            s.setL14(buffer.get(i).get(32));
            studentEntities.add(i, s);
        }
        return studentEntities;
    }

    //Selecting the first group of strings for distance [First_name Last_name Group].
    private List<String> getNamesAndGroupsFromCsv(List<List<String>> buffer) {
        List<String> namesAndGroup = new ArrayList<>();

        for (int i = 0; i < buffer.size(); i++) {
            namesAndGroup.add(i, (buffer.get(i).get(1).charAt(0) + buffer.get(i).get(1).toLowerCase().substring(1) + " " +
                                  buffer.get(i).get(3).charAt(0) + buffer.get(i).get(3).toLowerCase().substring(1) + " " +
                                  buffer.get(i).get(4)));
        }
        return namesAndGroup;
    }


    private List<Student> setAttendance(List<Student> students) {
        int d;
        for (int i = 0; i < students.size(); i++) {
            for (int j = 0; j < sbvBuffer.size(); j++) {
                d = EditDistance.calculate(getNamesAndGroupsFromCsv(csvBuffer).get(i), sbvBuffer.get(j));
                //System.out.println(getNamesAndGroupsFromCsv(csvBuffer).get(i));
                //System.out.println(sbvBuffer.get(j));
                //System.out.println(d);
                if (d < 22) {
                    /*System.out.println(getNamesAndGroupsFromCsv(csvBuffer).get(i));
                    System.out.println(sbvBuffer.get(j));
                    System.out.println(d);*/
                    students.get(i).setC1("1");
                } else {
                    students.get(i).setC1("0");
                }


            }
        }
        return students;
    }

    // The listener for the list
    private void onSelect() {
        csvList.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (String path : paths) {
                if (path.contains(newValue)) {
                    csvBuffer = Readers.readFromCsv(path);
                }
            }
            ObservableList<Student> data = FXCollections.observableArrayList(
                    setAttendance(setStudList(csvBuffer))
            );

            tableView.setEditable(true);
            tableView.setItems(data);
            tableView.refresh();
            csvBuffer.clear();

        });
    }

    @Override
    public void start(Stage primaryStage) {

        primaryStage.setTitle("Attendance Completer");
        ObservableList<String> csvItems = FXCollections.observableArrayList();
        btnAddAttendanceTable.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        csvItems.add(file.getName());
                        csvList.setItems(csvItems);
                        completeCsvPath = file.getAbsolutePath();
                        paths.add(completeCsvPath);
                    }
                });
        btnAddAttendance.setOnAction(
                e -> {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    if (file != null) {
                        completeSbvPath = file.getAbsolutePath();
                        sbvBuffer = Readers.readFromSbv(completeSbvPath);

                    }

                });

        onSelect();
        generateTable(tableView);
        VBox vbox = new VBox(csvList, getBtnAddAttendance(), getBtnAddAttendanceTable());
        HBox hbox = new HBox(vbox, tableView);
        Group root = new Group(setImage(), hbox);
        vbox.setSpacing(10);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(20, 0, 20, 20));
        primaryStage.setScene(new Scene(root, 1520, 620));
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

    // Setting up the effects for buttons
    @NotNull
    private Button getButton(Button btnAddAttendance) {
        DropShadow shadow = new DropShadow();
        btnAddAttendance.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> btnAddAttendance.setEffect(shadow));
        btnAddAttendance.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> btnAddAttendance.setEffect(null));
        btnAddAttendance.setStyle("-fx-font: 22 arial; -fx-base: #c0c0c0;");
        btnAddAttendance.setMaxWidth(Double.MAX_VALUE);

        return btnAddAttendance;
    }

    // Setting up the background image
    private ImageView setImage() {
        try {
            Image image = new Image(new FileInputStream("background.png"));
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            return imageView;
        } catch (Exception e) {
            System.out.println("Image not found!");
            return null;
        }
    }

    private Button getBtnAddAttendanceTable() {
        return getButton(btnAddAttendanceTable);
    }

    private Button getBtnAddAttendance() {
        return getButton(btnAddAttendance);
    }

    private void columns(TableColumn<Student, String> c1, TableColumn<Student, String> c2, TableColumn<Student, String> c3, TableColumn<Student, String> c4, TableColumn<Student, String> c5, TableColumn<Student, String> c6, TableColumn<Student, String> c7, TableColumn<Student, String> c8, TableColumn<Student, String> c9, TableColumn<Student, String> c10, TableColumn<Student, String> c11, TableColumn<Student, String> c12, TableColumn<Student, String> c13, TableColumn<Student, String> c14) {
        c1.setMaxWidth(30);
        c2.setMaxWidth(30);
        c3.setMaxWidth(30);
        c4.setMaxWidth(30);
        c5.setMaxWidth(30);
        c6.setMaxWidth(30);
        c7.setMaxWidth(30);
        c8.setMaxWidth(30);
        c9.setMaxWidth(30);
        c10.setMaxWidth(30);
        c11.setMaxWidth(30);
        c12.setMaxWidth(30);
        c13.setMaxWidth(30);
        c14.setMaxWidth(30);
    }

    private void generateTable(TableView<Student> tableView) {
        tableView.getColumns().clear();
        TableColumn<Student, String> nrCrtCol = new TableColumn<>("Nr.Crt.");
        TableColumn<Student, String> firstNameCol = new TableColumn<>("First Name");
        TableColumn<Student, String> fathersInitialCol = new TableColumn<>("I.T.");
        TableColumn<Student, String> lastNameCol = new TableColumn<>("Last Name");
        TableColumn<Student, String> groupCol = new TableColumn<>("Group");
        TableColumn<Student, String> l1 = new TableColumn<>("L1");
        TableColumn<Student, String> l2 = new TableColumn<>("L2");
        TableColumn<Student, String> l3 = new TableColumn<>("L3");
        TableColumn<Student, String> l4 = new TableColumn<>("L4");
        TableColumn<Student, String> l5 = new TableColumn<>("L5");
        TableColumn<Student, String> l6 = new TableColumn<>("L6");
        TableColumn<Student, String> l7 = new TableColumn<>("L7");
        TableColumn<Student, String> l8 = new TableColumn<>("L8");
        TableColumn<Student, String> l9 = new TableColumn<>("L9");
        TableColumn<Student, String> l10 = new TableColumn<>("L10");
        TableColumn<Student, String> l11 = new TableColumn<>("L11");
        TableColumn<Student, String> l12 = new TableColumn<>("L12");
        TableColumn<Student, String> l13 = new TableColumn<>("L13");
        TableColumn<Student, String> l14 = new TableColumn<>("L14");
        TableColumn<Student, String> c1 = new TableColumn<>("C1");
        TableColumn<Student, String> c2 = new TableColumn<>("C2");
        TableColumn<Student, String> c3 = new TableColumn<>("C3");
        TableColumn<Student, String> c4 = new TableColumn<>("C4");
        TableColumn<Student, String> c5 = new TableColumn<>("C5");
        TableColumn<Student, String> c6 = new TableColumn<>("C6");
        TableColumn<Student, String> c7 = new TableColumn<>("C7");
        TableColumn<Student, String> c8 = new TableColumn<>("C8");
        TableColumn<Student, String> c9 = new TableColumn<>("C9");
        TableColumn<Student, String> c10 = new TableColumn<>("C10");
        TableColumn<Student, String> c11 = new TableColumn<>("C11");
        TableColumn<Student, String> c12 = new TableColumn<>("C12");
        TableColumn<Student, String> c13 = new TableColumn<>("C13");
        TableColumn<Student, String> c14 = new TableColumn<>("C14");
        columns(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14);
        columns(l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14);

        nrCrtCol.setCellValueFactory(new PropertyValueFactory<>("nrCrt"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        fathersInitialCol.setCellValueFactory(new PropertyValueFactory<>("fathersInitial"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        groupCol.setCellValueFactory(new PropertyValueFactory<>("group"));
        c1.setCellValueFactory(new PropertyValueFactory<>("c1"));
        c2.setCellValueFactory(new PropertyValueFactory<>("c2"));
        c3.setCellValueFactory(new PropertyValueFactory<>("c3"));
        c4.setCellValueFactory(new PropertyValueFactory<>("c4"));
        c5.setCellValueFactory(new PropertyValueFactory<>("c5"));
        c6.setCellValueFactory(new PropertyValueFactory<>("c6"));
        c7.setCellValueFactory(new PropertyValueFactory<>("c7"));
        c8.setCellValueFactory(new PropertyValueFactory<>("c8"));
        c9.setCellValueFactory(new PropertyValueFactory<>("c9"));
        c10.setCellValueFactory(new PropertyValueFactory<>("c10"));
        c11.setCellValueFactory(new PropertyValueFactory<>("c11"));
        c12.setCellValueFactory(new PropertyValueFactory<>("c12"));
        c13.setCellValueFactory(new PropertyValueFactory<>("c13"));
        c14.setCellValueFactory(new PropertyValueFactory<>("c14"));
        l1.setCellValueFactory(new PropertyValueFactory<>("l1"));
        l2.setCellValueFactory(new PropertyValueFactory<>("l2"));
        l3.setCellValueFactory(new PropertyValueFactory<>("l3"));
        l4.setCellValueFactory(new PropertyValueFactory<>("l4"));
        l5.setCellValueFactory(new PropertyValueFactory<>("l5"));
        l6.setCellValueFactory(new PropertyValueFactory<>("l6"));
        l7.setCellValueFactory(new PropertyValueFactory<>("l7"));
        l8.setCellValueFactory(new PropertyValueFactory<>("l8"));
        l9.setCellValueFactory(new PropertyValueFactory<>("l9"));
        l10.setCellValueFactory(new PropertyValueFactory<>("l10"));
        l11.setCellValueFactory(new PropertyValueFactory<>("l11"));
        l12.setCellValueFactory(new PropertyValueFactory<>("l12"));
        l13.setCellValueFactory(new PropertyValueFactory<>("l13"));
        l14.setCellValueFactory(new PropertyValueFactory<>("l14"));
        tableView.getColumns().addAll(nrCrtCol, firstNameCol, fathersInitialCol, lastNameCol, groupCol, c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, l1, l2, l3, l4, l5, l6, l7, l8, l9, l10, l11, l12, l13, l14);
    }

}
