package shine.aba.model;

import lombok.Data;

import java.util.List;

@Data
public class UniqueContactsRequest {

    private List<Long> bookIds;
}
