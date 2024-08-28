import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.w3c.dom.Text;

class Vehicle {
    int vehicle_id;
    String vehicle_numberplate;
    String vehicle_type;
    double farePerKm;
    // String vehicle_model;

    Vehicle(int vehicle_id, String vehicle_numberplate, String vehicle_type, double farePerKm) {
        this.vehicle_id = vehicle_id;
        this.vehicle_numberplate = vehicle_numberplate;
        this.vehicle_type = vehicle_type;
        this.farePerKm = farePerKm;
    }

    Vehicle() {

    }

    public int getVehicle_id() {
        return vehicle_id;
    }
}

class Customer {
    int customer_id;
    String first_name;
    String last_name;
    String phone_no;
    String passward;

    Customer(int customer_id, String first_name, String last_name, String phone_no, String passward) {
        this.customer_id = customer_id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.phone_no = phone_no;
        this.passward = passward;
    }

    Customer() {

    }

    public String getPassward() {
        return passward;
    }

    public int getCustomer_id() {
        return customer_id;
    }

}

class Booking {
    int booking_id;
    double totalFare;
    String customer_firstname;
    String customer_lastname;
    Date dateofbooking;

    Booking(double totalFare, String customer_firstname, String customer_lastname, Date dateofbooking) {
        // this.booking_id = booking_id;
        this.totalFare = totalFare;
        this.customer_firstname = customer_firstname;
        this.customer_lastname = customer_lastname;
        this.dateofbooking = dateofbooking;
    }

}

class Driver {
    int driver_id;
    String Driver_firstname;
    String Drive_lastname;
    int driver_age;

    Driver(String driver_firstname, String drive_lastname, int driver_age) {
        this.driver_id = driver_id;
        Driver_firstname = driver_firstname;
        Drive_lastname = drive_lastname;
        this.driver_age = driver_age;
    }

    public int getdriver_id() {
        return driver_id;
    }

}

class cab_managment {
    class MyException extends Exception {
        MyException(String message) {
            super(message);
        }
    }

    static Connection con;
    static Scanner sc;

    static ArrayList<Booking> booking = new ArrayList<>();

    // Method to Add vehicle(for admin)

    void addVehicle() throws Exception {

        sc = new Scanner(System.in);

        System.out.println("Enter Number Plate");
        String number_plate = sc.nextLine();
        System.out.println("Enter vehicle type");
        String vehicle_type = sc.nextLine();
        System.out.println("Enter Price per KM");
        double price = sc.nextDouble();
        String sql = "insert into vehicles(vehicle_numberplate,vehicle_type,farePerKm) values(?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setString(1, number_plate);
        pst.setString(2, vehicle_type);
        pst.setDouble(3, price);
        pst.executeUpdate();
        System.out.println("-------------------------------");
        System.out.println("Vehicle Added Successfully");
        System.out.println("----------------------------------");

    }

    // Method to display all bookings

    void displayAllBookings() throws SQLException {
        String sql = "select * from booking";
        PreparedStatement pst = con.prepareCall(sql);
        ResultSet rst = pst.executeQuery();
        if (rst != null) {
            System.out.println("All bookings");
            while (rst.next()) {
                System.out.println("booking id=  " + rst.getInt(1) + " total fare = " + rst.getString(2)
                        + " customer firstname  =" + rst.getString(3) + " customer last name = " + rst.getString(4)
                        + " date of booking  =" + rst.getDate(5));
            }
        } else {
            System.out.println("no booking found");
        }
    }

    // Method to Display all vehicles(for admin)

    static void showallvehicles() throws SQLException {
        String sql = "select * from vehicles";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            System.out.print("\n" + "Vehicle ID: " + rst.getInt("vehicle_id") + "\n" + "Number plate:"
                    + rst.getString("vehicle_numberplate") + "\n" + "vehicle Type " + rst.getString("vehicle_type")
                    + "\n"
                    + "Fare Per KM" + rst.getDouble("fareperkm"));
            System.out.println();
        }
    }

    // Method to Add Driver (for admin)

    void add_driver() throws Exception {
        System.out.println("Enter Firstname of the Driver");
        sc.nextLine();
        String fname = sc.nextLine(); // Use the sc Scanner object
        System.out.println("Enter Lastname of the Driver");
        String lname = sc.nextLine(); // Use the sc Scanner object
        System.out.println("Enter age of the Driver");
        int age = sc.nextInt(); // Use the sc Scanner object
        String sql = "insert into driver(Driver_firstname,Drive_lastname,driver_age) values(?,?,?)";
        PreparedStatement pst = con.prepareStatement(sql);

        pst.setString(1, fname);
        pst.setString(2, lname);
        pst.setInt(3, age);
        pst.executeUpdate();
        System.out.println();
        System.out.println("--------------------------------------");
        System.out.println("Driver Added Successfully");
        System.out.println("--------------------------------------");
    }

    // Method to Display all driver(for admin)

    void showAllDrivers() throws SQLException {
        String sql = "select * from driver";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            System.out.println(
                    "Driver ID:" + rst.getInt("driver_id") + "\n" + "\tFirst Name:" + rst.getString("Driver_firstname:")
                            + "\tLast Name:" + rst.getString("Driver_lastname") + "\n" + "\tAge: " +
                            rst.getInt("age"));
        }
    }

    // Method to book cab (for customers)

    void bookCab(int customerId) throws SQLException {
        Customer customer = new Customer();
        Vehicle vehicle = new Vehicle();
        // Find the customer with the given ID
        String sql = "select * from customer";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            if (customerId == rst.getInt("customer_id"))

            {
                customer = new Customer(rst.getInt("customer_id"), rst.getString("first_name"),
                        rst.getString("last_name"), rst.getString("phone_no"), rst.getString("passward"));
                break;
            }
        }

        if (customer != null) {
            // Display available vehicles for selection
            String sql1 = "select * from vehicles";
            PreparedStatement pst1 = con.prepareStatement(sql1);
            ResultSet rst1 = pst1.executeQuery();
            while (rst1.next()) {
                vehicle = new Vehicle(rst1.getInt("vehicle_id"), rst1.getString("vehicle_numberplate"),
                        rst1.getString("vehicle_type"), rst1.getDouble("farePerKm"));
            }

            // Find the selected vehicle with the given ID
            Date date_of_booking = new Date(System.currentTimeMillis());
            if (vehicle != null) {
                // Calculate fare based on distance

                System.out.println("Enter Distance in KM:");
                double distance = sc.nextDouble();
                double totalFare = distance * vehicle.farePerKm;

                // Create a booking

                Booking newBooking = new Booking(totalFare, customer.first_name, customer.last_name,
                        date_of_booking);

                // Add the booking to the list
                booking.add(newBooking);

                System.out.println("Booking Successful!");
                // System.out.println("Booking ID: " + bookingId);
                System.out.println("Total Fare: $" + totalFare);

                // Remove the booking_id parameter from the SQL query
                String sql3 = "INSERT INTO booking (totalFare, customer_firstname, customer_lastname, dateofbooking) VALUES (?, ?, ?, ?)";
                PreparedStatement pst3 = con.prepareStatement(sql3);
                pst3.setDouble(1, totalFare);
                pst3.setString(2, customer.first_name);
                pst3.setString(3, customer.last_name);
                pst3.setDate(4, date_of_booking);
                pst3.executeUpdate();

            } else {
                System.out.println("Invalid Vehicle ID. Booking Failed.");
            }
        } else {
            System.out.println("Customer does not exist. Booking Failed.");
        }
    }

    // method to cancle booking

    void cancelBooking() throws SQLException {
        System.out.println("Enter Booking ID to cancel:");
        int bookingId = sc.nextInt();

        boolean bookingExists = false;
        String sql = "select * from booking where booking_id=" + bookingId;
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();
        while (rs.next()) {
            if (rs.getInt(1) == bookingId) {
                String sql1 = "delete from booking where booking_id=" + bookingId;
                PreparedStatement pst1 = con.prepareStatement(sql1);
                pst1.executeUpdate();
                bookingExists = true;
                System.out.println("Cab Cancel successfully!");
                break;
            }
        }

        if (!bookingExists) {
            System.out.println("Invalid Booking ID ");
        }
    }

    /*
     * void displayCustomerBookings(String fname, String lname) throws SQLException
     * {
     * String sql = "select * from booking where customer_firstname='" + fname +
     * "'";
     * PreparedStatement pst = con.prepareStatement(sql);
     * ResultSet rs = pst.executeQuery();
     * while (rs.next()) {
     * System.out.println("hello");
     * }
     * 
     * }
     */

    // method to get a bill(for customer)

    void getBill(int booking_id) throws Exception {

        BufferedWriter bw1 = new BufferedWriter(new FileWriter("H:\\bill2.txt"));
        String sql = "select * from booking where booking_id= " + booking_id;
        PreparedStatement psmt = con.prepareStatement(sql);
        ResultSet rs = psmt.executeQuery();
        while (rs.next()) {
            bw1.write(
                    "====================\n" + "\n" + "First Name: " + rs.getString("customer_firstname")
                            + "\n" + "Last Name: " + rs.getString("customer_lastname") + "\n" + "total fare: "
                            + rs.getDouble("totalFare") + "\n" + "Date of booking: " + rs.getDate("dateofbooking")
                            + "\n" + "\n"
                            + "Thanks for visiting!!\n" + "\n"
                            + "====================");
        }
        bw1.flush();
        bw1.close();

    }

    // Method to Update Customer Profile
    void updateCustomerProfile(int customerId) throws SQLException {
        sc.nextLine(); // Consume the newline character
        String newFirstName, newLastName, newPhoneNumber, newPassword;

        // Retrieve the customer's current information
        String sql = "SELECT * FROM customer WHERE customer_id = ?";
        PreparedStatement pst = con.prepareStatement(sql);
        pst.setInt(1, customerId);
        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Display current customer information
            System.out.println("---------------------------------------------");
            System.out.println();
            System.out.println("Current Customer Information:");
            System.out.println("First Name: " + rs.getString("first_name"));
            System.out.println("Last Name: " + rs.getString("last_name"));
            System.out.println("Phone Number: " + rs.getString("phone_no"));
            System.out.println();
            System.out.println("---------------------------------------------");
            System.out.println();

            // Prompt the user for updated information
            System.out.println("Enter New First Name (or press Enter to keep it unchanged):");
            newFirstName = sc.nextLine();

            System.out.println("Enter New Last Name (or press Enter to keep it unchanged):");
            newLastName = sc.nextLine();

            System.out.println("Enter New Phone Number (or press Enter to keep it unchanged):");
            newPhoneNumber = sc.nextLine();

            System.out.println("Enter New Password (or press Enter to keep it unchanged):");
            newPassword = sc.nextLine();

            // Update the customer's information in the database
            String updateSql = "UPDATE customer SET first_name = ?, last_name = ?, phone_no = ?, passward = ? WHERE customer_id = ?";
            PreparedStatement updatePst = con.prepareStatement(updateSql);
            updatePst.setString(1, (newFirstName.isEmpty() ? rs.getString("first_name") : newFirstName));
            updatePst.setString(2, (newLastName.isEmpty() ? rs.getString("last_name") : newLastName));
            updatePst.setString(3, (newPhoneNumber.isEmpty() ? rs.getString("phone_no") : newPhoneNumber));
            updatePst.setString(4, (newPassword.isEmpty() ? rs.getString("passward") : newPassword));
            updatePst.setInt(5, customerId);

            int rowsUpdated = updatePst.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Customer profile updated successfully.");
            } else {
                System.out.println("Failed to update customer profile.");
            }
        } else {
            System.out.println("Customer not found.");
        }
    }

    // Method to Remove a Vehicle (for admin)

    void removeVehicle(int vehicleId) throws SQLException {
        String sql = "delete from vehicles where vehicle_id=" + vehicleId;
        PreparedStatement pst = con.prepareStatement(sql);
        int i = pst.executeUpdate();
        System.out.println();
        if (i != -1) {
            System.out.println("----------------------------------------------");
            System.out.println("Vehicle Removed Successfully");
            System.out.println("----------------------------------------------");
        } else {
            System.out.println("------------------------------------------");
            System.out.println("Id not found");
            System.out.println("------------------------------------------");
        }

    }

    // Method to Remove a Driver (for admin)

    void removeDriver(int driverId) throws SQLException {
        String sql = "delete from driver where driver_id=" + driverId;
        PreparedStatement pst = con.prepareStatement(sql);
        int i = pst.executeUpdate();
        System.out.println();
        if (i != -1) {
            System.out.println("-------------------------------");
            System.out.println("Driver Removed Successfully");
            System.out.println("----------------------------------");
        } else {
            System.out.println("------------------------------------------");
            System.out.println("Id not found");
            System.out.println("------------------------------------------");
        }
    }

    // Method to Display All Customer Details (for admin)

    void displayAllCustomers() throws SQLException {
        String sql = "select * from customer";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            System.out.println("\tCustomer_id:" + rst.getInt("customer_id") + "\n" + "\tCustomer_firstname:"
                    + rst.getString("first_name") + "\n" + "\tCustomer_lastname:" + rst.getString("last_name") + "\n"
                    + "\tphone number:" + rst.getString("phone_no"));
        }
    }

    // Method to Display Total Earnings (for admin)

    void displayTotalEarnings() throws SQLException {
        double totalEarnings = 0;
        String sql = "select totalfare from booking";
        PreparedStatement pst = con.prepareStatement(sql);
        ResultSet rst = pst.executeQuery();
        while (rst.next()) {
            totalEarnings = totalEarnings + rst.getDouble("totalfare");

        }
        System.out.println("Total fare in $" + totalEarnings);
    }
    // Customer menu

    void customerMenu(int customerId) throws Exception {
        int customerChoice;
        do {
            System.out.println("                                ===============================");
            System.out.println();
            System.out.println("                                    Customer Menu:");
            System.out.println("                                    1) Book a Cab");
            System.out.println("                                    2) Cancel Booking");
            // System.out.println(" 3) View Your Bookings");
            System.out.println("                                    3) Update Profile");
            System.out.println("                                    4) Get Bill");
            System.out.println("                                    5)Exit");
            System.out.println();
            System.out.println("                                ===============================");
            sc = new Scanner(System.in);
            System.out.println("Enter Your choice");
            customerChoice = sc.nextInt();
            switch (customerChoice) {
                case 1:
                    bookCab(customerId);
                    break;
                case 2:
                    cancelBooking();
                    break;
                // case 3:
                /*
                 * System.out.println("Enter fname");
                 * String fname = sc.nextLine();
                 * sc.nextLine();
                 * System.out.println("Enter lname");
                 * String lname = sc.nextLine();
                 * displayCustomerBookings(fname, lname);
                 * break;
                 */
                case 3:
                    updateCustomerProfile(customerId);
                    break;
                case 4:
                    System.out.println("Enter booking id");
                    int bookingid = sc.nextInt();
                    getBill(bookingid);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice.");
            }
        } while (customerChoice != 5);
    }

    public static void main(String[] args) throws Exception {

        // Connection code

        String dburl = "jdbc:mysql://localhost:3306/cab";
        String dbuser = "root";
        String dbpass = "";
        String driver = "com.mysql.cj.jdbc.Driver";
        Class.forName(driver);
        con = DriverManager.getConnection(dburl, dbuser, dbpass);
        if (con != null) {
            System.out.println("Connection Successfull");
            System.out.println();
        } else {
            System.out.println("Connection Failed");
        }

        sc = new Scanner(System.in);
        System.out.println("========================");
        System.out.println();
        System.out.println("Enter who you are Admin customer");
        System.out.println();
        System.out.println("1) admin    ");
        System.out.println("2) customer ");
        System.out.println();
        int choice = sc.nextInt();
        String pass = "123"; // This is the password for the admin

        if (choice == 1) {
            sc.nextLine(); // Consume the newline character

            System.out.println("Enter Password");
            String password = sc.nextLine(); // Read the password

            if (password.equals(pass)) { // Use .equals() to compare strings
                cab_managment admin = new cab_managment();
                int choice1;

                do {
                    System.out.println("                       ====================================");
                    System.out.println();

                    System.out.println("                               1) Add Vehicle");
                    System.out.println("                               2) Remove Vehicle");
                    System.out.println("                               3) Add Driver");
                    System.out.println("                               4) Remove Driver");
                    System.out.println("                               5) Display All Customers");
                    System.out.println("                               6) Display All Booking");
                    System.out.println("                               7) Display Total Earnings");
                    System.out.println("                               8) Display all vehicles");
                    System.out.println("                               9) Exit");
                    System.out.println();
                    System.out.println("                       ====================================");
                    System.out.println("Enter your choice:");
                    choice1 = sc.nextInt();

                    switch (choice1) {
                        case 1:
                            admin.addVehicle();
                            break;
                        case 2:
                            System.out.println("Enter Vehicle ID to remove:");
                            int removeVehicleId = sc.nextInt();
                            admin.removeVehicle(removeVehicleId);
                            break;
                        case 3:
                            admin.add_driver();
                            break;
                        case 4:
                            System.out.println("Enter Driver ID to remove:");
                            int removeDriverId = sc.nextInt();
                            admin.removeDriver(removeDriverId);
                            break;
                        case 5:
                            admin.displayAllCustomers();
                            break;
                        case 6:

                        case 7:
                            admin.displayTotalEarnings();
                            break;
                        case 8:
                            admin.showallvehicles();
                            break;
                        case 9:
                            System.out.println("Exiting...");
                            break;
                        default:
                            System.out.println("Invalid choice.");
                            break;
                    }
                } while (choice1 != 8);
            } else {
                System.out.println("Invalid password");
            }
        } else if (choice == 2) {
            cab_managment obj1 = new cab_managment();
            System.out.println("============================");
            System.out.println("Enter Your Choice\n1)login\n2)Sign-up");
            System.out.println();
            int choice1 = sc.nextInt();
            if (choice1 == 1) {

                // login code
                int n = 0;
                System.out.println("Enter Customer id");
                int cust_id = sc.nextInt();
                sc.nextLine();
                String sql = "select * from customer where customer_id=" + cust_id;
                Statement pst = con.createStatement();
                ResultSet rs = pst.executeQuery(sql);
                if (rs != null) {

                    while (rs.next()) {
                        do {
                            System.out.println("Enter password");
                            String pass1 = sc.nextLine();

                            if (pass1.equals(rs.getString("passward"))) {
                                System.out.println("log in successful");
                                obj1.customerMenu(cust_id);
                                break;
                            } else {
                                n++;
                                System.out.println("Wrong password");
                                System.out.println();
                            }
                        } while (n != 3);
                    }

                    if (n == 3) {
                        System.out.println("you have exceeded a limit");
                    }

                }

            } else if (choice1 == 2) {

                // sign up code
                System.out.println("Enter First Name:");
                String fname = sc.next();

                System.out.println("Enter Last Name:");
                String lname = sc.next();

                System.out.println("Enter Phone number:");
                String ph_no = sc.next();

                System.out.println("Create Password:");
                String password1 = sc.next();

                System.out.println("Re-enter password:");
                String password2 = sc.next();

                if (password1.equals(password2)) {
                    // Corrected SQL query to insert data into the customer table
                    String sql = "insert into customer(first_name, last_name, phone_no, passward) values (?, ?, ?, ?)";
                    PreparedStatement ps = con.prepareStatement(sql);
                    ps.setString(1, fname);
                    ps.setString(2, lname);
                    ps.setString(3, ph_no);
                    ps.setString(4, password1);
                    ps.executeUpdate();
                    System.out.println("Signed up successfully");
                    int custid = 0;
                    String sql1 = "select customer_id from customer where phone_no='" + ph_no + "'";
                    PreparedStatement pst = con.prepareStatement(sql1);
                    ResultSet rst = pst.executeQuery();
                    while (rst.next()) {
                        custid = rst.getInt("customer_id");
                    }
                    obj1.customerMenu(custid);
                } else {
                    System.out.println("Passwords do not match");
                }
            }
        }
    }
}
