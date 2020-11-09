---
output:
  html_document: default
  pdf_document: default
---

# CSC207H1 Y - Software Design Project Phase 2

This is a project allowing trading of items among users of this system. It uses a textUI, the user selects options by entering
corresponding number in the prompt displayed.

## Getting Started

In IntelliJ, clone Group repository URL https://markus.teach.cs.toronto.edu/git/csc207-2020-05/group_0025.
Start by running class Main.

## Authors

**Group_0267:**  

- Ran (Megan) Shi  
- Yin (Luke) Liu      
- Chenhao (William) Wang  
- David Anugraha

## Usage
#### Login and Registration Guide:
This system allows people to register a new user account with username and password, if the username has not been taken. 
We can log in as a user if we have a user account, or as an administrator if we have an administrator account.
You can also choose to log in as a guest, which allows people to explore the system but not interact with it.

#### Admin Guide:
An administrator has a username and a password, in which we can edit them after logging in. Currently, we have 
already created 3 administrative accounts in the system. You can log in as an admin using the username "admin1"
with corresponding password "admin".

An administrator is able to:
* View all administrators in the system.
* Register new administrators in the system by providing new username and new password.
* Change thresholds for the business rules in trading (such as maximum number of cancelled trades, limit trades in
one week, maximum number of borrowing and lending difference, minimum length of password, and maximum number
of edits per meeting).
* View all users in the system and manage them by changing their status. If a user tries to violate 
the rules of the system, the user will be "Flagged", and an admin can decide whether the user should be frozen 
("Frozen") or restore the user's status ("Good").
* View all items in the system and manage the items by approving "Requested" items to be added (into "Available") or
remove inappropriate items from the system.

(Note about a special case: An admin can remove an item which is requested to trade by any user, however, 
the trade will be automatically rejected)

#### User Guide:
A user has username, password, email address, city address and status. Users can view and edit their information 
after logging in, however, the user will be notified when changing the user's city address if
there is an ongoing trade for the user.

A user is able to:
* Change the status into "Vacation" if a user status is "Good". A user with "Vacation" status cannot trade and unable
to receive trade requests from other users. Therefore, the user will be notified if there is an ongoing trade.
* Store items that the user wants to borrow in the inventory and store items that the user wants to lend in
the wishlist.
* View other users, together with their inventory and wishlist. In addition, the user can add items
in other users' inventory or wishlist, to the user's wishlist. 
*Add or remove items to the inventory or wishlist, however, in order for the items to be available to be traded, 
the user must wait for an admin's approval for the item.
* View all items and add items to the wishlist.
* Borrow, lend, or exchange items with other users.

A user can borrow from, lend to or exchange items with other users by requesting trade to other
users". A user can only trade with other users in the same city, in addition to the assumption that 
by making the trade, the user will not violate any of the trading rules.

In order to trade, a user can either search another user by username, search by item, or the system can 
recommend best trades automatically. The user will also provide the duration for the trade. A trade with status 
"Requested" will be created after you request a trade to another user.

If the other user accepts the trade, the status of the trade will become "Accepted", otherwise the status will become 
"Rejected". Once the user accepts the trade, the items involved in trade will become unavailable and 
the user will suggest an initial meeting place and time. Both users are able to suggest place and time for 
the meeting until they have reached an agreement. If the users cannot decide meeting places and time in a 
given number of turns, the trade will be automatically cancelled. 
After two users agree on a meeting, the trade status will become "Confirmed". 

After users meet in real life based on the meeting time and place, they will have to confirm their meeting, 
which changes the trade status to either "ReceiverConfirmed" or "RequesterConfirmed" depending on which
user confirms first. After both users have confirmed, the trade will become "Completed". If the trade is permanent,
then the items will become available again. However, if the trade is temporary, a new trade will be created where
the items involved in the previous trade should be returned. Both users will start from the "Accepted" phase,
however, both users cannot edit the time (since it is fixed). Cancellation of the temporary trade will result in
both users getting "Flagged".

If the user violates the rules set by the administrators, the user's status may become "Flagged" or "Frozen". 
The user cannot trade with this status, however, the user may send a request to the administrator to 
restore the user's status.

## Design Patterns Used:

These are the design patterns that are used in this program:
#### 1. Builder design pattern

We use SystemBuilder, which implements the Builder design pattern, in order to build systems in the controllers
that have complicated constructor. The construction of each system, which involves different number of use case 
classes, will be handled in the SystemBuilder. In addition, we would only need to read the .ser file from
the SystemBuilder. Therefore, in order to instantiate the systems, we do not need to 
do the reading from gateway in different parts of the file, and we do not need to construct complicated systems.

#### 2. Facade design pattern

We use AdminMainMenuSystem and UserMainMenuSystem, which both act like a facade where run methods for other controllers
will be called only from those classes.

#### 3. Strategy design pattern

* We use IAccountManager which has methods for managing usernames and passwords. We can switch
between Managers to deal with username/password of User or Admin Account since both of them 
have username and password.
* We use RequestTradeAlgorithm which consists of subclasses for different strategies in requesting a Trade
(either borrow/lend trade (RequestOneWayTradeAlgorithm) or borrowing and lending trade (RequestTwoWayTradeAlgorithm)).

In addition, we use dependency injection for most of our methods' parameter(s) by relying on abstraction
(using interface or abstract classes), which will provide flexibility in doing future extensions.
