import sympy
import re
from sympy import symbols
from sympy import sin
from sympy import cos
import subprocess
def execute_java(stdin, fname):
    cmd = ['java', '-jar', fname]
    proc = subprocess.Popen(cmd, stdin=subprocess.PIPE, stdout=subprocess.PIPE, stderr=subprocess.STDOUT)
    stdout, stderr = proc.communicate(stdin.encode())
    # Decode the stdout bytes to a string and split it into lines
    lines = stdout.decode().strip().split('\n')
    return lines

output = "36*exp((exp((exp(1)+1))+x))-180-exp((exp((exp(1)+1))+x+9))*40-exp((18+exp((exp(1)+1))))*400-exp((exp((1+exp(1)))+x*2))-exp(exp((exp(1)+1)))*324-exp(1)+exp((exp((1+exp(1)))+9))*720"
h = sympy.parse_expr(output.replace("^", "**"))
output3 = "3\n"+output
#output_lines = execute_java(output3.replace("**", "^"), 'std.jar')
#h = sympy.parse_expr(output_lines[0].replace("^", "**"))
forSympy = re.sub(r'\b0+(\d+)\b', r'\1', output.replace("^","**"))
f = sympy.parse_expr(forSympy)
output2 = "720*exp((9+exp((1+exp(1)))))+36*exp((x+exp((1+exp(1)))))-exp(1)-exp((2*x+exp((1+exp(1)))))-40*exp((9+x+exp((1+exp(1)))))-180-324*exp(exp((1+exp(1))))-400*exp((18+exp((1+exp(1)))))"
g = sympy.parse_expr(output2.replace("^", "**"))
#output4 = "exp((exp((exp((20+exp((28+2*exp((exp((20+exp(-18)))+9))+exp(15)*2-2*exp((x^2*exp(1)+exp(-17)))+2*exp(-18)))))+9))+exp(15)-exp((exp(-2)-exp(1)*7+exp((x+1))+exp(-17)+exp(-9)))+14))+exp((14-exp((exp((1+2*x))+exp(-17)))+exp(15)+exp((9+exp((exp((exp(5)+1))*2+21+exp((2+exp(5)*2))))))))"
#gg = sympy.parse_expr(output2.replace("^", "**"))

print(sympy.simplify(h).equals(g))