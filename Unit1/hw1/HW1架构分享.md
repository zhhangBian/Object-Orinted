# HW1架构分享

在本文中简单分享一下我在第一次作业中所采用的架构，自认为是一种还算方便的架构，可以实现括号嵌套、多变量计算等等，并且可扩展性较强。欢迎批评指正。

我的UML类图：

![src](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202402292051695.png)

## 数学上的思考

### 程序上的表示

第一单元的要求是完成一个表达式程序的计算，其核心在于如何采用合适的架构将变量、函数在程序中进行表示。并且，由于**可以预见到**后两次作业会加入多变量计算、对于函数（包括三角函数、自定义函数）的支持，我觉得一下几个问题是应该思考的：

- 怎么统一数和变量的计算
- 怎么预留出扩展性，让函数能够较为方便地接入
- 在递归下降角度，怎么从字符串中解析出对应的因子

自然的，会想到使用多项式进行表示，即采用$\sum C*x^{a_1}*y^{a_2}*f(x,y,z,...)$​​的表示方式来对表达式进行建模。即：

![](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202402291825452.png)

从代码角度，是这样的：

```java
// Unit.java
public class Unit implements Factor {
    private boolean isPositive;
    private BigInteger cofficient;
    private TreeMap<String, Polynome> MolyList;
    // TODO: 未来对函数的实现
    // TODO: private ArrayList<Func> funcArrayList;
}

// Molynome.java
// 这个类用来描述 x^a 这样的基元
public class Molynome {
    private String molyName;
    private BigInteger molyPower;
}
```

这样的表示有几个好处：

1. 将**数和变量的计算统一**了，比如1可以表示为$1=1*x^0$，$x^2$可以表示为$1*x^2$​​，数可以看作一种特殊的系数单项式。并且由于数的唯一位置是系数，不会出现`x*2y*3z^2`这种数字前一个后一个的混乱局面；
2. 预留出了**扩展**的接口，变量再多都不怕，可以向`TreeMap`中肆无忌惮地添加，叫abcd都可。并且以变量名为键后，不会由`x*y*x^2`的情况；
3. 对于可以相加的单项式，如`x+2x`，由以变量名做红黑树的键的特性，可以写一个比较方面的**有序比较的函数**来进行比较，且这个比较和变量的位置无关，即$x*y==y*x$。这样就能判断除系数外的部分是否相等，将表达式较为方便地化简，~~卷一卷性能分~~；
4. 和training的结构类似，可以借鉴部分代码（）

----

有了这样的表示后，Expr、Term、Factor的实现就是清晰的了。

我的Expr由多个Term相加组成；Term由多个Factor组成；Factor分别由Unit和Expr继承，重载`toString, CopyFactor`两个方法。这样的结构较好地保证了简洁性。
$$
\text{Expr} = \sum \text{Term} \\
\text{Term} = \prod \text{Factor} \\
\text{Factor} \leftarrow \text{Unit} \quad | \quad  \text{Expr}
$$

### 计算：Term的上升

实际的计算应该是什么样的？

我的所有计算都是自动完成的，不需要手动调用计算单元。在进行递归下降的过程中，处理表达式、项、因子的函数自动调用相关函数，进行自动化简。当进行到最后时，可以自动保证表达式的计算和化简。

> 递归下降依然是一种栈结构，不过是用OS在**函数调用过程中创建的函数栈**替代了在数据结构中我们**手动构建的表达式节点栈**。
>
> 如果采用了合适的递归下降结构（training中的就可以），可以天然地保证能够处理括号的嵌套，在递归的过程中完成了相应的栈的建立。不用去担心后续括号嵌套的问题。

- 在进行解析时，每解析到一个数字/变量（后文用单元Unit统称），就将其加入到当前的Term中，调用`AddFactor`，若有Unit则将其合并；
- 解析到加减符号+-，则将Term加入Expr中，调用`AddTerm`，判断是否是除系数外都相同的单项式，进行化简。
- 在这些加入过程中，编写相应的接口实现自动化简，这样能够保证计算过程中表达式的简洁性。

但是，这个过程并没有解决括号展开的过程，还需要对表达式进行化简。

为什么不是对项进行化简：

为了对括号进行展开，需要将`Term term1 = x*(x+1)`这样的形式进行展开。但是显然的，展开的结果是一个多项式，即一个表达式，应该用一个Expr来表示，这样在函数处理中不就遇到麻烦了吗！

如果对项进行化简，那么还是需要返回一个表达式。不如直接**把项上升为表达式**，直接对表达式进行化简。

对此，我的策略是统一为Expr的计算，在合适的位置对表达式进行自动的化简，将Term上升为Expr。以此来保证表达式在递归计算过程中的简洁性，保证计算的速度。

```java
// Parser.java
public Expression parseExpr() {
    //balabala...
    //只需要在这里调用化简即可，这是一个从内到外的递归过程
    return expression.ExprSimplify();
}

// Expression.java
// 将Term上升为Expr
public Expression ExprSimplify() {
    Expression finalExpr = new Expression();
    for (Term term : termList) {
        finalExpr.AddExpression(term.TermSimplify());
    }

    return finalExpr;
}
```

### 相关分析

相关复杂度：

![image-20240229205426192](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202402292054434.png)

可以支持这样的大规模计算，可以用来检查正确性：

![image-20240229182825297](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202402292055448.png)

## 相关注意事项

1. 表达式的预处理可以很方便。

   比如对`-2`，`-x`这一类负数使用字符串替换即可以变为减法，简化解析代码的书写量。彻底将`-`作为减号。

   还有对空格和`\t`，`^+`，`(-`，`*+`的处理，都可以使用预处理一步到位。

2. 深拷贝与浅拷贝

   很重要，在进行表达式乘法时要注意。比如`(expr)^a`，如果采用`expr.Mul(expr)`的方式，则会出现问题。

## 关于“面向对象”

在我看来，面向对象的核心在于在进行抽象后封装出一定接口，对这些接口进行编程。

如果在表达式计算中，需要在解析完成后手动调用计算，我觉得这是不方便的。这样，一方面会让最后的计算函数面对一个巨大的表达式（后缀/中缀），计算不方面；另一方面，这样将Expr、Term等对象仅作为了数据存储单元，没有很好地用到相应的行为逻辑，实际上也应该是有的。

面向什么样的接口？我采用了添加时自动化简、在解析时在处理表达式时返回化简后的结果，但是我依然感觉这样不太优雅，对Unit的实现没有很好地利用，Factor由Unit和Expr继承仅起到了表示表达式的数据存储功能，自我感觉没有很好地”面向对象“。

期待各位批评指正。
