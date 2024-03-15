---
teacher:荣文戈
group:5
member:
   - 22371345 贾博驿
   - 22373400 梁浩然
   - 22371056 孟烜宇
   - 22182610 杨馨悦
   - 22373017 卞卓航
   - 22373498 朱雄伟
   - 23060110 朱君凡
---

# 代码架构面对hw2的新增要求时可能会出现的问题及调整策略

经过讨论，我们从代码架构面对hw2的新增要求时可能会出现的问题及调整策略出发，细化到以下几个主题：

- hw1代码架构设计
- hw1架构面对hw2新增需求存在的问题
- 面对新需求的调整策略

会议由卞卓航同学主持，由贾博驿同学代表小组进行总结汇报，由朱雄伟同学进行会议记录。

------

## 第一个主题：hw1代码架构设计

该主题主要聚焦于hw1代码逻辑的组织架构，每位组员充分发表了自己代码架构设计的思路与不足之处。

首先，卞卓航同学提出自己架构设计的一些关键部分：

- 通过分析，发现最终展开的表达式（`多项式`）可分解为多个`单项式`，每个单项式均可表达为$ax^b$的形式，因此可以实现最小项形式的统一；
- 计算时将每一项实时进行合并操作，使得解析表达式支持自动判断合并；
- 读入表达式时进行字符串预处理，如`-1`转化为`0-1`，消去前导零等，解决符号在表达式第一位等问题。

朱雄伟同学对卞卓航同学的观点表示赞同，并补充了卞卓航同学的观点：

- 将单项式$ax^b$组织为单项式类`Mono`和多项式类`Poly`作为计算类，便于计算化简，`Expr`等逻辑类均实现`toPoly`方法；
- 在`Lexer`阶段处理`+++1`等表达式时，通过连续读取正负号判断整体正负性，在读取数字时同步处理前导零；

接着，与会其他同学表示自己的看法与以上两位同学的观点，并分享了自己代码架构设计的特点。

孟烜宇同学介绍了自己与朱雄伟同学实现方式不同的`Lexer`阶段连续符号处理的方法。

梁浩然同学介绍了自己在`Term`阶段通过添加`sign`标识符，从而标记整体正负性，化简运算的方法。

杨馨悦同学介绍了自己将`Factor`设为`Expr`和`Term`的父类，实现递归调用进行解析的方法。

- `Term`和`Expression`都存的是`ArrayList<Factor>`，方便进行乘法的计算，所有乘法都是用`expr*expr`。

贾博驿同学简要介绍了自己代码设计存在的问题：

- 递归下降分析文法时，架构层面存在错误继承；
- 带括号的式子在进行读取展开时错误地继承为`Factor`；
- 抽象层次过高，大量运用泛型等导致束手束脚。

大家相互了解了彼此的代码设计亮点与不足，表示十分受用，并会吸取其他同学犯错的经验。

## 第二个主题：架构面对hw2新增需求存在的问题

该主题主要聚焦于hw1架构面对hw2新增需求存在的问题。

首先由卞卓航同学基于自己完成hw2作业的经验分享自己的看法：

- 解析`Term`类时存在一定的问题，结合定义`Expr = Expr + Expr + …… + Expr`，将所有的`Term`转换为`Expr`，问题就转换为了多个`Expr`相加；
- hw1中的字符串预处理，在解析`f(`时存在错误替换的较大问题，需要根据需求实时改变预处理策略。

孟烜宇同学对卞卓航同学的观点表示十分认同。

朱雄伟同学分享自己的问题：

- 对函数表达式解析时，可以使用`Parser`对函数表达式进行预处理，但是因为没有实现多自变量计算，所以只能作罢。

贾博驿同学提出问题：`Term`类中如何存储`Factor`对象？如何判断是否是可合并的同类项？

- 孟烜宇同学、朱雄伟同学和杨馨悦同学均指出使用`ArrayList`储存`Factor`对象；
- 卞卓航同学提出使用`TreeMap`存储`Factor`对象的思路；
- 关于合并同类项的问题：
  - 朱雄伟同学提出自己递归判断同类项与合并同类项的方法；
  - 卞卓航同学分享了自己通过重写`equals`方法判断exp指数是否相等的思路；
  - 贾博驿同学认为卞卓航同学不同时重写`hashCode`方法是不妥当的，并分享了原因与思路。

## 第三个主题：面对新需求的调整策略

贾博驿同学提出问题：如何处理多重括号嵌套？

- 杨馨悦同学回答使用递归下降法可以很好地解决括号嵌套问题；

- 朱雄伟同学对杨馨悦同学的观点表示赞同，并指出只要读到括号就当表达式处理的策略；

- 卞卓航同学分享了自己的经验：在函数解析时尝试使用正则表达式，但效果并不好。

  进一步的，卞卓航同学分享了自己通过自定义的`Definer`类，将形参`xyz`转换为`abc`，再进行处理的思路。

贾博驿同学再次提出问题：如何对exp指数进行化简？

孟烜宇同学对这个问题进行了解答：设计化简类进行运算，与hw1中的多项式相加类似。