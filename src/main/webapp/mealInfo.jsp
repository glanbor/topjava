<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meal</title>
    <style>
        input {
            float: left;
            width: 20%;
            margin-top: 6px;
            background-color: paleturquoise;
        }

        label {
            float: left;
            width: 7%;
            margin-top: 6px

        }

        button {
            background-color: aquamarine;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>${param.action== "create" ? "Create meal" : "Update meal"}</h2>
<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<br>

<form method="post" action="meals">
    <input type="hidden" name="id" value="${meal.id}">
    <label for="dateTime">DateTime: </label>
    <input type="datetime-local" name="dateTime" value="${meal.dateTime}" id="dateTime" required>
    <br><br>
    <label for="description">Description: </label>
    <input type="text" name="description" value="${meal.description}" placeholder="Meal description" id="description"
           required>
    <br><br>
    <label for="calories">Calories: </label>
    <input type="number" name="calories" value="${meal.calories}" placeholder="Calories amount" id="calories" required>
    <br><br><br><br>
    <button type="submit">Save</button>
    <button onclick="window.history.back()">Cancel</button>
</form>

</body>
</html>