package com.barclayadunn.json;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * Data returned from this endpoint
 * https://commerces-lb.stage.renttherunway.it:8888/flis/user/{user_id}/flis
 * returns Financial Line Items (FLIs) for all orders for a given user.
 * Also view by executing `select * from financial_line_item where user_id = {user_id};` in the db
 * We were seeing really weird results for Tyler Nicoll - 89657552 - showing her having a negative
 * lifetime value to the company ... uncovered a data bug where a backfill had attributed all refunds
 * to the CX agent who did the refund, instead of to the customer. Dave Filion will be fixing.
 */
public class ParseFLIJsonFile extends AbstractParseJsonWithGson {
    private static final String jsonString = "[{ \"createdAt\": \"2017-02-15T15:39:01.000-05:00\", \"direction\": \"IN\", \"groupId\": 10066060, \"id\": 7406456, \"invoiceId\": 10404268, \"orderId\": 9514482, \"status\": \"CONFIRMED\", \"subtotal\": \"147.95\", \"tax\": \"13.13\", \"taxRate\": 0.0888, \"total\": \"161.08\", \"transactionAt\": null, \"type\": \"CREDIT_CARD\", \"updatedAt\": \"2017-02-15T15:39:01.000-05:00\", \"userId\": 13432204, \"history\": [ { \"createdAt\": \"2017-02-15T09:39:00.000-05:00\", \"fliId\": 7406456, \"id\": 8958004, \"toStatus\": \"CONFIRMED\", \"username\": \"USER\" } ] } ]";

    public static void main(String[] args) throws Exception {
//        String json = jsonString;
//        String json = stringOfFile("/Users/bdunn/Dropbox/Development/TestCode/misc/response_8957552.json"); // tyler
//        String json = stringOfFile("/Users/bdunn/Dropbox/Development/TestCode/misc/response_1429731.json"); // barc
        String json = stringOfFile("/Users/bdunn/Dropbox/Development/TestCode/misc/response_13432204.json");  // stephanie
        // Now do the magic.
        Type type = new TypeToken<List<Fli>>() {
        }.getType();
        List<Fli> flis = new Gson().fromJson(json, type);

        // Show it.
        flis.stream()
                .filter(fli -> fli.getStatus().equalsIgnoreCase("CONFIRMED"))
                .sorted(Comparator.comparing(Fli::getCreatedAt))
                .forEach(f -> {
                    if (f.getStatus().equalsIgnoreCase("CONFIRMED")) {
                        System.out.println(f.toString());
                    }
                });

        // get ins
        BigDecimal ins = flis.stream()
                .filter(fli -> fli.getStatus().equalsIgnoreCase("CONFIRMED") && fli.getDirection().equalsIgnoreCase("IN"))
                .sorted(Comparator.comparing(Fli::getCreatedAt))
                .map(Fli::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // get outs
        BigDecimal outs = flis.stream()
                .filter(fli -> fli.getStatus().equalsIgnoreCase("CONFIRMED") && fli.getDirection().equalsIgnoreCase("OUT"))
                .sorted(Comparator.comparing(Fli::getCreatedAt))
                .map(Fli::getTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("Total IN: " + ins + "; OUT: " + outs + "; NET: " + ins.subtract(outs));
    }
}

class Fli {
    private String createdAt;
    private String direction;
    private int groupId;
    private int id;
    private int invoiceId;
    private int orderId;
    private String status;
    private BigDecimal subtotal;
    private BigDecimal tax;
    private BigDecimal taxRate;
    private BigDecimal total;
    private String transactionAt;
    private String type;
    private String updatedAt;
    private int userId;
    private Collection<FliHistory> history;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(int invoiceId) {
        this.invoiceId = invoiceId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getTransactionAt() {
        return transactionAt;
    }

    public void setTransactionAt(String transactionAt) {
        this.transactionAt = transactionAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Collection<FliHistory> getHistory() {
        return history;
    }

    public void setHistory(Collection<FliHistory> history) {
        this.history = history;
    }

    @Override
    public String toString() {
        DateTime createdDateTime = DateTime.parse(createdAt, ISODateTimeFormat.dateTime());
        return MoreObjects.toStringHelper(this)
                .add("createdAt", createdDateTime.toString(ISODateTimeFormat.dateHourMinuteSecond()))
                .add("orderId", orderId)
                .add("groupId", groupId)
                .add("direction", direction)
                .add("type", type)
                .add("status", status)
                .add("total", total)
                .toString();
    }
}

class FliHistory {
    private String createdAt;
    private int fliId;
    private int id;
    private String toStatus;
    private String username;

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getFliId() {
        return fliId;
    }

    public void setFliId(int fliId) {
        this.fliId = fliId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToStatus() {
        return toStatus;
    }

    public void setToStatus(String toStatus) {
        this.toStatus = toStatus;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
