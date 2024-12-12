package dao;

public class CategoryDAO extends DAO<String>{


        public CategoryDAO(){
            super("files/categories.dat");
           // this.categories=this.getAll();
        }



    public String searchCategory(String category){
        for(String c : this.getAll()){
            if(category.equals(c)){
                return c;
            }
        }
        return null;

    }


}






