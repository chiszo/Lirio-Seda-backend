package pe.edu.lirio.Seda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        proveedor.setIdProveedor("A001");
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
    @WithMockUser(roles = "ADMIN")
    void movimientosActualizanStockPorSedeYValidanSalidas() throws Exception {
        mockMvc.perform(post("/api/entradas")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.importeTotal").value(6.75));
        assertEquals(13, stockEnSede());

        mockMvc.perform(put("/api/entradas/ENT0001")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importeTotal").value(4.50));
        assertEquals(12, stockEnSede());

        mockMvc.perform(post("/api/salidas")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(13)))
                .andExpect(status().isConflict());
        assertEquals(12, stockEnSede());

        mockMvc.perform(post("/api/salidas")
                        .with(user("admin").roles("ADMIN"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(5)))
                .andExpect(status().isCreated());
        assertEquals(7, stockEnSede());

        mockMvc.perform(delete("/api/salidas/SAL0001").with(user("admin").roles("ADMIN")))
          .andExpect(status().isNoContent());
        assertEquals(12, stockEnSede());

        mockMvc.perform(delete("/api/entradas/ENT0001").with(user("admin").roles("ADMIN")))
          .andExpect(status().isNoContent());
        assertEquals(10, stockEnSede());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void pedidoSeVinculaConUsuarioYEstadoConDetalleSoloCantidad() throws Exception {
        mockMvc.perform(post("/api/pedidos")
                        .with(user("admin").roles("ADMIN"))
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

    private String entradaJson(int cantidad) {
        return """
                {
                  "idEntrada": "ENT0001",
                  "idSedeUsuario": %d,
                  "idProveedor": "A001",
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
