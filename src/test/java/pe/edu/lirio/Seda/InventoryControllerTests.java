package pe.edu.lirio.Seda;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;
import pe.edu.lirio.Seda.model.bd.Marca;
import pe.edu.lirio.Seda.model.bd.Producto;
import pe.edu.lirio.Seda.model.bd.ProductoSede;
import pe.edu.lirio.Seda.model.bd.ProductoSedeId;
import pe.edu.lirio.Seda.model.bd.Proveedor;
import pe.edu.lirio.Seda.model.bd.Roles;
import pe.edu.lirio.Seda.model.bd.Sede;
import pe.edu.lirio.Seda.model.bd.Usuarios;
import pe.edu.lirio.Seda.repository.DetalleEntradaRepository;
import pe.edu.lirio.Seda.repository.MarcaRepository;
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
    private MarcaRepository marcaRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

        @Autowired
        private SedeRepository sedeRepository;

    @Autowired
    private ProductoRepository productoRepository;

        @Autowired
        private ProductoSedeRepository productoSedeRepository;

    @Autowired
    private DetalleEntradaRepository detalleEntradaRepository;

    private Usuarios admin;
    private Producto producto;
        private Sede sede;

    @BeforeEach
    void prepararDatos() {
        Roles rol = new Roles();
        rol.setNombre("ADMIN");
        rol.setDescripcion("Administrador de pruebas");
        rolesRepository.save(rol);

        admin = new Usuarios();
        admin.setNombre("Admin");
        admin.setApellido("Test");
        admin.setCorreo("admin@test.local");
        admin.setUsuario("admin-test");
        admin.setContrasena("hashed-password");
        admin.setRol(rol);
        admin.setFechaCreacion(LocalDateTime.now());
        admin.setActivo(true);
        admin = usuariosRepository.save(admin);

        Proveedor proveedor = new Proveedor();
        proveedor.setIdProveedor("A001");
        proveedor.setNombre("Proveedor");
        proveedorRepository.save(proveedor);

        Marca marca = new Marca();
        marca.setDescripcion("Marca de pruebas");
        marca = marcaRepository.save(marca);

        producto = new Producto();
        producto.setIdProducto("P001");
        producto.setNombre("Producto de prueba");
        producto.setMarca(marca);
        producto.setProveedor(proveedor);
        producto.setStock(10);
        producto.setPrecio(new BigDecimal("2.25"));
        producto = productoRepository.save(producto);

        sede = new Sede();
        sede.setDescripcion("Sede de pruebas");
        sede = sedeRepository.save(sede);

        ProductoSede productoSede = new ProductoSede();
        productoSede.setId(new ProductoSedeId(producto.getIdProducto(), sede.getIdSede()));
        productoSede.setProducto(producto);
        productoSede.setSede(sede);
        productoSede.setStock(10);
        productoSedeRepository.save(productoSede);
    }

    @Test
    @WithMockUser(authorities = "ROLE_USER")
    void usuarioComunNoPuedeConsultarUsuarios() throws Exception {
        mockMvc.perform(get("/api/usuarios"))
                .andExpect(status().isForbidden());
    }

                @Test
                @WithMockUser(authorities = "ROLE_ADMIN")
                void actualizarUsuarioSinEstadoNoLoReactivaNiExponeContrasena() throws Exception {
                                admin.setActivo(false);
                                usuariosRepository.save(admin);

                                mockMvc.perform(put("/api/usuarios/" + admin.getIdUsuario())
                                                                                                .contentType(MediaType.APPLICATION_JSON)
                                                                                                .content("""
                                                                                                                                {
                                                                                                                                        "nombre": "Admin",
                                                                                                                                        "apellido": "Test",
                                                                                                                                        "correo": "admin@test.local",
                                                                                                                                        "usuario": "admin-test",
                                                                                                                                        "idRol": %d
                                                                                                                                }
                                                                                                                                """.formatted(admin.getRol().getIdRol())))
                                                                .andExpect(status().isOk())
                                                                .andExpect(jsonPath("$.activo").value(false))
                                                                .andExpect(jsonPath("$.contrasena").doesNotExist());

                                assertEquals(false, usuariosRepository.findById(admin.getIdUsuario()).orElseThrow().getActivo());
                }

    @Test
    @WithMockUser(authorities = "ROLE_ADMIN")
    void movimientosAjustanStockTotalesYDetallesDeFormaAtomica() throws Exception {
        mockMvc.perform(post("/api/entradas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(3)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.importeTotal").value(6.75))
                .andExpect(jsonPath("$.detalles[0].importe").value(6.75));
        assertEquals(13, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(13, stockEnSede());

        mockMvc.perform(put("/api/entradas/ENT001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(entradaJson(2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.importeTotal").value(4.50));
        assertEquals(12, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(12, stockEnSede());
        assertEquals(1, detalleEntradaRepository.count());

        mockMvc.perform(post("/api/salidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(13)))
                .andExpect(status().isConflict());
        assertEquals(12, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(12, stockEnSede());

        mockMvc.perform(post("/api/salidas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(salidaJson(5)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.importeTotal").value(11.25));
        assertEquals(7, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(7, stockEnSede());

        mockMvc.perform(delete("/api/salidas/SAL001"))
                .andExpect(status().isNoContent());
        assertEquals(12, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(12, stockEnSede());

        mockMvc.perform(delete("/api/entradas/ENT001"))
                .andExpect(status().isNoContent());
        assertEquals(10, productoRepository.findById(producto.getIdProducto()).orElseThrow().getStock());
        assertEquals(10, stockEnSede());
    }

    private String entradaJson(int cantidad) {
        return """
                {
                  "idEntrada": "ENT001",
                  "idUsuario": %d,
                  "idProveedor": "A001",
                  "idSede": %d,
                  "importeTotal": 99999,
                  "detalles": [{
                    "idProducto": "P001",
                    "cantidad": %d,
                    "precioUnidad": 2.25,
                    "importe": 99999
                  }]
                }
                """.formatted(admin.getIdUsuario(), sede.getIdSede(), cantidad);
    }

    private String salidaJson(int cantidad) {
        return """
                {
                  "idSalida": "SAL001",
                  "idUsuario": %d,
                  "idSede": %d,
                  "destino": "Prueba",
                  "importeTotal": 99999,
                  "detalles": [{
                    "idProducto": "P001",
                    "cantidad": %d,
                    "precioUnidad": 2.25,
                    "importe": 99999
                  }]
                }
                                """.formatted(admin.getIdUsuario(), sede.getIdSede(), cantidad);
    }

        private int stockEnSede() {
                return productoSedeRepository.findById(new ProductoSedeId(producto.getIdProducto(), sede.getIdSede()))
                                .orElseThrow().getStock();
        }
}