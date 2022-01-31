package com.coderhouse.controller;


import com.coderhouse.handle.ApiRestException;
import com.coderhouse.model.Producto;
import com.coderhouse.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coder-house")
public class ProductoController {

    Logger logger = LogManager.getLogger(ProductoController.class);

    private final ProductoService productoService;
    private final ArrayList<Producto> productos;

    @PostConstruct
    void setProductosInit() {
        productos.add(new Producto(1L, "Producto-1"));
        productos.add(new Producto(1L, "Producto-2"));
        productos.add(new Producto(1L, "Producto-3"));
        productos.add(new Producto(1L, "Producto-4"));
        productos.add(new Producto(1L, "Producto-5"));
    }

    @GetMapping("/productos/example")
    public String getProductosString() {
        logger.info("GET Request recibido string");
        return "Ejemplo de respuesta";
    }

    @GetMapping("/productos/all")
    public List<Producto> getProductosAll() {
        logger.info("GET Request recibido string");
        return productos;
    }

    @PostMapping("/productos")
    public Producto createProductos(@RequestBody Producto producto) {
        logger.info("GET Request recibido string");
        productos.add(producto);
        return producto;
    }

    @GetMapping("/productos")
    public List<Producto> getProductosByDescription(@RequestParam String description) {
        logger.info("GET obtener productos que sean iguales a la descripción");
        var msjFiltered = productos.stream()
                .filter(productos -> productos.getDescription().equalsIgnoreCase(description));
        return msjFiltered.collect(Collectors.toList());
    }

    @GetMapping("/productos/{id}")
    public Producto getProductoById(@PathVariable Long id) throws ApiRestException {
        logger.info("GET obtener producto por el id");

        if (id == 0) {
            throw ApiRestException.builder().message("El identificador del producto debe ser mayor a 0").build();
        }
        var msjFiltered = productos.stream()
                .filter(productos -> Objects.equals(productos.getId(), id));
        return msjFiltered.findFirst().orElse(new Producto(0L, "No existe el producto"));
    }

}