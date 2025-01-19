package controller;


import dao.PermissionsDAO;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import view.HomeView;
import view.UpdatePermissionsView;

public class UpdatePermissionsController {

        private PermissionsDAO permissionsDAO;
        private final UpdatePermissionsView updatePermissionsView;

    public void setPermissionsDAO(PermissionsDAO permissionsDAO) {
        this.permissionsDAO = permissionsDAO;
    }

    private final Alert successAlert;
    private final Alert errorAlert;

    public Alert getErrorAlert() {
        return errorAlert;
    }

    public Alert getSuccessAlert() {
        return successAlert;
    }

    public UpdatePermissionsController(Stage stage, HomeView prevView){
            updatePermissionsView = new UpdatePermissionsView();
            permissionsDAO = new PermissionsDAO();

            successAlert = new Alert(Alert.AlertType.CONFIRMATION);
            successAlert.setContentText("Permissions updated successfully!");

            errorAlert= new Alert(Alert.AlertType.ERROR);
            errorAlert.setContentText("Failed to update permissions.");


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
                        successAlert.showAndWait();
                    } else{
                        System.out.println("Operation failed");
                        errorAlert.showAndWait();
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
