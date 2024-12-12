package dao;

import model.Role;



public class PermissionsDAO extends DAO<Role>{

    public PermissionsDAO(){
        super("files/permissions.dat");
    }

    public Role getCheckOutBookPermission(){
             return super.getAll().get(0);
    }
    public Role getAddBookPermission(){
        return super.getAll().get(1);
    }

    public Role getBookStatsPermission(){
        return super.getAll().get(2);
    }
    public Role getLibrarianStatsPermission(){

        return super.getAll().get(3);
    }

    public Role getManageEmployeesPermission(){

        return super.getAll().get(4);
    }

    public Role getFinanceStatsPermission(){

        return super.getAll().get(5);
    }

    public Role getManageLibraryPermission(){

        return super.getAll().get(6);
    }

    public boolean setCheckOutBookPermission(Role x){
        getAll().set(0,x);
       return updateAll();
    }

    public boolean setAddBookPermission(Role x){
        getAll().set(1,x);
        return updateAll();
    }

    public boolean setBookStatsPermission(Role x){
        getAll().set(2,x);
       return updateAll();
    }
    public boolean setLibrarianStatsPermission(Role x){
        getAll().set(3,x);
       return updateAll();
    }

    public boolean setManageEmployeesPermission(Role x){
        getAll().set(4,x);
        return updateAll();
    }

    public boolean setFinanceStatsPermission(Role x){
        getAll().set(5,x);
        return updateAll();
    }

    public boolean setManageLibraryPermission(Role x){
        getAll().set(6,x);
        return updateAll();
    }





}




