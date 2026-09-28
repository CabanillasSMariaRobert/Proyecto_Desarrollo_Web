const fs = require('fs');
const path = require('path');

const baseDir = 'src/main/java/com/ecommerce/tienda_kalza/controladores';
const pkgServices = 'com.ecommerce.tienda_kalza.servicios.publico';

const controllers = {
    'InicioController.java': {
        service: 'InicioService',
        methods: {
            'index': [
                'model.addAttribute("hero", inicioService.obtenerHero());',
                'model.addAttribute("categorias", inicioService.obtenerCategorias());',
                'model.addAttribute("destacados", inicioService.obtenerDestacados());',
                'model.addAttribute("oferta", inicioService.obtenerOferta());'
            ]
        }
    },
    'ProductoController.java': {
        service: 'ProductoService',
        methods: {
            'cargarProducto': [
                'model.addAttribute("producto", productoService.obtenerDetalle(id != null ? id.longValue() : 1L));'
            ]
        }
    },
    'CarritoController.java': {
        service: 'CarritoService',
        methods: {
            'carrito': [
                'model.addAttribute("resumen", carritoService.obtenerResumen());'
            ]
        }
    },
    'CheckoutController.java': {
        service: 'CheckoutService',
        methods: {
            'checkout': [
                'model.addAttribute("resumen", checkoutService.obtenerResumen());'
            ]
        }
    },
    'PedidoController.java': {
        service: 'PedidoService',
        methods: {
            'listaPedidos': [
                'model.addAttribute("pedidos", pedidoService.obtenerPedidos());'
            ]
        }
    },
    'CuentaController.java': {
        service: 'CuentaService',
        methods: {
            'perfil': [
                'model.addAttribute("usuario", cuentaService.obtenerPerfil());',
                'model.addAttribute("pedidos", cuentaService.obtenerPedidosRecientes());'
            ],
            'usuario': [
                'model.addAttribute("usuario", cuentaService.obtenerPerfil());'
            ]
        }
    },
    'ContactoController.java': {
        service: 'ContactoService',
        methods: {
            'contacto': [
                'model.addAttribute("datos", contactoService.obtenerDatos());',
                'model.addAttribute("horarios", contactoService.obtenerHorarios());',
                'model.addAttribute("faqs", contactoService.obtenerFaqs());'
            ]
        }
    },
    'PublicidadController.java': {
        service: 'PublicidadService',
        methods: {
            'nosotros': [
                'model.addAttribute("banners", publicidadService.obtenerBanners());',
                'model.addAttribute("legales", publicidadService.obtenerLegales());'
            ],
            'terminos': [
                'model.addAttribute("banners", publicidadService.obtenerBanners());',
                'model.addAttribute("legales", publicidadService.obtenerLegales());'
            ],
            'privacidad': [
                'model.addAttribute("banners", publicidadService.obtenerBanners());',
                'model.addAttribute("legales", publicidadService.obtenerLegales());'
            ]
        }
    }
};

for (const [ctrlFile, info] of Object.entries(controllers)) {
    const filePath = path.join(baseDir, ctrlFile);
    if (!fs.existsSync(filePath)) {
        console.log('Skipping ' + filePath);
        continue;
    }
    
    let content = fs.readFileSync(filePath, 'utf-8');
    const service = info.service;
    const serviceVar = service.charAt(0).toLowerCase() + service.slice(1);
    
    if (!content.includes(service)) {
        content = content.replace(/(import org\.springframework\.stereotype\.Controller;)/, `$1\nimport ${pkgServices}.${service};`);
        
        const className = ctrlFile.split('.')[0];
        const fieldStmt = `    private final ${service} ${serviceVar};\n\n    public ${className}(${service} ${serviceVar}) {\n        this.${serviceVar} = ${serviceVar};\n    }\n\n`;
        content = content.replace(new RegExp(`(@Controller\\s*public class ${className}\\s*\\{)`), `$1\n${fieldStmt}`);
    }
    
    for (const [methodName, callsArray] of Object.entries(info.methods)) {
        const calls = callsArray.join('\n        ');
        const pattern = new RegExp(`((?:public|private) String ${methodName}\\([^)]*\\)\\s*\\{[^}]*?)(return "vistas/[^"]+";)`);
        
        content = content.replace(pattern, (match, p1, p2) => {
            if (p1.includes(callsArray[0])) return match; // already added
            return p1 + calls + '\n        ' + p2;
        });
    }
    
    fs.writeFileSync(filePath, content, 'utf-8');
}

console.log('Controllers updated');
