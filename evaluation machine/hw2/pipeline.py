import json
import re
import sympy
import subprocess
from tqdm import tqdm
from genData import genData
from gen import gen
from subprocess import STDOUT, PIPE


def execute_java(stdin, fname):
    cmd = ['java', '-jar', fname]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    # Decode the stdout bytes to a string and split it into lines
    lines = stdout.decode().strip().split('\n')
    return lines


def main(fname, std, times=100):
    exprDict = dict()
    for _ in tqdm(range(times)):
        poly = genData()
        # print(poly.replace("**", "^"))
        output_lines2 = execute_java(poly.replace("**", "^"), fname)
        output_lines = execute_java(poly.replace("**", "^"), std)
        # print(output_lines[0])
        forSympy = re.sub(r'\b0+(\d+)\b', r'\1', output_lines[0].replace("^", "**"))
        # f = sympy.parse_expr(forSympy)
        try:
            g = sympy.parse_expr(output_lines[1].replace("^", "**"))
            h = sympy.parse_expr(output_lines2[0].replace("^", "**"))
            # sympy.simplify(sympy.expand(h))
            if h.equals(g):
                # print("AC : " + str(cnt))
                x = len(output_lines2[0]) / len(output_lines[1])
                # print(output_linee[0].replace("**","^"))
                # print(output_lines[1])
                if x < 1.0:
                    x = 1.0
                if x > 1.5:
                    x = 1.5
                exprDict[poly] = (
                    -31.8239 * x * x * x * x + 155.9038 * x * x * x - 279.2180 * x * x + 214.0743 * x - 57.9370, g)
                pass
            else:
                print("!!WA!! with " + "poly : ")
                print(poly.replace("**", "^"))
                print("yours: " + output_lines2[0])
                print("sympy: ", end="")
                print(output_lines[1])
                return
        except Exception as e:
            print(e)
            print("!!WA!! with poly : ")
            print(poly.replace("**", "^"))
            print("yours: " + output_lines2[0])
            print("sympy: ", end="")
            print(output_lines[1])
            return
            pass
    sorted_exprDict = sorted(exprDict.items(), key=lambda x: x[1][0], reverse=False)
    print("worst score (x): " + str(round(sorted_exprDict[0][1][0] * 15 + 85, 1)))
    print("best score (x): " + str(round(sorted_exprDict[-1][1][0] * 15 + 85, 1)))


if __name__ == '__main__':
    main('6.jar', 'std.jar', 500)
