package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RestockExistingBookView {



        private ValidatingTextField isbnTF = new ValidatingTextField(input -> input.matches("[0-9]{3}-[0-9]{2}-[0-9]{5}-[0-9]{2}-[0-9]"));
        private Label isbnLbl = new Label("ISBN of book: ");
        private final ValidatingTextField quantityTF = new ValidatingTextField(input -> input.matches("\\d+"));
        private final Label quantityLbl = new Label("Quantity: ");
        private final ValidatingTextField purchasedpriceTF = new ValidatingTextField(input -> input.matches("[0-9]+[.,]?[0-9]*"));
        private final Label purchasedpriceLbl = new Label("Purchased Price: ");

         private final Text isbnError = new Text();

        private Button restockButton = new Button("Restock");


        public Scene showView(Stage stage)
        {
            stage.setTitle("Restock Book");
            GridPane gr = new GridPane();
            gr.setHgap(10);
            gr.setVgap(10);
            gr.setPadding(new Insets(10,10,10,10));
            gr.setAlignment(Pos.CENTER);
            gr.add(isbnLbl, 0, 0);
            gr.add(isbnTF, 1, 0);
            isbnError.setFill(Color.RED);
            gr.add(isbnError, 2, 0);
            gr.add(purchasedpriceLbl, 0, 2);
            gr.add(purchasedpriceTF, 1, 2);
            gr.add(quantityLbl, 0, 3);
            gr.add(quantityTF, 1, 3);
            gr.add(restockButton, 1, 4);


            return new Scene(gr,400,400);
        }

    public ValidatingTextField getIsbnTF() {
        return isbnTF;
    }

    public ValidatingTextField getQuantityTF() {
        return quantityTF;
    }

    public ValidatingTextField getPurchasedpriceTF() {
        return purchasedpriceTF;
    }

    public Button getRestockButton() {
        return restockButton;
    }

    public Text getIsbnError() {
        return isbnError;
    }
}
