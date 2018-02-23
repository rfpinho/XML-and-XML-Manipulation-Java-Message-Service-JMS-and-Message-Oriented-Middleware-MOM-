<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
<xsl:template match="/">
	<html>
		<head>
			<meta charset="UTF-8"/>
			<title>Jogos Olímpicos</title>
			<link rel="stylesheet" media="all" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css"/>
			<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
		</head>
		<body>
			<div class="container">
			<h1><center>Jogos Olímpicos</center></h1>
			</div>
			<xsl:apply-templates/>
		</body>
	</html>
</xsl:template>

<xsl:template match="pais">
<html>
  <body>
	 <hr style="width: 100%; color: black; height: 1px; background-color:black;"/>
     <div class="container">
	  <table class="table">
		<tbody>
			<tr bgcolor="#329932">
				<td><xsl:value-of select="ranking"/></td>
				<td><xsl:value-of select="abreviatura"/></td>
				<td><xsl:value-of select="@nome"/></td>
				<td>Ouro: <xsl:value-of select="medalhasOuro"/></td>
				<td>Prata: <xsl:value-of select="medalhasPrata"/></td>
				<td>Bronze: <xsl:value-of select="medalhasBronze"/></td>
				<td>Total: <xsl:value-of select="totalMedalhas"/></td>
			</tr>
		</tbody>
	  </table>
	</div>
	
	<xsl:if test="count(ouro/atleta)>0">
		<div class="container">
		  <table class="table">
			<thead>
			  <tr bgcolor="#FFDF00">
				<th>Modalidade</th>
				<th>Categoria</th>
				<th>Nome</th>
			  </tr>
			</thead>
			<tbody>
			  <xsl:for-each select="ouro/atleta">
				<tr>
					<td><xsl:value-of select="modalidade"/></td>
					<td><xsl:value-of select="categoria"/></td>
					<td><xsl:value-of select="nome"/></td>
				</tr>
			</xsl:for-each>
			</tbody>
		  </table>
		</div>
	</xsl:if>
	
	<xsl:if test="count(prata/atleta)>0">
		<div class="container">
		  <table class="table">
			<thead>
			  <tr bgcolor="#C0C0C0">
				<th>Modalidade</th>
				<th>Categoria</th>
				<th>Nome</th>
			  </tr>
			</thead>
			<tbody>
			  <xsl:for-each select="prata/atleta">
				<tr>
					<td><xsl:value-of select="modalidade"/></td>
					<td><xsl:value-of select="categoria"/></td>
					<td><xsl:value-of select="nome"/></td>
				</tr>
			  </xsl:for-each>
			</tbody>
		  </table>
		</div>
	</xsl:if>
	
	<xsl:if test="count(bronze/atleta)>0">
		<div class="container">
		  <table class="table">
			<thead>
			  <tr bgcolor="#CD7F32">
				<th>Modalidade</th>
				<th>Categoria</th>
				<th>Nome</th>
			  </tr>
			</thead>
			<tbody>
			  <xsl:for-each select="bronze/atleta">
				<tr>
					<td><xsl:value-of select="modalidade"/></td>
					<td><xsl:value-of select="categoria"/></td>
					<td><xsl:value-of select="nome"/></td>
				</tr>
			</xsl:for-each>
			</tbody>
		  </table>
		</div>
	</xsl:if>
	</body>
</html>
</xsl:template>

</xsl:stylesheet>