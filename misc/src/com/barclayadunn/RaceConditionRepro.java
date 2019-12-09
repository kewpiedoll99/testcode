package com.barclayadunn;

import com.rtr.autumn.api.exception.SelfServiceExchangeBagException;
import com.rtr.autumn.api.selfservice.SelfServiceExchangeBag;
import com.rtr.autumn.api.selfservice.SelfServiceExchangeBagStatus;
import com.rtr.autumn.api.selfservice.SelfServiceExchangeBagStatusConverter;
import com.rtr.autumn.core.order.SelfServiceExchangeService;
import com.rtr.autumn.core.transaction.TransactionManager;
import com.rtr.autumn.db.access.AnnotationDAO;
import com.rtr.autumn.db.access.DBUnitTest;
import com.rtr.autumn.db.access.SelfServiceExchangeBagDAO;
import com.rtr.autumn.messaging.ThreadSafeCachingPublisher;
import com.rtr.godfather.client.GodfatherClient;
import com.rtr.rescache.clients.RescacheClient;
import com.rtr.services.commerce.client.CommerceClient;
import com.rtr.wizard.hibernate.ReferencedEnumMapBuilder;
import org.hibernate.Session;
import org.hibernate.context.internal.ManagedSessionContext;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Test;

import static com.google.common.collect.Sets.newHashSet;
import static com.rtr.autumn.api.exception.SelfServiceExchangeBagException.SSEBagExceptionType.PREEXISTING_BAG_IN_DB;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class RaceConditionRepro extends DBUnitTest {
    private static final long USER_ID = 543L;
    private static final Long ORDER_ID = 111L;
    private static final long GROUP_ID = 67890L;
    private static final DateTime NOW = new DateTime(2018, 10, 3, 15, 0, 0, DateTimeZone.UTC);

    private AnnotationDAO annotationDAO = mock(AnnotationDAO.class);
    private CommerceClient commerceClient = mock(CommerceClient.class);
    private GodfatherClient godfatherClient = mock(GodfatherClient.class);
    private RescacheClient rescacheClient = mock(RescacheClient.class);
    private ThreadSafeCachingPublisher notificationPublisher = mock(ThreadSafeCachingPublisher.class);
    private TransactionManager transactionManager = mock(TransactionManager.class);

    private SelfServiceExchangeBag raceTestResult = null;
    private SelfServiceExchangeBagException raceTestException = null;

    private SelfServiceExchangeBagDAO setupSSEBagDAO() {
        try {
            setupTest();
            importDataSet("sql/selfServiceExchangeEntities.xml");
        } catch (Exception e) {
            // no-op, if this happens test is f'd anyway
        }

        SelfServiceExchangeBagStatusConverter.initializeMap(new ReferencedEnumMapBuilder<>(
                SelfServiceExchangeBagStatus.class,
                SESSION_FACTORY,
                "self_service_exchange_bag_status")
                .build());
        SelfServiceExchangeBagDAO selfServiceExchangeBagDAO = new SelfServiceExchangeBagDAO(SESSION_FACTORY);
        Session session = SESSION_FACTORY.openSession();
        ManagedSessionContext.bind(session);
        return selfServiceExchangeBagDAO;
    }

    @Test
    public void testRaceCondition() {
        SelfServiceExchangeBag raceableBag = new SelfServiceExchangeBag();
        raceableBag.setUserId(USER_ID);
        raceableBag.setOrderId(ORDER_ID);
        raceableBag.setOriginalGroupId(GROUP_ID);
        raceableBag.setRentBegin(new DateTime(NOW.plusDays(2).getMillis()));
        raceableBag.setRentEnd(new DateTime(NOW.plusDays(8).getMillis()));
        raceableBag.setAddressId(1L);
        raceableBag.setProblemItemBookings(newHashSet(1L, 2L));
        raceableBag.setReplacementBookings(newHashSet(3L, 4L));

        Thread outer = new Thread(new OuterThreadRunner(raceableBag));
        outer.setName("outer");
        outer.run();

        assertNotNull(raceTestResult);
        assertEquals(raceableBag, raceTestResult);
        assertNotNull(raceTestException);
        assertEquals(PREEXISTING_BAG_IN_DB, raceTestException.getType());
    }

    class OuterThreadRunner implements Runnable {
        SelfServiceExchangeService selfServiceExchangeService;
        SelfServiceExchangeBagDAO selfServiceExchangeBagDAO = setupSSEBagDAO();
        SelfServiceExchangeBag selfServiceExchangeBag;

        OuterThreadRunner(SelfServiceExchangeBag selfServiceExchangeBag) {
            this.selfServiceExchangeService = new SelfServiceExchangeService(
                    this.selfServiceExchangeBagDAO,
                    commerceClient,
                    annotationDAO,
                    rescacheClient,
                    transactionManager,
                    notificationPublisher,
                    godfatherClient
            );
            this.selfServiceExchangeBag = selfServiceExchangeBag;
        }

        public void run() {
            Thread inner = new Thread(new InnerThreadRunner(selfServiceExchangeBag));
            inner.setName("inner");
            inner.run();

            try {
                this.selfServiceExchangeService.createSelfServiceExchangeBag(selfServiceExchangeBag);
            } catch (SelfServiceExchangeBagException e) {
                raceTestException = e;
            } finally {
                selfServiceExchangeBagDAO = null;
                ManagedSessionContext.unbind(SESSION_FACTORY);
            }
        }
    }

    class InnerThreadRunner implements Runnable {
        SelfServiceExchangeService selfServiceExchangeService;
        SelfServiceExchangeBagDAO selfServiceExchangeBagDAO = setupSSEBagDAO();

        SelfServiceExchangeBag selfServiceExchangeBag;

        InnerThreadRunner(SelfServiceExchangeBag selfServiceExchangeBag) {
            this.selfServiceExchangeService = new SelfServiceExchangeService(
                    this.selfServiceExchangeBagDAO,
                    commerceClient,
                    annotationDAO,
                    rescacheClient,
                    transactionManager,
                    notificationPublisher,
                    godfatherClient
            );
            this.selfServiceExchangeBag = selfServiceExchangeBag;
        }

        public void run() {
            try {
                raceTestResult =
                        this.selfServiceExchangeService.createSelfServiceExchangeBag(selfServiceExchangeBag);
            } catch (SelfServiceExchangeBagException e) {
                // no-op
            } finally {
                selfServiceExchangeBagDAO = null;
                ManagedSessionContext.unbind(SESSION_FACTORY);
            }
        }
    }
}
