import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryMoveInfo;
import com.oocourse.library2.LibraryReqCmd;
import com.oocourse.library2.annotation.Trigger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static com.oocourse.library2.LibrarySystem.PRINTER;

// 书籍的状态：正常normal，外借borrow

public class Library {
    private final HashMap<LibraryBookId, Integer> bookShelf;
    private final ArrayList<LibraryBookId> borrowAndReturnOffice;
    private final ArrayList<AppointmentInfo> appointmentOffice;
    private final ArrayList<AppointmentInfo> appointmentBuffer;
    private final HashSet<LibraryBookId> driftCorner;
    private final HashMap<LibraryBookId, Integer> driftCountMap;
    private final HashMap<String, Student> students;

    public Library(Map<LibraryBookId, Integer> map) {
        this.bookShelf = new HashMap<>(map);
        this.borrowAndReturnOffice = new ArrayList<>();
        this.appointmentOffice = new ArrayList<>();
        this.appointmentBuffer = new ArrayList<>();
        this.driftCorner = new HashSet<>();
        this.driftCountMap = new HashMap<>();
        this.students = new HashMap<>();
    }

    public void HandleRequest(LibraryReqCmd request, LocalDate date) {
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
            //捐赠图书
            case DONATED:
                this.Donate(request, date);
                break;
            // 续借
            case RENEWED:
                this.Renew(request, date);
                break;
            default:
                break;
        }
    }

    private void Query(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        if (bookId.isFormal()) {
            PRINTER.info(date, bookId, this.bookShelf.get(bookId));
        } else {
            PRINTER.info(date, bookId, this.driftCorner.contains(bookId) ? 1 : 0);
        }
    }

    @Trigger(from = "normal", to = {"normal", "borrow"})
    private void Borrow(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        if ((bookId.isFormal() && this.bookShelf.get(bookId) <= 0) ||
            (!bookId.isFormal() && !this.driftCorner.contains(bookId))) {
            PRINTER.reject(request);
        } else if (bookId.isTypeA() || bookId.isTypeAU()) {
            PRINTER.reject(request);
        } else if (bookId.isTypeB()) {
            // 书架到借阅处
            this.bookShelf.put(bookId, this.bookShelf.get(bookId) - 1);

            if (student.HaveTypeB()) {
                this.borrowAndReturnOffice.add(bookId);
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                PRINTER.accept(request);
            }
        } else if (bookId.isTypeC()) {
            // 书架到借阅处
            this.bookShelf.put(bookId, this.bookShelf.get(bookId) - 1);

            if (student.HaveBookC(bookId)) {
                this.borrowAndReturnOffice.add(bookId);
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                PRINTER.accept(request);
            }
        } else if (bookId.isTypeBU()) {
            // 漂流处到借阅处
            this.driftCorner.remove(bookId);

            if (student.HaveTypeBU()) {
                this.borrowAndReturnOffice.add(bookId);
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                PRINTER.accept(request);
            }
        } else if (bookId.isTypeCU()) {
            // 漂流处到借阅处
            this.driftCorner.remove(bookId);

            if (student.HaveBookCU(bookId)) {
                this.borrowAndReturnOffice.add(bookId);
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                PRINTER.accept(request);
            }
        } else {
            PRINTER.reject(request);
        }
    }

    private void Order(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        if (bookId.isTypeB()) {
            if (student.HaveTypeB()) {
                PRINTER.reject(request);
            } else {
                PRINTER.accept(request);
                this.appointmentBuffer.add(new AppointmentInfo(bookId, request.getStudentId()));
            }
        } else if (bookId.isTypeC()) {
            if (student.HaveBookC(bookId)) {
                PRINTER.reject(request);
            } else {
                PRINTER.accept(request);
                this.appointmentBuffer.add(new AppointmentInfo(bookId, request.getStudentId()));
            }
        } else {
            PRINTER.reject(request);
        }
    }

    @Trigger(from = "borrow", to = "normal")
    private void Return(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        Student student = this.GetStudent(request.getStudentId());

        boolean notOverDue = student.Return(bookId, date);
        this.borrowAndReturnOffice.add(bookId);
        PRINTER.accept(request, notOverDue ? "not overdue" : "overdue");

        if (!bookId.isFormal()) {
            this.driftCountMap.put(bookId, this.driftCountMap.get(bookId) + 1);
        }
    }

    @Trigger(from = "normal", to = {"normal", "borrow"})
    private void Pick(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        int index = this.GetAppointmentBookIndex(request);
        // 如果不存在预约的书籍
        if (index == -1) {
            PRINTER.reject(request);
            return;
        }
        // 如果存在
        Student student = this.GetStudent(request.getStudentId());
        if (bookId.isTypeB()) {
            if (student.HaveTypeB()) {
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                this.appointmentOffice.remove(index);
                PRINTER.accept(request);
            }
        } else if (bookId.isTypeC()) {
            if (student.HaveBookC(bookId)) {
                PRINTER.reject(request);
            } else {
                student.Borrow(bookId, date);
                this.appointmentOffice.remove(index);
                PRINTER.accept(request);
            }
        } else {
            PRINTER.reject(request);
        }
    }

    private void Donate(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        this.driftCorner.add(bookId);
        this.driftCountMap.put(bookId, 0);
        PRINTER.accept(request);
    }

    private void Renew(LibraryReqCmd request, LocalDate date) {
        LibraryBookId bookId = request.getBookId();
        if (bookId.isTypeB() || bookId.isTypeC()) {
            if (this.HaveAppointmentBook(bookId) && this.bookShelf.get(bookId) == 0) {
                PRINTER.reject(request);
                return;
            }

            Student student = this.GetStudent(request.getStudentId());
            boolean success = student.Renew(bookId, date);
            if (success) {
                PRINTER.accept(request);
            } else {
                PRINTER.reject(request);
            }
        } else {
            PRINTER.reject(request);
        }
    }

    // 是否有为特定用户保留的书
    private int GetAppointmentBookIndex(LibraryReqCmd request) {
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

    // 检查预约
    private boolean HaveAppointmentBook(LibraryBookId bookId) {
        // 检查预约buffer
        for (AppointmentInfo appointmentInfo : this.appointmentBuffer) {
            if (appointmentInfo.GetBookId().equals(bookId)) {
                return true;
            }
        }
        // 检查预约处
        for (AppointmentInfo appointmentInfo : this.appointmentOffice) {
            if (appointmentInfo.GetBookId().equals(bookId)) {
                return true;
            }
        }
        return false;
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
    // 检查漂流书籍，并把借还处的书全部放回书架
    private void CheckBorrowAndReturnOffice(ArrayList<LibraryMoveInfo> moveInfos) {
        for (LibraryBookId bookId : this.borrowAndReturnOffice) {
            if (bookId.isFormal()) {
                moveInfos.add(new LibraryMoveInfo(bookId, "bro", "bs"));
                this.bookShelf.put(bookId, this.bookShelf.get(bookId) + 1);
            } else {
                // 检查漂流书籍是否变为正式书籍
                if (this.driftCountMap.get(bookId) >= 2) {
                    moveInfos.add(new LibraryMoveInfo(bookId, "bro", "bs"));

                    this.bookShelf.put(bookId.toFormal(), 1);
                    this.driftCorner.remove(bookId);
                    this.driftCountMap.remove(bookId);
                } else {
                    moveInfos.add(new LibraryMoveInfo(bookId, "bro", "bdc"));
                    this.driftCorner.add(bookId);
                }
            }
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
