package com.coderhouse.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.coderhouse.model.Producto;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductosApplicationTests {

	private String url;

	@LocalServerPort
	private int port;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeAll
	static void setup() {
		System.out.println("@BeforeAll - se ejecuta antes de todos los tests");
	}

	@BeforeEach
	void init() {
		url = String.format("http://localhost:%d/coder-house/", port);
		System.out.println("@BeforeEach - se ejecuta antes de la ejecución de cada test");
	}

	@Test
	public void getAllProductos() throws Exception {
		var uriTest = String.format("%s%s", url, "productos/all");

		var productoResult = this.restTemplate.getForObject(uriTest, List.class);

		Assert.notNull(productoResult, "Lista de productos no nula");
		Assert.notEmpty(productoResult, "Lista de productos con elementos");
		Assert.isTrue(productoResult.size() == 5, "Tamaño de la lista es de 5");
	}

	@Test
	public void getProductoById() {
		var uriTest = String.format("%s%s", url, "productos/1");
		var productoResult =
				this.restTemplate.getForObject(uriTest, Producto.class);

		Assert.notNull(productoResult, "Productos no nula");
		Assert.isTrue(productoResult.getId() == 1, "ID del Producto OK");
		Assert.isTrue(productoResult.getDescription().equals("Producto-1"), "Descripción del mensaje OK");
	}

	@Test
	public void createProductos() {
		var uriTest = String.format("%s%s", url, "productos");
		var message = Producto.builder().id(18L).description("Producto de ejemplo").build();

		var productoResult =
				this.restTemplate.postForObject(uriTest, message, Producto.class);

		Assert.notNull(productoResult, "Producto no nula");
		Assert.isTrue(productoResult.getId() == 18L, "ID del Producto OK");
		Assert.isTrue(productoResult.getDescription().equals("Producto de ejemplo"), "Descripción del Producto OK");
	}


	@Test
	public void getAllProductosHttpRequestStatus() throws IOException {
		var uriTest = String.format("%s%s", url, "productos/all");

		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);

		Assert.isTrue(
				httpResponse
						.getStatusLine()
						.getStatusCode() == HttpStatus.SC_OK,
				"Response status OK");
	}

	@Test
	public void getAllProductosHttpRequestHeader() throws IOException {
		var uriTest = String.format("%s%s", url, "productos/all");
		var headerAppJson = "application/json";

		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);
		var mimeType = ContentType
				.getOrDefault(httpResponse.getEntity()).getMimeType();
		Assert.isTrue(headerAppJson.equals(mimeType),
				"Header application/json OK");
	}

	@Test
	public void getAllProductosHttpRequestPayload() throws IOException {
		var uriTest = String.format("%s%s", url, "productos/all");

		var request = new HttpGet(uriTest);
		var httpResponse =
				HttpClientBuilder.create().build().execute(request);

		String content = EntityUtils.toString(httpResponse.getEntity());
		var productoResult = objectMapper.readValue(content, List.class);

		Assert.notNull(productoResult, "Lista de Productos no nula");
		Assert.notEmpty(productoResult, "Lista de Productos con elementos");
		Assert.isTrue(productoResult.size() == 6, "Tamaño de la lista es de 5");

	}
}