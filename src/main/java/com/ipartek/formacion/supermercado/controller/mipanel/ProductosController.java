package com.ipartek.formacion.supermercado.controller.mipanel;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.log4j.Logger;

import com.ipartek.formacion.supermercado.controller.Alerta;
import com.ipartek.formacion.supermercado.modelo.dao.CategoriaDAO;
import com.ipartek.formacion.supermercado.modelo.dao.ProductoDAO;
import com.ipartek.formacion.supermercado.modelo.dao.ProductoException;
import com.ipartek.formacion.supermercado.modelo.dao.UsuarioDAO;
import com.ipartek.formacion.supermercado.modelo.pojo.Categoria;
import com.ipartek.formacion.supermercado.modelo.pojo.Producto;
import com.ipartek.formacion.supermercado.modelo.pojo.Usuario;

/**
 * Servlet implementation class ProductosController
 */
@WebServlet("/mipanel/productos")
public class ProductosController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	private final static Logger LOG = Logger.getLogger(ProductosController.class);

	private static final String VIEW_TABLA = "productos/index.jsp";
	private static final String VIEW_FORM = "productos/formulario.jsp";
	private static String vistaSeleccionda = VIEW_TABLA;
	
	private static ProductoDAO daoProducto;
	private static UsuarioDAO daoUsuario;
	private static CategoriaDAO daoCategoria;
	
	private Usuario uLogeado;
	//acciones
	public static final String ACCION_LISTAR = "listar";
	public static final String ACCION_IR_FORMULARIO = "formulario";
	public static final String ACCION_GUARDAR = "guardar";   // crear y modificar
	public static final String ACCION_ELIMINAR = "eliminar";
	private boolean isRedirect;
	
	//Crear Factoria y Validador
	ValidatorFactory factory;
	Validator validator;
	
	
	//parametros
	String pAccion;	
	String pId;
	String pNombre;
	String pPrecio;
	String pImagen;
	String pDescripcion;
	String pDescuento;
	
	String pIdCategoria;
	
	
	
	@Override
	public void init(ServletConfig config) throws ServletException {		
		super.init(config);
		daoProducto = ProductoDAO.getInstance();
		daoUsuario = UsuarioDAO.getInstance();
		daoCategoria = CategoriaDAO.getInstance();
		
		factory = Validation.buildDefaultValidatorFactory();
		validator = factory.getValidator();
	}
      
	@Override
	public void destroy() {	
		super.destroy();
		daoProducto = null;
		daoUsuario = null;
		daoCategoria = null;
		
		factory = null;
		validator = null;
	}
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doAction(request, response);
	}

	private void doAction(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
			isRedirect = false;
	
			//recoger parametros
			pAccion = request.getParameter("accion");
			pId = request.getParameter("id");
			pNombre = request.getParameter("nombre");
			pPrecio = request.getParameter("precio");
			pImagen = request.getParameter("imagen");
			pDescripcion = request.getParameter("descripcion");
			pDescuento = request.getParameter("descuento");
						
			uLogeado = (Usuario)request.getSession().getAttribute("usuarioLogeado");
			
			//TODO agujero de seguridad, comprobar que el usuario de session sea el propietario del Producto
			
			pIdCategoria = request.getParameter("idCategoria");
			
			try {
				
				switch (pAccion) {
				case ACCION_LISTAR:
					listar(request, response);
					break;
				case ACCION_ELIMINAR:	
					eliminar(request, response);
					break;
				case ACCION_GUARDAR:	
					guardar(request, response);
					break;
				case ACCION_IR_FORMULARIO:	
					irFormulario(request, response);
					break;
				default:
					listar(request, response);
					break;
				}
				
				
			}catch (ProductoException e) {
				
				LOG.warn("Problema al visualizar el producto" + e);
				isRedirect = true;
							
				
			}catch (Exception e) {
				
				LOG.warn("Ha habido algún problema" + e);
				
			}finally {
				if ( isRedirect) {
					//invalidamos la session del usuario => ProductoException
					response.sendRedirect( request.getContextPath() + "/logout");	
				}else {
					request.getRequestDispatcher(vistaSeleccionda).forward(request, response);
				}	
			}
			
			
		
		
	}


	private void irFormulario(HttpServletRequest request, HttpServletResponse response) throws SQLException, ProductoException {
		
		Producto pEditar = new Producto();
		
		if ( pId != null ) {
			
			int id = Integer.parseInt(pId);
			//pEditar = daoProducto.getById(id);
			pEditar = daoProducto.getByIdByUser(id, uLogeado.getId() );
			
		}
		
		request.setAttribute("categorias", daoCategoria.getAll() );
		request.setAttribute("producto", pEditar );
		vistaSeleccionda = VIEW_FORM;
		
	}

	private void guardar(HttpServletRequest request, HttpServletResponse response) throws ProductoException {
		
		
		int pId = Integer.parseInt(request.getParameter("id"));
		float pPrecioFloat = Float.parseFloat(pPrecio);
		int pDescuentoInt = Integer.parseInt(pDescuento);
		int pIdCategoria = Integer.parseInt(request.getParameter("idCategoria"));
		
		Producto pGuardar = new Producto();
		pGuardar.setId(pId);
		pGuardar.setNombre(pNombre);
		pGuardar.setPrecio(pPrecioFloat);
		pGuardar.setImagen(pImagen);
		pGuardar.setDescripcion(pDescripcion);
		pGuardar.setDescuento(pDescuentoInt);
		
		Usuario u = new Usuario();
		u.setId(uLogeado.getId()); //Evitar que se envie el parametro desde el formulario
		pGuardar.setUsuario(u);
		
		Categoria c = new Categoria();
		c.setId(pIdCategoria); 
		pGuardar.setCategoria(c);
				
		
		Set<ConstraintViolation<Producto>> validaciones = validator.validate(pGuardar);
		if( validaciones.size() > 0 ) {			
			mensajeValidacion(request, validaciones);
		}else {	
		
				try {
				
					if ( pId > 0 ) {  // modificar
						LOG.trace("Modificar datos del producto");
						daoProducto.updateByUser(pId, uLogeado.getId(), pGuardar);	
						request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_PRIMARY, "Los datos del producto se han modificado correctamente"));
						
					}else {            // crear
						LOG.trace("Crear un registro un producto nuevo");
						daoProducto.create(pGuardar);
						request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_PRIMARY, "Producto nuevo añadido"));
					}
				
				}catch (ProductoException e) {
					LOG.error(e);
					
				}catch (Exception e) {  // validacion a nivel de base datos
					LOG.fatal(e);
					request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, "El nombre ya existe, selecciona otro"));
				}					
			
		}	
		
		request.setAttribute("categorias", daoCategoria.getAll() );
		request.setAttribute("producto", pGuardar);
		vistaSeleccionda = VIEW_FORM;
	
		
	}

	private void mensajeValidacion(HttpServletRequest request, Set<ConstraintViolation<Producto>> validaciones ) {

		StringBuilder mensaje = new StringBuilder();
		for (ConstraintViolation<Producto> cv : validaciones) {
			
			mensaje.append("<p>");
			mensaje.append(cv.getPropertyPath()).append(": ");
			mensaje.append(cv.getMessage());
			mensaje.append("</p>");
			
		}
		
		request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_DANGER, mensaje.toString() ));
		
	}

	private void eliminar(HttpServletRequest request, HttpServletResponse response) throws ProductoException, SQLException {
	
		// recibimos parámetros:
		int pId = (request.getParameter("id") == null) ? 0 : Integer.parseInt(request.getParameter("id"));

		if (pId > 0) { 

			Producto pEliminado = daoProducto.deleteByUser(pId, uLogeado.getId() );
			request.setAttribute("mensajeAlerta", new Alerta(Alerta.TIPO_PRIMARY, "Eliminado " + pEliminado.getNombre() ));
			LOG.info("Se ha eliminado " + pEliminado.getNombre());
			
			listar(request, response);
		}
		
	}

	private void listar(HttpServletRequest request, HttpServletResponse response) {
		
		ArrayList<Producto> productos = (ArrayList<Producto>) daoProducto.getAllByUser(uLogeado.getId());
		request.setAttribute("productos", productos );
		vistaSeleccionda = VIEW_TABLA;
		
	}
	
	

}
