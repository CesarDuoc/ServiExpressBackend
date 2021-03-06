package com.serviexpress.apirest.controller;

import java.util.List;

import javax.validation.Valid;

import com.serviexpress.apirest.entity.Producto;
import com.serviexpress.apirest.payload.response.ProductoDTO;
import com.serviexpress.apirest.service.impl.ProductoServicesImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/producto")
public class ProductoController {

	@Autowired
	@Qualifier("serviProducto")
	ProductoServicesImpl productoServicesImpl;



	@PutMapping("/producto")
	public ResponseEntity<?> agregarCategoria(@RequestBody @Valid ProductoDTO productoDTO) {
		Producto producto=new Producto();
		producto.setCategoria(productoDTO.getCategoria());
		producto.setDescripcion(productoDTO.getDescripcion());
		producto.setNombre(productoDTO.getNombre());
		producto.setStock(productoDTO.getStock());
		producto.setValorbase(productoDTO.getValorbase());
		return ResponseEntity.ok(productoServicesImpl.crear(producto));

	}

	@PostMapping("/producto")
	public ResponseEntity<?> actualizarCategoria(@RequestBody @Valid ProductoDTO productoDTO) {
		Producto producto=new Producto();
		producto.setCategoria(productoDTO.getCategoria());
		producto.setDescripcion(productoDTO.getDescripcion());
		producto.setNombre(productoDTO.getNombre());
		producto.setStock(productoDTO.getStock());
		producto.setValorbase(productoDTO.getValorbase());
		producto.setIdproducto(productoDTO.getIdproducto());
		return ResponseEntity.ok(productoServicesImpl.actualizar(producto));
	}

	@GetMapping(value = "/{idCategoria}")
	public List<Producto> obtenerProductoByCategoria(Pageable pageable, @PathVariable(value = "idCategoria") Long idCategoria) {
		
		return productoServicesImpl.obtenerPorPaginacion(pageable,idCategoria);
	}

	@GetMapping("/producto/{idproducto}")
	public Producto show(@PathVariable Long idproducto){
		
		return productoServicesImpl.findByIdProducto(idproducto);
	}

	@GetMapping(value = "/productos")
	public List<Producto> getAll() {
		return productoServicesImpl.obtener();
	}


	@GetMapping(value = "/allproducto")
	public List<Producto> allServicios(Pageable pageable) {
		return productoServicesImpl.obtenerPorPaginacion(pageable);
	}


}