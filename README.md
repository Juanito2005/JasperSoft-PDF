📄 Entregable 5: Generación de Informes con JasperReports

Asignatura: Desarrollo de Interfaces (FP DAM)
Autor: Juan Camilo
Tecnologías: Java 21, Maven, JasperReports (6.21.5), Jaspersoft Studio (7.0.3), MySQL 8.0 (Docker/OrbStack). Docker OrbStack

📝 Descripción del Proyecto

Este proyecto demuestra la integración de la librería JasperReports en una aplicación Java estándar. El objetivo es generar documentos PDF dinámicos a partir de plantillas XML (.jrxml), extrayendo los datos en tiempo real de una base de datos relacional MySQL a través de JDBC.

El entregable se divide en dos ejercicios prácticos:

Informe Básico: Listado general de clientes.

Informe Parametrizado: Historial de pedidos filtrado dinámicamente por cliente y rango de fechas, incluyendo variables de cálculo (sumatorios y recuentos).

🗂️ Estructura del Proyecto

Se ha seguido una arquitectura basada en capas (separación de responsabilidades) para mantener el código limpio y escalable:

📁 jasperdeliverable
├── 📄 pom.xml (Dependencias de Maven: jasperreports y mysql-connector-j)
├── 📁 src/main/resources/
│   ├── 📄 informe_clientes.jrxml (Plantilla Ejercicio 1)
│   └── 📄 informe_pedidos.jrxml  (Plantilla Ejercicio 2)
└── 📁 src/main/java/dev/juanito/
    ├── ☕ ConectionJDBC.java (Patrón Singleton para la conexión a MySQL)
    ├── ☕ ReportService.java (Lógica de negocio: Compilar, rellenar y exportar)
    └── ☕ Main.java          (Punto de entrada y ejecución de bucles)

⚙️ Configuración de la Base de Datos

El proyecto consume una base de datos MySQL alojada en un contenedor Docker.

Modelo de datos (gestion_pedidos):

clientes (id, nombre, telefono)

articulos (id, nombre, precio)

pedidos (id, id_cliente, fecha, total)

detalle_pedidos (id, id_pedido, id_articulo, cantidad, precio_unitario)

<details> <summary>👉 Haz clic aquí para ver el Script SQL de creación e inserción de datos (DDL/DML)</summary>
CREATE DATABASE gestion_pedidos;
USE gestion_pedidos;

CREATE TABLE clientes (
     id INT PRIMARY KEY AUTO_INCREMENT,
     nombre VARCHAR(100) NOT NULL,
     telefono VARCHAR(20) NOT NULL
);

CREATE TABLE articulos (
     id INT PRIMARY KEY AUTO_INCREMENT,
     nombre VARCHAR(255) NOT NULL,
     precio DECIMAL(10, 2) NOT NULL
);

CREATE TABLE pedidos (
     id INT PRIMARY KEY AUTO_INCREMENT,
     id_cliente INT NOT NULL,
     fecha DATE NOT NULL,
     total DECIMAL(10, 2),
     CONSTRAINT fk_pedido_cliente FOREIGN KEY (id_cliente) REFERENCES clientes(id)
);

CREATE TABLE detalle_pedidos (
     id INT PRIMARY KEY AUTO_INCREMENT,
     id_pedido INT NOT NULL,
     id_articulo INT NOT NULL,
     cantidad INT NOT NULL,
     precio_unitario DECIMAL(10, 2) NOT NULL,
     CONSTRAINT fk_detalle_pedido FOREIGN KEY (id_pedido) REFERENCES pedidos(id),
     CONSTRAINT fk_detalle_articulo FOREIGN KEY (id_articulo) REFERENCES articulos(id)
);

-- Datos de prueba básicos
INSERT INTO clientes (nombre, telefono) VALUES 
  ('Juan Camilo', '123456789'),
  ('Ana Lopez', '666111222'),
  ('Luis Garcia', '666333444'),
  ('Marta Sanz', '666555666');

INSERT INTO articulos (nombre, precio) VALUES 
  ('Coca-Cola', 1.80),
  ('Bocadillo', 3.50),
  ('Menú del Día', 12.00);

INSERT INTO pedidos (id_cliente, fecha, total) VALUES 
  (1, '2026-02-12', 8.30),
  (2, '2026-02-13', 12.00),
  (3, '2023-10-15', 7.00);

</details>
🚀 Ejercicio 1: Listado de Clientes

Se ha diseñado un informe con las bandas Title, Column Header, Detail, Page Footer y Summary.
La banda Detail itera automáticamente sobre el ResultSet generado por la consulta interna de JasperReports.

Flujo en Java (ReportService.java):

Compilación: JasperCompileManager.compileReport() transforma el .jrxml en un objeto JasperReport.

Rellenado: JasperFillManager.fillReport() une la plantilla con la conexión JDBC obtenida de ConectionJDBC, devolviendo un JasperPrint.

Exportación: JasperExportManager.exportReportToPdfFile() guarda el documento como clientes_resultado.pdf.

🎯 Ejercicio 2: Historial de Pedidos Parametrizado

Este informe utiliza elementos avanzados de Jaspersoft:

1. Parámetros ($P{...})

Se crearon tres parámetros en el .jrxml (pClienteId, pFechaInicio, pFechaFin) que permiten filtrar la consulta SQL dinámicamente.
Desde Java se inyectan usando un Map:

Map<String, Object> parametros = new HashMap<>();
parametros.put("pClienteId", 1);
parametros.put("pFechaInicio", Date.valueOf("2000-01-01"));
parametros.put("pFechaFin", Date.valueOf("2099-12-31"));

2. Variables de Cálculo ($V{...})

En la banda Summary se añadieron dos variables que JasperReports calcula automáticamente sin necesidad de programar bucles en Java:

vTotalimporte: java.math.BigDecimal con cálculo Sum apuntando al campo $F{total}.

vNumPedidos: java.lang.Integer con cálculo Count apuntando al campo $F{pedido_id}.

3. Reutilización del Código

En el método main se ejecuta un bucle for con varias iteraciones, cambiando los valores del mapa en cada vuelta para demostrar que la misma plantilla .jrxml puede generar múltiples reportes distintos (pedidos_reporte_1.pdf, pedidos_reporte_2.pdf, pedidos_reporte_3.pdf) basándose en los filtros pasados.

💻 Ejecución

Para generar todos los reportes:

Compila el proyecto con Maven (mvn clean package).

Ejecuta la clase principal Main.java.

Los PDFs resultantes se guardarán automáticamente en el directorio raíz del proyecto:

clientes_resultado.pdf

pedidos_reporte_1.pdf

pedidos_reporte_2.pdf

pedidos_reporte_3.pdf