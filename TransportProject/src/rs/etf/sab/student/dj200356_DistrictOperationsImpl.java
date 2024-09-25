/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import rs.etf.sab.operations.DistrictOperations;

/**
 *
 * @author Jelena
 */
public class dj200356_DistrictOperationsImpl implements DistrictOperations {
    private final Connection connection = DB.getInstance().getConnection();

    public BigDecimal getDistance(int from,int to) throws SQLException{
        String getDistricts = "select X, Y from Opstina where IdO=?";
        try(PreparedStatement ps=connection.prepareStatement(getDistricts)){
            ps.setInt(1, from);
            int X1=0,Y1=0;
            try(ResultSet rs= ps.executeQuery()){
                if(rs.next()){
                    X1 = rs.getInt(1);
                    Y1= rs.getInt(2);
                    
                }
            }
            ps.setInt(1, to);
                    try(ResultSet rs1 =ps.executeQuery()){
                        if(rs1.next()){
                            return BigDecimal.valueOf(Math.sqrt(Math.pow(X1-rs1.getInt(1), 2)+Math.pow(Y1-rs1.getInt(2), 2)));
                        }
                    }
        }
        return null;
    }
    @Override
    public int insertDistrict(String Naziv, int IdG, int X, int Y) {
        String insertDis = "INSERT INTO [dbo].[Opstina] " +
                                    "       (Naziv, X, Y, IdG) \n" +
                                    "   VALUES (?, ?, ?, ?) ";
        
        try(PreparedStatement ps = connection.prepareStatement(insertDis, Statement.RETURN_GENERATED_KEYS);) {           
            ps.setString(1, Naziv);
            ps.setInt(2, X);
            ps.setInt(3, Y);
            ps.setInt(4, IdG);
           

            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException ex) { 
            //Logger.getLogger(dj200356_DistrictOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    public int getDistrictId(String name) throws SQLException{
        String string = "select IdO from Opstina where Naziv like ?";
        try(PreparedStatement ps=connection.prepareStatement(string)){
            ps.setString(1, name);
            try(ResultSet rs=ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
                else{
                    throw new SQLException();
                }
            }
        }
    }

    @Override
    public int deleteDistricts(String... names) {
        String deleteDis = "DELETE FROM [dbo].[Opstina] " +
                    "	WHERE Naziv LIKE ?";
        int numberOfDeletedDisctricts = 0;

        try(PreparedStatement ps = connection.prepareStatement(deleteDis);) {   

            for(String districtName: names) {
                
               
                ps.setString(1, districtName);
                numberOfDeletedDisctricts += ps.executeUpdate();
                
            }
            return numberOfDeletedDisctricts;

        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return numberOfDeletedDisctricts;        
        
    }

    @Override
    public boolean deleteDistrict(int IdO) {
        String deleteDis = "DELETE FROM [dbo].[Opstina] " 
                                        + " WHERE IdO = ?; ";

        try(PreparedStatement ps = connection.prepareStatement(deleteDis);) {           
            ps.setInt(1, IdO);

            int numberOfDeletedDisctricts = ps.executeUpdate();
            return numberOfDeletedDisctricts != 0;
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return false;

    }

    @Override
    public int deleteAllDistrictsFromCity(String Grad) {
        String deleteDis = "DELETE FROM [dbo].[Opstina] " 
                                            + " WHERE IdG = (select IdG from Grad where Naziv = ? ); ";

        try(PreparedStatement ps = connection.prepareStatement(deleteDis);) {           
            ps.setString(1, Grad);

            int numberOfDeletedDisctricts = ps.executeUpdate();
            return numberOfDeletedDisctricts;
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return 0;
    }

    @Override
    public List<Integer> getAllDistricts() {
        List<Integer> listOfIds = new ArrayList<>();
        
        String getAllIdAQuery = "SELECT IdO "
                                + " FROM [dbo].[Opstina]; ";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getAllIdAQuery)){
            
            while(rs.next()){
                listOfIds.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }

        return listOfIds;
    }

    @Override
    public List<Integer> getAllDistrictsFromCity(int IdG) {
        List<Integer> listOfIds = new ArrayList<>();
        
        String getAllIdAFromCityQuery = "SELECT IdO "
                                        + " FROM [dbo].[Opstina] "
                                        + " WHERE IdG = ?; ";

        try(PreparedStatement ps = connection.prepareStatement(getAllIdAFromCityQuery);) {           
            
            ps.setInt(1, IdG);
            try(ResultSet rs = ps.executeQuery()){
                
                while(rs.next()){
                    listOfIds.add(rs.getInt(1));
                }
                if(!listOfIds.isEmpty())
                    return listOfIds;
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        
        return null;
    }
    
}
