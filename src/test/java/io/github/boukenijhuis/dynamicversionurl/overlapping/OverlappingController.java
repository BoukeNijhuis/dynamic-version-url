package io.github.boukenijhuis.dynamicversionurl.overlapping;

import io.github.boukenijhuis.dynamicversionurl.annotation.GetVersionMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
public class OverlappingController {

    @GetVersionMapping(value = "/overlap", versions = {1,3})
    public String a1() {
        return "a1";
    }

    @GetVersionMapping(value = "/overlap", version = {3,3})
    public String a2() {
        return "a2";
    }
}
