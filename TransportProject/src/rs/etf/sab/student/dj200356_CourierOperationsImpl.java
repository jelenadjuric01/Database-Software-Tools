/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;
import java.util.logging.Level;
import rs.etf.sab.operations.CourierOperations;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author Jelena
 */

//AKO NESTO NE RADI TESTIRAJ OVO
public class dj200356_CourierOperationsImpl implements CourierOperations {

    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public boolean insertCourier(String KorIme, String RegBr) {
        String insertCourier = "INSERT INTO [dbo].[Kurir] " +
                                    "       (KorIme, RegBr)" +
                                    "   VALUES (?, ?) ";
        
        try(PreparedStatement ps = connection.prepareStatement(insertCourier);) {           
            ps.setString(1, KorIme);
            ps.setString(2, RegBr);
            
            int numberOfInsertedCouriers = ps.executeUpdate();
            return numberOfInsertedCouriers != 0;
            
        } catch (SQLException ex) {
            //Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        } catch (Exception ex) {
            //Logger.getLogger(CourierOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false; 
    }
    @Override
    public boolean deleteCourier(String KorIme) {
        String deleteCourier = "DELETE FROM [dbo].[Kurir] " +
                                                "      WHERE KorIme = ? ";
        try(PreparedStatement ps = connection.prepareStatement(deleteCourier);
                ) {           
            
            ps.setString(1, KorIme);
            int numberOfDeletedCouriers = ps.executeUpdate();
            return numberOfDeletedCouriers != 0;                                        
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return false;

    }

    @Override
    public List<String> getCouriersWithStatus(int Status) {
        List<String> listOfUsernames = new ArrayList<>();
        
        String getCouriers = "SELECT KorIme " +
                                    "       FROM [dbo].[Kurir] " +
                                    "       WHERE Status = ? ";
       
        try(PreparedStatement ps = connection.prepareStatement(getCouriers);) {
            
            ps.setInt(1, Status);
            
            try(ResultSet rs = ps.executeQuery()){
            
                while(rs.next()){
                    listOfUsernames.add(rs.getString(1));
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfUsernames;
    }

    @Override
    public List<String> getAllCouriers() {
        List<String> listOfUsernames = new ArrayList<>();
        
        String getCouriers = "SELECT KorIme " +
                                    "       FROM [dbo].[Kurir]";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getCouriers)){
            
            while(rs.next()){
                listOfUsernames.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
//            Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfUsernames;
    }

    @Override
    public BigDecimal getAverageCourierProfit(int BrIsporucenhPaketa) {
        String getProfit = "SELECT AVG(Profit) AS AverageProfit" +
                                    "	FROM [dbo].[Kurir] " +
                                    "	WHERE  BrisporucenihPak>= ? ";
       
        BigDecimal averageProfit = BigDecimal.ZERO;
        try(PreparedStatement ps = connection.prepareStatement(getProfit);) {
            
         
                ps.setInt(1, BrIsporucenhPaketa);
            
            
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next())
                    averageProfit = rs.getBigDecimal(1);
            }
        } catch (SQLException ex) {
            Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return averageProfit;

    }
    
}
