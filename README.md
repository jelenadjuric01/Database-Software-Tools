# Transport Package Management System

A comprehensive Java-based database application for managing a package delivery service, including couriers, vehicles, packages, and transport operations.

## Project Overview

This system simulates a complete package delivery service with support for:
- User and administrator management
- Courier registration and operations
- Vehicle fleet management
- Package creation, pricing, and delivery tracking
- Transport offer system with competitive bidding
- Automated delivery routing and profit calculation

## Technologies Used

- **Java** - Core application logic
- **JDBC** - Database connectivity
- **Microsoft SQL Server** - Database management system
- **SQL** - Stored procedures and triggers

## Database Schema

The system uses the following main entities:

- **Korisnik (User)** - Base user accounts
- **Administrator** - Admin user accounts
- **Kurir (Courier)** - Delivery personnel
- **Vozilo (Vehicle)** - Fleet vehicles with fuel type and consumption
- **Grad (City)** - Cities in the delivery network
- **Opstina (District)** - Districts within cities with coordinates
- **Paket (Package)** - Delivery packages with pricing and status tracking
- **Ponuda (Offer)** - Transport offers from couriers
- **Vozi (Drive)** - Active delivery routes

## Key Features

### Package Management
- Multiple package types (0, 1, 2) with different pricing models
- Dynamic pricing based on:
  - Package type and weight
  - Euclidean distance between districts
  - Courier bid percentage
- Package status tracking (Created → Accepted → In Transit → Delivered)

### Courier Operations
- Courier request system with vehicle assignment
- Automated delivery routing
- Profit calculation considering:
  - Package delivery fees
  - Fuel consumption and costs
  - Distance traveled
- Status management (Idle/Driving)

### Business Logic
- Competitive bidding system for package delivery
- Automatic route optimization
- Real-time status updates
- Transaction management for data integrity

## Project Structure

```
TransportProject/
├── src/rs/etf/sab/
│   ├── StudentMain.java                    # Application entry point
│   └── student/
│       ├── DB.java                         # Database connection singleton
│       ├── dj200356_CityOperationsImpl.java
│       ├── dj200356_CourierOperationsImpl.java
│       ├── dj200356_CourierRequestOperationsImpl.java
│       ├── dj200356_DistrictOperationsImpl.java
│       ├── dj200356_GeneralOperationsImpl.java
│       ├── dj200356_PackageOperationsImpl.java
│       ├── dj200356_UserOperationsImpl.java
│       ├── dj200356_VehicleOperationsImpl.java
│       ├── dj200356_Par.java               # Pair implementation
│       └── util/
│           └── Util.java                   # Helper functions
├── createBase.sql                          # Database schema
└── aceptOffer.sql                          # Trigger for offer acceptance
```

## Database Configuration

Update the database connection parameters in `DB.java`:

```java
private static final String username = "sa";
private static final String password = "123";
private static final String database = "TransportPaketa";
private static final int port = 1433;
private static final String server = "localhost";
```

## Setup Instructions

1. **Database Setup**
   ```sql
   -- Run createBase.sql to create the database schema
   -- Run aceptOffer.sql to create the trigger
   ```

2. **Configure Connection**
   - Update database credentials in `DB.java`
   - Ensure SQL Server is running and accessible

3. **Run Application**
   ```bash
   # Compile and run StudentMain.java
   java rs.etf.sab.StudentMain
   ```

## Core Operations

### User Operations
- Insert users
- Declare administrators
- Track sent packages
- Delete users

### Courier Operations
- Register courier requests
- Grant courier status
- Track courier profit
- Manage courier status

### Package Operations
- Create packages with automatic pricing
- Submit transport offers
- Accept offers
- Track delivery status
- Execute deliveries with route optimization

### Vehicle Operations
- Register vehicles with fuel specifications
- Update fuel type and consumption
- Track vehicle assignments

## Pricing Algorithm

Package price is calculated as:
```
price = distance × (basePrice + (weightFactor × weight × kiloPricing))
```

Where values depend on package type:
- **Type 0**: Letter (base: 10, no weight factor)
- **Type 1**: Standard (base: 25, weight factor: 1 × 100)
- **Type 2**: Fragile (base: 75, weight factor: 2 × 300)

## Delivery System

The `driveNextPackage()` method implements intelligent routing:
1. Checks for packages currently being delivered
2. Delivers oldest package in route
3. Loads all accepted packages for courier
4. Updates package and courier status
5. Calculates profit including fuel costs
6. Cleans up completed routes

## Testing

The project includes comprehensive test scenarios in `dj200356_PackageOperationsImpl.main()` demonstrating:
- User and courier creation
- Vehicle registration
- Package creation and offer acceptance
- Multi-package delivery routing
- Profit calculation validation

## Author

**Student ID**: dj200356

## License

Academic project for ETF (School of Electrical Engineering), University of Belgrade.
