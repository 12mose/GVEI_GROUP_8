// ExchangeOfferDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExchangeOfferDAO {

    public boolean createExchangeOffer(ExchangeOffer offer) {
        String sql = "INSERT INTO exchange_offers (vehicle_id, exchange_value, subsidy_percent, status) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, offer.getVehicleId());
            pstmt.setDouble(2, offer.getExchangeValue());
            pstmt.setDouble(3, offer.getSubsidyPercent());
            pstmt.setString(4, offer.getStatus());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<ExchangeOffer> getOffersByVehicle(int vehicleId) {
        List<ExchangeOffer> offers = new ArrayList<>();
        String sql = "SELECT * FROM exchange_offers WHERE vehicle_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                offers.add(new ExchangeOffer(
                        rs.getInt("offer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDouble("exchange_value"),
                        rs.getDouble("subsidy_percent"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    public List<ExchangeOffer> getAllOffers() {
        List<ExchangeOffer> offers = new ArrayList<>();
        String sql = "SELECT eo.*, v.plate_no, u.name as owner_name, v.vehicle_type " +
                "FROM exchange_offers eo " +
                "JOIN vehicles v ON eo.vehicle_id = v.vehicle_id " +
                "JOIN users u ON v.owner_id = u.user_id";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                offers.add(new ExchangeOffer(
                        rs.getInt("offer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getDouble("exchange_value"),
                        rs.getDouble("subsidy_percent"),
                        rs.getString("status"),
                        rs.getTimestamp("created_date")
                ));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return offers;
    }

    public boolean updateOfferStatus(int offerId, String status) {
        String sql = "UPDATE exchange_offers SET status = ? WHERE offer_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, status);
            pstmt.setInt(2, offerId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Statistics methods
    public int getTotalExchangedVehicles() {
        String sql = "SELECT COUNT(*) FROM exchange_offers WHERE status = 'approved'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getTotalSubsidies() {
        String sql = "SELECT SUM(exchange_value * subsidy_percent / 100) FROM exchange_offers WHERE status = 'approved'";

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getDouble(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public double getEstimatedCarbonReduction() {
        // Estimate: Each vehicle exchange reduces ~2 tons of CO2 per year
        return getTotalExchangedVehicles() * 2.0;
    }
}