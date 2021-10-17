package shine.aba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shine.aba.service.AddressBookService;

@RestController
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    @GetMapping("/")
    public String index() {
        return String.format("Service returned %s", addressBookService.doSomething());
    }
}
