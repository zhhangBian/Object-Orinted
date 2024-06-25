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
    return stdout.decode().strip()


def judge(user, times):
    std = 'untitled4.jar'
    exprDict = dict()
    for _ in tqdm(range(times)):
        poly = gen()
        #print(poly.replace("**", "^"))
        output_lines = execute_java(poly.replace("**", "^"), user)
        output_lines2 = execute_java(poly.replace("**", "^") + "-" + "(" + output_lines + ")", std)
        output_lines3 = execute_java(poly.replace("**", "^"), std)
        try:
            # sympy.simplify(sympy.expand(h))
            if output_lines2 == "0":
                # print("AC : " + str(cnt))
                x = len(output_lines) / len(output_lines3)
                if (x < 1.0):
                    x = 1.0
                    print("You're better than best!")
                if (x > 1.5):
                    x = 1.5
                    print("You're too low!")
                exprDict[poly] = (
                    -31.8239 * x * x * x * x + 155.9038 * x * x * x - 279.2180 * x * x + 214.0743 * x - 57.9370,
                    output_lines, output_lines3)
                pass
            else:
                print("WA with " + "poly : ")
                print(poly.replace("**", "^"))
                print("yours: " + output_lines)
                print("mine: " + output_lines3)
                return
        except Exception as e:
            print(e)
            print("Exception with poly : ")
            print(poly.replace("**", "^"))
            print("yours: " + output_lines)
            print("mine: " + output_lines3)
            return
            pass
    sorted_exprDict = sorted(exprDict.items(), key=lambda x: x[1][0], reverse=False)
    print("worst score (x): " + str(round(sorted_exprDict[0][1][0] * 15 + 85, 1)))
    print("with your result:" + str(sorted_exprDict[0][1][1]))
    print("with best result:" + str(sorted_exprDict[0][1][2]))
    print("best score (x): " + str(round(sorted_exprDict[-1][1][0] * 15 + 85, 1)))
    print("with your result:" + str(sorted_exprDict[-1][1][1]))
    print("with best result:" + str(sorted_exprDict[-1][1][2]))


def main(*args):
    if len(args) == 0:
        print("Wrong Input!!!")
        return
    times = int(args[-1])
    for user in args:
        if (user == args[-1]):
            break
        if (user != 'untitled4.jar'):
            print("Now Judging user " + user + ":")
            judge(user, times)


if __name__ == '__main__':
    import sys

    arg_list = [arg for arg in sys.argv[1:]]
    main('mine.jar', '100')  # 这行替换成arg_list应该就行
