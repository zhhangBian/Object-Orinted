import random
from datetime import datetime


class DataGenerator:

    def genData(self, max_depth):
        ans = self.genExpr(max_depth)
        return ans

    def genExpr(self, depth):
        flag = random.randint(0, 1)
        ans = ""
        if depth == 0 or flag == 0:
            ans = self.genBlank()
            symbol = random.randint(0, 1)
            if symbol:
                ans += self.genAddorSub() + self.genBlank()
            ans += self.genTerm(depth) + self.genBlank()
        else:
            ans = self.genExpr(depth) + self.genAddorSub() + self.genBlank() + self.genTerm(depth) + self.genBlank()
        return ans

    def genTerm(self, depth):
        flag = random.randint(0, 1)
        ans = ""
        if flag == 0:
            symbol = random.randint(0, 1)
            if symbol:
                ans = self.genAddorSub() + self.genBlank()
            ans += self.genFactor(depth)
        else:
            ans = self.genTerm(depth) + self.genBlank() + "*" + self.genBlank() + self.genFactor(depth)
        return ans

    def genFactor(self, depth):
        symbol = random.randint(0, 2)
        if symbol == 0:
            return self.genVariFactor()
        elif symbol == 1:
            return self.genConstFactor()
        else:
            if depth:
                return self.genExprFactor(depth - 1)
            symbol = random.randint(0, 1)
            if symbol == 0:
                return self.genVariFactor()
            else:
                return self.genConstFactor()

    def genVariFactor(self):
        return self.genPower()

    def genConstFactor(self):
        return self.genSignedInteger()

    def genExprFactor(self, depth):
        ans = "(" + self.genExpr(depth) + ")"
        symbol = random.randint(0, 1)
        if symbol:
            ans += self.genBlank() + self.genIndex()
        return ans

    def genPower(self):
        ans = "x"
        symbol = random.randint(0, 1)
        if symbol:
            ans += self.genBlank() + self.genIndex()
        return ans

    def genIndex(self):
        ans = "^" + self.genBlank()
        symbol = random.randint(0, 1)
        if symbol:
            ans += "+"
        ans += self.genUnsignedInteger()
        return ans

    def genSignedInteger(self):
        ans = ""
        symbol = random.randint(0, 1)
        if symbol:
            ans = self.genAddorSub()
        ans += self.genUnsignedInteger()
        return ans

    def genUnsignedInteger(self):
        ans = self.genInt()
        symbol = random.randint(0, 1)
        while symbol:
            ans += self.genInt()
            symbol = random.randint(0, 1)
        return ans

    def genInt(self):
        ans = str(random.randint(0, 9))
        return ans

    def genBlank(self):
        ans = ""
        # symbol = random.randint(0, 4)
        # if symbol == 1:
        #     ans = " "
        # elif symbol == 2:
        #     ans = "\t"
        # elif symbol == 3:
        #     ans = " " + self.genBlank()
        # else:
        #     ans = "\t" +self.genBlank()
        return ans

    def genAddorSub(self):
        ans = ""
        symbol = random.randint(0, 1)
        if symbol == 0:
            ans = "+"
        else:
            ans = "-"
        return ans


dataGenerator = DataGenerator()
num_test = 100
for i in range(num_test):
    print(dataGenerator.genData(1))
