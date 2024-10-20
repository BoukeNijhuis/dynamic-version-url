package io.github.boukenijhuis.dynamicversionurl.versionmapping;

import io.github.boukenijhuis.dynamicversionurl.annotation.VersionMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VersionMappingController {

    @GetMapping(value = "/version_mapping")
    @VersionMapping(2)
    public String a2() {
        return "a2";
    }
}
