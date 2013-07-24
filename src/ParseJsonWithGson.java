import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.io.IOUtils;

/**
 * User: barclaydunn
 * Date: Mar 2, 2011
 * Time: 12:05:43 PM
 */
public class ParseJsonWithGson {

    public static void main(String... args) throws Exception {
//        String json =
//            "[{\"pk\": 1211, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"99.1\", " +
//                    "\"customer_support\": \"95.0\", \"company\": \"Zappos.com\", \"overall_stella_rating\": \"96.5\", " +
//                    "\"online_tools\": \"95.5\", \"returns_info\": \"Items can be returned for a full cash refund within 365 days of the purchase date. Return shipping is free.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.zappos.com/shipping-and-returns\"}}, " +
//             "{\"pk\": 1212, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"98.6\", " +
//                    "\"customer_support\": \"93.2\", \"company\": \"Soap.com\", \"overall_stella_rating\": \"96.4\", " +
//                    "\"online_tools\": \"97.4\", \"returns_info\": \"Returns can be made for a full cash refund for up to 365 days. Return shipping is free.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.soap.com/helpcenter/returns.aspx#Return\"}}, " +
//             "{\"pk\": 1213, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"98.6\", " +
//                    "\"customer_support\": \"93.2\", \"company\": \"Diapers.com\", \"overall_stella_rating\": \"96.0\", " +
//                    "\"online_tools\": \"96.2\", \"returns_info\": \"Returns can be made for a full cash refund for up to 365 days. Return shipping is free.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.diapers.com/HelpCenter/Returns.aspx\"}}, " +
//             "{\"pk\": 1214, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"94.4\", " +
//                    "\"customer_support\": \"96.4\", \"company\": \"BlueNile.com\", \"overall_stella_rating\": \"95.3\", " +
//                    "\"online_tools\": \"95.1\", \"returns_info\": \"All items can be returned for a full cash refund within 30 days. Return shipping is paid for by the customer.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.bluenile.com/returns\"}}, " +
//             "{\"pk\": 1215, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"95.7\", " +
//                    "\"customer_support\": \"94.6\", \"company\": \"Staples.com\", \"overall_stella_rating\": \"94.0\", " +
//                    "\"online_tools\": \"91.8\", \"returns_info\": \"Most items can be returned at any time. There are no return shipping expenses; the company sends a driver to pick up the returned product.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.staples.com/sbd/content/help/using/returns_policy.html\"}}, " +
//             "{\"pk\": 1216, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"93.2\", " +
//                    "\"customer_support\": \"87.2\", \"company\": \"Amazon.com\", \"overall_stella_rating\": \"93.1\", " +
//                    "\"online_tools\": \"98.9\", \"returns_info\": \"Most items can be returned for a full cash refund for up to 30 days. Return shipping is only free if the return is a result of Amazon's error.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.amazon.com/gp/help/customer/display.html/ref=hy_f_4?ie=UTF8&nodeId=901888\"}}, " +
//             "{\"pk\": 1217, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"91.8\", " +
//                    "\"customer_support\": \"92.4\", \"company\": \"DVDEmpire.com\", \"overall_stella_rating\": \"93.0\", " +
//                    "\"online_tools\": \"94.8\", \"returns_info\": \"All items can be returned for a full cash refund within 30 days. Return shipping is paid for by the customer.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.dvdempire.com/Exec/Cust_Service/v4_index.asp?site=0&tab=6\"}}, " +
//             "{\"pk\": 1218, \"model\": \"ptnr.company\", \"fields\": {\"shipping_delivery_returns\": \"90.6\", " +
//                    "\"customer_support\": \"96.9\", \"company\": \"L.L. Bean.com\", \"overall_stella_rating\": \"92.7\", " +
//                    "\"online_tools\": \"90.7\", \"returns_info\": \"All items can be returned for a full cash refund with no time limit or deadline. Return shipping is paid for by the customer. However, exchanges are free.\", " +
//                    "\"seal\": \"ELITE\", \"link_to_return_policy\": \"http://www.llbean.com/shop/returns/index.html?nav=ftbar\"}}]";

        String json = stringOfUrl("http://ptnr.stellaservice.com/restex/88fcJ97g6enccf7H7Gfdveu");        

        // Now do the magic.
        Type type = new TypeToken<List<StellaRatingRow>>(){}.getType();
        List<StellaRatingRow> stellaRatingRows = new Gson().fromJson(json, type);

        // Show it.
        for (StellaRatingRow stellaRatingRow : stellaRatingRows) {
            System.out.println(stellaRatingRow.toString());
        }
    }

    public static String stringOfUrl(String addr) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        URL url = new URL(addr);
        IOUtils.copy(url.openStream(), output);
        return output.toString();
    }
}

class StellaRatingRow {
    private int pk;
    private String model;
    private StellaRatingRowFields fields;

    public StellaRatingRowFields getFields() { return fields; }

    public void setFields(StellaRatingRowFields fields) { this.fields = fields; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public int getPk() { return pk; }

    public void setPk(int pk) { this.pk = pk; }

    @Override
    public String toString() {
        return "StellaRatingRow{" +
                "pk=" + pk +
                ", model='" + model + '\'' +
                ", fields=" + fields +
                '}';
    }
}

class StellaRatingRowFields {
    private float shipping_delivery_returns;
    private float customer_support;
    private String company;
    private float overall_stella_rating;
    private float online_tools;
    private String returns_info;
    private String seal;
    private String link_to_return_policy;

    public String getCompany() { return company; }

    public void setCompany(String company) { this.company = company; }

    public float getCustomer_support() { return customer_support; }

    public void setCustomer_support(float customer_support) { this.customer_support = customer_support; }

    public String getLink_to_return_policy() { return link_to_return_policy; }

    public void setLink_to_return_policy(String link_to_return_policy) { this.link_to_return_policy = link_to_return_policy; }

    public float getOnline_tools() { return online_tools; }

    public void setOnline_tools(float online_tools) { this.online_tools = online_tools; }

    public float getOverall_stella_rating() { return overall_stella_rating; }

    public void setOverall_stella_rating(float overall_stella_rating) { this.overall_stella_rating = overall_stella_rating; }

    public String getReturns_info() { return returns_info; }

    public void setReturns_info(String returns_info) { this.returns_info = returns_info; }

    public String getSeal() { return seal; }

    public void setSeal(String seal) { this.seal = seal; }

    public float getShipping_delivery_returns() { return shipping_delivery_returns; }

    public void setShipping_delivery_returns(float shipping_delivery_returns) { this.shipping_delivery_returns = shipping_delivery_returns; }

    @Override
    public String toString() {
        return "StellaRatingRowFields{" +
                "company='" + company + '\'' +
                ", shipping_delivery_returns=" + shipping_delivery_returns +
                ", customer_support=" + customer_support +
                ", overall_stella_rating=" + overall_stella_rating +
                ", online_tools=" + online_tools +
                ", returns_info='" + returns_info + '\'' +
                ", seal='" + seal + '\'' +
                ", link_to_return_policy='" + link_to_return_policy + '\'' +
                '}';
    }
}
