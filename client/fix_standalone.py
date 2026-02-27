import os
import re

def fix_standalone(file_path):
    with open(file_path, 'r', encoding='utf-8') as f:
        content = f.read()
    
    # regex to find @Component, @Directive, @Pipe decorators
    # and their opening brace
    pattern = re.compile(r'(@(Component|Directive|Pipe)\s*\(\s*\{)')
    
    new_content = content
    offset = 0
    for match in pattern.finditer(content):
        # check if 'standalone' is already in the decorator content
        # we need to find the closing brace of the decorator
        start_idx = match.end()
        brace_count = 1
        end_idx = start_idx
        while brace_count > 0 and end_idx < len(content):
            if content[end_idx] == '{':
                brace_count += 1
            elif content[end_idx] == '}':
                brace_count -= 1
            end_idx += 1
        
        decorator_body = content[start_idx:end_idx]
        if 'standalone' not in decorator_body:
            # find where to insert
            # we can insert right after '{'
            insertion = '\n  standalone: false,'
            new_content = new_content[:match.end() + offset] + insertion + new_content[match.end() + offset:]
            offset += len(insertion)
            
    if new_content != content:
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(new_content)
        return True
    return False

count = 0
for root, dirs, files in os.walk('src'):
    for file in files:
        if file.endswith('.ts'):
            if fix_standalone(os.path.join(root, file)):
                count += 1

print(f'Fixed {count} files.')
