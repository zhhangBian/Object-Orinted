import com.oocourse.library1.LibraryBookId;
import com.oocourse.library1.LibraryCommand;
import com.oocourse.library1.LibraryRequest;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Map;

import static com.oocourse.library1.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        // 读取图书的库存
        Map<LibraryBookId, Integer> map = SCANNER.getInventory();
        Library library = new Library(map);

        // 读取指令
        while (true) {
            LibraryCommand<?> command = SCANNER.nextCommand();
            if (command == null) {
                break;
            }

            LocalDate date = command.getDate();
            if (command.getCmd().equals("OPEN")) {
                library.TidyLibraryWhenOpen(date);
            } else if (command.getCmd().equals("CLOSE")) {
                library.AfterClose(date);
            } else {
                library.HandleRequest((LibraryRequest) command.getCmd(), date);
            }
        }
    }
}
