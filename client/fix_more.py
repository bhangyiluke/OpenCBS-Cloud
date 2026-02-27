import os
import re

for root, dirs, files in os.walk('src'):
    for file in files:
        if file.endswith('.ts'):
            path = os.path.join(root, file)
            with open(path, 'r') as f:
                content = f.read()
            original = content
            
            # fix action interface
            if 'action.interface.ts' in path:
                content = content.replace('readonly type: any;', 'readonly type: string;')
            
            # NglModule.forRoot
            content = content.replace('NglModule.forRoot()', 'NglModule')
            
            # fix cannot find name 'of'
            if "import { of } from 'rxjs'" not in content and "import { Observable, of } from 'rxjs'" not in content:
                content = re.sub(r"import\s*\{\s*Observable\s*\}\s*from\s*['\"]rxjs['\"];", "import { Observable, of } from 'rxjs';", content)

            if "import { of } from 'rxjs'" not in content and "import { Observable, of } from 'rxjs'" not in content:
                if 'of(' in content and 'import ' in content:
                    content = content.replace("import { Observable } from 'rxjs';", "import { Observable, of } from 'rxjs';")
            
            # fix "Property 'of' does not exist on type 'typeof Observable'."
            content = content.replace("Observable.of(", "of(")
            
            # TooltipModule
            if "import { TooltipModule } from 'primeng/api';" in content:
                content = content.replace("import { TooltipModule } from 'primeng/api';", "import { TooltipModule } from 'primeng/tooltip';")
            
            # DropdownModule
            if "import { DropdownModule } from 'primeng/api';" in content:
                content = content.replace("import { DropdownModule } from 'primeng/api';", "import { DropdownModule } from 'primeng/dropdown';")
            
            # DataTable
            if "import { DataTable } from 'primeng/api';" in content:
                content = content.replace("import { DataTable } from 'primeng/api';", "import { Table as DataTable } from 'primeng/table';")
            if "import { DataTable } from 'primeng/primeng';" in content:
                content = content.replace("import { DataTable } from 'primeng/primeng';", "import { Table as DataTable } from 'primeng/table';")
            
            if original != content:
                with open(path, 'w') as f:
                    f.write(content)

with open('src/app/core/store/redux-base/redux.base.effects.ts', 'r') as f:
    rbe = f.read()
rbe = rbe.replace('static getConfig(actions$: Actions, baseActions: ReduxBaseActions, method) {', 'static getConfig(actions$: Actions, baseActions: ReduxBaseActions, method: any): Observable<any> {')
with open('src/app/core/store/redux-base/redux.base.effects.ts', 'w') as f:
    f.write(rbe)
