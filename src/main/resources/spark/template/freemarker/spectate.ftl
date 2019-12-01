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
        <#if currentGames??>
            <h2>Current Games</h2><br>
            <#list currentGames?keys as key>
                <br>
                <form action="/spectate/game" method="GET">
                    <input type="hidden" name="gameID" value="${key}">
                    <input type="submit" value="${currentGames[key].redPlayer.name} v. ${currentGames[key].whitePlayer.name}">
                </form>
            </#list>
        </#if>






    </div>

</div>
</body>

</html>
