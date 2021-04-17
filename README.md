# TODO:
1. annotation processing between enrichRequest and execution level
2. javadoc
3. unit testing

---------------------------------
## Current idea:

### 1. API Contract _(Request Level)_ 
_NOTE: Coinbase Pro API is well-documented online (see: [link](https://docs.pro.coinbase.com/))._

All Coinbase Pro API requests follow a strict format, with defined query parameters,
required fields, optional fields (with interrelated rules), etc. We will define an
API contract that strictly follows the documentation. All enrichRequest constraints and API
contract validation will be handled at _request_ level.

The current strategy is to define each enrichRequest with explicitly-designated required
fields with validation functions, which are composite predicates of the enrichRequest's
required and optional fields. The authority of input/query validation definitions is on the enrichRequest
class.

### 2. Execution _(Execution Level)_
Before dispatching a given enrichRequest to Coinbase Pro, whether it be simply querying for
accounts, or submitting an ambitious limit buy, preliminary steps are required.

By definition, each enrichRequest is enforced to have its own validation function. All enrichRequest-level
validation steps will occur, along with any custom constraints, which may be injected by the
_user_. In addition, there is some degree of authentication required by Coinbase (e.g. message 
signing).

In short, execution level can be broken up into two steps:
1. Request content validation and custom constraints check
2. Authentication

### 3. HTTP Client _(Dispatch Level)_
Should be pretty bare bones, mostly in that the HTTP client implementation
may be very generic. In other words, it could be used to handle generic HTTP requests.

The current idea is to have enrichRequest validation done before enrichRequest dispatch.
Having the "execution layer" will provide more abstraction from which
we can do this (overall this will make our client more flexible and powerful).