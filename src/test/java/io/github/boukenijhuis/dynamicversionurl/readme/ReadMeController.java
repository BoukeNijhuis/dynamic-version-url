package io.github.boukenijhuis.dynamicversionurl.readme;

import io.github.boukenijhuis.dynamicversionurl.GetVersionMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * This class is used to test the code samples in the README.md.
 */
@RestController()
public class ReadMeController {

//    @GetMapping("/v1/a")
//    public String a1() {
//        return "a1";
//    }
//
//    @GetMapping("/v2/a")
//    public String a2() {
//        return "a2";
//    }
//
//    @GetMapping("/v{version}/a")
//    public String a(@PathVariable int version) {
//        return switch (version) {
//            case 1 -> a1();
//            default -> a2();
//        };
//    }

//    @GetVersionMapping(value = "/a", version = 1)
//    public String a1() {
//        return "a1";
//    }
//
//    @GetVersionMapping(value = "/a", version = 2)
//    public String a2() {
//        return "a2";
//    }

//    @GetVersionMapping(value = "/a", versions = {1, 10})
//    public String a1() {
//        return "a1";
//    }
//
//    @GetVersionMapping(value = "/a", versions = {11, 20})
//    public String a11() {
//        return "a11";
//    }

    private static final int LATEST_VERSION = 10;

    @GetVersionMapping(value = "/a", versions = {1, LATEST_VERSION})
    public String a() {
        return "a";
    }

}
