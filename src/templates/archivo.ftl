<!DOCTYPE html>
<html lang="es_GT">
<head>
<meta charset="UTF-8">
<title>${titulo}</title>

<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-sRIl4kxILFvY47J16cr9ZwB07vP4J8+LH7qKQnuqkuIAvNWLzeN8tE5YBujZqJLB" crossorigin="anonymous">

</head>
<body style="background-color: #212529;">

<div class="container mt-4">

<h3 style="color: #ffffff;">${titulo}</h3>

<table class="table table-bordered">
	<thead>
		<tr>
            <#list columnas as col>
                <th>${col}</th>
            </#list>
        </tr>
	</thead>
	<tbody>
		<#list filas as fila>
            <tr>
                <#list columnas as col>
                    <td>${fila[col]!""}</td>
                </#list>
            </tr>
        </#list>
  	</tbody>
</table>

</div>

</body>
</html>