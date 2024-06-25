# HW3与重构分享

只完成HW3的要求很简单，查看了一下代码的数据，第三次作业我总共净增加了63行代码就完成了需求。

但是在写的过程中我愈发感受到原本的代码的拘束，显得束手束脚，便一气之下进行了重构，在完成功能的基础上让整体代码结构变得更为清晰。

## HW3分享

主要的任务就是求导和自定义函数的嵌套定义。

### 求导

在刚开始写作本次作业时，我的架构和[刚开始时](http://oo.buaa.edu.cn/assignment/503/discussion/1459)并没有产生太大的变化，依旧是采用了`Expr-Term-Factor`这样的基础架构，在原本单项式的基础上添加为了$\text{Unit}=ax^b\text{exp}(expr)$。

分析一下求导的需求，如果你也采用了这种类多项式结构，那么对Expr的求导最后的抓手都是这样的一个个Unit，完成对Unit的求导即可。

从数学上表示，即为：$\text{dx}(\text{Unit})=a(bx^{b-1}+\text{dx}(expr))\text{exp}(expr)$，落回到程序上，即分别解决x^b多项式求导和指数exp的求导，再加起来，为：

```java
//derivation函数是Factor接口中提供的函数
public Expression derivation() {
    Expression resultExpression = new Expression();
    expExpr = ...;
    polyExpr = ...;
    resultExpression = expExpr.Add(polyExpr);
    
    return resultExpression;
}
```

完成了这样对基本因子Unit的求导，剩下的部分就可以开始愉快地进行迭代调用了，对exp括号中的表达式（我存的是表达式Expr）调用求导函数，解析到Term时向下继续调用Term的求导函数，进而最后落到对Unit的求导。这样的递归调用总有一个尽头，也就是最后的一个个Unit组成的多项式。

总结一下流程，也就是：

1. 遇到dx，调用对Expr的求导函数，进行`parseExpression.derivation()`，该函数返回一个Expr，即求导后的结果。
2. 在Expr的求导函数中，由于Expr由一个个Term相加而成，对每个Term调用`term.derivation()`函数，该函数返回的依然是一个Expr（单项式的求导由于exp的存在会使得结果可能是Expr）。
3. 在Term的求导函数中，调用对Unit的求导函数，返回的是Expr，由于存在`exp(Expr)`这种存在，在对exp求导时，会返回到1调用Expr的求导函数，直至递归结束。

对于这个步骤，有几点是可以注意的：

1. 之前的两次作业相信大家已经有了自己的表达式化简函数，在`parseExpression`、对exp中的Expr进行处理前，都可以对这些Expr进行化简，减少递归深度，提高程序性能。
2. 注意深浅拷贝问题

### 自定义函数

为了提高我的义父在评测我程序时的速度，减少因为频繁调用自定义函数造成的频繁字符串替换-解析等流程，我在第二次作业中针对自定义函数做了如下的优化：

1. 由于我的程序可以支持自动多变量计算，我把每个自定义函数的等号右边全算了一遍。
2. 算完以后得到的是化简后的多项式相加结果，恼人的`exp()^0`等形式全部消失了，在后续对自定义函数的调用时可以提高很多性能
3. （当然这种优化只有在多次调用长得很丑很长的函数时才有用，而评测机生成的数据往往是这样的）

回到本次作业时，每当解析到原先的函数时，我会查阅之前建立的函数名list，将相关的函数表达式待会，这样做到了支持函数的嵌套定义。

删除了大量细节的伪代码如下：

```java
// Definer.java
public void AddFuncDefine(String funcExpression) {
    String paraString = funcExpression.substring(...);
    String funcString = GetFuncReplaced(...);
    this.funcMap.put(funcName, funcString);
}

//得到带入的结果
private String GetFuncReplaced(...) {
    // 直接把函数先算一遍
    Lexer lexer = new Lexer(funOriginalExpression);
    Parser parser = new Parser(lexer, this);
    String funcExpression = parser.parseExpression().toString();

    //...

    return funcString;
}
```

## 关于重构

> 重构之心早已有之，但是在HW2时需求还是不那么强烈，直到做本次作业时，实现一个多变量函数的全微分让我变得十分难受。
>
> （当然多变量这些都是没必要的，纯属第一次作业时错误预判，后面又不舍得删:clown_face:）

在写代码时，我一直在思考Term这一个结构的作用是什么。Factor可以视作表达式的基本因子，作为接口由Expression和Unit分别实现，在表达式的形式上也满足如$\text{Expr}=\sum\text{Unit}*\text{Expr}$的形式。

那么Term呢？

或许你会说，Term是由表达式中加减符号分隔的一个个元素，但是这种分割**注定是暂时的**：最后的化简需要将括号全部都打开，能合并的都合并。这时候加减符号分割的就只是一个个基本元素Unit了。再用Term去体现代码的结构，那么就是$\text{Expr}=\sum\text{Term},\text{Term}=\text{Unit}$，即**化简后每个Term中只有唯一的一个Unit**，没有先前的Expr，唯一的Factor就是Unit。

这样的操作使得我的代码在这样的情景下十分丑陋：表达式化简结束后，在转换为字符串环节，此时Term中的`factorList`只有Unit而为Expr（化简都拆出去了），为了取出这唯一的Unit，甚至需要做指针的类型转换！

```java
Unit thisUnit = (Unit) this.factorList.get(0);
Unit otherUnit = (Unit) otherTerm.factorList.get(0);
```

到做HW3时，我开始感觉Term这样一个类，保持`factorList`实际上是没有必要的，可以维持一个暂时的Expr，解析到别的Factor时直接乘进去，**并不需要严格地按照Term分隔表达式**。

至此可以得到结论：**Term只是在解析表达式的中间过程中一个暂时的数据形式，在最后的阶段完全可以用**$\text{Expr}=\sum\text{Unit}$**的形式来表现**。

于是，在这个结论的基础上，我进行了如下的重构：

1. 删除了Term的数据存储单元，即Term不再有数据（有也只是在解析表达式时暂时存一下），转而变为了解析时的一个行为对象、一个方法集合。

2. 在调用`parseTerm`函数时，不再返回原先的Term类，而是直接返回Expr，具体的流程为：

   `parseTerm`开始后，保留一个暂时的新的Expr，直接与后续解析到的Factor相乘，不再暂存存储相乘Factor的list

3. 至此，Term不再是一个存储数据的类，而变成了一个解析由`*`相连的因子的方法集合

我认为，这样的好处有：

1. 数据的结构更为灵活。使得表达式$\text{Polynome}=\sum\text{Molynome}$的形式得到了统一。并且唯二的数据单元就是Unit和Expr，都实现了Factor的接口，这样操作使得类的统一性更高。同质化的操作可以直接在接口中进行定义。
2. 尽管直接进行了相乘，但是运行速度却没有影响，一方面最后的化简总归要这样展开，区别就是集中展开还是解析到就乘进去；另一方面由于及时化简，不会造成项挤压太多造成运算缓慢的问题。
3. 自己调整过后的架构自己代码写的舒服。

## 总结

这次的作业主要就是增加了求导这种运算算符，总的来说不算难。回看这三次作业，重点还是在于怎么合适地表示数据单元，怎么统一行为操作。优秀的架构固然值得借鉴，自己调整后的还是写的最舒服。

最后说一句性能优化，经历了HW2特意去掉括号的惨挂，我现在的策略就是很保守的提公因子。至于HW3跟HW2一致就好，求导只是计算方式不是表现方式。把括号加好是最重要的:clown_face:
