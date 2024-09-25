/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import rs.etf.sab.operations.GeneralOperations;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author Jelena
 */
public class dj200356_GeneralOperationsImpl implements GeneralOperations {
    private final Connection connection = DB.getInstance().getConnection();

    @Override
    public void eraseAll() {
        String eraseAllQuery = "{ call eraseAll() }; ";
        
        try(CallableStatement cs = connection.prepareCall(eraseAllQuery)) {
            
            cs.execute();
            
        } catch (SQLException ex) {
            Logger.getLogger(dj200356_GeneralOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
}
