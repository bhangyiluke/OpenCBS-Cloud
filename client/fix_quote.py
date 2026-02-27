import os
for root, dirs, files in os.walk('src'):
    for file in files:
        if file.endswith('.ts'):
            path = os.path.join(root, file)
            with open(path, 'r') as f:
                content = f.read()
            original = content
            # The backslash literal from python was `\'`
            content = content.replace(r"\'@angular/core/testing\'", "'@angular/core/testing'")
            if "EffectResult<Action<string>>" in content:
                content = content.replace("EffectResult<Action<string>>", "any")
            content = content.replace("import { Action } from '@ngrx/store';", "import { Action } from '@ngrx/store';\ntype EffectResult<T> = any;")
            if original != content:
                with open(path, 'w') as f:
                    f.write(content)
