package com.melonbar.exchange.coinbase.websocket.processing;

import com.melonbar.exchange.coinbase.util.Guard;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.MessageHandler;
import java.util.Arrays;
import java.util.LinkedList;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AggregatedMessageHandler<T> implements MessageHandler.Whole<T> {

    private final LinkedList<MessageHandler.Whole<T>> messageHandlers = new LinkedList<>();

    public static <T> AggregatedMessageHandler<T> init() {
        return new AggregatedMessageHandler<>();
    }

    @SafeVarargs
    public static <T> AggregatedMessageHandler<T> aggregate(final MessageHandler.Whole<T> ... messageHandlers) {
        Guard.nonNull(messageHandlers);
        final AggregatedMessageHandler<T> aggregatedMessageHandler = init();
        Arrays.stream(messageHandlers).forEach(aggregatedMessageHandler::addMessageHandler);
        return aggregatedMessageHandler;
    }

    @Override
    public void onMessage(final T message) {
        synchronized (messageHandlers) {
            for (final MessageHandler.Whole<T> handler : messageHandlers) {
                handler.onMessage(message);
            }
        }
    }

    public void addMessageHandler(final MessageHandler.Whole<T> messageHandler) {
        if (messageHandler instanceof AggregatedMessageHandler) {
            log.warn("Adding handler [{}] to itself will cause recursive message handler invocations!",
                    this.getClass().getName());
        }
        synchronized (messageHandlers) {
            messageHandlers.add(messageHandler);
        }
    }


}
