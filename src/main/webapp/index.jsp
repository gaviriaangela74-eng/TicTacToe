<jsp:useBean id="gameBean" scope="session" class="game.GameBean" />
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Tres en Raya</title>
</head>
<body>
<h1>Tres en Raya</h1>

<form action="EntryServlet" method="post">
    <input type="submit" name="User" value="Tú empiezas"><br/><br/>
    <input type="submit" name="Computer" value="El computador empieza">
</form>

</body>
</html>