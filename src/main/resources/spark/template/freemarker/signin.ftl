<!DOCTYPE html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"></meta>
    <meta http-equiv="refresh" content="10">
    <title>${title} | Web Checkers </title>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
<div class="page">

    <h1>Web Checkers | ${title}</h1>

    <#include "nav-bar.ftl" />

        <div class="body">
            <form action='./signin' method="POST">
                User Name:<br>
                <input type="text" name="username"><br><br>
                <input type="submit" value="Submit">

            </form>
        </div>
</div>
</body>

</html>


