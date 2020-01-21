<%@ page contentType="text/html; charset=UTF-8"%>

<%@ include file="/includes/header.jsp"%>

	<style>
		td img {
			max-width: 150px;
			height: auto;
		}
	</style>
	
	<a href="seguridad/productos?accion=formulario">Nuevo Producto</a>
	
	<!-- DATATABLES -->
	<link rel="stylesheet" type="text/css"
		href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css" />
	<link rel="stylesheet" type="text/css"
		href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.dataTables.min.css" />
	
	<!-- JAVASCRIPT los incluimos en el pie-->
	
	<!-- TABLA -->
	
	<table class="tabla reponsive display">
		<thead>
			<tr>
				<th>ID</th>
				<th>Nombre</th>
				<th>Precio</th>
				<th>Imagen</th>
				<th>Descripción</th>
				<th>Descuento</th>
				<th>Usuario</th>
				<th>Categoría</th>
				<th>Acción</th>
			</tr>
		</thead>
	
		<tbody>
	
			<c:forEach items="${productos}" var="p">
				<tr>
					<td>${p.id}</td>
					<td>${p.nombre}</td>
					<td>${p.precio}</td>
					<td><img class="imagen_producto_tabla" src="${p.imagen}"></td>
					<td>${p.descripcion}</td>
					<td>${p.descuento}</td>
					<td>${p.usuario.nombre}</td>
					<td>${p.categoria.nombre}</td>
					<td><a href="seguridad/productos?accion=formulario&id=${p.id}">Editar</a></td>
				</tr>
			</c:forEach>
	
		</tbody>
	
		<tfoot>
			<tr>
				<th>ID</th>
				<th>Nombre</th>
				<th>Precio</th>
				<th>Imagen</th>
				<th>Descripción</th>
				<th>Descuento</th>
				<th>Usuario</th>
				<th>Categoría</th>
				<th>Acción</th>
			</tr>
		</tfoot>
	</table>




<%@ include file="/includes/footer.jsp"%>
