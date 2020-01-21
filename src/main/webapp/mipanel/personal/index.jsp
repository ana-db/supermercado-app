<%@ page contentType="text/html; charset=UTF-8" %>

<%@include file="/includes/header.jsp" %>

 
	<h1 class="text-info" id="top">Mi panel</h1>
		
		
	<!-- DATATABLES -->
	<link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/1.10.20/css/jquery.dataTables.min.css"/>
    <link rel="stylesheet" type="text/css" href="https://cdn.datatables.net/responsive/2.2.3/css/responsive.dataTables.min.css"/>
        
	<!-- JAVASCRIPT los incluimos en el pie-->
	
	<br>
	<h4>Bienvenido a tu panel, ${usuario.nombre}</h1>
	<br>
	
	
	
	
    <!-- TABLA -->
    <table class="tabla reponsive display">
	    <thead>
	        <tr>
	            <th>ID</th>
	            <th>Nombre</th>
	            <th>Contraseña</th>
	            <th>Rol</th>
	        </tr>
	    </thead>
	    
	    <tbody>
	     						
			<tr>
				<td>${usuario.id}</td>
				<td>${usuario.nombre}</td>
				<td>${usuario.contrasenia}</td>
				<td>${usuario.rol.nombre}</td>
			</tr>
								
		</tbody>
		          
		<tfoot>
	          <tr>
	              <th>ID</th>
				  <th>Nombre</th>
				  <th>Contraseña</th>
				  <th>Rol</th>
	          </tr>
		</tfoot>
 	</table>           
		
	        
	<!-- VENTANA MODAL BOOTSTRAP PARA EDITAR CONTRASEÑA -->
	<!-- Button trigger modal -->
	<button type="button" class="btn btn-outline-danger" data-toggle="modal" data-target="#exampleModal">Editar contraseña</button>
	
	<!-- Modal -->
	<div class="modal fade" id="exampleModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
	  <div class="modal-dialog" role="document">
	    <div class="modal-content">
	      <div class="modal-header">
	        <h5 class="modal-title" id="exampleModalLabel">Editar contraseña</h5>
	        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
	          <span aria-hidden="true">&times;</span>
	        </button>
	      </div>
	      <div class="modal-body">
	        
				<!-- FORMULARIO PARA EDITAR CONTRASEÑA -->
				<form action="mipanel/personal" method="post">
	
					<div class="form-group">
						<!-- necesitamos enviar la acción para que el controlador sepa a qué case del switch tiene que entrar -->	
						<input type="hidden" 
								name=accion
								value="guardar">
					</div>
					
					<div class="form-group">
				        <label for="id">ID</label>
				        <input type="number" 
				               class="form-control" 
				               name="id" id="id" 
				               required
				               readonly
				               value = "${usuario.id}"
				               placeholder="Identificador del usuario"
				               pattern="[0-9]"
				               min="0" max="100"
				               aria-describedby="idHelp">
				        <small id="idHelp" class="form-text text-muted">Identificador del usuario</small>
					</div>
					
					<div class="form-group">
				        <label for="nombre">Usuario</label>
				        <input type="text" 
				               class="form-control" 
				               name="nombre" id="nombre" 
				               required
				               readonly
				               value = "${usuario.nombre}"
				               placeholder="Mínimo 2 Máximo 50 caracteres"
				               aria-describedby="nombreHelp">
				    </div>
					
					<div class="form-group">
				        <label for="contrasenia">Nueva contraseña</label>
				        <input type="password" 
				               class="form-control" 
				               name="contrasenia" id="contrasenia" 
				               required
				               value = "${usuario.contrasenia}"
				               placeholder="Mínimo 2 Máximo 50 caracteres"
				               aria-describedby="contraseniaHelp">
				    </div>	
	
				    <input type="submit" class="btn btn-block btn-outline-primary" value="Modificar"}">   
				</form>

	      </div>
	      <div class="modal-footer">
	        <button type="button" class="btn btn-secondary" data-dismiss="modal">Cerrar</button>
	        <!--  <a class="btn btn-danger" href="mipanel/personal?accion=formulario&id=${usuario.id}&accion=guardar">Editar</a>  -->
	      </div>
	    </div>
	  </div>
	</div>


	
	
	
	
	

	<a id="btn-top" href="#top" class="btn btn-primary">top</a>

<%@include file="/includes/footer.jsp" %>    