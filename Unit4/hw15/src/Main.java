import com.oocourse.library3.LibraryBookId;
import com.oocourse.library3.LibraryCloseCmd;
import com.oocourse.library3.LibraryCommand;
import com.oocourse.library3.LibraryOpenCmd;
import com.oocourse.library3.LibraryQcsCmd;
import com.oocourse.library3.LibraryReqCmd;

//import java.io.FileInputStream;
import java.io.FileNotFoundException;
//import java.io.PrintStream;
import java.time.LocalDate;
import java.util.Map;

import static com.oocourse.library3.LibrarySystem.SCANNER;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        //FileInputStream in = new FileInputStream("in.txt");
        //System.setIn(in);
        //PrintStream out = new PrintStream("out.txt");
        //System.setOut(out);

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
            } else if (command instanceof LibraryQcsCmd) {
                library.QueryCredit((LibraryQcsCmd) command);
            } else {
                LibraryReqCmd request = (LibraryReqCmd) command;
                library.HandleRequest(request, date);
            }
        }
    }
}
