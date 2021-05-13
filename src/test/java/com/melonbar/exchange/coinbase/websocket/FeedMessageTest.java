package com.melonbar.exchange.coinbase.websocket;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.melonbar.exchange.coinbase.testutil.RandomValueProvider;
import com.melonbar.exchange.coinbase.util.Format;
import com.melonbar.exchange.coinbase.websocket.message.ErrorMessage;
import com.melonbar.exchange.coinbase.websocket.message.FeedMessage;
import com.melonbar.exchange.coinbase.websocket.message.HeartbeatMessage;
import com.melonbar.exchange.coinbase.websocket.message.L2UpdateMessage;
import com.melonbar.exchange.coinbase.websocket.message.SnapshotMessage;
import com.melonbar.exchange.coinbase.websocket.message.SubscribeMessage;
import com.melonbar.exchange.coinbase.websocket.message.SubscriptionsMessage;
import com.melonbar.exchange.coinbase.websocket.message.TickerMessage;
import com.melonbar.exchange.coinbase.websocket.message.deserializer.JsonMessageMapper;
import com.melonbar.exchange.coinbase.websocket.message.full.ActivatedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ChangedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ClosedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.MatchedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.OpenedOrderMessage;
import com.melonbar.exchange.coinbase.websocket.message.full.ReceivedOrderMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class FeedMessageTest {

    private SecureRandom random;

    private final int epochs = 10;
    private final int threads = 10;

    @BeforeTest
    public void init() {
        random = new SecureRandom();
    }

    @Test(dataProvider = "feedMessageTypeDataProvider")
    public <T extends FeedMessage> void testFeedMessageTypeIsNonLossy(final Class<T> feedMessageType) {
        T feedMessage = null;

        // perform multiple epochs to try different random configuration of set fields
        for (int i = 0; i < epochs; i++) {
            try {
                feedMessage = feedMessageType.getConstructor().newInstance();
            } catch (Exception exception) {
                Assert.fail("Failed instantiation for type: " + feedMessageType.getName(), exception);
            }

            // fetch all fields and apply random value
            final Field[] fields = FieldUtils.getAllFields(feedMessageType);
            for (final Field field : fields) {
                // only care about json properties
                if (field.getAnnotation(JsonProperty.class) == null) {
                    continue;
                }

                field.setAccessible(true);

                // set fields at random
                if (chance(0.3f)) {
                    try {
                        field.set(feedMessage, RandomValueProvider.random(field.getType()));
                    } catch (IllegalAccessException illegalAccessException) {
                        Assert.fail(
                                Format.format("Failed to set randomly generated field for field type: [{}], "
                                        + "and feed message type: [{}]", field.getType(), feedMessage),
                                illegalAccessException);
                    }
                }
            }

            // test that conversion to and from json is non-lossy
            Assert.assertEquals(feedMessage.getText(),
                    JsonMessageMapper.jsonToObject(feedMessage.getText()).get().getText());
        }
    }

    @Test(dataProvider = "feedMessageTypeDataProvider")
    public <T extends FeedMessage> void testFeedMessageTypeIsNonLossyAsync(final Class<T> feedMessageType) {
        final ExecutorService executorService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < epochs*10; i++) {
            executorService.submit(() -> testFeedMessageTypeIsNonLossy(feedMessageType));
        }
        executorService.shutdown();
    }

    @DataProvider(name = "feedMessageTypeDataProvider")
    public Object[][] feedMessageTypeDataProvider() {
        return new Object[][] {
                {SubscribeMessage.class},
                {SubscriptionsMessage.class},
                {ErrorMessage.class},
                {TickerMessage.class},
                {HeartbeatMessage.class},
                {SnapshotMessage.class},
                {L2UpdateMessage.class},
                {ReceivedOrderMessage.class},
                {OpenedOrderMessage.class},
                {ClosedOrderMessage.class},
                {MatchedOrderMessage.class},
                {ChangedOrderMessage.class},
                {ActivatedOrderMessage.class},
        };
    }

    private boolean chance(final float p) {
        return random.nextDouble() < p;
    }
}
