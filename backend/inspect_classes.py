from pathlib import Path
import struct

root = Path('target/classes')
print('Class files at root:')
for p in sorted(root.glob('*.class')):
    print(p.name)

print('\nClass files under com:')
for p in sorted(root.glob('com/**/*.class')):
    print(str(p.relative_to(root)))


def get_class_name(path):
    data = path.read_bytes()
    if data[:4] != b'\xca\xfe\xba\xbe':
        return None
    constant_pool_count = struct.unpack('>H', data[8:10])[0]
    i = 10
    cp = [None]
    while len(cp) < constant_pool_count:
        tag = data[i]
        i += 1
        if tag == 1:  # Utf8
            length = struct.unpack('>H', data[i:i+2])[0]
            i += 2
            s = data[i:i+length].decode('utf-8')
            i += length
            cp.append(s)
        elif tag in {3, 4}:  # Integer/Float
            i += 4
            cp.append(None)
        elif tag in {5, 6}:  # Long/Double
            i += 8
            cp.append(None)
            cp.append(None)
        elif tag in {7, 8}:  # Class/String
            i += 2
            cp.append(None)
        elif tag in {9, 10, 11, 12, 15, 16, 18}:
            if tag == 15:
                i += 3
            elif tag == 16:
                i += 2
            else:
                i += 4
            cp.append(None)
        else:
            return None
    access_flags = struct.unpack('>H', data[i:i+2])[0]
    this_class = struct.unpack('>H', data[i+2:i+4])[0]
    return cp[cp[this_class]] if cp[this_class] is not None else None

print('\nClass internal names:')
for p in sorted(root.glob('*.class')):
    print(p.name, get_class_name(p))
for p in sorted(root.glob('com/**/*.class')):
    print(str(p.relative_to(root)), get_class_name(p))
