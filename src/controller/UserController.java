package controller;
import dao.UsersDAO;
import javafx.stage.Stage;
import model.User;
import view.LogInView;


public class UserController {

    private User currentUser;
    private UsersDAO usersDAO;
    private final LogInView logInView;

    public UserController(Stage stage){
        this.logInView = new LogInView();
        this.usersDAO =  new UsersDAO();

        logInView.getLoginBtn().setOnAction(e -> {
            User user = loginUsername(logInView.getEmailF().getText());
            if (user != null) {
                if(loginPassword(logInView.getPasswF().getText(),user)!=null){
                    this.currentUser=user;
                    HomeViewController hc = new HomeViewController(stage, currentUser,this);
                    stage.setScene(hc.getView().showView(stage));
                }else{
                    logInView.getWrongUsernameF().setText(" ");
                    logInView.getWrongPasswordF().setText("Wrong password.");
                }
            } else {
                logInView.getWrongUsernameF().setText("No user with this username");
                logInView.getWrongPasswordF().setText(" ");
            }

        });

    }

    public LogInView getView(){
        return this.logInView;
    }

        public User loginUsername(String username) {
            for (User user : usersDAO.getAllActive()) {
                if ((user.getUsername()).compareTo(username)==0) {
                    return user;
                }
            }
            return null;
        }

        public User loginPassword(String password, User user) {
                if ((user.getPassword()).compareTo(password)==0) {
                    return user;
                }
            return null;
        }

}



