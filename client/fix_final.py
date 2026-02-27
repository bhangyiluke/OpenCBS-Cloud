import os
import re

# 3. event-manager.module.ts
path = 'src/app/containers/event-manager/event-manager.module.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace('ModuleWithProviders {', 'ModuleWithProviders<any> {')
with open(path, 'w') as f:
    f.write(text)

# 7. core.module.ts
path = 'src/app/core/core.module.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace('ModuleWithProviders {', 'ModuleWithProviders<any> {')
with open(path, 'w') as f:
    f.write(text)

# 5. loan.module.ts
path = 'src/app/containers/loan/loan.module.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("import { TooltipModule } from 'primeng/api';", "import { TooltipModule } from 'primeng/tooltip';")
with open(path, 'w') as f:
    f.write(text)

# 15. cbs-form.module.ts
path = 'src/app/shared/modules/cbs-form/cbs-form.module.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("NglModule.forRoot(),", "NglModule,")
with open(path, 'w') as f:
    f.write(text)

# 16. test.ts
path = 'src/test.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("declare const require: any;", "declare const require: { context: any };")
with open(path, 'w') as f:
    f.write(text)

# 14. payee-form-modal.component.ts
path = 'src/app/shared/components/payee-form-modal/payee-form-modal.component.ts'
with open(path, 'r') as f:
    text = f.read()
text = re.sub(r"import \{.*?\} from '@angular/core/src/metadata/lifecycle_hooks';", "", text)
with open(path, 'w') as f:
    f.write(text)

# 11. message.service.ts
path = 'src/app/core/store/message-broker/message.service.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("this.queueNumber = 0;", "this.queueNumber = '0';")
with open(path, 'w') as f:
    f.write(text)

# 8. auth.reducer.ts
path = 'src/app/core/store/auth/auth.reducer.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("type AuthState = Map<string, any>;", "type AuthState = any;")
with open(path, 'w') as f:
    f.write(text)

# 4. loan-app-new.component.ts
path = 'src/app/containers/loan-application/loan-app-new/loan-app-new.component.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("import * as Big from 'big.js';", "import Big from 'big.js';")
text = text.replace("let totalAmount = Big(0);", "let totalAmount = new Big(0);")
text = text.replace("totalAmount = totalAmount.plus(Big(asset.value));", "totalAmount = totalAmount.plus(new Big(asset.value));")
with open(path, 'w') as f:
    f.write(text)
    
path = 'src/app/core/store/loan-application/loan-application-form/loan-application.utils.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("import * as Big from 'big.js';", "import Big from 'big.js';")
text = text.replace("Big(totalAmount)", "new Big(totalAmount)")
text = text.replace("Big(val.value)", "new Big(val.value)")
with open(path, 'w') as f:
    f.write(text)

# 13. fields-readonly.component.ts
path = 'src/app/shared/components/cbs-fields-readonly/fields-readonly.component.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("profileType === 'profiles/people'", "(profileType as any) === 'profiles/people'")
text = text.replace("profileType === 'profiles/company'", "(profileType as any) === 'profiles/company'")
with open(path, 'w') as f:
    f.write(text)

# effect typings
path = 'src/app/core/store/auth/auth.effect.ts'
with open(path, 'r') as f:
    text = f.read()
text = text.replace("Observable<never>", "any")
with open(path, 'w') as f:
    f.write(text)
