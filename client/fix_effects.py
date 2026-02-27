import os
import re

def process_file(filepath):
    with open(filepath, 'r') as f:
        content = f.read()

    new_content = content.replace("ReflectiveInjector.resolveAndCreate([", "new ")
    new_content = re.sub(r'new\s+([a-zA-Z0-9_]+Actions)\]\)\s*\.get\(\1\)', r'new \1()', new_content)
    new_content = re.sub(r'ReflectiveInjector\.resolveAndCreate\(\s*([a-zA-Z0-9_]+Actions)\s*\)\s*\n*\s*\.get\(\1\)', r'new \1()', new_content)

    if '@Effect' in new_content:
        # replace import
        new_content = re.sub(r'import\s+\{([^}]*)\bEffect\b([^}]*)\}\s+from\s+[\'"]@ngrx/effects[\'"];', 
                             r'import {\1createEffect\2} from \'@ngrx/effects\';', new_content)
        
        # We need a proper parser for @Effect()
        # Find all occurrences of @Effect
        while True:
            match = re.search(r'@Effect\(([^)]*)\)\s+(?:public\s+|private\s+|protected\s+)?([a-zA-Z0-9_$]+(?:\s*:\s*[a-zA-Z0-9_<>\s,]+)?)\s*=\s*', new_content)
            if not match:
                break
            
            start_index = match.end()
            # find the end of the statement by handling {} () []
            brackets = 0
            end_index = start_index
            in_string = False
            string_char = ''
            
            for i in range(start_index, len(new_content)):
                ch = new_content[i]
                if not in_string:
                    if ch in ["'", '"', '`']:
                        in_string = True
                        string_char = ch
                    elif ch in ['{', '(', '[']:
                        brackets += 1
                    elif ch in ['}', ')', ']']:
                        brackets -= 1
                    elif ch == ';' and brackets == 0:
                        end_index = i
                        break
                else:
                    if ch == string_char and new_content[i-1] != '\\':
                        in_string = False
            
            # extract the expression
            expr = new_content[start_index:end_index]
            args = match.group(1).strip()
            if args:
                replacement = f"{match.group(2)} = createEffect(() => {expr}, {args});"
            else:
                replacement = f"{match.group(2)} = createEffect(() => {expr});"
                
            new_content = new_content[:match.start()] + replacement + new_content[end_index+1:]

    with open(filepath, 'w') as f:
        f.write(new_content)

for root, dirs, files in os.walk('src'):
    for file in files:
        if file.endswith('.ts'):
            process_file(os.path.join(root, file))

