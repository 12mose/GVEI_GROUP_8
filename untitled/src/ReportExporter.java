// ReportExporter.java
import java.io.*;
import java.util.List;

public class ReportExporter {

    public static boolean exportToCSV(List<ExchangeOffer> offers, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            // Write header
            writer.println("Offer ID,Vehicle ID,Exchange Value,Subsidy %,Status,Created Date");

            // Write data
            for (ExchangeOffer offer : offers) {
                writer.printf("%d,%d,%.2f,%.2f,%s,%s%n",
                        offer.getOfferId(),
                        offer.getVehicleId(),
                        offer.getExchangeValue(),
                        offer.getSubsidyPercent(),
                        offer.getStatus(),
                        offer.getCreatedDate());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean exportToTXT(List<ExchangeOffer> offers, String filename) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
            writer.println("GVEI Exchange Offers Report");
            writer.println("Generated on: " + java.time.LocalDateTime.now());
            writer.println("=============================================");

            for (ExchangeOffer offer : offers) {
                writer.printf("Offer ID: %d%n", offer.getOfferId());
                writer.printf("Vehicle ID: %d%n", offer.getVehicleId());
                writer.printf("Exchange Value: RWF %,.2f%n", offer.getExchangeValue());
                writer.printf("Subsidy: %.2f%%%n", offer.getSubsidyPercent());
                writer.printf("Status: %s%n", offer.getStatus());
                writer.printf("Created: %s%n%n", offer.getCreatedDate());
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}