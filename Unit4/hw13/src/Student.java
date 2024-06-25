import com.oocourse.library1.LibraryBookId;

import java.util.HashSet;

public class Student {
    private final String id;
    private final HashSet<LibraryBookId> booksBType;
    private final HashSet<LibraryBookId> booksCType;

    public Student(String id) {
        this.id = id;
        this.booksBType = new HashSet<>();
        this.booksCType = new HashSet<>();
    }

    public boolean HaveBType() {
        return !this.booksBType.isEmpty();
    }

    public boolean HaveCBook(LibraryBookId bookId) {
        return this.booksCType.contains(bookId);
    }

    public void Borrow(LibraryBookId bookId) {
        if (bookId.getType().equals(LibraryBookId.Type.B)) {
            this.booksBType.add(bookId);
        } else {
            this.booksCType.add(bookId);
        }
    }

    public void Return(LibraryBookId bookId) {
        if (bookId.getType().equals(LibraryBookId.Type.B)) {
            this.booksBType.remove(bookId);
        } else {
            this.booksCType.remove(bookId);
        }
    }
}
