package controller;


import dao.PermissionsDAO;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.HomeView;
import view.UpdatePermissionsView;

public class UpdatePermissionsController {

        private PermissionsDAO permissionsDAO;
        private final UpdatePermissionsView updatePermissionsView;


        public UpdatePermissionsController(Stage stage, HomeView prevView){
            updatePermissionsView = new UpdatePermissionsView();
            permissionsDAO = new PermissionsDAO();


            updatePermissionsView.getUpdateBtn().setOnAction(e -> {
               permissionsDAO.setAddBookPermission(updatePermissionsView.getAddBook().getValue());
               permissionsDAO.setBookStatsPermission(updatePermissionsView.getBookStat().getValue());
               permissionsDAO.setManageEmployeesPermission(updatePermissionsView.getManageEmployees().getValue());
               permissionsDAO.setLibrarianStatsPermission(updatePermissionsView.getLibrarianStat().getValue());
               permissionsDAO.setFinanceStatsPermission(updatePermissionsView.getFinanceStat().getValue());
               permissionsDAO.setCheckOutBookPermission(updatePermissionsView.getCheckOutBook().getValue());
               permissionsDAO.setManageLibraryPermission(updatePermissionsView.getManageLibrary().getValue());



                    if (permissionsDAO.updateAll()) {
                        System.out.println("Permissions updated successfully!");
                        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                        a.setContentText("Permissions updated successfully!");
                        a.showAndWait();
                    } else{
                        System.out.println("Operation failed");
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setContentText("Failed to update permissions.");
                        a.showAndWait();
                    }

            });

            updatePermissionsView.getHomeBtn().setOnAction(e -> {
                stage.setScene(prevView.showView(stage));
            });

        }

        public UpdatePermissionsView getView(){
            return this.updatePermissionsView;
        }









}
