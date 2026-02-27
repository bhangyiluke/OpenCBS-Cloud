import os, re

for root, dirs, files in os.walk('src'):
    for file in files:
        if file.endswith('.ts'):
            path = os.path.join(root, file)
            with open(path, 'r') as f:
                content = f.read()
            original = content
            
            # RxJS
            content = content.replace("'rxjs/Observable'", "'rxjs'")
            content = content.replace('"rxjs/Observable"', "'rxjs'")
            content = content.replace("'rxjs/Rx'", "'rxjs'")
            content = content.replace('"rxjs/Rx"', "'rxjs'")
            content = content.replace("'rxjs/internal/Rx'", "'rxjs'")
            
            # ReflectiveInjector removal from import
            content = re.sub(r'ReflectiveInjector,\s*', '', content)
            content = re.sub(r',\s*ReflectiveInjector', '', content)
            content = re.sub(r'import\s*\{\s*ReflectiveInjector\s*\}\s*from\s*[\'"]@angular/core[\'"];', '', content)
            
            # async to waitForAsync
            content = re.sub(r'\basync\b(.*?)from\s+[\'"]@angular/core/testing[\'"]', r'waitForAsync as async\1from \'@angular/core/testing\'', content)
            
            # NglModule.forRoot() -> NglModule
            content = content.replace('NglModule.forRoot()', 'NglModule')
            
            # EffectResult errors: remove type annotation on createEffect assignments
            # e.g., myEffect$: Observable<any> = createEffect(...)
            content = re.sub(r'([a-zA-Z0-9_$]+)\s*:\s*[a-zA-Z0-9_<>\s,]+(?=\s*=\s*createEffect)', r'\1', content)
            
            # Primeng imports
            content = content.replace("'primeng/primeng'", "'primeng/api'") # Just a placeholder, some might need specific components.
            # Actually let's do:
            content = content.replace("import { DataTable } from 'primeng/primeng';", "import { Table as DataTable } from 'primeng/table';")
            content = content.replace("import { TooltipModule } from 'primeng/primeng';", "import { TooltipModule } from 'primeng/tooltip';")
            content = content.replace("import { DropdownModule } from 'primeng/primeng';", "import { DropdownModule } from 'primeng/dropdown';")
            
            if original != content:
                with open(path, 'w') as f:
                    f.write(content)
