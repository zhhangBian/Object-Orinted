import com.oocourse.library1.LibraryBookId;

import java.time.LocalDate;

public class AppointmentInfo {
    private final LibraryBookId bookId;
    private final String studentId;
    private LocalDate date;

    public AppointmentInfo(LibraryBookId bookId, String studentId) {
        this.bookId = bookId;
        this.studentId = studentId;
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
}
