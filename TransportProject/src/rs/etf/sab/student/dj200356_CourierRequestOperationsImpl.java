/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.util.List;
import rs.etf.sab.operations.CourierRequestOperation;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Jelena
 */
public class dj200356_CourierRequestOperationsImpl implements CourierRequestOperation {

        private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean insertCourierRequest(String KorIme, String RegBr) {
       String alreadyCourier = "SELECT KorIme " +
                                        "   FROM [dbo].[Kurir] " +
                                        "   WHERE KorIme = ?; "; 
        
        String insertAdmin = "INSERT INTO [dbo].[ZahtevZaKurira] " +
                                    "       (KorIme, RegBr) " +
                                    "   VALUES (?, ?); ";
        String vehicleExists = "select RegBr from Vozilo where RegBr = ?"; 
        String vehicleUsed = "select RegBr from Kurir where RegBr = ?";
        try(PreparedStatement ps = connection.prepareStatement(alreadyCourier);
            PreparedStatement ps1 = connection.prepareStatement(insertAdmin);
                PreparedStatement ps2 = connection.prepareStatement(vehicleExists);
                PreparedStatement ps3 = connection.prepareStatement(vehicleUsed)) {           

            ps.setString(1, KorIme);
            ps2.setString(1, RegBr);
            ps3.setString(1, RegBr);
            try(ResultSet rs = ps2.executeQuery()){
                if(!rs.next()){
                    return false;
                }
            }
            try(ResultSet rs = ps3.executeQuery()){
                if(rs.next()){
                    return false;
                }
            }
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()) {
                    return false;
                } else {            
                    ps1.setString(1, KorIme);
                    ps1.setString(2, RegBr);

                    int numberOfInsertedCourierRequests = ps1.executeUpdate();
                    return numberOfInsertedCourierRequests != 0;
                }
            }
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (Exception ex) {
//            Logger.getLogger(UserOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;

    }


    @Override
    public boolean deleteCourierRequest(String KorIme) {
        String deleteCourier = "DELETE FROM [dbo].[ZahtevZaKurira] " +
                                                "      WHERE KorIme =?";
        
        try(PreparedStatement ps = connection.prepareStatement(deleteCourier);) {           
            ps.setString(1, KorIme);
            int numberOfDeletedCourierRequests = ps.executeUpdate();
            return numberOfDeletedCourierRequests != 0;                                        
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return false;

    }

    @Override
    public boolean changeVehicleInCourierRequest(String KorIme, String RegBr) {
        String vehExists = "select RegBr from Vozilo where RegBr=?";
       String changeDrivLic = "UPDATE [dbo].[ZahtevZaKurira] " +
                                                "	SET RegBr = ? " +
                                                "	WHERE KorIme = ?; ";
        
        try(PreparedStatement ps = connection.prepareStatement(changeDrivLic);
                PreparedStatement ps1 = connection.prepareStatement(vehExists)) {           
            ps1.setString(1, RegBr);
            try(ResultSet rs = ps1.executeQuery()){
                if(!rs.next()){
                    return false;
                }
            }
            ps.setString(1, RegBr);
            ps.setString(2, KorIme);

            int numberOfUpdatedDriverLicenceNubmer = ps.executeUpdate();
            return numberOfUpdatedDriverLicenceNubmer != 0;                                        
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        } 
        
        return false;

    }

    @Override
    public List<String> getAllCourierRequests() {
        List<String> listOfUsernames = new ArrayList<>();
        
        String getRequests = "SELECT KorIme " +
                                    "       FROM [dbo].[ZahtevZaKurira] ";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getRequests)){
            
            while(rs.next()){
                listOfUsernames.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfUsernames;


    }
    @Override
    public boolean grantRequest(String KorIme) {
        String grantRequest = "{call grantRequest(?)}";
        
        try(CallableStatement cs = connection.prepareCall(grantRequest)){
            cs.setString(1, KorIme);
            cs.execute();
            return true;
        }   catch (SQLException ex) {
                //Logger.getLogger(CourierRequestOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        return false;
    }
     
}
