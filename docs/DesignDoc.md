---
geometry: margin=1in
---
# PROJECT Design Documentation

> _The following template provides the headings for your Design
> Documentation.  As you edit each section make sure you remove these
> commentary 'blockquotes'; the lines that start with a > character
> and appear in the generated PDF in italics._

## Team Information
* Team name: A Team
* Team members
  * Alan Chen
  * Alec Jackson
  * Ash Nguyen
  * Nicholas Curl

## Executive Summary

Webcheckers allow users to play a game of American checkers against other users.

### Purpose
> _Provide a very brief statement about the project and the most
> important user group and user goals._

The project follows the rules of American checkers. Players should be able to play 
a game until someone wins/loses.

### Glossary and Acronyms
> _Provide a table of terms and acronyms._

| Term | Definition |
|------|------------|
| VO | Value Object |


## Requirements

This section describes the features of the application.

> _In this section you do not need to be exhaustive and list every
> story.  Focus on top-level features from the Vision document and
> maybe Epics and critical Stories._

Players are able to create and sign in with an unique username. After they are signed in, they 
will be able to challenge other signed-in players to a match. When players
are in a match, they will be able to move the pieces by drag-and-drop, and also receive 
assistance through the help menu if they are confused.
### Definition of MVP
> _Provide a simple description of the Minimum Viable Product._

Our MVP is when users are able to sign in and start a match.

### MVP Features
> _Provide a list of top-level Epics and/or Stories of the MVP._

* Sign in
* Sign out
* Start a match

### Roadmap of Enhancements
> _Provide a list of top-level features in the order you plan to consider them._
* Help menu
* Crowning

## Application Domain

This section describes the application domain.

![The WebCheckers Domain Model](Alec_Jackson_-_Domain_Model-page-001-1.jpg)

> _Provide a high-level overview of the domain for this application. You
> can discuss the more important domain entities and their relationship
> to each other._

* Player signs in on the home page.
* Player can join a match with other signed in players.
* The two players play a match of American checkers until one side loses/wins.
* The players make moves by moving the pieces.
* There are different types of moves that can be made, and a player can lose by having no moves/pieces left, or 
when he/she resigns from the match.

## Architecture and Design

This section describes the application architecture.

### Summary

The following Tiers/Layers model shows a high-level view of the webapp's architecture.

![The Tiers & Layers of the Architecture](architecture-tiers-and-layers.png)

As a web application, the user interacts with the system using a
browser.  The client-side of the UI is composed of HTML pages with
some minimal CSS for styling the page.  There is also some JavaScript
that has been provided to the team by the architect.

The server-side tiers include the UI Tier that is composed of UI Controllers and Views.
Controllers are built using the Spark framework and View are built using the FreeMarker framework.  The Application and Model tiers are built using plain-old Java objects (POJOs).

Details of the components within these tiers are supplied below.


### Overview of User Interface

This section describes the web interface flow; this is how the user views and interacts
with the WebCheckers application.

![The WebCheckers Web Interface Statechart](web-interface-placeholder.png)

> _Provide a summary of the application's user interface.  Describe, from
> the user's perspective, the flow of the pages in the web application._


### UI Tier
> _Provide a summary of the Server-side UI tier of your architecture.
> Describe the types of components in the tier and describe their
> responsibilities.  This should be a narrative description, i.e. it has
> a flow or "story line" that the reader can follow._

> _At appropriate places as part of this narrative provide one or more
> static models (UML class structure or object diagrams) with some
> details such as critical attributes and methods._

> _You must also provide any dynamic models, such as statechart and
> sequence diagrams, as is relevant to a particular aspect of the design
> that you are describing.  For example, in WebCheckers you might create
> a sequence diagram of the `POST /validateMove` HTTP request processing
> or you might show a statechart diagram if the Game component uses a
> state machine to manage the game._

> _If a dynamic model, such as a statechart describes a feature that is
> not mostly in this tier and cuts across multiple tiers, you can
> consider placing the narrative description of that feature in a
> separate section for describing significant features. Place this after
> you describe the design of the three tiers._



### Application Tier
> _Provide a summary of the Application tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._

* GameBoard displays a board with checkers pieces. The GameCenter connects the game 
board with the player lobby. The PlayerLobby is where it shows all of the signed in players.


### Model Tier
> _Provide a summary of the Application tier of your architecture. This
> section will follow the same instructions that are given for the UI
> Tier above._

* GameView shows the game board based on the player. The pieces, player, and rows all interact with each other in 
order to make moves.

### Design Improvements
> _Discuss design improvements that you would make if the project were
> to continue. These improvement should be based on your direct
> analysis of where there are problems in the code base which could be
> addressed with design changes, and describe those suggested design
> improvements. After completion of the Code metrics exercise, you
> will also discuss the resulting metric measurements.  Indicate the
> hot spots the metrics identified in your code base, and your
> suggested design improvements to address those hot spots._

* If the project were to continue, we could add a spectator mode or additional features 
in order to make the gameplay smoother.

## Testing
> _This section will provide information about the testing performed
> and the results of the testing._

* Unit test classes have been made for classes of different tiers. 

### Acceptance Testing
> _Report on the number of user stories that have passed all their
> acceptance criteria tests, the number that have some acceptance
> criteria tests failing, and the number of user stories that
> have not had any testing yet. Highlight the issues found during
> acceptance testing and if there are any concerns._

* 5 user stories have passed all of their acceptance criteria tests. 

### Unit Testing and Code Coverage
> _Discuss your unit testing strategy. Report on the code coverage
> achieved from unit testing of the code base. Discuss the team's
> coverage targets, why you selected those values, and how well your
> code coverage met your targets. If there are any anomalies, discuss
> those._
