<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>

<table border="1">
    <thead>
    <tr>
        <th>spz</th>
        <th>model</th>
        <th>cena</th>
        <th>dostupnost</th>
    </tr>
    </thead>
    <c:forEach items="${cars}" var="car">
        <tr>
            <td><c:out value="${car.licencePlate}"/></td>
            <td><c:out value="${car.model}"/></td>
            <td><c:out value="${car.rentalPayment}"/></td>
            <td><c:out value="${car.status}"/></td>
            <td>
                <form method="post" action="${pageContext.request.contextPath}/cars/delete?id=${car.ID}"
                      style="margin-bottom: 0;"><input type="submit" value="Smazat"></form>
            </td>
        </tr>
    </c:forEach>
</table>

<h2>Zadejte auto</h2>
<c:if test="${not empty chyba}">
    <div style="border: solid 1px red; background-color: yellow; padding: 10px">
        <c:out value="${chyba}"/>
    </div>
</c:if>
<form action="${pageContext.request.contextPath}/cars/add" method="post">
    <table>
        <tr>
            <th>spz auta:</th>
            <td><input type="text" name="licencePlate" value="<c:out value='${param.licencePlate}'/>"/></td>
        </tr>
        <tr>
            <th>model:</th>
            <td><input type="text" name="model" value="<c:out value='${param.model}'/>"/></td>
        </tr>
        <tr>
            <th>cena:</th>
            <td><input type="text" name="rentalPayment" value="<c:out value='${param.rentalPayment}'/>"/></td>
        </tr>
        <tr>
            <th>status:</th>
            <td><input type="text" name="status" value="<c:out value='${param.status}'/>"/></td>
        </tr>
    </table>
    <input type="Submit" value="Zadat"/>
</form>

</body>
</html>