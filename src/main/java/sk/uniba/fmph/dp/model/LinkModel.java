package sk.uniba.fmph.dp.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LinkModel {

    String label;
    String href;
    String validation;
    String responseStatus;

}
