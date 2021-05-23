# TODO:
* Track TODO in `Issues` tab
* Implement all APIs
* Separate core definitions into another package like `MelonBarCore` or `MelonBarCommons`

---------------------------------
## Example usage:

#### Initialize Coinbase Pro HTTPS client:
```java
// initialized via factory method
final CoinbaseProClient coinbaseProClient = CoinbaseProClientFactory.createClient(
        API_KEY, API_PASSWORD, API_SECRET_KEY);

// ready for use!
...
coinbaseProClient.placeLimitOrder(...);
```

#### Initialize Websocket Feed client:
```java
// price tracker for live price updates
final PriceTracker priceTracker = new PriceTracker();

// initialized via builder
final CoinbaseProWebsocketFeedClient client = CoinbaseProWebsocketFeedClient.builder()
        // pass in pre-initialized trackers
        .withTrackers(priceTracker)
        // pass lambdas accepting String input message (more on this later)
        .withMessageHandlers(
                (message) -> log.info("got message: {}"))
        // specify channels to subscribe to
        .withChannels(
                Channel.of(ChannelType.TICKER),
                Channel.of(ChannelType.STATUS))
        // specify products to listen for on each channel 
        .withProducts(
                ProductId.ETH_USD,
                ProductId.BTC_USD)
        // default text buffer size is 1024 * 8 bytes 
        .withTextBufferSize(1024 * 8)
        // build
        .build();
```

#### Market order:
```java
// market order to buy 0.01 ETH using USD.
final MarketOrderRequest marketOrderRequest = MarketOrderRequest.builder()
        .side(OrderSide.BUY)
        .size(new BigDecimal("0.01"))
        .productId(Product.ETH_USD)
        .build();
final Response response = coinbaseProClient.placeMarketOrder(marketOrderRequest);
```

#### Limit order:
```java
// limit order to sell 0.01 ETH in exchange for USD.
final LimitOrderRequest limitOrderRequest = LimitOrderRequest.builder()
        .side(OrderSide.SELL)
        .size(new BigDecimal("0.01"))
        .productId(ProductId.ETH_USD)
        .build();
final Response response = coinbaseProClient.placeMarketOrder(marketOrderRequest);
```

#### (Awful) Trading strategy example:
```java
// see initialized websocket feed client and REST client above
...
new Thread(() ->{
    while (true) {
        // buy 100 ETH whenever 1 ETH <= 2 USD
        if (priceTracker.getPrice(ProductId.ETH_USD).compareTo(BigDecimal.TWO) <= 0) {
            coinbaseProClient.placeMarketOrder();
        }
    }
}).start();
```

---------------------------------
## Features:

```
TODO
```

---------------------------------

## Current structure:

### 1. API Contract _(Request Level)_ 
_NOTE: Coinbase Pro API is well-documented online (see: [link](https://docs.pro.coinbase.com/))._

All Coinbase Pro API requests follow a strict format, with defined query parameters,
required fields, optional fields (with interrelated rules), etc. We will define an
API contract that strictly follows the documentation. All request constraints and API
contract validation will be handled at _request_ level.

The current strategy is to define each request with explicitly-designated required
fields with validation functions, which are composite predicates of the request's
required and optional fields. The authority of input/query validation definitions is on the request
class.

### 2. Execution _(Execution Level)_
Before dispatching a given request to Coinbase Pro, whether it be simply querying for
accounts, or submitting an ambitious limit buy, preliminary steps are required.

By definition, each request is enforced to have its own validation function. All request-level
validation steps will occur, along with any custom constraints, which may be injected by the
_user_. In addition, there is some degree of authentication required by Coinbase (e.g. message 
signing).

In short, execution level can be broken up into steps:
1. Request content validation and custom constraints check
2. Generate URI and request body, referred to as the _enrichment_ step
2. Authentication

### 3. HTTP Client _(Dispatch Level)_
Builds and sends the HTTP request, and handles building the HTTP response. Any further
evaluation of the response should be done as post-processing.
