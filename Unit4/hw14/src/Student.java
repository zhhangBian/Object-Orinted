import com.oocourse.library2.LibraryBookId;

import java.time.LocalDate;
import java.util.HashMap;

public class Student {
    private final String id;
    private final HashMap<LibraryBookId, LocalDate> bookMapB;
    private final HashMap<LibraryBookId, LocalDate> bookMapC;
    private final HashMap<LibraryBookId, LocalDate> bookMapBU;
    private final HashMap<LibraryBookId, LocalDate> bookMapCU;

    public Student(String id) {
        this.id = id;
        this.bookMapB = new HashMap<>();
        this.bookMapC = new HashMap<>();
        this.bookMapBU = new HashMap<>();
        this.bookMapCU = new HashMap<>();
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

    public void Borrow(LibraryBookId bookId, LocalDate date) {
        switch (bookId.getType()) {
            case B:
                this.bookMapB.put(bookId, date.plusDays(30));
                break;
            case C:
                this.bookMapC.put(bookId, date.plusDays(60));
                break;
            case BU:
                this.bookMapBU.put(bookId, date.plusDays(7));
                break;
            case CU:
                this.bookMapCU.put(bookId, date.plusDays(14));
                break;
            default:
                break;
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
