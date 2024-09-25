/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.PackageOperations;
import rs.etf.sab.student.util.Util;

/**
 *
 * @author Jelena
 */
public class dj200356_PackageOperationsImpl implements PackageOperations {
    private final dj200356_DistrictOperationsImpl district = new dj200356_DistrictOperationsImpl();
    private final Connection connection = DB.getInstance().getConnection();
    
    private BigDecimal returnBasicPrice(int type){
        switch (type) {
            case 0 -> {
                return BigDecimal.TEN;
            }
                
            case 1 -> {
                return BigDecimal.valueOf(25);
            }
            case 2 -> {
                return BigDecimal.valueOf(75);
            }
            default -> throw new AssertionError();
        }
    }
    
     private BigDecimal returnWeightPrice(int type){
        switch (type) {
            case 0 -> {
                return BigDecimal.ZERO;
            }
                
            case 1 -> {
                return BigDecimal.ONE;
            }
            case 2 -> {
                return BigDecimal.TWO;
            }
            default -> throw new AssertionError();
        }
    }
      private BigDecimal returnKiloPrice(int type){
        switch (type) {
            case 0 -> {
                return BigDecimal.ONE;
            }
                
            case 1 -> {
                return BigDecimal.valueOf(100);
            }
            case 2 -> {
                return BigDecimal.valueOf(300);
            }
            default -> throw new AssertionError();
        }
    }
    public BigDecimal calculatePrice(int packetType, BigDecimal weight,int districtFrom,int disctrictTo){
        try {
            BigDecimal euclid= district.getDistance(districtFrom, disctrictTo);
            if(euclid==null) throw new SQLException();
            BigDecimal weightFactor = returnWeightPrice(packetType).multiply(weight).multiply(returnKiloPrice(packetType));
            return euclid.multiply(weightFactor.add(returnBasicPrice(packetType)));
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.ZERO;
    }
    @Override
    public int insertPackage(int districtFrom,int districtTo,String userName,int packageType,BigDecimal weight) {
        String insertPackage = "insert into Paket(Cena,Korisnik,IdOPre,IdOIsp,TipPaketa,Tezina) values (?,?,?,?,?,?)";
        try(PreparedStatement ps=connection.prepareStatement(insertPackage,Statement.RETURN_GENERATED_KEYS)){
            
            ps.setBigDecimal(1, calculatePrice(packageType, weight, districtFrom, districtTo));
            ps.setString(2, userName);
            ps.setInt(3, districtFrom);
            ps.setInt(4, districtTo);
            ps.setInt(5, packageType);
            ps.setBigDecimal(6, weight);
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) {
                   
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
           //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        } 
        return -1;
    }
//potencijalno proveriti da li je kurir u statusu ne vozi
    @Override
    public int insertTransportOffer(String couriersUserName,int packageId,BigDecimal pricePercentage) {
        String insertOffer="insert into Ponuda(Procenat,IdP,KorIme) values (?,?,?)";
        String getKurir = "select KorIme from Kurir where KorIme=? and Status = 0";
        String getPackage = "select IdP from Paket where IdP=? and Status = 0";
        if(pricePercentage==null){
            Random random = new Random();
             double randomDouble = -10 + (20 * random.nextDouble());
             pricePercentage = BigDecimal.valueOf(randomDouble);

        }
        try(PreparedStatement ps=connection.prepareStatement(insertOffer,Statement.RETURN_GENERATED_KEYS);PreparedStatement ps1 = connection.prepareStatement(getKurir);
                PreparedStatement ps2 = connection.prepareStatement(getPackage)){
           ps1.setString(1, couriersUserName);
           ps2.setInt(1, packageId);
           try(ResultSet rs = ps2.executeQuery()){
               if(!rs.next()){
                   return -1;
               }
           }
           try(ResultSet rs = ps1.executeQuery()){
               if(!rs.next()){
                   return -1;
               }
           }
            ps.setBigDecimal(1, pricePercentage);
            ps.setInt(2, packageId);
            ps.setString(3, couriersUserName);
            ps.executeUpdate();
            try(ResultSet rs1 = ps.getGeneratedKeys()){
                if(rs1.next()){
                    return rs1.getInt(1);
                }
            }
        } catch (SQLException ex) {
           //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public boolean acceptAnOffer(int IdPon) {
        String getOffer = "select IdP,KorIme,Procenat from Ponuda where IdPon = ?";
        String updatePaket = "update Paket set Cena =Cena+(Cena*?), Status = 1, Kurir=?, VremePrihZaht=? where IdP=?";
        //

        try(PreparedStatement ps = connection.prepareStatement(getOffer);PreparedStatement ps1 = connection.prepareStatement(updatePaket);
                ){
            ps.setInt(1, IdPon);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    connection.setAutoCommit(false);
                int idP=rs.getInt(1);
                String korIme = rs.getString(2);
                BigDecimal procentage = rs.getBigDecimal(3);
                ps1.setBigDecimal(1, procentage.divide(BigDecimal.valueOf(100)));
                ps1.setString(2, korIme);
                ps1.setTimestamp(3, new Timestamp(Calendar.getInstance().getTimeInMillis()));
                ps1.setInt(4, idP);
               
                int executeUpdate = ps1.executeUpdate();
                if(executeUpdate==0){
                    throw new SQLException();
                }
                connection.commit();
                return executeUpdate!=0;
                }
            }
        } catch (SQLException ex) {
            try {
                //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
                connection.rollback();
            } catch (SQLException ex1) {
                //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public List<Integer> getAllOffers() {
        List<Integer> listOfOffers=new ArrayList<>();
        String getallOfers="select IdPon from Ponuda";
        try(Statement st=connection.createStatement();
            ResultSet offers = st.executeQuery(getallOfers)){
            while(offers.next()){
                listOfOffers.add(offers.getInt(1));
            }
        } catch (SQLException ex) {
           // Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOfOffers;
    }

    @Override
    public List<Pair<Integer, BigDecimal>> getAllOffersForPackage(int IdP) {
        List<Pair<Integer,BigDecimal>> offers = new ArrayList<>();
        String getOffers = "select IdPon,Procenat from Ponuda\n" +
"where IdP=?";
        try(PreparedStatement ps=connection.prepareStatement(getOffers)){
            ps.setInt(1, IdP);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    dj200356_Par<Integer,BigDecimal> par = new dj200356_Par<>(rs.getInt(1),rs.getBigDecimal(2));
                    offers.add(par);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return offers;
    }

    @Override
    public boolean deletePackage(int IdP) {
        String deleteRequest = "delete from Ponuda where IdP=?";
        String deletePackage = "delete from Paket where IdP=? and Status in (0,1)";
        try(PreparedStatement ps1=connection.prepareStatement(deleteRequest);PreparedStatement ps2=connection.prepareStatement(deletePackage);
                ){
            ps1.setInt(1, IdP);
            ps2.setInt(1, IdP);
            connection.setAutoCommit(false);
            ps1.executeUpdate();
            int executeUpdate1 = ps2.executeUpdate();
            if( executeUpdate1!=0){
                connection.commit();
                return true;
            }
            
        } catch (SQLException ex) {
           // Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
              //  Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }

        }
        finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return false;
    }

    @Override
    public boolean changeWeight(int IdP, BigDecimal weight) {
        String changeType="update Paket set Tezina = ? where IdP=? and Status =0";
        try(PreparedStatement ps=connection.prepareStatement(changeType)){
            ps.setBigDecimal(1, weight);
            ps.setInt(2, IdP);
            int executeUpdate = ps.executeUpdate();
            return executeUpdate!=0;
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public boolean changeType(int IdP, int type) {
        String changeType="update Paket set TipPaketa = ? where IdP=? and Status =0";
        try(PreparedStatement ps=connection.prepareStatement(changeType)){
            ps.setInt(1, type);
            ps.setInt(2, IdP);
            int executeUpdate = ps.executeUpdate();
            return executeUpdate!=0;
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public Integer getDeliveryStatus(int IdP) {
        String getDeliveryStatus = "select Status from Paket where IdP=?";
        try(PreparedStatement ps=connection.prepareStatement(getDeliveryStatus)){
            ps.setInt(1, IdP);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public BigDecimal getPriceOfDelivery(int IdP) {
        String getDelivery = "select Cena from Paket where IdP=?";
        try(PreparedStatement ps=connection.prepareStatement(getDelivery)){
            ps.setInt(1, IdP);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return rs.getBigDecimal(1);
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    

    @Override
    public Date getAcceptanceTime(int IdP) {
        String getAcceptanceTimeQuery = "SELECT [VremePrihZaht] " +
                                        "	FROM [dbo].[Paket] " +
                                        "	WHERE IdP = ? " +
                                        "		AND Status IN (1, 2, 3) ";
        
        try(PreparedStatement ps = connection.prepareStatement(getAcceptanceTimeQuery);) {           
            
            ps.setInt(1, IdP);
            try(ResultSet rs = ps.executeQuery()){                
                if(rs.next()){
                    return rs.getDate(1);
                }
                
            }
        } catch (SQLException ex) {
           // Logger.getLogger(dj200356_CityOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);            
        }
        return null;
    }
    

    @Override
    public List<Integer> getAllPackagesWithSpecificType(int IdP) {
        List<Integer> listOfPackages=new ArrayList<>();
        String getAllPackages="select IdP from Paket where TipPaketa=?";
        try(PreparedStatement st = connection.prepareStatement(getAllPackages)){
            st.setInt(1, IdP);
            try(ResultSet rs = st.executeQuery()){
            while(rs.next()){
                listOfPackages.add(rs.getInt(1));
            }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOfPackages;
    }


    @Override
    public List<Integer> getAllPackages() {
        List<Integer> listOfPackages=new ArrayList<>();
        String getAllPackages="select IdP from Paket";
        try(Statement st = connection.createStatement();ResultSet rs = st.executeQuery(getAllPackages)){
            while(rs.next()){
                listOfPackages.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            //Logger.getLogger(PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listOfPackages;
    }

    @Override
    public List<Integer> getDrive(String kurir) {
        String getDribe = "select IdP from Vozi where KorIme=?";
        List<Integer> getDrives = new ArrayList<>();
        try(PreparedStatement ps= connection.prepareStatement(getDribe)){
            ps.setString(1, kurir);
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    getDrives.add(rs.getInt(1));
                }
            }
        } catch (SQLException ex) {
           // Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return getDrives;
    }
    private double getSpent(int type){
        switch (type) {
            case 0 -> {
                return 15;
            }
            case 1 -> {
                return 36;
            }
            case 2 -> {
                return 32;
            }
                
            default -> throw new AssertionError();
        }
    }
    private BigDecimal getProfit(String korIme){
        String getVozi = "select Paket.IdP,IdOPre,IdOIsp from Paket join Vozi on (Vozi.IdP=Paket.IdP) where KorIme=? order by VremePrihZaht";
        double profit = 0;
        double fuel=0;
        double spend = 0;
        String getSum = "select sum(Cena) from Paket join Vozi on (Vozi.IdP=Paket.IdP) where KorIme=?";
        String getFuel = "select TipGoriva, Potrosnja from Vozilo join Kurir on (Kurir.RegBr=Vozilo.RegBr) where KorIme=?";
        try(PreparedStatement ps = connection.prepareStatement(getVozi);
                PreparedStatement ps1 = connection.prepareStatement(getSum);
                PreparedStatement ps2 = connection.prepareStatement(getFuel)){
            ps.setString(1, korIme);
            ps1.setString(1, korIme);
            ps2.setString(1, korIme);
            try(ResultSet rs = ps2.executeQuery()){
                if(rs.next()){
                    fuel=getSpent(rs.getInt(1));
                    spend = rs.getBigDecimal(2).doubleValue();
                }
            }
            try(ResultSet rs = ps1.executeQuery()){
                if(rs.next()){
                    //System.out.println("gain:"+rs.getBigDecimal(1));
                    profit+=rs.getBigDecimal(1).doubleValue();
                }
            }
            try(ResultSet rs =ps.executeQuery()){
                List<Integer> districts = new ArrayList<>();
                while(rs.next()){
                    districts.add(rs.getInt(2));
                    districts.add(rs.getInt(3));
                }
                double distance=0;
                for(int i = 0;i+1<districts.size();i++){
                    int j = i+1;
                    //System.out.println("Distance:"+district.getDistance(districts.get(i), districts.get(j)));
                    distance+=district.getDistance(districts.get(i), districts.get(j)).doubleValue();
                    
                }
                //System.out.println("Distance end:"+distance);
                //System.out.println("Loss:"+distance*spend*fuel);
                profit-=(distance*spend*fuel);
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return BigDecimal.valueOf(profit);
    }
//status kurira, isporuceni paketi tj njihov broj, status paketa, profit
    @Override
    public int driveNextPackage(String kurir) {
        String getVozi="select IdP,KorIme from Vozi where KorIme=?";
        String getPakets = "select IdP,VremePrihZaht,IdOPre,IdOIsp from Paket where Kurir=? and Status = 1";
        String insertVozi = "insert into Vozi(IdP,KorIme) values(?,?)";
        String updatePaket = "update Paket set Status = ? where IdP=?";
        String deliverPaket = "select top(2) Paket.IdP from Paket join Vozi on(Vozi.IdP=Paket.IdP) where Status = 2 and KorIme=? order by VremePrihZaht";
        String updateKurir = "update Kurir set Status = ? where KorIme=?";
        String updateNumber = "update Kurir set BrisporucenihPak=BrisporucenihPak+1 where KorIme=?";
        String deleteFromVozi = "delete from Vozi where KorIme=?";
        String updateProfit ="update Kurir set Profit=? where KorIme=?";
        String incrementNumber = "update Korisnik set BrPoslatihPaketa=BrPoslatihPaketa+1 where KorIme=(select Korisnik from Paket where IdP=?)";
        try(PreparedStatement stmt = connection.prepareStatement(getVozi);PreparedStatement ps = connection.prepareStatement(getPakets);
                PreparedStatement ps2 = connection.prepareStatement(insertVozi);
                PreparedStatement ps3 = connection.prepareStatement(updatePaket);
                PreparedStatement ps4 = connection.prepareStatement(updateKurir);
                PreparedStatement ps5 = connection.prepareStatement(updateNumber);
                PreparedStatement ps6 = connection.prepareStatement(deleteFromVozi);
                PreparedStatement ps7 = connection.prepareStatement(deliverPaket);
                PreparedStatement ps8 = connection.prepareStatement(updateProfit);
                PreparedStatement ps9 = connection.prepareStatement(incrementNumber)){
            connection.setAutoCommit(false);
            stmt.setString(1, kurir);
            try(ResultSet rs = stmt.executeQuery()){
                if(rs.next()){
                    ps7.setString(1, kurir);
                    try(ResultSet rs3=ps7.executeQuery()){
                        if(rs3.next()){
                                ps3.setInt(1, 3);
                                ps3.setInt(2,rs3.getInt(1) );
                                ps5.setString(1, kurir);
                                ps3.executeUpdate();
                                ps5.executeUpdate();
                                int returnId=rs3.getInt(1);
                                if(!rs3.next()){
                                    ps4.setString(2, kurir);
                                ps4.setInt(1, 0);
                                ps4.executeUpdate();
                                ps8.setString(2, kurir);
                                ps8.setBigDecimal(1, getProfit(kurir));
                                ps8.executeUpdate();
                                ps6.setString(1, kurir);
                                ps6.executeUpdate();
                                }
                                connection.commit();
                                return returnId;

                                }
                        else{
                            
                            return -1;
                        }
                    }
                }
                else{
                    ps.setString(1, kurir);
                    try(ResultSet rs1 = ps.executeQuery()){
                        if(rs1.next()){
                            ps2.setInt(1, rs1.getInt(1));
                            ps3.setInt(2, rs1.getInt(1));
                            ps3.setInt(1, 2);
                            ps9.setInt(1, rs1.getInt(1));
                            ps9.executeUpdate();
                            ps2.setString(2, kurir);
                            ps2.executeUpdate();
                            ps3.executeUpdate();
                            ps4.setInt(1, 1);
                            ps4.setString(2,kurir );
                            ps4.executeUpdate();
                            while(rs1.next()){
                                ps2.setInt(1, rs1.getInt(1));
                            ps3.setInt(2, rs1.getInt(1));
                            ps3.setInt(1, 2);
                             ps9.setInt(1, rs1.getInt(1));
                            ps9.executeUpdate();
                            ps2.setString(2, kurir);
                            ps2.executeUpdate();
                            ps3.executeUpdate();
                            }
                            ps7.setString(1, kurir);
                            try(ResultSet rs2 = ps7.executeQuery()){
                                if(rs2.next()){
                                ps3.setInt(1, 3);
                                ps3.setInt(2,rs2.getInt(1) );
                                ps5.setString(1, kurir);
                                ps3.executeUpdate();
                                ps5.executeUpdate();
                                int returnId=rs2.getInt(1);
                                if(!rs2.next()){
                                    ps4.setString(2, kurir);
                                ps4.setInt(1, 0);
                                ps4.executeUpdate();
                                ps8.setString(2, kurir);
                                ps8.setBigDecimal(1, getProfit(kurir));
                                ps8.executeUpdate();
                                ps6.setString(1, kurir);
                                ps6.executeUpdate();
                                }
                                connection.commit();
                                return returnId;
                               
                                }
                                else{
                                    connection.rollback();
                                    return -2;
                                }
                            }
                            
                        }
                        else{
                            return -1;
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            try {
                connection.rollback();
            } catch (SQLException ex1) {
                //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        finally{
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                //Logger.getLogger(dj200356_PackageOperationsImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return 2;
    }
        public static void main(String[] args) {
             dj200356_CityOperationsImpl cityOperations = new dj200356_CityOperationsImpl();
     dj200356_CourierOperationsImpl courierOperations = new dj200356_CourierOperationsImpl();
     dj200356_CourierRequestOperationsImpl courierRequestOperation=new dj200356_CourierRequestOperationsImpl();
     dj200356_DistrictOperationsImpl districtOperations= new dj200356_DistrictOperationsImpl();
     dj200356_PackageOperationsImpl packageOperations = new dj200356_PackageOperationsImpl();
     dj200356_UserOperationsImpl userOperations = new dj200356_UserOperationsImpl();
     dj200356_VehicleOperationsImpl vehicleOperations = new dj200356_VehicleOperationsImpl();
            dj200356_GeneralOperationsImpl g = new dj200356_GeneralOperationsImpl();
            g.eraseAll();
            
            String courierLastName = "Ckalja";
        String courierFirstName = "Pero";
        String courierUsername = "perkan";
        String password = "sabi2018";
        boolean userInserted = userOperations.insertUser(courierUsername, courierFirstName, courierLastName, password);
        if (userInserted) {
            System.out.println("User inserted successfully.");
        } else {
            System.out.println("User insertion failed.");
            return;
        }

        String licencePlate = "BG323WE";
        int fuelType = 0;
        BigDecimal fuelConsumption = new BigDecimal(8.3D);
        boolean vehicleInserted = vehicleOperations.insertVehicle(licencePlate, fuelType, fuelConsumption);
        if (vehicleInserted) {
            System.out.println("Vehicle inserted successfully.");
        } else {
            System.out.println("Vehicle insertion failed.");
            return;
        }

        boolean courierRequestInserted = courierRequestOperation.insertCourierRequest(courierUsername, licencePlate);
        if (courierRequestInserted) {
            System.out.println("Courier request inserted successfully.");
        } else {
            System.out.println("Courier request insertion failed.");
            return;
        }

        boolean requestGranted = courierRequestOperation.grantRequest(courierUsername);
        if (requestGranted) {
            System.out.println("Courier request granted successfully.");
        } else {
            System.out.println("Courier request granting failed.");
            return;
        }

        if (courierOperations.getAllCouriers().contains(courierUsername)) {
            System.out.println("Courier is in the list of all couriers.");
        } else {
            System.out.println("Courier is not in the list of all couriers.");
            return;
        }

        String senderUsername = "masa";
        String senderFirstName = "Masana";
        String senderLastName = "Leposava";
        password = "lepasampasta1";
        boolean senderInserted = userOperations.insertUser(senderUsername, senderFirstName, senderLastName, password);
        if (senderInserted) {
            System.out.println("Sender inserted successfully.");
        } else {
            System.out.println("Sender insertion failed.");
            return;
        }

        int cityId = cityOperations.insertCity("Novo Milosevo", "21234");
        if (cityId > 0) {
            System.out.println("City inserted successfully.");
        } else {
            System.out.println("City insertion failed.");
            return;
        }

        int cordXd1 = 10;
        int cordYd1 = 2;
        int districtIdOne = districtOperations.insertDistrict("Novo Milosevo", cityId, cordXd1, cordYd1);
        if (districtIdOne > 0) {
            System.out.println("District One inserted successfully.");
        } else {
            System.out.println("District One insertion failed.");
            return;
        }

        int cordXd2 = 2;
        int cordYd2 = 10;
        int districtIdTwo = districtOperations.insertDistrict("Vojinovica", cityId, cordXd2, cordYd2);
        if (districtIdTwo > 0) {
            System.out.println("District Two inserted successfully.");
        } else {
            System.out.println("District Two insertion failed.");
            return;
        }

        int type1 = 0;
        BigDecimal weight1 = new BigDecimal(123);
        int packageId1 = packageOperations.insertPackage(districtIdOne, districtIdTwo, courierUsername, type1, weight1);
        if (packageId1 > 0) {
            System.out.println("Package One inserted successfully.");
        } else {
            System.out.println("Package One insertion failed.");
            return;
        }

        BigDecimal packageOnePrice = Util.getPackagePriceForPacket(type1, weight1, Util.euclideanDist(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        int offerId = packageOperations.insertTransportOffer(courierUsername, packageId1, new BigDecimal(5));
        if (offerId > 0) {
            System.out.println("Offer for Package One inserted successfully.");
        } else {
            System.out.println("Offer for Package One insertion failed.");
            return;
        }

        boolean offerAccepted = packageOperations.acceptAnOffer(offerId);
        if (offerAccepted) {
            System.out.println("Offer for Package One accepted successfully.");
        } else {
            System.out.println("Offer for Package One acceptance failed.");
            return;
        }

        int type2 = 1;
        BigDecimal weight2 = new BigDecimal(321);
        int packageId2 = packageOperations.insertPackage(districtIdTwo, districtIdOne, courierUsername, type2, weight2);
        if (packageId2 > 0) {
            System.out.println("Package Two inserted successfully.");
        } else {
            System.out.println("Package Two insertion failed.");
            return;
        }

        BigDecimal packageTwoPrice = Util.getPackagePriceForPacket(type2, weight2, Util.euclideanDist(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = packageOperations.insertTransportOffer(courierUsername, packageId2, new BigDecimal(5));
        if (offerId > 0) {
            System.out.println("Offer for Package Two inserted successfully.");
        } else {
            System.out.println("Offer for Package Two insertion failed.");
            return;
        }

        offerAccepted = packageOperations.acceptAnOffer(offerId);
        if (offerAccepted) {
            System.out.println("Offer for Package Two accepted successfully.");
        } else {
            System.out.println("Offer for Package Two acceptance failed.");
            return;
        }

        int type3 = 1;
        BigDecimal weight3 = new BigDecimal(222);
        int packageId3 = packageOperations.insertPackage(districtIdTwo, districtIdOne, courierUsername, type3, weight3);
        if (packageId3 > 0) {
            System.out.println("Package Three inserted successfully.");
        } else {
            System.out.println("Package Three insertion failed.");
            return;
        }

        BigDecimal packageThreePrice = Util.getPackagePriceForPacket(type3, weight3, Util.euclideanDist(cordXd1, cordYd1, cordXd2, cordYd2), new BigDecimal(5));
        offerId = packageOperations.insertTransportOffer(courierUsername, packageId3, new BigDecimal(5));
        if (offerId > 0) {
            System.out.println("Offer for Package Three inserted successfully.");
        } else {
            System.out.println("Offer for Package Three insertion failed.");
            return;
        }

        offerAccepted = packageOperations.acceptAnOffer(offerId);
        if (offerAccepted) {
            System.out.println("Offer for Package Three accepted successfully.");
        } else {
            System.out.println("Offer for Package Three acceptance failed.");
            return;
        }

        Integer status1 = packageOperations.getDeliveryStatus(packageId1);
        if (status1 != null && status1 == 1) {
            System.out.println("Correct initial delivery status for Package One.");
        } else {
            System.out.println("Incorrect initial delivery status for Package One.");
            return;
        }

        Integer drivenPackageId1 = packageOperations.driveNextPackage(courierUsername);
        if (packageId1 == drivenPackageId1) {
            System.out.println("Correct package driven first.");
        } else {
            System.out.println("Incorrect package driven first.");
            return;
        }

        status1 = packageOperations.getDeliveryStatus(packageId1);
        if (status1 != null && status1 == 3) {
            System.out.println("Correct delivery status after driving for Package One.");
        } else {
            System.out.println("Incorrect delivery status after driving for Package One.");
            return;
        }

        Integer status2 = packageOperations.getDeliveryStatus(packageId2);
        if (status2 != null && status2 == 2) {
            System.out.println("Correct initial delivery status for Package Two.");
        } else {
            System.out.println("Incorrect initial delivery status for Package Two.");
            return;
        }

        Integer drivenPackageId2 = packageOperations.driveNextPackage(courierUsername);
        if (packageId2 == drivenPackageId2) {
            System.out.println("Correct package driven second.");
        } else {
            System.out.println("Incorrect package driven second.");
            return;
        }

        status2 = packageOperations.getDeliveryStatus(packageId2);
        if (status2 != null && status2 == 3) {
            System.out.println("Correct delivery status after driving for Package Two.");
        } else {
            System.out.println("Incorrect delivery status after driving for Package Two.");
            return;
        }

        Integer status3 = packageOperations.getDeliveryStatus(packageId3);
        if (status3 != null && status3 == 2) {
            System.out.println("Correct initial delivery status for Package Three.");
        } else {
            System.out.println("Incorrect initial delivery status for Package Three.");
            return;
        }

        Integer drivenPackageId3 = packageOperations.driveNextPackage(courierUsername);
        if (packageId3 == drivenPackageId3) {
            System.out.println("Correct package driven third.");
        } else {
            System.out.println("Incorrect package driven third.");
            return;
        }

        status3 = packageOperations.getDeliveryStatus(packageId3);
        if (status3 != null && status3 == 3) {
            System.out.println("Correct delivery status after driving for Package Three.");
        } else {
            System.out.println("Incorrect delivery status after driving for Package Three.");
            return;
        }

        BigDecimal gain = packageOnePrice.add(packageTwoPrice).add(packageThreePrice);
        System.out.println("gain:"+gain);
      
        BigDecimal loss = (new BigDecimal(Util.euclideanDist(cordXd1, cordYd1, cordXd2, cordYd2) * 4.0D * 15.0D)).multiply(fuelConsumption);
        BigDecimal actual = courierOperations.getAverageCourierProfit(0);
        System.out.println("gain:"+gain);
        System.out.println("loss:"+loss);
        System.out.println("Actual:"+actual);
        if (gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(1.001D))) < 0 &&
                gain.subtract(loss).compareTo(actual.multiply(new BigDecimal(0.999D))) > 0) {
            System.out.println("Average courier profit is within acceptable range.");
        } else {
            System.out.println("Average courier profit is not within acceptable range.");
        }
        }
}
