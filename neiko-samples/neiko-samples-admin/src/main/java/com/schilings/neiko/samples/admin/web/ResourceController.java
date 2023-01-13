package com.schilings.neiko.samples.admin.web;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
public class ResourceController {

    @GetMapping("/1")
    @PreAuthorize(value = "hasAuthority('ROLE_USER')")
    public String test1() {
        return "1111";
    }

    @PreAuthorize(value = "hasAuthority('SCOPE_message.read')")
    @GetMapping("/2")
    public String test2() {
        return "2222";
    }

    @PreAuthorize(value = "hasAuthority('SCOPE_message.write')")
    @GetMapping("/3")
    public String test3() {
        return "3333";
    }

    @PreAuthorize(value = "hasAuthority('none')")
    @GetMapping("/4")
    public String test4() {
        return "4444";
    }


}
