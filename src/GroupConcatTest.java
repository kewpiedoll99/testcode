import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;

/**
 * User: barclaydunn
 * Date: 5/17/12
 * Time: 12:44 PM
 */
public class GroupConcatTest {

    // to run this, comment this out and uncomment one of the target db's
//    static final String dbURI = "";
    //    static final String dbURI = "jdbc:mysql://localhost:3306/mexp";
        static final String dbURI = "jdbc:mysql://devdbrw01:3306/mexp";
    //    static final String dbURI = "jdbc:mysql://qadbrw01:3306/mexp";
    //    static final String dbURI = "jdbc:mysql://dbrw101:3306/mexp";
    static final String dbUser = "rc";
    static final String dbPass = "shop";
    static final String dbName = "mexp";

    public static void main(String[] args) {

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setDatabaseName(dbName);
        mysqlDataSource.setURL(dbURI);
        mysqlDataSource.setUser(dbUser);
        mysqlDataSource.setPassword(dbPass);

//        String querystring =
//                "SELECT mc.contactid, ce.emailAddress as emailAddress\n" +
//                "FROM merchant_contact mc\n" +
//                "JOIN (\n" +
//                "   SELECT contactid, GROUP_CONCAT(DISTINCT emailaddress SEPARATOR ',') as emailAddress\n" +
//                "   FROM contact_email GROUP BY contactid limit 10\n" +
//                ") ce ON ce.contactid = mc.contactid\n";

        String querystring = "\n" +
                "SELECT concat('PO',LPAD(ma.merchantacctid,7,'0')) as PO,\n" +
                "ma.displayname as displayName,\n" +
                "IF (BILLED_REVENUE_DATA.hasBilledRevenue=1,'A',IF(ma.acctactive=1,'A','I')) as status,\n" +
                //                "concat(concat('PO',LPAD(ma.merchantacctid,7,'0')),'_',concat('PO',LPAD(bci.billingcontactid,7,'0'))) as billingContactId,\n" +
                "concat(concat('PO',LPAD(ma.merchantacctid,7,'0')),'_',concat('PO',LPAD(mc.oraclebillingid,7,'0'))) as billingContactId,\n" +
                "con.address1 as address1,\n" +
                "con.address2 as address2,\n" +
                "con.city as city,\n" +
                "con.state as state,\n" +
                "con.postalcode as postalCode,\n" +
                "con.country as country,\n" +
                "con.firstname as firstname,\n" +
                "con.lastname as lastname,\n" +
                "ce.emailAddress as emailAddress,\n" +
                "'' as phoneAreaCode,\n" +
                "IF (con.phoneext is not null,concat(con.phonenumber,' ',con.phoneext),con.phonenumber) as phoneNumber,\n" +
                "'' as faxAreaCode,\n" +
                "'' as faxNumber,\n" +
                "mt.domain as domainName,\n" +
                "mp.paymentplantypeid as paymentPlanTypeId,\n" +
                "IF (mp.paymentplantypeid = 4, mp.depositamt, NULL) AS invoiceCap\n" +
                "FROM merchant_account ma\n" +
                "JOIN merchant mt on mt.merchantacctid = ma.merchantacctid\n" +
                "JOIN merchant_payment mp on ma.merchantpaymentid = mp.paymentid\n" +
                //                "JOIN mexp.merchant_billing_contact_info bci ON ma.billingcontactid = bci.billingcontactid\n" +
                "JOIN merchant_contact mc ON mc.merchantacctid = ma.merchantacctid AND mc.billingcontact = 1\n" +
                "JOIN contact con ON con.contactid = mc.contactid\n" +
                "JOIN (\n" +
                "   SELECT contactid, GROUP_CONCAT(DISTINCT emailaddress SEPARATOR ',') as emailAddress\n" +
                "   FROM contact_email GROUP BY contactid\n" +
                ") ce ON ce.contactid = mc.contactid\n" +
                "LEFT JOIN (\n" +
                "   SELECT B.merchantAcctId as billedRevenueMerchantAcctId, IF(B.netBilledRevenue>0,1,0) as hasBilledRevenue\n" +
                "   FROM (\n" +
                "       SELECT ma.merchantacctid as merchantAcctId, SUM(A.clickamount) - IFNULL(SUM(A.unmonetizedClicks),0)  - IFNULL(SUM(A.leadcreditamount),0) - IFNULL(SUM(A.promoCreditAmount),0)  AS netBilledRevenue\n" +
                "       FROM (\n" +
                "           SELECT m.merchantacctid AS merchantacctid,0 clickamount, SUM(cr.amt) leadcreditamount,\n" +
                "           0 promoCreditAmount, 0 numLeads, 0 unmonetizedClicks\n" +
                "           FROM credit_txn cr JOIN merchant m ON m.merchantid = cr.merchantid\n" +
                "           WHERE type='lead' AND cr.createddate>='2012-04-01 00:00:00' AND cr.createddate<='2012-04-30 23:59:59'\n" +
                "           GROUP BY m.merchantid\n" +
                "           UNION\n" +
                "           SELECT m.merchantacctid AS merchantacctid,0 clickamount, 0 leadcreditamount,\n" +
                "           SUM(cr.amt) promoCreditAmount, 0 numLeads, 0 unmonetizedClicks\n" +
                "           FROM credit_txn cr JOIN merchant m ON m.merchantid = cr.merchantid\n" +
                "           WHERE type='promo' AND cr.createddate>='2012-04-01 00:00:00' AND cr.createddate<='2012-04-30 23:59:59'\n" +
                "           GROUP BY m.merchantid\n" +
                "           UNION\n" +
                "           SELECT m.merchantacctid AS merchantacctid,SUM(cl.totalcost) clickamount, 0 leadcreditamount,\n" +
                "           0 promoCreditAmount, SUM(cl.numleads) numLeads, 0 unmonetizedClicks\n" +
                "           FROM click_txn cl JOIN merchant m ON m.merchantid = cl.merchantid\n" +
                "           WHERE cl.processeddate>='2012-04-01 00:00:00' AND cl.processeddate<='2012-04-30 23:59:59'\n" +
                "           GROUP BY m.merchantid\n" +
                "           UNION\n" +
                "           SELECT umc.merchantacctid AS merchantacctid, 0 clickamount, 0 leadcreditamount, 0 promoCreditAmount,\n" +
                "           0 numLeads, SUM(umc.amt) AS unmonetizedClicks\n" +
                "           FROM unmonetized_click_txn umc\n" +
                "           WHERE umc.createddate>='2012-04-01 00:00:00' AND umc.createddate<='2012-04-30 23:59:59'\n" +
                "           GROUP BY umc.merchantacctid\n" +
                "       ) A\n" +
                "       JOIN merchant_account ma ON ma.merchantacctid = A.merchantacctid\n" +
                "       JOIN merchant M ON A.merchantacctid = M.merchantacctid\n" +
                "       GROUP BY A.merchantacctid " +
                "   ) B" +
                ") BILLED_REVENUE_DATA ON BILLED_REVENUE_DATA.billedRevenueMerchantAcctId = ma.merchantacctid\n" +
                "UNION\n" +
                "SELECT concat('POA',LPAD(a.agencyid,6,'0')) as PO,\n" +
                "a.agencyname as displayName,\n" +
                "'A' as status,\n" +
                //                "concat(concat('POA',LPAD(a.agencyid,6,'0')),'_',concat('POA',LPAD(bci.billingcontactid,6,'0'))) as billingContactId,\n" +
                "concat(concat('POA',LPAD(a.agencyid,6,'0')),'_',concat('POA',LPAD(mc.oraclebillingid,6,'0'))) as billingContactId,\n" +
                "con.address1 as address1,\n" +
                "con.address2 as address2,\n" +
                "con.city as city,\n" +
                "con.state as state,\n" +
                "con.postalcode as postalCode,\n" +
                "con.country as country,\n" +
                "con.firstname as firstname,\n" +
                "con.lastname as lastname,\n" +
                "ce.emailAddress as emailAddress,\n" +
                "'' as phoneAreaCode,\n" +
                "IF (con.phoneext is not null,concat(con.phonenumber,' ',con.phoneext),con.phonenumber) as phoneNumber,\n" +
                "'' as faxAreaCode,\n" +
                "'' as faxNumber,\n" +
                "M.domain as domain_name,\n" +
                "mp.paymentplantypeid as paymentPlanTypeId,\n" +
                "IF(mp.paymentplantypeid = 4, mp.depositamt, NULL) AS invoiceCap\n" +
                "FROM merchant_account ma\n" +
                "JOIN merchant_payment mp on ma.merchantpaymentid = mp.paymentid\n" +
                "JOIN merchant M ON  M.merchantacctid = ma.merchantacctid\n" +
                "JOIN agency a ON a.agencyid = ma.agencyid\n" +
                //                "JOIN mexp.merchant_billing_contact_info bci ON ma.billingcontactid = bci.billingcontactid\n" +
                "JOIN merchant_contact mc ON mc.merchantacctid = ma.merchantacctid AND mc.billingcontact = 1\n" +
                "JOIN contact con ON con.contactid = mc.contactid\n" +
                "JOIN (\n" +
                "   SELECT contactid, GROUP_CONCAT(DISTINCT emailaddress SEPARATOR ',') as emailAddress\n" +
                "   FROM contact_email GROUP BY contactid\n" +
                ") ce ON ce.contactid = mc.contactid\n" +

                "WHERE ma.agencyid>0 AND ma.agencybillingenabled=1 AND ma.testaccount = 0";


        int contactId;
        String emailAddress;

        // Connection reference
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            // Make Connection, get Statement, ResultSet
            conn = mysqlDataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(querystring);

            while (rs.next()) {
//                contactId = rs.getInt("contactid");
//                emailAddress = rs.getString("emailAddress");
//                System.out.println("contactId: " + contactId + "; emailAddress: " + emailAddress);

                System.out.print(rs.getString("PO") + "\t");
                System.out.print(rs.getString("displayName") + "\t");
                System.out.print(rs.getString("status") + "\t");
                System.out.print(rs.getString("billingContactId") + "\t");
                System.out.print(rs.getString("address1") + "\t");
                System.out.print(rs.getString("address2") + "\t");
                System.out.print(rs.getString("city") + "\t");
                System.out.print(rs.getString("state") + "\t");
                System.out.print(rs.getString("postalCode") + "\t");
                System.out.print(rs.getString("country") + "\t");
                System.out.print(rs.getString("firstname") + "\t");
                System.out.print(rs.getString("lastname") + "\t");
                System.out.print(rs.getString("emailAddress") + "\t");
                System.out.print(rs.getString("phoneAreaCode") + "\t");
                System.out.print(rs.getString("phoneNumber") + "\t");
                System.out.print(rs.getString("faxAreaCode") + "\t");
                System.out.print(rs.getString("faxNumber") + "\t");
                System.out.print(rs.getString("domainName") + "\t");
                System.out.print(rs.getString("paymentPlanTypeId") + "\t");
                System.out.println(rs.getFloat("invoiceCap"));
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
}