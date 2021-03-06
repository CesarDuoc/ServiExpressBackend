package com.serviexpress.apirest.controller;


import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import com.serviexpress.apirest.entity.Cliente;
import com.serviexpress.apirest.entity.Producto;
import com.serviexpress.apirest.entity.Reserva;
import com.serviexpress.apirest.entity.Servicio;
import com.serviexpress.apirest.entity.User;
import com.serviexpress.apirest.entity.Vehiculo;
import com.serviexpress.apirest.payload.ReservaRequest;
import com.serviexpress.apirest.payload.ReservaResponse;
import com.serviexpress.apirest.payload.response.IngresoVsEgreso;
import com.serviexpress.apirest.payload.response.ReporteServicio;
import com.serviexpress.apirest.payload.response.ResponseReservaPago;
import com.serviexpress.apirest.service.EmailService;
import com.serviexpress.apirest.service.MyBatisService;
import com.serviexpress.apirest.service.impl.ProductoServicesImpl;
import com.serviexpress.apirest.service.impl.ReporteInServicesImpl;
import com.serviexpress.apirest.service.impl.ReservaServicesImpl;
import com.serviexpress.apirest.service.impl.ServicioServicesImpl;

import org.jose4j.json.internal.json_simple.JSONArray;
import org.jose4j.json.internal.json_simple.JSONObject;
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

import com.serviexpress.apirest.repository.ClienteRepository;
import com.serviexpress.apirest.repository.ProductoRepository;
import com.serviexpress.apirest.repository.ReservaRepository;
import com.serviexpress.apirest.repository.ServicioRepository;
import com.serviexpress.apirest.repository.UserRepository;
import com.serviexpress.apirest.repository.VehiculoRepository;

import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/reserva")
public class ReservaController {

	@Autowired
	MyBatisService myBatisService;

	@Autowired
	EmailService emailService;

	@Autowired
	ReservaServicesImpl reservaServicesImpl;

	@Autowired
	@Qualifier("repositorioproducto")
	private ProductoRepository productoRepository;

	@Autowired
	@Qualifier("repositorioservicio")
	private ServicioRepository servicioRepository;

	@Autowired
	@Qualifier("repositoriovehiculo")
	private VehiculoRepository vehiculoRepository;

	@Autowired
	@Qualifier("serviProducto")
	ProductoServicesImpl productoServicesImpl;

	@Autowired
	@Qualifier("serviServicio")
	ServicioServicesImpl servicioServicesImpl;

	@Autowired
	@Qualifier("servireportein")
	ReporteInServicesImpl reporteInServicesImpl;

	@Autowired
	private ReservaRepository repositorio;

	@Autowired
	UserRepository userRepository;

	@Autowired
	@Qualifier("repositoriocli")
	private ClienteRepository clienteRepository;

	// Cliente
	@PutMapping("/reserva")
	public ResponseEntity<?> agregarReserva(@RequestBody @Valid final ReservaRequest reserva) {
		Reserva res = new Reserva();
		res.setActivo(false);
		res.setEstado(reserva.getEstado());
		res.setFecha(reserva.getFecha());
		res.setHorareserva(reserva.getHorareserva());
		res.setIdcliente(reserva.getIdcliente());
		res.setIdvehiculo(reserva.getIdvehiculo());
		res.setProductos(reserva.getProductos());
		res.setServicios(reserva.getServicios());
		res.setTotalreserva(reserva.getTotalreserva());
		return ResponseEntity.ok(reservaServicesImpl.crear(res));

	}

	@PostMapping("/reserva")
	public ResponseEntity<?> actualizarReserva(@RequestBody @Valid final ReservaRequest reserva) {
		Reserva res = new Reserva();
		res.setActivo(false);
		res.setEstado(reserva.getEstado());
		res.setFecha(reserva.getFecha());
		res.setHorareserva(reserva.getHorareserva());
		res.setIdcliente(reserva.getIdcliente());
		res.setIdvehiculo(reserva.getIdvehiculo());
		res.setProductos(reserva.getProductos());
		res.setServicios(reserva.getServicios());
		res.setIdreserva(reserva.getIdreserva());
		res.setTotalreserva(reserva.getTotalreserva());
		return ResponseEntity.ok(reservaServicesImpl.actualizar(res));
	}

	// todas las reservas empleado por fecha hoy

	@GetMapping(value = "/reservasday")
	public ResponseEntity<?> obtenerReservaDay(final Pageable pageable) {

		List<Reserva> reserva = reservaServicesImpl.obtenerPorDay(pageable);
		JSONArray array = new JSONArray();

		for (Reserva reserva2 : reserva) {
			ReservaResponse reservaResponse = new ReservaResponse();
			reservaResponse.setIdreserva(reserva2.getIdreserva());

			long num = Long.parseLong(reserva2.getServicios());
			Servicio servicio = servicioRepository.findById(num)
					.orElseThrow(() -> new IllegalStateException("Servicio no existe."));
			reservaResponse.setServicios(servicio.getNombre());

			long num2 = Long.parseLong(reserva2.getProductos());
			Producto producto = productoRepository.findById(num2)
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));
			reservaResponse.setProductos(producto.getNombre());

			reservaResponse.setHorareserva(reserva2.getHorareserva());
			reservaResponse.setFechareserva(reserva2.getFecha());

			Vehiculo vehiculo2 = vehiculoRepository.findById(reserva2.getIdvehiculo())
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));

			reservaResponse.setVeichulo(vehiculo2.getTipovehiculo());
			reservaResponse.setMarca(vehiculo2.getMarca());
			reservaResponse.setPatente(vehiculo2.getPatente());

			reservaResponse.setEstado(reserva2.getEstado());
			array.add(reservaResponse);

		}

		return ResponseEntity.ok(array);
	}

	@GetMapping(value = "/reservasmonth")
	public ResponseEntity<?> obtenerReservaMonth(final Pageable pageable) {
		List<Reserva> reserva = reservaServicesImpl.obtenerPorMonth(pageable);
		JSONArray array = new JSONArray();

		for (Reserva reserva2 : reserva) {
			ReservaResponse reservaResponse = new ReservaResponse();
			reservaResponse.setIdreserva(reserva2.getIdreserva());

			long num = Long.parseLong(reserva2.getServicios());
			Servicio servicio = servicioRepository.findById(num)
					.orElseThrow(() -> new IllegalStateException("Servicio no existe."));
			reservaResponse.setServicios(servicio.getNombre());

			long num2 = Long.parseLong(reserva2.getProductos());
			Producto producto = productoRepository.findById(num2)
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));
			reservaResponse.setProductos(producto.getNombre());

			reservaResponse.setHorareserva(reserva2.getHorareserva());
			reservaResponse.setFechareserva(reserva2.getFecha());

			Vehiculo vehiculo2 = vehiculoRepository.findById(reserva2.getIdvehiculo())
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));

			reservaResponse.setVeichulo(vehiculo2.getTipovehiculo());
			reservaResponse.setMarca(vehiculo2.getMarca());
			reservaResponse.setPatente(vehiculo2.getPatente());

			reservaResponse.setEstado(reserva2.getEstado());
			array.add(reservaResponse);

		}

		return ResponseEntity.ok(array);
	}

	// todas las reservas
	@GetMapping(value = "/reservas")
	public ResponseEntity<?> obtenerReserva(final Pageable pageable) {
		List<Reserva> reserva = reservaServicesImpl.obtenerPorPaginacion(pageable);
		JSONArray array = new JSONArray();

		for (Reserva reserva2 : reserva) {
			ReservaResponse reservaResponse = new ReservaResponse();
			reservaResponse.setIdreserva(reserva2.getIdreserva());

			long num = Long.parseLong(reserva2.getServicios());
			Servicio servicio = servicioRepository.findById(num)
					.orElseThrow(() -> new IllegalStateException("Servicio no existe."));
			reservaResponse.setServicios(servicio.getNombre());

			long num2 = Long.parseLong(reserva2.getProductos());
			Producto producto = productoRepository.findById(num2)
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));
			reservaResponse.setProductos(producto.getNombre());

			reservaResponse.setHorareserva(reserva2.getHorareserva());
			reservaResponse.setFechareserva(reserva2.getFecha());

			Vehiculo vehiculo2 = vehiculoRepository.findById(reserva2.getIdvehiculo())
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));

			reservaResponse.setVeichulo(vehiculo2.getTipovehiculo());
			reservaResponse.setMarca(vehiculo2.getMarca());
			reservaResponse.setPatente(vehiculo2.getPatente());

			reservaResponse.setEstado(reserva2.getEstado());
			array.add(reservaResponse);

		}

		return ResponseEntity.ok(array);
	}

	// para lista de clientes
	@GetMapping(value = "/{idCliente}/cliente")
	public ResponseEntity<?> obtenerReservaCliente(final Pageable pageable,
			@PathVariable(value = "idCliente") final Long idCliente) {
		List<Reserva> reserva = reservaServicesImpl.obtenerPorPaginacion(pageable, idCliente);
		JSONArray array = new JSONArray();

		for (Reserva reserva2 : reserva) {
			ReservaResponse reservaResponse = new ReservaResponse();
			reservaResponse.setIdreserva(reserva2.getIdreserva());

			long num = Long.parseLong(reserva2.getServicios());
			Servicio servicio = servicioRepository.findById(num)
					.orElseThrow(() -> new IllegalStateException("Servicio no existe."));
			reservaResponse.setServicios(servicio.getNombre());

			System.out.println(reserva2.getProductos());
			long num2 = Long.parseLong(reserva2.getProductos());
			Producto producto = productoRepository.findById(num2)
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));
			reservaResponse.setProductos(producto.getNombre());

			reservaResponse.setHorareserva(reserva2.getHorareserva());
			reservaResponse.setFechareserva(reserva2.getFecha());

			Vehiculo vehiculo2 = vehiculoRepository.findById(reserva2.getIdvehiculo())
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));

			reservaResponse.setVeichulo(vehiculo2.getTipovehiculo());
			reservaResponse.setMarca(vehiculo2.getMarca());
			reservaResponse.setPatente(vehiculo2.getPatente());
			reservaResponse.setEstado(reserva2.getEstado());
			reservaResponse.setTotalreserva(reserva2.getTotalreserva());
			array.add(reservaResponse);

		}

		return ResponseEntity.ok(array);

	}

	// para lista por estado
	@GetMapping(value = "/{estado}/estado")
	public List<Reserva> obtenerReservaEstado(final Pageable pageable,
			@PathVariable(value = "estado") final Integer estado) {
		return reservaServicesImpl.obtenerPorPaginacion(pageable, estado);
	}

	@GetMapping(value = "/{idCliente}/{activo}/cliente")
	public ResponseEntity<?> obtenerReservaClienteActivo(final Pageable pageable,
			@PathVariable(value = "idCliente") final Long idCliente,
			@PathVariable(value = "activo") final Boolean activo) {

		List<Reserva> reserva = reservaServicesImpl.obtenerPorPaginacionReservaActiva(pageable, idCliente, activo);
		JSONObject lista = new JSONObject();
		double monto = 0;
		for (Reserva reserva2 : reserva) {


			try {
				if (reserva2.getActivo() && reserva2.getEstado() != 6) {

					long num = Long.parseLong(reserva2.getProductos());
					long num2 = Long.parseLong(reserva2.getServicios());
					double valorProducto = productoServicesImpl.findByIdProducto(num).getValorbase();
					double valorServocio = servicioServicesImpl.findByIdServicio(num2).getValorbase();
					String servicio = servicioServicesImpl.findByIdServicio(num2).getNombre();
					monto = valorProducto + valorServocio;
				
					lista.put("estado", reserva2.getEstado());
					lista.put("monto", monto);
					lista.put("servicio", servicio);
					lista.put("idReserva", reserva2.getIdreserva());

					return ResponseEntity.ok(lista);
				} else {

				}
			} catch (Exception e) {
				lista.put("estado", "0");

				return ResponseEntity.ok(lista);
			}

		}

		lista.put("estado", "0");

		return ResponseEntity.ok(lista);

	}

	@GetMapping(value = "/{id}/{estado}/reserva")
	public ResponseEntity<?> actualizarEstadoReserva(@PathVariable(value = "estado") final Integer estado,
			@PathVariable(value = "id") final Long id) {
		String mensaje = "El estado de tu vehículo";
		String estado1 = "";
		double monto = 0;
		if (estado == 1) {
			estado1 = "Revision";
		} else if (estado == 2) {
			estado1 = "Trabajando";
		} else if (estado == 3) {
			estado1 = "Limpieza";
		} else if (estado == 4) {
			estado1 = "Pagar Servicio";
		} else if (estado == 5) {
			estado1 = "Servicio Completo";
		}

		Reserva reserva = repositorio.findById(id).orElseThrow(() -> new IllegalStateException("reserva no existe."));

		Cliente cliente = clienteRepository.findById(reserva.getIdcliente())
				.orElseThrow(() -> new IllegalStateException("IdCliente no existe."));
		User user = userRepository.findById(cliente.getIdusuario())
				.orElseThrow(() -> new IllegalStateException("IdUsuario no existe."));

		emailService.emailSendReserva(user.getEmail(), cliente.getNombre(), mensaje, estado1);
		reservaServicesImpl.findByIdReserva(id, estado);
		if (estado == 5) {
			emailService.emailSendEncuesta(user.getEmail(), cliente.getNombre(), mensaje, estado1);
			long num = Long.parseLong(reserva.getProductos());
			long num2 = Long.parseLong(reserva.getServicios());
			double valorProducto = productoServicesImpl.findByIdProducto(num).getValorbase();
			double valorServocio = servicioServicesImpl.findByIdServicio(num2).getValorbase();
			reservaServicesImpl.findByIdReserva(id, 6);
			System.out
					.println("oki " + valorProducto + " " + valorServocio + "falta meterlo a la tabla para el reporte");
			monto = valorProducto + valorServocio;

			reporteInServicesImpl.crearVS(monto, id);
			return ResponseEntity.ok("Reserva Terminada");
		} else {
			return ResponseEntity.ok("Reserva Actulizada");
		}

	}

	@GetMapping(value = "/{id}/{estado}/estado")
	public ResponseEntity<?> obtenerReservaClienteAndEstado(final Pageable pageable,
			@PathVariable(value = "id") final Long id, @PathVariable(value = "estado") final Integer estado) {
		List<Reserva> reserva = reservaServicesImpl.obtenerPorIdClienteAndEstado(pageable, id, estado);
		JSONArray array = new JSONArray();

		for (Reserva reserva2 : reserva) {
			ReservaResponse reservaResponse = new ReservaResponse();
			reservaResponse.setIdreserva(reserva2.getIdreserva());

			long num = Long.parseLong(reserva2.getServicios());
			Servicio servicio = servicioRepository.findById(num)
					.orElseThrow(() -> new IllegalStateException("Servicio no existe."));
			reservaResponse.setServicios(servicio.getNombre());

			long num2 = Long.parseLong(reserva2.getProductos());
			Producto producto = productoRepository.findById(num2)
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));
			reservaResponse.setProductos(producto.getNombre());

			reservaResponse.setHorareserva(reserva2.getHorareserva());
			reservaResponse.setFechareserva(reserva2.getFecha());

			Vehiculo vehiculo2 = vehiculoRepository.findById(reserva2.getIdvehiculo())
					.orElseThrow(() -> new IllegalStateException("Patente no existe."));

			reservaResponse.setVeichulo(vehiculo2.getTipovehiculo());
			reservaResponse.setMarca(vehiculo2.getMarca());
			reservaResponse.setPatente(vehiculo2.getPatente());
			reservaResponse.setEstado(reserva2.getEstado());
			reservaResponse.setTotalreserva(reserva2.getTotalreserva());
			array.add(reservaResponse);

		}

		return ResponseEntity.ok(array);
	}

	@GetMapping(value = "/{patente}/patente")
	public List<ResponseReservaPago> obtenerReservaPago(@PathVariable(value = "patente") final String patente) {

		return myBatisService.getReservaCliente(patente);
	}

	@GetMapping(value = "/reporteservicio")
	public ResponseEntity<?> obtenerReporteServicios() {
		
		List<String> mes= new ArrayList<>();
		List<String> totalServicio= new ArrayList<>();
		List<String> nombreServicio= new ArrayList<>();
		JSONObject lista = new JSONObject();

		List <ReporteServicio> reporteServicio = myBatisService.getReporteServicio();

		
		for (ReporteServicio reporteServicio1 : reporteServicio) {
			totalServicio.add(reporteServicio1.getTotalservicios());
			nombreServicio.add(reporteServicio1.getNombreservicio());
			// if (reporteServicio1.getMes().substring(0, 2).equals("01")) {
			// 	mes.add("Enero");
			// } else if(reporteServicio1.getMes().substring(0, 2).equals("02")) {
			// 	mes.add("Febrero");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("03")) {
			// 	mes.add("Marzo");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("04")) {
			// 	mes.add("Abril");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("05")) {
			// 	mes.add("Mayo");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("06")) {
			// 	mes.add("Junio");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("07")) {
			// 	mes.add("Julio");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("08")) {
			// 	mes.add("Agosto");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("09")) {
			// 	mes.add("Septiembre");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("10")) {
			// 	mes.add("Octubre");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("11")) {
			// 	mes.add("Noviembre");
			// }else if(reporteServicio1.getMes().substring(0, 2).equals("12")) {
			// 	mes.add("Diciembre");

			// }
		
			
		}

		lista.put("mes", mes);
		lista.put("totalServicio", totalServicio);
		lista.put("nombreServicio", nombreServicio);
	

		return ResponseEntity.ok(lista);
	}
}
