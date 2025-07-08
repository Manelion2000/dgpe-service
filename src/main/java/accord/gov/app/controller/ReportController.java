package accord.gov.app.controller;

import accord.gov.app.service.BaReportService;
import accord.gov.app.utils.BaConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(BaConstants.URL.BASE_URL + "/reporting")
public class ReportController {

    private final BaReportService reportService;

    @GetMapping(value = BaConstants.URL.PRODUCT)
    public byte[] reportProduits() {
        return reportService.printProduits();
    }

}
