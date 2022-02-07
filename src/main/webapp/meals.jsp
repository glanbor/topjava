<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        .excess {
            color: red;
        }

        .notExcess {
            color: green;
        }

        table {
            border: solid black;
            table-layout: fixed;
            border-collapse: collapse;
            text-align: left;
        }
        th {
            border: solid black;
            text-align: center;
            padding: 10px;
        }
        td {
            border: solid black;
            padding: 10px;
        }

    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<br>
<a href="meals?action=create">Add Meal</a>
<br><br>
<table>
    <thead>
    <tr>
        <th>Id</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    </thead>
    <c:forEach var="meal" items="${meals}">
        <tr class="${meal.excess ? "excess" : "notExcess"}">
            <td>${meal.id}</td>
            <td>
                <fmt:parseDate value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime" type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/>
            </td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a> </td>
            <td><a href="meals?action=delete&id=${meal.id}">Delete</a> </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>

