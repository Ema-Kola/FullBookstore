package dao;


import model.Author;

public class AuthorsDAO extends DAO<Author>{

        public AuthorsDAO(){
            super("files/authors.dat");
        }

        public AuthorsDAO(String filename){
            super(filename);
        }



    public Author searchAuthor(String name, String surname){
        for(Author a : this.getAll()){
            if(name.equals(a.getFirstName()) && surname.equals(a.getLastName())){
                return a;
            }
        }
        return null;
    }


}




