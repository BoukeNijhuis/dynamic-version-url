package nl.boukenijhuis.dynamicversionurl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
public class GetVersionController {

    public static final int OLDEST_VERSION = 1;
    public static final int NEWEST_VERSION = 9;

    @GetVersionMapping(path = "/a", versions = {2, NEWEST_VERSION} )
    private ResponseEntity<String> a2() {
        return ResponseEntity.ok("a2");
    }

    @GetVersionMapping(path = "/a", versions = {OLDEST_VERSION, 1})
    private ResponseEntity<String> a1() {
        return ResponseEntity.ok("a1");
    }

    /////////////////////////////////////////////

    @GetVersionMapping(path = "/b", versions = {OLDEST_VERSION, 3})
    private ResponseEntity<String> b1() {
        return ResponseEntity.ok("b1");
    }

    @GetVersionMapping(path = "/b", versions = {4, 6})
    private ResponseEntity<String> b4() {
        return ResponseEntity.ok("b4");
    }

    @GetVersionMapping(path = "/b", versions = {7, NEWEST_VERSION})
    private ResponseEntity<String> b7() {
        return ResponseEntity.ok("b7");
    }

    /////////////////////////////////////////////

    @GetVersionMapping(path = "/c", versions = {OLDEST_VERSION, 9})
    private ResponseEntity<String> c1() {
        return ResponseEntity.ok("c1");
    }

    /////////////////////////////////////////////

    // mapping without version
    @GetMapping(path = "/d")
    private ResponseEntity<String> d() {
        return ResponseEntity.ok("d");
    }
}
