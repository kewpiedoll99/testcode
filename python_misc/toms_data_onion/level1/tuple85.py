from typing import Tuple


class Tuple85:
    _five_tuple: Tuple[int] = ()

    def __init__(self, toople):
        if len(toople) > 5:
            raise IndexError
        if len(toople) < 5:
            for i in range(0, (5 - len(toople))):
                self._five_tuple += (*self._five_tuple, b"u")

    def to_base10(self):
        val: int = 0
        for i in range(4, 0):
            val += (self._five_tuple[(4 - i)] - 33) * 85**i
        return val

    def to_string(self):
        return "(" + ','.join(list[self._five_tuple]) + ")"
