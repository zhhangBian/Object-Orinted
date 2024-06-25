阅读魏佬[大作](http://oo.buaa.edu.cn/assignment/512/discussion/1545)后深受启发，但是感觉其中关于影子电梯拷贝的部分，主动获取6个锁实在是不太优雅。

那么有没有一种不用加锁的思路呢，很巧的是我也是用影子电梯和量子电梯实现的，我实在接受不了对电梯上锁这个过程，于是自己探索了一种自认为也许还算稍微有一点可能更优雅的实现：**从获取深拷贝变为电梯状态更新后的主动投影**。

## 什么是主动投影

深拷贝的目的是什么：获取电梯的当前状态。

电梯的状态什么时候会改变：状态机的状态改变后。

基于对状态机的认识，**如果状态不转移，那么电梯状态应该不改变**。也就是说，如果每次状态转移后都使用一个寄存器（对象）保存下转移后的状态，那么可以直接在该寄存器对象中获取电梯的最新状态。

于是可以使用这样的思路，**在电梯的每次状态转移后，主动保存下当前的状态**，也就是**主动留下自己的影子**，进行主动投影。主动留下影子后可以在分发需求时直接**访问对应的影子**，不用再尝试获取电梯的拷贝，线程更安全，实现更简洁。

通过设置一个共享资源区来保存影子电梯，平时电梯线程主动投影下电梯的影子，需要分发乘客时分发线程访问影子电梯，个人觉得比获取锁更优雅一些。

我传递影子的过程是在电梯线程中，传递**线程安全**的数据（自定义所需的数据，非基本类型数据如等待队列则新建`ArrayList`实现）来实现的。

```java
// ElevatorThread.java
public void run() {
    while (true) {
        switch (nextStatus) {
        	// 电梯动作，完成后状态更新
        }
        // 状态转移后留下影子
        ShadowArea.getInstance().ElevatorLeaveShadow(/* information */);
    }
}
```

我的实现是建立了一个单例类`ShadowArea`，用来管理影子电梯对象`ShadowElevator`。

对于为什么要使用单例：

1. 语义上是单例的
2. 使用单例在写代码的时候可以通过`类名.getInstance()`的方法来获取实例，可以在代码中任何地方直接调用，不用将`shadowArea`作为参数传来传去

```java
public class ShadowArea {
    private static ShadowArea shadowArea = new ShadowArea();
    private final HashMap<Integer, ShadowElevator> shadowElevatorMap;
    
    public synchronized void ElevatorLeaveShadow() {
        this.shadowElevatorMap.get(id).LeaveShadow(/* information */);
        this.notify();
    }

    public synchronized void ShadowElevatorSetReset(int id) {
        this.shadowElevatorMap.get(id).SetReset();
        this.notify();
    }

    public synchronized int GetShadowEstimateId(PersonRequest personRequest) {
        // 遍历6个影子电梯一遍找最好性能，具体实现应该大差不差，可以参考魏佬博客
        int estimatePerformance = this.shadowElevatorMap.get(i).clone().
                GetEstimatePerformance(personRequest);
    }
}

```

对于影子电梯类，实际上**不用完全拷贝所有信息，只需要拷贝有用的信息**，可以在原先电梯类的基础上做减法。

比如等待队列，原先应该还保存有是否结束等信息，这些信息对于影子电梯来说是无用的，完全不需要，可以通过在主动投影的时候传递的信息进行调节。

```java
public class ShadowElevator {
    private int velocity;
    private int capacity;
    private int nowFloor;
    private int direction;
    private int passengerNum;
    private HashMap<Integer, ArrayList<PersonRequest>> passengerMap;
    private HashMap<Integer, ArrayList<Request>> requestMap;
    // 影子电梯的性能指标，每次状态更新后清零
    private int time;
    private int count;
    
    //其余函数模拟电梯的运行，把sleep/wait换为time+
    //如果reset，那么time初始值为1200
}
```

这样子操作以后，逻辑就变为了：

1. 电梯的行为视作一种状态转移
2. 状态转移后主动把留下影子，方便分发需求时访问
3. 影子电梯由一个总类进行管理，只有这个总类会被多个线程访问

### reset

对于正在reset的电梯，我在电梯reset开始时主动对投影进行操作，将影子电梯模拟时间的初始值设置为1200，这样可以充分模拟乘客被赶下去后的等待：如果你还选择这个电梯，你要等待reset完成。

```java
// ElevatorThread.java
private void Reset() {
    ShadowArea.getInstance().ShadowElevatorSetReset(this.elevator.GetId(),...);
    // ...
}
```

## 弊端

**性能消耗增加了！！！**

原先只有在分配的时候才会去获取这样的一个影子，但现在变成了电梯状态改变就会去主动投影，明显这样操作会频繁地增加投影环节的时间消耗。

但是，我这样做的考量是如下的：

1. 评测给的性能时间只要不TLE还是挺充足的
2. 对于电梯而言，电梯状态更新的最短时间间隔也至少需要0.2s（reset后的最快移动速度），而经过我本地测试，这样的一次主动投影过程并不慢，比0.2s几乎低一个数量级，我选择相信现代CPU和编译器
3. 情感上实在接受不了对电梯上锁
   1. 对乘客队列上锁是天经地义的：其本身就是共享对象，加锁是一种多线程读写一致的保护
   2. **在我看来**，电梯是电梯线程的私有对象，“锁”这个过程在程序上纯粹是为了影子电梯妥协，电梯加锁是为了进行线程间通信获取信息，我个人选择留下信息，在共享区域等待读
   3. 我的实现是电梯和电梯线程分离，加锁操作还好实现一点，但是我了解到不少同学电梯的全部信息就是直接存在电梯线程中的，给线程加锁实在是太…