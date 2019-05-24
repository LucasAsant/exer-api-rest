package hello;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.logging.*;

@RestController
@RequestMapping(value = "embarcadores")
public class Embarcador {

    @CrossOrigin
    @PostMapping()
    public String create() {
        return "{create}";
    }

    @CrossOrigin
    @GetMapping()
    public String read() {

	    Logger logger = Logger.getLogger("MyLog");
	    FileHandler fh;

        try {
			fh = new FileHandler("my-log-file.log");
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();
	        fh.setFormatter(formatter);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		logger.info("{read}");
        return "{read}";
    }

    @CrossOrigin
    @PutMapping()
    public String update() {
        return "{update}";
    }

    @CrossOrigin
    @DeleteMapping()
    public String delete() {
        return "{delete}";
    }

}
