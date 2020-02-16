package com.abnamro.report.api;

import com.abnamro.report.util.CsvUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@RestController
public class TradingReportController {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(TradingReportController.class);

    @Autowired
    private TradingReportService tradingReportService;

    @GetMapping("/report/json")
    public List<Transaction> getReport() {
        return tradingReportService.getReport();
    }

    @GetMapping("/report/csv")
    public void downloadCsv(HttpServletResponse response) throws IOException {
        logger.info("Downloading the csv file...");
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactionReport.csv");
        CsvUtils.downloadCsv(response.getWriter(), tradingReportService.getReport()) ;
        logger.info("Downloading the csv completed.");
    }
}
