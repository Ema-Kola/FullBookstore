
package view;


import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class AddCategoryView {



    private ValidatingTextField categoryTf = new ValidatingTextField(input ->input.matches("[a-zA-Z]+"));
    private Label categoryLbl = new Label("New Category: ");
    private Button sbButton = new Button("Submit");

    private final Label categoryWarning = new Label("*enter category");

    public Scene showView(Stage stage)
    {
        stage.setTitle("AddCategory");
        categoryWarning.setTextFill(Color.GRAY);
        categoryWarning.visibleProperty().bind(categoryTf.isValid().not());
        GridPane gr = new GridPane();
        gr.setHgap(10);
        gr.setVgap(10);
        gr.setPadding(new Insets(10,10,10,10));
        gr.setAlignment(Pos.CENTER);
        gr.add(categoryLbl, 0, 0);
        gr.add(categoryTf, 1, 0);
        gr.add(sbButton,1,1);
        sbButton.setStyle("-fx-background-color: #73c273");


        return new Scene(gr,400,400);
    }

    public String getCategory()
    {
        return categoryTf.getText();
    }

    public TextField getCategoryTf() {
        return categoryTf;
    }

    public Button getSubmitButton()
    {
        return sbButton;
    }



}


