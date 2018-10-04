package sample;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Main extends Application {
    String[] days = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

    @Override
    public void start(Stage primaryStage) throws Exception{

        int studCount = 5, dayCount = 14, constraintViolation = 0, penalty = 0;
        int maxChecked = 3;
        List<Student> students = new ArrayList<>();

        StackPane rootPane = new StackPane();
        rootPane.setPrefWidth(1400);
        rootPane.setPrefHeight(650);
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        rootPane.getChildren().add(gridPane);
        gridPane.setHgap(40);
        gridPane.setVgap(30);

        for (int i = 1; i <= studCount; i++){
            gridPane.add(new Label(String.valueOf(i)), 0, i);
        }

        for (int i = 0, j = 0; i < dayCount; i++, j++){
            if (j >= days.length)
                j = 0;
            String day = days[j];
            gridPane.add(new Label(day), i+1, 0);
        }

        for (int i = 0; i < dayCount; i++){ // col
            final Set<ToggleButton> activeBoxes = new LinkedHashSet<>();

            Rectangle rect = new Rectangle(20, 20);
            rect.setStroke(Color.GREY);

            ChangeListener<Boolean> listener = (o, oldValue, newValue) -> {
                // get checkbox containing property
                ToggleButton cb = (ToggleButton) ((ReadOnlyProperty) o).getBean();

                if (newValue) {
                    activeBoxes.add(cb);
                    if (activeBoxes.size() > maxChecked) {
                        // get first checkbox to be activated
                        cb = activeBoxes.iterator().next();

                        // unselect; change listener will remove
                        cb.setSelected(false);
                    }
                } else {
                    activeBoxes.remove(cb);
                }
            };

            for (int j = 0; j < studCount; j++){ // row
                ToggleButton toggleButton = new ToggleButton();
                toggleButton.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                toggleButton.selectedProperty().addListener(listener);
                gridPane.add(toggleButton, i+1, j+1);
            }
        }


        primaryStage.setScene(new Scene(rootPane));
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}

class Student {
    private int id;
    private ArrayList<StudentPref> studentPrefs;

    public Student(int id) {
        this.id = id;
    }

    private void populateData() {

    }

    public ArrayList<StudentPref> getStudentPrefs() {
        return studentPrefs;
    }
}

class StudentPref {
    private int weekDay;
    private boolean isAllowed, isDisliked;

    public StudentPref(int studId) {
        // get studId from db
    }

    public int getWeekDay() {
        return weekDay;
    }

    public boolean isAllowed() {
        return isAllowed;
    }

    public boolean isDisliked() {
        return isDisliked;
    }
}
