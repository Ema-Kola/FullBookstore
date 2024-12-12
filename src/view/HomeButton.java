package view;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.FileInputStream;

public class HomeButton extends Button {
    public HomeButton(String text){
        super(text);

        try {
            FileInputStream input = new FileInputStream("files/images/home.png");
            ImageView homeImg = new ImageView(new Image(input));
            homeImg.setFitHeight(15);
            homeImg.setFitWidth(15);
            this.setGraphic(homeImg);
            this.setPrefSize(70,30);
            this.setStyle("-fx-background-color: white");

        }catch (java.io.FileNotFoundException e)  {
            System.out.println("Could not find image.");
        }

    }

}
