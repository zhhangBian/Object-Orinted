import java.util.ArrayList;

public class MySet implements IntSet {
    //@ public instance model int[] ia;
    //@ public invariant (\ int i, j; 0 <= i && i < j && j < ia.length; ia[i] < ia[j]);
    private ArrayList<Integer> array;
    private int count;

    public MySet() {
        this.array = new ArrayList<>();
        this.count = 0;
    }

    //@ ensures \result == (\exists int i; 0 <= i && i < ia.length; ia[i] == x);
    @Override
    public Boolean contains(int x) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == x) {
                return true;
            }
        }
        return false;
    }

    /*@ public normal_behavior
      @ assignable \nothing
      @ requires 0 <= x && x < ia.length;
      @ ensures \result == ia[x];
      @ also
      @ public exceptional_behavior
      @ signals (IndexOutOfBoundsException e) (x < 0 || x >= ia.length);
      @*/
    @Override
    public int getNum(int x) throws IndexOutOfBoundsException {
        if (x >= 0 && x < count) {
            return array.get(x);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    /*@ public normal_behavior
      @ assignable ia;
      @ requires !contains(x);
      @ ensures contains(x);
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length); contains(\old(ia[i])));
      @ ensures (\forall int i; 0 <= i && i < ia.length && ia[i] != x; \old(contains(ia[i])));
      @*/
    @Override
    public void insert(int x) {
        int left = 1;
        int right = count - 1;
        int pos = -1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (array.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos == -1) {
            count++;
            array.add(x);
        } else {
            array.add(pos, x);
            count++;
        }
    }

    /*@ public normal_behavior
      @ assignable ia;
      @ requires contains(x);
      @ ensures !contains(x);
      @ ensures (\forall int i; 0 <= i && i < ia.length; \old(contains(ia[i])));
      @ ensures (\forall int i; 0 <= i && i < \old(ia.length) && \old(ia[i]) != x; contains(\old(ia[i])));
      @*/
    @Override
    public void delete(int x) {
        int left = 0;
        int right = count - 1;
        int pos = -1;
        while (left < right) {
            int mid = (left + right) / 2;
            if (array.get(mid) >= x) {
                pos = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        if (pos != -1 && array.get(pos) == x) {
            array.remove(pos);
            count--;
        }
    }

    //@ ensures \result == ia.length;
    @Override
    public int size() {
        return count;
    }

    /* 该方法完成两个IntSet对象所包含元素的交换, 例如：
       IntSet对象a中的元素为{1，2，3}，IntSet对象b中的元素为{4，5，6}
       经过交换操作后，a中的元素应为{4, 5, 6}, b中的元素为{1, 2, 3}
       两个IntSet对象中元素的数量可以不相同，例如：
       IntSet对象a中的元素为{1，2，3，4}，IntSet对象b中的元素为{5，6}
       经过交换操作后，a中的元素应为{5, 6}, b中的元素为{1, 2, 3，4}
       该方法无返回值
      */
    @Override
    public void elementSwap(IntSet a) {
        //TODO
    }

    /* 该方法返回两个IntSet对象的对称差运算结果
       数学上，两个集合的对称差是只属于其中一个集合，而不属于另一个集合的元素组成的集合
       集合论中的这个运算相当于布尔逻辑中的异或运算，如 A,B两个集合的对称差记为A⊕B，
       则 A⊕B = (A-B)∪(B-A) = (A∪B)-(A∩B)
       例如：集合{1,2,3}和{3,4}的对称差为{1,2,4}
       如果对空的IntSet对象进行对称差的运算，将抛出NullPointerException的异常
      */
    @Override
    public IntSet symmetricDifference(IntSet a) throws NullPointerException {
        //TODO
        return null;
    }

    //@ ensures \result ==
    //                  (\forall int i, j ;
    //                      0 <= i && i < j && j < ia.length;
    //                      ia[i] != ia[j] && ia[i] < ia[j]);
    @Override
    public boolean repOK() {
        //TODO
        return false;
    }
}
