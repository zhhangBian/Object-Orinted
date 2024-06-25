import random
import time
import re
from sympy import symbols, Eq, solve

d, wel, v = 0, 0, [0] * 10
dd = 7


def print_expression(con, res):
    c = ""
    a = 1
    for i in range(1, 1):
        x = random.randint(0, 1)
        if x % 2 == 1:
            c += "+"
        else:
            c += "-"
            a = -a
    if a * res == -1:
        c += "-"
    else:
        c += "+"
    return c


def qdl():
    t = random.randint(0, 4)
    s = "0" * t
    return s


def dimension(s):
    x = random.randint(0, 7)
    if x == 0 or x == 3 or x == 4 or x == 7:
        return "(" + s + ")"
    elif x == 6:
        return "exp((" + s + "))"
    else:
        return "(dx(" + s + "))"


def dfs(d, wel):
    shai = random.randint(0, 5)
    if d == dd + 1 or shai == 3:
        x = random.randint(0, 1)
        y = random.randint(0, 1)
        z = random.randint(0, 3)
        u = 0
        if wel == -1:
            u = random.randint(0, v[0] - 1)
        elif wel == -2:
            u = random.randint(0, v[1] - 1)
        elif wel == -3:
            u = random.randint(0, v[2] - 1)
        elif wel == 1:
            u = random.randint(0, 2)

        if x == 0:
            if y == 0:
                return (print_expression(random.randint(0, 2), 1) + str(chr(120 + u)) + "**" +
                        print_expression(random.randint(0, 1), 1) + qdl() + str(z))
            else:
                return (print_expression(random.randint(0, 2), 1) + str(chr(120 + u)) + "**" +
                        print_expression(random.randint(0, 1), 1) + qdl() + str(z))
        else:
            x = random.randint(0, 49999)
            y = random.randint(0, 49999)
            return print_expression(random.randint(0, 2), -1 + random.randint(0, 1) * 2) + qdl() + str(x * y % 20)

    if wel == 1:
        x = random.randint(0, 6)
    else:
        x = random.randint(0, 3)
    if x == 0:
        return dimension(dfs(d + 1, wel)) + print_expression(random.randint(0, 2), 0) + dimension(dfs(d + 1, wel))
    elif x == 1:
        return dimension(dfs(d + 1, wel)) + print_expression(random.randint(0, 2), 0) + dimension(dfs(d + 1, wel))
    elif x == 2:
        return dimension(dfs(d + 1, wel)) + "*" + dimension(dfs(d + 1, wel))
    elif x == 3:
        z = random.randint(0, 3)
        return dimension(dfs(d + 1, wel)) + "**" + print_expression(random.randint(0, 1), 1) + qdl() + str(z)
    elif x == 4:
        if v[0] == 3:
            return "f(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + "," + dimension(
                dfs(d + 1, wel)) + ")"
        elif v[0] == 2:
            return "f(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + ")"
        else:
            return "f(" + dimension(dfs(d + 1, wel)) + ")"
    elif x == 5:
        if v[1] == 3:
            return "g(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + "," + dimension(
                dfs(d + 1, wel)) + ")"
        elif v[1] == 2:
            return "g(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + ")"
        else:
            return "g(" + dimension(dfs(d + 1, wel)) + ")"
    else:  # x must be 6
        if v[2] == 3:
            return "h(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + "," + dimension(
                dfs(d + 1, wel)) + ")"
        elif v[2] == 2:
            return "h(" + dimension(dfs(d + 1, wel)) + "," + dimension(dfs(d + 1, wel)) + ")"
        else:
            return "h(" + dimension(dfs(d + 1, wel)) + ")"


def uname():
    random.seed(time.time())
    s = "0\n"
    v[0] = random.randint(1, 3)
    v[1] = random.randint(1, 3)
    v[2] = random.randint(1, 3)
    wel = -3
    v[2] = 1
    s = s + (print_expression(random.randint(0, 1), 1 - random.randint(0, 1) * 2) + dfs(1, wel))
    return s


def gen():
    expr = uname()
    toEval = re.sub(r'\b0+(\d+)\b', r'\1', expr)
    return str(expr)
