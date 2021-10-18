package shine.aba.service;

import org.springframework.stereotype.Component;
import shine.aba.repository.AddressBookRepository;

@Component
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;

    public AddressBookService(final AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    public String doSomething() {
        return addressBookRepository.goGetSomething();
    }
}
