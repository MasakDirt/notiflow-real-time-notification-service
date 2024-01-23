package com.proj.logo;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;

@RestController
@RequestMapping("/api/v1/logo")
public class LogoController {

    @GetMapping(produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource getLogo() {
        return new ClassPathResource("static/logo.png");
    }
}
