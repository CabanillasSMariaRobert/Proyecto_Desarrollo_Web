const fs = require('fs');
const path = require('path');
const dir = 'src/main/java/com/ecommerce/tienda_kalza/servicios/publico';

fs.readdirSync(dir).forEach(file => {
    if (file.endsWith('.java')) {
        const filePath = path.join(dir, file);
        let content = fs.readFileSync(filePath, 'utf-8');
        if (content.charCodeAt(0) === 0xFEFF) {
            content = content.slice(1);
            fs.writeFileSync(filePath, content, 'utf-8');
            console.log('Removed BOM from ' + file);
        }
    }
});
