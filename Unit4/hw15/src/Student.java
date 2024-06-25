import com.oocourse.library3.LibraryBookId;

import java.time.LocalDate;
import java.util.HashMap;

public class Student {
    private final String id;
    private int credit;
    private final HashMap<LibraryBookId, LocalDate> bookMapB;
    private final HashMap<LibraryBookId, LocalDate> bookMapC;
    private final HashMap<LibraryBookId, LocalDate> bookMapBU;
    private final HashMap<LibraryBookId, LocalDate> bookMapCU;

    public Student(String id) {
        this.id = id;
        this.credit = 10;
        this.bookMapB = new HashMap<>();
        this.bookMapC = new HashMap<>();
        this.bookMapBU = new HashMap<>();
        this.bookMapCU = new HashMap<>();
    }

    public int GetCredit() {
        return this.credit;
    }

    public boolean NotInGoodCredit() {
        return this.credit < 0;
    }

    public void AddCredit(int addNum) {
        this.credit = Math.min(this.credit + addNum, 20);
    }

    public boolean HaveTypeB() {
        return !this.bookMapB.isEmpty();
    }

    public boolean HaveBookC(LibraryBookId bookId) {
        return this.bookMapC.containsKey(bookId);
    }

    public boolean HaveTypeBU() {
        return !this.bookMapBU.isEmpty();
    }

    public boolean HaveBookCU(LibraryBookId bookId) {
        return this.bookMapCU.containsKey(bookId);
    }

    public LocalDate Borrow(LibraryBookId bookId, LocalDate date) {
        LocalDate dueDate;
        switch (bookId.getType()) {
            case B:
                dueDate = date.plusDays(30);
                this.bookMapB.put(bookId, dueDate);
                return dueDate;
            case C:
                dueDate = date.plusDays(60);
                this.bookMapC.put(bookId, dueDate);
                return dueDate;
            case BU:
                dueDate = date.plusDays(7);
                this.bookMapBU.put(bookId, dueDate);
                return dueDate;
            case CU:
                dueDate = date.plusDays(14);
                this.bookMapCU.put(bookId, dueDate);
                return dueDate;
            default:
                return date;
        }
    }

    public boolean Return(LibraryBookId bookId, LocalDate date) {
        LocalDate endDate;
        switch (bookId.getType()) {
            case B:
                endDate = this.bookMapB.get(bookId);
                this.bookMapB.remove(bookId);
                break;
            case C:
                endDate = this.bookMapC.get(bookId);
                this.bookMapC.remove(bookId);
                break;
            case BU:
                endDate = this.bookMapBU.get(bookId);
                this.bookMapBU.remove(bookId);
                break;
            case CU:
                endDate = this.bookMapCU.get(bookId);
                this.bookMapCU.remove(bookId);
                break;
            default:
                endDate = date;
                break;
        }

        return !date.isAfter(endDate);
    }

    public boolean Renew(LibraryBookId bookId, LocalDate date) {
        LocalDate endDate = this.GetDate(bookId);
        LocalDate beginDate = endDate.minusDays(4);

        if (date.isBefore(beginDate) || date.isAfter(endDate)) {
            return false;
        }

        if (bookId.isTypeB()) {
            this.bookMapB.put(bookId, endDate.plusDays(30));
        } else if (bookId.isTypeC()) {
            this.bookMapC.put(bookId, endDate.plusDays(30));
        } else {
            return false;
        }

        return true;
    }

    private LocalDate GetDate(LibraryBookId bookId) {
        switch (bookId.getType()) {
            case B:
                return this.bookMapB.get(bookId);
            case C:
                return this.bookMapC.get(bookId);
            case BU:
                return this.bookMapBU.get(bookId);
            case CU:
                return this.bookMapCU.get(bookId);
            default:
                return null;
        }
    }
}
