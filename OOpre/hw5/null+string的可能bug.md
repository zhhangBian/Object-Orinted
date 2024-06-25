在本次的作业中，不少同学都在最后一个测试点上产生了问题。分享一下我的bug和相关的知识点。

本文经过瞪眼法、打印法等，仅供参考，有些同学最后一个测试点可能不是本篇文章的bug。欢迎批评指正。

## null和空串

在题目要求中有这样一个地方要求出现空串：7 将编号为`{id}`的队的所有士兵的咒语更新为各自进行`{int1}`,`{int2}`截取后的咒语字符串。如果有不符合要求，则截取为**空串**。

在我的最初处理中，直接将所有的空串当成了`null`，并且在之后所有涉及`incantation`的操作都加上了判断是否为null的操作。看起来没有问题，程序跑的十分爽快，可惜没通过最后一个测试点。

```java
if (a > b || a >= this.incantation.length()) {
    //this.incantation = "";
    this.incantation = null;
}
```

在排除了代码其他部分都不大可能出现bug后，我注意到了这里的`null`，有一个操作是两个咒语的拼接，**null+string是什么？**

## null的字符串拼接

对于如下的代码`System.out.println(null+"1234");`，**输出为：`null1234`**

这是一个相当让人意外的结果，`null`不应该是空指针吗，为何会被转换为了字符串，`java`中的拼接操作究竟是如何实现的。

### `+`的作用

Java将表达式`"A String" + x`转换为类似于`"A String" + String.valueOf(x)`的内容，在实际的使用中，将null隐式地转换为了`“null”`，然后和字符串拼接。这是一个相当奇怪的特性，在别的情况下`null`都无法强制转换为`string`类型，却在这种+的情况下完成了强制转换。

也就是说，在使用`null+"1234"`的时候，我们预想的是空指针+字符串=字符串，但是实际结果却是null被隐式强制类型转换为`“null”+"1234"`，因此产生了bug

![image-20231016001328558](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202310222237378.png)

不仅如此，在查找过程中发现，如果使用`StringBuilder`中的`append`，`append(null)`无法通过编译，需要使用`Optional.ofNullable(null)`将`null`包起来，此时的输出也变成了`1234Optional.empty`，尽管不是特别清楚Optional类型究竟是如何实现的，但可以肯定的是这不是一个`string`类型，如果对其单独打印，输出为`Optional.empty`

```java
StringBuilder stringBuilder = new StringBuilder();
stringBuilder.append("1234");
System.out.println(stringBuilder);    //1234
stringBuilder.append(Optional.ofNullable(null));    
System.out.println(stringBuilder);    //1234Optional.empty
System.out.println(Optional.ofNullable(null));    //Optional.empty
```

### 截取

`substring`是自带的库函数，如果遇到`substring(a,b)`的情况会截取为`“”`的空串。

## bug的具体出现

了解了这些后，就可以清楚bug是如何出现的了：

1. 在拼接操作中，遇到了`null+string`的情况，java将`null`隐式地转换为字符串`“null”`，然后再和`string`拼接，形成了`“nullstring”`这样意想不到的结果
2. 因为截取字串可能会出现空串`“”`，而null和空串同时出现的情况，对于equal的比较比较不利，需要连续的判断来处理

```java
public boolean equal(Soldier soldier) {
    if (this.incantation == null && soldier.incantation == null) {
        return this.name.equals(soldier.name);
    } else if (this.incantation == null && soldier.incantation.equals("")) {
        return this.name.equals(soldier.name);
    } else if (this.incantation == null) {
        return false;
    } else if (this.incantation.equals("") && soldier.incantation == null) {
        return this.name.equals(soldier.name);
    } else if (soldier.incantation == null) {
        return false;
    } else {
        return this.name.equals(soldier.name)
            && this.incantation.equals(soldier.incantation);
    }
}
```

在我把截取的空串改为`""`后，解决了这个bug，顺利通过了最后一个测试点。把空串处理为`""`，可以更好地统一字符串的操作，避免这种隐形bug和RE的存在。

## 启示

1. 除非特殊情况，尽量不要引入`null`，使用空字符串`“”`来作为空串。一方面可以避免RE，另一方面在字符串的拼接、查找子串等都不需要太多的特殊处理
2. 对于拼接操作，`null`会被强制转换为string类型
3. 上课中强调了空指针可能的访问异常，涉及null的场景要多加注意可能的RE