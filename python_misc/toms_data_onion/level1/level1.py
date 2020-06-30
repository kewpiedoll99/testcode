from typing import List
import base64
from toms_data_onion.level1.tuple85 import Tuple85
import os

def decode85():
    THIS_FOLDER = os.path.dirname(os.path.abspath(__file__))
    filename = os.path.join(THIS_FOLDER, "tomdalling1.txt")
    with open(filename, "rb") as in_file:
        data = in_file.read()
    tuple85s: List[Tuple85] = tuple85fy(data)
    for tuple85 in tuple85s:
        bignum = tuple85.to_base10
        print(f"bignum: {bignum}")
        # binbignum = base64.b32encode(bignum)
        # print(f"binbignum: {binbignum}")

    # print(f"decoded len: {len(decoded)}")
    # decoded += b"\0\0"
    # print(f"decoded len: {len(decoded)}")
    # # decoded = codecs.decode(decoded, "unicode_escape")
    # decoded = decoded.decode
    # print(f"Decoded:\n{decoded}")

def tuple85fy(data: bin) -> List[Tuple85]:
    tuplefied: List[Tuple85] = []
    for i in range(0, len(data), 5):
        chunk = tuple(data[i:i+5])
        tuplefied.append(Tuple85(chunk))
    return tuplefied


if __name__ == '__main__':
    decode85()
