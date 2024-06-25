import com.oocourse.library3.LibraryBookId;

import java.time.LocalDate;

public class BookInfo {
    private final LibraryBookId bookId;
    private final String studentId;
    private LocalDate date;

    public BookInfo(LibraryBookId bookId, String studentId) {
        this.bookId = bookId;
        this.studentId = studentId;
    }

    public BookInfo(LibraryBookId bookId, String studentId, LocalDate date) {
        this.bookId = bookId;
        this.studentId = studentId;
        this.date = date;
    }

    public void SetDate(LocalDate date) {
        this.date = date;
    }

    public LibraryBookId GetBookId() {
        return this.bookId;
    }

    public String GetStudentId() {
        return this.studentId;
    }

    public LocalDate GetDate() {
        return this.date;
    }

    public void RenewDate() {
        this.date = this.date.plusDays(30);
    }
}
