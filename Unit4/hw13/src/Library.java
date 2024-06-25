import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryMoveInfo;
import com.oocourse.library1.LibraryRequest;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.oocourse.library1.LibrarySystem.PRINTER;

public class Library {
    private final HashMap<LibraryBookId, Integer> bookShelf;
    private final ArrayList<LibraryBookId> borrowAndReturnOffice;
    private final ArrayList<AppointmentInfo> appointmentOffice;
    private final ArrayList<AppointmentInfo> appointmentBuffer;
    private final HashMap<String, Student> students;

    public Library(Map<LibraryBookId, Integer> map) {
        this.bookShelf = new HashMap<>(map);
        this.borrowAndReturnOffice = new ArrayList<>();
        this.appointmentOffice = new ArrayList<>();
        this.appointmentBuffer = new ArrayList<>();
        this.students = new HashMap<>();
    }

    public void HandleRequest(LibraryRequest request, LocalDate date) {
        switch (request.getType()) {
            // 查询
            case QUERIED:
                this.Query(request, date);
                break;
            // 借阅
            case BORROWED:
                this.Borrow(request, date);
                break;
            // 预约
            case ORDERED:
                this.Order(request, date);
                break;
            // 还书
            case RETURNED:
                this.Return(request, date);
                break;
            // 取书
            case PICKED:
                this.Pick(request, date);
                break;
            default:
                break;
        }
    }

    private void Query(LibraryRequest request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        PRINTER.info(date, bookId, this.bookShelf.get(bookId));
    }

    private void Borrow(LibraryRequest request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        int bookNum = this.bookShelf.get(bookId);
        if (bookNum <= 0) {
            PRINTER.reject(date, request);
        } else if (bookId.getType().equals(LibraryBookId.Type.A)) {
            PRINTER.reject(date, request);
        } else {
            // 从书架到借还处
            this.bookShelf.put(bookId, bookNum - 1);

            if (bookId.getType().equals(LibraryBookId.Type.B)) {
                if (student.HaveBType()) {
                    this.borrowAndReturnOffice.add(bookId);
                    PRINTER.reject(date, request);
                } else {
                    student.Borrow(bookId);
                    PRINTER.accept(date, request);
                }
            } else {
                if (student.HaveCBook(bookId)) {
                    this.borrowAndReturnOffice.add(bookId);
                    PRINTER.reject(date, request);
                } else {
                    student.Borrow(bookId);
                    PRINTER.accept(date, request);
                }
            }
        }
    }

    private void Order(LibraryRequest request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        if (bookId.getType().equals(LibraryBookId.Type.A)) {
            PRINTER.reject(date, request);
        } else if (bookId.getType().equals(LibraryBookId.Type.B)) {
            if (student.HaveBType()) {
                PRINTER.reject(date, request);
            } else {
                PRINTER.accept(date, request);
                this.appointmentBuffer.add(new AppointmentInfo(bookId, request.getStudentId()));
            }
        } else {
            if (student.HaveCBook(bookId)) {
                PRINTER.reject(date, request);
            } else {
                PRINTER.accept(date, request);
                this.appointmentBuffer.add(new AppointmentInfo(bookId, request.getStudentId()));
            }
        }
    }

    private void Return(LibraryRequest request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        student.Return(bookId);
        this.borrowAndReturnOffice.add(bookId);
        PRINTER.accept(date, request);
    }

    private void Pick(LibraryRequest request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        int index = this.GetAppointmentBookIndex(request);
        // 如果不存在预约的书籍
        if (index == -1) {
            PRINTER.reject(date, request);
            return;
        }
        // 如果存在
        Student student = this.GetStudent(request.getStudentId());
        if (bookId.getType().equals(LibraryBookId.Type.B)) {
            if (student.HaveBType()) {
                PRINTER.reject(date, request);
            } else {
                student.Borrow(bookId);
                this.appointmentOffice.remove(index);
                PRINTER.accept(date, request);
            }
        } else {
            if (student.HaveCBook(bookId)) {
                PRINTER.reject(date, request);
            } else {
                student.Borrow(bookId);
                this.appointmentOffice.remove(index);
                PRINTER.accept(date, request);
            }
        }
    }

    // 是否有为特定用户保留的书
    private int GetAppointmentBookIndex(LibraryRequest request) {
        LibraryBookId bookId = request.getBookId();
        String studentId = request.getStudentId();
        for (int i = 0; i < this.appointmentOffice.size(); i++) {
            if (this.appointmentOffice.get(i).GetBookId().equals(bookId) &&
                this.appointmentOffice.get(i).GetStudentId().equals(studentId)) {
                return i;
            }
        }
        return -1;
    }

    public void AfterClose(LocalDate date) {
        PRINTER.move(date, new ArrayList<>());
    }

    public void TidyLibraryWhenOpen(LocalDate date) {
        ArrayList<LibraryMoveInfo> moveInfos = new ArrayList<>();
        // 检查借还处
        this.CheckBorrowAndReturnOffice(moveInfos);
        // 检查预约处是否有过期
        this.CheckAppointmentOffice(date, moveInfos);
        // 处理预约，书架上是否有书满足预约
        this.HandleAppointmentRequest(date, moveInfos);

        PRINTER.move(date, moveInfos);
    }

    // 检查借还处
    // 把借还处的书全部放回书架
    private void CheckBorrowAndReturnOffice(ArrayList<LibraryMoveInfo> moveInfos) {
        for (LibraryBookId bookId : this.borrowAndReturnOffice) {
            moveInfos.add(new LibraryMoveInfo(bookId, "bro", "bs"));
            this.bookShelf.put(bookId, this.bookShelf.get(bookId) + 1);
        }
        this.borrowAndReturnOffice.clear();
    }

    // 检查预约处
    // 如果有过期书籍，放回书架
    private void CheckAppointmentOffice(LocalDate date, ArrayList<LibraryMoveInfo> moveInfos) {
        Iterator<AppointmentInfo> iterator = this.appointmentOffice.iterator();
        while (iterator.hasNext()) {
            AppointmentInfo appointmentInfo = iterator.next();
            LibraryBookId bookId = appointmentInfo.GetBookId();
            LocalDate appointDate = appointmentInfo.GetDate();

            long daysBetween = ChronoUnit.DAYS.between(appointDate, date);
            if (daysBetween >= 5) {
                this.bookShelf.put(bookId, this.bookShelf.get(bookId) + 1);
                moveInfos.add(new LibraryMoveInfo(bookId, "ao", "bs"));
                iterator.remove();
            }
        }
    }

    // 处理预约
    // 如果书架上有对应书，则放到预约处
    private void HandleAppointmentRequest(LocalDate date, ArrayList<LibraryMoveInfo> moveInfos) {
        Iterator<AppointmentInfo> iterator = this.appointmentBuffer.iterator();
        while (iterator.hasNext()) {
            AppointmentInfo appointmentInfo = iterator.next();
            LibraryBookId bookId = appointmentInfo.GetBookId();
            String studentId = appointmentInfo.GetStudentId();

            int bookNum = this.bookShelf.get(bookId);
            if (bookNum > 0) {
                this.bookShelf.put(bookId, bookNum - 1);
                moveInfos.add(new LibraryMoveInfo(bookId, "bs", "ao", studentId));
                appointmentInfo.SetDate(date);
                this.appointmentOffice.add(appointmentInfo);
                iterator.remove();
            }
        }
    }

    private Student GetStudent(String studentID) {
        if (this.students.containsKey(studentID)) {
            return this.students.get(studentID);
        } else {
            Student student = new Student(studentID);
            this.students.put(studentID, student);
            return student;
        }
    }
}
