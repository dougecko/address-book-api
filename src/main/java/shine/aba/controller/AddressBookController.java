package shine.aba.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import shine.aba.service.AddressBookService;

@RestController
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @GetMapping("/")
    public String index() {
        return String.format("Service returned %s", addressBookService.doSomething());
    }
}
