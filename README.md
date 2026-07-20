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

## Paquetes Java
- `GUI` — estilos y configuración general de la interfaz.
- `GUI.CustomerControl` — componentes personalizados para la GUI.
- `GUI.Form` — formularios y paneles de la aplicación.
- `GUI.Resource` — recursos visuales y soporte de imágenes.
- `SistemaVentas` — clases centrales del sistema de ventas y manejo de datos.
