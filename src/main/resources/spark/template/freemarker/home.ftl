<!DOCTYPE html>

<head xmlns="http://www.w3.org/1999/html">
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
      <#if currentUser??>
          <form action="/replay" method="GET">
              <input type="submit" value="Replay a Game">
          </form>
          <br>
      </#if>


    <#if numPlayers??>
        Active Players: ${numPlayers}
        <br>
    </#if>

    <#if activePlayers??>
        <h2>Players Online</h2>
      <#list activePlayers?keys as key>
        <#if activePlayers[key].name != currentUser>
                <br>
                <form action="/game" method="GET">
                    <input type="submit" name="player" value=${activePlayers[key].name}>
                </form>

        </#if>
      </#list>
    </#if>


  </div>

</div>
</body>

</html>
