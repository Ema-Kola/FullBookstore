package view;
import javafx.scene.control.Button;
import model.Role;
public class ButtonWithPermission extends Button{
    private final Role permissionOfButton;

    ButtonWithPermission(Role permissionOfButton,Role permissionUser, String text){
        super(text);
        setMinHeight(25);
        setMinWidth(150);
        setStyle("-fx-background-radius: 7em;");

        //setStyle("-fx-border-color: white");
        //setStyle("-fx-background-color: transparent; -fx-border-color: white ; -fx-font-weight: bold");
        //setStyle("-fx-font-weight: bold");
        this.permissionOfButton=permissionOfButton;
        if(!isPermissible(permissionUser)){
            this.setVisible(false);
            this.setManaged(false);
        }
    }
    Role getPermission(){
        return permissionOfButton;
    }

    private boolean isPermissible(Role pUser){
       if((pUser==Role.LIBRARIAN && (permissionOfButton==Role.ADMINISTRATOR || permissionOfButton==Role.MANAGER)) || (pUser==Role.MANAGER && permissionOfButton==Role.ADMINISTRATOR) ){
               return false;
           }

        return true;

    }
}
