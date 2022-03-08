package com.daenerys.lndservice.service;

import com.daenerys.lndservice.dto.quicklinks.QuickLinksRequest;
import com.daenerys.lndservice.model.lnd.QuickLinksEntity;

import java.util.List;

public interface QuickLinksService {
    List<QuickLinksEntity> fetchQuickLinks();
    void createQuickLinks(QuickLinksEntity request);
}
