package controller;

import dao.CategoryDAO;
import javafx.scene.control.Alert;
import view.AddCategoryView;
import view.AddBookView;

public class AddCategoryController {


        private CategoryDAO categoryDAO;
        private final AddCategoryView addCategoryView;


        public AddCategoryController(AddBookView prevView){
            this.addCategoryView = new AddCategoryView();
            this.categoryDAO=new CategoryDAO();
            addCategoryView.getSubmitButton().disableProperty().bind(addCategoryView.getCategoryTf().textProperty().isEmpty());

            addCategoryView.getSubmitButton().setOnAction(e -> {
                String newCategory = addCategoryView.getCategory();
                if(categoryDAO.searchCategory(newCategory)==null) {

                    if (categoryDAO.create(newCategory)) {
                        prevView.setCategories();
                        this.addCategoryView.getCategoryTf().clear();
                        System.out.println("New category added!");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setContentText("New category added!");
                        a.showAndWait();
                    } else{
                        System.out.println("Operation failed");
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Failed to save author.");
                        a.showAndWait();
                    }

                }else{
                    System.out.println("This category already exists.");
                    Alert a = new Alert(Alert.AlertType.ERROR);
                    a.setContentText("This category already exists. ");
                    a.showAndWait();
                }

            });

        }

        public AddCategoryView getView(){
            return this.addCategoryView;
        }


    }







