<%@ page isErrorPage="true" import="forseti.JUtil" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<title>Forseti</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<link href="../compfsi/estilos.css" rel="stylesheet" type="text/css">
</head>
<body bgcolor="#333333">
<div align="center">
  <p class="titGiganteNar"><%= JUtil.Msj("GLB","REGISTRO","SESION","ERROR-SESION", 1) %></p>
  <p class="titCuerpoBco"><%= JUtil.Msj("GLB","REGISTRO","SESION","ERROR-SESION", 2) %></p>
  <p class="titChico"><%= JUtil.Msj("GLB","REGISTRO","SESION","ERROR-SESION", 3) %></p>
</div>
</body>
</html>