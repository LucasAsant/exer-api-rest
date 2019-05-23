package hello;

import org.springframework.web.bind.annotation.*;
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
