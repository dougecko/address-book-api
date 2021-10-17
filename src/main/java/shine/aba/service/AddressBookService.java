package shine.aba.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import shine.aba.repository.AddressBookRepository;

@Component
public class AddressBookService {

    @Autowired
    private AddressBookRepository addressBookRepository;

    public String doSomething() {
        return addressBookRepository.goGetSomething();
    }
}
