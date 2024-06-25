import com.oocourse.library2.LibraryBookId;
import com.oocourse.library2.LibraryCloseCmd;
import com.oocourse.library2.LibraryCommand;
import com.oocourse.library2.LibraryOpenCmd;
import com.oocourse.library2.LibraryReqCmd;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Map;

import static com.oocourse.library2.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // 读取图书的库存
        Map<LibraryBookId, Integer> map = SCANNER.getInventory();
        Library library = new Library(map);

        // 读取指令
        while (true) {
            LibraryCommand command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }

            LocalDate date = command.getDate();
            if (command instanceof LibraryOpenCmd) {
                library.TidyLibraryWhenOpen(date);
            } else if (command instanceof LibraryCloseCmd) {
                library.AfterClose(date);
            } else {
                LibraryReqCmd request = (LibraryReqCmd) command;
                library.HandleRequest(request, date);
            }
        }
    }
}
