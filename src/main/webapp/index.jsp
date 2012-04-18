<%--
  User: boycook
  Date: Jul 14, 2010
  Time: 6:17:26 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.bt.collaborate.service.provider.OrderProvider" %>
<%@ page import="com.bt.collaborate.domain.Order" %>
<html>
<head><title>BT - NEO</title></head>
<body>
<%
    for (Order order : OrderProvider.orders) {
%>
<h3>
    <%=order.getId() != null? order.getId(): "na"%> -
    <%=order.getDescription() != null? order.getDescription(): "na"%> -
    <%=order.getAEndDeviceId() != null? order.getAEndDeviceId(): "na"%> -
    <%=order.getAEndLocation() != null? order.getAEndLocation(): "na"%> -
    <%=order.getAEndLocationId() != null? order.getAEndLocationId(): "na"%> -
    <%=order.getAEndPortId() != null? order.getAEndPortId(): "na"%> -
    <%=order.getBandwidth() != null? order.getBandwidth(): "na"%> -
    <%=order.getZEndDeviceId() != null? order.getZEndDeviceId(): "na"%> -
    <%=order.getZEndLocation() != null? order.getZEndLocation(): "na"%> -
    <%=order.getZEndLocationId() != null? order.getZEndLocationId(): "na"%> -
    <%=order.getZEndShelfId() != null? order.getZEndShelfId(): "na"%> -
    <%=order.getZEndSlotId() != null? order.getZEndSlotId(): "na"%>
</h3>
<br/>
<%
    }
%>
</body>
</html>
