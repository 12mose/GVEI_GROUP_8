// ExchangeOffer.java
import java.sql.Timestamp;

public class ExchangeOffer {
    private int offerId;
    private int vehicleId;
    private double exchangeValue;
    private double subsidyPercent;
    private String status;
    private Timestamp createdDate;

    public ExchangeOffer() {}

    public ExchangeOffer(int offerId, int vehicleId, double exchangeValue,
                         double subsidyPercent, String status, Timestamp createdDate) {
        this.offerId = offerId;
        this.vehicleId = vehicleId;
        this.exchangeValue = exchangeValue;
        this.subsidyPercent = subsidyPercent;
        this.status = status;
        this.createdDate = createdDate;
    }

    // Getters and setters
    public int getOfferId() { return offerId; }
    public void setOfferId(int offerId) { this.offerId = offerId; }

    public int getVehicleId() { return vehicleId; }
    public void setVehicleId(int vehicleId) { this.vehicleId = vehicleId; }

    public double getExchangeValue() { return exchangeValue; }
    public void setExchangeValue(double exchangeValue) { this.exchangeValue = exchangeValue; }

    public double getSubsidyPercent() { return subsidyPercent; }
    public void setSubsidyPercent(double subsidyPercent) { this.subsidyPercent = subsidyPercent; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getCreatedDate() { return createdDate; }
    public void setCreatedDate(Timestamp createdDate) { this.createdDate = createdDate; }
}