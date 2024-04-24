package logs_analysis_tool.example.analysis_tool.models;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class DateObjectCombination {

    private LocalDate localDate;

    private Long ordersPerDate;

    public DateObjectCombination(LocalDate localDate, long l) {
        this.localDate = localDate;
        this.ordersPerDate = l;
    }

}
