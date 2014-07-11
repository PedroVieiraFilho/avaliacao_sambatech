<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<html>
<head>
	<title>Conversor de V�deos</title>
</head>
<body>
<h1 class="tituloTela"> Conversor de V�deos 
</h1>
	<br>
	<h2 class="subtituloTela">Armazenamento de V�deo </h2>
	<P><label class="instrucao">Informe a url contendo o v�deo (ou selecione um arquivo local) e clique em Guardar para coloc�-lo na nuvem.</label></p>
	<table class="bloco">
		<tr>
			<td width="100">Arquivo local:</td>
			<td><input class="inputFile" type="file"></td>
		</tr>
		<tr>
			<td>Arquivo da web (url):</td>
			<td><input class="inputUrl" type="url" size="100"></td>
		</tr>
		<tr>
			<td>Descri��o</td>
			<td><textarea class="textarea" rows="5" cols="80"></textarea></td>
		<tr>
			<td colspan=2 align="center"><input type="button" value="Guardar"></td>
		</tr>
	</table>
	<h2 class="subtituloTela">Arquivos dispon�veis para convers�o ou visualiza��o</h2>
	<P><label class="instrucao">Para visualizar um v�deo, clique no �cone para convers�o.</label></p>
	<table class="tabela" border="1">
		<tr>
			<td><b>Nome original</b></td>
			<td><b>Arquivo convertido</b></td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
			<td>&nbsp;</td>
		<tr>
		<!-- Aqui ser� o loop para cada arquivo existente na nuvem. Trecho de exemplo: -->
		<tr>
			<td>sample.dv</td>
			<td>sample.mp4</td>
			<td><img src="${pageContext.servletContext.contextPath}/resources/convert.gif" border="0" width="45" height="32" alt="Converter"/></td>
			<!-- Ir� aparecer somente se o arquivo foi convertido -->
				<td><img src="${pageContext.servletContext.contextPath}/resources/play.gif" border="0" width="32" height="32" alt="Visualizar"/></td> 
			<td><img src="${pageContext.servletContext.contextPath}/resources/download.gif" border="0" width="32" height="32" alt="Download"></td>
			<!-- Apagar� ambos os arquivos (original e convertido) -->
			<td><img src="${pageContext.servletContext.contextPath}/resources/excluir.jpg" border="0" width="32" height="32" alt="Excluir"></td>
		</tr>		
	</table>
</body>
</html>
