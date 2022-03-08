package com.daenerys.lndservice.service.implementation;

import com.daenerys.lndservice.dto.quicklinks.QuickLinksRequest;
import com.daenerys.lndservice.dto.quicklinks.QuickLinksResponse;
import com.daenerys.lndservice.model.lnd.QuickLinksEntity;
import com.daenerys.lndservice.repository.lnd.QuickLinksRepository;
import com.daenerys.lndservice.service.QuickLinksService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuickLinksServiceImpl implements QuickLinksService {

    private final QuickLinksRepository quickLinksRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<QuickLinksEntity> fetchQuickLinks() {
        return quickLinksRepository.findAll();
    }

    @Override
    public void createQuickLinks(QuickLinksEntity request) {
        if (quickLinksRepository.existsById(request.getHeader())) {
            modelMapper.map(request, quickLinksRepository.findById(request.getHeader()));
        }
        quickLinksRepository.save(request);
    }
}
