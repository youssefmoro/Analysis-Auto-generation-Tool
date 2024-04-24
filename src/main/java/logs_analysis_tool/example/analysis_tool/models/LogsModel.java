package logs_analysis_tool.example.analysis_tool.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name ="logs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LogsModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false)
    //@Length(min=10,max = 50,message = "")
    private String operationUrl;
    @Column(nullable = false)
    private String requestIdentifier;
    @Column(nullable = false)
    private boolean status;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String sessionId;
    @Column(nullable = false)
    private String ip;
    @Column(nullable = false)
    private String device;
    @Column(nullable = false)
    private String deviceTrace;
    @Column(nullable = false)
    private LocalDate localDate;
    @Column(nullable = false)
    private LocalTime localTime;
    @Column(nullable = false)
    private long operationExecutionTime_milliSec;
    @Column(nullable = false)
    private String currentPortalId;
    @Column(nullable = false)
    private String currentOrganizationId;
    @Column(nullable = false)
    private String currentSiteId;
    @Column(name = "requestHeaders", length = 5000,nullable = false)
    private String requestHeaders;
    @Column(name = "j_sessionId",length=5000, nullable = false)
    private String j_sessionId;
    @Column(name = "responseHeaders", length = 5000,nullable = false)
    private String responseHeaders;
}
