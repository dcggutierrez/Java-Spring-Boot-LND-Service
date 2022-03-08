package com.daenerys.lndservice.controller;

import com.daenerys.lndservice.dto.quicklinks.QuickLinksRequest;
import com.daenerys.lndservice.dto.quicklinks.QuickLinksResponse;
import com.daenerys.lndservice.model.lnd.QuickLinksEntity;
import com.daenerys.lndservice.service.QuickLinksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static com.daenerys.lndservice.config.SwaggerConfig.QUICK_LINKS_TAG;

@RestController
@RequestMapping("/api/lnd/quicklinks")
@RequiredArgsConstructor
@Api(tags = QUICK_LINKS_TAG)
public class QuickLinksController {

    private final QuickLinksService quickLinksService;
    private final ModelMapper mapper;

    @ApiOperation(value = "Create or Edit quick links", notes =
            "Create or edit quick links and save to database", tags = {QUICK_LINKS_TAG})
    @PutMapping
    public void createQuickLinks(@Valid @RequestBody QuickLinksRequest request){
        quickLinksService.createQuickLinks(mapper.map(request, QuickLinksEntity.class));
    }

    @ApiOperation(value = "Get quick links details", notes =
            "Fetch quick links details from database", tags = {QUICK_LINKS_TAG})
    @GetMapping
    public List<QuickLinksResponse> getQuickLinks(){
        List<QuickLinksEntity> quickLinks = quickLinksService.fetchQuickLinks();
        return quickLinks.stream().map(link ->
                mapper.map(link, QuickLinksResponse.class)).collect(Collectors.toList());
    }
}
