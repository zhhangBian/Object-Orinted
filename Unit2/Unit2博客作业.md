# 第二单元总结

第二单元学到了java多线程编程和一些函数式编程的方法。学期过半，反思自己再上路。

## 同步块和锁的设置

在我本次的作业中，绝大部分操作都是给方法加上`synchronized`加上类锁，直接用最大的临界区保证线程安全。其余地方为了实现一些互斥操作，如对等待队列占有使得电梯无法得到策略进行下一步操作，则会手动用`synchronized(lock){}`占用临界资源。

对于另一种线程同步的方法，手动的加锁设计，我只在实现双轿厢中采用了这种方法，主要的考量为：

1. 对于电梯这种操作，设计锁相关的操作比较多，如果手动进行管理较为繁琐，直接加上`synchronized`进行修饰，结束时进行`notify()`是一种较为方便的方法。
2. 显式的锁修饰如果在迭代过程中出现了多重锁的获取，如果没有一个统一的取锁顺序，那么很可能会出现死锁问题，这样是得不偿失的。

在实际的代码编写中，我并没有遇到很多死锁问题，通过`synchronized`加上类锁的设计，比较好的保护了线程安全性，出现的bug也都是比较显示的。

另外，这种方法使得我的程序中的共享对象较少（也足够大的代价），可以维持较为统一的操作。在实现双轿厢电梯时，我曾经数次尝试范围更小的临界区手动管理锁，但是因为锁的种类变多，同一对象不同操作获得的锁可能不同出现了难以修复的死锁问题，最后还是回到了<del>有些摆烂</del>的大锁设计方式。

## 整体架构设计

我采用了解耦合的调度方式，将乘客的调度、电梯的调度都与调度线程、电梯及电梯线程分离开，采用了独立的影子电梯类和策略类来分别调度乘客和电梯。

相应的分离达到了**高内聚，低耦合**的目标，在迭代开发中较为方便。

![uml](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202404191956151.png)

### 每个类的作用

我的架构参考了实验中的结构，采用了双层“生产者消费者模型”，即**输入线程-分发线程-电梯线程**的运行模式，用UML协作图来表示，就是：

![协作图](https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202404192015624.png)

### 电梯的调度

将电梯和电梯线程做了拆分、将电梯运行策略和电梯线程、电梯也做了拆分。这样的好处就是灵活，在迭代过程中我可以保持相关的接口不变，只需要添加内部实现即可。同时有新需求修改起来也很方便，不用对架构做出大的调整。

电梯的调度我采用了LOOK算法，同时将reset请求、换乘操作也作为电梯的一种操作，在迭代开发中较为方便地实现了相关的操作。

```c
if (requestQueue.IsResetNow()) {
    return Status.RESET;
}

if (elevator.HavePassengerOutNow()) {
    return Status.OPEN;
}

if ((requestQueue.HavePassengerSameDirectionNow(nowFloor, direction)
     && elevator.GetLeftCapacity() > 0)) {
    return Status.OPEN;
}

if (elevator.InExchangeFloorLater() &&
    (elevator.HavePassengerWantExchange() ||
     requestQueue.HavePassengerInLater(nowFloor, direction))) {
    return Status.EXCHANGE;
}

if (requestQueue.HavePassengerInLater(nowFloor, direction)) {
    return Status.MOVE;
}

if (elevator.HavePassengerInsideNow()) {
    return Status.MOVE;
}

if (requestQueue.IfIsEmpty()) {
    if (requestQueue.IfRequestEnd() && otherRequestQueue.IfIsEmpty()) {
        return Status.END;
    } else {
        return Status.WAIT;
    }
}

return Status.REVERSE;
```

这样的调度还是很快的，每个电梯可以独立运行，根据等待队列和当前电梯的性质得到接下来的运行策略。

### 乘客的调度

第五次作业乘客是事先分配好的，只需要将乘客加入对应的等待区域即可。

第六第七次作业的大部分经历都是在设计调度器，我最后采用了精确设计的、**能模拟换乘的影子电梯**。

分发线程在每次分配乘客时，会进行一次模拟：如果把乘客加入这个电梯，那么运行时间会是多少，对六个电梯均进行模拟一遍后，找出最优的情况，进行分发。

我将电梯视作了一种状态机：所在楼层、运行方向都会影响其状态。基于对状态机的认识，**如果状态不转移，那么电梯状态应该不改变**。也就是说，如果每次状态转移后都使用一个寄存器（对象）保存下转移后的状态，那么可以直接在该寄存器对象中获取电梯的最新状态。

于是可以使用这样的思路，**在电梯的每次状态转移后，主动保存下当前的状态**，也就是**主动留下自己的影子**，进行主动投影。主动留下影子后可以在分发需求时直接**访问对应的影子**，不用再尝试获取电梯的拷贝，线程更安全，实现更简洁。

我的实现是建立了一个单例类`ShadowArea`，用来管理影子电梯对象`ShadowElevator`。电梯线程中的电梯每次完成一次操作，都会像影子区域中留下自己的投影，方便下次分发乘客时进行模拟。

```java
// ElevatorThread.java
ShadowArea.getInstance().ElevatorLeaveShadow(
    this.elevator.GetId(),
    this.elevator.GetType(),
    this.elevator.GetNowFloor(),
    this.elevator.GetDirection(),
    this.elevator.GetPassengerNum(),
    this.elevator.ClonePassengerMap(),
    this.requestQueue.CloneRequestMap());

// ShadowArea.java
public synchronized void ElevatorLeaveShadow() {}

public synchronized void ShadowElevatorNormalReset() {}

public synchronized void ShadowElevatorDoubleReset() {}

public synchronized int GetShadowEstimateId() {}
```

在第七次作业中，由于设计到了双轿厢电梯，传统的影子电梯无法精准模拟换乘所需要的时间同步，只能近似估计。为了解决这个问题，我采用了**自定义时钟的状态机思路**。通过设置一个时钟，使用状态机模拟电梯的运行、睡眠，达到尽可能真实的运行效果，达到了能模拟换乘的效果。

对于电梯的如MOVE、REVERSE等操作，只需要设置是否在睡眠的标志位即可，但是对于换乘这种复杂操作（来到换乘区，上下客、离开换乘区），我设计了一个子状态机，达到了更好的效果。

<img src="https://pigkiller-011955-1319328397.cos.ap-beijing.myqcloud.com/img/202404192031918.png" alt="状态机" style="zoom:10%;" />

```java
// ShadowElevator
Status nextStatusA;
Status nextStatusB;
while (!this.IfEnd() && time <= 1000) {
    if (this.waitTimeA != 0) {
        this.waitTimeA--;
    } else {
        if (this.stageA == 0) {
            nextStatusA = this.GetAdvice("A");
        } else {
            nextStatusA = Status.EXCHANGE;
        }
        this.ShadowElevatorRun("A", nextStatusA);
    }

    if (this.inDoubleMode) {
        if (this.waitTimeB != 0) {
            this.waitTimeB--;
        } else {
            if (this.stageB == 0) {
                nextStatusB = this.GetAdvice("B");
            } else {
                nextStatusB = Status.EXCHANGE;
            }
            this.ShadowElevatorRun("B", nextStatusB);
        }
    }

    this.time++;
}

private void Exchange(String type) {
    int stage = type.equals("A") ? this.stageA : this.stageB;
    if (stage == 0) {
        this.Move(type);
        stage = 1;
    } else if (stage == 1) {
        this.ExchangePassenger(type);
        this.Reverse(type);
        this.InPassenger(type);
        if (type.equals("A")) {
            this.waitTimeA = 4;
        } else {
            this.waitTimeB = 4;
        }
        stage = 2;
    } else {
        this.Move(type);
        stage = 0;
    }

    if (type.equals("A")) {
        this.stageA = stage;
    } else {
        this.stageB = stage;
    }
}
```

通过这样的操作，实现了较好的性能，同时为了避免可能的bug导致在模拟时陷入轮询，我设置了影子电梯最多进行1000次计算，超出后直接返回，认为此时运行时间过长，没有继续模拟的必要了。

同时，我的影子电梯还统计了开关门次数，通过调节相关的权重，可以达到耗电量-运行时间组合最优的电梯。

### 变与不变

变化的是电梯的运行方式：加入了reset和换乘，都将其视作一种运行方式。其中双轿厢reset最需要注意，直接改变了电梯的运行方式，需要在整体上进行考虑。

不变的是架构的设计：方式都是双层生产者-消费者模型，采用了输入-分发-运行的基本模式。

面对需要第一时间响应的reset请求，我在电梯的等候区域中设置了优先发送队列，但是对外的接口依然没有改变，这是一种不变。

面对在reset时可能会有的乘客分发，我在电梯的等候区域中设置了缓冲队列，保持了分发操作的接口不变，但是需要电梯在reset结束后手动将缓冲队列中的乘客加回正常队列，这在电梯的运行逻辑上发生了变化，这是一种改变。

电梯的思想，其实是一种最基本的多线程交互模型：怎么统一化各线程之间的交互，保证操作的准确性，这是一直以来没有改变的。

## 双轿厢的设计

对于双轿厢，我在架构层面默认了电梯可能会变成了双轿厢，初始化时即设置了相应的等待队列，但没有启动相应的线程。等到有双轿厢reset请求时，再开启相应的线程。

这样的操作目的为：

1. 无必要不开启双轿厢，减少不必要的可能唤醒
2. 延后开启双轿厢，可以更方便地设置另一个轿厢的属性，毕竟是在电梯A的运行线程内设置电梯B，暴露了自身的一些属性方便设置

双轿厢不涉及换乘则是一样的，请求建议-运行，唯一特别的就是换乘操作。我在两个轿厢之间设置了一个换乘锁，最多只有一人可以持有进行相关的换乘操作。

同时，我将换乘和前后动作统一为了一种操作：**来到换乘区-上下客-离开换乘区**，这样强制电梯离开换乘区可以避免电梯留在换乘区不离开。

```java
// 换乘操作
synchronized (this.exchangeLock) {
    this.elevator.Move();
    this.elevator.Reverse();
    this.elevator.OpenDoor();
	this.elevator.OutAndIn();
    this.elevator.CloseDoor();
    this.elevator.Move();
    this.exchangeLock.notify();
}
```

说到双轿厢，电梯是优雅的吗？从第一次引入量子电梯开始，电梯就变成了变成训练而不是实际的训练代码。在开启双轿厢后，第二单元就变成了彻底的学校练习，但是也确实学习到了不少多线程编程的方法和函数式编程的一点内容，对OO的理解也有了一些新的认识。

## bug分析

本单元出现了惨痛的bug。

在第二次作业中，出现了如果电梯正在reset，但是此时给乘客分配了一个乘客，会使得整个分配线程被阻塞1.2s，后续的reset请求无法得到及时响应。在bug修复环节添加了一个缓冲队列在修复此bug。

在第三次作业中，双轿厢电梯共享一个等待队列，向A的队列中塞入乘客也会唤醒B，这样不太合理的线程唤醒策略导致了CTLE。尽管我觉得这样的架构实现是美观的，但是客观上这样不合理的唤醒是一个大问题。讽刺的是，尽管中测的几个数据点已经出现了CTLE的问题，但是我却是治标不治本地延缓了双轿厢B线程启动的时间，并没有从本质上解决这个问题。最后在bug修复环节对架构进行了一次较大的调整，将共享的等候队列拆开，解决了该问题。

本单元无疑是失败的，强侧和互测都出现了bug，在课下完成时坦诚地讲没有特别用心。也许熬了几天夜感动了一下自己，但是更多地还是类似去祈祷自己的代码不出bug。每次互测看到自己进A房就开始认为自己代码不会出问题，也没有阅读代码借鉴一下架构，扔几个高压力数据点混满base分就结束。

找bug方法是单一的，在通过测试点、改完公开评测机能测出来的bug、自己捏完自以为的边界数据后，往往就不想改了。但是这样实际上是有很大的问题，比如一些极限情况是很难测出来的，而且对于像CTLE这样的bug，本地很难会对CPU使用时间进行测试，不合理的线程唤醒、代码轮询等操作在本地的表现是正常的，但是在课程评测平台却会出现大问题。

debug方法是单一的，通过在代码各处塞入打印语句来输出相关信息，通过这些信息来判断哪里可能出现bug。盯着无数的输出语句找bug，尽管有效，轮询什么都能找出来，但还是bug在前-debug在后的模式，不能有效地先验地找出自己的bug。

实际上多线程debug尽管不方便但还是有着多种方法的，比如IDEA的调试功能，判断死锁等等。后续的工作应该离不开多线程，以此单元开始，要更多地注意多线程中的bug啊！

## 心得体会

又是一个月啊！最近到了忙的时候，有OO的每一周都很紧凑，每周刚开始的时候总会有一些其他事情导致来不及第一时间写OO，到了周三开始紧迫起来，周四周五熬个夜把OO写完，周六再全面测试一遍、做些优化。评测机是来不及写的，能白嫖同学的最好，嫖不到就自己捏几组数据，造成了测试强度的大大降低。每周都匆匆忙忙，但是却也说不出自己忙了什么。

当然这不是课程的问题，是我自己没有合理地安排任务量，导致了每周OO做的很赶，为了实现所谓的“高性能量子影子电梯”，没有花大量的时间在检查电梯本身基础功能的正确性上，反而去做一些边际效益不高的优化。

优化真没什么太大的必要，我应该做完了能做的绝大部分优化：影子电梯、自己提出的演延时开关门、（应该是）模拟最准确的影子电梯、考虑耗电量的评价函数等等，但是这些都不如强测不出bug来的好。

哎！学期已经过半了，OO也已经过半了，坦白讲我第二单元的OO挺失败的，出现了许多意想不到的bug，究其里还是态度上不够重视，尽管第七次作业已经猜到了可能会CTLE，但是当时“麻了”的感受导致没有继续试一下继续优化代码，而是去忙别的事情去了，想当然地认为代码的正确性，没有经过太多的测试，当然因为多线程的不确定性有时候也懒得去测试。

还是要多思考，用思考的勤劳来代替自己代码的笨拙。电梯代码写到后面是有些臃肿的，这就是没有太多思考，为了尽快完成任务的莽干，最后也付出了相应的代价。

悟已往之不谏，知来者之可追。OO还有半个学期，其他的事物有些陆陆续续结束，又有些陆陆续续开始，还是要在OO上花更多的时间啊，不能像之前一样有些轻敌了！
