package electronicstore.model.transactions;

import java.io.Serializable;
import java.time.LocalDate;

public class Report implements Serializable {
    private static final long serialVersionUID = 1L;

    private LocalDate startDate;
    private LocalDate endDate;
    private double totalIncome;
    private double totalCosts;
    private double netProfit;

    public Report(LocalDate startDate, LocalDate endDate, double totalIncome, double totalCosts) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.totalIncome = totalIncome;
        this.totalCosts = totalCosts;
        this.netProfit = totalIncome - totalCosts;
    }

    public String generateReport() {
        return "Financial Report from " + startDate + " to " + endDate +
               "\nTotal Income: $" + String.format("%.2f", totalIncome) +
               "\nTotal Costs: $" + String.format("%.2f", totalCosts) +
               "\nNet Profit: $" + String.format("%.2f", netProfit);
    }

    
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public double getTotalIncome() { return totalIncome; }
    public double getTotalCosts() { return totalCosts; }
    public double getNetProfit() { return netProfit; }
}