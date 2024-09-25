/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.List;
import rs.etf.sab.operations.VehicleOperations;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jelena
 */
public class dj200356_VehicleOperationsImpl implements VehicleOperations {
    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean insertVehicle(String RegBr, int TipGoriva, BigDecimal Potrosnja) {
        String insertVeh = "INSERT INTO [dbo].[Vozilo] " +
                                    "       (RegBr, TipGoriva, Potrosnja)" +
                                    "   VALUES (?, ?, ?) ";
        
        try(PreparedStatement ps = connection.prepareStatement(insertVeh);) {           
            ps.setString(1, RegBr);
            ps.setInt(2, TipGoriva);
            ps.setBigDecimal(3, Potrosnja);
          
            
            int numberOfInsertedVehicles = ps.executeUpdate();
            return numberOfInsertedVehicles != 0;
            
        } catch (SQLException ex) { 
            //Logger.getLogger(dj200356_VehicleOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;        

    }

    @Override
    public int deleteVehicles(String... strings) {
        String deleteVeh = "DELETE FROM [dbo].[Vozilo] " 
                                                        + " WHERE RegBr= ?; ";
        int numberOfDeletedVehicles = 0;
        try(PreparedStatement ps = connection.prepareStatement(deleteVeh);) {           
            for(String licencePlateNumber: strings) {

                ps.setString(1, licencePlateNumber);
                numberOfDeletedVehicles += ps.executeUpdate();
                
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return numberOfDeletedVehicles;
    }


    @Override
    public List<String> getAllVehichles() {
         List<String> listOfId = new ArrayList<>();
        
        String getVeh = "SELECT RegBr "
                                      + " FROM [dbo].[Vozilo]; ";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getVeh)){
            
            while(rs.next()){
                listOfId.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfId;
    }

    @Override
    public boolean changeFuelType(String string, int i) {
        String changeFuel = "UPDATE [dbo].[Vozilo] " +
                                "	SET " +
                                "		TipGoriva = ? " +
                                "	WHERE RegBr = ?; ";
        
        try(PreparedStatement ps = connection.prepareStatement(changeFuel);) {           
            ps.setInt(1, i);
            ps.setString(2, string);
            int numberOfUpdatedVehicles = ps.executeUpdate();
            return numberOfUpdatedVehicles != 0;                                        
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return false;
    }

    @Override
    public boolean changeConsumption(String string, BigDecimal bd) {
        String changeConsumption = "UPDATE [dbo].[Vozilo] " +
                                "	SET " +
                                "		Potrosnja = ? " +
                                "	WHERE RegBr = ?; ";
        
        try(PreparedStatement ps = connection.prepareStatement(changeConsumption);) {           
            ps.setBigDecimal(1, bd);
            ps.setString(2, string);
            int numberOfUpdatedVehicles = ps.executeUpdate();
            return numberOfUpdatedVehicles != 0;                                        
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return false;
    }

}
