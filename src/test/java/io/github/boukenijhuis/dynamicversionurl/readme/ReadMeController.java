package io.github.boukenijhuis.dynamicversionurl.readme;

import io.github.boukenijhuis.dynamicversionurl.GetVersionMapping;
import org.springframework.web.bind.annotation.RestController;

/***
 * This class is used to test the code samples in the README.md.
 */
@RestController()
public class ReadMeController {

//    @GetMapping("/v1/random")
//    public String random1() {
//        return String.format("%s", Math.random());
//    }
//
//    @GetMapping("/v2/random")
//    public String random2() {
//        return String.format("Random number: %s", Math.random());
//    }

//    @GetMapping("/v{version}/random")
//    public String random2(@PathVariable int version) {
//        return switch (version) {
//            case 1 -> random1();
//            default -> random2();
//        };
//    }

//    @GetVersionMapping(value = "/random", version = 1)
//    public String random1() {
//        return String.format("%s", Math.random());
//    }
//
//    @GetVersionMapping(value = "/random", version = 2)
//    public String random2() {
//        return String.format("Random number: %s", Math.random());
//    }

    @GetVersionMapping(value = "/random", versions = {1, 10})
    public String random1() {
        return String.format("%s", Math.random());
    }

    @GetVersionMapping(value = "/random", versions = {11, 20})
    public String random11() {
        return String.format("Random number: %s", Math.random());
    }

}
