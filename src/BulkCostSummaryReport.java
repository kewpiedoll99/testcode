import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * User: barclaydunn
 * Date: 7/12/12
 * Time: 2:11 PM
 */
public class BulkCostSummaryReport {

    public static void main(String[] args) throws Exception {

        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setDatabaseName(dbName);
        mysqlDataSource.setURL(dbURI);
        mysqlDataSource.setUser(dbUser);
        mysqlDataSource.setPassword(dbPass);

        MerchantInfo merchantInfo;
        List<MerchantInfo> merchantInfoList = new ArrayList<MerchantInfo>();

        // Connection reference
        Connection conn = null;
        Statement stmt = null;
        PreparedStatement pstmtCosts = null, pstmtCredits = null;
        ResultSet rs = null;
        try {
            // Make Connection, get Statement, ResultSet
            conn = mysqlDataSource.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery(merchantInfoSelect);

            // get the merchant domains, merchantids, and merchantacctids
            while (rs.next()) {
                merchantInfo = new MerchantInfo(rs.getInt("merchantId"), rs.getInt("merchantAcctId"), rs.getString("domain"));
                merchantInfoList.add(merchantInfo);
            }

            pstmtCosts = conn.prepareStatement(costsSelect);
            pstmtCredits = conn.prepareStatement(creditsSelect);
            for (MerchantInfo mi : merchantInfoList) {
                pstmtCosts.setInt(1, mi.getMerchantAcctId());
                pstmtCosts.setInt(2, mi.getMerchantId());

                rs = pstmtCosts.executeQuery();
                while (rs.next()) {
                    mi.setLeads(rs.getInt("numleads"));
                    mi.setCost(rs.getFloat("totalcost"));
                    mi.setAverageCpc(rs.getFloat("avgcpc"));
                }

                pstmtCredits.setInt(1, mi.getMerchantId());

                rs = pstmtCredits.executeQuery();
                while (rs.next()) {
                    mi.setCredits(rs.getFloat("totalcredits"));
                }
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

            if (pstmtCosts != null) {
                try { pstmtCosts.close(); } catch (SQLException e) { } // ignore
                pstmtCosts = null;
            }

            if (pstmtCredits != null) {
                try { pstmtCredits.close(); } catch (SQLException e) { } // ignore
                pstmtCredits = null;
            }

            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { } // ignore
                conn = null;
            }
        }

        System.out.println("Domain\tMerchantId\tMerchantAcctId\tLeads\tCost\tAvg CPC\tCredits");
        if (merchantInfoList.size() > 0) for (MerchantInfo mi : merchantInfoList) {
            System.out.println(mi.getDomain() + "\t" + mi.getMerchantId() + "\t" + mi.getMerchantAcctId() + "\t" +
                    mi.getLeads() + "\t" + String.format("%.2f", mi.getCost()) + "\t" +
                    String.format("%.2f", mi.getAverageCpc()) + "\t" + String.format("%.2f", mi.getCredits()));
        }
    }

    static final String dbURI = "jdbc:mysql://dbrw102:3306/mexp";
    static final String dbUser = "rc";
    static final String dbPass = "shop";
    static final String dbName = "mexp";

    static final String costsSelect =
//            "SELECT date(c.processeddate) as processeddate, " +
            "SELECT " +
            "SUM(c.numleads) as numleads, " +
            "SUM(c.totalcost) - IFNULL(uct.uctamount,0) as totalcost, " +
            "(SUM(c.totalcost) - IFNULL(uct.uctamount,0))/SUM(c.numleads) as avgcpc " +
            "FROM click_txn c " +
            "LEFT JOIN ( " +
            "        select date(createddate) as uctcreateddate,sum(amt) as uctamount from unmonetized_click_txn " +
            "where merchantacctid = ? " + // merchantacctid
            "and createddate >= '2012-07-01 00:00:00' " +
            "AND createddate <= '2012-07-15 23:59:59' group by date(createddate) " +
            ") uct ON date(c.processeddate) = uct.uctcreateddate " +
            "WHERE c.merchantid = ? " + // merchantid
            "AND c.processed = 1 " +
            "AND c.processeddate >= '2012-07-01 00:00:00' " +
            "AND c.processeddate <= '2012-07-15 23:59:59' " +
            "GROUP BY date_format(c.processeddate, '%m');";

    static final String creditsSelect =
//            "SELECT c.merchantid, date(c.createddate) as createddate, " +
            "SELECT " +
            "SUM(c.amt) as totalcredits " +
            "FROM credit_txn c " +
            "WHERE c.merchantid = ? " + // merchantid
            "AND c.processed = 1 " +
            "AND c.createddate >= '2012-07-01 00:00:00' " +
            "AND c.createddate <= '2012-07-15 23:59:59' " +
            "GROUP BY date_format(c.createddate, '%m');";

    static final String merchantInfoSelect =
            "select domain, merchantid, merchantacctid from merchant where domain in ( " +
            "'1000bulbs.com', " +
            "'1800anylens.com', " +
            "'1800baskets.com', " +
            "'1800flowers.com', " +
            "'1-800lighting.com', " +
            "'1800petmeds.com', " +
            "'1stoplighting.com', " +
            "'3balls.com', " +
            "'4inkjets.com', " +
            "'911healthshop.com', " +
            "'a1components.com', " +
            "'abbottstore.com', " +
            "'accessfurniture.com', " +
            "'accessorygeeks.com', " +
            "'acehardware.com', " +
            "'acsuperstore.com', " +
            "'advanceautoparts.com', " +
            "'advancedlamps.com', " +
            "'ae.com', " +
            "'alattehotte.com', " +
            "'albeebaby.com', " +
            "'alice.com', " +
            "'all-battery.com', " +
            "'allegromedical.com', " +
            "'allergybegone.com', " +
            "'alloy.com', " +
            "'altrec.com', " +
            "'amazingsocks.com', " +
            "'amazing-solutions.com', " +
            "'ambfurniture.com', " +
            "'ameds.com', " +
            "'americandiabeteswholesale.com', " +
            "'americanmuscle.com', " +
            "'amerimark.com', " +
            "'ameswalker.com', " +
            "'angara.com', " +
            "'antonline.com', " +
            "'anu-skin.net', " +
            "'arcocoffee.com', " +
            "'ashford.com', " +
            "'askderm.com', " +
            "'astonefitness.com', " +
            "'athleta.com', " +
            "'atlanticbeds.com', " +
            "'austinkayak.com', " +
            "'autoanything.com', " +
            "'autosportcatalog.com', " +
            "'babieschristeningoutfits.com', " +
            "'babooshbaby.com', " +
            "'babyearth.com', " +
            "'babysupermall.com', " +
            "'backinthesaddle.com', " +
            "'backyardocean.com', " +
            "'backyardpoolsuperstore.com', " +
            "'bambeco.com', " +
            "'bambibaby.com', " +
            "'bananarepublic.com', " +
            "'barenecessities.com', " +
            "'barristerbooks.com', " +
            "'barstooldirect.com', " +
            "'basequipment.com', " +
            "'bathroom-glass-vanities.com', " +
            "'batterybay.net', " +
            "'batteryclerk.com', " +
            "'batterynerds.com', " +
            "'batteryoperatedcandles.net', " +
            "'beachmall.com', " +
            "'beallsflorida.com', " +
            "'beau-coup.com', " +
            "'beautedeparis.fr', " +
            "'beautiesltd.com', " +
            "'beautisol.com', " +
            "'beautybar.com', " +
            "'beautybridge.com', " +
            "'beercollections.com', " +
            "'bellacor.com', " +
            "'benmeadows.com', " +
            "'berenshoes.com', " +
            "'bestnest.com', " +
            "'besttonerbuys.com', " +
            "'besttonershop.com', " +
            "'betterhealthinternational.com', " +
            "'bigduffles.com', " +
            "'blankterrmall.com', " +
            "'bloomingdales.com', " +
            "'bluefly.com', " +
            "'blueskyscrubs.com', " +
            "'bnbtobacco.com', " +
            "'boardgames4us.com', " +
            "'bobscycle.com', " +
            "'bonobos.com', " +
            "'bose.com', " +
            "'boundarywaters.biz', " +
            "'boutiquetoyou.com', " +
            "'bradfordexchange.com', " +
            "'brakepadwholesale.com', " +
            "'breastpumpdeals.com', " +
            "'brownies.com', " +
            "'brylanehome.com', " +
            "'buckle.com', " +
            "'budgetmailboxes.com', " +
            "'budovideos.com', " +
            "'build.com', " +
            "'builddirect.com', " +
            "'bunkbeds.zoostores.com', " +
            "'buy4ssd.com', " +
            "'buydirectvitamins.com', " +
            "'cabelas.com', " +
            "'campmor.com', " +
            "'candleluxury.com', " +
            "'canvas.landsend.com', " +
            "'carid.com', " +
            "'carriagegifts.com', " +
            "'carrollscove.com', " +
            "'carters.com', " +
            "'carterswatchthewear.com', " +
            "'catalogfavorites.com', " +
            "'cb2.com', " +
            "'celebrateexpress.com', " +
            "'cellphoneshop.net', " +
            "'centurynovelty.com', " +
            "'cglasses.com', " +
            "'championusa.com', " +
            "'charlotterusse.com', " +
            "'cheapchux.com', " +
            "'chefscatalog.com', " +
            "'chefsresource.com', " +
            "'childrens-island.com', " +
            "'childrensplace.com', " +
            "'chineselaundry.com', " +
            "'cigarsinternational.com', " +
            "'claflinequip.com', " +
            "'clarisonic.com', " +
            "'clarybusinessmachines.com', " +
            "'claryco.com', " +
            "'classichostess.com', " +
            "'classicshapewear.com', " +
            "'cleanitsupply.com', " +
            "'cleanspirited.com', " +
            "'clearlice.com', " +
            "'coastalcontacts.com', " +
            "'coinmerc.com', " +
            "'coldwatercreek.com', " +
            "'combatoptical.com', " +
            "'commercialvacuum.com', " +
            "'compactappliance.com', " +
            "'compressionstockings.com', " +
            "'constplay.com', " +
            "'cookieskids.com', " +
            "'coolibar.com', " +
            "'copyfaxes.com', " +
            "'costumediscounters.com', " +
            "'costumes4less.com', " +
            "'countrysidepet.com', " +
            "'crane.com', " +
            "'crateandbarrel.com', " +
            "'createforless.com', " +
            "'crucial.com', " +
            "'crucialvacuum.com', " +
            "'crystalplus.com', " +
            "'customizedgirl.com', " +
            "'customtreasures.net', " +
            "'cutathome.com', " +
            "'cutleryandmore.com', " +
            "'cyberswim.com', " +
            "'davesdiscountautoparts.com', " +
            "'dazadi.com', " +
            "'debspecs.com', " +
            "'denniskirk.com', " +
            "'denversecuritygroup.com', " +
            "'dermadoctor.com', " +
            "'designed2bsweet.com', " +
            "'designerplumbing.com', " +
            "'designtoscano.com', " +
            "'destinationlighting.com', " +
            "'detailing.com', " +
            "'dezignwithaz.com', " +
            "'dhstyles.com', " +
            "'digitalbuyer.com', " +
            "'dinodirect.com', " +
            "'directbuylighting.com', " +
            "'discountcatholicproducts.com', " +
            "'discountdecorativeflags.com', " +
            "'discountfilters.com', " +
            "'discountramps.com', " +
            "'distinctivestores.com', " +
            "'district5boutique.com', " +
            "'divers-supply.com', " +
            "'dollargeneral.com', " +
            "'domestications.com', " +
            "'drdavidwilliams.com', " +
            "'drjays.com', " +
            "'drugsupplystore.com', " +
            "'drwhitaker.com', " +
            "'dsw.com', " +
            "'duluthtrading.com', " +
            "'duplicatordepot.com', " +
            "'eastbay.com', " +
            "'ectaco.com', " +
            "'ekindom.com', " +
            "'electricgeneratorsdirect.com', " +
            "'electronicwhiteboardswarehouse.com', " +
            "'elitefixtures.com', " +
            "'empirecovers.com', " +
            "'entertainmentcenterspot.com', " +
            "'entirelypets.com', " +
            "'ephedrawholesale.com', " +
            "'epichealthshop.com', " +
            "'epicsports.com', " +
            "'esportsonline.com', " +
            "'essentialapparel.com', " +
            "'everythingfurniture.com', " +
            "'evo.com', " +
            "'exclusivelyweddings.com', " +
            "'exposuresonline.com', " +
            "'express-inks.com', " +
            "'factorydirect2you.com', " +
            "'fansedge.com', " +
            "'fashambeauty.com', " +
            "'fashionandbeautystore.com', " +
            "'fashionbug.com', " +
            "'faucetdirect.com', " +
            "'favorfavor.com', " +
            "'filtersfast.com', " +
            "'filters-now.com', " +
            "'finishline.com', " +
            "'floormall.com', " +
            "'footballfanatics.com', " +
            "'fossil.com', " +
            "'fragrancenet.com', " +
            "'frameitall.com', " +
            "'franklinplanner.com', " +
            "'frugah.com', " +
            "'funkidspajamas.com', " +
            "'gap.com', " +
            "'garagecabinetsonline.com', " +
            "'garage-organization.com', " +
            "'gardenfun.com', " +
            "'geminicomputersinc.com', " +
            "'gemplers.com', " +
            "'genesisautoparts.com', " +
            "'genuinebrandsupplies.com', " +
            "'giorgioarmanibeauty-usa.com', " +
            "'globalindustrial.com', " +
            "'goldenteak.com', " +
            "'golfgpsdeals4u.com', " +
            "'golfsmith.com', " +
            "'goodstewardbooks.com', " +
            "'go-part.com', " +
            "'goprocamera.com', " +
            "'gourmetgiftbaskets.com', " +
            "'grainger.com', " +
            "'graphictoners.com', " +
            "'greenmountaincoffee.com', " +
            "'haband.com', " +
            "'haircollection.net', " +
            "'handlesets.com', " +
            "'hanes.com', " +
            "'hansensurf.com', " +
            "'harryanddavid.com', " +
            "'hatland.com', " +
            "'hayneedle.com', " +
            "'healthproductsforyou.com', " +
            "'heavenlytreasures.com', " +
            "'heels.com', " +
            "'hertzfurniture.com', " +
            "'holabirdsports.com', " +
            "'homedepot.com', " +
            "'homegyms.zoostores.com', " +
            "'homelement.com', " +
            "'hometheaterseatinghq.com', " +
            "'homewetbar.com', " +
            "'honeybearhealthcare.com', " +
            "'hookbag.com', " +
            "'horchow.com', " +
            "'hotbuckles.com', " +
            "'hotmoviesale.com', " +
            "'houseofantiquehardware.com', " +
            "'hp.com', " +
            "'hpbusiness.com', " +
            "'hurricanegolf.com', " +
            "'iaqsource.com', " +
            "'ibraggiotti.com', " +
            "'ice.com', " +
            "'icemachinesplus.com', " +
            "'improvementscatalog.com', " +
            "'inkgrabber.com', " +
            "'innovateistore.com', " +
            "'instawares.com', " +
            "'inthecompanyofdogs.com', " +
            "'intheholegolf.com', " +
            "'intheswim.com', " +
            "'iqhardware.com', " +
            "'irobot.com', " +
            "'isabellacatalog.com', " +
            "'jampaper.com', " +
            "'jcomputech.com', " +
            "'jcpenney.com', " +
            "'jessicalondon.com', " +
            "'jms.com', " +
            "'joesnewbalanceoutlet.com', " +
            "'jpcycles.com', " +
            "'kajeet.com', " +
            "'kardiel.com', " +
            "'katom.com', " +
            "'kiehls.com', " +
            "'kitchenall.com', " +
            "'kitchensource.com', " +
            "'kjbeckett.com', " +
            "'kohls.com', " +
            "'kranichs.com', " +
            "'lacoste.com', " +
            "'lacrosse.com', " +
            "'lancome-usa.com', " +
            "'landofnod.com', " +
            "'landsend.com', " +
            "'lanebryant.com', " +
            "'laptopgears.com', " +
            "'largodrive.com', " +
            "'lastcall.com', " +
            "'ldproducts.com', " +
            "'leapsandbounds.com', " +
            "'leatherpaks.com', " +
            "'lensdiscounters.com', " +
            "'lensway.com', " +
            "'lenyaonlinejewelrystore.com', " +
            "'level8technology.com', " +
            "'lightingcatalog.com', " +
            "'lightingdirect.com', " +
            "'lightingnewyork.com', " +
            "'lightingshowroom.com', " +
            "'lightinthebox.com', " +
            "'lindasonline.com', " +
            "'linensource.com', " +
            "'littlesoftshop.com', " +
            "'llbean.com', " +
            "'loehmanns.com', " +
            "'lowes.com', " +
            "'lowpriceline.com', " +
            "'lubattery.com', " +
            "'luckyvitamin.com', " +
            "'luggageonline.com', " +
            "'lumens.com', " +
            "'lunawarehouse.com', " +
            "'madisonseating.com', " +
            "'maggylondon.com', " +
            "'maidenform.com', " +
            "'manhattanhomedesign.com', " +
            "'maplehillarts.com', " +
            "'marshallfloralproducts.com', " +
            "'massagechairs.com', " +
            "'massagetables.zoostores.com', " +
            "'masterspaparts.com', " +
            "'mattel.com', " +
            "'medicaldevicedepot.com', " +
            "'mercantila.com', " +
            "'mexicaliblues.com', " +
            "'mezuzahmaster.com', " +
            "'miamiskinautiques.com', " +
            "'microsoftstore.com', " +
            "'misterart.com', " +
            "'modani.com', " +
            "'moosejaw.com', " +
            "'moreofmetolove.com', " +
            "'motorcycle-superstore.com', " +
            "'musiciansfriend.com', " +
            "'mybabyclothes.com', " +
            "'mybinding.com', " +
            "'mycleaningsupply.us', " +
            "'myfoodstorage.com', " +
            "'myprinterparts.com', " +
            "'myspashop.com', " +
            "'nantucketbrand.com', " +
            "'nationalpetpharmacy.com', " +
            "'nationaltoolwarehouse.com', " +
            "'nativeremedies.com', " +
            "'natlallergy.com', " +
            "'naturesbasin.com', " +
            "'neimanmarcus.com', " +
            "'newvitality.com', " +
            "'nittanyoutlet.com', " +
            "'njpoolstore.com', " +
            "'nordstrom.com', " +
            "'normthompson.com', " +
            "'northerntool.com', " +
            "'northshorecare.com', " +
            "'novica.com', " +
            "'nuvalife.com', " +
            "'o2gearshop.com', " +
            "'oempcworld.com', " +
            "'oewheelsllc.com', " +
            "'officechairs.com', " +
            "'officefurniture.com', " +
            "'officemax.com', " +
            "'oldnavy.com', " +
            "'oldwillknottscales.com', " +
            "'omegastores.com', " +
            "'omorfiashop.com', " +
            "'onehanesplace.com', " +
            "'onestopplus.com', " +
            "'onewayfurniture.com', " +
            "'onlinevillagecafe.com', " +
            "'oreck.com', " +
            "'origincrafts.com', " +
            "'orvis.com', " +
            "'oshkoshbgosh.com', " +
            "'overstock.com', " +
            "'overstockbait.com', " +
            "'ozonebilliards.com', " +
            "'pacificcoast.com', " +
            "'painting4u.com', " +
            "'palmbeachjewelry.com', " +
            "'partyatlewis.com', " +
            "'pdaparts.com', " +
            "'peachsuite.com', " +
            "'pegasuslighting.com', " +
            "'pensnmore.com', " +
            "'personalcreations.com', " +
            "'personalizationmall.com', " +
            "'petco.com', " +
            "'petedge.com', " +
            "'petfooddirect.com', " +
            "'petsolutions.com', " +
            "'pfile.com', " +
            "'pills4u-store.com', " +
            "'pinmart.com', " +
            "'plessers.com', " +
            "'plushbeds.com', " +
            "'polar-ray.com', " +
            "'pos-equipment.net', " +
            "'potpourrigift.com', " +
            "'pricewiseevents.com', " +
            "'pricewisefavors.com', " +
            "'primevapor.com', " +
            "'probioticsmart.com', " +
            "'produplicator.com', " +
            "'proflowers.com', " +
            "'pullsdirect.com', " +
            "'pureformulas.com', " +
            "'purepointgolf.com', " +
            "'puritan.com', " +
            "'queenbeeofbeverlyhills.com', " +
            "'quicksupply.net', " +
            "'quikshiptoner.com', " +
            "'quiksilver.com', " +
            "'radiatorspot.com', " +
            "'radioshack.com', " +
            "'rainebrooke.com', " +
            "'ralphlauren.com', " +
            "'red21boys.com', " +
            "'reebok.com', " +
            "'relicbrand.com', " +
            "'replacements.com', " +
            "'revivalanimal.com', " +
            "'rfvr.com', " +
            "'roamans.com', " +
            "'rockbottomgolf.com', " +
            "'rockcreek.com', " +
            "'rockstartactical.com', " +
            "'rockymountaintrail.com', " +
            "'ross-simons.com', " +
            "'roxy.com', " +
            "'rugsusa.com', " +
            "'runoutlet.com', " +
            "'rythercamera.com', " +
            "'saddleonline.com', " +
            "'samedaymusic.com', " +
            "'sassysarongs.com', " +
            "'savvyleathersofas.com', " +
            "'schooloutfitters.com', " +
            "'scooterdepot.us', " +
            "'sears.com', " +
            "'seejanerun.com', " +
            "'seismicaudiospeakers.com', " +
            "'sheplers.com', " +
            "'shindigz.com', " +
            "'shoebuy.com', " +
            "'shoemall.com', " +
            "'shopchristophers.com', " +
            "'shoplinksys.com', " +
            "'shopnewbalance.com', " +
            "'shoptrudeau.com', " +
            "'sierratradingpost.com', " +
            "'signaturehardware.com', " +
            "'simplytapestries.com', " +
            "'simplywallclocks.com', " +
            "'sitbetter.com', " +
            "'sjf.com', " +
            "'skinbotanica.com', " +
            "'skindimensionsonline.com', " +
            "'skullcandy.com', " +
            "'sleekhair.com', " +
            "'smartbuyglasses.com', " +
            "'smartcisco.com', " +
            "'smartfishtechnologies.com', " +
            "'soapsoundz.com', " +
            "'soccer.com', " +
            "'socksmax.com', " +
            "'softwaremediaexpress.com', " +
            "'soldiercity.com', " +
            "'sonicelectronix.com', " +
            "'spaandpoolstore.com', " +
            "'sportskids.com', " +
            "'ssense.com', " +
            "'stuff4scrapbooking.com', " +
            "'stuffedsafari.com', " +
            "'stylesoflighting.com', " +
            "'surfride.com', " +
            "'surveillance-video.com', " +
            "'susanb.com', " +
            "'swansonvitamins.com', " +
            "'swell.com', " +
            "'swim2000.com', " +
            "'swimoutlet.com', " +
            "'swimtowin.com', " +
            "'tanninglotionsale.com', " +
            "'target.com', " +
            "'technorv.com', " +
            "'tek-micro.com', " +
            "'tennisexpress.com', " +
            "'terrysvillage.com', " +
            "'tgw.com', " +
            "'thatswholesale.com', " +
            "'thecompanystore.com', " +
            "'thedigitalscale.com', " +
            "'thefeltstore.com', " +
            "'thefutonshop.com', " +
            "'thelightingshop.com', " +
            "'themedicalsupplydepot.com', " +
            "'thenaturalonline.com', " +
            "'theshoemart.com', " +
            "'thingsremembered.com', " +
            "'thompsoncigar.com', " +
            "'tikishackimporter.com', " +
            "'tillys.com', " +
            "'tnvitamins.com', " +
            "'toms.com', " +
            "'tonyperottiusa.com', " +
            "'toryburch.com', " +
            "'toshibadirect.com', " +
            "'totalcomputing.net', " +
            "'totaldiabetessupply.com', " +
            "'totalvac.com', " +
            "'touchofclass.com', " +
            "'trainparty.com', " +
            "'travelsmith.com', " +
            "'truemodelships.com', " +
            "'truereligionbrandjeans.com', " +
            "'tscfurniture.com', " +
            "'tschome.com', " +
            "'tscpets.com', " +
            "'tsctoys.com', " +
            "'tsonlinesoftware.com', " +
            "'ubershamo.com', " +
            "'ultrasonicsdirect.com', " +
            "'undergear.com', " +
            "'usaprinterguy.com', " +
            "'usknobs.com', " +
            "'usmedicalsupplies.com', " +
            "'vacuumsinc.com', " +
            "'ventingdirect.com', " +
            "'verabradley.com', " +
            "'vermontcountrystore.com', " +
            "'vetdepot.com', " +
            "'vintagefringe.com', " +
            "'vintagetub.com', " +
            "'vitacost.com', " +
            "'voguewigs.com', " +
            "'vonvonni.com', " +
            "'vpgames.com', " +
            "'vrp.com', " +
            "'walletbe.com', " +
            "'wayfair.com', " +
            "'webstaurantstore.com', " +
            "'webundies.com', " +
            "'weddingcoo.com', " +
            "'wellsasa.com', " +
            "'westsidewholesale.com', " +
            "'whateverworks.com', " +
            "'whatshebuys.com', " +
            "'whitakerbrothers.com', " +
            "'wholesalecostumeclub.com', " +
            "'wholesalehalloweencostumes.com', " +
            "'wholesalekeychain.com', " +
            "'wickercentral.com', " +
            "'williams-sonoma.com', " +
            "'winecountrygiftbaskets.com', " +
            "'womanwithin.com', " +
            "'worcesterwreath.com', " +
            "'workingperson.com', " +
            "'world-buy.com', " +
            "'worldsoccershop.com', " +
            "'wrangler.com', " +
            "'xump.com', " +
            "'yonofoco.com', " +
            "'yourpoolhq.com', " +
            "'zagg.com', " +
            "'zappos.com', " +
            "'zgostore.com', " +
            "'zoostores.com', " +
            "'ztechsoftware.com', " +
            "'zwello.com', " +
            "'zzounds.com'" +
            ");";
}

class MerchantInfo {
    int merchantId;
    int merchantAcctId;
    String domain;
    int leads;
    float cost;
    float averageCpc;
    float credits;

    MerchantInfo(int merchantId, int merchantAcctId, String domain) {
        this.merchantId = merchantId;
        this.merchantAcctId = merchantAcctId;
        this.domain = domain;
    }

    public int getMerchantId() { return merchantId; }
    public void setMerchantId(int merchantId) { this.merchantId = merchantId; }
    public int getMerchantAcctId() { return merchantAcctId; }
    public void setMerchantAcctId(int merchantAcctId) { this.merchantAcctId = merchantAcctId; }
    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public int getLeads() { return leads; }
    public void setLeads(int leads) { this.leads = leads; }
    public float getCost() { return cost; }
    public void setCost(float cost) { this.cost = cost; }
    public float getAverageCpc() { return averageCpc; }
    public void setAverageCpc(float averageCpc) { this.averageCpc = averageCpc; }
    public float getCredits() { return credits; }
    public void setCredits(float credits) { this.credits = credits; }
}
