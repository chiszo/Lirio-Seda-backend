package pe.edu.lirio.Seda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.lirio.Seda.model.bd.Estado;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.model.bd.Motivo;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;
import pe.edu.lirio.Seda.model.bd.Proveedor;
import pe.edu.lirio.Seda.model.bd.Roles;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.repository.EstadoRepository;
import pe.edu.lirio.Seda.repository.DetalleEntradaRepository;
import pe.edu.lirio.Seda.repository.DetalleSalidaRepository;
import pe.edu.lirio.Seda.repository.DetallePedidoRepository;
import pe.edu.lirio.Seda.repository.EntradaRepository;
import pe.edu.lirio.Seda.repository.MarcaRepository;
import pe.edu.lirio.Seda.repository.MotivoRepository;
import pe.edu.lirio.Seda.repository.ProductoRepository;
import pe.edu.lirio.Seda.repository.ProductoSedeRepository;
import pe.edu.lirio.Seda.repository.ProveedorRepository;
import pe.edu.lirio.Seda.repository.RolesRepository;
import pe.edu.lirio.Seda.repository.SedeRepository;
import pe.edu.lirio.Seda.repository.UsuariosRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class InventoryControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsuariosRepository usuariosRepository;

    @Autowired
    private MarcaRepository modeloRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private SedeRepository sedeRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Autowired
    private MotivoRepository motivoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private ProductoSedeRepository productoSedeRepository;

    @Autowired
    private DetalleEntradaRepository detalleEntradaRepository;

    @Autowired
    private DetalleSalidaRepository detalleSalidaRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    private Usuarios admin;
    private Producto producto;
    private Sede sede;
    private Estado estado;
    private Motivo motivo;

    @BeforeEach
    void prepararDatos() {
        Roles rol = new Roles();
        rol.setNombre("ADMIN");
        rolesRepository.save(rol);

        sede = new Sede();
        sede.setDescripcion("Sede de pruebas");
        sede = sedeRepository.save(sede);

        admin = new Usuarios();
        admin.setNombre("Admin");
        admin.setApellido("Test");
        admin.setCorreo("admin@test.local");
        admin.setTelefono("999999999");
        admin.setDocumento("12345678");
        admin.setFechaCreacion(LocalDate.now());
        admin.setClave("hashed-password");
        admin.setActivo("S");
        admin.setRol(rol);
        admin.setSede(sede);
        admin = usuariosRepository.save(admin);

        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor("P001");
        proveedor.setNombre("Proveedor de pruebas");
        proveedorRepository.save(proveedor);

        Marca modelo = new Marca();
        modelo.setDescripcion("Modelo de pruebas");
        modelo = modeloRepository.save(modelo);

        producto = new Producto();
        producto.setIdProducto("P001");
        producto.setNombre("Producto de prueba");
        producto.setModelo(modelo);
        producto.setEstado("A");
        producto.setPrecio(new BigDecimal("2.25"));
        producto = productoRepository.save(producto);

        ProductoSede productoSede = new ProductoSede();
        productoSede.setId(new ProductoSedeId(producto.getIdProducto(), sede.getIdSede()));
        productoSede.setProducto(producto);
        productoSede.setSede(sede);
        productoSede.setStock(10);
        productoSedeRepository.save(productoSede);

        estado = new Estado();
        estado.setDescripcion("Pendiente");
        estadoRepository.save(estado);

        motivo = new Motivo();
        motivo.setDescripcion("Ajuste de inventario");
        motivoRepository.save(motivo);
    }

    @Test
    @WithMockUser(roles = "USER")
    void usuarioComunNoPuedeConsultarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios")).andExpect(status().isForbidden());
    }

    @Test
    void usuarioAutenticadoPuedeConsultarSuPropioPerfilYIds() throws Exception {
        mockMvc.perform(get("/api/usuarios/me").with(user(admin.getCorreo()).roles("USER")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idUsuario").value(admin.getIdUsuario()))
                .andExpect(jsonPath("$.idSede").value(sede.getIdSede()))
                .andExpect(jsonPath("$.idRol").value(admin.getRol().getIdRol()))
                .andExpect(jsonPath("$.correo").value(admin.getCorreo()))
                .andExpect(jsonPath("$.clave").doesNotExist());
    }

    @Test
    void permitePreflightCorsDesdeFrontendLocal() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                        .header("Origin", "http://localhost:4200")
                        .header("Access-Control-Request-Method", "POST")
                        .header("Access-Control-Request-Headers", "authorization,content-type"))
                .andExpect(status().isOk())
                .andExpect(org.springframework.test.web.servlet.result.MockMvcResultMatchers
                        .header().string("Access-Control-Allow-Origin", "http://localhost:4200"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuarioSinActivoConservaElEstadoYNoExponeClave() throws Exception {
        admin.setActivo("N");
        usuariosRepository.save(admin);

        mockMvc.perform(put("/api/usuarios/" + admin.getIdUsuario())
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "nombre": "Admin",
                                  "apellido": "Test",
                                  "correo": "admin@test.local",
                                  "documento": "12345678",
                                  "idRol": %d,
                                  "idSede": %d
                                }
                                """.formatted(admin.getRol().getIdRol(), sede.getIdSede())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.activo").value("N"))
                .andExpect(jsonPath("$.clave").doesNotExist());

        assertEquals("N", usuariosRepository.findById(admin.getIdUsuario()).orElseThrow().getActivo());
    }

    @Test
    @WithMockUser(roles = "USER")
    void movimientosActualizanStockPorSedeYValidanSalidas() throws Exception {
        mockMvc.perform(post("/api/entradas")
              .with(user("almacenero").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.importeTotal").value(6.75));
        assertEquals(13, stockEnSede());

        mockMvc.perform(put("/api/entradas/ENT0001")
                        .with(user("almacenero").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importeTotal").value(4.50));
        assertEquals(12, stockEnSede());

        mockMvc.perform(post("/api/salidas")
                        .with(user("almacenero").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(13)))
                .andExpect(status().isConflict());
        assertEquals(12, stockEnSede());

        mockMvc.perform(post("/api/salidas")
                        .with(user("almacenero").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(5)))
                .andExpect(status().isCreated());
        assertEquals(7, stockEnSede());

        mockMvc.perform(delete("/api/salidas/SAL0001").with(user("almacenero").roles("USER")))
          .andExpect(status().isNoContent());
        assertEquals(12, stockEnSede());

        mockMvc.perform(delete("/api/entradas/ENT0001").with(user("almacenero").roles("USER")))
          .andExpect(status().isNoContent());
        assertEquals(10, stockEnSede());
    }

    @Test
    @WithMockUser(roles = "USER")
    void pedidoSeVinculaConUsuarioYEstadoConDetalleSoloCantidad() throws Exception {
        mockMvc.perform(post("/api/pedidos")
              .with(user("vendedor").roles("USER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "idPedido": "PED0001",
                                  "idSedeUsuario": %d,
                                  "idEstado": %d,
                                  "idUsuario": %d,
                                  "detalles": [{"idProducto": "P001", "cantidad": 4}]
                                }
                                """.formatted(sede.getIdSede(), estado.getIdEstado(), admin.getIdUsuario())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstado").value(estado.getIdEstado()))
                .andExpect(jsonPath("$.detalles[0].cantidad").value(4));
    }

        @Test
        @WithMockUser(roles = "USER")
        void usuarioAutenticadoPuedeModificarProductosProveedoresYStockPorSede() throws Exception {
      mockMvc.perform(post("/api/productos")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"idProducto":"PR02","nombre":"Nuevo","idModelo":%d,"precio":12.50,"estado":"A"}
            """.formatted(producto.getModelo().getIdModelo())))
        .andExpect(status().isCreated());

      mockMvc.perform(put("/api/productos/PR02")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"nombre":"Actualizado","idModelo":%d,"precio":15.00,"estado":"A"}
            """.formatted(producto.getModelo().getIdModelo())))
        .andExpect(status().isOk());

      mockMvc.perform(post("/api/productos-sedes")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"idProducto":"PR02","idSede":%d,"stock":8}
            """.formatted(sede.getIdSede())))
        .andExpect(status().isCreated());

      mockMvc.perform(put("/api/productos-sedes/PR02/" + sede.getIdSede())
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"stock":9}
            """))
        .andExpect(status().isOk());

      mockMvc.perform(delete("/api/productos-sedes/PR02/" + sede.getIdSede())
          .with(user("almacenero").roles("USER")))
        .andExpect(status().isNoContent());

      mockMvc.perform(delete("/api/productos/PR02").with(user("almacenero").roles("USER")))
        .andExpect(status().isNoContent());

      mockMvc.perform(put("/api/proveedores/P001")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"nombre":"Proveedor actualizado","telefono":"999","correo":"proveedor@test.local","direccion":"Local","ruc":"RUC1"}
            """))
        .andExpect(status().isOk());

      mockMvc.perform(post("/api/proveedores")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"idProveedor":"P002","nombre":"Otro proveedor","telefono":"111","correo":"otro@test.local","direccion":"Av. Lima 123","ruc":"RUC2"}
            """))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.ruc").value("RUC2"))
        .andExpect(jsonPath("$.correo").value("otro@test.local"))
        .andExpect(jsonPath("$.direccion").value("Av. Lima 123"));

      mockMvc.perform(delete("/api/proveedores/P002").with(user("almacenero").roles("USER")))
        .andExpect(status().isNoContent());
        }

        @Test
        @WithMockUser(roles = "USER")
        void usuarioAutenticadoPuedeCrearEditarYEliminarDetallesConIntegridad() throws Exception {
      mockMvc.perform(post("/api/entradas")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content(entradaJson(3)))
        .andExpect(status().isCreated());

      mockMvc.perform(post("/api/detalles-entrada")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":1,"precioUnitario":2.25,"idEntrada":"ENT0001","idProducto":"P001"}
            """))
        .andExpect(status().isCreated());
      assertEquals(14, stockEnSede());
      assertEquals(new BigDecimal("9.00"), entradaRepository.findById("ENT0001").orElseThrow().getImporteTotal());

      Integer idDetalleEntrada = detalleEntradaRepository.findAllByEntrada_IdEntrada("ENT0001").stream()
        .filter(detalle -> detalle.getCantidad() == 1).findFirst().orElseThrow().getIdDetalleEntrada();
      mockMvc.perform(put("/api/detalles-entrada/" + idDetalleEntrada)
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":2,"precioUnitario":2.25,"idEntrada":"ENT0001","idProducto":"P001"}
            """))
        .andExpect(status().isOk());
      assertEquals(15, stockEnSede());
      assertEquals(new BigDecimal("11.25"), entradaRepository.findById("ENT0001").orElseThrow().getImporteTotal());

      mockMvc.perform(delete("/api/detalles-entrada/" + idDetalleEntrada)
          .with(user("almacenero").roles("USER")))
        .andExpect(status().isNoContent());
      assertEquals(13, stockEnSede());
      assertEquals(new BigDecimal("6.75"), entradaRepository.findById("ENT0001").orElseThrow().getImporteTotal());

      mockMvc.perform(post("/api/salidas")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content(salidaJson(2)))
        .andExpect(status().isCreated());

      mockMvc.perform(post("/api/detalles-salida")
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":1,"idProducto":"P001","idSalida":"SAL0001"}
            """))
        .andExpect(status().isCreated());
      assertEquals(10, stockEnSede());

      Integer idDetalleSalida = detalleSalidaRepository.findAllBySalida_IdSalida("SAL0001").stream()
        .filter(detalle -> detalle.getCantidad() == 1).findFirst().orElseThrow().getIdDetalleSalida();
      mockMvc.perform(put("/api/detalles-salida/" + idDetalleSalida)
          .with(user("almacenero").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":2,"idProducto":"P001","idSalida":"SAL0001"}
            """))
        .andExpect(status().isOk());
      assertEquals(9, stockEnSede());

      mockMvc.perform(delete("/api/detalles-salida/" + idDetalleSalida)
          .with(user("almacenero").roles("USER")))
        .andExpect(status().isNoContent());
      assertEquals(11, stockEnSede());

      mockMvc.perform(post("/api/pedidos")
          .with(user("vendedor").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"idPedido":"PED0002","idSedeUsuario":%d,"idEstado":%d,"idUsuario":%d}
            """.formatted(sede.getIdSede(), estado.getIdEstado(), admin.getIdUsuario())))
        .andExpect(status().isCreated());
      mockMvc.perform(post("/api/detalles-pedido")
          .with(user("vendedor").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":2,"idPedido":"PED0002","idProducto":"P001"}
            """))
        .andExpect(status().isCreated());
      Integer idDetallePedido = detallePedidoRepository.findAllByPedido_IdPedido("PED0002").get(0)
        .getIdDetallePedido();
      mockMvc.perform(put("/api/detalles-pedido/" + idDetallePedido)
          .with(user("vendedor").roles("USER"))
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
            {"cantidad":3,"idPedido":"PED0002","idProducto":"P001"}
            """))
        .andExpect(status().isOk());
      mockMvc.perform(delete("/api/detalles-pedido/" + idDetallePedido)
          .with(user("vendedor").roles("USER")))
        .andExpect(status().isNoContent());
      assertEquals(11, stockEnSede());
        }

    private String entradaJson(int cantidad) {
        return """
                {
                  "idEntrada": "ENT0001",
                  "idSedeUsuario": %d,
                  "idProveedor": "P001",
                  "idUsuario": %d,
                  "detalles": [{
                    "idProducto": "P001",
                    "cantidad": %d,
                    "precioUnitario": 2.25
                  }]
                }
                """.formatted(sede.getIdSede(), admin.getIdUsuario(), cantidad);
    }

    private String salidaJson(int cantidad) {
        return """
                {
                  "idSalida": "SAL0001",
                  "idSedeUsuario": %d,
                  "idMotivo": %d,
                  "idUsuario": %d,
                  "detalles": [{"idProducto": "P001", "cantidad": %d}]
                }
                """.formatted(sede.getIdSede(), motivo.getIdMotivo(), admin.getIdUsuario(), cantidad);
    }

    private int stockEnSede() {
        return productoSedeRepository.findById(new ProductoSedeId(producto.getIdProducto(), sede.getIdSede()))
                .orElseThrow().getStock();
    }
}
