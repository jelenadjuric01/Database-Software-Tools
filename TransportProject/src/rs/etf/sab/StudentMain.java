
package rs.etf.sab;

import rs.etf.sab.student.*;
import rs.etf.sab.tests.*;
import rs.etf.sab.operations.*;

//import rs.etf.sab.tests.TestRunner;


public class StudentMain {
    
    
    public static void main(String[] args) {
        CityOperations cityOperations = new dj200356_CityOperationsImpl(); // Change this to your implementation.
        DistrictOperations districtOperations = new dj200356_DistrictOperationsImpl(); // Do it for all classes.
        CourierOperations courierOperations = new dj200356_CourierOperationsImpl(); // e.g. = new MyDistrictOperations();
        CourierRequestOperation courierRequestOperation = new dj200356_CourierRequestOperationsImpl();
        GeneralOperations generalOperations = new dj200356_GeneralOperationsImpl();
        UserOperations userOperations = new dj200356_UserOperationsImpl();
        VehicleOperations vehicleOperations = new dj200356_VehicleOperationsImpl();
        PackageOperations packageOperations = new dj200356_PackageOperationsImpl();

        TestHandler.createInstance(
                cityOperations,
                courierOperations,
                courierRequestOperation,
                districtOperations,
                generalOperations,
                userOperations,
                vehicleOperations,
                packageOperations);

        TestRunner.runTests();
    }
}
