# TODO:
* AspectJ wiring
* TESTING
* Implement the rest of the APIs
* More javadoc
* De-lombok, too hacky

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