package com.barclayadunn.convert;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * User: barclaydunn
 * Date: 12/13/12
 * Time: 2:12 PM
 */
public class ConvertMRSOutputToOldStyleLog {

    public static void main(String... args) throws Exception {

        json = getFileContents("/Users/barclaydunn/Documents/backup/MEXP-751-logsprocessing/samplemrsoutput.txt");

        Type type = new TypeToken<List<MRSOutputHolder>>(){}.getType();
        List<MRSOutputHolder> mrsOutputHolders = new Gson().fromJson(json, type);

        StringBuffer oldStyleLogHolder = new StringBuffer();
        for (MRSOutputHolder moh : mrsOutputHolders) {
            oldStyleLogHolder.append("ProductRedirectionResponse|");
            oldStyleLogHolder.append(dateFormattterMethod(moh.getTimestamp())).append("|");
            oldStyleLogHolder.append(moh.getParentId()).append("|"); // parent id
            oldStyleLogHolder.append(moh.getRequestId()).append("|"); // request id
            oldStyleLogHolder.append(moh.getUserId()).append("|"); // user id
            oldStyleLogHolder.append(moh.getCookieId()).append("|"); // permCookieId
            oldStyleLogHolder.append(moh.getUserAgent()).append("|"); // userAgent
            oldStyleLogHolder.append(moh.getIpAddress()).append("|"); // ipAddress
            oldStyleLogHolder.append("").append("|"); // bhoName
            oldStyleLogHolder.append("").append("|"); // bhoVersion
            oldStyleLogHolder.append(moh.getLocalHost()).append("|"); // machineId
            oldStyleLogHolder.append(moh.getSearchEngine()).append("|"); // referrerSearchEngine
            oldStyleLogHolder.append(moh.getFullReferrer()).append("|"); // referrerQueryString
            oldStyleLogHolder.append(moh.getAdId()).append("|"); // adID
            oldStyleLogHolder.append("").append("|"); // coBrandID
            oldStyleLogHolder.append(moh.getAbTestingId()).append("|"); // ABTestingID
            oldStyleLogHolder.append(moh.getProntoProductId()).append("|"); // merchantProductId
            oldStyleLogHolder.append(moh.getMerchantId()).append("|"); // merchantId
            oldStyleLogHolder.append(moh.getOfferSourceId()).append("|"); // productSourceId
            oldStyleLogHolder.append(moh.getCpc()).append("|"); // cpc
            oldStyleLogHolder.append("").append("|"); // product title, will not be sent in new style
            oldStyleLogHolder.append(moh.getMexpCategoryId()).append("|"); // mexpCategoryId
            oldStyleLogHolder.append(moh.getPrice()).append("|"); // price
            oldStyleLogHolder.append(moh.getSourceOfferUrl()).append("|"); // url
            oldStyleLogHolder.append(moh.getCountryCode()).append("|"); // countryCode
            oldStyleLogHolder.append(moh.getPapiPartnerId()).append("|"); // partner id
            oldStyleLogHolder.append(moh.getPapiUserId()).append("|"); // channel id
            oldStyleLogHolder.append("\n"); // end of this log row
        }

        System.out.println(oldStyleLogHolder.toString());
    }

    public static String getFileContents(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader (filePath));
        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while( ( line = reader.readLine() ) != null ) {
            stringBuilder.append( line );
            stringBuilder.append( ls );
        }

        return stringBuilder.toString();
    }

    /**
     * @param timestamp date in seconds (not millis)
     * @return date formatted like "12/13/2012 01:53:36"
     */
    private static String dateFormattterMethod(String timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(timestamp)*1000);

        SimpleDateFormat outboundDateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        return outboundDateFormatter.format(calendar.getTime());
    }

    private static String json = "[{\"status\":\"WARN_TOKEN\",\"localHost\":\"va-mrs-dev-www101.cluster\",\"requestId\":\"33995-3029s3j-mjw2-2s\",\"sessionId\":\"\"," +
        "\"timestamp\":1354891577,\"logType\":\"productRedirect\",\"userId\":\"413413\",\"cookieId\":\"\"," +
        "\"userAgent\":\"Mozilla/5.0 (Macintosh; Intel Mac OS X 10.6; rv:15.0) Gecko/20100101 Firefox/15.0.1\"," +
        "\"ipAddress\":\"10.168.2.225\",\"currentUrl\":\"http://www.pronto.com/product/michael-kors-stretch-boucle-shift-p_2163387970\"," +
        "\"fullReferrer\":\"http://www.pronto.com/product/michael-kors-stretch-boucle-shift-p_2163387970\",\"searchEngine\":\"google\"," +
        "\"queryString\":\"red dress\",\"adId\":\"bbb-1\",\"abTestingId\":1234,\"isSem\":1,\"pageLocation\":\"cell-0\",\"timeToExecute\":15," +
        "\"countryCode\":\"us\",\"region\":\"nyc\",\"state\":\"ny\",\"zipCode\":\"10036\",\"merchantOfferId\":1947954621," +
        "\"prontoProductId\":\"p_1947954621\",\"merchantId\":\"92\",\"offerSourceId\":\"13\",\"isPaying\":1,\"cpc\":0.22,\"price\":55.99," +
        "\"mexpCategoryId\":\"106\",\"browseId\":\"v1_2_3_4\"," +
        "\"sourceOfferUrl\":\"http://onlineshoes.rdr.fake.fake/go.asp?fVhzOGNRAAQIASNiE1JSRRlpEC44YRlOAgRKbWBYVFgPESJDKzAnXQgfH0AoJUEMBR9IMFpXV0smN31OXV0fSC09CzwZFkU3ViYnJxwcUUNJYCIMCg9OEGsGe3FmQBRRVUQtMwwIVgNTMVECTFYgJzxJVw1TXDYNUwAHGkQwUCogfBMUU0BYPD0LBlZPQjJWIC86SR0O\u0026nAID\u003d16776730\"," +
        "\"feedDate\":\"\",\"papiPartnerId\":\"20100201\",\"papiUserId\":\"456\",\"position\":\"top\",\"siteId\":3,\"parentId\":\"fk39xm328-3q3-fwe23-343\"," +
        "\"hasInterstitial\":\"1\",\"trackingId\":\"\"}]";
}

class MRSOutputHolder {
    String status;
    String localHost;
    String requestId;
    String sessionId;
    String timestamp;
    String logType;
    String userId;
    String cookieId;
    String userAgent;
    String ipAddress;
    String currentUrl;
    String fullReferrer;
    String searchEngine;
    String queryString;
    String adId;
    String abTestingId;
    String isSem;
    String pageLocation;
    String timeToExecute;
    String countryCode;
    String region;
    String state;
    String zipCode;
    String merchantOfferId;
    String prontoProductId;
    String merchantId;
    String offerSourceId;
    String isPaying;
    String cpc;
    String price;
    String mexpCategoryId;
    String browseId;
    String sourceOfferUrl;
    String feedDate;
    String papiPartnerId;
    String papiUserId;
    String position;
    String siteId;
    String parentId;
    String hasInterstitial;
    String trackingId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLocalHost() {
        return localHost;
    }

    public void setLocalHost(String localHost) {
        this.localHost = localHost;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCurrentUrl() {
        return currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    public String getFullReferrer() {
        return fullReferrer;
    }

    public void setFullReferrer(String fullReferrer) {
        this.fullReferrer = fullReferrer;
    }

    public String getSearchEngine() {
        return searchEngine;
    }

    public void setSearchEngine(String searchEngine) {
        this.searchEngine = searchEngine;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getAdId() {
        return adId;
    }

    public void setAdId(String adId) {
        this.adId = adId;
    }

    public String getAbTestingId() {
        return abTestingId;
    }

    public void setAbTestingId(String abTestingId) {
        this.abTestingId = abTestingId;
    }

    public String getSem() {
        return isSem;
    }

    public void setSem(String sem) {
        isSem = sem;
    }

    public String getPageLocation() {
        return pageLocation;
    }

    public void setPageLocation(String pageLocation) {
        this.pageLocation = pageLocation;
    }

    public String getTimeToExecute() {
        return timeToExecute;
    }

    public void setTimeToExecute(String timeToExecute) {
        this.timeToExecute = timeToExecute;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getMerchantOfferId() {
        return merchantOfferId;
    }

    public void setMerchantOfferId(String merchantOfferId) {
        this.merchantOfferId = merchantOfferId;
    }

    public String getProntoProductId() {
        return prontoProductId;
    }

    public void setProntoProductId(String prontoProductId) {
        this.prontoProductId = prontoProductId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOfferSourceId() {
        return offerSourceId;
    }

    public void setOfferSourceId(String offerSourceId) {
        this.offerSourceId = offerSourceId;
    }

    public String getPaying() {
        return isPaying;
    }

    public void setPaying(String paying) {
        isPaying = paying;
    }

    public String getCpc() {
        return cpc;
    }

    public void setCpc(String cpc) {
        this.cpc = cpc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMexpCategoryId() {
        return mexpCategoryId;
    }

    public void setMexpCategoryId(String mexpCategoryId) {
        this.mexpCategoryId = mexpCategoryId;
    }

    public String getBrowseId() {
        return browseId;
    }

    public void setBrowseId(String browseId) {
        this.browseId = browseId;
    }

    public String getSourceOfferUrl() {
        return sourceOfferUrl;
    }

    public void setSourceOfferUrl(String sourceOfferUrl) {
        this.sourceOfferUrl = sourceOfferUrl;
    }

    public String getFeedDate() {
        return feedDate;
    }

    public void setFeedDate(String feedDate) {
        this.feedDate = feedDate;
    }

    public String getPapiPartnerId() {
        return papiPartnerId;
    }

    public void setPapiPartnerId(String papiPartnerId) {
        this.papiPartnerId = papiPartnerId;
    }

    public String getPapiUserId() {
        return papiUserId;
    }

    public void setPapiUserId(String papiUserId) {
        this.papiUserId = papiUserId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getHasInterstitial() {
        return hasInterstitial;
    }

    public void setHasInterstitial(String hasInterstitial) {
        this.hasInterstitial = hasInterstitial;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
