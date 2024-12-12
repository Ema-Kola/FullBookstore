
package view;


import dao.CategoryDAO;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Author;
import dao.AuthorsDAO;



public class AddBookView extends GridPane  {

    private HomeView prevView;
    private final ValidatingTextField titleTF = new ValidatingTextField(input -> !input.isEmpty());
    private final Label titleLbl = new Label("Title");
    private ValidatingTextField isbnTF = new ValidatingTextField(input -> input.matches("[0-9]{3}-[0-9]{2}-[0-9]{5}-[0-9]{2}-[0-9]"));
    private final Label isbnLbl = new Label("ISBN");
    private final ValidatingTextField supplierTF = new ValidatingTextField(input -> !input.isEmpty());
    private final Label supplierLbl = new Label("Supplier");
    private final ValidatingTextField sellingpriceTF = new ValidatingTextField(input -> input.matches("[0-9]+[.,]?[0-9]*"));
    private final Label sellingpriceLbl = new Label("Selling Price");
    private final ValidatingTextField purchasedpriceTF = new ValidatingTextField(input -> input.matches("[0-9]+[.,]?[0-9]*"));
    private final Label purchasedpriceLbl = new Label("Purchased Price");
    private final ValidatingTextField stockTF = new ValidatingTextField(input -> input.matches("\\d+"));
    private final Label stockLbl = new Label("Stock");
    private final Label descriptionLbl = new Label("Description");
    private final TextArea descriptionTA = new TextArea();
    private final Label authorsLbl = new Label("Select an author: ");
    private final ComboBox<Author> authorComboBox = new ComboBox<>();
    private final Label categoryLbl = new Label("Select a category: ");
    private final ComboBox<String> categoryComboBox = new ComboBox<>();
    private Button submitBtn = new Button("Submit");
    private HomeButton backToHomeBtn = new HomeButton("Home");
    private Button addAuthorButton = new Button(" + Add");
    private Button addCategoryButton = new Button("+ Add");
    private Button restockButton = new Button("Restock existing book. ");
    private Text wrongTitle = new Text("*cannot be empty.");
    private Text wrongIsbn = new Text("*follow format.");
    private Text wrongSupplier = new Text("*cannot be empty.");
    private Text wrongSellingPrice = new Text("*cannot be negative.");
    private Text wrongPurchasedPrice = new Text("*cannot be negative.");
    private Text wrongStock = new Text("*cannot be negative.");
    private Text wrongDescription = new Text("*cannot be empty.");
    private Text wrongAuthor = new Text("*cannot be empty.");
    private Text wrongCategory = new Text("*cannot be empty.");


    public void setAuthors(){
        ObservableList<Author> authors = (new AuthorsDAO()).getAll();
        authorComboBox.setItems(authors);
    }
    public void setCategories(){
        ObservableList<String> categories = (new CategoryDAO()).getAll();
        categoryComboBox.setItems(categories);
    }
    public AddBookView(HomeView prevView){
        this.prevView = prevView;
        setAuthors();
        setCategories();
        submitBtn.disableProperty().bind((isbnTF.isValid().not()).or(titleTF.isValid().not()).or(supplierTF.isValid().not()).or(sellingpriceTF.isValid().not()).or(stockTF.isValid().not()).or(purchasedpriceTF.isValid().not()).or(authorComboBox.valueProperty().isNull()).or(categoryComboBox.valueProperty().isNull()).or(descriptionTA.textProperty().isEmpty()));
        wrongTitle.visibleProperty().bind(titleTF.isValid().not());
        wrongIsbn.visibleProperty().bind(isbnTF.isValid().not());
        wrongAuthor.visibleProperty().bind(authorComboBox.valueProperty().isNull());
        wrongSupplier.visibleProperty().bind(supplierTF.isValid().not());
        wrongCategory.visibleProperty().bind(categoryComboBox.valueProperty().isNull());
        wrongDescription.visibleProperty().bind(descriptionTA.textProperty().isEmpty());
        wrongPurchasedPrice.visibleProperty().bind(purchasedpriceTF.isValid().not());
        wrongSellingPrice.visibleProperty().bind(sellingpriceTF.isValid().not());
        wrongStock.visibleProperty().bind(stockTF.isValid().not());
    }

    public Scene showView(Stage s) {
        s.setTitle("AddBook");
        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(0, 10, 0, 10));
        this.setVgap(10);
        this.setHgap(10);

        descriptionTA.setPrefColumnCount(20);
        descriptionTA.setPrefRowCount(5);
        descriptionTA.setWrapText(true);

        isbnTF.setPromptText("13 digit format XXX-XX-XXXXX-XX-X");

        wrongSupplier.setFill(Color.GRAY);
        wrongSupplier.setFont(Font.font("Courier",  10));
        wrongCategory.setFill(Color.GRAY);
        wrongCategory.setFont(Font.font("Courier",  10));
        wrongStock.setFill(Color.GRAY);
        wrongStock.setFont(Font.font("Courier",  10));
        wrongDescription.setFill(Color.GRAY);
        wrongDescription.setFont(Font.font("Courier",  10));
        wrongSellingPrice.setFill(Color.GRAY);
        wrongSellingPrice.setFont(Font.font("Courier",  10));
        wrongPurchasedPrice.setFill(Color.GRAY);
        wrongPurchasedPrice.setFont(Font.font("Courier",  10));
        wrongTitle.setFill(Color.GRAY);
        wrongTitle.setFont(Font.font("Courier",  10));
        wrongAuthor.setFill(Color.GRAY);
        wrongAuthor.setFont(Font.font("Courier",  10));
        wrongIsbn.setFill(Color.GRAY);
        wrongIsbn.setFont(Font.font("Courier",  10));


        this.add(backToHomeBtn, 0, 0);
        this.add(titleLbl, 0, 1);
        this.add(restockButton, 1,0);
        this.add(titleTF, 1, 1);
        this.add(wrongTitle,2,1);
        this.add(isbnLbl, 0, 2);
        this.add(isbnTF, 1, 2);
        this.add(wrongIsbn,2,2);
        this.add(supplierLbl, 0, 3);
        this.add(supplierTF, 1, 3);
        this.add(wrongSupplier, 2, 3);
        this.add(categoryLbl, 0, 4);
        this.add(categoryComboBox, 1, 4);
        this.add(wrongCategory, 3, 4);
        this.add(addCategoryButton,2,4);
        this.add(purchasedpriceLbl, 0, 5);
        this.add(purchasedpriceTF, 1, 5);
        this.add(wrongPurchasedPrice, 2, 5);
        this.add(sellingpriceLbl, 0, 6);
        this.add(sellingpriceTF, 1, 6);
        this.add(wrongSellingPrice, 2, 6);
        this.add(stockLbl, 0, 7);
        this.add(stockTF, 1, 7);
        this.add(wrongStock, 2, 7);
        this.add(descriptionLbl, 0, 8);
        this.add(descriptionTA, 1, 8);
        this.add(wrongDescription, 2, 8);
        this.add(authorsLbl, 0, 9);
        this.add(authorComboBox, 1, 9);
        this.add(wrongAuthor, 3, 9);
        this.add(addAuthorButton,2, 9);
        this.add(submitBtn, 1, 10);
        submitBtn.setStyle("-fx-background-color: #73c273");
        addAuthorButton.setStyle("-fx-background-color: #73c273");
        addCategoryButton.setStyle("-fx-background-color: #73c273");
        //this.setStyle("-fx-background-color: #e0e3f4;");
        Scene sc = new Scene(this, 800, 600);
        return sc;
    }

    public TextField getTitleTF() {
        return titleTF;
    }

    public ValidatingTextField getIsbnTF() {
        return isbnTF;
    }

    public TextArea getDescriptionTA() {
        return descriptionTA;
    }

    public ComboBox<Author> getAuthorComboBox() {
        return authorComboBox;
    }

    public Author getAuthor(){
        return authorComboBox.getValue();
    }


    public TextField getSupplierTF() {
        return supplierTF;
    }

    public ComboBox<String> getCategoryComboBox() {
        return categoryComboBox;
    }

    public ValidatingTextField getPurchasedpriceTF() {
        return purchasedpriceTF;
    }

    public ValidatingTextField getStockTF() {
        return stockTF;
    }

    public ValidatingTextField getSellingpriceTF() {
        return sellingpriceTF;
    }

    public Button getAddAuthorBtn() {
        return addAuthorButton;
    }
    public Button getSubmitBtn() {
        return submitBtn;
    }
    public HomeButton getHomeBtn() {
        return backToHomeBtn;
    }
    public Button getAddCategoryButton(){ return addCategoryButton; }

    public Button getRestockButton(){ return restockButton; }

    public HomeView getPrevView() {
        return prevView;
    }

    public Text getWrongTitle() {
        return wrongTitle;
    }

}


