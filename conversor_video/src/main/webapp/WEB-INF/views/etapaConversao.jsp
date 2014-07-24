<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false" %>
<html>
<head>
	<title>Conversor de Vídeos</title>
</head>
<body>
<h1 class="tituloTela"> Conversor de Vídeos 
</h1>
	<br>
	<h2 class="subtituloTela">${etapaConversao.subtitulo}</h2>
	<p>${etapaConversao.msgErro}</p>
	<p>${etapaConversao.msgSucesso}</p>
	<p>${etapaConversao.backTraceEtapas}</p>
	<p>${etapaConversao.instrucao}</p>
	<form:form method="POST" modelAttribute="etapaConversao" action="${etapaConversao.acao}" enctype="multipart/form-data"> 
	<table class="bloco">
		<tr>
			<td width="100">Arquivo local:</td>
			<td><form:input id="file" name="file" path="${etapaConversao.pathArquivoLocal}" class="inputFile" type="file" /></td>
		</tr>
		<tr>
			<td colspan=2 align="center">
				<input type="submit" value="Etapa Anterior">
				<c:if test="${etapaConversao.idEtapa ==1}"><input type="submit" value="Guardar"></c:if>
				<c:if test="${etapaConversao.idEtapa ==2}"><input type="submit" value="Converter"></c:if>
				<c:if test="${etapaConversao.idEtapa ==3}"><input type="submit" value="Visualizar"></c:if>
			</td>
		</tr>
	</table>
	</form:form>
</body>
</html>
