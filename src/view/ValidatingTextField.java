package view;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import java.util.function.Predicate;
import javafx.scene.control.TextField;

public class ValidatingTextField extends TextField {
    private final Predicate<String> validation;
    private BooleanProperty IsValid= new SimpleBooleanProperty();

    public BooleanProperty isValid() {
        return IsValid;
    }

    public ValidatingTextField(Predicate<String> validation){
       this.validation = validation;
       textProperty().addListener((o, oldvalue, newText)->{
           IsValid.set(validation.test(newText));
       });
    }
}
