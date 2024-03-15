import subprocess
from subprocess import STDOUT, PIPE
import sympy


def execute_java(stdin, fname):
    cmd = ['java', '-jar', fname]
    proc = subprocess.Popen(cmd, stdin=PIPE, stdout=PIPE, stderr=STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    return stdout.decode().strip()


str = input()

str1 = execute_java(str, '1.jar')
str2 = execute_java(str, '2.jar')
str3 = execute_java(str, '3.jar')
str4 = execute_java(str, 'hw1.jar')
str5 = execute_java(str, '5.jar')
str6 = execute_java(str, '6.jar')
str7 = execute_java(str, '7.jar')

print("1: " + str1)
print("2: " + str2)
print("3: " + str3)
print("4: " + str4)
print("5: " + str5)
print("6: " + str6)
print("7: " + str7)