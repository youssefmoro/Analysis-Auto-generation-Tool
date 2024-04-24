package logs_analysis_tool.example.analysis_tool.logs_repo;

import logs_analysis_tool.example.analysis_tool.models.DateObjectCombination;
import logs_analysis_tool.example.analysis_tool.models.LogsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LogsRepo extends JpaRepository<LogsModel,Long> {

    @Query(value = "SELECT * FROM LOGS " +
            "WHERE EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "ORDER BY OPERATION_EXECUTION_TIME_MILLI_SEC " +
            "DESC LIMIT 20", nativeQuery = true)
    List<LogsModel> findMaximumExecutionData(@Param("month")int month);

    @Query(value = "SELECT COUNT(*) AS SUCCESS_ORDERS " +
            "FROM LOGS " +
            "WHERE OPERATION_URL " +
            "LIKE '%PlaceOrder%' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "AND STATUS LIKE 'TRUE' " +
            "GROUP BY LOCAL_DATE " +
            "ORDER BY LOCAL_DATE", nativeQuery = true)
    List<Long> successOrders(@Param("month") int month);


    @Query(value = "SELECT COUNT(*) AS FAILED_ORDERS " +
            "FROM LOGS " +
            "WHERE OPERATION_URL " +
            "LIKE '%PlaceOrder%' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "AND STATUS LIKE 'FALSE' " +
            "GROUP BY LOCAL_DATE " +
            "ORDER BY LOCAL_DATE", nativeQuery = true)
    List<Long> failedOrders(@Param("month") int month);

    @Query(value = "SELECT LOCAL_DATE ,COUNT(*) AS ORDERS " +
            "FROM LOGS " +
            "WHERE OPERATION_URL " +
            "LIKE '%/IMOREGATEWAY/IMORECORE/EINVEST/EINVEST/EINVEST/page/accessPageContentsByPageUrl/PlaceOrder%' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY LOCAL_DATE " +
            "ORDER BY LOCAL_DATE", nativeQuery = true)
    List<DateObjectCombination> totalOrders(@Param("month") int month);


    @Query(value = "SELECT COUNT(*) AS count " +
            "FROM LOGS " +
            "WHERE EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "AND STATUS LIKE 'FALSE' " +
            "GROUP BY LOCAL_DATE " +
            "ORDER BY LOCAL_DATE ",nativeQuery = true)
    List<Long> totalErrors(@Param("month") int month);


    @Query(value = "SELECT OPERATION_URL FROM LOGS " +
            "WHERE REQUEST_HEADERS LIKE '%error%' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY OPERATION_URL " +
            "ORDER BY COUNT(*) DESC LIMIT 10", nativeQuery = true)
    List<String> topApiErrors(@Param("month") int month);


    @Query(value = "SELECT OPERATION_URL FROM LOGS " +
            "WHERE EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY OPERATION_URL " +
            "ORDER BY COUNT(*) DESC LIMIT 10", nativeQuery = true)
    List<String> topApiUsed(@Param("month") int month);


    @Query(value = "SELECT COUNT(*) AS count " +
            "FROM LOGS " +
            "WHERE EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY HOUR(LOCAL_TIME) " +
            "ORDER BY HOUR(LOCAL_TIME)", nativeQuery = true)
    List<Long> requestsPerHour(@Param("month") int month);

    @Query(value = "SELECT COUNT(*) AS count " +
            " FROM LOGS " +
            " WHERE EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY DAY(LOCAL_DATE) " +
            "ORDER BY DAY(LOCAL_DATE)"
            , nativeQuery = true)
    List<Long> requestsPerMonth(@Param("month") int month);

    @Query(value = "SELECT COUNT(*) AS count " +
            "FROM LOGS " +
            "WHERE OPERATION_URL " +
            "LIKE '%/IMOREGATEWAY/IMORECORE/EINVEST/EINVEST/EINVEST/page/accessPageContentsByPageUrl/Login%' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY HOUR(LOCAL_TIME) " +
            "ORDER BY HOUR(LOCAL_TIME)", nativeQuery = true)
    List<Long> logInPerHour(@Param("month") int month);

    @Query(value = "SELECT COUNT(*) AS count " +
            " FROM LOGS " +
            " WHERE OPERATION_URL " +
            " LIKE '%/IMOREGATEWAY/IMORECORE/EINVEST/EINVEST/EINVEST/page/accessPageContentsByPageUrl/Login%' " +
            " AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            " GROUP BY DAY(LOCAL_DATE) " +
            " ORDER BY DAY(LOCAL_DATE)", nativeQuery = true)
    List<Long> logInPerMonth(@Param("month") int month);

    @Query(value = "SELECT COUNT(*) AS count " +
            "FROM LOGS " +
            "WHERE DEVICE " +
            "LIKE 'MOBILE' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month ", nativeQuery = true)
    Long mobileBrowserPerMonth(@Param("month") int month);


    @Query(value = "SELECT COUNT(*) AS count " +
            "FROM LOGS " +
            "WHERE DEVICE " +
            "LIKE 'WEB' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month ", nativeQuery = true)
    Long webBrowserPerMonth(@Param("month") int month);

    @Query(value = "SELECT TIMESTAMPDIFF(minute, MIN(LOCAL_TIME + LOCAL_DATE), MAX(LOCAL_TIME + LOCAL_DATE)) AS duration_time " +
            "FROM LOGS " +
            "WHERE SESSION_ID != 'access denied' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY SESSION_ID " +
            "ORDER BY duration_time DESC LIMIT 1;",nativeQuery = true)
    Long maxSessionDuration(@Param("month") int month);

    @Query(value = "SELECT " +
            "OPERATION_EXECUTION_TIME_MILLI_SEC " +
            "FROM LOGS " +
            "WHERE SESSION_ID = ( " +
            "SELECT  session_duration.SESSION_ID " +
            "FROM (" +
            "SELECT " +
            "SESSION_ID, " +
            "TIMESTAMPDIFF(SECOND, MIN(LOCAL_TIME + LOCAL_DATE), MAX(LOCAL_TIME + LOCAL_DATE)) AS duration_per_session " +
            "FROM LOGS " +
            " WHERE SESSION_ID != 'access denied' AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "GROUP BY SESSION_ID " +
            "ORDER BY duration_per_session ASC   LIMIT 1) AS session_duration);",nativeQuery = true)
    Long minSessionDuration(@Param("month") int month);

    @Query(value = "SELECT AVG(duration_per_session) AS average_duration " +
            "FROM ( " +
            "    SELECT SESSION_ID, TIMESTAMPDIFF(minute, MIN(LOCAL_TIME + LOCAL_DATE), MAX(LOCAL_TIME + LOCAL_DATE)) AS duration_per_session " +
            "    FROM LOGS " +
            "    WHERE SESSION_ID != 'access denied' " +
            "AND EXTRACT(MONTH FROM LOCAL_DATE) = :month " +
            "    GROUP BY SESSION_ID " +
            ") AS session_durations;", nativeQuery = true)
    Long averageSessionDuration(@Param("month") int month);
}
