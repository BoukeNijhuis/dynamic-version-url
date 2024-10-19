package io.github.boukenijhuis.dynamicversionurl.pathvariable;

import io.github.boukenijhuis.dynamicversionurl.annotation.GetVersionMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathVariableController {

    @GetVersionMapping(value = "/pathVariable/{pv}", version = 2)
    public String pv(@PathVariable("pv") String pv) {
        return pv;
    }
}
