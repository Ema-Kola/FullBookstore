package dao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;


public class UsersDAO extends DAO<User>{

    public UsersDAO(){
        super("files/users.dat");
    }

    public ObservableList<User> getAllActive() {
        ObservableList<User> items = FXCollections.observableArrayList();
        for(User u : getAll()){
            if(u.isActive()){
                items.add(u);
            }
        }

        return items;
    }

    public User searchUser(String username){
        for(User u : this.getAll()){
            if(username.equals(u.getUsername())){
                return u;
            }
        }
        return null;

    }




}
