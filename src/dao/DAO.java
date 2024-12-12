package dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;


public abstract class DAO<E> {



        public final String FILE_PATH;
        private final File DATA_FILE;
        private final ObservableList<E> items = FXCollections.observableArrayList();

        public ObservableList<E> getAll() {
            if(items.isEmpty()) {
                loadItemsFromFile();
            }
            return items;
        }

        public DAO(String FILE_PATH){
            this.FILE_PATH=FILE_PATH;
            this.DATA_FILE =  new File(FILE_PATH);
        }
        private void loadItemsFromFile() {
            try(ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(DATA_FILE))) {
                while(true) {
                    E item = (E) inputStream.readObject();
                    items.add(item);
                }
            }
            catch (EOFException ignored) {
            }
            catch (IOException | ClassNotFoundException ex) {
                // log to a file
                System.out.println(ex.getMessage());
            }
        }

        public boolean create(E item) {
            try(FileOutputStream outputStream = new FileOutputStream(DATA_FILE, true)) {
                ObjectOutputStream writer;

                if (DATA_FILE.length() > 0)
                    writer = new HeaderlessObjectOutputStream(outputStream);
                else
                    writer = new ObjectOutputStream(outputStream);

                writer.writeObject(item);
                items.add(item);
                return true;
            } catch(IOException ex) {
                return false;
            }
        }



        public boolean updateAll() {
            try(ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
                for(E e : items) {
                    outputStream.writeObject(e);
                }
                return true;
            } catch (IOException ex) {
                return false;
            }
        }


    public void printAll(){
        for(E e : this.getAll()) {
            System.out.println(e);

        }

    }




}
