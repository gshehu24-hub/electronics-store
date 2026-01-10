#!/usr/bin/env python3
import sys
import os
import argparse

def strip_comments_from_java(source: str) -> str:
    out = []
    i = 0
    n = len(source)
    in_sline = False
    in_mline = False
    in_string = False
    in_char = False
    escape = False
    while i < n:
        c = source[i]
        # handle end of single-line comment
        if in_sline:
            if c == '\n':
                in_sline = False
                out.append(c)
            # else skip
            i += 1
            continue
        # handle end of multi-line comment
        if in_mline:
            if c == '*' and i+1 < n and source[i+1] == '/':
                in_mline = False
                i += 2
            else:
                i += 1
            continue
        # handle string/char escapes
        if in_string:
            out.append(c)
            if escape:
                escape = False
            else:
                if c == '\\':
                    escape = True
                elif c == '"':
                    in_string = False
            i += 1
            continue
        if in_char:
            out.append(c)
            if escape:
                escape = False
            else:
                if c == '\\':
                    escape = True
                elif c == "'":
                    in_char = False
            i += 1
            continue
        # not in any special state
        if c == '/' and i+1 < n:
            nxt = source[i+1]
            if nxt == '/':
                in_sline = True
                i += 2
                continue
            if nxt == '*':
                in_mline = True
                i += 2
                continue
        if c == '"':
            in_string = True
            out.append(c)
            i += 1
            continue
        if c == "'":
            in_char = True
            out.append(c)
            i += 1
            continue
        out.append(c)
        i += 1
    return ''.join(out)


def process_file(path: str, backup: bool = True):
    with open(path, 'r', encoding='utf-8') as f:
        src = f.read()
    new = strip_comments_from_java(src)
    if new == src:
        return False
    if backup:
        bak = path + '.bak'
        with open(bak, 'w', encoding='utf-8') as f:
            f.write(src)
    with open(path, 'w', encoding='utf-8') as f:
        f.write(new)
    return True


def main():
    parser = argparse.ArgumentParser(description='Strip comments from Java source files')
    parser.add_argument('paths', nargs='+', help='Directories or files to process')
    parser.add_argument('--ext', default='.java', help='File extension to process')
    parser.add_argument('--no-backup', dest='backup', action='store_false', help='Do not create .bak backups')
    args = parser.parse_args()

    changed = []
    for p in args.paths:
        if os.path.isdir(p):
            for root, dirs, files in os.walk(p):
                for fn in files:
                    if fn.endswith(args.ext):
                        full = os.path.join(root, fn)
                        try:
                            if process_file(full, backup=args.backup):
                                changed.append(full)
                        except Exception as e:
                            print(f'ERROR processing {full}: {e}', file=sys.stderr)
        elif os.path.isfile(p):
            if p.endswith(args.ext):
                if process_file(p, backup=args.backup):
                    changed.append(p)
    for c in changed:
        print('Stripped comments:', c)
    print('Done. Files changed:', len(changed))

if __name__ == '__main__':
    main()
