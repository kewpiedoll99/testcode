package com.barclayadunn.tivo;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * User: barclayadunn
 * Date: 2/5/14
 * Time: 4:26 PM
 */
public class TivoTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please enter user eschatus1's password: ");
        String password = sc.nextLine();

        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();

            HttpPost httpPost = new HttpPost("https://www.tivo.com/tivo-mma/login.do");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("cams_cb_username", "eschatus1"));
            nvps.add(new BasicNameValuePair("cams_cb_password", password));
            nvps.add(new BasicNameValuePair("cams_security_domain", "tivocom"));
            nvps.add(new BasicNameValuePair("cams_original_url", "/tivo-mma/index.do"));
            nvps.add(new BasicNameValuePair("cams_login_config", "http"));
            nvps.add(new BasicNameValuePair("cams_cb_partner", ""));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
//                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }

            System.out.println("Please press return.");
            String unused = sc.nextLine();
            System.out.println("Okay, please wait.");

            HttpGet httpGet = new HttpGet("https://www.tivo.com/tivo-tco/search/simple.do?dispatch=simplesearch&searchFor=Tootsie");
            CloseableHttpResponse response1 = httpclient.execute(httpGet);
            // The underlying HTTP connection is still held by the response object
            // to allow the response content to be streamed directly from the network socket.
            // In order to ensure correct deallocation of system resources
            // the user MUST either fully consume the response content  or abort request
            // execution by calling CloseableHttpResponse#close().

            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                String html = EntityUtils.toString(entity1);
                Document doc = Jsoup.parse(html);
                Elements pageContent = doc.select("#page-content");
                System.out.println("result:\n" + pageContent.toString());
            } catch (java.io.IOException ioe) {
                System.err.println(ioe.getMessage());
            } catch (org.apache.http.ParseException pe) {
                System.err.println(pe.getMessage());
            } finally {
                response1.close();
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
}
