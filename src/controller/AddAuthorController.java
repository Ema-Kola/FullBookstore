package controller;

import dao.AuthorsDAO;
import javafx.scene.control.Alert;
import model.*;
import view.AddAuthorView;
import view.AddBookView;


public class AddAuthorController {

        private AuthorsDAO authorsDAO;
        private final AddAuthorView addAuthorView;


        public AddAuthorController(AddBookView prevView){
            this.addAuthorView = new AddAuthorView();
            addAuthorView.getSubmitButton().disableProperty().bind(addAuthorView.getAuthorNameTf().textProperty().isEmpty().or(addAuthorView.getAuthorSurnameTf().textProperty().isEmpty()).or(addAuthorView.getGendercomboBox().valueProperty().isNull()));

            addAuthorView.getSubmitButton().setOnAction(e -> {
                String name = addAuthorView.getAuthorName();
                String lastname = addAuthorView.getAuthorLastName();
                Gender gender= addAuthorView.getGender();
                Author newAuthor = new Author(name, lastname, gender);
                authorsDAO=new AuthorsDAO();
                if(authorsDAO.searchAuthor(name, lastname)==null) {

                    if (authorsDAO.create(newAuthor)) {
                        prevView.setAuthors();
                        this.addAuthorView.getAuthorNameTf().clear();
                        this.addAuthorView.getAuthorSurnameTf().clear();
                        System.out.println("Author saved successfully");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setContentText("Author saved succesfully!");
                        a.showAndWait();
                    } else{
                        System.out.println("Operation failed");
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Failed to save author.");
                        a.showAndWait();
                    }

                }else{
                    System.out.println("This author already exists.");
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("This author already exists. ");
                    a.showAndWait();
                }


            });

        }

        public AddAuthorView getView(){
            return this.addAuthorView;
        }


    }





