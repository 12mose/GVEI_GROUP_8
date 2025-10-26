// VehicleDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleDAO {

    public boolean registerVehicle(Vehicle vehicle) {
        String sql = "INSERT INTO vehicles (owner_id, plate_no, vehicle_type, fuel_type, manufacture_year, mileage, condition_rating) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicle.getOwnerId());
            pstmt.setString(2, vehicle.getPlateNo());
            pstmt.setString(3, vehicle.getVehicleType());
            pstmt.setString(4, vehicle.getFuelType());
            pstmt.setInt(5, vehicle.getManufactureYear());
            pstmt.setInt(6, vehicle.getMileage());
            pstmt.setInt(7, vehicle.getConditionRating());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehicle> getVehiclesByOwner(int ownerId) {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT * FROM vehicles WHERE owner_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, ownerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                vehicles.add(new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getInt("owner_id"),
                        rs.getString("plate_no"),
                        rs.getString("vehicle_type"),
                        rs.getString("fuel_type"),
                        rs.getInt("manufacture_year"),
                        rs.getInt("mileage"),
                        rs.getInt("condition_rating")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public List<Vehicle> getAllVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        String sql = "SELECT v.*, u.name as owner_name FROM vehicles v JOIN users u ON v.owner_id = u.user_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getInt("owner_id"),
                        rs.getString("plate_no"),
                        rs.getString("vehicle_type"),
                        rs.getString("fuel_type"),
                        rs.getInt("manufacture_year"),
                        rs.getInt("mileage"),
                        rs.getInt("condition_rating")
                );
                vehicles.add(vehicle);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public boolean isVehicleEligible(Vehicle vehicle) {
        int currentYear = java.time.Year.now().getValue();
        int vehicleAge = currentYear - vehicle.getManufactureYear();

        return vehicleAge > 5 &&
                (vehicle.getFuelType().equalsIgnoreCase("Petrol") ||
                        vehicle.getFuelType().equalsIgnoreCase("Diesel"));
    }

    public double calculateExchangeValue(Vehicle vehicle) {
        int currentYear = java.time.Year.now().getValue();
        int vehicleAge = currentYear - vehicle.getManufactureYear();
        double baseValue = 5000000; // 5 million RWF base value

        // Adjust value based on age and condition
        double ageFactor = Math.max(0.1, 1.0 - (vehicleAge - 5) * 0.1);
        double conditionFactor = vehicle.getConditionRating() / 10.0;
        double mileageFactor = Math.max(0.3, 1.0 - (vehicle.getMileage() / 200000.0));

        return baseValue * ageFactor * conditionFactor * mileageFactor;
    }
}