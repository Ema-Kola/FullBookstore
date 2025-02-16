package controller;
import dao.UsersDAO;
import javafx.stage.Stage;
import model.User;
import view.LogInView;


public class UserController {

    private User currentUser;
    private UsersDAO usersDAO;
    private final LogInView logInView;
    private HomeViewController homeViewController;

    public UserController(Stage stage){
        this.logInView = new LogInView();
        if (this.usersDAO==null) this.usersDAO =  new UsersDAO();

        logInView.getLoginBtn().setOnAction(e -> {
            User user = loginUsername(logInView.getEmailF().getText());
            if (user != null) {
                if(loginPassword(logInView.getPasswF().getText(),user)!=null){
                    this.currentUser=user;
                    this.homeViewController = new HomeViewController(stage, currentUser,this);
//                    HomeViewController hc = new HomeViewController(stage, currentUser,this);
                    stage.setScene(homeViewController.getView().showView(stage));
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

    public HomeViewController getHomeViewController() {
        return homeViewController;
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

    public void setUsersDAO(UsersDAO usersDAO) {
        this.usersDAO = usersDAO;
    }
}



