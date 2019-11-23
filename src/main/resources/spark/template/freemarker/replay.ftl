<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>Web Checkers | ${title}</title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>

<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <!-- Provide a navigation bar -->
    <#include "nav-bar.ftl" />

    <div class="body">



        <!-- Provide a message to the user, if supplied. -->
        <#include "message.ftl" />
        <br>
        <form action="/" method="get">
            <input type="submit" value="Go Back">
        </form>
        <br>
        <#if savedGames??>
            <h2>Saved Games</h2><br>
            <#list savedGames?keys as key>
                <br>
                <form action="/replay/game" method="GET">
                    <input type="hidden" name="gameID" value="${key}">
                    <input type="submit" value="${savedGames[key].gameBoard.redPlayer.name} v. ${savedGames[key].gameBoard.whitePlayer.name}">
                </form>
            </#list>
        </#if>






    </div>

</div>
</body>

</html>
