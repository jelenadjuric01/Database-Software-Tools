/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.util.List;
import rs.etf.sab.operations.UserOperations;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Jelena
 */
public class dj200356_UserOperationsImpl implements UserOperations {

    private final Connection connection = DB.getInstance().getConnection();

    
    @Override
    public boolean insertUser(String korIme, String Ime, String Prezime, String Sifra) {
        String insertUser = "INSERT INTO [dbo].[Korisnik] " +
                                    "       (Ime, Prezime, KorIme, Sifra) \n" +
                                    "   VALUES (?, ?, ?, ?) ";
        
        try(PreparedStatement ps = connection.prepareStatement(insertUser);) {           
            ps.setString(1, Ime);
            ps.setString(2, Prezime);
            ps.setString(3, korIme);
            ps.setString(4, Sifra);
           
            
            int numberOfInsertedUsers = ps.executeUpdate();
            return numberOfInsertedUsers != 0;
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (Exception ex) {
//            Logger.getLogger(UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        
        
    }

    @Override
    public int declareAdmin(String string) {
        String checkAdmin = "SELECT * from Administrator where KorIme=?";
        
        try(PreparedStatement pt = connection.prepareStatement(checkAdmin)){
            pt.setString(1, string);
            try(ResultSet rs = pt.executeQuery()){
                if(rs.next()){
                    return 1;
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String checkUser = "SELECT * from Korisnik where KorIme=?";
        
        try(PreparedStatement pt = connection.prepareStatement(checkUser)){
            pt.setString(1, string);
            try(ResultSet rs = pt.executeQuery()){
                if(!rs.next()){
                    return 2;
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String declareAdmin = "INSERT INTO [dbo].[Administrator] " +
                                    "       (KorIme) " +
                                    "   VALUES (?); ";
        
        try(PreparedStatement ps = connection.prepareStatement(declareAdmin);) {           

            ps.setString(1, string);
            int executeUpdate = ps.executeUpdate();
            if(executeUpdate!=0){
                return 0;
            }

            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (Exception ex) {
//            Logger.getLogger(UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 1;
        
    }

    @Override
    public Integer getSentPackages(String... strings) {
        String numberOfSentPackages = "SELECT BrPoslatihPaketa " +
                                                "	FROM [dbo].[Korisnik] " +
                                                "	WHERE KorIme=? " ;

        int numOfSentPackages = 0;
        boolean hasExistingUser = false;
        for(String userName: strings) {

            try(PreparedStatement ps = connection.prepareStatement(numberOfSentPackages);) {           
                ps.setString(1, userName);

                try(ResultSet rs = ps.executeQuery()){
                    if(rs.next()) {
                        hasExistingUser = true;                       

                        numOfSentPackages += rs.getInt(1);
                    }
                }                

            } catch (SQLException ex) {
//                Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
            } catch (Exception ex) {
//                Logger.getLogger(UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if(hasExistingUser)
            return numOfSentPackages;
        return null;
        
    }
    @Override
    public int deleteUsers(String... strings) {
        String deleteUser = "DELETE FROM [dbo].[Korisnik] " 
                                    + " WHERE KorIme LIKE ?; ";
        int numberOfDeletedUsers = 0;
        for(String username: strings) {
            try(PreparedStatement ps = connection.prepareStatement(deleteUser);) {           

                    ps.setString(1, username);
                    numberOfDeletedUsers += ps.executeUpdate();

            } catch (SQLException ex) {
//                Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
            }
        }
        return numberOfDeletedUsers;
    }

    @Override
    public List<String> getAllUsers() {
        List<String> listOfUsernames = new ArrayList<>();
        
        String getAllUsernamesQuery = "SELECT KorIme "
                                + " FROM [dbo].[Korisnik]; ";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getAllUsernamesQuery)){
            
            while(rs.next()){
                listOfUsernames.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfUsernames;


    }
    
}
