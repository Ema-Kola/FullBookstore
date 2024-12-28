package controller;

import dao.AuthorsDAO;
import javafx.scene.control.Alert;
import model.*;
import view.AddAuthorView;
import view.AddBookView;


public class AddAuthorController {

        private AuthorsDAO authorsDAO;
        private final AddAuthorView addAuthorView;
        private final Alert alert1;
        private final Alert alert2;
        private final Alert alert3;




    public AddAuthorController(AddBookView prevView){
            this.addAuthorView = new AddAuthorView();
            addAuthorView.getSubmitButton().disableProperty().bind(addAuthorView.getAuthorNameTf().textProperty().isEmpty().or(addAuthorView.getAuthorSurnameTf().textProperty().isEmpty()).or(addAuthorView.getGendercomboBox().valueProperty().isNull()));

            alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert1.setContentText("Author saved successfully!");

            alert2 = new Alert(Alert.AlertType.ERROR);
            alert2.setContentText("Failed to save author.");

            alert3 = new Alert(Alert.AlertType.ERROR);
            alert3.setContentText("This author already exists. ");
            authorsDAO=new AuthorsDAO();
            addAuthorView.getSubmitButton().setOnAction(e -> {
                System.out.println("Submit button clicked!");
                String name = addAuthorView.getAuthorName();
                String lastname = addAuthorView.getAuthorLastName();
                Gender gender= addAuthorView.getGender();
                Author newAuthor = new Author(name, lastname, gender);

                if(authorsDAO.searchAuthor(name, lastname)==null) {

                    if (authorsDAO.create(newAuthor)) {
                        prevView.setAuthors();
                        this.addAuthorView.getAuthorNameTf().clear();
                        this.addAuthorView.getAuthorSurnameTf().clear();
                        System.out.println("Author saved successfully");
                        alert1.showAndWait();
                    } else{
                        alert2.showAndWait();
                    }

                }else{
                    alert3.showAndWait();
                    System.out.println("This author already exists.");
                }


            });

        }

        public AddAuthorView getView(){
            return this.addAuthorView;
        }


        public void setAuthorsDAO(AuthorsDAO authorsDAO) {
            this.authorsDAO = authorsDAO;
        }
        public Alert getAlert1() {
            return alert1;
        }

        public Alert getAlert2() {
            return alert2;
        }

        public Alert getAlert3() {
            return alert3;
        }


    }





