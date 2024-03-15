```java
Runtime r = Runtime.getRuntime();
r.gc();//计算内存前先垃圾回收一次

long start = System.currentTimeMillis();//开始Time
long startMem = r.totalMemory(); // 开始Memory

long endMem = r.freeMemory(); // 末尾Memory
long end = System.currentTimeMillis();//末尾Time
System.out.println("用时消耗: " + (end - start) / 1000 + "s");
System.out.println("内存消耗: " + (startMem - endMem) / 1024 / 1024 + "MB");
```