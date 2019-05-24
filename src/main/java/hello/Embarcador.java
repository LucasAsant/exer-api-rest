package hello;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.*;
import java.io.*;

@RestController
@RequestMapping(value = "embarcadores")
public class Embarcador {

    @PostMapping()
    public String create() {
        loger("create");
        return "{create}";
    }

    @GetMapping()
    public String read() {
        loger("read");
        return "{read}";
    }

    @PutMapping()
    public String update() {
        loger("update");
        return "{update}";
    }

    @DeleteMapping()
    public String delete() {
        loger("delete");
        return "{delete}";
    }

    private void loger(String request) {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {
            fh = new FileHandler("my-log-file.log", true);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info(request);
            fh.close();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
