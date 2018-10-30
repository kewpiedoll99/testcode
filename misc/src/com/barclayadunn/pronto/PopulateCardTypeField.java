package com.barclayadunn.pronto;

import com.barclayadunn.crypt.Cryptography;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: barclaydunn
 * Date: Feb 1, 2012
 * Time: 4:58:10 PM
 */
public class PopulateCardTypeField {

    // to run this, comment this out and uncomment one of the target db's
    static final String dbURI = "";
//    static final String dbURI = "jdbc:mysql://localhost:3306/mexp";
//    static final String dbURI = "jdbc:mysql://devdbrw01:3306/mexp";
//    static final String dbURI = "jdbc:mysql://qadbrw01:3306/mexp";
//    static final String dbURI = "jdbc:mysql://dbrw101:3306/mexp";
    static final String dbUser = "rc";
    static final String dbPass = "shop";
    static final String dbName = "mexp";

    private static final String MC_PREFIX = "51,52,53,54,55,";
    private static final String DSC_PREFIX = "6011";
    private static final String AMEX_PREFIX = "34,37,";
    private static final String VISA_PREFIX = "4,5,";

    public static void main(String args[]) throws Exception {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setDatabaseName(dbName);
        mysqlDataSource.setURL(dbURI);
        mysqlDataSource.setUser(dbUser);
        mysqlDataSource.setPassword(dbPass);

        String retrieveRows = "select creditcardid, cardnum from merchant_credit_card " +
                "where status = 'A' and (cardtype = '' or cardtype is null)";
        String updateRow = "update merchant_credit_card set cardtype = ? where creditcardid = ?";
        int creditCardId;
        String cardNum, cardType;
        Map<Integer, String> creditCardsToUpdate = new HashMap<Integer, String>();

        Cryptography cryptography = new Cryptography("DC4BC5EC90B888F4", true);

        // Connection reference
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Make Connection, get Statement, ResultSet
            conn = mysqlDataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(retrieveRows);

            while (rs.next()) {
                creditCardId = rs.getInt("creditcardid");
                System.out.print("creditCardId: " + creditCardId + "; ");
                cardNum = rs.getString("cardnum");
                System.out.print("cardnum: " + cardNum);

                String decryptedCardNum = null;
                try {
                    decryptedCardNum = cryptography.decryptToString(cardNum);
//                    System.out.println("decryptedCardNum: " + decryptedCardNum);

                    // parse the cardnum for cardtype
                    cardType = getFullCardTypeFromNumber(decryptedCardNum);
                    System.out.println("; cardtype: " + cardType);
                    creditCardsToUpdate.put(creditCardId, cardType);
                } catch (Exception e) {
                    System.out.println(" --> ERROR with creditcardid: " + creditCardId + "; cardnum: " + cardNum);
                }
            }

            // get PreparedStatement
            pstmt = conn.prepareStatement(updateRow);
            // iterate through creditCardsToUpdate and update their rows in the db
            for (Map.Entry<Integer, String> cc : creditCardsToUpdate.entrySet()) {
                pstmt.setString(1, cc.getValue());
                pstmt.setInt(2, cc.getKey());
                pstmt.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            // release resources in a finally block in reverse creation order
            if (rs != null) {
                try { rs.close(); } catch (SQLException e) { } // ignore
                rs = null;
            }

            if (stmt != null) {
                try { stmt.close(); } catch (SQLException e) { } // ignore
                stmt = null;
            }

            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { } // ignore
                pstmt = null;
            }

            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { } // ignore
                conn = null;
            }
        }
    }

    public static String getFullCardTypeFromNumber(String number) {
        if (matchesMastercard(number)) {
            return "mastercard";
        }
        if (matchesAmex(number)) {
            return "amex";
        }
        if (matchesDiscover(number)) {
            return "discover";
        }
        if (matchesVisa(number)) {
            return "visa";
        }
        return "";
    }

    // MasterCard
    public static boolean matchesMastercard(String card) {
        if (card.length() < 2)
            return false;
        String prefix2 = card.substring(0, 2) + ",";
        return MC_PREFIX.indexOf(prefix2) != -1 && card.length() == 16;
    }

    //Discover
    public static boolean matchesDiscover(String card) {
        return card.length() >= 4 && card.substring(0, 4).equals(DSC_PREFIX) && card.length() == 16;
    }

    //Amex
    public static boolean matchesAmex(String card) {
        if (card.length() < 2)
            return false;
        String prefix2 = card.substring(0, 2) + ",";
        return AMEX_PREFIX.indexOf(prefix2) != -1 && card.length() == 15;
    }

    //Visa
    public static boolean matchesVisa(String card) {
        if (card.length() < 1)
            return false;
        String prefix2 = card.substring(0, 1) + ",";
        return VISA_PREFIX.indexOf(prefix2) != -1 && (card.length() == 13 || card.length() == 16);
    }
}
