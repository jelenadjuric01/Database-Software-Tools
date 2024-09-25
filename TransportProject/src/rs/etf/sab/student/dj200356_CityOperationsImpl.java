/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.util.List;
import rs.etf.sab.operations.CityOperations;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author Jelena
 */
public class dj200356_CityOperationsImpl implements CityOperations {
    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public int insertCity(String naziv, String posbroj) {
        String insertCity = "INSERT INTO [dbo].[Grad] " +
                                "           (Naziv, PostanskiBroj) " +
                                "       VALUES (?, ?); ";

        try(PreparedStatement ps = connection.prepareStatement(insertCity, Statement.RETURN_GENERATED_KEYS);) {           
            ps.setString(1, naziv);
            ps.setString(2, posbroj);

            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
            
        } catch (SQLException ex) {
            //Logger.getLogger(CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return -1;
    }

    @Override
    public int deleteCity(String... names) {
        String deleteCity = "DELETE FROM [dbo].[Grad] " 
                                    + " WHERE Naziv LIKE ?; ";
        int numberOfDeletedCities = 0;
        try(PreparedStatement ps = connection.prepareStatement(deleteCity);) {           
            for(String cityName: names) {

                ps.setString(1, cityName);
                numberOfDeletedCities += ps.executeUpdate();
                
            }
        } catch (SQLException ex) { 
        }
        return numberOfDeletedCities;    }

    @Override
    public boolean deleteCity(int idG) {
        String deleteCity = "DELETE FROM [dbo].[Grad] " 
                                    + " WHERE IdG = ?; ";

        try(PreparedStatement ps = connection.prepareStatement(deleteCity);) {           
            ps.setInt(1, idG);

            int numberOfDeletedCities = ps.executeUpdate();
            return numberOfDeletedCities != 0;
        } catch (SQLException ex) {
        }
        return false;    
    }

    @Override
    public List<Integer> getAllCities() {
    List<Integer> listOfIds = new ArrayList<>();
        
        String getCities = "SELECT IdG "
                                + " FROM [dbo].[Grad]; ";
       
        try(Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(getCities)){
            
            while(rs.next()){
                listOfIds.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
        }

        return listOfIds;    
    }
    
}
