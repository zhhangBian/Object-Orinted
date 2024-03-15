import subprocess
#from tqdm import tqdm
from genData import genData
from gen import gen
from subprocess import STDOUT, PIPE


def execute_java(stdin, fname):
    cmd = ['java', '-jar', fname]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    # Decode the stdout bytes to a string and split it into lines
    return stdout.decode().strip()


def main(fname, std, times=100):
    exprDict = dict()
    for _ in range(times):
        poly = gen()
        #print(poly.replace("**", "^"))
        output_lines = execute_java(poly.replace("**", "^"), fname)
        output_lines2 = execute_java(poly.replace("**", "^") + "-" + "(" + output_lines + ")", std)
        output_lines3 = execute_java(poly.replace("**", "^"), std)
        try:
            # sympy.simplify(sympy.expand(h))
            if output_lines2 == "0":
                # print("AC : " + str(cnt))
                x = len(output_lines) / len(output_lines3)
                print(output_lines)
                print(output_lines3)
                if (x < 1.0):
                    x = 1.0
                if (x > 1.5):
                    x = 1.5
                exprDict[poly] = (
                    -31.8239 * x * x * x * x + 155.9038 * x * x * x - 279.2180 * x * x + 214.0743 * x - 57.9370,
                    output_lines)
                pass
            else:
                print("!!WA!! with " + "poly : ")
                print(poly.replace("**", "^"))
                print("yours: " + output_lines2)
                print("sympy: ", end="")
                print(output_lines)
                return
        except Exception as e:
            print(e)
            print("!!WAA!! with poly : ")
            print(poly.replace("**", "^"))
            print("yours: " + output_lines2)
            print("sympy: ", end="")
            print(output_lines)
            return
            pass
    sorted_exprDict = sorted(exprDict.items(), key=lambda x: x[1][0], reverse=False)
    print("worst score (x): " + str(round(sorted_exprDict[0][1][0] * 15 + 85, 1)))
    print("best score (x): " + str(round(sorted_exprDict[-1][1][0] * 15 + 85, 1)))


if __name__ == '__main__':
    main('2.jar', 'untitled4.jar', 100)
