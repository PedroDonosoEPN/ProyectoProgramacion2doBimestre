# Proyecto de Ventas

## Descripción
Aplicación de sistema de ventas que gestiona productos, inventario y operaciones de venta con autenticación de usuarios.

## Funcionalidades
- Inicio de sesión de usuario
- Gestión de inventario
- Registro de ventas
- Soporte para escaneo de códigos de barras
- Generación de facturas

## Requisitos
- JDK 25
- Windows 10 o posterior

## Instalación
1. Descargar el instalador.
2. Ejecutar `Setup.exe`.

## Ejecución
Hacer doble clic en la aplicación o ejecutar:

```sh
java -jar Main.jar
```

## Estructura del proyecto
- `src/`
  - `GUI/`
    - `CustomerControl/` — controles reutilizables de interfaz (botones, etiquetas, textos).
    - `Form/` — paneles y vistas principales de la aplicación (login, menú, inventario, ventas, splash).
    - `Resource/` — recursos de interfaz y estilos.
      - `Img/` — imágenes usadas en la interfaz.
  - `SistemaVentas/` — lógica de dominio de ventas, inventario, facturación y lector de barras.

## Normas de código
- Los paquetes deben escribirse en minúsculas y usar camelCase cuando sea necesario, por ejemplo: `sistemaVentas`
- Las clases deben usar PascalCase, iniciando con mayúscula, por ejemplo: `LoginPanel`, `Venta`.
- Las variables, métodos y atributos deben nombrarse en español, usando camelCase y nombres descriptivos, por ejemplo: `nombreCliente`, `totalVenta`, `precioUnitario`.
- Las constantes deben escribirse en mayúsculas y separar palabras con guion bajo, por ejemplo: `PRECIO_MAXIMO`.
- Los comentarios y documentación del código deben realizarse en español, de forma clara y breve.

## Paquetes Java
- `GUI` — estilos y configuración general de la interfaz.
- `GUI.CustomerControl` — componentes personalizados para la GUI.
- `GUI.Form` — formularios y paneles de la aplicación.
- `GUI.Resource` — recursos visuales y soporte de imágenes.
- `SistemaVentas` — clases centrales del sistema de ventas y manejo de datos.
