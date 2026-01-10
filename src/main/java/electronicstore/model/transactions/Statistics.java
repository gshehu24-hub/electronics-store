package electronicstore.model.transactions;

import java.io.Serializable;
import java.time.LocalDate;

public class Statistics implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;
    private int totalBills;
    private int totalItemsSold;
    private double totalRevenue;
    private double averageBillAmount;
    private String cashierUsername;

    public Statistics(LocalDate startDate, LocalDate endDate, String cashierUsername) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.cashierUsername = cashierUsername;
        this.totalBills = 0;
        this.totalItemsSold = 0;
        this.totalRevenue = 0.0;
        this.averageBillAmount = 0.0;
    }

    public double calculateAverageBillAmount() {
        if (totalBills > 0) {
            averageBillAmount = totalRevenue / totalBills;
        }
        return averageBillAmount;
    }

    public String generateReport() {
        return "Statistics for " + cashierUsername + " from " + startDate + " to " + endDate +
               "\nTotal Bills: " + totalBills +
               "\nTotal Items Sold: " + totalItemsSold +
               "\nTotal Revenue: $" + String.format("%.2f", totalRevenue) +
               "\nAverage Bill Amount: $" + String.format("%.2f", calculateAverageBillAmount());
    }

    
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public int getTotalBills() { return totalBills; }
    public void setTotalBills(int totalBills) { this.totalBills = totalBills; }

    public int getTotalItemsSold() { return totalItemsSold; }
    public void setTotalItemsSold(int totalItemsSold) { this.totalItemsSold = totalItemsSold; }

    public double getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(double totalRevenue) { this.totalRevenue = totalRevenue; }

    public double getAverageBillAmount() { return averageBillAmount; }
    public void setAverageBillAmount(double averageBillAmount) { this.averageBillAmount = averageBillAmount; }

    public String getCashierUsername() { return cashierUsername; }
    public void setCashierUsername(String cashierUsername) { this.cashierUsername = cashierUsername; }
}